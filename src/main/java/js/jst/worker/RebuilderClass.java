package js.jst.worker;

import js.jst.interfaces.JCTest;
import js.jst.interfaces.JEntity;
import js.jst.param.JTMethod;
import js.jst.param.enums.JParamEnum;
import js.mybatis.join.sql.SelectSpliceSql;
import js.mybatis.join.sql.SpliceSqlContext;
import js.mybatis.proxy.ProxySqlMapper;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.*;

public class RebuilderClass {

    private String ENTITY_TABLE_NAME = "";

    private Set<String> ENTITY_TABLE_FIELD = new HashSet<>();

    public RebuilderClass(String className){
        try {
            //根据反射来获取这个类的是否存在测试的注解
            Class<?> clazz = Class.forName(className);
            Method[] methods = clazz.getMethods();
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
                                rebuilderClassParamToAddAnnon(parameters);
                            }
                        }
                    }
                }
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void rebuilderClassParamToAddAnnon(Parameter[] parameters){
        for (Parameter parameter : parameters) {
            Class<?> paramClass = parameter.getType();
            JEntity jEntity = paramClass.getAnnotation(JEntity.class);
            if(jEntity!=null){
                ENTITY_TABLE_NAME = jEntity.value();
                InitializingNewClass(paramClass);
            }
        }
    }

    private void InitializingNewClass(Class<?> clazz){
        Field[] fields = clazz.getDeclaredFields();
        //获取表字段，进行查询操作,代理修改SqlMapper的类
        if(ENTITY_TABLE_NAME!=null&&ENTITY_TABLE_NAME.length()>0){
            if(fields!=null&&fields.length>0){
                for (Field field : fields) {
                    ENTITY_TABLE_FIELD.add(field.getName());
                }
            }
            //进行表的查询,然后分别对字段进行
            String sql = new SpliceSqlContext(new SelectSpliceSql()).spliceSql(ENTITY_TABLE_NAME,ENTITY_TABLE_FIELD);
            //创建类
            Class proxyMapperClass = ProxySqlMapper.proxySqlMapper(sql);
            System.out.println("***************");
        }
    }

}
