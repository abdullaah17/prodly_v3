#include "VendorHashTable.h"

VendorHashTable::VendorHashTable() {
}

VendorHashTable::~VendorHashTable() {
}

void VendorHashTable::insert(const std::string& vendorId, VendorData* data) {
    if (data != nullptr) {
        table[vendorId] = std::unique_ptr<VendorData>(new VendorData(*data));
    }
}

VendorData* VendorHashTable::get(const std::string& vendorId) {
    if (table.find(vendorId) != table.end()) {
        return table[vendorId].get();
    }
    return nullptr;
}

bool VendorHashTable::contains(const std::string& vendorId) {
    return table.find(vendorId) != table.end();
}

std::vector<std::pair<std::string, VendorData*>> VendorHashTable::getAllEntries() {
    std::vector<std::pair<std::string, VendorData*>> result;
    for (auto& pair : table) {
        result.push_back(std::make_pair(pair.first, pair.second.get()));
    }
    return result; //
}

