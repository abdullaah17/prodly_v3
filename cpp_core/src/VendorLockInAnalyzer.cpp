#include "../include/VendorLockInAnalyzer.h"
#include "VendorHashTable.h"
#include "VendorGraph.h"
#include <algorithm>
#include <cmath>

VendorLockInAnalyzer::VendorLockInAnalyzer() {
    vendorTable = std::make_unique<VendorHashTable>();
    dependencyGraph = std::make_unique<VendorGraph>();
}

VendorLockInAnalyzer::~VendorLockInAnalyzer() {
}

void VendorLockInAnalyzer::addVendor(const std::string& vendorId, const std::string& vendorName,
                                     double contractValue, int contractMonths,
                                     double dataVolumeGB, int apiDependencies,
                                     bool hasCustomIntegration, double switchingCost) {
    VendorData* data = new VendorData();
    data->vendorId = vendorId;
    data->vendorName = vendorName;
    data->contractValue = contractValue;
    data->contractMonths = contractMonths;
    data->dataVolumeGB = dataVolumeGB;
    data->apiDependencies = apiDependencies;
    data->hasCustomIntegration = hasCustomIntegration;
    data->switchingCost = switchingCost;
    
    vendorTable->insert(vendorId, data);
    dependencyGraph->addVendor(vendorId);
    
    // Calculate and store lock-in score
    data->lockInScore = calculateLockInScore(vendorId);
}

double VendorLockInAnalyzer::calculateLockInScore(const std::string& vendorId) {
    VendorData* vendor = vendorTable->get(vendorId);
    if (vendor == nullptr) return 0.0;
    
    return calculateRiskFactors(*vendor);
}

double VendorLockInAnalyzer::calculateRiskFactors(const VendorData& vendor) {
    double score = 0.0;
    
    // Contract value factor (0-25 points)
    // Higher contract value = higher lock-in
    double contractFactor = std::min(25.0, (vendor.contractValue / 1000000.0) * 5.0);
    
    // Contract duration factor (0-20 points)
    // Longer contracts = higher lock-in
    double durationFactor = std::min(20.0, (vendor.contractMonths / 36.0) * 20.0);
    
    // Data volume factor (0-15 points)
    // More data = harder to migrate
    double dataFactor = std::min(15.0, (vendor.dataVolumeGB / 1000.0) * 15.0);
    
    // API dependencies factor (0-15 points)
    // More dependencies = tighter coupling
    double apiFactor = std::min(15.0, (vendor.apiDependencies / 10.0) * 15.0);
    
    // Custom integration factor (0-10 points)
    // Custom integrations increase lock-in significantly
    double integrationFactor = vendor.hasCustomIntegration ? 10.0 : 0.0;
    
    // Switching cost factor (0-15 points)
    // Direct switching cost impact
    double switchingFactor = std::min(15.0, (vendor.switchingCost / vendor.contractValue) * 15.0);
    
    score = contractFactor + durationFactor + dataFactor + apiFactor + integrationFactor + switchingFactor;
    
    // Normalize to 0-100 range
    return std::min(100.0, std::max(0.0, score));
}

std::vector<std::string> VendorLockInAnalyzer::getVendorDependencies(const std::string& vendorId) {
    return dependencyGraph->getDependencies(vendorId);
}

std::vector<std::pair<std::string, double>> VendorLockInAnalyzer::getAllScores() {
    std::vector<std::pair<std::string, double>> result;
    auto entries = vendorTable->getAllEntries();
    
    for (const auto& entry : entries) {
        if (entry.second != nullptr) {
            result.push_back(std::make_pair(entry.first, entry.second->lockInScore));
        }
    }
    
    return result;
}

VendorData* VendorLockInAnalyzer::getVendorData(const std::string& vendorId) {
    return vendorTable->get(vendorId);
}

