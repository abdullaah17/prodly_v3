# Build Instructions

## Quick Start

### Windows

1. **Build C++ Core**:
```cmd
cd cpp_core
mkdir build
cd build
cmake .. -G "Visual Studio 16 2019" -A x64
cmake --build . --config Release
```

2. **Set Library Path**:
```cmd
set PATH=%PATH%;%CD%\Release
```

3. **Build and Run Java GUI**:
```cmd
cd ..\..\java_gui
mvn clean compile javafx:run
```

### Linux/macOS

1. **Build C++ Core**:
```bash
cd cpp_core
mkdir build
cd build
cmake ..
make -j4
```

2. **Set Library Path**:
```bash
export LD_LIBRARY_PATH=$PWD:$LD_LIBRARY_PATH
```

3. **Build and Run Java GUI**:
```bash
cd ../../java_gui
mvn clean compile javafx:run
```

## Detailed Instructions

### Prerequisites

- **C++ Compiler**: GCC 7+, Clang 8+, or MSVC 2019+
- **CMake**: 3.15+
- **Java JDK**: 11 or higher
- **Maven**: 3.6+
- **JavaFX**: Will be downloaded by Maven

### C++ Build

The C++ core uses CMake for building. The native library must be built before running the Java application.

**Windows (MSVC)**:
```cmd
cd cpp_core
mkdir build
cd build
cmake .. -G "Visual Studio 16 2019" -A x64
cmake --build . --config Release
```
Output: `Release\prodlyjni.dll`

**Linux**:
```bash
cd cpp_core
mkdir build && cd build
cmake .. -DCMAKE_BUILD_TYPE=Release
make -j$(nproc)
```
Output: `libprodlyjni.so`

**macOS**:
```bash
cd cpp_core
mkdir build && cd build
cmake .. -DCMAKE_BUILD_TYPE=Release
make -j$(sysctl -n hw.ncpu)
```
Output: `libprodlyjni.dylib`

### Java Build

The Java GUI uses Maven for dependency management and building.

**Compile**:
```bash
cd java_gui
mvn clean compile
```

**Run**:
```bash
mvn javafx:run
```

**Package JAR**:
```bash
mvn clean package
java -jar target/prodly-gui-1.0.0.jar
```

**Note**: When running the JAR, ensure the native library is in the library path:
```bash
# Linux/macOS
java -Djava.library.path=/path/to/cpp_core/build -jar target/prodly-gui-1.0.0.jar

# Windows
java -Djava.library.path=C:\path\to\cpp_core\build\Release -jar target/prodly-gui-1.0.0.jar
```

## Troubleshooting

### Java Cannot Find Native Library

**Error**: `java.lang.UnsatisfiedLinkError: no prodlyjni in java.library.path`

**Solution**: 
1. Ensure the C++ library is built
2. Add library directory to `java.library.path` system property
3. Or copy library to system library directory
4. On Windows, ensure `.dll` is in PATH
5. On Linux, ensure `.so` is in LD_LIBRARY_PATH

### CMake Cannot Find JNI

**Error**: `Could NOT find JNI`

**Solution**:
1. Ensure JDK is installed (not just JRE)
2. Set `JAVA_HOME` environment variable
3. On Windows: `set JAVA_HOME=C:\Program Files\Java\jdk-11`
4. On Linux/macOS: `export JAVA_HOME=/usr/lib/jvm/java-11-openjdk`

### Maven Build Fails

**Error**: JavaFX dependencies not found

**Solution**:
1. Ensure Java 11+ is installed
2. Check `JAVA_HOME` points to JDK, not JRE
3. Update Maven: `mvn -U clean compile`

### Compilation Errors in C++

**Common Issues**:
1. **C++17 not supported**: Update compiler
2. **Missing includes**: Check all headers are in `include/` directory
3. **Link errors**: Ensure all source files are in CMakeLists.txt

## IDE Setup

### VS Code (C++)

1. Install C/C++ extension
2. Install CMake Tools extension
3. Open `cpp_core` folder
4. Configure CMake (Ctrl+Shift+P -> "CMake: Configure")
5. Build (Ctrl+Shift+P -> "CMake: Build")

### NetBeans/IntelliJ (Java)

1. Open `java_gui` folder as Maven project
2. Wait for Maven dependencies to download
3. Set main class: `com.prodly.ProdlyApplication`
4. Run configuration:
   - VM options: `-Djava.library.path=/path/to/cpp_core/build`
   - Main class: `com.prodly.ProdlyApplication`

## Testing

After building, test each module:

1. **Module 1**: Add a vendor and verify lock-in score calculation
2. **Module 2**: Add migration tasks and verify sequence generation
3. **Module 3**: Add vendor metrics and verify readiness ranking

## Development Workflow

1. **Edit C++ code** → Rebuild C++ library
2. **Edit Java code** → Recompile Java (Maven handles this)
3. **Edit CSS** → Refresh Java application
4. **Test** → Run application and verify functionality

## Notes

- C++ library must be rebuilt after any C++ code changes
- Java application can be reloaded without rebuilding C++ (if no interface changes)
- JNI interface changes require rebuilding both C++ and Java
- Native library loading happens at runtime in Java

