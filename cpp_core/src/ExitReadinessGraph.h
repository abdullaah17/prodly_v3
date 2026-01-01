#ifndef EXIT_READINESS_GRAPH_H
#define EXIT_READINESS_GRAPH_H

#include "../include/Graph.h"
#include <string>
#include <vector>
#include <unordered_map>

/**
 * Graph wrapper for exit path calculation
 * Level-2 DSA: Graph with Dijkstra's algorithm for optimal exit path
 */
class ExitReadinessGraph {
public:
    ExitReadinessGraph();
    ~ExitReadinessGraph();

    void addVendorState(const std::string& vendorId, const std::vector<std::string>& nextStates, 
                       const std::vector<double>& transitionCosts);
    std::vector<std::string> getOptimalPath(const std::string& start, const std::string& end);

private:
    Graph* graph;
};

#endif // EXIT_READINESS_GRAPH_H

