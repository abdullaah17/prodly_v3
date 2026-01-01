# Why Project Isn't Running - Issues & Solutions

## 🔍 Root Causes Identified

### Issue #1: C++ Library Built in Wrong Configuration ⚠️ **CRITICAL**

**Problem**:
- Library exists only in **Debug** mode: `cpp_core/build/Debug/prodlyjni.dll`
- Documentation and Java configs point to **Release** directory
- NetBeans VM options expect: `../cpp_core/build/Release/prodlyjni.dll`
- Actual location: `../cpp_core/build/Debug/prodlyjni.dll`

**Impact**: 
- Application cannot find native library
- Results in `UnsatisfiedLinkError`
- Application runs in demo mode only

**Solution Options**:

**Option A: Build in Release Mode (Recommended)**
```powershell
cd cpp_core\build
cmake --build . --config Release
```
Then use VM option: `-Djava.library.path=../cpp_core/build/Release`

**Option B: Use Debug Build (Quick Fix)**
Change NetBeans VM option to: `-Djava.library.path=../cpp_core/build/Debug`

---

### Issue #2: VS Code Build Task Configuration ❌ **FIXED**

**Problem**:
- CMake Build task was set to `"isDefault": false`
- MinGW task was set as default (incompatible with this project)
- Pressing `Ctrl+Shift+B` would try wrong build method

**Status**: ✅ **FIXED**
- Updated `cpp_core/.vscode/tasks.json`
- CMake Build task is now default
- Removed incompatible MinGW task

**Action Required**: None - already fixed

---

### Issue #3: Missing NetBeans Configuration ❌ **FIXED**

**Problem**:
- No `nbactions.xml` file for NetBeans
- No pre-configured run/debug actions

**Status**: ✅ **FIXED**
- Created `java_gui/nbactions.xml`
- Provides run/debug actions for NetBeans

**Action Required**: 
- Still need to manually set VM options in Project Properties
- See NetBeans setup instructions below

---

### Issue #4: POM.xml XML Tag Issue ✅ **VERIFIED OK**

**Status**: ✅ **VERIFIED** - POM.xml is correct
- XML tags are properly formatted
- Maven build succeeds
- No syntax errors

---

## 🛠️ Step-by-Step Fix Instructions

### For VS Code (C++ Development)

1. **Open VS Code**
   - File → Open Folder → Select `cpp_core` folder

2. **Install Extensions** (if needed):
   - C/C++ (Microsoft)
   - CMake Tools (Microsoft)

3. **Configure CMake**:
   - Press `Ctrl+Shift+P`
   - Type: `CMake: Configure`
   - Select compiler (e.g., "Visual Studio Build Tools 2019 Release - x86_amd64")

4. **Build**:
   - Press `Ctrl+Shift+B` (or `F7`)
   - Should now use CMake Build task (fixed)
   - Wait for build to complete

5. **Verify Output**:
   - Check: `build/Debug/prodlyjni.dll` (Debug build)
   - OR: `build/Release/prodlyjni.dll` (Release build - if configured)

---

### For NetBeans (Java Development)

1. **Open Project**:
   - File → Open Project → Select `java_gui` folder
   - Wait for Maven to download dependencies

2. **Configure Project Properties**:
   - Right-click project → **Properties**
   - Go to **Run** category

3. **Set Main Class**:
   - Main Class: `com.prodly.ProdlyApplication`

4. **Set VM Options** (⚠️ **IMPORTANT**):
   
   **If library is in Debug folder:**
   ```
   -Djava.library.path=../cpp_core/build/Debug
   ```
   
   **If library is in Release folder:**
   ```
   -Djava.library.path=../cpp_core/build/Release
   ```
   
   **Use absolute path if relative doesn't work:**
   ```
   -Djava.library.path=C:\Users\hp\Desktop\prodly_v3\cpp_core\build\Debug
   ```

5. **Run Application**:
   - Press `F6` (Run Project)
   - Or click green "Run" button

---

## 🔧 Quick Fixes Applied

✅ **Fixed VS Code tasks.json**
- CMake Build is now default task
- Removed incompatible MinGW task

✅ **Created NetBeans nbactions.xml**
- Provides run/debug actions
- Still need VM options in project properties

---

## ⚠️ Action Required

### CRITICAL: Set Correct Library Path in NetBeans

**Current Status**:
- Library exists at: `cpp_core/build/Debug/prodlyjni.dll`
- You need to set VM options to point to this location

**Steps**:
1. Open NetBeans
2. Right-click `java_gui` project → Properties
3. Go to **Run** category
4. In **VM Options**, add:
   ```
   -Djava.library.path=../cpp_core/build/Debug
   ```
5. Click OK
6. Run project (F6)

**OR** build in Release mode and use:
```
-Djava.library.path=../cpp_core/build/Release
```

---

## ✅ Verification Checklist

After applying fixes, verify:

- [ ] VS Code can build C++ library (`Ctrl+Shift+B`)
- [ ] Native library DLL exists (Debug or Release folder)
- [ ] NetBeans project opens without errors
- [ ] Maven dependencies downloaded successfully
- [ ] VM options set correctly in NetBeans
- [ ] Application launches (press F6)
- [ ] No `UnsatisfiedLinkError` in console
- [ ] GUI window appears

---

## 🐛 Common Errors & Solutions

### Error: "no prodlyjni in java.library.path"

**Cause**: Library path not set correctly

**Solution**:
1. Check library exists: `cpp_core/build/Debug/prodlyjni.dll`
2. Verify VM options path matches actual location
3. Use absolute path if relative doesn't work

### Error: "CMake Build task not found"

**Cause**: Wrong folder open in VS Code

**Solution**:
- Open VS Code in `cpp_core` folder (not root folder)
- Install CMake Tools extension

### Error: "Application runs but shows demo mode warning"

**Cause**: Library not loading (path issue)

**Solution**:
- Check VM options are set correctly
- Verify DLL file exists at specified path
- Try absolute path instead of relative

### Error: "Maven project not recognized"

**Cause**: Wrong folder or corrupted POM

**Solution**:
- Ensure `pom.xml` is in `java_gui` root
- Check POM.xml syntax is correct
- Re-import Maven project

---

## 📋 Summary

**Main Issue**: Library path mismatch between actual location (Debug) and expected location (Release/Documentation)

**Quick Fix**: Set NetBeans VM options to: `-Djava.library.path=../cpp_core/build/Debug`

**Best Practice**: Build in Release mode and use Release path for production

**Status**: VS Code configuration fixed, NetBeans config file created, main issue is library path configuration

