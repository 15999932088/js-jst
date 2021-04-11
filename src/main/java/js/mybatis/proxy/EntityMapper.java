package js.mybatis.proxy;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.Loader;
import js.mybatis.config.MyBatisConfig;
import js.mybatis.mapper.ISqlMapper;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

@Component
public class EntityMapper{

    @Autowired
    SqlSessionFactory sqlSessionFactory;

    private String mapperSql = "select * from `user`";

    public void setMapperSql(String mapperSql) {
        this.mapperSql = mapperSql;
    }

    public List<Map<String,Object>> selectMapper(String sql) {
        try {
            SqlSessionTemplate sqlSessionTemplate = new SqlSessionTemplate(sqlSessionFactory);
            Connection connection = sqlSessionTemplate.getConnection();
//        Class proxyClass = ProxySqlMapper.proxySqlMapper(mapperSql);
            ClassPool classPool = ClassPool.getDefault();
            CtClass ctClass = classPool.get(ISqlMapper.class.getName());
            Loader loadClass = new Loader(classPool);
            Class proxyClass = loadClass.loadClass(ISqlMapper.class.getName());
            //要在扫描之前把类生成
            ISqlMapper sqlMapper = (ISqlMapper) sqlSessionTemplate.getMapper(proxyClass);
//            List<Map<String, Object>> dataList = sqlMapper.selectMapper(sql);
            List<Map<String, Object>> dataList = null;
            connection.close();
            return dataList;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

}
