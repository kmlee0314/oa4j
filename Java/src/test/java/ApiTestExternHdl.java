import at.rocworks.oa4j.jni.ExternHdlFunction;
import at.rocworks.oa4j.utils.Debug;
import at.rocworks.oa4j.var.DynVar;
import at.rocworks.oa4j.var.IntegerVar;
import at.rocworks.oa4j.var.TextVar;

import java.util.logging.Level;

/**
 * Created by vogler on 2/20/2017.
 */
public class ApiTestExternHdl extends ExternHdlFunction {
    public ApiTestExternHdl(long waitCondPtr) {
        super(waitCondPtr);
    }

    /**
     *
     * @param function Name of a (sub)program unit, must be handled by yourself (e.g. case statement)
     * @param parameter List of parameter values
     * @return List of values
     */
    @Override
    public DynVar execute(TextVar function, DynVar parameter) {
        Debug.out.log(Level.INFO, "execute function={0} parameter={1}", new Object[] { function, parameter.formatValue() });
        if (function.equals("TestDpTest")) {
            Debug.out.info(function.toString());
            callback("jEvalScript",
                    new DynVar(new TextVar("main() { dpSet($1, $2); }"),
                            new DynVar(
                                    new TextVar("$1:ExampleDP_Arg1."),
                                    new TextVar("$2:0")),
                            new DynVar()));
        } else if (function.equals("NestedCall")) {
                Debug.out.info(function.toString());
                TextVar script=new TextVar(
                        "main() { dyn_anytype out;"+
                        "int ret = javaCallAsync(\"ApiTestExternHdl\", \"Inside\", makeDynString(\"inside!\"), out);"+
                        "}");
                callback("jEvalScript", new DynVar(script, new DynVar(), new DynVar()));
        } else {
            Debug.out.info("unhandled: "+function.toString());
        }


        return new DynVar(new IntegerVar(0));
    }
}
