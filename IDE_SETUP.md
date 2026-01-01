# IDE Setup Instructions

## VS Code Setup (for C++ Core)

### Prerequisites
1. Install VS Code extensions:
   - **C/C++** (by Microsoft)
   - **CMake Tools** (by Microsoft)
   - **CMake** (by twxs)

### Setup Steps

1. **Open the C++ project**:
   ```
   File > Open Folder > Select: cpp_core
   ```

2. **Configure CMake**:
   - Press `Ctrl+Shift+P` (or `Cmd+Shift+P` on Mac)
   - Type: `CMake: Configure`
   - Select your compiler (e.g., "Visual Studio Build Tools 2019 Release - x86_amd64")
   - VS Code will create a `build` folder

3. **Build the project**:
   - Press `Ctrl+Shift+P`
   - Type: `CMake: Build`
   - Or use the build button in the status bar
   - Or press `F7`

4. **Output location**:
   - Windows: `build/Release/prodlyjni.dll`
   - Linux: `build/libprodlyjni.so`
   - macOS: `build/libprodlyjni.dylib`

### VS Code Tasks (Optional)

Create `.vscode/tasks.json` in `cpp_core/`:

```json
{
    "version": "2.0.0",
    "tasks": [
        {
            "label": "CMake Build",
            "type": "shell",
            "command": "cmake",
            "args": [
                "--build",
                "build",
                "--config",
                "Release"
            ],
            "group": {
                "kind": "build",
                "isDefault": true
            },
            "problemMatcher": []
        },
        {
            "label": "CMake Clean",
            "type": "shell",
            "command": "cmake",
            "args": [
                "--build",
                "build",
                "--target",
                "clean"
            ],
            "problemMatcher": []
        }
    ]
}
```

### VS Code Launch Configuration (for debugging)

Create `.vscode/launch.json` in `cpp_core/`:

```json
{
    "version": "0.2.0",
    "configurations": [
        {
            "name": "Debug C++ Library",
            "type": "cppdbg",
            "request": "launch",
            "program": "${workspaceFolder}/build/Release/prodlyjni.dll",
            "args": [],
            "stopAtEntry": false,
            "cwd": "${workspaceFolder}",
            "environment": [],
            "externalConsole": false,
            "MIMode": "gdb",
            "setupCommands": [
                {
                    "description": "Enable pretty-printing for gdb",
                    "text": "-enable-pretty-printing",
                    "ignoreFailures": true
                }
            ]
        }
    ]
}
```

---

## NetBeans Setup (for Java GUI)

### Prerequisites
1. Install NetBeans IDE (with Java support)
2. Ensure Maven is installed (or use NetBeans bundled Maven)

### Setup Steps

1. **Open the Java project**:
   ```
   File > Open Project > Select: java_gui folder
   ```
   - NetBeans will recognize it as a Maven project

2. **Wait for Maven to download dependencies**:
   - NetBeans will automatically download JavaFX and other dependencies
   - Check the "Services" tab > Maven Repositories for progress

3. **Configure Project Properties**:
   - Right-click project > Properties
   - **Sources**: Verify Java version is 11+
   - **Run**: Set Main Class to `com.prodly.ProdlyApplication`

4. **Set Native Library Path** (IMPORTANT):
   - Right-click project > Properties
   - Go to **Run** category
   - In **VM Options**, add:
     ```
     -Djava.library.path=../cpp_core/build/Debug
     ```
     (Use `Debug` if building in Debug mode, or `Release` if building in Release mode)
   - Or use absolute path:
     ```
     -Djava.library.path=C:\Users\hp\Desktop\prodly_v3\cpp_core\build\Debug
     ```
   - **Note**: The path must match where the DLL actually exists!

5. **Run the Application**:
   - Press `F6` (Run Project)
   - Or right-click project > Run
   - Or click the green "Run" button in toolbar

### NetBeans Project Properties Screenshot Locations

**Main Class Configuration:**
- Properties > Run > Main Class: `com.prodly.ProdlyApplication`

**VM Options:**
- Properties > Run > VM Options: `-Djava.library.path=../cpp_core/build/Release`

### NetBeans Run Configuration

If you want to create a custom run configuration:

1. Right-click project > **Set Configuration** > **Customize**
2. Set:
   - Main Class: `com.prodly.ProdlyApplication`
   - VM Options: `-Djava.library.path=${PROJECT_DIR}/../cpp_core/build/Release`
   - Working Directory: `${PROJECT_DIR}`

