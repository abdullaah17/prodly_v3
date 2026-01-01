#ifndef VENDOR_LOCKIN_ANALYZER_H
#define VENDOR_LOCKIN_ANALYZER_H

#include <string>
#include <unordered_map>
#include <vector>
#include <memory>

// Forward declarations
class VendorGraph;
class VendorHashTable;

/**
 * Vendor data structure
 */
struct VendorData {
    std::string vendorId;
    std::string vendorName;
    double contractValue;
    int contractMonths;
    double dataVolumeGB;
    int apiDependencies;
    bool hasCustomIntegration;
    double switchingCost;
    double lockInScore;

    VendorData() : contractValue(0), contractMonths(0), dataVolumeGB(0),
                   apiDependencies(0), hasCustomIntegration(false),
                   switchingCost(0), lockInScore(0) {}
};

/**
 * Module 1: Vendor Lock-In Score Calculator
 * 
 * DSA Implementation:
 * - Level-1: Hash Table (VendorHashTable) - O(1) vendor lookup
 * - Level-2: Graph (VendorGraph) - Dependency analysis with DFS
 */
class VendorLockInAnalyzer {
public:
    VendorLockInAnalyzer();
    ~VendorLockInAnalyzer();

    // Add vendor data
    void addVendor(const std::string& vendorId, const std::string& vendorName,
                   double contractValue, int contractMonths, 
                   double dataVolumeGB, int apiDependencies,
                   bool hasCustomIntegration, double switchingCost);

    // Calculate lock-in score (0-100, higher = more locked in)
    double calculateLockInScore(const std::string& vendorId);

    // Get vendor dependencies (using graph traversal)
    std::vector<std::string> getVendorDependencies(const std::string& vendorId);

    // Get all vendors with scores
    std::vector<std::pair<std::string, double>> getAllScores();

    // Get vendor details
    VendorData* getVendorData(const std::string& vendorId);

private:
    std::unique_ptr<VendorHashTable> vendorTable;
    std::unique_ptr<VendorGraph> dependencyGraph;
    
    double calculateRiskFactors(const VendorData& vendor);
};

#endif // VENDOR_LOCKIN_ANALYZER_H

