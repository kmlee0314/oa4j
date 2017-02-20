import at.rocworks.oa4j.jni.ExternHdlFunction;
import at.rocworks.oa4j.utils.Debug;
import at.rocworks.oa4j.var.DynVar;
import at.rocworks.oa4j.var.IntegerVar;

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
    public DynVar execute(String function, DynVar parameter) {
        Debug.out.log(Level.INFO, "execute function={0} parameter={1}", new Object[] { function, parameter.formatValue() });
        return new DynVar(new IntegerVar(0));
    }
}
