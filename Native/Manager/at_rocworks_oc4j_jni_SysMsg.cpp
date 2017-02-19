#include <at_rocworks_oc4j_jni_SysMsg.h>
#include <WCCOAJavaManager.hxx>

JNIEXPORT jint JNICALL Java_at_rocworks_oc4j_jni_SysMsg_getSysMsgType
(JNIEnv *, jobject, jlong cptr)
{
	SysMsg *v = (SysMsg*)(cptr);
	return v->getSysMsgType();
}