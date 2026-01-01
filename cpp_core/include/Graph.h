#ifndef GRAPH_H
#define GRAPH_H

#include <string>
#include <vector>
#include <unordered_map>
#include <unordered_set>

/**
 * Generic Graph implementation for various modules
 * Supports both directed and undirected graphs
 */
class Graph {
public:
    Graph(bool directed = true);
    ~Graph();

    // Add vertex
    void addVertex(const std::string& vertexId);

    // Add edge
    void addEdge(const std::string& from, const std::string& to, double weight = 1.0);

    // Get neighbors
    std::vector<std::string> getNeighbors(const std::string& vertexId);

    // Check if vertex exists
    bool hasVertex(const std::string& vertexId);

    // Get all vertices
    std::vector<std::string> getAllVertices();

    // BFS traversal
    std::vector<std::string> BFS(const std::string& start);

    // DFS traversal
    std::vector<std::string> DFS(const std::string& start);

    // Dijkstra's algorithm - shortest path
    struct PathResult {
        std::vector<std::string> path;
        double totalWeight;
    };
    
    PathResult dijkstra(const std::string& start, const std::string& end);

    // Topological sort (for DAGs)
    std::vector<std::string> topologicalSort();

private:
    bool isDirected;
    std::unordered_map<std::string, std::vector<std::pair<std::string, double>>> adjacencyList;
    
    void DFSHelper(const std::string& vertex, std::unordered_set<std::string>& visited,
                   std::vector<std::string>& result);
    void topologicalSortHelper(const std::string& vertex,
                               std::unordered_set<std::string>& visited,
                               std::unordered_set<std::string>& recStack,
                               std::vector<std::string>& result);
};

#endif // GRAPH_H

