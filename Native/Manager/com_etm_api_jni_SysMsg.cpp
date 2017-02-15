#include <com_etm_api_jni_SysMsg.h>
#include <WCCOAJavaManager.hxx>

JNIEXPORT jint JNICALL Java_com_etm_api_jni_SysMsg_getSysMsgType
(JNIEnv *, jobject, jlong cptr)
{
	SysMsg *v = (SysMsg*)(cptr);
	return v->getSysMsgType();
}