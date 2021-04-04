package js.jst.worker;

import js.jst.interfaces.JCTest;
import js.jst.param.JTMethod;
import js.jst.param.enums.JParamEnum;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.*;

public class Reflecter {

    private Map<Class<?>, List<JTMethod>> reflectProxyMap;

    public Map<Class<?>, List<JTMethod>> getReflectProxyMap() {
        return reflectProxyMap;
    }

    public void reflectClass(String className){
        try {
            reflectProxyMap = new HashMap<>();
            //根据反射来获取这个类的是否存在测试的注解
            Class<?> clazz = Class.forName(className);
            Method[] methods = clazz.getMethods();
            List<JTMethod> methodList = new ArrayList<>();
            if(methods!=null&&methods.length>0){
                for (Method method : methods) {
                    Annotation[] annotations = method.getAnnotations();
                    if(annotations!=null&&annotations.length>0){
                        for (Annotation annotation : annotations) {
                            if(annotation.annotationType().equals(JCTest.class)){
                                //保存当前注解下的method
                                JTMethod jtMethod = new JTMethod();
                                jtMethod.setMethod(method);
                                Parameter[] parameters = method.getParameters();
                                jtMethod.setParams(explainParams(parameters));
                                methodList.add(jtMethod);
                            }
                        }
                    }
                    reflectProxyMap.put(clazz,methodList);
                }
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取方法的参数
     * @param parameters
     * @return
     */
    public Object[] explainParams(Parameter[] parameters){
        List<Object> objectList = new ArrayList<>();
        for (Parameter parameter : parameters) {
            Class<?> paramClass = parameter.getType();
            if(paramClass.equals(String.class)){
                objectList.add(JParamEnum.StringParam.getOne());
            }else{
                //认为他是一个类，具有自己的属性
                Object entityObject = explainEntityObject(paramClass);
                if(entityObject!=null) {
                    objectList.add(entityObject);
                }
            }
        }
        return objectList.toArray();
    }

    public Object explainEntityObject(Class<?> clazz){
        Field[] fields = clazz.getDeclaredFields();
        if(fields!=null&&fields.length>0){
            try {
                Object classObject = clazz.newInstance();
                for (Field field : fields) {
                    explainField(classObject, field);
                }
                return classObject;
            }catch (Exception e ){
                e.printStackTrace();
            }
        }
        return new Object();
    }

    /**
     * 这里要自动装配属性值
     * @param clazz
     * @param field
     */
    public void explainField(Object clazz,Field field){
        field.setAccessible(true);
        Class fieldType = field.getType();
        try {
            if (Integer.class.equals(fieldType)) {
                field.set(clazz,55555555);
            } else if (String.class.equals(fieldType)) {
                field.set(clazz, "111111");
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void invokeMethod(){
        Set<Class<?>> classes = reflectProxyMap.keySet();
        for (Class<?> clazz : classes) {
            List<JTMethod> jtMethodList = reflectProxyMap.get(clazz);
            for (JTMethod jtMethod : jtMethodList) {
                try {
                    jtMethod.getMethod().invoke(clazz.newInstance(),jtMethod.getParams());
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }catch (InstantiationException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
