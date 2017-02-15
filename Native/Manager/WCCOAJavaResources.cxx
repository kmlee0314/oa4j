// Our Resources administration
#include  <WCCOAJavaResources.hxx>
#include  <ErrHdl.hxx>

// Our static Variable
CharString  WCCOAJavaResources::jvmOption = "";
CharString  WCCOAJavaResources::jvmClassPath = "";
CharString  WCCOAJavaResources::jvmLibraryPath = "";

// Wrapper to read config file
void  WCCOAJavaResources::init(int &argc, char *argv[])
{
  begin(argc, argv);
  while ( readSection() || generalSection() )
    ;

  end(argc, argv);

  // Are we called with -helpdbg or -help ?
  if (WCCOAJavaResources::getHelpDbgFlag())
  {
	  WCCOAJavaResources::printHelpDbg();
  }

  if (WCCOAJavaResources::getHelpFlag())
  {
	  WCCOAJavaResources::printHelp();
  }

  // the user wants to know std. report flags
  if (WCCOAJavaResources::getHelpReportFlag())
  {
	  WCCOAJavaResources::printHelpReport();	  
  }
}


// Read the config file.
// Our section is [WCCOAJava] or [WCCOAJava<num>],
PVSSboolean  WCCOAJavaResources::readSection()
{
  // Is it our section ?
  if (!isSection("java"))
    return PVSS_FALSE;

  // Read next entry
  getNextEntry();

  // Loop thru section
  while ( (cfgState != CFG_SECT_START) &&  // Not next section
          (cfgState != CFG_EOF) )          // End of config file
  {
    // Compare keyword with "dpNameSet" and dpNameConnect
    if (!keyWord.icmp("jvmOption"))             // It matches
		cfgStream >> jvmOption;                   // read the value
	else if (!keyWord.icmp("classPath"))             // It matches
		cfgStream >> jvmClassPath;                   // read the value
	else if (!keyWord.icmp("libraryPath"))             // It matches
		cfgStream >> jvmLibraryPath;                   // read the value
    else if (!readGeneralKeyWords())            // keywords handled in Resources
    {
      ErrHdl::error(ErrClass::PRIO_WARNING,     // not that bad
                    ErrClass::ERR_PARAM,        // wrong parametrization
                    ErrClass::ILLEGAL_KEYWORD,  // illegal Keyword in Res.
                    keyWord);

      // Signal error, so we stop later
      cfgError = PVSS_TRUE;
    }

    getNextEntry();
  }

  // So the loop will stop at the end of the file
  return cfgState != CFG_EOF;
}
