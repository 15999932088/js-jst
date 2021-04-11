package js.jst.worker;

import js.jst.interfaces.JTScan;
import js.mybatis.proxy.EntityMapper;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.factory.BeanNameAware;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.util.CollectionUtils;

import javax.annotation.PostConstruct;
import java.util.*;

public class JSWorkerRegistrar implements ImportBeanDefinitionRegistrar{


    //注册一个bean到容器中
    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
        Map<String,Object> jtScanMap = importingClassMetadata.getAnnotationAttributes(JTScan.class.getName());
        String[] packagePaths = (String[]) jtScanMap.get("value");
        Set<String> classNames = new HashSet<>();
        if(packagePaths!=null&&packagePaths.length>0) {
            for (String packagePath:packagePaths) {
                classNames.addAll(JSScanner.scanPackage(packagePath));
            }
        }
        BeanDefinition beanDefinition = BeanDefinitionBuilder.genericBeanDefinition(JSWorker.class).getBeanDefinition();
        MutablePropertyValues propertyValues = beanDefinition.getPropertyValues();
        propertyValues.add("classNames",classNames);
        registry.registerBeanDefinition("JSWorker",beanDefinition);
    }

}
