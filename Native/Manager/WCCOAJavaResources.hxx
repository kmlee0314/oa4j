// The Resource file for the WCCOAJava manager
#ifndef WCCOAJavaRESOURCES_H
#define WCCOAJavaRESOURCES_H

#include  <Resources.hxx>

class WCCOAJavaResources : public Resources
{
  public:
    // These functions initializes the manager
    static void init(int &argc, char *argv[]);

    // Read the config section
    static PVSSboolean readSection();

  public:
	static const CharString & getJvmOption() { return jvmOption; }
	static const CharString & getJvmClassPath() { return  jvmClassPath; }
	static const CharString & getJvmLibraryPath() { return jvmLibraryPath; }

  private:
	static CharString jvmOption;
	static CharString jvmClassPath;
	static CharString jvmLibraryPath;

};

#endif
