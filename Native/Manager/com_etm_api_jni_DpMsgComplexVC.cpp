#include <com_etm_api_jni_DpMsgComplexVC.h>
#include <WCCOAJavaManager.hxx>
#include <../LibJava/Java.hxx>

/*
* Class:     com_etm_api_jni_DpMsgComplexVC
* Method:    getFirstGroup
* Signature: ()Lcom/etm/api/jni/DpVCGroup;
*/
JNIEXPORT jobject JNICALL Java_com_etm_api_jni_DpMsgComplexVC_getFirstGroup
(JNIEnv *env, jobject obj)
{
	jclass cls;
	cls = env->GetObjectClass(obj);
	jlong cptr = env->GetLongField(obj, env->GetFieldID(cls, "cptr", "J"));
	env->DeleteLocalRef(cls);

	DpMsgComplexVC *list = (DpMsgComplexVC*)cptr;
	DpVCGroup *item = list->getFirstGroup();

	if (item == nil) return nil;

	cls = env->FindClass("com/etm/api/jni/DpVCGroup");
	jmethodID mid = env->GetMethodID(cls, "<init>", "(J)V");
	jobject jitem = env->NewObject(cls, mid, (jlong)item);
	env->DeleteLocalRef(cls);

	return jitem;
}

/*
* Class:     com_etm_api_jni_DpMsgComplexVC
* Method:    getNextGroup
* Signature: ()Lcom/etm/api/jni/DpVCGroup;
*/
JNIEXPORT jobject JNICALL Java_com_etm_api_jni_DpMsgComplexVC_getNextGroup
(JNIEnv *env, jobject obj)
{
	jclass cls;
	cls = env->GetObjectClass(obj);
	jlong cptr = env->GetLongField(obj, env->GetFieldID(cls, "cptr", "J"));
	env->DeleteLocalRef(cls);

	DpMsgComplexVC *list = (DpMsgComplexVC*)cptr;
	DpVCGroup *item = list->getNextGroup();

	if (item == nil) return nil;

	cls = env->FindClass("com/etm/api/jni/DpVCGroup");
	jmethodID mid = env->GetMethodID(cls, "<init>", "(J)V");
	jobject jitem = env->NewObject(cls, mid, (jlong)item);
	env->DeleteLocalRef(cls);

	return jitem;
}


/*
* Class:     com_etm_api_jni_DpMsgComplexVC
* Method:    getLastGroup
* Signature: ()Lcom/etm/api/jni/DpVCGroup;
*/
JNIEXPORT jobject JNICALL Java_com_etm_api_jni_DpMsgComplexVC_getLastGroup
(JNIEnv *env, jobject obj)
{
	jclass cls;
	cls = env->GetObjectClass(obj);
	jlong cptr = env->GetLongField(obj, env->GetFieldID(cls, "cptr", "J"));
	env->DeleteLocalRef(cls);

	DpMsgComplexVC *list = (DpMsgComplexVC*)cptr;
	DpVCGroup *item = list->getLastGroup();

	if (item == nil) return nil;

	cls = env->FindClass("com/etm/api/jni/DpVCGroup");
	jmethodID mid = env->GetMethodID(cls, "<init>", "(J)V");
	jobject jitem = env->NewObject(cls, mid, (jlong)item);
	env->DeleteLocalRef(cls);

	return jitem;
}

/*
* Class:     com_etm_api_jni_DpMsgComplexVC
* Method:    getNrOfGroups
* Signature: ()I
*/
JNIEXPORT jint JNICALL Java_com_etm_api_jni_DpMsgComplexVC_getNrOfGroups
(JNIEnv *env, jobject obj)
{
	jclass cls;
	cls = env->GetObjectClass(obj);
	jlong cptr = env->GetLongField(obj, env->GetFieldID(cls, "cptr", "J"));
	env->DeleteLocalRef(cls);

	DpMsgComplexVC *list = (DpMsgComplexVC*)cptr;
	return list->getNrOfGroups();	
}

JNIEXPORT jstring JNICALL Java_com_etm_api_jni_DpMsgComplexVC_toDebug
(JNIEnv *env, jobject obj, jint level)
{
	jclass cls = env->GetObjectClass(obj);
	DpMsgComplexVC *msg = (DpMsgComplexVC*)env->GetLongField(obj, env->GetFieldID(cls, "cptr", "J"));
	env->DeleteLocalRef(cls);

	std::ostringstream stream;
	msg->debug(stream, level);
	std::string str = stream.str();

	jstring jstr = env->NewStringUTF(str.c_str());
	return jstr;
}