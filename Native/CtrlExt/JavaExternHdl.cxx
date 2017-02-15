#include <../LibJava/Java.hxx>

#include <JavaExternHdl.hxx>
#include <JavaResources.hxx>
#include <Manager.hxx>     
#include <WaitCond.hxx>

#include <DpIdentifierVar.hxx>
#include <DpVCItem.hxx>               
#include <Variable.hxx>
#include <FloatVar.hxx>
#include <IntegerVar.hxx>
#include <CharVar.hxx>
#include <ULongVar.hxx>
#include <TimeVar.hxx>
#include <AnyTypeVar.hxx>
#include <DynVar.hxx>
#include <UIntegerVar.hxx>

const bool JavaExternHdl::DEBUG = false;

const char *JavaExternHdl::ManagerName = "WCCOAjava";

const char *JavaExternHdl::ExternHdlClassName = "com/etm/net/client/ExternHdl";
const char *JavaExternHdl::DpGetPeriodClassName = "com/etm/net/client/DpGetPeriod";
const char *JavaExternHdl::DpGetPeriodResultClassName = "com/etm/net/server/DpGetPeriodResultJava";


jclass JavaExternHdl::clsDpGetPeriod;
jclass JavaExternHdl::clsDpGetPeriodResult;


//------------------------------------------------------------------------------

static FunctionListRec fnList[] =
{
	// TODO add for every new function an entry
	{ INTEGER_VAR, "startJVM", "()", false },
	{ INTEGER_VAR, "stopJVM", "()", false },
	{ INTEGER_VAR, "dpGetPeriod", "(time t1, time t2, int count, ..)", false },
	{ INTEGER_VAR, "xxx", "(time t1, time t2, int count, ..)", false }
};

CTRL_EXTENSION(JavaExternHdl, fnList)

//------------------------------------------------------------------------------

const Variable *JavaExternHdl::execute(ExecuteParamRec &param)
{
  enum
  {
    F_startJVM = 0,
	F_stopJVM = 1,
	F_dpGetPeriod = 2,
	F_xxx
  };

  switch ( param.funcNum )
  {
	case F_startJVM:		return startVM(param); 
	case F_stopJVM:			return stopVM(param);
	case F_dpGetPeriod:		return dpGetPeriod(param);  
	case F_xxx:             return xxx(param);
    default:
      return &errorIntVar;
  }
}

// ---------------------------------------------------------------------
// WaitCond
class DpGetPeriodWaitCond : public WaitCond
{
public:
	DpGetPeriodWaitCond(JNIEnv *p_env, CtrlThread *thread, ExprList *args);
	~DpGetPeriodWaitCond();

	virtual const TimeVar &nextCheck() const;
	virtual int checkDone();

	IntegerVar result;

	CtrlThread *thread;
	ExprList *args;

private:
	JNIEnv *env;
	jobject jThread;

};

