/* DO NOT EDIT THIS FILE - it is machine generated */
#include <jni.h>
/* Header for class com_etm_api_jni_DpVCItem */

#ifndef _Included_com_etm_api_jni_DpVCItem
#define _Included_com_etm_api_jni_DpVCItem
#ifdef __cplusplus
extern "C" {
#endif
/*
 * Class:     com_etm_api_jni_DpVCItem
 * Method:    malloc
 * Signature: ()J
 */
JNIEXPORT jlong JNICALL Java_com_etm_api_jni_DpVCItem_malloc
  (JNIEnv *, jobject);

/*
 * Class:     com_etm_api_jni_DpVCItem
 * Method:    free
 * Signature: (J)V
 */
JNIEXPORT void JNICALL Java_com_etm_api_jni_DpVCItem_free
  (JNIEnv *, jobject, jlong);

/*
 * Class:     com_etm_api_jni_DpVCItem
 * Method:    toDebug
 * Signature: (I)Ljava/lang/String;
 */
JNIEXPORT jstring JNICALL Java_com_etm_api_jni_DpVCItem_toDebug
  (JNIEnv *, jobject, jint);

/*
 * Class:     com_etm_api_jni_DpVCItem
 * Method:    getDpIdentifier
 * Signature: ()Lcom/etm/api/var/DpIdentifierVar;
 */
JNIEXPORT jobject JNICALL Java_com_etm_api_jni_DpVCItem_getDpIdentifier
  (JNIEnv *, jobject);

/*
 * Class:     com_etm_api_jni_DpVCItem
 * Method:    setDpIdentifier
 * Signature: (Lcom/etm/api/var/DpIdentifierVar;)Z
 */
JNIEXPORT jboolean JNICALL Java_com_etm_api_jni_DpVCItem_setDpIdentifier
  (JNIEnv *, jobject, jobject);

/*
 * Class:     com_etm_api_jni_DpVCItem
 * Method:    getValue
 * Signature: ()Lcom/etm/api/var/Variable;
 */
JNIEXPORT jobject JNICALL Java_com_etm_api_jni_DpVCItem_getValue
  (JNIEnv *, jobject);

/*
 * Class:     com_etm_api_jni_DpVCItem
 * Method:    setValue
 * Signature: (Lcom/etm/api/var/Variable;)Z
 */
JNIEXPORT jboolean JNICALL Java_com_etm_api_jni_DpVCItem_setValue
  (JNIEnv *, jobject, jobject);

#ifdef __cplusplus
}
#endif
#endif