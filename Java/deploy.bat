copy target\*.jar ..\Project\Example\lib
del ..\Project\Example\classes\*.class
copy target\test-classes\*.class ..\Project\Example\classes

copy target\*.jar C:\WinCC_OA_Proj\Example\lib
del C:\WinCC_OA_Proj\Example\classes\*.class
copy target\test-classes\*.class C:\WinCC_OA_Proj\Example\classes