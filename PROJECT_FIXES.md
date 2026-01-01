# Project Fixes Applied

## Fixed Issues

### ✅ Fix 1: VS Code Tasks Configuration
- **Changed**: CMake Build task is now set as default (`isDefault: true`)
- **Removed**: MinGW build task (incompatible with this project)
- **File**: `cpp_core/.vscode/tasks.json`
- **Result**: Pressing `Ctrl+Shift+B` will now run the correct CMake build

### ✅ Fix 2: Created NetBeans Configuration
- **Created**: `java_gui/nbactions.xml` for NetBeans run configurations
- **Purpose**: Provides run/debug actions for NetBeans
- **Note**: Still need to set VM options manually in project properties

### ✅ Fix 3: Build C++ Library in Release Mode

Run this command to build in Release mode:
```powershell
cd cpp_core/build
cmake --build . --config Release
```

The library will be created at: `cpp_core/build/Release/prodlyjni.dll`

---

## Remaining Issues to Address

### ⚠️ Issue: Library Path Configuration

**Current Status**:
- Debug build exists at: `cpp_core/build/Debug/prodlyjni.dll`
- Release build location: `cpp_core/build/Release/prodlyjni.dll` (after building)

**Action Required**:
1. Build in Release mode (see command above)
2. Update NetBeans VM options to: `-Djava.library.path=../cpp_core/build/Release`

---

## Step-by-Step Fix Instructions

### For VS Code (C++ Development)

1. **Open VS Code** in `cpp_core` folder
2. **Press `Ctrl+Shift+P`** → Type "CMake: Configure"
3. **Press `F7`** or `Ctrl+Shift+B` → Build (should now use CMake Build task)
4. **Verify build**: Check `build/Release/prodlyjni.dll` exists

### For NetBeans (Java Development)

1. **Open NetBeans** → File → Open Project → Select `java_gui` folder
2. **Right-click project** → Properties → Run
3. **Set Main Class**: `com.prodly.ProdlyApplication`
4. **Set VM Options**: 
   ```
   -Djava.library.path=../cpp_core/build/Release
   ```
   (Use `Debug` if you didn't build in Release mode)
5. **Press F6** to Run

---

## Verification Checklist

- [ ] C++ library built successfully (Release or Debug)
- [ ] VS Code build task works (`Ctrl+Shift+B`)
- [ ] NetBeans project opens without errors
- [ ] VM options set correctly in NetBeans
- [ ] Application runs and loads native library
- [ ] No `UnsatisfiedLinkError` in console

---

## Testing

After applying fixes, test:

1. **Build C++ library**: Should complete without errors
2. **Run Java app from NetBeans**: Should launch GUI window
3. **Check console**: Should NOT see "Warning: Native library not found"
4. **Test functionality**: Try adding a vendor in Module 1

---

## Common Errors and Solutions

### Error: "no prodlyjni in java.library.path"
- **Solution**: Check VM options path matches actual library location
- **Check**: Library file exists at specified path

### Error: "CMake Build task not found"
- **Solution**: Open VS Code in `cpp_core` folder (not root)
- **Check**: CMake Tools extension is installed

### Error: "Maven project not recognized"
- **Solution**: Ensure `pom.xml` is in `java_gui` root
- **Check**: NetBeans shows project with Maven icon

### Error: "JavaFX not found"
- **Solution**: Let Maven download dependencies
- **Check**: Maven repositories are accessible

