#include "../include/MigrationDifficultyAnalyzer.h"
#include "MigrationGraph.h"
#include <algorithm>
#include <queue>
#include <cmath>

MigrationDifficultyAnalyzer::MigrationDifficultyAnalyzer() {
    taskGraph = std::make_unique<MigrationGraph>();
}

MigrationDifficultyAnalyzer::~MigrationDifficultyAnalyzer() {
}

void MigrationDifficultyAnalyzer::addTask(const std::string& taskId, const std::string& taskName,
                                          int difficulty, int estimatedDays,
                                          const std::vector<std::string>& dependencies) {
    MigrationTask task;
    task.taskId = taskId;
    task.taskName = taskName;
    task.difficulty = difficulty;
    task.estimatedDays = estimatedDays;
    task.dependencies = dependencies;
    
    taskGraph->addTask(task);
}

double MigrationDifficultyAnalyzer::calculateMigrationDifficulty(const std::string& vendorId) {
    // Calculate difficulty based on tasks
    auto sequence = getOptimalMigrationSequence(vendorId);
    int totalDays = getTotalMigrationDays(vendorId);
    
    // Difficulty score: 0-100
    // Factors: total days, number of tasks, complexity
    double difficultyScore = 0.0;
    
    // Time factor (0-40 points)
    difficultyScore += std::min(40.0, (totalDays / 180.0) * 40.0);
    
    // Task count factor (0-30 points)
    difficultyScore += std::min(30.0, (sequence.size() / 20.0) * 30.0);
    
    // Complexity factor (0-30 points) - based on critical path length
    auto criticalPath = getCriticalPath(vendorId);
    difficultyScore += std::min(30.0, (criticalPath.size() / 15.0) * 30.0);
    
    return std::min(100.0, std::max(0.0, difficultyScore));
}

std::vector<std::string> MigrationDifficultyAnalyzer::getOptimalMigrationSequence(const std::string& vendorId) {
    // Use topological sort (implemented via BFS in graph)
    return taskGraph->getTopologicalOrder();
}

std::vector<std::string> MigrationDifficultyAnalyzer::getCriticalPath(const std::string& vendorId) {
    // Get longest dependency chain using graph traversal
    auto sequence = taskGraph->getTopologicalOrder();
    if (!sequence.empty()) {
        return taskGraph->getCriticalPath(sequence[0]);
    }
    return std::vector<std::string>();
}

int MigrationDifficultyAnalyzer::getTotalMigrationDays(const std::string& vendorId) {
    auto sequence = taskGraph->getTopologicalOrder();
    if (!sequence.empty()) {
        return taskGraph->getTotalDays(sequence[0]);
    }
    return 0;
}

std::vector<MigrationDifficultyAnalyzer::TaskPriority> MigrationDifficultyAnalyzer::prioritizeTasks(const std::string& vendorId) {
    // This would use a priority queue internally
    // For now, return empty vector as it's a helper method
    return std::vector<TaskPriority>();
}

int MigrationDifficultyAnalyzer::calculateTaskPriority(const MigrationTask& task) {
    // Higher difficulty and more dependencies = higher priority
    return task.difficulty * 10 + static_cast<int>(task.dependencies.size()) * 5;
}

