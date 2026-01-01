# Quick Start Guide - Running Prodly

## Prerequisites Check

Before running, ensure you have:
- ✅ Java JDK 11 or higher
- ✅ Maven 3.6+ (optional if using IDE)
- ✅ CMake 3.15+ (optional for first run - can run in demo mode)

Check Java:
```powershell
java -version
```

---

## Option 1: Quick Run (Demo Mode)

**If you just want to see the UI without full functionality:**

```powershell
cd java_gui
mvn javafx:run
```

This runs in demo mode (limited functionality) but shows the full UI.

---

## Option 2: Full Functionality (Recommended)

### Step 1: Build C++ Library (One-Time Setup)

**Windows (Command Line):**
```powershell
cd cpp_core
mkdir build
cd build
cmake .. -G "Visual Studio 16 2019" -A x64
cmake --build . --config Release
```

**Or Windows (VS Code):**
1. Open VS Code in `cpp_core` folder
2. Press `Ctrl+Shift+P` → "CMake: Configure"
3. Press `F7` to build
4. Library will be at: `build/Release/prodlyjni.dll`

**Linux/macOS:**
```bash
cd cpp_core
mkdir build && cd build
cmake .. -DCMAKE_BUILD_TYPE=Release
make -j4
```

### Step 2: Run Java Application

**Command Line (Windows):**
```powershell
cd java_gui
mvn javafx:run
```

**Command Line (Linux/macOS):**
```bash
cd java_gui
mvn javafx:run
```

**Or with Library Path:**
```powershell
# Windows
java -Djava.library.path=../cpp_core/build/Release -jar target/prodly-gui-1.0.0.jar

# Linux/macOS  
java -Djava.library.path=../cpp_core/build -jar target/prodly-gui-1.0.0.jar
```

---

## Option 3: Run from NetBeans IDE

1. **Open Project:**
   - File → Open Project → Select `java_gui` folder

2. **Configure:**
   - Right-click project → Properties → Run
   - **Main Class**: `com.prodly.ProdlyApplication`
   - **VM Options**: `-Djava.library.path=../cpp_core/build/Release`
     (Use `Debug` if you built in Debug mode)

3. **Run:**
   - Press `F6` or click Run button

---

## Option 4: Run from IntelliJ IDEA

1. **Open Project:**
   - File → Open → Select `java_gui` folder
   - Import Maven project when prompted

2. **Configure Run:**
   - Run → Edit Configurations
   - Add Application:
     - Name: `ProdlyApplication`
     - Main class: `com.prodly.ProdlyApplication`
     - VM options: `-Djava.library.path=../cpp_core/build/Release`
     - Working directory: `$PROJECT_DIR$/java_gui`

3. **Run:**
   - Press `Shift+F10` or click Run

---

## Verify It's Working

✅ Application window opens  
✅ Navigation bar with 3 modules visible  
✅ No errors in console  
✅ Can navigate between modules  

**If you see**: "Warning: Native library not found. Running in demo mode"  
→ The library path is incorrect, but UI will still work (limited functionality)

---

## Troubleshooting

### CMake Not Found
- Install from: https://cmake.org/download/
- Or skip C++ build and run in demo mode

### Java Not Found
- Install JDK 11+ from: https://adoptium.net/
- Set JAVA_HOME environment variable

### Library Not Found Error
- Verify library exists: `cpp_core/build/Release/prodlyjni.dll` (Windows)
- Check VM options path is correct
- Use absolute path if relative doesn't work

### Maven Build Fails
- Check Java version: `java -version` (should be 11+)
- Try: `mvn clean install -U`
- Check internet connection (downloads dependencies)

---

## Need More Details?

- **Full Guide**: See `USER_GUIDE.md` for comprehensive documentation
- **Build Details**: See `BUILD_INSTRUCTIONS.md` for detailed build steps
- **IDE Setup**: See `IDE_SETUP.md` for IDE-specific instructions
- **Troubleshooting**: See `RUNNING_ISSUES_SOLVED.md` for common issues

---

## Quick Reference

**Most Common Command:**
```powershell
cd java_gui
mvn javafx:run
```

**After Building C++ Library:**
```powershell
# Build once
cd cpp_core/build
cmake --build . --config Release

# Run anytime
cd ../../java_gui
mvn javafx:run
```

