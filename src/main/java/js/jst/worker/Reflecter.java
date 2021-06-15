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
                            //如果注解是JCTest的情况下,说明要进行单元测试
                            if(annotation.annotationType().equals(JCTest.class)){
                                //保存当前注解下的method
                                //开始解读这个方法,并读取方法的具体参数.
                                JTMethod jtMethod = new JTMethod();
                                jtMethod.setMethod(method);
                                Parameter[] parameters = method.getParameters();
                                //解析这些参数的名,并且根据sql的名称进行与数据库对接,产出具体的sql执行
                                jtMethod.setParams(explainParams(parameters));
                                methodList.add(jtMethod);
                            }
                        }
                    }
                    //先封装,等待后面执行方法
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

    /**
     * 解析获取父类的属性
     * @param fieldList
     * @param clazz
     * @return
     */
    public List<Field> explainEntitySuperObject(List<Field> fieldList,Class<?> clazz){
        fieldList.addAll(Arrays.asList(clazz.getDeclaredFields()));
        Class<?> superClass = clazz.getSuperclass();
        if(superClass!=null){
            explainEntitySuperObject(fieldList,superClass);
        }
        return fieldList;
    }
    /**
     * 解析实体,将他们的属性以sql的列名做匹配,完成sql的查询
     * @param clazz
     * @return
     */
    public Object explainEntityObject(Class<?> clazz){
        try {
            //递归获取这个类的父类所有的属性
//            Field[] fields = clazz.getDeclaredFields();
            List<Field> fields = new ArrayList<>();
            explainEntitySuperObject(fields,clazz);
            //获取表字段，进行查询操作,代理修改SqlMapper的类
            if (ENTITY_TABLE_NAME != null && ENTITY_TABLE_NAME.length() > 0) {
                if (fields != null && fields.size() > 0) {
                    for (Field field : fields) {
                        ENTITY_TABLE_FIELD.add(strTohump(field.getName()));
                    }
                }
                Object classObject = clazz.newInstance();
                ISqlMapper sqlMapper = SpringUtils.getBean(SqlMapper.class);
                List<Map<String, Object>> sqlDataList = sqlMapper.selectMapper(ENTITY_TABLE_NAME, ENTITY_TABLE_FIELD);
                if (fields != null && fields.size() > 0) {
                    for (Field field : fields) {
                        explainField(classObject, field,strTohump(field.getName()), sqlDataList);
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
    public void explainField(Object clazz,Field field,String fieldName,List<Map<String,Object>> dataList){
        field.setAccessible(true);
        Class fieldType = field.getType();
        try {
            boolean hasSetValue = false;
            if(dataList!=null&&dataList.size()>0){
                Map<String,Object> dataMap = dataList.get(RandomUtils.getRandom(dataList.size()));
                if(dataMap.containsKey(fieldName)){
                    hasSetValue = true;
                    if(Integer.class.equals(fieldType)){
                        field.set(clazz,Integer.valueOf(String.valueOf(dataMap.get(fieldName))));
                    }
                    if(String.class.equals(fieldType)){
                        field.set(clazz,String.valueOf(dataMap.get(fieldName)));
                    }
                }
            }
            if(!hasSetValue){
                //TODO:是否有注解

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

    //转成驼峰式字符串
    public String strTohump(String str){
        char[] strArray =  str.toCharArray();
        StringBuilder stringBuilder = new StringBuilder();
        for(char c:strArray){
            if(c>='A'&&c<='Z'){
                stringBuilder.append("_");
                c = (char) (c+32);
            }
            stringBuilder.append(c);
        }
        return stringBuilder.toString();
    }
}
