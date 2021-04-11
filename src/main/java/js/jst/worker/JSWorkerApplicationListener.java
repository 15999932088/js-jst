package js.jst.worker;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

@Component
public class JSWorkerApplicationListener implements ApplicationListener {

    @Override
    public void onApplicationEvent(ApplicationEvent event) {
        AnnotationConfigApplicationContext ac = ((AnnotationConfigApplicationContext)event.getSource());
        JSWorker jsWorker = (JSWorker) ac.getBean("JSWorker");
        if(!CollectionUtils.isEmpty(jsWorker.getClassNames())){
            for(String className:jsWorker.getClassNames()){
                //反射这个类/或者对这个类进行代理
                //现在要想办法将
                Reflecter reflecter = new Reflecter();
                reflecter.reflectClass(className);
                reflecter.invokeMethod();
            }
        }
    }

}
