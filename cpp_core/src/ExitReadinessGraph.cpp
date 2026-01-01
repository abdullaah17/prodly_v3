#include "ExitReadinessGraph.h"

ExitReadinessGraph::ExitReadinessGraph() {
    graph = new Graph(true); // Directed graph
}

ExitReadinessGraph::~ExitReadinessGraph() {
    delete graph;
}

void ExitReadinessGraph::addVendorState(const std::string& vendorId, 
                                       const std::vector<std::string>& nextStates,
                                       const std::vector<double>& transitionCosts) {
    graph->addVertex(vendorId);
    
    for (size_t i = 0; i < nextStates.size() && i < transitionCosts.size(); ++i) {
        graph->addEdge(vendorId, nextStates[i], transitionCosts[i]);
    }
}

std::vector<std::string> ExitReadinessGraph::getOptimalPath(const std::string& start, const std::string& end) {
    Graph::PathResult result = graph->dijkstra(start, end);
    return result.path;
}