---

## IntelliJ IDEA Setup (Alternative Java IDE)

### Setup Steps

1. **Open the Java project**:
   ```
   File > Open > Select: java_gui folder
   ```
   - IntelliJ will recognize it as a Maven project

2. **Wait for Maven import**:
   - IntelliJ will show "Maven projects need to be imported"
   - Click "Import Maven Project"

3. **Configure Run Configuration**:
   - Run > Edit Configurations
   - Click "+" > Application
   - Set:
     - Name: `ProdlyApplication`
     - Main class: `com.prodly.ProdlyApplication`
     - VM options: `-Djava.library.path=../cpp_core/build/Release`
     - Working directory: `$PROJECT_DIR$/java_gui`

4. **Run**:
   - Press `Shift+F10` (Run)
   - Or click the green "Run" button

---

## Complete Workflow

### First Time Setup

1. **Build C++ Library (VS Code)**:
   ```bash
   cd cpp_core
   # In VS Code: CMake: Configure, then CMake: Build
   # Or command line:
   mkdir build && cd build
   cmake .. -G "Visual Studio 16 2019" -A x64
   cmake --build . --config Release
   ```

2. **Verify Native Library Exists**:
   - Check: `cpp_core/build/Release/prodlyjni.dll` (Windows)
   - Or: `cpp_core/build/libprodlyjni.so` (Linux)
   - Or: `cpp_core/build/libprodlyjni.dylib` (macOS)

3. **Run Java GUI (NetBeans/IntelliJ)**:
   - Open `java_gui` as Maven project
   - Set VM options with library path
   - Run the application

### Development Workflow

1. **Edit C++ code** (in VS Code):
   - Make changes in `cpp_core/src/` or `cpp_core/include/`
   - Build: `CMake: Build` in VS Code
   - Library is rebuilt

2. **Edit Java code** (in NetBeans/IntelliJ):
   - Make changes in `java_gui/src/main/java/com/prodly/`
   - Run: `F6` or Run button
   - Application restarts with changes

3. **Edit CSS/Resources**:
   - Make changes in `java_gui/src/main/resources/styles.css`
   - Run: Application restarts with new styles

---

## Troubleshooting

### VS Code - CMake Not Found
- Install CMake: https://cmake.org/download/
- Or install via VS Code extension: CMake Tools
- Restart VS Code

### VS Code - JNI Headers Not Found
- Set `JAVA_HOME` environment variable:
  ```
  Windows: set JAVA_HOME=C:\Program Files\Java\jdk-17
  Linux/Mac: export JAVA_HOME=/usr/lib/jvm/java-17-openjdk
  ```
- Restart VS Code

### NetBeans - Native Library Not Found
- Verify library path in VM Options is correct
- Use absolute path if relative path doesn't work
- Windows: Use `\` or `/` in path (both work)
- Check library file exists in the specified directory

### NetBeans - JavaFX Not Found
- Maven should download it automatically
- Check: Tools > Options > Java > Maven > Repositories
- Try: Right-click project > Dependencies > Download Declared Dependencies

### Application Crashes on Startup
- Check console for error messages
- Verify native library is built and in correct location
- Check VM options are set correctly
- Ensure Java version is 11+

---

## Quick Reference

### VS Code Shortcuts
- `Ctrl+Shift+P` → CMake: Configure
- `F7` → Build
- `Ctrl+Shift+B` → Run Build Task

### NetBeans Shortcuts
- `F6` → Run Project
- `F11` → Build Project
- `Shift+F11` → Clean and Build

### IntelliJ Shortcuts
- `Shift+F10` → Run
- `Ctrl+F9` → Build Project
- `Shift+F9` → Debug

---

## File Paths Reference

```
prodly_v3/
├── cpp_core/                    # VS Code project
│   ├── build/                   # Build output (created by CMake)
│   │   └── Release/
│   │       └── prodlyjni.dll    # Native library (Windows)
│   ├── include/                 # C++ headers
│   ├── src/                     # C++ source files
│   └── JNI/                     # JNI interface
│
└── java_gui/                    # NetBeans/IntelliJ project
    ├── src/main/java/com/prodly/
    │   └── ProdlyApplication.java  # Main class
    ├── src/main/resources/
    │   └── styles.css           # UI styling
    └── pom.xml                  # Maven configuration
```

