#include "VendorGraph.h"

VendorGraph::VendorGraph() {
    graph = new Graph(true); // Directed graph
}

VendorGraph::~VendorGraph() {
    delete graph;
}

void VendorGraph::addVendor(const std::string& vendorId) {
    graph->addVertex(vendorId);
}

void VendorGraph::addDependency(const std::string& from, const std::string& to) {
    graph->addEdge(from, to);
}

std::vector<std::string> VendorGraph::getDependencies(const std::string& vendorId) {
    return graph->DFS(vendorId); // Use DFS to find all dependencies
}

