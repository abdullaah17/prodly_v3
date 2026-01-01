# How to Run the Application in VS Code

## Quick Method: Use Terminal (Easiest)

1. Open VS Code in the project root folder
2. Press `` Ctrl+` `` (backtick) to open the integrated terminal
3. Type:
   ```powershell
   cd java_gui
   mvn javafx:run
   ```
4. Press Enter - the application will start!

## Method 2: Run Task

1. Press `Ctrl+Shift+P` to open Command Palette
2. Type: `Tasks: Run Task`
3. You should see these tasks:
   - **Run JavaFX Application** (recommended)
   - **maven-javafx-run** (same as above)
   - **Clean and Run** (clean, compile, then run)
4. Select one and press Enter

**If you don't see the tasks:**
- Make sure you opened VS Code in the project root (where `.vscode` folder is)
- Try reloading VS Code window: `Ctrl+Shift+P` → "Developer: Reload Window"

## Method 3: Run and Debug Panel

1. Press `Ctrl+Shift+D` to open Run and Debug
2. Select "Launch Prodly Application" from dropdown
3. Press `F5`

## Troubleshooting

### Tasks don't appear in Run Task menu
- Ensure you're in the project root directory
- Check that `.vscode/tasks.json` exists
- Reload VS Code window
- Use Terminal method instead (Method 1 - always works)

### "mvn command not found"
- Make sure Maven is installed
- Test: Open terminal and type `mvn -version`

### Application doesn't start
- Try the Terminal method (Method 1) - it's most reliable
- Make sure you're in the `java_gui` directory when running `mvn javafx:run`

## Recommended: Terminal Method

The terminal method is the most reliable:
1. Open terminal (`Ctrl+` ` `)
2. `cd java_gui`
3. `mvn javafx:run`

This works exactly the same as NetBeans!

