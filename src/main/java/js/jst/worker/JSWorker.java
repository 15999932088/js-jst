package js.jst.worker;

import com.alibaba.fastjson.JSON;
import js.jst.interfaces.JTScan;
import js.mybatis.mapper.ISqlMapper;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.*;

public class JSWorker{

    List<String> classNames;

    public List<String> getClassNames() {
        return classNames;
    }

    public void setClassNames(List<String> classNames) {
        this.classNames = classNames;
    }

}
