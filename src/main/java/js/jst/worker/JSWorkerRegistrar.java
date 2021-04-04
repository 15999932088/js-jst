package js.jst.worker;

import js.jst.interfaces.JTScan;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.util.CollectionUtils;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class JSWorkerRegistrar implements ImportBeanDefinitionRegistrar {

    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
//        BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder.genericBeanDefinition(JSWorker.class);
        Map<String,Object> jtScanMap = importingClassMetadata.getAnnotationAttributes(JTScan.class.getName());
        String[] packagePaths = (String[]) jtScanMap.get("value");
        Set<String> classNames = new HashSet<>();
        if(packagePaths!=null&&packagePaths.length>0) {
            for (String packagePath:packagePaths) {
                classNames.addAll(JSScanner.scanPackage(packagePath));
            }
        }
        if(!CollectionUtils.isEmpty(classNames)){
            for(String className:classNames){
                //反射这个类/或者对这个类进行代理
                Reflecter rf = new Reflecter();
                rf.reflectClass(className);
                rf.invokeMethod();
            }
        }
    }

}
