# Prodly - Vendor Lock-In Analysis Platform

A professional SaaS platform for analyzing vendor lock-in risks, migration difficulty, and exit readiness. Built with C++ core logic and JavaFX GUI.

## Architecture

### System Overview
- **Unified System**: Single application with integrated modules
- **Split Codebases**: C++ for core logic, Java for GUI
- **Modules**: Three functional modules sharing the same backend logic & data

### Project Structure
```
prodly/
├── cpp_core/          # C++ Core (VS Code)
│   ├── include/       # Header files
│   ├── src/           # Implementation files
│   └── JNI/           # JNI interface layer
│
└── java_gui/          # Java GUI (NetBeans/IntelliJ)
    ├── src/main/java/com/prodly/
    └── src/main/resources/
```

## Modules

### Module 1: Vendor Lock-In Score Calculator
**Purpose**: Calculate vendor lock-in risk scores based on contract terms, data volume, and dependencies.

**C++ DSA Implementation**:
- **Level-1**: Hash Table (`VendorHashTable`) - O(1) vendor data lookup
- **Level-2**: Graph (`VendorGraph`) - Dependency analysis using DFS traversal

**Features**:
- Contract value analysis
- Contract duration assessment
- Data volume impact
- API dependency tracking
- Custom integration detection
- Switching cost calculation

### Module 2: Migration Difficulty Analyzer
**Purpose**: Analyze migration complexity and generate optimal task sequences.

**C++ DSA Implementation**:
- **Level-1**: Priority Queue - Task prioritization
- **Level-2**: Graph with BFS/DFS - Dependency traversal and topological sorting

**Features**:
- Task dependency management
- Optimal migration sequence generation
- Critical path analysis
- Total migration time estimation
- Difficulty scoring (0-100)

### Module 3: Exit Readiness Dashboard
**Purpose**: Analyze vendor exit readiness and optimal migration paths.

**C++ DSA Implementation**:
- **Level-1**: AVL Tree (`VendorAVLTree`) - Sorted vendor data by readiness score
- **Level-2**: Graph with Dijkstra's Algorithm - Optimal exit path calculation

**Features**:
- Exit readiness scoring (0-100)
- Vendor ranking by readiness
- Optimal exit path calculation
- Multi-factor analysis (lock-in, migration, technical, contract)

## Building the Project

### Prerequisites
- **C++**: C++17 compatible compiler (GCC, Clang, or MSVC)
- **Java**: JDK 11 or higher
- **JavaFX**: Included via Maven
- **CMake**: Version 3.15 or higher
- **Maven**: For Java build

### Building C++ Core

```bash
cd cpp_core
mkdir build
cd build
cmake ..
cmake --build .
```

On Windows with MSVC:
```cmd
cmake .. -G "Visual Studio 16 2019" -A x64
cmake --build . --config Release
```

The output will be:
- **Windows**: `prodlyjni.dll`
- **Linux**: `libprodlyjni.so`
- **macOS**: `libprodlyjni.dylib`

### Building Java GUI

```bash
cd java_gui
mvn clean compile
mvn javafx:run
```

Or build a JAR:
```bash
mvn clean package
java -jar target/prodly-gui-1.0.0.jar
```

### Running the Application

1. **Build the C++ library first** (see above)
2. **Copy the native library** to the Java library path or set `java.library.path`:
   ```bash
   # Linux/macOS
   export LD_LIBRARY_PATH=/path/to/prodly_v3/cpp_core/build:$LD_LIBRARY_PATH
   
   # Windows
   set PATH=%PATH%;C:\path\to\prodly_v3\cpp_core\build\Release
   ```
3. **Run the Java application**:
   ```bash
   cd java_gui
   mvn javafx:run
   ```

## Usage

### Module 1: Vendor Lock-In Score
1. Navigate to "Vendor Lock-In Score" tab
2. Fill in vendor details:
   - Vendor ID and Name
   - Contract Value ($)
   - Contract Duration (months)
   - Data Volume (GB)
   - API Dependencies count
   - Switching Cost ($)
   - Custom Integration checkbox
3. Click "Calculate Lock-In Score"
4. View results in the table with color-coded risk indicators

### Module 2: Migration Difficulty
1. Navigate to "Migration Difficulty" tab
2. Enter Vendor ID for the migration
3. Add migration tasks:
   - Task ID and Name
   - Difficulty (1-10)
   - Estimated Days
   - Dependencies (one per line)
4. Click "Calculate Migration Difficulty"
5. View optimal sequence and total estimated days

### Module 3: Exit Readiness
1. Navigate to "Exit Readiness" tab
2. Enter vendor metrics:
   - Vendor ID
   - Lock-In Score (0-100)
   - Migration Difficulty (0-100)
   - Data Export Capability (0-100)
   - Contract Flexibility (0-100)
   - Technical Complexity (0-100)
3. Click "Calculate Exit Readiness"
4. View ranked vendors sorted by readiness score

## Technical Details

### Data Structures & Algorithms

#### Level-1 DSAs
- **Hash Tables**: Vendor data storage and lookup
- **Priority Queues**: Task prioritization
- **AVL Trees**: Sorted vendor data management

#### Level-2 DSAs
- **Graphs**: Dependency analysis, task sequencing
- **BFS/DFS**: Graph traversal for dependencies
- **Dijkstra's Algorithm**: Optimal path calculation
- **Topological Sort**: Task ordering

### JNI Integration
- JNI layer handles communication between Java and C++
- Native methods are declared in JNI wrapper classes
- Library must be loaded before use: `System.loadLibrary("prodlyjni")`

### UI/UX Features
- Modern, professional design
- Responsive layout
- Color-coded risk indicators
- Interactive tables with progress bars
- Form validation and error handling
- Toast notifications (via Alert dialogs)
- Smooth transitions and hover effects

## Development Notes

### C++ Development (VS Code)
- Edit files in `cpp_core/`
- Use CMake for building
- Header files in `cpp_core/include/`
- Implementation in `cpp_core/src/`

### Java Development (NetBeans/IntelliJ)
- Edit files in `java_gui/src/main/java/com/prodly/`
- Use Maven for dependency management
- Resources in `java_gui/src/main/resources/`
- Main class: `com.prodly.ProdlyApplication`

### Module Integration
- All modules share the same data structures
- Modules are not separate mini-projects
- Backend logic is unified in C++
- Java GUI provides different views of the same data

## License

This project is built for educational/demonstration purposes.

## Contact

For questions or issues, please refer to the project documentation.

