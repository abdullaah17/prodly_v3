#ifndef MIGRATION_DIFFICULTY_ANALYZER_H
#define MIGRATION_DIFFICULTY_ANALYZER_H

#include <string>
#include <vector>
#include <memory>
#include <queue>

// Forward declarations
class MigrationGraph;
struct MigrationTask;

/**
 * Module 2: Migration Difficulty Analyzer
 * 
 * DSA Implementation:
 * - Level-1: Priority Queue - Task prioritization
 * - Level-2: Graph with BFS/DFS - Dependency traversal
 */
class MigrationDifficultyAnalyzer {
public:
    MigrationDifficultyAnalyzer();
    ~MigrationDifficultyAnalyzer();

    // Add migration task
    void addTask(const std::string& taskId, const std::string& taskName,
                 int difficulty, int estimatedDays, 
                 const std::vector<std::string>& dependencies);

    // Calculate migration difficulty score
    double calculateMigrationDifficulty(const std::string& vendorId);

    // Get optimal migration sequence (using topological sort via BFS)
    std::vector<std::string> getOptimalMigrationSequence(const std::string& vendorId);

    // Get critical path (longest dependency chain using DFS)
    std::vector<std::string> getCriticalPath(const std::string& vendorId);

    // Get total migration time estimate
    int getTotalMigrationDays(const std::string& vendorId);

private:
    std::unique_ptr<MigrationGraph> taskGraph;
    
    // Priority queue for task prioritization
    struct TaskPriority {
        std::string taskId;
        int priority; // Lower = higher priority
        
        bool operator>(const TaskPriority& other) const {
            return priority > other.priority;
        }
    };
    
    std::vector<TaskPriority> prioritizeTasks(const std::string& vendorId);
    int calculateTaskPriority(const MigrationTask& task);
};

/**
 * Migration task structure
 */
struct MigrationTask {
    std::string taskId;
    std::string taskName;
    int difficulty; // 1-10
    int estimatedDays;
    std::vector<std::string> dependencies;
    
    MigrationTask() : difficulty(0), estimatedDays(0) {}
};

#endif // MIGRATION_DIFFICULTY_ANALYZER_H

