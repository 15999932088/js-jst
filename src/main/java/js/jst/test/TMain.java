package js.jst.test;

import com.alibaba.fastjson.JSON;
import js.jst.interfaces.JTScan;
import js.jst.worker.JSScanner;
import js.jst.worker.JSWorker;
import js.mybatis.mapper.SqlMapper;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;

import java.util.HashSet;
import java.util.Set;

//@ComponentScan("js.jst.test")
@ComponentScan({"js.mybatis","js.jst"})
@JTScan("js.jst.test.test")
@MapperScan("js.mybatis.mapper")
public class TMain {

    public static void main(String[] args) {
        AnnotationConfigApplicationContext ac = new AnnotationConfigApplicationContext(TMain.class);
        SqlMapper sqlExecuteMapper = ac.getBean(SqlMapper.class);
        Set<String> hashSet  =new HashSet<>();
        hashSet.add("id");
        hashSet.add("name");
        sqlExecuteMapper.selectMapper("user",hashSet);
    }
}
