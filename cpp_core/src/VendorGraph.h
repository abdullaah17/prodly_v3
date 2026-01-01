#ifndef VENDOR_GRAPH_H
#define VENDOR_GRAPH_H

#include "../include/Graph.h"
#include <string>
#include <vector>

/**
 * Graph wrapper for vendor dependency analysis
 * Level-2 DSA: Graph with DFS for dependency traversal
 */
class VendorGraph {
public:
    VendorGraph();
    ~VendorGraph();

    void addVendor(const std::string& vendorId);
    void addDependency(const std::string& from, const std::string& to);
    std::vector<std::string> getDependencies(const std::string& vendorId);

private:
    Graph* graph;
};

#endif // VENDOR_GRAPH_H

