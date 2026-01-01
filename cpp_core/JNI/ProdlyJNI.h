#ifndef PRODLY_JNI_H
#define PRODLY_JNI_H

#include <jni.h>

#ifdef __cplusplus
extern "C" {
#endif

// Module 1: Vendor Lock-In Analyzer
JNIEXPORT jlong JNICALL Java_com_prodly_VendorLockInAnalyzerJNI_createNativeObject(JNIEnv *, jobject);
JNIEXPORT void JNICALL Java_com_prodly_VendorLockInAnalyzerJNI_addVendor(JNIEnv *, jobject, jlong, jstring, jstring, jdouble, jint, jdouble, jint, jboolean, jdouble);
JNIEXPORT jdouble JNICALL Java_com_prodly_VendorLockInAnalyzerJNI_calculateLockInScore(JNIEnv *, jobject, jlong, jstring);
JNIEXPORT jobjectArray JNICALL Java_com_prodly_VendorLockInAnalyzerJNI_getAllScores(JNIEnv *, jobject, jlong);
JNIEXPORT void JNICALL Java_com_prodly_VendorLockInAnalyzerJNI_deleteNativeObject(JNIEnv *, jobject, jlong);

// Module 2: Migration Difficulty Analyzer
JNIEXPORT jlong JNICALL Java_com_prodly_MigrationDifficultyAnalyzerJNI_createNativeObject(JNIEnv *, jobject);
JNIEXPORT void JNICALL Java_com_prodly_MigrationDifficultyAnalyzerJNI_addTask(JNIEnv *, jobject, jlong, jstring, jstring, jint, jint, jobjectArray);
JNIEXPORT jdouble JNICALL Java_com_prodly_MigrationDifficultyAnalyzerJNI_calculateMigrationDifficulty(JNIEnv *, jobject, jlong, jstring);
JNIEXPORT jobjectArray JNICALL Java_com_prodly_MigrationDifficultyAnalyzerJNI_getOptimalMigrationSequence(JNIEnv *, jobject, jlong, jstring);
JNIEXPORT jint JNICALL Java_com_prodly_MigrationDifficultyAnalyzerJNI_getTotalMigrationDays(JNIEnv *, jobject, jlong, jstring);
JNIEXPORT void JNICALL Java_com_prodly_MigrationDifficultyAnalyzerJNI_deleteNativeObject(JNIEnv *, jobject, jlong);

// Module 3: Exit Readiness Dashboard
JNIEXPORT jlong JNICALL Java_com_prodly_ExitReadinessDashboardJNI_createNativeObject(JNIEnv *, jobject);
JNIEXPORT void JNICALL Java_com_prodly_ExitReadinessDashboardJNI_addVendorMetrics(JNIEnv *, jobject, jlong, jstring, jdouble, jdouble, jint, jint, jint);
JNIEXPORT jdouble JNICALL Java_com_prodly_ExitReadinessDashboardJNI_calculateExitReadiness(JNIEnv *, jobject, jlong, jstring);
JNIEXPORT jobjectArray JNICALL Java_com_prodly_ExitReadinessDashboardJNI_getVendorsSortedByReadiness(JNIEnv *, jobject, jlong);
JNIEXPORT void JNICALL Java_com_prodly_ExitReadinessDashboardJNI_deleteNativeObject(JNIEnv *, jobject, jlong);

#ifdef __cplusplus
}
#endif

#endif // PRODLY_JNI_H

