1) Java 8+ JRE must be installed
   http://www.oracle.com/technetwork/java/javase/downloads/jre8-downloads-2133155.html

2) Copy WCCOAjava.dll, WCCOAjava.exe and WCCOAjava-<version>.jar from the bin directory to your project bin directory 
   C:\WinCC_OA_Proj\Example\bin
   The files are already pre compiled for the WinCC OA version bin-<version>. 
   The Manager Sources are compiled as .dll and additionall as .exe which is used by WinCC OA console for java startup
   (to change the compilation target in Visual Studio go to: Project/Properties, Configuratoin Properties/General, Configuration Type)

3) Copy the example classes from the "classes" directory to your project directory 
   C:\WinCC_OA_Proj\Example\classes

4) Add the path to the jvm.dll of your Java installation to the PATH environment variable 
   e.g. PATH=C:\Program Files\Java\jre1.8.0_72\bin\server
   you have to restart the pmon/console afterwards

5) Copy and adapt the config/config.java file so that the paths fit to your project path

6) Add a new manager "WCCOAjava" in the console with the parameter "-class Example"
   or start the java program as a normal java program, see bin/Example.bat
   (there needs to be a directory "log" below your working directory)
   The program is connected to "ExampleDP_Trend1." and prints the value to the log.

*) If you get a message "MSVCR100.dll is missing", then you need to install
   Microsoft Visual C++ 2010 SP1 Redistributable Package (x64)
   http://www.microsoft.com/en-us/download/details.aspx?id=13523

