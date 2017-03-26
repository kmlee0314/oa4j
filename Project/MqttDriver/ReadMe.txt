*) Java 8+ JRE must be installed
   http://www.oracle.com/technetwork/java/javase/downloads/jre8-downloads-2133155.html

*) Add the path to the jvm.dll of your Java installation to the PATH environment variable 
   e.g. PATH=C:\Program Files\Java\jre1.8.0_<version>\bin\server
   you have to restart the pmon/console afterwards

*) Copy the oa4j/Bundle to your project bin directory

*) Copy the directories bin,config,dplist to your project directory

*) Import the JavaDrv.dpl file with the ASCII Manager from the dplist directory 

*) Add the Java Driver Manager to the console with to following parameters and start it

WCCOAjavadrv -num 2 -cp bin/WCCOAmqtt.jar -url ssl://iot.eclipse.org:8883 -cid winccoa -json

-url ... mqtt broker endpoint
-cid ... client id of the driver
-json ... write and integer & float's as a json value {value=1.2323}

*) If you get a message "MSVCR100.dll is missing", then you need to install
   Microsoft Visual C++ 2010 SP1 Redistributable Package (x64)
   http://www.microsoft.com/en-us/download/details.aspx?id=13523