//----------------------------------------------------------------------
DpGetPeriodWaitCond::DpGetPeriodWaitCond(JNIEnv *p_env, CtrlThread *p_thread, ExprList *p_args)
{
	env = p_env;
	thread = p_thread;
	args = p_args;

	//jclass jClass = env->FindClass(JavaExternHdl::DpGetPeriodClassName);
	jclass jClass = Java::FindClass(env, JavaExternHdl::DpGetPeriodClassName);
	if (jClass == nil) { result.setValue(-1); return; }

	jmethodID jMethodInit = Java::GetMethodID(env, jClass, "<init>", "(JLjava/lang/String;Lcom/etm/api/var/Variable;Lcom/etm/api/var/Variable;Lcom/etm/api/var/Variable;)V");
	if (jMethodInit == nil) { result.setValue(-1); return; }

	jmethodID jMethodAddDp = Java::GetMethodID(env, jClass, "addDp", "(Lcom/etm/api/var/Variable;JJ)V");
	if (jMethodAddDp == nil) { result.setValue(-2); return; }	

	jmethodID jMethodStart = Java::GetMethodID(env, jClass, "start", "()V");
	if (jMethodStart == nil) { result.setValue(-2); return; }	

	const Variable *t1 = (args->getFirst()->evaluate(thread));
	const Variable *t2 = (args->getNext()->evaluate(thread));
	const Variable *cnt = (args->getNext()->evaluate(thread));
	
	jobject jt1 = Java::convertToJava(env, (VariablePtr)t1);
	jobject jt2 = Java::convertToJava(env, (VariablePtr)t2);
	jobject jcnt = Java::convertToJava(env, (VariablePtr)cnt);

	jstring jUrl = env->NewStringUTF(JavaResources::getQueryServerURL().c_str());
	jThread = env->NewObject(jClass, jMethodInit, (jlong)this, jUrl, jt1, jt2, jcnt);
	env->DeleteLocalRef(jUrl);

	//env->CallVoidMethod(jThread, jMethodSetCPtr, (jlong)this);

	env->DeleteLocalRef(jt1);
	env->DeleteLocalRef(jt2);
	env->DeleteLocalRef(jcnt);

	if (!jThread) {
		result.setValue(-4);
	}
	else {
		CtrlExpr* expr;
		for (int i = 1; expr=args->getNext(); i++) {

			//std::cout << "addDp " << dp->formatValue() << std::endl;

			//for (int j = 1; j <= 2 && (expr = args->getNext()); j++); // skip xa, ta

			DynVar *xa = (DynVar*)args->getNext()->getTarget(thread);
			DynVar *ta = (DynVar*)args->getNext()->getTarget(thread);
			xa->clear();
			ta->clear();

			const Variable *dp = expr->evaluate(thread);
			jobject jdp = Java::convertToJava(env, (VariablePtr)dp);
			env->CallVoidMethod(jThread, jMethodAddDp, jdp, (jlong)ta, (jlong)xa);
			env->DeleteLocalRef(jdp);
		}		

		env->CallVoidMethod(jThread, jMethodStart);
		result.setValue(0);
	}

	env->DeleteLocalRef(jClass);
}

//----------------------------------------------------------------------
DpGetPeriodWaitCond::~DpGetPeriodWaitCond()
{
	env->DeleteLocalRef(jThread);
}

//----------------------------------------------------------------------
const TimeVar& DpGetPeriodWaitCond::nextCheck() const
{
	static TimeVar timeOfNextCall;
	timeOfNextCall.setCurrentTime();
	timeOfNextCall += TimeVar(0, (PVSSshort)100);
	return timeOfNextCall;
}

//----------------------------------------------------------------------
int DpGetPeriodWaitCond::checkDone()
{
	//jclass jClass = Java::FindClass(env, JavaExternHdl::DpGetPeriodClassName);
	//if (jClass == nil) { result.setValue(-1); return true; }

	jmethodID jMethod = Java::GetMethodID(env, JavaExternHdl::clsDpGetPeriod, "checkDone", "()Z");
	if (jMethod == nil) { result.setValue(-2); return true; }

	jboolean jDone = env->CallBooleanMethod(jThread, jMethod);

	//env->DeleteLocalRef(jClass);
	return jDone;
}

