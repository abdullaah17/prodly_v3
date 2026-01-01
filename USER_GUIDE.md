# Prodly - Vendor Lock-In Analysis Platform
## Comprehensive User Guide

---

## Table of Contents

1. [Introduction](#introduction)
2. [Getting Started](#getting-started)
3. [System Requirements](#system-requirements)
4. [Installation & Setup](#installation--setup)
5. [Application Overview](#application-overview)
6. [Module 1: Vendor Lock-In Score Calculator](#module-1-vendor-lock-in-score-calculator)
7. [Module 2: Migration Difficulty Analyzer](#module-2-migration-difficulty-analyzer)
8. [Module 3: Exit Readiness Dashboard](#module-3-exit-readiness-dashboard)
9. [Navigation & Interface](#navigation--interface)
10. [Common Tasks](#common-tasks)
11. [Best Practices](#best-practices)
12. [Troubleshooting](#troubleshooting)
13. [FAQ](#faq)

---

## Introduction

### What is Prodly?

Prodly is a professional SaaS platform designed to help companies analyze and understand vendor lock-in risks. It helps organizations make informed decisions about vendor relationships by providing:

- **Lock-In Risk Scoring**: Quantify how "locked in" you are to specific vendors
- **Migration Planning**: Assess the difficulty and complexity of migrating away from vendors
- **Exit Readiness Analysis**: Determine your readiness to exit vendor relationships

### Why Use Prodly?

Many companies discover vendor lock-in risks too late, when:
- Switching costs become prohibitive
- Data is trapped in proprietary systems
- Alternative solutions were actually cheaper
- Migration becomes extremely difficult

Prodly helps you identify these risks **before** they become problems.

---

## Getting Started

### Quick Start

1. **Build the C++ library** (one-time setup)
2. **Run the Java application**
3. **Navigate to Module 1** to start analyzing vendors

See [Installation & Setup](#installation--setup) for detailed instructions.

---

## System Requirements

### Minimum Requirements

- **Operating System**: Windows 10+, Linux, or macOS
- **RAM**: 4 GB minimum, 8 GB recommended
- **Storage**: 500 MB free space
- **Screen Resolution**: 1200x800 minimum (1920x1080 recommended)

### Software Requirements

- **Java**: JDK 11 or higher
- **Maven**: 3.6+ (for building Java GUI)
- **CMake**: 3.15+ (for building C++ core)
- **C++ Compiler**: MSVC 2019+, GCC 7+, or Clang 8+

---

## Installation & Setup

### Step 1: Build C++ Core Library

The C++ library must be built before running the application.

#### Windows (Command Line)

```powershell
cd cpp_core
mkdir build
cd build
cmake .. -G "Visual Studio 16 2019" -A x64
cmake --build . --config Release
```

The library will be created at: `cpp_core/build/Release/prodlyjni.dll`

#### Windows (VS Code)

1. Open VS Code in the `cpp_core` folder
2. Install extensions: C/C++ and CMake Tools
3. Press `Ctrl+Shift+P` → Type "CMake: Configure"
4. Press `F7` or `Ctrl+Shift+B` to build

#### Linux/macOS

```bash
cd cpp_core
mkdir build && cd build
cmake .. -DCMAKE_BUILD_TYPE=Release
make -j4
```

The library will be at: `cpp_core/build/libprodlyjni.so` (Linux) or `libprodlyjni.dylib` (macOS)

### Step 2: Run Java Application

#### Option A: Command Line (Recommended for Testing)

```bash
cd java_gui
mvn javafx:run
```

#### Option B: NetBeans IDE

1. Open NetBeans
2. File → Open Project → Select `java_gui` folder
3. Right-click project → Properties → Run
4. Set **Main Class**: `com.prodly.ProdlyApplication`
5. Set **VM Options**: `-Djava.library.path=../cpp_core/build/Release`
   - Use `Debug` instead of `Release` if you built in Debug mode
6. Press `F6` to run

#### Option C: IntelliJ IDEA

1. File → Open → Select `java_gui` folder
2. Import Maven project when prompted
3. Run → Edit Configurations
4. Add new Application configuration:
   - Name: `ProdlyApplication`
   - Main class: `com.prodly.ProdlyApplication`
   - VM options: `-Djava.library.path=../cpp_core/build/Release`
5. Click Run

### Step 3: Verify Installation

1. Application window should open
2. You should see the navigation bar with three modules
3. No errors in the console about missing native library

**Note**: If you see "Warning: Native library not found. Running in demo mode", the library path is incorrect. Check VM options in your IDE or set the library path correctly.

---

## Application Overview

### Main Window Layout

The application consists of:

1. **Navigation Bar** (Top)
   - PRODLY logo
   - Three module buttons:
     - Vendor Lock-In Score
     - Migration Difficulty
     - Exit Readiness

2. **Content Area** (Center)
   - Displays the active module's interface
   - Scrollable when content exceeds viewport

3. **Window Controls** (Top-Right)
   - Minimize, Maximize, Close buttons

### Features

- **Professional UI**: Modern, clean interface designed for business use
- **Multi-Module**: Three integrated analysis modules
- **Scrollable Content**: All screens support scrolling
- **Responsive Design**: Adapts to window size
- **Color-Coded Results**: Visual indicators for risk levels

---

## Module 1: Vendor Lock-In Score Calculator

### Purpose

Calculate a quantitative lock-in risk score (0-100) for vendors based on:
- Contract terms and duration
- Data volume and dependencies
- Custom integrations
- Switching costs

### How to Use

#### Step 1: Navigate to Module 1

Click **"Vendor Lock-In Score"** in the navigation bar.

#### Step 2: Fill in Vendor Information

**Required Fields:**
- **Vendor ID**: Unique identifier (e.g., "VENDOR-001")
- **Vendor Name**: Full vendor name (e.g., "Acme Cloud Services")

**Contract Information:**
- **Contract Value ($)**: Total contract value in USD
- **Contract Duration (months)**: Length of contract (e.g., 36)

**Technical Details:**
- **Data Volume (GB)**: Amount of data stored with vendor
- **API Dependencies**: Number of API integrations
- **Switching Cost ($)**: Estimated cost to switch vendors

**Integration:**
- **Has Custom Integration**: Check if vendor has custom integrations

#### Step 3: Calculate Score

Click **"Calculate Lock-In Score"** button.

#### Step 4: View Results

Results appear in the table on the right:
- **Vendor ID**: Identifier
- **Vendor Name**: Name
- **Lock-In Score**: Color-coded bar (0-100)
  - **Green (0-33)**: Low risk
  - **Amber (34-66)**: Medium risk
  - **Red (67-100)**: High risk

### Understanding the Score

**Score Calculation Factors:**
- Contract Value: Higher value = higher lock-in
- Contract Duration: Longer contracts = higher lock-in
- Data Volume: More data = harder to migrate
- API Dependencies: More dependencies = tighter coupling
- Custom Integration: Custom integrations significantly increase lock-in
- Switching Cost: Direct cost impact

**Interpretation:**
- **0-33 (Low)**: Low lock-in risk, easy to switch
- **34-66 (Medium)**: Moderate risk, switching possible but requires planning
- **67-100 (High)**: High risk, significant barriers to switching

### Example Workflow

1. Enter vendor: "AWS-001", "Amazon Web Services"
2. Contract: $500,000, 36 months
3. Technical: 10,000 GB, 15 APIs, $50,000 switching cost
4. Custom Integration: Checked
5. Click "Calculate Lock-In Score"
6. Result: Score of 78 (High Risk) displayed in red bar

---

## Module 2: Migration Difficulty Analyzer

### Purpose

Analyze the complexity and difficulty of migrating away from a vendor by:
- Breaking down migration into tasks
- Identifying dependencies between tasks
- Calculating optimal migration sequence
- Estimating total time and difficulty

### How to Use

#### Step 1: Navigate to Module 2

Click **"Migration Difficulty"** in the navigation bar.

#### Step 2: Set Vendor ID

Enter a **Vendor ID** for this migration analysis (e.g., "AWS-001").

#### Step 3: Add Migration Tasks

For each migration task, fill in:

**Required:**
- **Task ID**: Unique identifier (e.g., "TASK-001")
- **Task Name**: Descriptive name (e.g., "Export User Data")

**Metrics:**
- **Difficulty (1-10)**: How difficult is this task?
  - 1-3: Easy
  - 4-6: Moderate
  - 7-10: Difficult
- **Estimated Days**: How many days will this take?

**Dependencies:**
- List task IDs this task depends on (one per line)
  - Example:
    ```
    TASK-001
    TASK-002
    ```
  - Leave empty if no dependencies

Click **"Add Task"** to add the task to the list.

#### Step 4: Calculate Migration Difficulty

Once all tasks are added:
1. Click **"Calculate Migration Difficulty"**
2. View results:
   - **Difficulty Score**: Overall difficulty (0-100)
   - **Total Estimated Days**: Sum of all task days
   - **Optimal Migration Sequence**: Ordered list of tasks

### Understanding the Results

**Difficulty Score:**
- Based on total days, task count, and complexity
- Higher score = more difficult migration

**Optimal Sequence:**
- Tasks listed in execution order
- Respects dependencies (dependencies come first)
- Generated using graph algorithms (BFS/topological sort)

**Critical Path:**
- Longest dependency chain
- Tasks that must be completed in sequence
- Determines minimum migration time

### Example Workflow

**Scenario**: Migrating from AWS to Azure

1. Vendor ID: "AWS-001"

2. Add Tasks:
   - TASK-001: "Export Database" (Difficulty: 7, Days: 5)
   - TASK-002: "Export User Files" (Difficulty: 5, Days: 3)
   - TASK-003: "Setup Azure Infrastructure" (Difficulty: 8, Days: 10, Dependencies: TASK-001)
   - TASK-004: "Migrate Data to Azure" (Difficulty: 9, Days: 7, Dependencies: TASK-002, TASK-003)

3. Click "Calculate Migration Difficulty"
4. Results:
   - Difficulty Score: 72
   - Total Days: 25
   - Optimal Sequence: TASK-001, TASK-002, TASK-003, TASK-004

---

## Module 3: Exit Readiness Dashboard

### Purpose

Assess your readiness to exit vendor relationships by analyzing:
- Lock-in scores (from Module 1)
- Migration difficulty (from Module 2)
- Data export capabilities
- Contract flexibility
- Technical complexity

### How to Use

#### Step 1: Navigate to Module 3

Click **"Exit Readiness"** in the navigation bar.

#### Step 2: Add Vendor Metrics

Fill in vendor readiness metrics:

**Required:**
- **Vendor ID**: Vendor identifier

**Scores (0-100 scale):**
- **Lock-In Score**: From Module 1 (or estimate)
- **Migration Difficulty**: From Module 2 (or estimate)
- **Data Export Capability**: How easy to export data? (100 = very easy)
- **Contract Flexibility**: How flexible is the contract? (100 = very flexible)
- **Technical Complexity**: How complex is the integration? (100 = very complex)

#### Step 3: Calculate Exit Readiness

Click **"Calculate Exit Readiness"**.

#### Step 4: View Rankings

Click **"Refresh Rankings"** to see all vendors sorted by readiness score.

The table shows:
- **Rank**: Position in readiness ranking (#1 = most ready)
- **Vendor ID**: Identifier
- **Exit Readiness Score**: Color-coded bar (0-100)
  - **Red (0-33)**: Low readiness, difficult to exit
  - **Amber (34-66)**: Moderate readiness
  - **Green (67-100)**: High readiness, easier to exit

### Understanding Exit Readiness

**Score Calculation:**
- **Inverse Lock-In**: Lower lock-in = higher readiness
- **Inverse Migration Difficulty**: Easier migration = higher readiness
- **Data Export**: Higher capability = higher readiness
- **Contract Flexibility**: More flexible = higher readiness
- **Inverse Technical Complexity**: Simpler = higher readiness

**Interpretation:**
- **0-33 (Low)**: Not ready, significant barriers
- **34-66 (Medium)**: Partially ready, requires planning
- **67-100 (High)**: Ready to exit, minimal barriers

**Rankings:**
- Vendors sorted by readiness (highest first)
- Helps prioritize which vendors to exit first
- Uses AVL tree for efficient sorting

### Example Workflow

1. Vendor ID: "VENDOR-001"
2. Metrics:
   - Lock-In Score: 65 (from Module 1)
   - Migration Difficulty: 55 (from Module 2)
   - Data Export: 80 (good export tools)
   - Contract Flexibility: 40 (rigid contract)
   - Technical Complexity: 70 (complex system)
3. Click "Calculate Exit Readiness"
4. Result: Score of 52 (Medium Readiness)
5. Click "Refresh Rankings" to see where this vendor ranks

---

## Navigation & Interface

### Navigation Bar

**Logo**: "PRODLY" branding (left side)

**Module Buttons**:
- Click any button to switch modules
- Active module highlighted in blue
- Inactive modules in gray

**Behavior**:
- Clicking a module button loads that module's interface
- Data is preserved when switching modules
- Each module maintains its own state

### Window Controls

**Minimize** (bottom line icon):
- Minimizes window to taskbar
- Click to restore

**Maximize/Restore** (square icon):
- Maximize: Expands to full screen (keeps controls)
- Restore: Returns to previous size

**Close** (X icon):
- Closes the application
- Unsaved data may be lost (consider saving before closing)

### Scrolling

**When Content Exceeds Viewport**:
- Vertical scrollbar appears on the right
- Scroll using:
  - Mouse wheel
  - Scrollbar drag
  - Arrow keys (when focused)
  - Page Up/Down keys

**Scrollbar Appearance**:
- Only appears when needed
- Styled to match application theme
- Smooth scrolling behavior

### Tables

**Features**:
- Sortable columns (where applicable)
- Color-coded progress bars
- Hover effects on rows
- Alternating row colors for readability

**Interactions**:
- Click column headers to sort (if enabled)
- Hover over rows to highlight
- Scroll within tables if content is large

### Forms

**Input Fields**:
- Clear labels and placeholders
- Validation on submission
- Error messages for invalid input
- Focus indicators (blue border)

**Buttons**:
- Primary buttons (blue): Main actions
- Secondary buttons (white with border): Secondary actions
- Hover effects: Darker shade on hover
- Disabled state: Grayed out when unavailable

---

## Common Tasks

### Task 1: Analyze a Single Vendor's Lock-In Risk

1. Navigate to **Module 1: Vendor Lock-In Score**
2. Fill in all vendor information
3. Click **"Calculate Lock-In Score"**
4. Review the score and color coding
5. Interpret the risk level (Low/Medium/High)

### Task 2: Plan a Vendor Migration

1. Navigate to **Module 2: Migration Difficulty**
2. Enter Vendor ID
3. Add all migration tasks with dependencies
4. Click **"Calculate Migration Difficulty"**
5. Review:
   - Overall difficulty score
   - Total estimated days
   - Optimal task sequence
6. Use sequence to plan migration timeline

### Task 3: Assess Exit Readiness for Multiple Vendors

1. Add vendors to **Module 1** to get lock-in scores
2. Add migration tasks to **Module 2** to get difficulty scores
3. Navigate to **Module 3: Exit Readiness**
4. For each vendor:
   - Enter Vendor ID and metrics
   - Click **"Calculate Exit Readiness"**
5. Click **"Refresh Rankings"** to see all vendors
6. Review ranking to prioritize exit strategy

### Task 4: Compare Multiple Vendors

1. Add several vendors to Module 1
2. Review all scores in the results table
3. Compare scores side-by-side
4. Identify vendors with highest lock-in risk
5. Focus exit planning on high-risk vendors

### Task 5: Estimate Migration Timeline

1. Go to **Module 2**
2. Enter Vendor ID
3. Add all tasks with realistic time estimates
4. Calculate difficulty
5. Review **"Total Estimated Days"**
6. Add buffer time (typically 20-30%)
7. Plan project timeline based on sequence

---

## Best Practices

### Data Entry

1. **Use Consistent Naming**: 
   - Use same Vendor ID across all modules
   - Example: "AWS-001" not "aws-001" or "AWS_001"

2. **Be Accurate**:
   - Provide realistic estimates
   - Don't underestimate difficulty or time

3. **Complete All Fields**:
   - Missing data affects score accuracy
   - Use 0 for values that don't apply

4. **Document Assumptions**:
   - Keep notes outside the system
   - Record why certain values were used

### Analysis

1. **Start with Module 1**:
   - Get lock-in scores first
   - This provides baseline metrics

2. **Use Module 2 for Planning**:
   - Break down migration into specific tasks
   - Include all dependencies

3. **Use Module 3 for Strategy**:
   - Compare readiness across vendors
   - Prioritize exit strategy

4. **Review Scores Regularly**:
   - Update as contracts change
   - Recalculate when adding new vendors

### Interpretation

1. **Context Matters**:
   - High score doesn't always mean "bad"
   - Consider business value alongside risk

2. **Use Ranges, Not Exact Numbers**:
   - Scores are estimates, not absolutes
   - Focus on relative differences

3. **Combine with Business Knowledge**:
   - Tool provides data, you provide judgment
   - Consider strategic factors not in the model

4. **Review Dependencies Carefully**:
   - Task dependencies affect timeline
   - Identify critical path items

---

## Troubleshooting

### Application Won't Start

**Symptom**: Application doesn't launch or crashes immediately

**Solutions**:
1. Check Java version: `java -version` (should be 11+)
2. Verify C++ library is built and exists
3. Check library path in VM options
4. Review console for error messages
5. Try running from command line: `mvn javafx:run`

### Native Library Not Found

**Symptom**: Console shows "Warning: Native library 'prodlyjni' not found"

**Solutions**:
1. Verify library file exists:
   - Windows: `cpp_core/build/Release/prodlyjni.dll`
   - Linux: `cpp_core/build/libprodlyjni.so`
   - macOS: `cpp_core/build/libprodlyjni.dylib`

2. Check VM options path:
   - Should point to directory containing the library
   - Use absolute path if relative doesn't work

3. Rebuild the library if missing

4. Application will run in demo mode (limited functionality)

### Scores Not Calculating

**Symptom**: Clicking calculate button does nothing or shows error

**Solutions**:
1. Check all required fields are filled
2. Verify numeric fields contain valid numbers
3. Check console for error messages
4. Ensure library is loaded (no demo mode warning)

### Tables Not Showing Data

**Symptom**: Tables appear empty after calculation

**Solutions**:
1. Click "Refresh" button if available
2. Verify calculation completed successfully
3. Check if data was actually added
4. Try adding a new entry to test

### Scrolling Not Working

**Symptom**: Can't scroll down in screens

**Solutions**:
1. Ensure you're using the latest version
2. Try mouse wheel instead of scrollbar
3. Check if content actually exceeds viewport
4. Restart application

### Window Too Small or Large

**Symptom**: Window size doesn't fit screen

**Solutions**:
1. Click maximize/restore button
2. Drag window edges to resize
3. Application remembers window size on restart

### Build Errors

**Symptom**: Maven or CMake build fails

**Solutions**:
1. **CMake errors**:
   - Verify CMake version (3.15+)
   - Check JAVA_HOME is set
   - Ensure JDK (not just JRE) is installed

2. **Maven errors**:
   - Check Java version matches pom.xml (11+)
   - Verify internet connection (dependencies download)
   - Try: `mvn clean install -U`

3. **Compilation errors**:
   - Check error messages for specific issues
   - Verify all dependencies are available
   - Review IDE_SETUP.md for configuration

---

## FAQ

### General Questions

**Q: Is my data saved?**
A: Currently, data is stored in memory during the session. Closing the application will lose data. Consider exporting results if needed.

**Q: Can I export results?**
A: Export functionality is planned but not yet implemented. Use screenshots or manual documentation for now.

**Q: How accurate are the scores?**
A: Scores are estimates based on the algorithms and factors included. They should be used as guidance, not absolute truth.

**Q: Can I use this for multiple vendors?**
A: Yes! Add multiple vendors to compare them. All three modules support multiple vendors.

**Q: Do I need to build the C++ library every time?**
A: No, only when C++ code changes. The Java application can run multiple times using the same library.

### Technical Questions

**Q: What if I don't have CMake?**
A: You can run in demo mode (limited functionality), but full features require the C++ library. Install CMake from https://cmake.org/download/

**Q: Can I run this on Linux/Mac?**
A: Yes! The application is cross-platform. Build instructions differ slightly (see BUILD_INSTRUCTIONS.md).

**Q: Why does it take time to start?**
A: First run downloads JavaFX dependencies. Subsequent runs are faster.

**Q: Can I customize the scoring algorithms?**
A: Algorithm customization requires modifying C++ source code and rebuilding.

### Usage Questions

**Q: What if I don't know exact values?**
A: Use estimates. The tool is designed to work with approximate values. Better to have estimates than no analysis.

**Q: How do I handle multiple contracts with same vendor?**
A: Treat each contract as a separate vendor entry with unique Vendor ID (e.g., "VENDOR-001-CONTRACT-A").

**Q: Can I analyze vendors I'm considering?**
A: Yes! Enter estimated values based on typical contracts for that vendor type.

**Q: How often should I recalculate scores?**
A: Recalculate when:
- Contracts are renewed or changed
- New vendors are added
- Migration plans are updated
- Significant changes to data volume or integrations

### Module-Specific Questions

**Q: Module 1 - What if vendor has no switching cost?**
A: Enter 0 for switching cost. The algorithm will adjust accordingly.

**Q: Module 2 - How detailed should tasks be?**
A: Break down to meaningful units (typically 1-10 days per task). Too granular becomes unwieldy, too broad loses accuracy.

**Q: Module 2 - What if tasks can run in parallel?**
A: Dependencies determine sequence. Tasks without dependencies can run in parallel. The tool shows the critical path.

**Q: Module 3 - Can I use estimated values?**
A: Yes, but accuracy improves with real data from Modules 1 and 2.

---

## Additional Resources

### Documentation Files

- **README.md**: Project overview and architecture
- **BUILD_INSTRUCTIONS.md**: Detailed build steps
- **IDE_SETUP.md**: IDE-specific setup instructions
- **RUNNING_ISSUES_SOLVED.md**: Common issues and solutions

### Getting Help

1. **Check Documentation**: Review relevant guide sections
2. **Review Error Messages**: Console output often indicates the issue
3. **Check Build Status**: Ensure library is built correctly
4. **Verify Configuration**: Check VM options and paths

### Project Structure

```
prodly_v3/
├── cpp_core/           # C++ core logic (build this first)
├── java_gui/           # Java GUI application
├── README.md           # Project overview
├── USER_GUIDE.md       # This file
├── BUILD_INSTRUCTIONS.md
└── IDE_SETUP.md
```

---

## Version Information

- **Application Version**: 1.0.0
- **Java Version**: 11+
- **JavaFX Version**: 17.0.2
- **C++ Standard**: C++17

---

## Conclusion

Prodly provides powerful tools for analyzing vendor lock-in risks. Use this guide to:

1. **Understand** what each module does
2. **Navigate** the application effectively
3. **Analyze** vendors systematically
4. **Interpret** results meaningfully
5. **Troubleshoot** common issues

Remember: The tool provides data and analysis, but business judgment should guide final decisions.

**Happy Analyzing!**

