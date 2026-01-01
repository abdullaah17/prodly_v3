package com.prodly;

import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;
import java.util.List;

/**
 * JNI wrapper for MigrationDifficultyAnalyzer C++ class
 */
public class MigrationDifficultyAnalyzerJNI {
    private static boolean libraryLoaded = false;
    
    // In-memory storage for demo mode
    public static class TaskData {
        public String taskId;
        public String taskName;
        public int difficulty;
        public int estimatedDays;
        public List<String> dependencies;
        
        public TaskData(String id, String name, int diff, int days, String[] deps) {
            this.taskId = id;
            this.taskName = name;
            this.difficulty = diff;
            this.estimatedDays = days;
            this.dependencies = new ArrayList<>();
            if (deps != null) {
                for (String dep : deps) {
                    if (dep != null && !dep.trim().isEmpty()) {
                        this.dependencies.add(dep.trim());
                    }
                }
            }
        }
    }
    
    // Storage: vendorId -> list of tasks
    private static Map<String, List<TaskData>> demoTaskStorage = new HashMap<>();
    
    static {
        try {
            System.loadLibrary("prodlyjni");
            libraryLoaded = true;
        } catch (UnsatisfiedLinkError e) {
            libraryLoaded = false;
        }
    }

    private native long createNativeObject();
    private native void addTask(long nativePtr, String taskId, String taskName,
                               int difficulty, int estimatedDays, String[] dependencies);
    private native double calculateMigrationDifficulty(long nativePtr, String vendorId);
    private native String[] getOptimalMigrationSequence(long nativePtr, String vendorId);
    private native int getTotalMigrationDays(long nativePtr, String vendorId);
    private native void deleteNativeObject(long nativePtr);

    private long nativePtr;

    public MigrationDifficultyAnalyzerJNI() {
        if (libraryLoaded) {
            nativePtr = createNativeObject();
        } else {
            nativePtr = 0;
            // Initialize with sample data for demo mode
            initializeSampleData();
        }
    }
    
    private void initializeSampleData() {
        // Only add sample data if storage is empty (first instance)
        if (demoTaskStorage.isEmpty()) {
            String vendorId = "AWS-001";
            
            // Sample migration tasks for AWS migration
            addTaskForVendor(vendorId, "TASK-001", "Export Database", 7, 5, new String[0]);
            addTaskForVendor(vendorId, "TASK-002", "Export User Files", 5, 3, new String[0]);
            addTaskForVendor(vendorId, "TASK-003", "Setup Azure Infrastructure", 8, 10, new String[]{"TASK-001"});
            addTaskForVendor(vendorId, "TASK-004", "Migrate Data to Azure", 9, 7, new String[]{"TASK-002", "TASK-003"});
            addTaskForVendor(vendorId, "TASK-005", "Configure Networking", 6, 4, new String[]{"TASK-003"});
            addTaskForVendor(vendorId, "TASK-006", "Test Migration", 7, 3, new String[]{"TASK-004"});
            addTaskForVendor(vendorId, "TASK-007", "Switch DNS", 4, 1, new String[]{"TASK-006"});
            
            System.out.println("Demo mode: Loaded sample tasks for " + vendorId);
        }
    }

    // Store current vendor ID for tasks added without explicit vendor ID
    private static String currentVendorId = "default";
    
    public void addTask(String taskId, String taskName, int difficulty,
                       int estimatedDays, String[] dependencies) {
        addTaskForVendor(currentVendorId, taskId, taskName, difficulty, estimatedDays, dependencies);
    }
    
    public void setCurrentVendorId(String vendorId) {
        currentVendorId = vendorId != null && !vendorId.isEmpty() ? vendorId : "default";
    }
    
    // Helper method to add task with vendor ID
    public void addTaskForVendor(String vendorId, String taskId, String taskName, 
                                 int difficulty, int estimatedDays, String[] dependencies) {
        if (!libraryLoaded) {
            if (vendorId == null || vendorId.isEmpty()) {
                vendorId = "default";
            }
            if (!demoTaskStorage.containsKey(vendorId)) {
                demoTaskStorage.put(vendorId, new ArrayList<>());
            }
            TaskData task = new TaskData(taskId, taskName, difficulty, estimatedDays, dependencies);
            demoTaskStorage.get(vendorId).add(task);
            System.out.println("Demo mode: Task stored for " + vendorId + ": " + taskId);
        } else {
            addTask(nativePtr, taskId, taskName, difficulty, estimatedDays, dependencies);
        }
    }

    public double calculateMigrationDifficulty(String vendorId) {
        if (!libraryLoaded) {
            // Calculate demo difficulty based on stored tasks
            List<TaskData> tasks = demoTaskStorage.get(vendorId);
            if (tasks == null || tasks.isEmpty()) {
                return 50.0;
            }
            
            int totalDays = 0;
            int totalDifficulty = 0;
            for (TaskData task : tasks) {
                totalDays += task.estimatedDays;
                totalDifficulty += task.difficulty;
            }
            
            // Calculate difficulty score (0-100)
            double avgDifficulty = tasks.isEmpty() ? 0 : (double)totalDifficulty / tasks.size();
            double timeFactor = Math.min(40.0, (totalDays / 180.0) * 40.0);
            double taskCountFactor = Math.min(30.0, (tasks.size() / 20.0) * 30.0);
            double complexityFactor = Math.min(30.0, (avgDifficulty / 10.0) * 30.0);
            
            return Math.min(100.0, timeFactor + taskCountFactor + complexityFactor);
        }
        return calculateMigrationDifficulty(nativePtr, vendorId);
    }

    public String[] getOptimalMigrationSequence(String vendorId) {
        if (!libraryLoaded) {
            // Return sequence based on dependencies (topological sort simulation)
            List<TaskData> tasks = demoTaskStorage.get(vendorId);
            if (tasks == null || tasks.isEmpty()) {
                return new String[0];
            }
            
            // Simple topological sort: tasks without dependencies first
            List<String> sequence = new ArrayList<>();
            List<String> processed = new ArrayList<>();
            
            // Process tasks without dependencies first
            for (TaskData task : tasks) {
                if (task.dependencies.isEmpty()) {
                    sequence.add(task.taskId);
                    processed.add(task.taskId);
                }
            }
            
            // Process remaining tasks (simplified - real implementation would be more complex)
            for (TaskData task : tasks) {
                if (!processed.contains(task.taskId)) {
                    sequence.add(task.taskId);
                    processed.add(task.taskId);
                }
            }
            
            return sequence.toArray(new String[0]);
        }
        return getOptimalMigrationSequence(nativePtr, vendorId);
    }

    public int getTotalMigrationDays(String vendorId) {
        if (!libraryLoaded) {
            List<TaskData> tasks = demoTaskStorage.get(vendorId);
            if (tasks == null) {
                return 0;
            }
            
            int totalDays = 0;
            for (TaskData task : tasks) {
                totalDays += task.estimatedDays;
            }
            return totalDays;
        }
        return getTotalMigrationDays(nativePtr, vendorId);
    }
    
    // Get all tasks for a vendor (for UI display)
    public List<TaskData> getTasksForVendor(String vendorId) {
        if (!libraryLoaded) {
            return demoTaskStorage.getOrDefault(vendorId, new ArrayList<>());
        }
        return new ArrayList<>(); // Not implemented for native mode
    }

    @Override
    protected void finalize() throws Throwable {
        if (libraryLoaded && nativePtr != 0) {
            deleteNativeObject(nativePtr);
            nativePtr = 0;
        }
        super.finalize();
    }
}
