# Security Testing Guide - Kali Linux in VMware

This guide explains how to perform security testing on the Prodly application using Kali Linux in a VMware virtual machine.

## Table of Contents
1. [VMware Setup](#vmware-setup)
2. [Kali Linux Installation](#kali-linux-installation)
3. [Application Setup in Kali](#application-setup-in-kali)
4. [Security Testing Approaches](#security-testing-approaches)
5. [Testing Tools](#testing-tools)
6. [Test Scenarios](#test-scenarios)

---

## VMware Setup

### Step 1: Install VMware Workstation/Player

1. **Download VMware:**
   - VMware Workstation Pro (paid): https://www.vmware.com/products/workstation-pro.html
   - VMware Workstation Player (free): https://www.vmware.com/products/workstation-player.html

2. **Install VMware** on your Windows host machine

3. **System Requirements:**
   - Minimum 4GB RAM (8GB+ recommended)
   - 20GB+ free disk space
   - Virtualization enabled in BIOS (Intel VT-x or AMD-V)

### Step 2: Create New Virtual Machine

1. Open VMware
2. **File → New Virtual Machine**
3. Select **"Typical"** configuration
4. Choose **"I will install the operating system later"**
5. Select **"Linux"** → **"Debian 11.x 64-bit"** (Kali is Debian-based)
6. Name: `Kali-Linux-Security-Testing`
7. Disk size: **40GB** (minimum)
8. Click **"Finish"**

---

## Kali Linux Installation

### Step 1: Download Kali Linux

1. Visit: https://www.kali.org/get-kali/
2. Download **Kali Linux Virtual Machines** → **VMware** version
   - Or download ISO: **Kali Linux 64-bit ISO**
3. Extract the VM files (if using pre-built VM) or use ISO

### Step 2: Configure VM Settings

1. **Right-click VM → Settings**
2. **Memory:** Allocate **4GB RAM** (or 50% of host RAM)
3. **Processors:** 2 cores minimum
4. **Network Adapter:** 
   - **NAT** (for internet access)
   - **Bridged** (to test from host network)
5. **CD/DVD:** Point to Kali ISO (if using ISO installation)

### Step 3: Install Kali Linux

**Option A: Using Pre-built VM (Easier)**
1. Extract downloaded VM files
2. In VMware: **File → Open** → Select `.vmx` file
3. Power on VM
4. Default credentials: `kali/kali`

**Option B: Using ISO (Full Installation)**
1. Power on VM
2. Boot from ISO
3. Select **"Graphical Install"**
4. Follow installation wizard:
   - Language: English
   - Location: Your location
   - Keyboard: US
   - Hostname: `kali-testing`
   - Domain: (leave blank)
   - Root password: (set strong password)
   - User account: Create `kali` user
   - Partition: Use entire disk
   - Software: Install default tools
5. Complete installation and reboot

### Step 4: Initial Kali Setup

```bash
# Update system
sudo apt update && sudo apt upgrade -y

# Install essential tools
sudo apt install -y git curl wget build-essential

# Install Java (for testing JavaFX app)
sudo apt install -y openjdk-11-jdk maven

# Install C++ build tools (for building native library)
sudo apt install -y cmake g++ gcc

# Install security testing tools
sudo apt install -y burpsuite wireshark nmap sqlmap metasploit-framework
```

---

## Application Setup in Kali

### Step 1: Transfer Application to Kali

**Option A: Shared Folder (Recommended)**
1. In VMware: **VM → Settings → Options → Shared Folders**
2. Add shared folder pointing to `C:\Users\hp\Desktop\prodly_v3`
3. In Kali:
   ```bash
   sudo mkdir -p /mnt/hgfs
   sudo vmhgfs-fuse .host:/ /mnt/hgfs -o subtype=vmhgfs-fuse,allow_other
   cd /mnt/hgfs/prodly_v3
   ```

**Option B: Network Transfer**
1. On Windows host, share the folder
2. In Kali:
   ```bash
   # Find Windows host IP
   nmap -sn 192.168.1.0/24
   
   # Mount Windows share
   sudo mkdir /mnt/windows
   sudo mount -t cifs //HOST_IP/prodly_v3 /mnt/windows -o username=YOUR_USER
   ```

**Option C: SCP/SFTP**
```bash
# From Windows PowerShell (if SSH enabled)
scp -r C:\Users\hp\Desktop\prodly_v3 kali@KALI_IP:/home/kali/
```

### Step 2: Build Application in Kali

```bash
cd /path/to/prodly_v3

# Build C++ core
cd cpp_core
mkdir build && cd build
cmake .. -DCMAKE_BUILD_TYPE=Release
make -j4

# Verify library created
ls -la libprodlyjni.so

# Build Java GUI
cd ../../java_gui
mvn clean package

# Verify JAR created
ls -la target/prodly-gui-1.0.0.jar
```

### Step 3: Run Application

```bash
cd java_gui
export LD_LIBRARY_PATH=../cpp_core/build:$LD_LIBRARY_PATH
java -Djava.library.path=../cpp_core/build -jar target/prodly-gui-1.0.0.jar
```

---

## Security Testing Approaches

### 1. Static Code Analysis

**C++ Code Analysis:**
```bash
# Install static analysis tools
sudo apt install -y cppcheck flawfinder

# Analyze C++ code
cd cpp_core
cppcheck --enable=all --xml --xml-version=2 src/ 2> cppcheck-report.xml
flawfinder src/
```

**Java Code Analysis:**
```bash
# Install SpotBugs
sudo apt install -y spotbugs

# Analyze Java code
cd java_gui
spotbugs -textui -output spotbugs-report.txt target/classes/
```

### 2. Binary Analysis

**Analyze Native Library:**
```bash
# Install analysis tools
sudo apt install -y binutils file strings objdump readelf

# Analyze library
cd cpp_core/build
file libprodlyjni.so
strings libprodlyjni.so | grep -i "password\|secret\|key"
readelf -h libprodlyjni.so
objdump -d libprodlyjni.so > disassembly.txt
```

**Check for Vulnerabilities:**
```bash
# Check for security issues
checksec --file=libprodlyjni.so
```

### 3. Dynamic Analysis

**Runtime Analysis:**
```bash
# Install debugging tools
sudo apt install -y gdb valgrind strace ltrace

# Monitor system calls
strace -f -e trace=file,network java -jar target/prodly-gui-1.0.0.jar

# Memory leak detection
valgrind --leak-check=full --show-leak-kinds=all java -jar target/prodly-gui-1.0.0.jar
```

### 4. Input Validation Testing

**Fuzz Testing:**
```bash
# Install fuzzing tools
sudo apt install -y afl-fuzz radamsa

# Create test inputs
mkdir -p /tmp/fuzz_inputs
echo "test" > /tmp/fuzz_inputs/input1.txt

# Fuzz test (if CLI interface exists)
# afl-fuzz -i /tmp/fuzz_inputs -o /tmp/fuzz_outputs ./your_binary
```

### 5. Network Security Testing

**Monitor Network Traffic:**
```bash
# Capture network traffic
sudo wireshark &
# Or command line
sudo tcpdump -i any -w capture.pcap

# Analyze with tshark
tshark -r capture.pcap -Y "http or tcp"
```

### 6. JNI Security Testing

**Test JNI Interface:**
```bash
# Check JNI method signatures
javap -s -p com.prodly.ProdlyJNI

# Test with invalid inputs
# Create test script to call JNI methods with:
# - NULL pointers
# - Invalid memory addresses
# - Buffer overflows
# - Integer overflows
```

---

## Testing Tools

### Pre-installed in Kali Linux

1. **Burp Suite** - Web/API security testing
   ```bash
   burpsuite &
   ```

2. **Wireshark** - Network protocol analyzer
   ```bash
   wireshark &
   ```

3. **Nmap** - Network scanning
   ```bash
   nmap -sV -sC localhost
   ```

4. **Metasploit** - Penetration testing framework
   ```bash
   msfconsole
   ```

5. **GDB** - Debugger
   ```bash
   gdb java
   ```

6. **Valgrind** - Memory debugging
   ```bash
   valgrind --tool=memcheck java -jar app.jar
   ```

### Additional Tools to Install

```bash
# Binary analysis
sudo apt install -y radare2 ghidra

# Fuzzing
sudo apt install -y afl-fuzz radamsa

# Reverse engineering
sudo apt install -y ghidra cutter

# Code analysis
sudo apt install -y sonar-scanner
```

---

## Test Scenarios

### 1. Input Validation Tests

**Test Cases:**
- [ ] **Integer Overflow:** Enter extremely large numbers (e.g., 999999999999)
- [ ] **Negative Values:** Enter negative numbers where not expected
- [ ] **String Injection:** Enter SQL-like strings, script tags, special characters
- [ ] **Buffer Overflow:** Enter very long strings (1000+ characters)
- [ ] **NULL/Empty Inputs:** Submit empty forms, NULL values
- [ ] **Special Characters:** Test with `' " ; < > & | \ /`

**Example Test Script:**
```bash
# Create test inputs
cat > /tmp/test_inputs.txt << EOF
-999999
999999999999999
'; DROP TABLE vendors; --
<script>alert('XSS')</script>
../../etc/passwd
EOF

# Test each input
while IFS= read -r input; do
    echo "Testing: $input"
    # Run application with input
done < /tmp/test_inputs.txt
```

### 2. Memory Safety Tests

**Test Cases:**
- [ ] **Memory Leaks:** Run application for extended period, check memory usage
- [ ] **Use-After-Free:** Test rapid create/delete operations
- [ ] **Double Free:** Attempt to free same memory twice
- [ ] **Buffer Overflow:** Test with oversized inputs

**Using Valgrind:**
```bash
valgrind --leak-check=full \
         --show-leak-kinds=all \
         --track-origins=yes \
         --verbose \
         java -Djava.library.path=../cpp_core/build \
              -jar target/prodly-gui-1.0.0.jar
```

### 3. JNI Security Tests

**Test Cases:**
- [ ] **Invalid Pointer Handling:** Pass NULL to JNI methods
- [ ] **Array Bounds:** Access out-of-bounds array indices
- [ ] **Type Confusion:** Pass wrong data types
- [ ] **Exception Handling:** Test exception propagation

**Create Test Java Class:**
```java
// TestJNI.java
public class TestJNI {
    static {
        System.loadLibrary("prodlyjni");
    }
    
    public static void main(String[] args) {
        // Test with NULL
        try {
            nativeMethod(null);
        } catch (Exception e) {
            System.out.println("NULL test: " + e);
        }
        
        // Test with invalid data
        try {
            nativeMethod(new int[]{-1, Integer.MAX_VALUE});
        } catch (Exception e) {
            System.out.println("Invalid data test: " + e);
        }
    }
    
    private native void nativeMethod(int[] data);
}
```

### 4. File System Security Tests

**Test Cases:**
- [ ] **Path Traversal:** Attempt `../../etc/passwd` access
- [ ] **File Permissions:** Check if app creates files with proper permissions
- [ ] **Symlink Attacks:** Test with symbolic links
- [ ] **Race Conditions:** Test concurrent file access

**Test Script:**
```bash
# Test path traversal
cd /tmp
mkdir -p test/../../etc
echo "test" > test/../../etc/passwd_test

# Test symlink
ln -s /etc/passwd symlink_test
```

### 5. Authentication & Authorization Tests

**Test Cases:**
- [ ] **Session Management:** If sessions exist, test timeout, hijacking
- [ ] **Privilege Escalation:** Test if user can access admin functions
- [ ] **Data Access Control:** Verify users can only access their data

### 6. Data Validation Tests

**Test Cases:**
- [ ] **Vendor ID Format:** Test invalid formats, special characters
- [ ] **Contract Value:** Test negative, zero, extremely large values
- [ ] **Date Validation:** Test invalid dates, future dates, past dates
- [ ] **Dependency Cycles:** Test circular dependencies in migration tasks

### 7. Performance & Stress Tests

**Test Cases:**
- [ ] **Load Testing:** Create 1000+ vendors, test performance
- [ ] **Memory Exhaustion:** Test with maximum data volume
- [ ] **CPU Exhaustion:** Test with complex dependency graphs

**Load Test Script:**
```bash
# Create load test
for i in {1..1000}; do
    # Add vendor via API or GUI automation
    echo "Adding vendor $i"
done
```

---

## Automated Testing Script

Create a comprehensive test script:

```bash
#!/bin/bash
# security_test.sh

APP_DIR="/path/to/prodly_v3"
REPORT_DIR="/tmp/security_reports"

mkdir -p $REPORT_DIR

echo "=== Security Testing Report ===" > $REPORT_DIR/report.txt
echo "Date: $(date)" >> $REPORT_DIR/report.txt
echo "" >> $REPORT_DIR/report.txt

# Static Analysis
echo "Running static analysis..."
cd $APP_DIR/cpp_core
cppcheck --enable=all src/ >> $REPORT_DIR/cppcheck.txt 2>&1

# Binary Analysis
echo "Analyzing binary..."
cd $APP_DIR/cpp_core/build
strings libprodlyjni.so | grep -iE "password|secret|key|token" > $REPORT_DIR/strings_analysis.txt

# Memory Testing
echo "Running memory tests..."
valgrind --leak-check=full --log-file=$REPORT_DIR/valgrind.txt \
    java -Djava.library.path=$APP_DIR/cpp_core/build \
         -jar $APP_DIR/java_gui/target/prodly-gui-1.0.0.jar &

sleep 10
pkill -f java

echo "Testing complete. Reports in $REPORT_DIR"
```

---

## Reporting

### Create Test Report Template

```bash
cat > /tmp/test_report_template.md << 'EOF'
# Security Testing Report - Prodly Application

## Test Environment
- **OS:** Kali Linux
- **Date:** [DATE]
- **Tester:** [NAME]

## Test Results

### 1. Static Code Analysis
- **Tool:** cppcheck
- **Findings:** [LIST ISSUES]

### 2. Binary Analysis
- **Tool:** strings, readelf
- **Findings:** [LIST ISSUES]

### 3. Memory Safety
- **Tool:** Valgrind
- **Findings:** [LIST ISSUES]

### 4. Input Validation
- **Test Cases:** [LIST]
- **Findings:** [LIST ISSUES]

### 5. JNI Security
- **Test Cases:** [LIST]
- **Findings:** [LIST ISSUES]

## Recommendations
1. [RECOMMENDATION 1]
2. [RECOMMENDATION 2]

## Severity Classification
- **Critical:** [COUNT]
- **High:** [COUNT]
- **Medium:** [COUNT]
- **Low:** [COUNT]
EOF
```

---

## Best Practices

1. **Isolated Environment:** Always test in isolated VM, never on production
2. **Documentation:** Document all findings with screenshots/logs
3. **Reproducibility:** Ensure all tests are reproducible
4. **Ethical Testing:** Only test applications you own or have permission to test
5. **Backup:** Keep backups before destructive tests
6. **Version Control:** Test specific versions, document versions tested

---

## Troubleshooting

### Common Issues

**Issue: Application won't run in Kali**
```bash
# Check Java version
java -version

# Check library path
export LD_LIBRARY_PATH=/path/to/lib:$LD_LIBRARY_PATH
ldd libprodlyjni.so  # Check dependencies
```

**Issue: Shared folder not accessible**
```bash
# Install VMware tools
sudo apt install -y open-vm-tools open-vm-tools-desktop
sudo reboot
```

**Issue: Network connectivity**
```bash
# Check network settings
ip addr show
ping 8.8.8.8

# Restart network
sudo systemctl restart networking
```

---

## Additional Resources

- **Kali Linux Documentation:** https://www.kali.org/docs/
- **OWASP Testing Guide:** https://owasp.org/www-project-web-security-testing-guide/
- **CWE Top 25:** https://cwe.mitre.org/top25/
- **JNI Best Practices:** https://docs.oracle.com/javase/8/docs/technotes/guides/jni/

---

## Next Steps

1. Set up VMware and Kali Linux
2. Transfer application to Kali
3. Build and run application
4. Execute test scenarios
5. Document findings
6. Create security report
7. Implement fixes based on findings