//----------------------------------------------------------------------
JNIEXPORT jint JNICALL Java_com_etm_net_client_ExternHdl_apiReadChunk
(JNIEnv *env, jclass, jlong jWaitCondPtr, jobject jChunk, jstring jDpName, jint jDpNr, jlong jTAPtr, jlong jXAPtr)
{
	// get pointer to waitCond object
	DpGetPeriodWaitCond *waitCond = (DpGetPeriodWaitCond*)jWaitCondPtr;
	ExprList *args = waitCond->args;
	CtrlThread *thread = waitCond->thread;

	jmethodID jMethodGetValues = Java::GetMethodID(env, JavaExternHdl::clsDpGetPeriodResult, "getValues", "(Ljava/lang/String;)Lcom/etm/api/var/DynVar;");
	if (jMethodGetValues == nil) return -1;

	jmethodID jMethodGetTimes = Java::GetMethodID(env, JavaExternHdl::clsDpGetPeriodResult, "getTimes", "(Ljava/lang/String;)Lcom/etm/api/var/DynVar;");
	if (jMethodGetTimes == nil) return -1;


	DynVar *xa = (DynVar*)jXAPtr;
	DynVar *ta = (DynVar*)jTAPtr;

	//DynVar *xa = (DynVar*)args->getNext()->getTarget(thread);
	//DynVar *ta = (DynVar*)args->getNext()->getTarget(thread);


	//TextVar *dpArg = (TextVar*)expr->evaluate(thread);
	//CharString *dp = Java::convertJString(env, jDpName);

	//if (strcmp(dpArg->getValue(), dp->c_str())!=0) continue;

	jobject jvar;
	Variable *var;
	Variable *item;
	AnyTypeVar *avar;

	// Values						
	if ((jvar = env->CallObjectMethod(jChunk, jMethodGetValues, jDpName)) != NULL) {
		if ((var = Java::convertJVariable(env, jvar)) != NULL) {
			if (var->isA(DYN_VAR) == DYN_VAR)
			{
				while (item = ((DynVar*)var)->cutFirstVar())
				{
					avar = new AnyTypeVar(item);
					if (!xa->append(avar))
					{
						ErrHdl::error(ErrClass::PRIO_SEVERE, ErrClass::ERR_IMPL, ErrClass::UNEXPECTEDSTATE,
							JavaExternHdl::ManagerName, "apiReadChunk", CharString("error adding value to dyn"));
						delete avar;
					}
				}
			}
			delete var;
		}
	}
	env->DeleteLocalRef(jvar);

	// Times
	if ((jvar = env->CallObjectMethod(jChunk, jMethodGetTimes, jDpName)) != NULL) {
		if ((var = Java::convertJVariable(env, jvar)) != NULL) {
			if (var->isA(DYN_VAR) == DYN_VAR)
			{
				while (item = ((DynVar*)var)->cutFirstVar())
				{
					avar = new AnyTypeVar(item);
					if (!ta->append(avar))
					{
						ErrHdl::error(ErrClass::PRIO_SEVERE, ErrClass::ERR_IMPL, ErrClass::UNEXPECTEDSTATE,
							JavaExternHdl::ManagerName, "apiReadChunk", CharString("error adding time to dyn"));
						delete item;
					}
				}
			}
			delete var;
		}
	}
	env->DeleteLocalRef(jvar);
	
	return 0;
}

