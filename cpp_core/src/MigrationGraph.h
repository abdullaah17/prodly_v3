#ifndef MIGRATION_GRAPH_H
#define MIGRATION_GRAPH_H

#include "../include/Graph.h"
#include "../include/MigrationDifficultyAnalyzer.h"
#include <string>
#include <vector>
#include <unordered_map>

/**
 * Graph wrapper for migration task dependencies
 * Level-2 DSA: Graph with BFS for task sequencing
 */
class MigrationGraph {
public:
    MigrationGraph();
    ~MigrationGraph();

    void addTask(const MigrationTask& task);
    std::vector<std::string> getTopologicalOrder(); // BFS-based topological sort
    std::vector<std::string> getCriticalPath(const std::string& startTask);
    int getTotalDays(const std::string& startTask);

private:
    Graph* graph;
    std::unordered_map<std::string, MigrationTask> tasks;
};

#endif // MIGRATION_GRAPH_H

