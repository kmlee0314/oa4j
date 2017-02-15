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
	// TODO you likely have different things in the config file
	static const CharString & getJvmOption() { return jvmOption; }
	static const CharString & getJvmClassPath() { return  jvmClassPath; }
	static const CharString & getJvmLibraryPath() { return jvmLibraryPath; }

	// Get the number of names we need the DpId for
	virtual int getNumberOfDpNames();

	// TODO in this template we do not use internal DPs in the driver
	// If you need DPs, then also some other methods must be implemented

private:
	// TODO you likely have different things in the config file
	static CharString jvmOption;
	static CharString jvmClassPath;
	static CharString jvmLibraryPath;
	WCCOAJavaDrvIntDp drvIntDp;

public:
	static CharString drvDpName;

};

#endif