// ---------------------------------------------------------------------
// startJVM
const Variable* JavaExternHdl::startVM(ExecuteParamRec &param)
{
	static IntegerVar result(-1);

	param.thread->clearLastError();

	if (jvmState == JNI_OK)  {
		ErrHdl::error(ErrClass::PRIO_INFO, ErrClass::ERR_IMPL, ErrClass::UNEXPECTEDSTATE,
			ManagerName, "java", CharString("jvm already started"));
		result.setValue(0);
	}
	else {
		JavaResources::init();

		//================== prepare loading of Java VM ============================
		JavaVMInitArgs vm_args;                        // Initialization arguments
		JavaVMOption* options = new JavaVMOption[99];   // JVM invocation options		

		int idx = -1;

		// 0) jvmOption e.g. -Xmx512m
		options[++idx].optionString = JavaResources::getJvmOption();
		std::cout << "jmvOption=" << options[0].optionString << std::endl;
		
		// 1) jvmClassPath
		if (strlen(JavaResources::getJvmClassPath().c_str()) > 0)
		{
			std::string cp = "-Djava.class.path=" + JavaResources::getJvmClassPath();
			options[++idx].optionString = (char*)cp.c_str();
			std::cout << "jmvClassPath=" << options[idx].optionString << std::endl;
		}

		// 2) jvmLibraryPath
		if (strlen(JavaResources::getJvmLibraryPath().c_str()) > 0)
		{
			std::string lp = "-Djava.library.path=" + JavaResources::getJvmLibraryPath();
			options[++idx].optionString = (char*)lp.c_str();
			std::cout << "jmvLibraryPath=" << options[idx].optionString << std::endl;
		}

		// config.java
		std::ifstream t(Resources::getConfigDir() + "config.java");
		std::string line;
		while (std::getline(t, line) && ++idx<99)
		{
			std::cout << "config.java: " << line << std::endl;
			char * cstr = new char[line.length() + 1];
			std::strcpy(cstr, line.c_str());
			options[idx].optionString = cstr;
		}

		//std::string str((std::istreambuf_iterator<char>(t)), std::istreambuf_iterator<char>());
		//options[1].optionString = (char*)str.c_str();
		//std::cout << "config.java=" << options[1].optionString << std::endl;

		// queryServer
		std::cout << "queryServer=" << JavaResources::getQueryServerURL().c_str() << std::endl;

		//CharString jvmoption1 = CharString("-Xmx32m");
		//CharString jvmoption2 = CharString("-Djava.class.path=.;C:/Workspace/BigDataLogger/Java/lib/json-simple-1.1.1.jar;C:/Workspace/BigDataLogger/Java/lib/json-simple-1.1.1.jar;C:/Workspace/BigDataLogger/Java/build/classes");
		//CharString jvmoption3 = CharString("-Djava.library.path=.;C:/Workspace/BigDataLogger/WinCCOA/bin");
		//options[0].optionString = jvmoption1;
		//options[1].optionString = jvmoption2;
		//options[2].optionString = jvmoption3;

		vm_args.version = JNI_VERSION_1_8;             // minimum Java version
		vm_args.nOptions = 3;                          // number of options
		vm_args.options = options;
		vm_args.ignoreUnrecognized = false;     // invalid options make the JVM init fail 

		//=============== load and initialize Java VM and JNI interface =============
		jvmState = JNI_CreateJavaVM(&jvm, (void**)&env, &vm_args);
		delete options;    // we then no longer need the initialisation options. 
		if (jvmState != JNI_OK) {
			ErrHdl::error(ErrClass::PRIO_SEVERE, ErrClass::ERR_IMPL, ErrClass::UNEXPECTEDSTATE,
				ManagerName, "java", CharString("error creating jvm"));
			result.setValue(-1);
		}
		else {
			//=============== Display JVM version =======================================
			std::cout << "JVM load succeeded: Version ";
			jint ver = env->GetVersion();
			std::cout << ((ver >> 16) & 0x0f) << "." << (ver & 0x0f) << std::endl;

			// register native callbacks from java to c++
			const JNINativeMethod methods[] = { 
				{ "apiGetManType", "()I", (void*)&Java_com_etm_net_client_ExternHdl_apiGetManType },
				{ "apiGetManNum", "()I", (void*)&Java_com_etm_net_client_ExternHdl_apiGetManNum },
				{ "apiGetLogDir", "()Ljava/lang/String;", (void*)&Java_com_etm_net_client_ExternHdl_apiGetLogDir },
				{ "apiReadChunk", "(JLcom/etm/net/server/DpGetPeriodResultJava;Ljava/lang/String;IJJ)I", (void*)&Java_com_etm_net_client_ExternHdl_apiReadChunk }
			};


			const int methods_size = sizeof(methods) / sizeof(methods[0]);
			jclass jExternHdlClass = env->FindClass(ExternHdlClassName);
			env->RegisterNatives(jExternHdlClass, methods, methods_size);
			env->DeleteLocalRef(jExternHdlClass);

			// find java extern hdl class
			jclass jClass = Java::FindClass(env, JavaExternHdl::ExternHdlClassName);
			if (jClass == nil) result.setValue(-2);
			else {
				// Initalized ExternHdl
				// TODO pass log path
				jmethodID jMethodInit = Java::GetStaticMethodID(env, jClass, "init", "()V");
				if (jMethodInit == nil) result.setValue(-3);
				else {
					env->CallStaticVoidMethod(jClass, jMethodInit);
					std::cout << "JVM init done" << std::endl;
					result.setValue(0);
				}
				env->DeleteLocalRef(jClass);

				// Get/Cache Java Classes
				clsDpGetPeriod = Java::FindClass(env, JavaExternHdl::DpGetPeriodClassName);
				if (jClass == nil) result.setValue(-2);

				clsDpGetPeriodResult = Java::FindClass(env, JavaExternHdl::DpGetPeriodResultClassName);
				if (jClass == nil) result.setValue(-2);
			}
		}
	}
	return &result;
}

// ---------------------------------------------------------------------
// stopJVM
const Variable* JavaExternHdl::stopVM(ExecuteParamRec &param)
{
	static IntegerVar result(-1);

	if (jvmState == JNI_OK && jvm != NULL) {
		jint ret;
		jvmState = ((ret = jvm->DestroyJavaVM()) == JNI_OK) ? JNI_ABORT : JNI_ERR;
		result.setValue(ret);
	}
	else {
		ErrHdl::error(ErrClass::PRIO_SEVERE, ErrClass::ERR_IMPL, ErrClass::UNEXPECTEDSTATE,
			ManagerName, "java", CharString("jvm not started"));
		result.setValue(jvmState);
	}

	return &result;
}

