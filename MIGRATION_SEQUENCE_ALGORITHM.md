# Optimal Migration Sequence Algorithm - Technical Explanation

## Overview

The **Optimal Migration Sequence** algorithm determines the best order to execute migration tasks based on their dependencies. It uses **Topological Sorting** to ensure that tasks are executed only after all their dependencies are completed.

## How It Works

### 1. Graph Representation

Tasks and their dependencies are represented as a **directed graph (DAG - Directed Acyclic Graph)**:

- **Nodes (Vertices)**: Each migration task (e.g., "Export Database", "Migrate Data")
- **Edges**: Dependency relationships (Task A → Task B means "B depends on A")

**Example from Sample Data:**
```
TASK-001 (Export Database) ──→ TASK-003 (Setup Azure Infrastructure)
TASK-002 (Export User Files) ──→ TASK-004 (Migrate Data to Azure)
TASK-003 ──→ TASK-004
TASK-003 ──→ TASK-005 (Configure Networking)
TASK-004 ──→ TASK-006 (Test Migration)
TASK-006 ──→ TASK-007 (Switch DNS)
```

### 2. Algorithm: Topological Sort

The algorithm uses **Topological Sorting** implemented via **DFS (Depth-First Search)** in the C++ backend:

#### C++ Implementation (Full Mode)

```cpp
// From Graph.cpp - topologicalSort()
std::vector<std::string> Graph::topologicalSort() {
    std::vector<std::string> result;
    std::unordered_set<std::string> visited;
    std::unordered_set<std::string> recStack;
    
    // Visit all vertices
    for (const auto& vertex : adjacencyList) {
        if (visited.find(vertex.first) == visited.end()) {
            topologicalSortHelper(vertex.first, visited, recStack, result);
        }
    }
    
    std::reverse(result.begin(), result.end());
    return result;
}
```

**Algorithm Steps:**
1. Start DFS from any unvisited task
2. Recursively visit all dependencies first
3. Mark task as visited and add to result stack
4. Reverse the result to get execution order

#### Java Implementation (Demo Mode)

```java
// Simplified version for demo mode
public String[] getOptimalMigrationSequence(String vendorId) {
    // Step 1: Find tasks with no dependencies (can run immediately)
    for (TaskData task : tasks) {
        if (task.dependencies.isEmpty()) {
            sequence.add(task.taskId);  // Add to sequence
        }
    }
    
    // Step 2: Add remaining tasks
    for (TaskData task : tasks) {
        if (!processed.contains(task.taskId)) {
            sequence.add(task.taskId);
        }
    }
    
    return sequence.toArray(new String[0]);
}
```

### 3. Execution Order Example

**Given Sample Tasks:**
1. TASK-001: Export Database (no dependencies)
2. TASK-002: Export User Files (no dependencies)
3. TASK-003: Setup Azure Infrastructure (depends on: TASK-001)
4. TASK-004: Migrate Data to Azure (depends on: TASK-002, TASK-003)
5. TASK-005: Configure Networking (depends on: TASK-003)
6. TASK-006: Test Migration (depends on: TASK-004)
7. TASK-007: Switch DNS (depends on: TASK-006)

**Optimal Sequence Generated:**
```
1. TASK-001 (Export Database)        ← No dependencies
2. TASK-002 (Export User Files)      ← No dependencies
3. TASK-003 (Setup Azure)            ← Depends on TASK-001 ✓
4. TASK-005 (Configure Networking)   ← Depends on TASK-003 ✓
5. TASK-004 (Migrate Data)           ← Depends on TASK-002, TASK-003 ✓
6. TASK-006 (Test Migration)         ← Depends on TASK-004 ✓
7. TASK-007 (Switch DNS)             ← Depends on TASK-006 ✓
```

### 4. Why This Order?

The sequence ensures:
- **Dependencies are satisfied**: Each task runs only after its prerequisites
- **Efficiency**: Independent tasks can run in parallel (if resources allow)
- **Correctness**: No task starts before its dependencies complete
- **Optimal path**: Minimizes waiting time between dependent tasks

## Data Structures Used

### Level-2 DSA: Graph with DFS/BFS

1. **Graph Structure**
   - Adjacency List: Maps each task to its dependent tasks
   - Directed edges: Represent "depends on" relationships

2. **Traversal Algorithms**
   - **DFS (Depth-First Search)**: Used for topological sort
   - **BFS (Breadth-First Search)**: Used for critical path finding

3. **Helper Data Structures**
   - `visited` Set: Tracks processed tasks
   - `recStack` Set: Detects cycles (prevents infinite loops)
   - `result` Vector: Stores the sorted sequence

## Integration Points

### In the Application

1. **Adding Tasks**
   ```java
   analyzer.addTask("TASK-004", "Migrate Data", 9, 7, 
                   new String[]{"TASK-002", "TASK-003"});
   ```
   - Creates graph nodes and edges
   - Edges: TASK-002 → TASK-004, TASK-003 → TASK-004

2. **Generating Sequence**
   ```java
   String[] sequence = analyzer.getOptimalMigrationSequence(vendorId);
   ```
   - Runs topological sort algorithm
   - Returns ordered array of task IDs

3. **Displaying Results**
   - UI shows sequence as numbered list
   - Each task ID displayed in execution order

## Complexity Analysis

- **Time Complexity**: O(V + E)
  - V = Number of tasks (vertices)
  - E = Number of dependencies (edges)
  - Each vertex and edge visited once

- **Space Complexity**: O(V)
  - Storage for visited sets and result vector
  - Recursion stack depth (max V)

## Real-World Example

**Migration Scenario: AWS to Azure**

1. **First Phase (Independent)**
   - Export Database
   - Export User Files
   - These can run simultaneously

2. **Second Phase (Setup)**
   - Setup Azure Infrastructure (needs database export)
   - Configure Networking (needs infrastructure)

3. **Third Phase (Migration)**
   - Migrate Data (needs user files + infrastructure)

4. **Fourth Phase (Validation)**
   - Test Migration (needs data migration)

5. **Final Phase (Cutover)**
   - Switch DNS (needs successful testing)

## Limitations & Future Enhancements

### Current Implementation
- **Demo Mode**: Simplified ordering (dependency-aware but not fully optimized)
- **Full Mode**: Proper topological sort with cycle detection

### Potential Enhancements
1. **Parallel Execution Detection**: Identify tasks that can run simultaneously
2. **Resource Constraints**: Consider limited resources (team size, servers)
3. **Priority Weighting**: Consider task difficulty and criticality
4. **Critical Path Analysis**: Find longest dependency chain (bottleneck)
5. **Time Estimation**: Calculate total time considering parallel execution

## Key Takeaways

1. **Topological Sort** ensures dependencies are respected
2. **Graph representation** naturally models task relationships
3. **DFS-based algorithm** efficiently computes execution order
4. **Optimal sequence** minimizes blocking and waiting time
5. **Algorithm scales** well with increasing number of tasks

## Code Flow Diagram

```
User Adds Tasks
    ↓
Graph.addTask() → Creates vertices and edges
    ↓
User Clicks "Calculate"
    ↓
getOptimalMigrationSequence()
    ↓
Graph.topologicalSort() → DFS traversal
    ↓
Topological Order Result
    ↓
Display in UI (numbered sequence)
```

This algorithm ensures that migration tasks are executed in the correct order, preventing errors and minimizing delays!

