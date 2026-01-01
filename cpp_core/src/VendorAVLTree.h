#ifndef VENDOR_AVL_TREE_H
#define VENDOR_AVL_TREE_H

#include "../include/AVLTree.h"
#include <string>
#include <vector>

/**
 * AVL Tree wrapper for vendor readiness data
 * Level-1 DSA: AVL Tree for sorted vendor data
 */
class VendorAVLTree {
public:
    VendorAVLTree();
    ~VendorAVLTree();

    void insert(const std::string& vendorId, double readinessScore);
    double get(const std::string& vendorId);
    std::vector<std::pair<std::string, double>> getAllSortedByReadiness();

private:
    AVLTree* tree;
};

#endif // VENDOR_AVL_TREE_H

