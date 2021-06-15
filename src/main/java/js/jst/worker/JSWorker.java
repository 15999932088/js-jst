package js.jst.worker;

import com.alibaba.fastjson.JSON;
import js.jst.interfaces.JTScan;
import js.mybatis.mapper.ISqlMapper;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.*;

/**
 * 用来接收注入内容,因为ImportBeanDefinitionRegistrar是beanDefinition注入之前的,bean尚未实例化
 * 所以用这个类来做中间级,将importBeanDeinitionRegistrar的数据存放在这里
 */
public class JSWorker{

    List<String> classNames;

    public List<String> getClassNames() {
        return classNames;
    }

    public void setClassNames(List<String> classNames) {
        this.classNames = classNames;
    }

}
