package js.jst.worker;

import com.alibaba.fastjson.JSON;
import js.jst.interfaces.JCTest;
import js.jst.interfaces.JEntity;
import js.jst.param.JTMethod;
import js.jst.param.enums.JParamEnum;
import js.jst.tool.RandomUtils;
import js.jst.tool.SpringUtils;
import js.mybatis.join.sql.ISpliceSql;
import js.mybatis.join.sql.SelectSpliceSql;
import js.mybatis.join.sql.SpliceSqlContext;
import js.mybatis.mapper.AbsSqlMapper;
import js.mybatis.mapper.ISqlMapper;
import js.mybatis.mapper.SqlMapper;
import js.mybatis.proxy.EntityMapper;
import js.mybatis.proxy.ProxySqlMapper;
import org.springframework.util.StringUtils;

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


    /**
     * 反射封装好方法
     * @param className
     */
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


    private String ENTITY_TABLE_NAME = "";

    private Set<String> ENTITY_TABLE_FIELD = new HashSet<>();
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
                JEntity jEntity = paramClass.getAnnotation(JEntity.class);
                if(jEntity!=null){
                    ENTITY_TABLE_NAME = jEntity.value();
                }
                Object clazz = explainEntityObject(paramClass);
                if(clazz!=null) {
                    objectList.add(clazz);
                }
            }
        }
        return objectList.toArray();
    }

    public Object explainEntityObject(Class<?> clazz){
        try {
            Field[] fields = clazz.getDeclaredFields();
            //获取表字段，进行查询操作,代理修改SqlMapper的类
            if (ENTITY_TABLE_NAME != null && ENTITY_TABLE_NAME.length() > 0) {
                if (fields != null && fields.length > 0) {
                    for (Field field : fields) {
                        ENTITY_TABLE_FIELD.add(field.getName());
                    }
                }
                Object classObject = clazz.newInstance();
                ISqlMapper sqlMapper = SpringUtils.getBean(SqlMapper.class);
                List<Map<String, Object>> sqlDataList = sqlMapper.selectMapper(ENTITY_TABLE_NAME, ENTITY_TABLE_FIELD);
                if (fields != null && fields.length > 0) {
                    for (Field field : fields) {
                        explainField(classObject, field, sqlDataList);
                    }
                }
                return classObject;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return new Object();
    }

    /**
     * 这里要自动装配属性值
     * @param clazz
     * @param field
     */
    public void explainField(Object clazz,Field field,List<Map<String,Object>> dataList){
        field.setAccessible(true);
        Class fieldType = field.getType();
        try {
            if(dataList!=null&&dataList.size()>0){
                Map<String,Object> dataMap = dataList.get(RandomUtils.getRandom(dataList.size()));
                if(dataMap.containsKey(field.getName())){
                    if(Integer.class.equals(fieldType)){
                        field.set(clazz,Integer.valueOf(String.valueOf(dataMap.get(field.getName()))));
                    }
                    if(String.class.equals(fieldType)){
                        field.set(clazz,String.valueOf(dataMap.get(field.getName())));
                    }
                }
            }
//            if (Integer.class.equals(fieldType)) {
//                field.set(clazz, 55555555);
//            } else if (String.class.equals(fieldType)) {
//                field.set(clazz, "111111");
//            }
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