// ---------------------------------------------------------------------
// dpGetPeriod
const Variable* JavaExternHdl::dpGetPeriod(ExecuteParamRec &param)
{
	static IntegerVar result(-2);

	param.thread->clearLastError();

	if (jvmState != JNI_OK) {
		ErrHdl::error(ErrClass::PRIO_SEVERE, ErrClass::ERR_IMPL, ErrClass::UNEXPECTEDSTATE,
			ManagerName, "dpGetPeriod", CharString("jvm not started"));
		result.setValue(-1);
		return &result;
	}	

	DpGetPeriodWaitCond *cond = new DpGetPeriodWaitCond(env, param.thread, param.args);
	param.thread->setWaitCond(cond);	
	
	return &cond->result;
}

JNIEXPORT jstring JNICALL Java_com_etm_net_client_ExternHdl_apiGetLogDir
(JNIEnv *env, jclass)
{
	return env->NewStringUTF(Resources::getLogDir());
}

JNIEXPORT jint JNICALL Java_com_etm_net_client_ExternHdl_apiGetManType
(JNIEnv *env, jclass)
{
	return (jint)Resources::getManType();
}

JNIEXPORT jint JNICALL Java_com_etm_net_client_ExternHdl_apiGetManNum
(JNIEnv *env, jclass)
{
	return (jint)Resources::getManNum();
}

