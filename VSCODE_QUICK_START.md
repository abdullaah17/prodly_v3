# VS Code Quick Start Guide

## Prerequisites
- Install **Extension Pack for Java** in VS Code (Ctrl+Shift+X, search "Extension Pack for Java")

## Running the Application

### Method 1: Using Maven Task (Recommended)
1. Press `Ctrl+Shift+P` (Command Palette)
2. Type: `Tasks: Run Task`
3. Select: `maven-javafx-run`
4. Application will start!

### Method 2: Using Terminal
1. Open Terminal in VS Code (`Ctrl+` ` `)
2. Navigate to java_gui:
   ```powershell
   cd java_gui
   ```
3. Run:
   ```powershell
   mvn javafx:run
   ```

### Method 3: Using Launch Configuration
1. Go to Run and Debug (`Ctrl+Shift+D`)
2. Select "Launch Prodly Application" from dropdown
3. Press `F5`

**Note**: Launch configuration may require JavaFX module path setup. Method 1 (Maven task) is most reliable.

## Building the Project

1. Press `Ctrl+Shift+P`
2. Type: `Tasks: Run Build Task`
3. Select: `maven-compile`

Or press `Ctrl+Shift+B` (default build task)

## Troubleshooting

### "Java extension not found"
- Install Extension Pack for Java from VS Code Extensions

### "Maven command not found"
- Ensure Maven is installed and in PATH
- Test: `mvn -version` in terminal

### Application won't start
- Use Method 1 (Maven task) - it handles JavaFX automatically
- Or run in terminal: `cd java_gui && mvn javafx:run`

