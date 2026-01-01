# Sample Data Integration & JNI Testing Guide

## Overview
All three modules of the Prodly Vendor Lock-In Analysis Platform now include sample data for demonstration and testing purposes. This allows the application to run in demo mode (without the C++ native library) while still showcasing full functionality.

## Changes Made

### 1. Module 1: Vendor Lock-In Score Calculator

**File**: `java_gui/src/main/java/com/prodly/VendorLockInAnalyzerJNI.java`

- **Sample Data Added**:
  - AWS-001: Amazon Web Services (High lock-in: $500k contract, 36 months, 15TB data, 25 APIs)
  - SF-001: Salesforce CRM (Medium lock-in: $250k contract, 24 months, 5TB data, 12 APIs)
  - O365-001: Microsoft Office 365 (Low lock-in: $50k contract, 12 months, 2TB data, 5 APIs)
  - ORC-001: Oracle Database Enterprise (Very high lock-in: $800k contract, 60 months, 50TB data, 40 APIs)

- **Features**:
  - In-memory storage using `HashMap<String, VendorData>`
  - Automatic sample data initialization on first instance
  - Demo score calculation based on vendor parameters
  - Full CRUD operations supported in demo mode

**File**: `java_gui/src/main/java/com/prodly/VendorLockInScreen.java`

- Sample data automatically loads when screen initializes via `refreshResults()` call

### 2. Module 2: Migration Difficulty Analyzer

**File**: `java_gui/src/main/java/com/prodly/MigrationDifficultyAnalyzerJNI.java`

- **Sample Data Added**:
  - Vendor ID: AWS-001
  - 7 migration tasks with dependencies:
    1. Export Database (Difficulty: 7, Days: 5)
    2. Export User Files (Difficulty: 5, Days: 3)
    3. Setup Azure Infrastructure (Difficulty: 8, Days: 10, Depends on: TASK-001)
    4. Migrate Data to Azure (Difficulty: 9, Days: 7, Depends on: TASK-002, TASK-003)
    5. Configure Networking (Difficulty: 6, Days: 4, Depends on: TASK-003)
    6. Test Migration (Difficulty: 7, Days: 3, Depends on: TASK-004)
    7. Switch DNS (Difficulty: 4, Days: 1, Depends on: TASK-006)

- **Features**:
  - In-memory storage using `Map<String, List<TaskData>>`
  - Vendor-specific task storage
  - Task dependency tracking
  - Demo difficulty calculation (based on task count, total days, average difficulty)
  - Optimal sequence generation (simplified topological sort)

**File**: `java_gui/src/main/java/com/prodly/MigrationDifficultyScreen.java`

- Vendor ID field pre-populated with "AWS-001"
- Sample tasks automatically load when screen initializes
- Tasks display in table on load
- Users can add new tasks for any vendor ID
- Calculate button triggers sequence generation and difficulty calculation

### 3. Module 3: Exit Readiness Dashboard

**File**: `java_gui/src/main/java/com/prodly/ExitReadinessDashboardJNI.java`

- **Sample Data Added** (aligned with Module 1 vendors):
  - AWS-001: Lock-in: 85, Migration: 75, Data Export: 30, Contract Flex: 20, Tech Complexity: 80
  - SF-001: Lock-in: 50, Migration: 55, Data Export: 60, Contract Flex: 50, Tech Complexity: 60
  - O365-001: Lock-in: 25, Migration: 30, Data Export: 90, Contract Flex: 85, Tech Complexity: 30
  - ORC-001: Lock-in: 95, Migration: 90, Data Export: 20, Contract Flex: 10, Tech Complexity: 95

- **Features**:
  - In-memory storage using `Map<String, VendorReadinessData>`
  - Exit readiness score calculation (weighted formula considering all factors)
  - Automatic sorting by readiness score (highest first)
  - Demo mode compatible

**File**: `java_gui/src/main/java/com/prodly/ExitReadinessScreen.java`

- Sample data automatically loads when screen initializes via `refreshTable()` call
- Vendors displayed sorted by exit readiness score

## JNI Integration Testing

### Demo Mode (Current Setup)
The application runs in **demo mode** when the C++ native library is not available:
- All data stored in Java memory (HashMaps, Lists)
- Calculations performed using Java algorithms
- Full functionality available for testing
- No native library required

### Full Mode (When C++ Library is Built)
When the native library (`prodlyjni.dll`) is built and available:
- Set `java.library.path` to point to the library location
- Native methods will be called instead of demo implementations
- Data stored in C++ backend (Hash Tables, AVL Trees, Graphs)
- Full DSA implementations active

### Testing Checklist

#### Module 1: Vendor Lock-In Score Calculator
- [x] Sample vendors load on screen initialization
- [x] All 4 sample vendors display in results table
- [x] Vendor names correctly displayed
- [x] Lock-in scores calculated and shown
- [x] Can add new vendors via form
- [x] New vendors appear in table immediately
- [x] Refresh button updates table

#### Module 2: Migration Difficulty Analyzer
- [x] Sample tasks load for AWS-001 vendor
- [x] Vendor ID field pre-populated with "AWS-001"
- [x] 7 sample tasks display in table
- [x] Can calculate migration difficulty for AWS-001
- [x] Optimal sequence generated and displayed
- [x] Total migration days calculated
- [x] Can add new tasks for any vendor
- [x] Tasks persist in memory

#### Module 3: Exit Readiness Dashboard
- [x] Sample vendors load on screen initialization
- [x] All 4 vendors display sorted by readiness
- [x] Readiness scores calculated correctly
- [x] Visual bars display scores
- [x] Can add new vendor metrics
- [x] Table refreshes after adding vendor

## Running the Application

### Quick Start (Demo Mode)
```powershell
cd java_gui
mvn clean compile javafx:run
```

### Full Mode (With Native Library)
1. Build C++ library first (requires CMake):
```powershell
cd cpp_core
mkdir build
cd build
cmake .. -G "Visual Studio 16 2019" -A x64
cmake --build . --config Release
```

2. Run Java application with library path:
```powershell
cd java_gui
mvn javafx:run -Djava.library.path=../cpp_core/build/Release
```

## Architecture Notes

### Data Persistence
- **Demo Mode**: All data stored in static HashMaps/Lists (session-only, lost on restart)
- **Full Mode**: Data stored in C++ backend structures (persistent during application lifetime)

### Cross-Module Integration
- Module 1 and Module 3 share vendor IDs (AWS-001, SF-001, O365-001, ORC-001)
- Module 2 uses vendor IDs for task grouping
- All modules can work independently or together

### JNI Error Handling
- Graceful degradation to demo mode if library not found
- Console warnings indicate demo mode operation
- No exceptions thrown when library unavailable
- Full functionality maintained in demo mode

## Next Steps

1. **Build C++ Library**: Install CMake and build the native library for full functionality
2. **Test Native Integration**: Verify JNI calls work correctly with C++ backend
3. **Add Persistence**: Consider adding file-based persistence for demo mode data
4. **Performance Testing**: Compare demo mode vs. native mode performance
5. **Data Export**: Implement export functionality for all modules

## Troubleshooting

### Sample Data Not Showing
- Check console for initialization messages
- Verify `initializeSampleData()` is called in constructor
- Ensure static storage maps are initialized

### Tasks Not Loading
- Verify vendor ID matches sample data ("AWS-001")
- Check that `getTasksForVendor()` returns data
- Ensure UI is fully initialized before loading (Platform.runLater)

### Scores Not Calculating
- Verify demo mode is active (check console warnings)
- Check that sample data was initialized
- Ensure vendor IDs match between modules

