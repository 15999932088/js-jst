package js.jst.param.enums;

import java.util.Random;

public class JParamEnum {

    public enum StringParam{
        PARAM(new Object[]{null,"testString","longtext"})
        ;

        Object[] objects;

        StringParam(Object[] objects){
            this.objects = objects;
        }

        public static StringParam get(){
            StringParam[] params = values();
            Random random = new Random();
            return params[random.nextInt(params.length-1)];
        }

        public static Object getOne(){
            StringParam param = get();
            Random random = new Random();
            return param.objects[random.nextInt(param.objects.length-1)];
        }
    }

}
