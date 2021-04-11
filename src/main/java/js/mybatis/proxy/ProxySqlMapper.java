package js.mybatis.proxy;

import javassist.*;
import javassist.bytecode.AnnotationsAttribute;
import javassist.bytecode.ConstPool;
import javassist.bytecode.MethodInfo;
import javassist.bytecode.annotation.Annotation;
import javassist.bytecode.annotation.MemberValue;
import javassist.bytecode.annotation.StringMemberValue;
import js.mybatis.mapper.ISqlMapper;
import js.mybatis.mapper.ISqlMapper;
import org.apache.ibatis.annotations.Select;

@Deprecated
public class ProxySqlMapper {

    public static Class proxySqlMapper(String sql){
        try {
            ClassPool classPool = ClassPool.getDefault();
            CtClass ctClass = classPool.get(ISqlMapper.class.getName());
            CtMethod selectMapper = ctClass.getDeclaredMethod("selectMapper");
            MethodInfo methodInfo = selectMapper.getMethodInfo();
            ConstPool constPool = methodInfo.getConstPool();
            AnnotationsAttribute annotationsAttribute = new AnnotationsAttribute(constPool, AnnotationsAttribute.visibleTag);
            Annotation annotation = new Annotation(Select.class.getName(), constPool);
//            annotation.addMemberValue("value", new StringMemberValue("select * from `user`", constPool));
            annotation.addMemberValue("value", new StringMemberValue(sql, constPool));
            annotationsAttribute.setAnnotation(annotation);
            methodInfo.addAttribute(annotationsAttribute);
//            ctClass.writeFile();
            Loader loadClass = new Loader(classPool);
            return loadClass.loadClass(ISqlMapper.class.getName());
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
}
