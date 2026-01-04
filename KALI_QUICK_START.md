# Kali Linux Quick Start Guide

## Quick Setup Checklist

### 1. VMware Setup (5 minutes)
- [ ] Install VMware Workstation Player (free) or Pro
- [ ] Download Kali Linux VM from https://www.kali.org/get-kali/
- [ ] Extract and open `.vmx` file in VMware
- [ ] Power on VM (default: `kali/kali`)

### 2. Initial Kali Setup (10 minutes)
```bash
# Update system
sudo apt update && sudo apt upgrade -y

# Install required tools
sudo apt install -y openjdk-11-jdk maven cmake g++ gcc cppcheck valgrind
```

### 3. Transfer Application (5 minutes)

**Option A: Shared Folder (Easiest)**
1. VMware: VM → Settings → Options → Shared Folders
2. Add folder: `C:\Users\hp\Desktop\prodly_v3`
3. In Kali:
```bash
sudo mkdir -p /mnt/hgfs
sudo vmhgfs-fuse .host:/ /mnt/hgfs -o subtype=vmhgfs-fuse,allow_other
cd /mnt/hgfs/prodly_v3
```

**Option B: Network Share**
```bash
# Find Windows IP (from Windows PowerShell)
ipconfig

# In Kali, mount Windows share
sudo mkdir /mnt/windows
sudo mount -t cifs //WINDOWS_IP/prodly_v3 /mnt/windows -o username=YOUR_USER
```

### 4. Build Application (5 minutes)
```bash
cd /path/to/prodly_v3

# Build C++ core
cd cpp_core
mkdir -p build && cd build
cmake .. -DCMAKE_BUILD_TYPE=Release
make -j4

# Build Java GUI
cd ../../java_gui
mvn clean package
```

### 5. Run Security Tests (10 minutes)
```bash
# Make script executable
chmod +x security_test_script.sh

# Run automated tests
./security_test_script.sh /path/to/prodly_v3

# View results
cat /tmp/prodly_security_reports_*/report.txt
```

## Common Commands

### Run Application
```bash
cd java_gui
export LD_LIBRARY_PATH=../cpp_core/build:$LD_LIBRARY_PATH
java -Djava.library.path=../cpp_core/build -jar target/prodly-gui-1.0.0.jar
```

### Static Analysis
```bash
# C++ analysis
cd cpp_core
cppcheck --enable=all src/

# Binary analysis
strings build/libprodlyjni.so | grep -i "password\|secret"
```

### Memory Testing
```bash
valgrind --leak-check=full \
         java -Djava.library.path=../cpp_core/build \
              -jar target/prodly-gui-1.0.0.jar
```

### Network Monitoring
```bash
# Monitor connections
sudo netstat -tulpn | grep java

# Capture traffic
sudo wireshark &
```

## Troubleshooting

**Can't access shared folder?**
```bash
sudo apt install -y open-vm-tools open-vm-tools-desktop
sudo reboot
```

**Application won't run?**
```bash
# Check Java
java -version

# Check library
ldd cpp_core/build/libprodlyjni.so

# Set library path
export LD_LIBRARY_PATH=$(pwd)/cpp_core/build:$LD_LIBRARY_PATH
```

**No internet in Kali?**
```bash
# Check network
ip addr show

# Restart network
sudo systemctl restart networking
```

## Test Scenarios Quick Reference

1. **Input Validation:** Test with negative numbers, huge values, special chars
2. **Memory Safety:** Run Valgrind, check for leaks
3. **Binary Analysis:** Check for hardcoded secrets, analyze strings
4. **JNI Security:** Test NULL pointers, invalid inputs
5. **File System:** Test path traversal, permissions

## Full Documentation

See `SECURITY_TESTING_KALI.md` for complete guide.



