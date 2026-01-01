#include "../include/VendorLockInAnalyzer.h"
#include "../include/MigrationDifficultyAnalyzer.h"
#include "../include/ExitReadinessDashboard.h"
#include "ProdlyJNI.h"
#include <string>
#include <vector>

// Helper function to convert jstring to std::string
std::string jstringToString(JNIEnv* env, jstring jstr) {
    if (jstr == nullptr) return "";
    const char* chars = env->GetStringUTFChars(jstr, nullptr);
    std::string str(chars);
    env->ReleaseStringUTFChars(jstr, chars);
    return str;
}

// Helper function to create Java String array
jobjectArray createStringArray(JNIEnv* env, const std::vector<std::string>& strings) {
    jclass stringClass = env->FindClass("java/lang/String");
    jobjectArray result = env->NewObjectArray(strings.size(), stringClass, nullptr);
    for (size_t i = 0; i < strings.size(); i++) {
        jstring jstr = env->NewStringUTF(strings[i].c_str());
        env->SetObjectArrayElement(result, i, jstr);
        env->DeleteLocalRef(jstr);
    }
    return result;
}

// Module 1: Vendor Lock-In Analyzer JNI
JNIEXPORT jlong JNICALL Java_com_prodly_VendorLockInAnalyzerJNI_createNativeObject(JNIEnv* env, jobject obj) {
    VendorLockInAnalyzer* analyzer = new VendorLockInAnalyzer();
    return reinterpret_cast<jlong>(analyzer);
}

JNIEXPORT void JNICALL Java_com_prodly_VendorLockInAnalyzerJNI_addVendor(JNIEnv* env, jobject obj,
        jlong nativePtr, jstring vendorId, jstring vendorName, jdouble contractValue,
        jint contractMonths, jdouble dataVolumeGB, jint apiDependencies,
        jboolean hasCustomIntegration, jdouble switchingCost) {
    VendorLockInAnalyzer* analyzer = reinterpret_cast<VendorLockInAnalyzer*>(nativePtr);
    std::string id = jstringToString(env, vendorId);
    std::string name = jstringToString(env, vendorName);
    
    analyzer->addVendor(id, name, contractValue, contractMonths, dataVolumeGB,
                       apiDependencies, hasCustomIntegration == JNI_TRUE, switchingCost);
}

JNIEXPORT jdouble JNICALL Java_com_prodly_VendorLockInAnalyzerJNI_calculateLockInScore(JNIEnv* env, jobject obj, jlong nativePtr, jstring vendorId) {
    VendorLockInAnalyzer* analyzer = reinterpret_cast<VendorLockInAnalyzer*>(nativePtr);
    std::string id = jstringToString(env, vendorId);
    return analyzer->calculateLockInScore(id);
}

JNIEXPORT jobjectArray JNICALL Java_com_prodly_VendorLockInAnalyzerJNI_getAllScores(JNIEnv* env, jobject obj, jlong nativePtr) {
    VendorLockInAnalyzer* analyzer = reinterpret_cast<VendorLockInAnalyzer*>(nativePtr);
    auto scores = analyzer->getAllScores();
    
    // Create array of String arrays (vendorId, score)
    jclass stringArrayClass = env->FindClass("[Ljava/lang/String;");
    jobjectArray result = env->NewObjectArray(scores.size(), stringArrayClass, nullptr);
    
    for (size_t i = 0; i < scores.size(); i++) {
        jobjectArray pair = env->NewObjectArray(2, env->FindClass("java/lang/String"), nullptr);
        env->SetObjectArrayElement(pair, 0, env->NewStringUTF(scores[i].first.c_str()));
        
        std::string scoreStr = std::to_string(scores[i].second);
        env->SetObjectArrayElement(pair, 1, env->NewStringUTF(scoreStr.c_str()));
        env->SetObjectArrayElement(result, i, pair);
        env->DeleteLocalRef(pair);
    }
    
    return result;
}

JNIEXPORT void JNICALL Java_com_prodly_VendorLockInAnalyzerJNI_deleteNativeObject(JNIEnv* env, jobject obj, jlong nativePtr) {
    VendorLockInAnalyzer* analyzer = reinterpret_cast<VendorLockInAnalyzer*>(nativePtr);
    delete analyzer;
}

// Module 2: Migration Difficulty Analyzer JNI
JNIEXPORT jlong JNICALL Java_com_prodly_MigrationDifficultyAnalyzerJNI_createNativeObject(JNIEnv* env, jobject obj) {
    MigrationDifficultyAnalyzer* analyzer = new MigrationDifficultyAnalyzer();
    return reinterpret_cast<jlong>(analyzer);
}

JNIEXPORT void JNICALL Java_com_prodly_MigrationDifficultyAnalyzerJNI_addTask(JNIEnv* env, jobject obj,
        jlong nativePtr, jstring taskId, jstring taskName, jint difficulty,
        jint estimatedDays, jobjectArray dependencies) {
    MigrationDifficultyAnalyzer* analyzer = reinterpret_cast<MigrationDifficultyAnalyzer*>(nativePtr);
    std::string id = jstringToString(env, taskId);
    std::string name = jstringToString(env, taskName);
    
    std::vector<std::string> deps;
    if (dependencies != nullptr) {
        jsize length = env->GetArrayLength(dependencies);
        for (jsize i = 0; i < length; i++) {
            jstring dep = (jstring)env->GetObjectArrayElement(dependencies, i);
            deps.push_back(jstringToString(env, dep));
            env->DeleteLocalRef(dep);
        }
    }
    
    analyzer->addTask(id, name, difficulty, estimatedDays, deps);
}

