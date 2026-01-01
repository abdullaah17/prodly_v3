package com.prodly;

import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;
import java.util.List;

/**
 * JNI wrapper for VendorLockInAnalyzer C++ class
 */
public class VendorLockInAnalyzerJNI {
    private static boolean libraryLoaded = false;
    
    // In-memory storage for demo mode
    private static class VendorData {
        String vendorId;
        String vendorName;
        double contractValue;
        int contractMonths;
        double dataVolumeGB;
        int apiDependencies;
        boolean hasCustomIntegration;
        double switchingCost;
        double lockInScore;
        
        VendorData(String id, String name, double contractVal, int months,
                   double dataVol, int apiDeps, boolean customIntegration,
                   double switchingCost) {
            this.vendorId = id;
            this.vendorName = name;
            this.contractValue = contractVal;
            this.contractMonths = months;
            this.dataVolumeGB = dataVol;
            this.apiDependencies = apiDeps;
            this.hasCustomIntegration = customIntegration;
            this.switchingCost = switchingCost;
            this.lockInScore = calculateDemoScore();
        }
        
        private double calculateDemoScore() {
            // Demo calculation similar to C++ logic
            double score = 0.0;
            
            // Contract value factor (0-25 points)
            score += Math.min(25.0, (contractValue / 1000000.0) * 5.0);
            
            // Contract duration factor (0-20 points)
            score += Math.min(20.0, (contractMonths / 36.0) * 20.0);
            
            // Data volume factor (0-15 points)
            score += Math.min(15.0, (dataVolumeGB / 1000.0) * 15.0);
            
            // API dependencies factor (0-15 points)
            score += Math.min(15.0, (apiDependencies / 10.0) * 15.0);
            
            // Custom integration factor (0-10 points)
            if (hasCustomIntegration) {
                score += 10.0;
            }
            
            // Switching cost factor (0-15 points)
            if (contractValue > 0) {
                score += Math.min(15.0, (switchingCost / contractValue) * 15.0);
            }
            
            return Math.min(100.0, Math.max(0.0, score));
        }
    }
    
    private static Map<String, VendorData> demoVendorStorage = new HashMap<>();
    
    static {
        try {
            System.loadLibrary("prodlyjni"); // Load native library
            libraryLoaded = true;
        } catch (UnsatisfiedLinkError e) {
            libraryLoaded = false;
            System.err.println("Warning: Native library 'prodlyjni' not found. Running in demo mode.");
            System.err.println("To use full functionality, build the C++ library with CMake first.");
        }
    }

    private native long createNativeObject();
    private native void addVendor(long nativePtr, String vendorId, String vendorName,
                                  double contractValue, int contractMonths,
                                  double dataVolumeGB, int apiDependencies,
                                  boolean hasCustomIntegration, double switchingCost);
    private native double calculateLockInScore(long nativePtr, String vendorId);
    private native String[][] getAllScores(long nativePtr);
    private native void deleteNativeObject(long nativePtr);

    private long nativePtr;

    public VendorLockInAnalyzerJNI() {
        if (libraryLoaded) {
            nativePtr = createNativeObject();
        } else {
            nativePtr = 0;
            // Initialize with sample data for demo mode
            initializeSampleData();
        }
    }
    
    private void initializeSampleData() {
        // Only add sample data if storage is empty (first instance)
        if (demoVendorStorage.isEmpty()) {
            // Sample Vendor 1: High lock-in risk (AWS)
            addVendor("AWS-001", "Amazon Web Services", 
                     500000.0,  // Contract Value: $500k
                     36,        // 36 months
                     15000.0,   // 15,000 GB data
                     25,        // 25 API dependencies
                     true,      // Custom integration
                     75000.0);  // $75k switching cost
            
            // Sample Vendor 2: Medium lock-in risk (Salesforce)
            addVendor("SF-001", "Salesforce CRM", 
                     250000.0,  // Contract Value: $250k
                     24,        // 24 months
                     5000.0,    // 5,000 GB data
                     12,        // 12 API dependencies
                     false,     // Standard integration
                     30000.0);  // $30k switching cost
            
            // Sample Vendor 3: Low lock-in risk (Microsoft Office 365)
            addVendor("O365-001", "Microsoft Office 365", 
                     50000.0,   // Contract Value: $50k
                     12,        // 12 months
                     2000.0,    // 2,000 GB data
                     5,         // 5 API dependencies
                     false,     // Standard integration
                     5000.0);   // $5k switching cost
            
            // Sample Vendor 4: High lock-in risk (Oracle Database)
            addVendor("ORC-001", "Oracle Database Enterprise", 
                     800000.0,  // Contract Value: $800k
                     60,        // 60 months (5 years)
                     50000.0,   // 50,000 GB data
                     40,        // 40 API dependencies
                     true,      // Custom integration
                     150000.0); // $150k switching cost
            
            System.out.println("Demo mode: Loaded " + demoVendorStorage.size() + " sample vendors");
        }
    }

    public void addVendor(String vendorId, String vendorName, double contractValue,
                         int contractMonths, double dataVolumeGB, int apiDependencies,
                         boolean hasCustomIntegration, double switchingCost) {
        if (!libraryLoaded) {
            // Store in demo mode
            VendorData vendor = new VendorData(vendorId, vendorName, contractValue,
                                             contractMonths, dataVolumeGB, apiDependencies,
                                             hasCustomIntegration, switchingCost);
            demoVendorStorage.put(vendorId, vendor);
            System.out.println("Demo mode: Vendor stored: " + vendorId);
        } else {
            addVendor(nativePtr, vendorId, vendorName, contractValue, contractMonths,
                     dataVolumeGB, apiDependencies, hasCustomIntegration, switchingCost);
        }
    }

    public double calculateLockInScore(String vendorId) {
        if (!libraryLoaded) {
            // Return score from demo storage
            VendorData vendor = demoVendorStorage.get(vendorId);
            if (vendor != null) {
                return vendor.lockInScore;
            }
            // Return a demo score based on vendor ID hash if not found
            return Math.abs(vendorId.hashCode() % 100);
        }
        return calculateLockInScore(nativePtr, vendorId);
    }

    public String[][] getAllScores() {
        if (!libraryLoaded) {
            // Return scores from demo storage
            List<String[]> result = new ArrayList<>();
            for (VendorData vendor : demoVendorStorage.values()) {
                result.add(new String[]{
                    vendor.vendorId,
                    String.valueOf(vendor.lockInScore),
                    vendor.vendorName  // Include vendor name
                });
            }
            return result.toArray(new String[result.size()][]);
        }
        return getAllScores(nativePtr);
    }

    @Override
    protected void finalize() throws Throwable {
        if (libraryLoaded && nativePtr != 0) {
            deleteNativeObject(nativePtr);
            nativePtr = 0;
        }
        super.finalize();
    }
}