//----------------------------------------------------------------------
// ---------------------------------------------------------------------
// ---------------------------------------------------------------------
// ---------------------------------------------------------------------
const Variable* JavaExternHdl::xxx(ExecuteParamRec &param)
{
	static IntegerVar result(-1);

	ExprList *args = param.args;
	CtrlThread *thread = param.thread;

	jmethodID jMethodInit = env->GetMethodID(clsDpGetPeriod, "<init>", "(Lcom/etm/api/var/Variable;Lcom/etm/api/var/Variable;Lcom/etm/api/var/Variable;)V");
	//jmethodID jMethodInit = env->GetMethodID(jClass, "<init>", "()V");
	if (jMethodInit == nil) {
		std::string msg = "mid init not found";
		ErrHdl::error(ErrClass::PRIO_SEVERE, ErrClass::ERR_IMPL, ErrClass::UNEXPECTEDSTATE,
			JavaExternHdl::ManagerName, "xxx", msg.c_str());
		result.setValue(-2);
		return &result;
	}

	jmethodID jMethodAddDp = env->GetMethodID(clsDpGetPeriod, "addDp", "(Lcom/etm/api/var/Variable;)V");
	if (jMethodAddDp == nil) {
		std::string msg = "mid addDp not found";
		ErrHdl::error(ErrClass::PRIO_SEVERE, ErrClass::ERR_IMPL, ErrClass::UNEXPECTEDSTATE,
			JavaExternHdl::ManagerName, "xxx", msg.c_str());
		result.setValue(-2);
		return &result;
	}

	const Variable *t1 = (args->getFirst()->evaluate(thread));
	const Variable *t2 = (args->getNext()->evaluate(thread));
	const Variable *cnt = (args->getNext()->evaluate(thread));

	jobject jt1 = Java::convertToJava(env, (VariablePtr)t1);
	jobject jt2 = Java::convertToJava(env, (VariablePtr)t2);
	jobject jcnt = Java::convertToJava(env, (VariablePtr)cnt);

	jobject jObject = env->NewObject(clsDpGetPeriod, jMethodInit, jt1, jt2, jcnt);
	//jobject jObject = env->NewObject(jClass, jMethodInit);

	if (env->ExceptionCheck()) {
		std::string msg = "java exception!";
		ErrHdl::error(ErrClass::PRIO_SEVERE, ErrClass::ERR_IMPL, ErrClass::UNEXPECTEDSTATE,
			JavaExternHdl::ManagerName, "NewObject", msg.c_str());
		result.setValue(-99);
		env->ExceptionDescribe();
		return &result;
	}

	env->DeleteLocalRef(jt1);
	env->DeleteLocalRef(jt2);
	env->DeleteLocalRef(jcnt);

	// TESTPOINT
	//env->DeleteLocalRef(jClass);
	//env->DeleteLocalRef(jObject);
	//return &result;

	const Variable *dp = args->getNext()->evaluate(thread);
	jobject jdp = Java::convertToJava(env, (VariablePtr)dp);
	//std::cout << "addDp " << dp->formatValue() << std::endl;
	env->CallVoidMethod(jObject, jMethodAddDp, jdp);
	if (env->ExceptionCheck()) {
		std::string msg = "java exception!";
		ErrHdl::error(ErrClass::PRIO_SEVERE, ErrClass::ERR_IMPL, ErrClass::UNEXPECTEDSTATE,
			JavaExternHdl::ManagerName, "convertToJava", msg.c_str());
		result.setValue(-99);
		env->ExceptionDescribe();
		return &result;
	}
	env->DeleteLocalRef(jdp);

	// TESTPOINT
	//env->DeleteLocalRef(jClass);
	//env->DeleteLocalRef(jObject);
	//return &result;

	jmethodID jMethodStart = env->GetMethodID(clsDpGetPeriod, "test", "()V");
	if (jMethodStart == nil) {
		std::string msg = "mid start not found";
		ErrHdl::error(ErrClass::PRIO_SEVERE, ErrClass::ERR_IMPL, ErrClass::UNEXPECTEDSTATE,
			JavaExternHdl::ManagerName, "xxx", msg.c_str());
		result.setValue(-2);
		return &result;
	}
	env->CallVoidMethod(jObject, jMethodStart);

	if (env->ExceptionCheck()) {
		std::string msg = "java exception!";
		ErrHdl::error(ErrClass::PRIO_SEVERE, ErrClass::ERR_IMPL, ErrClass::UNEXPECTEDSTATE,
			JavaExternHdl::ManagerName, "test", msg.c_str());
		result.setValue(-99);
		env->ExceptionDescribe();
		return &result;
	}

	jmethodID jMethodGetValues = env->GetMethodID(clsDpGetPeriod, "getValues", "(I)Lcom/etm/api/var/DynVar;");
	if (jMethodGetValues == nil) {
		std::string msg = "mid getValues not found";
		ErrHdl::error(ErrClass::PRIO_SEVERE, ErrClass::ERR_IMPL, ErrClass::UNEXPECTEDSTATE,
			JavaExternHdl::ManagerName, "xxx", msg.c_str());
		result.setValue(-2);
		return &result;
	}

	if (env->ExceptionCheck()) {
		std::string msg = "java exception!";
		ErrHdl::error(ErrClass::PRIO_SEVERE, ErrClass::ERR_IMPL, ErrClass::UNEXPECTEDSTATE,
			JavaExternHdl::ManagerName, "getValues", msg.c_str());
		result.setValue(-99);
		env->ExceptionDescribe();
		return &result;
	}

	DynVar *xa = (DynVar*)args->getNext()->getTarget(thread);
	//xa->clear();

	DynVar tmp(ANYTYPE_VAR);
	*xa = tmp;

	Variable *var;
	Variable *item;
	jobject jvar;
	if ((jvar = env->CallObjectMethod(jObject, jMethodGetValues, 0)) != NULL)
	{
		if ((var = Java::convertJVariable(env, jvar)) != NULL)
		{
			//std::cout << var->isA(DYN_VAR) << " - " << var->isA() << std::endl;
			if (var->isA(DYN_VAR) == DYN_VAR)
			{
				DynVar *dvar = (DynVar*)var;
				while (item = dvar->cutFirstVar())
				{
					AnyTypeVar *avar = new AnyTypeVar(item);
					if (!xa->append(avar))
					{						
						ErrHdl::error(ErrClass::PRIO_SEVERE, ErrClass::ERR_IMPL, ErrClass::UNEXPECTEDSTATE,
							JavaExternHdl::ManagerName, "dpGetPeriod", CharString("error adding value to dyn"));
						delete avar;
					}
				}
			}
			delete var;
		}
	}
	env->DeleteLocalRef(jvar);
	env->DeleteLocalRef(jObject);
	return &result;
}
