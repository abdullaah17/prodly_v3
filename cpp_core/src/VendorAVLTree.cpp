#include "VendorAVLTree.h"

VendorAVLTree::VendorAVLTree() {
    tree = new AVLTree();
}

VendorAVLTree::~VendorAVLTree() {
    delete tree;
}

void VendorAVLTree::insert(const std::string& vendorId, double readinessScore) {
    tree->insert(vendorId, readinessScore);
}

double VendorAVLTree::get(const std::string& vendorId) {
    return tree->get(vendorId);
}

std::vector<std::pair<std::string, double>> VendorAVLTree::getAllSortedByReadiness() {
    // Return in descending order (highest readiness first)
    return tree->getAllSortedReverse();
}

