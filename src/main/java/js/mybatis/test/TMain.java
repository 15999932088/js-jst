package js.mybatis.test;


import com.alibaba.fastjson.JSON;
import js.mybatis.mapper.SqlMapper;
import js.mybatis.proxy.EntityMapper;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;

import java.util.List;

@ComponentScan("js.mybatis")
@MapperScan("js.mybatis.mapper")
public class TMain {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext ac = new AnnotationConfigApplicationContext(TMain.class);

        SqlMapper sqlMapper = ac.getBean(EntityMapper.class);
        List<String> entity = sqlMapper.selectMapper();
        System.out.println("******************");
        System.out.println(JSON.toJSONString(entity));
    }
}
