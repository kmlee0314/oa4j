package at.rocworks.oa4j.jni;

import at.rocworks.oa4j.utils.Debug;
import at.rocworks.oa4j.var.DynVar;
import at.rocworks.oa4j.var.Variable;

import java.util.logging.Level;

/**
 * Created by vogler on 2/19/2017.
 */
abstract public class ExternHdlFunction implements Runnable {
    private volatile boolean done = false;

    private final long waitCondPtr; // c++ pointer to waitCond object

    private String function;
    private DynVar parameter;

    public ExternHdlFunction(long waitCondPtr) {
        this.waitCondPtr = waitCondPtr;
    }

    /**
     * Internal, is called from the JNI in the case of an async function
     * @param function Function name which will be passed to execute function
     * @param parameter Parameters which will be passed to execute function
     */
    public void start(String function, DynVar parameter) {
        //Debug.out.log(Level.INFO, "start function={0} parameter={1}", new Object[] { function, parameter.formatValue() });

        this.function=function;
        this.parameter=parameter;

        new Thread(this).start();
    }

    /**
     * Function which will be executed from WinCC OA Control script
     * @param function Name of a (sub)program unit, must be handled by yourself (e.g. case statement)
     * @param parameter List of parameter values
     * @return List of values
     */
    abstract public DynVar execute(String function, DynVar parameter);

    /**
     * Internal, called from JNI in the case of threaded function
     * @return
     */
    public boolean checkDone() {
        return done;
    }

    /**
     * Add a result values in the case when the function is called async
     * @param var Variable which should be passed out
     */
    public void addResult(Variable var) {
        if ( waitCondPtr > 0 )
            ExternHdl.apiAddResult(waitCondPtr, var);
    }

    /**
     * Internal, called when funciton is called async
     */
    @Override
    public void run() {
        try {
            addResult(execute(function, parameter));
        } catch (Exception ex) {
            Debug.StackTrace(Level.SEVERE, ex);
        }
        //Debug.out.info("run done");
        done = true;
    }
}
