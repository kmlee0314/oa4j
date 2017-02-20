copy ..\Native\Manager\bin\WCCOAjava.dll   ..\Project\Example\bin-3.14\
copy ..\Native\Manager\bin\WCCOAjava.exe   ..\Project\Example\bin-3.14\

copy ..\Native\Driver\bin\WCCOAjavadrv.dll ..\Project\Example\bin-3.14\
copy ..\Native\Driver\bin\WCCOAjavadrv.exe ..\Project\Example\bin-3.14\

copy ..\Native\CtrlExt\bin\JavaCtrlExt.dll ..\Project\Example\bin-3.14\

copy ..\Java\out\artifacts\WCCOAjava_jar\WCCOAjava.jar ..\Project\Example\lib\
copy ..\Java\target\test-classes                       ..\Project\Example\classes\

copy ..\Project\Example\bin-3.14\* C:\WinCC_OA_Proj\Example\bin\
copy ..\Project\Example\lib\*      C:\WinCC_OA_Proj\Example\lib\
copy ..\Project\Example\classes\*  C:\WinCC_OA_Proj\Example\classes\

pause