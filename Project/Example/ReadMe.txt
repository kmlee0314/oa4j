1) Java 8+ JRE must be installed
   http://www.oracle.com/technetwork/java/javase/downloads/jre8-downloads-2133155.html

2) C:\WinCC_OA_Proj\Example\bin
   Copy the bins from the "bin" directory to your project bin directory    
   The files are already pre compiled for the WinCC OA version bin-<version>. 
   The Manager Sources are compiled as .dll and additionall as .exe which is used by WinCC OA console for java startup
   (to change the compilation target in Visual Studio go to: Project/Properties, Configuratoin Properties/General, Configuration Type)

3) C:\WinCC_OA_Proj\Example\lib\
   Copy the libs from the "lib" directory to your project lib directory   

4) C:\WinCC_OA_Proj\Example\classes\
   Copy the example classes from the "classes" directory to your project classes directory    

5) Copy and adapt the config/config.java file so that the paths fit to your project path

6) Add the path to the jvm.dll of your Java installation to the PATH environment variable 
   e.g. PATH=C:\Program Files\Java\jre1.8.0_72\bin\server
   you have to restart the pmon/console afterwards

7) Add a new manager "WCCOAjava" in the console with the parameter "-class Example"
   or start the java program as a normal java program, see bin/Example.bat
   (there needs to be a directory "log" below your working directory)
   The program is connected to "ExampleDP_Trend1." and prints the value to the log.

*) If you get a message "MSVCR100.dll is missing", then you need to install
   Microsoft Visual C++ 2010 SP1 Redistributable Package (x64)
   http://www.microsoft.com/en-us/download/details.aspx?id=13523

*) Add the following lines to the WinCC OA message catalouge "managers.cat"
   WCCOAjava,Java Manager
   WCCOAjavadrv,Java Driver

*) MQTT Example Driver
   + Import the dplist "dplist\JavaDrv.dpl"
   + Add a Java Driver to the console with the options:
     WCCOAjavadrv -num 2 -class MqttDriver -url tcp://<mqtt-host> -cid <client-id>
   + Create some datapoint and add a peripherial address config "Sample Driver" to it
     - Reference: e.g. "rpi2/temp" => mqtt tag name
     - Driver-Number: 2 => the number of your java driver (-num 2)
     - Transformation: Float => values in mqtt must be stored as a json document "{Value: 5.2132412}"
     - Subindex: 0 => not used
     - Direction: Input or Output
     - Input-Mode: Spontaneous (Polling, Single-Query is not implemented)
     - Low-Level-Compression: Yes or No
     - Poll-Group: not used
     - Active: Yes    