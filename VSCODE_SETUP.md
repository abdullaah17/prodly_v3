# VS Code Setup Guide for Prodly Application

## Prerequisites

1. **VS Code Extensions** (Install from VS Code Extensions Marketplace):
   - **Extension Pack for Java** (Microsoft) - includes:
     - Language Support for Java
     - Debugger for Java
     - Test Runner for Java
     - Maven for Java
     - Project Manager for Java
     - Visual Studio IntelliCode

2. **Java JDK 11 or later** - Ensure Java is installed and `JAVA_HOME` is set

3. **Maven** - Ensure Maven is installed and in PATH

## Setup Steps

### 1. Install Required Extensions

1. Open VS Code
2. Press `Ctrl+Shift+X` to open Extensions
3. Search for "Extension Pack for Java" by Microsoft
4. Click Install

### 2. Open the Project

1. Open VS Code
2. File → Open Folder
3. Navigate to the project root: `C:\Users\hp\Desktop\prodly_v3`
4. Click "Select Folder"

### 3. Configure Java Project

VS Code should automatically detect the Java project. If not:

1. Press `Ctrl+Shift+P` to open Command Palette
2. Type "Java: Clean Java Language Server Workspace"
3. Select and run it
4. Reload the window

### 4. Running the Application

#### Option 1: Using Launch Configuration (Recommended)

1. Press `F5` or go to Run and Debug (Ctrl+Shift+D)
2. Select "Launch Prodly Application (Maven JavaFX)" from the dropdown
3. Click the green play button or press F5

#### Option 2: Using Maven Task

1. Press `Ctrl+Shift+P` to open Command Palette
2. Type "Tasks: Run Task"
3. Select "maven-javafx-run"

#### Option 3: Using Terminal

1. Open Terminal in VS Code (Ctrl+`)
2. Navigate to java_gui directory:
   ```powershell
   cd java_gui
   ```
3. Run the application:
   ```powershell
   mvn javafx:run
   ```

### 5. Debugging

1. Set breakpoints in your Java code (click left of line numbers)
2. Press `F5` or go to Run and Debug
3. Select "Debug Prodly Application"
4. The debugger will pause at breakpoints

## Configuration Files

### `.vscode/launch.json`
Contains launch configurations for running and debugging the application.

### `.vscode/tasks.json`
Contains Maven build tasks:
- `maven-compile`: Compiles the Java code
- `maven-javafx-run`: Runs the JavaFX application
- `maven-clean-compile`: Clean, compile, and run

### `.vscode/settings.json`
Contains Java project settings and file exclusions.

## Troubleshooting

### Application Won't Start

1. **Check Java Installation**:
   ```powershell
   java -version
   ```

2. **Check Maven Installation**:
   ```powershell
   mvn -version
   ```

3. **Clean and Rebuild**:
   ```powershell
   cd java_gui
   mvn clean compile
   ```

4. **Check JavaFX Dependencies**:
   Ensure Maven has downloaded JavaFX dependencies:
   ```powershell
   mvn dependency:resolve
   ```

### VS Code Doesn't Recognize Java Files

1. Press `Ctrl+Shift+P`
2. Type "Java: Clean Java Language Server Workspace"
3. Restart VS Code

### Module Path Issues

If you see "module not found" errors:
1. The JavaFX Maven plugin should handle module paths automatically
2. If issues persist, check that JavaFX version in `pom.xml` matches installed version

### Native Library Issues

If the native C++ library is not found:
- The application will run in demo mode (this is expected if C++ library is not built)
- To use the native library, build it first with CMake
- Update the `java.library.path` in launch.json if needed

## Quick Reference

| Action | Shortcut/Command |
|--------|-----------------|
| Run Application | F5 (with launch config) |
| Debug Application | F5 (with debug config) |
| Run Maven Task | Ctrl+Shift+P → Tasks: Run Task |
| Open Terminal | Ctrl+` |
| Command Palette | Ctrl+Shift+P |
| Run and Debug Panel | Ctrl+Shift+D |

## Notes

- The application runs in **demo mode** by default if the C++ native library is not available
- Sample data is automatically loaded when the application starts
- All three modules (Vendor Lock-In, Migration Difficulty, Exit Readiness) are fully functional in demo mode

