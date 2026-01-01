package com.prodly;

import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;
import java.util.List;
import java.util.Collections;
import java.util.Comparator;

/**
 * JNI wrapper for ExitReadinessDashboard C++ class
 */
public class ExitReadinessDashboardJNI {
    private static boolean libraryLoaded = false;
    
    // In-memory storage for demo mode
    private static class VendorReadinessData {
        String vendorId;
        double lockInScore;
        double migrationDifficulty;
        int dataExportCapability;
        int contractFlexibility;
        int technicalComplexity;
        double exitReadiness;
        
        VendorReadinessData(String id, double lockIn, double migration, 
                           int dataExport, int contractFlex, int techComplexity) {
            this.vendorId = id;
            this.lockInScore = lockIn;
            this.migrationDifficulty = migration;
            this.dataExportCapability = dataExport;
            this.contractFlexibility = contractFlex;
            this.technicalComplexity = techComplexity;
            this.exitReadiness = calculateReadiness();
        }
        
        private double calculateReadiness() {
            // Calculate exit readiness (inverse factors)
            double readiness = 0.0;
            
            // Lock-in factor (inverse: lower lock-in = higher readiness) (0-30 points)
            readiness += (100.0 - lockInScore) * 0.3;
            
            // Migration difficulty factor (inverse: easier = higher readiness) (0-25 points)
            readiness += (100.0 - migrationDifficulty) * 0.25;
            
            // Data export capability (0-20 points)
            readiness += dataExportCapability * 0.2;
            
            // Contract flexibility (0-15 points)
            readiness += contractFlexibility * 0.15;
            
            // Technical complexity (inverse: simpler = higher readiness) (0-10 points)
            readiness += (100.0 - technicalComplexity) * 0.1;
            
            return Math.min(100.0, Math.max(0.0, readiness));
        }
    }
    
    private static Map<String, VendorReadinessData> demoReadinessStorage = new HashMap<>();
    
    static {
        try {
            System.loadLibrary("prodlyjni");
            libraryLoaded = true;
        } catch (UnsatisfiedLinkError e) {
            libraryLoaded = false;
        }
    }

    private native long createNativeObject();
    private native void addVendorMetrics(long nativePtr, String vendorId,
                                        double lockInScore, double migrationDifficulty,
                                        int dataExportCapability, int contractFlexibility,
                                        int technicalComplexity);
    private native double calculateExitReadiness(long nativePtr, String vendorId);
    private native String[][] getVendorsSortedByReadiness(long nativePtr);
    private native void deleteNativeObject(long nativePtr);

    private long nativePtr;

    public ExitReadinessDashboardJNI() {
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
        if (demoReadinessStorage.isEmpty()) {
            // Sample data based on Module 1 vendors
            // AWS-001: High lock-in (85), High migration difficulty (75), Low export (30), Low flexibility (20), High complexity (80)
            addVendorMetrics("AWS-001", 85.0, 75.0, 30, 20, 80);
            
            // SF-001: Medium lock-in (50), Medium migration (55), Medium export (60), Medium flexibility (50), Medium complexity (60)
            addVendorMetrics("SF-001", 50.0, 55.0, 60, 50, 60);
            
            // O365-001: Low lock-in (25), Low migration (30), High export (90), High flexibility (85), Low complexity (30)
            addVendorMetrics("O365-001", 25.0, 30.0, 90, 85, 30);
            
            // ORC-001: Very high lock-in (95), Very high migration (90), Very low export (20), Very low flexibility (10), Very high complexity (95)
            addVendorMetrics("ORC-001", 95.0, 90.0, 20, 10, 95);
            
            System.out.println("Demo mode: Loaded " + demoReadinessStorage.size() + " sample readiness records");
        }
    }

    public void addVendorMetrics(String vendorId, double lockInScore,
                                double migrationDifficulty, int dataExportCapability,
                                int contractFlexibility, int technicalComplexity) {
        if (!libraryLoaded) {
            // Store in demo mode
            VendorReadinessData data = new VendorReadinessData(vendorId, lockInScore,
                                                              migrationDifficulty, dataExportCapability,
                                                              contractFlexibility, technicalComplexity);
            demoReadinessStorage.put(vendorId, data);
            System.out.println("Demo mode: Readiness metrics stored for: " + vendorId);
        } else {
            addVendorMetrics(nativePtr, vendorId, lockInScore, migrationDifficulty,
                            dataExportCapability, contractFlexibility, technicalComplexity);
        }
    }

    public double calculateExitReadiness(String vendorId) {
        if (!libraryLoaded) {
            VendorReadinessData data = demoReadinessStorage.get(vendorId);
            if (data != null) {
                return data.exitReadiness;
            }
            return 50.0; // Default value
        }
        return calculateExitReadiness(nativePtr, vendorId);
    }

    public String[][] getVendorsSortedByReadiness() {
        if (!libraryLoaded) {
            // Return vendors sorted by readiness (highest first) - using AVL tree logic (sorted map)
            List<VendorReadinessData> vendors = new ArrayList<>(demoReadinessStorage.values());
            
            // Sort by exit readiness descending (highest readiness first)
            Collections.sort(vendors, new Comparator<VendorReadinessData>() {
                @Override
                public int compare(VendorReadinessData a, VendorReadinessData b) {
                    return Double.compare(b.exitReadiness, a.exitReadiness); // Descending
                }
            });
            
            List<String[]> result = new ArrayList<>();
            for (VendorReadinessData vendor : vendors) {
                result.add(new String[]{
                    vendor.vendorId,
                    String.valueOf(vendor.exitReadiness)
                });
            }
            return result.toArray(new String[result.size()][]);
        }
        return getVendorsSortedByReadiness(nativePtr);
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
