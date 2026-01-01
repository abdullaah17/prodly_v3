# How to Run in VS Code - Step by Step

## Current Problem
You're in the `cpp_core` folder, but you need to be in the `java_gui` folder to run the Java application.

## Solution

### Step 1: Navigate to the Correct Directory

In the VS Code terminal, type:

```powershell
cd ..
cd java_gui
```

**OR** navigate directly:

```powershell
cd C:\Users\hp\Desktop\prodly_v3\java_gui
```

### Step 2: Verify You're in the Right Place

Check that you can see `pom.xml`:

```powershell
dir pom.xml
```

You should see `pom.xml` listed. If not, you're in the wrong directory.

### Step 3: Run the Application

```powershell
mvn javafx:run
```

## Complete Command Sequence

Copy and paste this entire sequence:

```powershell
cd C:\Users\hp\Desktop\prodly_v3\java_gui
mvn javafx:run
```

## Quick Reference

**Project Structure:**
```
prodly_v3/
├── cpp_core/          ← You were here (wrong!)
├── java_gui/          ← You need to be here!
│   ├── pom.xml        ← Maven file is here
│   └── src/
└── .vscode/
```

**From `cpp_core` directory:**
- Go up one level: `cd ..`
- Go into java_gui: `cd java_gui`
- Run: `mvn javafx:run`

**From project root (`prodly_v3`):**
- Go into java_gui: `cd java_gui`
- Run: `mvn javafx:run`

## Alternative: Use Full Path

You can always use the full path from anywhere:

```powershell
cd C:\Users\hp\Desktop\prodly_v3\java_gui
mvn javafx:run
```

