package js.jst.param;

import java.io.Serializable;
import java.lang.reflect.Method;

public class JTMethod {

    private Method method;

    private Object[] params;

    private Serializable paramEntity;

    public Method getMethod() {
        return method;
    }

    public void setMethod(Method method) {
        this.method = method;
    }

    public Object[] getParams() {
        return params;
    }

    public void setParams(Object[] params) {
        this.params = params;
    }

    public Serializable getParamEntity() {
        return paramEntity;
    }

    public void setParamEntity(Serializable paramEntity) {
        this.paramEntity = paramEntity;
    }
}
