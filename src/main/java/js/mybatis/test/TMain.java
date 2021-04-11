package js.mybatis.test;


import com.alibaba.fastjson.JSON;
import js.mybatis.mapper.SqlMapper;
import js.mybatis.mapper.ISqlMapper;
import js.mybatis.proxy.EntityMapper;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@ComponentScan("js.mybatis")
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
