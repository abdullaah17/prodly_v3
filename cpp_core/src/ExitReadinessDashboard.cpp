#include "../include/ExitReadinessDashboard.h"
#include "VendorAVLTree.h"
#include "ExitReadinessGraph.h"
#include <unordered_map>
#include <algorithm>
#include <cmath>

struct VendorReadinessMetrics {
    std::string vendorId;
    double lockInScore;
    double migrationDifficulty;
    int dataExportCapability;
    int contractFlexibility;
    int technicalComplexity;
    double exitReadiness;
};

// Private implementation - store metrics separately since header uses forward declarations
static std::unordered_map<std::string, VendorReadinessMetrics> g_vendorMetrics;

ExitReadinessDashboard::ExitReadinessDashboard() {
    vendorTree = std::make_unique<VendorAVLTree>();
    readinessGraph = std::make_unique<ExitReadinessGraph>();
}

ExitReadinessDashboard::~ExitReadinessDashboard() {
}

void ExitReadinessDashboard::addVendorMetrics(const std::string& vendorId,
                                              double lockInScore,
                                              double migrationDifficulty,
                                              int dataExportCapability,
                                              int contractFlexibility,
                                              int technicalComplexity) {
    VendorReadinessMetrics metrics;
    metrics.vendorId = vendorId;
    metrics.lockInScore = lockInScore;
    metrics.migrationDifficulty = migrationDifficulty;
    metrics.dataExportCapability = dataExportCapability;
    metrics.contractFlexibility = contractFlexibility;
    metrics.technicalComplexity = technicalComplexity;
    
    // Store metrics first
    g_vendorMetrics[vendorId] = metrics;
    
    // Calculate exit readiness score
    metrics.exitReadiness = calculateReadinessFactors(vendorId);
    g_vendorMetrics[vendorId].exitReadiness = metrics.exitReadiness;
    
    vendorTree->insert(vendorId, metrics.exitReadiness);
    
    // Add to graph for path calculation
    std::vector<std::string> states = {"planning", "preparation", "migration", "exited"};
    std::vector<double> costs = {
        100.0 - metrics.exitReadiness,  // Cost to move from current to planning
        metrics.migrationDifficulty,     // Cost to move from planning to preparation
        metrics.migrationDifficulty * 0.7, // Cost to move from preparation to migration
        metrics.lockInScore * 0.5        // Cost to move from migration to exited
    };
    
    // For simplicity, create edges from vendor to each state
    for (size_t i = 0; i < states.size() && i < costs.size(); ++i) {
        std::vector<std::string> nextStates = {states[i]};
        std::vector<double> nextCosts = {costs[i]};
        readinessGraph->addVendorState(vendorId, nextStates, nextCosts);
    }
}

double ExitReadinessDashboard::calculateExitReadiness(const std::string& vendorId) {
    if (g_vendorMetrics.find(vendorId) == g_vendorMetrics.end()) {
        return 0.0;
    }
    
    return calculateReadinessFactors(vendorId);
}

double ExitReadinessDashboard::calculateReadinessFactors(const std::string& vendorId) {
    if (g_vendorMetrics.find(vendorId) == g_vendorMetrics.end()) {
        return 0.0;
    }
    
    const VendorReadinessMetrics& metrics = g_vendorMetrics[vendorId];
    
    double readiness = 0.0;
    
    // Lock-in factor (inverse: lower lock-in = higher readiness) (0-30 points)
    double lockInFactor = (100.0 - metrics.lockInScore) * 0.3;
    
    // Migration difficulty factor (inverse: easier = higher readiness) (0-25 points)
    double migrationFactor = (100.0 - metrics.migrationDifficulty) * 0.25;
    
    // Data export capability (0-20 points)
    double dataFactor = metrics.dataExportCapability * 0.2;
    
    // Contract flexibility (0-15 points)
    double contractFactor = metrics.contractFlexibility * 0.15;
    
    // Technical complexity (inverse: simpler = higher readiness) (0-10 points)
    double techFactor = (100.0 - metrics.technicalComplexity) * 0.1;
    
    readiness = lockInFactor + migrationFactor + dataFactor + contractFactor + techFactor;
    
    return std::min(100.0, std::max(0.0, readiness));
}

std::vector<std::string> ExitReadinessDashboard::getOptimalExitPath(const std::string& startVendorId,
                                                                     const std::string& targetState) {
    return readinessGraph->getOptimalPath(startVendorId, targetState);
}

std::vector<std::pair<std::string, double>> ExitReadinessDashboard::getVendorsSortedByReadiness() {
    return vendorTree->getAllSortedByReadiness();
}

ExitReadinessDashboard::ReadinessBreakdown ExitReadinessDashboard::getReadinessBreakdown(const std::string& vendorId) {
    ReadinessBreakdown breakdown;
    
    if (g_vendorMetrics.find(vendorId) == g_vendorMetrics.end()) {
        return breakdown;
    }
    
    const VendorReadinessMetrics& metrics = g_vendorMetrics[vendorId];
    
    breakdown.overallScore = metrics.exitReadiness;
    breakdown.lockInFactor = (100.0 - metrics.lockInScore) * 0.3;
    breakdown.migrationFactor = (100.0 - metrics.migrationDifficulty) * 0.25;
    breakdown.technicalFactor = (100.0 - metrics.technicalComplexity) * 0.1;
    breakdown.contractFactor = metrics.contractFlexibility * 0.15;
    
    return breakdown;
}

// Fix: Need to add the pImpl member to the header
// Actually, let me check the header again and fix it properly

