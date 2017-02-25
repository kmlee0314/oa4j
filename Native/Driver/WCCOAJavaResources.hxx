#ifndef WCCOAJavaRESOURCES_H_
#define WCCOAJavaRESOURCES_H_

// Our Resources class
// This class has two tasks:
//  - Interpret commandline and read config file
//  - Be an interface to internal datapoints

#include <DrvRsrce.hxx>
#include <WCCOAJavaDrvIntDp.hxx>

class WCCOAJavaResources : public DrvRsrce
{
public:
	static void init(int &argc, char *argv[]); // Initialize statics
	static PVSSboolean readSection();          // read config file

	// Get our static Variables
	static const CharString & getJvmOption() { return jvmOption; }
	static const CharString & getJvmUserDir() { return  jvmUserDir; }
	static const CharString & getJvmClassPath() { return  jvmClassPath; }
	static const CharString & getJvmLibraryPath() { return jvmLibraryPath; }
	static const CharString & getJvmConfigFile() { return jvmConfigFile; }

	// Get the number of names we need the DpId for
	virtual int getNumberOfDpNames();

private:
	static CharString jvmOption;
	static CharString jvmUserDir;
	static CharString jvmClassPath;
	static CharString jvmLibraryPath;
	static CharString jvmConfigFile;
	WCCOAJavaDrvIntDp drvIntDp;

public:
	static CharString drvDpName;

};

#endif
