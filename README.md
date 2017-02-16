# oa4j - WinCC Open Architecture for Java
WinCC Open Architecture for Java is an API to connect WinCC OA to Java.<br>
It is based on the WinCC OA native API+JNI and works on Windows and Linux.<br>
An example with a compiled version of oa4j is available in the project directory.<br>
A version for Scala will maybe available in future releases.<br>
## Manager (oa4j/Native/Manager <-> com.etm.api.base)<br>
Examples can be found in the Java/src/test directory and in the project project directory. The JManagerClient class is an easy to use static class. It should be thread safe and callback functions are processed in a separate thread, so that the main WinCC OA thread/loop will not be blocked by callback functions.<br>
## Driver (oa4j/Native/Driver <-> com.etm.api.driver)<br>
There is also an API and framework to implement a WinCC OA driver program in Java. A driver program is used to connect WinCC OA to peripherial devices and exchange data. There is a driver class com.etm.api.driver.JDriver and a com.etm.atpi.driver.JDriverSimple class which can be used to implement a driver. Examples and HowTo's for drivers will be added soon (e.g. MQTT, Kafka, Epics, Esper ...)<br>
## Control Extension (oa4j\Native\CtrlExt)<br>
An API for a WinCC OA Ctrl Extension is under construction. It is possible to use and call Java functions from WinCC OA control programming language. But currently it is necessary to code C++ to integrate new Java functions, there should be a generic way to add Java code without the need to change the C++ code. <br>

## Simple dpSet Example
```
// Example how to connect to WinCC OA and set some tags (datapoints)
JManager m = new JManager();
m.init(args).start(); 
ret = JManagerClient.dpSet()
  .add("System1:ExampleDP_Trend1.:_original.._value", new FloatVar(Math.random()))
  .add("System1:ExampleDP_SumAlert.:_original.._value", new TextVar("hello world"))
  .await()
  .getRetCode();
m.stop();
```
