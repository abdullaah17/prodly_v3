#ifndef VENDOR_HASH_TABLE_H
#define VENDOR_HASH_TABLE_H

#include "../include/VendorLockInAnalyzer.h"
#include <unordered_map>

/**
 * Hash Table implementation for vendor data storage
 * Level-1 DSA: Hash Table with O(1) average case lookup
 */
class VendorHashTable {
public:
    VendorHashTable();
    ~VendorHashTable();

    void insert(const std::string& vendorId, VendorData* data);
    VendorData* get(const std::string& vendorId);
    bool contains(const std::string& vendorId);
    std::vector<std::pair<std::string, VendorData*>> getAllEntries();

private:
    std::unordered_map<std::string, std::unique_ptr<VendorData>> table;
};

#endif // VENDOR_HASH_TABLE_H

