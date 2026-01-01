#include "../include/Graph.h"
#include <queue>
#include <algorithm>
#include <limits>
#include <sstream>

Graph::Graph(bool directed) : isDirected(directed) {
}

Graph::~Graph() {
}

void Graph::addVertex(const std::string& vertexId) {
    if (adjacencyList.find(vertexId) == adjacencyList.end()) {
        adjacencyList[vertexId] = std::vector<std::pair<std::string, double>>();
    }
}

void Graph::addEdge(const std::string& from, const std::string& to, double weight) {
    addVertex(from);
    addVertex(to);
    
    adjacencyList[from].push_back(std::make_pair(to, weight));
    
    if (!isDirected) {
        adjacencyList[to].push_back(std::make_pair(from, weight));
    }
}

std::vector<std::string> Graph::getNeighbors(const std::string& vertexId) {
    std::vector<std::string> neighbors;
    if (adjacencyList.find(vertexId) != adjacencyList.end()) {
        for (const auto& edge : adjacencyList[vertexId]) {
            neighbors.push_back(edge.first);
        }
    }
    return neighbors;
}

bool Graph::hasVertex(const std::string& vertexId) {
    return adjacencyList.find(vertexId) != adjacencyList.end();
}

std::vector<std::string> Graph::getAllVertices() {
    std::vector<std::string> vertices;
    for (const auto& pair : adjacencyList) {
        vertices.push_back(pair.first);
    }
    return vertices;
}

std::vector<std::string> Graph::BFS(const std::string& start) {
    std::vector<std::string> result;
    std::unordered_set<std::string> visited;
    std::queue<std::string> queue;
    
    if (!hasVertex(start)) return result;
    
    queue.push(start);
    visited.insert(start);
    
    while (!queue.empty()) {
        std::string current = queue.front();
        queue.pop();
        result.push_back(current);
        
        for (const auto& neighbor : getNeighbors(current)) {
            if (visited.find(neighbor) == visited.end()) {
                visited.insert(neighbor);
                queue.push(neighbor);
            }
        }
    }
    
    return result;
}

std::vector<std::string> Graph::DFS(const std::string& start) {
    std::vector<std::string> result;
    std::unordered_set<std::string> visited;
    
    if (!hasVertex(start)) return result;
    
    DFSHelper(start, visited, result);
    return result;
}

void Graph::DFSHelper(const std::string& vertex, std::unordered_set<std::string>& visited,
                      std::vector<std::string>& result) {
    visited.insert(vertex);
    result.push_back(vertex);
    
    for (const auto& neighbor : getNeighbors(vertex)) {
        if (visited.find(neighbor) == visited.end()) {
            DFSHelper(neighbor, visited, result);
        }
    }
}

Graph::PathResult Graph::dijkstra(const std::string& start, const std::string& end) {
    PathResult result;
    result.totalWeight = std::numeric_limits<double>::max();
    
    if (!hasVertex(start) || !hasVertex(end)) {
        return result;
    }
    
    std::unordered_map<std::string, double> distances;
    std::unordered_map<std::string, std::string> previous;
    std::unordered_set<std::string> unvisited;
    
    // Initialize distances
    for (const auto& vertex : adjacencyList) {
        distances[vertex.first] = std::numeric_limits<double>::max();
        unvisited.insert(vertex.first);
    }
    distances[start] = 0.0;
    
    // Dijkstra's algorithm
    while (!unvisited.empty()) {
        // Find unvisited vertex with minimum distance
        std::string current;
        double minDist = std::numeric_limits<double>::max();
        
        for (const auto& vertex : unvisited) {
            if (distances[vertex] < minDist) {
                minDist = distances[vertex];
                current = vertex;
            }
        }
        
        if (current.empty() || minDist == std::numeric_limits<double>::max()) {
            break;
        }
        
        unvisited.erase(current);
        
        if (current == end) {
            break;
        }
        
        // Update distances to neighbors
        for (const auto& edge : adjacencyList[current]) {
            std::string neighbor = edge.first;
            double weight = edge.second;
            
            if (unvisited.find(neighbor) != unvisited.end()) {
                double alt = distances[current] + weight;
                if (alt < distances[neighbor]) {
                    distances[neighbor] = alt;
                    previous[neighbor] = current;
                }
            }
        }
    }
    
    // Reconstruct path
    if (distances[end] != std::numeric_limits<double>::max()) {
        std::string current = end;
        while (!current.empty()) {
            result.path.insert(result.path.begin(), current);
            if (previous.find(current) != previous.end()) {
                current = previous[current];
            } else {
                break;
            }
        }
        result.totalWeight = distances[end];
    }
    
    return result;
}

std::vector<std::string> Graph::topologicalSort() {
    std::vector<std::string> result;
    std::unordered_set<std::string> visited;
    std::unordered_set<std::string> recStack;
    
    for (const auto& vertex : adjacencyList) {
        if (visited.find(vertex.first) == visited.end()) {
            topologicalSortHelper(vertex.first, visited, recStack, result);
        }
    }
    
    std::reverse(result.begin(), result.end());
    return result;
}

void Graph::topologicalSortHelper(const std::string& vertex,
                                  std::unordered_set<std::string>& visited,
                                  std::unordered_set<std::string>& recStack,
                                  std::vector<std::string>& result) {
    visited.insert(vertex);
    recStack.insert(vertex);
    
    for (const auto& neighbor : getNeighbors(vertex)) {
        if (visited.find(neighbor) == visited.end()) {
            topologicalSortHelper(neighbor, visited, recStack, result);
        }
    }
    
    recStack.erase(vertex);
    result.push_back(vertex);
}

