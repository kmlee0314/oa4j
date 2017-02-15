#ifndef _JavaEXTERNHDL_H_
#define _JavaEXTERNHDL_H_

#include <BaseExternHdl.hxx>
#include <DpIdentifier.hxx>   
#include <jni.h>


class JavaExternHdl : public BaseExternHdl
{
public:
	JavaExternHdl(BaseExternHdl *nextHdl, PVSSulong funcCount, FunctionListRec fnList[])
		: BaseExternHdl(nextHdl, funcCount, fnList) {}

	virtual const Variable *execute(ExecuteParamRec &param);

	static const char *ManagerName;
	static const bool DEBUG;

	static const char *ExternHdlClassName;
	static const char *DpGetPeriodClassName;
	static const char *DpGetPeriodResultClassName;

	static jclass clsDpGetPeriod;
	static jclass clsDpGetPeriodResult;

private:

	JavaVM *jvm;                      // Pointer to the JVM (Java Virtual Machine)
	JNIEnv *env;                      // Pointer to native interface
	jint jvmState = JNI_ABORT;

	const Variable* startVM(ExecuteParamRec &param);
	const Variable* stopVM(ExecuteParamRec &param);
	const Variable* dpGetPeriod(ExecuteParamRec &param);
	const Variable* xxx(ExecuteParamRec &param);
};

JNIEXPORT jstring JNICALL Java_com_etm_net_client_ExternHdl_apiGetLogDir
(JNIEnv *, jclass);

JNIEXPORT jint JNICALL Java_com_etm_net_client_ExternHdl_apiGetManType
(JNIEnv *, jclass);

JNIEXPORT jint JNICALL Java_com_etm_net_client_ExternHdl_apiGetManNum
(JNIEnv *, jclass);


JNIEXPORT jint JNICALL Java_com_etm_net_client_ExternHdl_apiReadChunk
(JNIEnv *env, jclass, jlong jWaitCondPtr, jobject jChunk, jstring jDpName, jint jDpNr, jlong jTAPtr, jlong jXAPtr);

#endif
