#include "MigrationGraph.h"
#include <algorithm>

MigrationGraph::MigrationGraph() {
    graph = new Graph(true); // Directed graph for task dependencies
}

MigrationGraph::~MigrationGraph() {
    delete graph;
}

void MigrationGraph::addTask(const MigrationTask& task) {
    tasks[task.taskId] = task;
    graph->addVertex(task.taskId);
    
    for (const auto& dep : task.dependencies) {
        graph->addEdge(dep, task.taskId); // Dependency edge
    }
}

std::vector<std::string> MigrationGraph::getTopologicalOrder() {
    return graph->topologicalSort(); // Returns tasks in execution order
}

std::vector<std::string> MigrationGraph::getCriticalPath(const std::string& startTask) {
    // Use BFS to find longest path (critical path)
    std::vector<std::string> bfsOrder = graph->BFS(startTask);
    
    // Filter to get dependency chain
    std::vector<std::string> criticalPath;
    for (const auto& taskId : bfsOrder) {
        if (tasks.find(taskId) != tasks.end()) {
            criticalPath.push_back(taskId);
        }
    }
    
    return criticalPath;
}

int MigrationGraph::getTotalDays(const std::string& startTask) {
    std::vector<std::string> sequence = getTopologicalOrder();
    int totalDays = 0;
    
    for (const auto& taskId : sequence) {
        if (tasks.find(taskId) != tasks.end()) {
            totalDays += tasks[taskId].estimatedDays;
        }
    }
    
    return totalDays;
}

