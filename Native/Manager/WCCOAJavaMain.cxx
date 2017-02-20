/*
  Example of an API manager
  This will copy the values from one datapoint to another.
  To do this the manager will connect to the first datapoint and
  for every hotlink it receives it will set the second one.
  The names of both datapoints can be given in the config file.
*/

#include <WCCOAJavaManager.hxx>
#include <WCCOAJavaResources.hxx>
#include <at_rocworks_oa4j_jni_Manager.h>
#include <signal.h>
#include <cstring>

//------------------------------------------------------------------------------------------------

int main(int argc, char *argv[])
{
	JavaVM *jvm;                      // Pointer to the JVM (Java Virtual Machine)
	JNIEnv *env;                      // Pointer to native interface

	WCCOAJavaResources::init(argc, argv);

	//================== prepare loading of Java VM ============================
	JavaVMInitArgs vm_args;                        // Initialization arguments
	JavaVMOption* options = new JavaVMOption[99];   // JVM invocation options

	int idx = -1;

	// 0) jvmOption e.g. -Xmx512m
	if (strlen(WCCOAJavaResources::getJvmOption().c_str()) > 0)
	{
		options[++idx].optionString = WCCOAJavaResources::getJvmOption();
		std::cout << "jvmOption='" << options[0].optionString << "'" << std::endl;
	}

	// 1) jvmClassPath
	if (strlen(WCCOAJavaResources::getJvmClassPath().c_str()) > 0)
	{
		std::string cp = "-Djava.class.path=" + (const char&)WCCOAJavaResources::getJvmClassPath();
		options[++idx].optionString = (char*)cp.c_str();
		std::cout << "jvmClassPath=" << options[idx].optionString << std::endl;
	}

	// 2) jvmLibraryPath
	if (strlen(WCCOAJavaResources::getJvmLibraryPath().c_str()) > 0)
	{
		std::string lp = "-Djava.library.path=" + (const char&)WCCOAJavaResources::getJvmLibraryPath();
		options[++idx].optionString = (char*)lp.c_str();
		std::cout << "jvmLibraryPath=" << options[idx].optionString << std::endl;
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

	vm_args.version = JNI_VERSION_1_8;             // minimum Java version
	vm_args.nOptions = idx + 1;                          // number of options
	vm_args.options = options;
	vm_args.ignoreUnrecognized = true;     // invalid options make the JVM init fail 

	//=============== load and initialize Java VM and JNI interface =============
	jint rc = JNI_CreateJavaVM(&jvm, (void**)&env, &vm_args);
	delete options;    // we then no longer need the initialisation options. 
	if (rc != JNI_OK) {
		exit(EXIT_FAILURE);
	}
	//=============== Display JVM version =======================================
	std::cout << "JVM load succeeded: Version ";
	jint ver = env->GetVersion();
	std::cout << ((ver >> 16) & 0x0f) << "." << (ver & 0x0f) << std::endl;

	//=============== Call Java Main Method =====================================
	jstring str;
	jobjectArray jargv = 0;
	int classIdx = -1;
	char *className = nil;

	jargv = env->NewObjectArray(argc + 6, env->FindClass("java/lang/String"), 0);
	int i;

	// pass arguments through
	for (i = 0; i<argc; i++)
	{
		str = env->NewStringUTF(argv[i]);
		env->SetObjectArrayElement(jargv, i, str);
		if (strcmp(argv[i], "-class") == 0) {
			classIdx = i + 1;
		}
		if (classIdx == i) {
			className = argv[i];
		}
	}

	// add project name to argument list
	str = env->NewStringUTF("-proj");
	env->SetObjectArrayElement(jargv, i, str);
	i++;

	CharString projName = Resources::getProjectName();
	str = env->NewStringUTF(projName);
	env->SetObjectArrayElement(jargv, i, str);
	i++;


	// add project dir to argument list
	str = env->NewStringUTF("-path");
	env->SetObjectArrayElement(jargv, i, str);
	i++;

	CharString projDir = Resources::getProjDir();
	str = env->NewStringUTF(projDir);
	env->SetObjectArrayElement(jargv, i, str);
	i++;

	// add manager num to argument list
	str = env->NewStringUTF("-num");
	env->SetObjectArrayElement(jargv, i, str);
	i++;

	CharString manNum = Resources::getManNum();
	str = env->NewStringUTF(manNum);
	env->SetObjectArrayElement(jargv, i, str);
	i++;

	// check if classname was given
	if (className == nil) {
		std::cout << "parameter -class missing!" << std::endl;
		return -1;
	}

	jclass javaMainClass = env->FindClass(className);
	if (javaMainClass == nil) {
		std::cout << "class " << className << " not found!" << std::endl;
		return -2;
	}

	jmethodID javaMainMethod = env->GetStaticMethodID(javaMainClass, "main", "([Ljava/lang/String;)V");
	if (javaMainMethod == nil) {
		std::cout << "main method not found!" << std::endl;
		return -3;
	}

	env->CallStaticVoidMethod(javaMainClass, javaMainMethod, jargv);

	jvm->DestroyJavaVM();

	return 0;
}