JNIEXPORT jdouble JNICALL Java_com_prodly_MigrationDifficultyAnalyzerJNI_calculateMigrationDifficulty(JNIEnv* env, jobject obj, jlong nativePtr, jstring vendorId) {
    MigrationDifficultyAnalyzer* analyzer = reinterpret_cast<MigrationDifficultyAnalyzer*>(nativePtr);
    std::string id = jstringToString(env, vendorId);
    return analyzer->calculateMigrationDifficulty(id);
}

JNIEXPORT jobjectArray JNICALL Java_com_prodly_MigrationDifficultyAnalyzerJNI_getOptimalMigrationSequence(JNIEnv* env, jobject obj, jlong nativePtr, jstring vendorId) {
    MigrationDifficultyAnalyzer* analyzer = reinterpret_cast<MigrationDifficultyAnalyzer*>(nativePtr);
    std::string id = jstringToString(env, vendorId);
    auto sequence = analyzer->getOptimalMigrationSequence(id);
    return createStringArray(env, sequence);
}

JNIEXPORT jint JNICALL Java_com_prodly_MigrationDifficultyAnalyzerJNI_getTotalMigrationDays(JNIEnv* env, jobject obj, jlong nativePtr, jstring vendorId) {
    MigrationDifficultyAnalyzer* analyzer = reinterpret_cast<MigrationDifficultyAnalyzer*>(nativePtr);
    std::string id = jstringToString(env, vendorId);
    return analyzer->getTotalMigrationDays(id);
}

JNIEXPORT void JNICALL Java_com_prodly_MigrationDifficultyAnalyzerJNI_deleteNativeObject(JNIEnv* env, jobject obj, jlong nativePtr) {
    MigrationDifficultyAnalyzer* analyzer = reinterpret_cast<MigrationDifficultyAnalyzer*>(nativePtr);
    delete analyzer;
}

// Module 3: Exit Readiness Dashboard JNI
JNIEXPORT jlong JNICALL Java_com_prodly_ExitReadinessDashboardJNI_createNativeObject(JNIEnv* env, jobject obj) {
    ExitReadinessDashboard* dashboard = new ExitReadinessDashboard();
    return reinterpret_cast<jlong>(dashboard);
}

JNIEXPORT void JNICALL Java_com_prodly_ExitReadinessDashboardJNI_addVendorMetrics(JNIEnv* env, jobject obj,
        jlong nativePtr, jstring vendorId, jdouble lockInScore, jdouble migrationDifficulty,
        jint dataExportCapability, jint contractFlexibility, jint technicalComplexity) {
    ExitReadinessDashboard* dashboard = reinterpret_cast<ExitReadinessDashboard*>(nativePtr);
    std::string id = jstringToString(env, vendorId);
    
    dashboard->addVendorMetrics(id, lockInScore, migrationDifficulty,
                               dataExportCapability, contractFlexibility, technicalComplexity);
}

JNIEXPORT jdouble JNICALL Java_com_prodly_ExitReadinessDashboardJNI_calculateExitReadiness(JNIEnv* env, jobject obj, jlong nativePtr, jstring vendorId) {
    ExitReadinessDashboard* dashboard = reinterpret_cast<ExitReadinessDashboard*>(nativePtr);
    std::string id = jstringToString(env, vendorId);
    return dashboard->calculateExitReadiness(id);
}

JNIEXPORT jobjectArray JNICALL Java_com_prodly_ExitReadinessDashboardJNI_getVendorsSortedByReadiness(JNIEnv* env, jobject obj, jlong nativePtr) {
    ExitReadinessDashboard* dashboard = reinterpret_cast<ExitReadinessDashboard*>(nativePtr);
    auto vendors = dashboard->getVendorsSortedByReadiness();
    
    jclass stringArrayClass = env->FindClass("[Ljava/lang/String;");
    jobjectArray result = env->NewObjectArray(vendors.size(), stringArrayClass, nullptr);
    
    for (size_t i = 0; i < vendors.size(); i++) {
        jobjectArray pair = env->NewObjectArray(2, env->FindClass("java/lang/String"), nullptr);
        env->SetObjectArrayElement(pair, 0, env->NewStringUTF(vendors[i].first.c_str()));
        std::string scoreStr = std::to_string(vendors[i].second);
        env->SetObjectArrayElement(pair, 1, env->NewStringUTF(scoreStr.c_str()));
        env->SetObjectArrayElement(result, i, pair);
        env->DeleteLocalRef(pair);
    }
    
    return result;
}

JNIEXPORT void JNICALL Java_com_prodly_ExitReadinessDashboardJNI_deleteNativeObject(JNIEnv* env, jobject obj, jlong nativePtr) {
    ExitReadinessDashboard* dashboard = reinterpret_cast<ExitReadinessDashboard*>(nativePtr);
    delete dashboard;
}

