# oa4j - WinCC Open Architecture for Java
WinCC Open Architecture for Java is an API to connect WinCC OA to Java.<br>
It is based on the WinCC OA native API+JNI and works on Windows and Linux.<br>
Because it's using the WinCC OA API you need to have a valid WinCC OA API license.<br>
An example with a compiled version of oa4j is available in the project directory.<br>
An example for Scala can be found in the Scala directory.<br>
## Manager (oa4j/Native/Manager <-> at.rocworks.oa4j.base)<br>
Examples can be found in the Java/src/test directory and in the project/example directory, please read the project/Example/Readme.txt. <br>
The JClient class is an easy to use static class. It should be thread safe and callback functions are processed in a separate thread, so that the main WinCC OA thread/loop will not be blocked by callback functions.<br>
```
// Example how to connect to WinCC OA and set some tags (datapoints)
JManager m = new JManager();
m.init(args).start(); 
ret = JClient.dpSet()
  .add("System1:ExampleDP_Trend1.:_original.._value", new FloatVar(Math.random()))
  .add("System1:ExampleDP_SumAlert.:_original.._value", new TextVar("hello world"))
  .await()
  .getRetCode();
m.stop();
```

## CtrlExt (oa4j\Native\CtrlExt <-> at.rocworks.oa4j.base.ExternHdl)<br>
It is possible to call a Java function from WinCC OA control language. 
The control extension JavaCtrlExt must be loaded and a Subclass of ExternHdlFunction must be implemented. This class can be used to execute a function. Executing the Java function in Control is done with the control function "javaCall" and "javaCallAsync". E.g.: javaCall("ApiTestExternHdl", "myFunTest", makeDynAnytype("hello", 1, true), out); A list of variables can be passed as input parameters and a list of values can be passed out by the Java function (it is the return value of the Java function). It is also possible to execute ctrl callback functions from Java. An example can be found in project/example/panels/JavaCtrlExt.pnl and the Java source src/test/java/ApiTestExternHdl.java. For each javaCall a new Java object of the given class is created (where a reference pointer to the WaitCond object in C++ is stored in the case of an async call).<br>
```
    // Java Code (Class ApiTestExternHdl)
    public DynVar execute(String function, DynVar parameter) {
        Debug.out.log(Level.INFO, "execute function={0} parameter={1}", new Object[] { function, parameter.formatValue() });
        return new DynVar(new IntegerVar(0));
    }

    // Control Code
    dyn_anytype out;
    dyn_anytype in = makeDynAnytype("hallo", 1, getCurrentTime());
    int ret = javaCall("ApiTestExternHdl", "myFunTest", in, out);
```

## Driver (oa4j/Native/Driver <-> at.rocworks.oa4j.driver)<br>
There is also an API and framework to implement a WinCC OA driver program in Java. A driver program is used to connect WinCC OA to peripherial devices and exchange data. There is a driver class driver.JDriver and a driver.JDriverSimple class, which can be used to implement a driver. An example driver can be found in test/java/DrvTestMqtt.java <br>
