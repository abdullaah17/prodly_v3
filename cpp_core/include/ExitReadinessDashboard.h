#ifndef EXIT_READINESS_DASHBOARD_H
#define EXIT_READINESS_DASHBOARD_H

#include <string>
#include <vector>
#include <memory>

// Forward declarations
class VendorAVLTree;
class ExitReadinessGraph;

/**
 * Module 3: Exit Readiness Dashboard
 * 
 * DSA Implementation:
 * - Level-1: AVL Tree - Sorted vendor data by readiness score
 * - Level-2: Graph with Dijkstra - Optimal exit path calculation
 */
class ExitReadinessDashboard {
public:
    ExitReadinessDashboard();
    ~ExitReadinessDashboard();

    // Add vendor with readiness metrics
    void addVendorMetrics(const std::string& vendorId,
                         double lockInScore,
                         double migrationDifficulty,
                         int dataExportCapability, // 0-100
                         int contractFlexibility,  // 0-100
                         int technicalComplexity); // 0-100

    // Calculate exit readiness score (0-100, higher = more ready to exit)
    double calculateExitReadiness(const std::string& vendorId);

    // Get optimal exit path (using Dijkstra's algorithm)
    std::vector<std::string> getOptimalExitPath(const std::string& startVendorId,
                                                const std::string& targetState = "exited");

    // Get all vendors sorted by readiness (using AVL tree)
    std::vector<std::pair<std::string, double>> getVendorsSortedByReadiness();

    // Get readiness breakdown
    struct ReadinessBreakdown {
        double overallScore;
        double lockInFactor;
        double migrationFactor;
        double technicalFactor;
        double contractFactor;
    };
    
    ReadinessBreakdown getReadinessBreakdown(const std::string& vendorId);

private:
    std::unique_ptr<VendorAVLTree> vendorTree;
    std::unique_ptr<ExitReadinessGraph> readinessGraph;
    
    double calculateReadinessFactors(const std::string& vendorId);
};

#endif // EXIT_READINESS_DASHBOARD_H

