copy ..\Native\Manager\bin\WCCOAjava.dll   ..\Native\Version\3.15\
copy ..\Native\Manager\bin\WCCOAjava.exe   ..\Native\Version\3.15\

copy ..\Native\Driver\bin\WCCOAjavadrv.dll ..\Native\Version\3.15\
copy ..\Native\Driver\bin\WCCOAjavadrv.exe ..\Native\Version\3.15\

copy ..\Native\CtrlExt\bin\JavaCtrlExt.dll ..\Native\Version\3.15\

copy ..\Java\bin\WCCOAjava.jar      ..\Project\Example\bin\
copy ..\Java\target\test-classes\*  ..\Project\Example\bin\

copy ..\Native\Version\3.15\*      C:\WinCC_OA_Proj\Example-3.15\bin\
copy ..\Project\Example\bin\*      C:\WinCC_OA_Proj\Example-3.15\bin\

pause