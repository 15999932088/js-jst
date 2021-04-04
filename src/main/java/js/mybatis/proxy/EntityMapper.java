package js.mybatis.proxy;

import js.mybatis.mapper.SqlMapper;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class EntityMapper implements SqlMapper {

    @Autowired
    SqlSessionFactory sqlSessionFactory;

    @Override
    public List<String> selectMapper() {
        SqlSessionTemplate sqlSessionTemplate = new SqlSessionTemplate(sqlSessionFactory);
        List<String> dataList = sqlSessionTemplate.getMapper(SqlMapper.class).selectMapper();
//        sqlSessionTemplate.close();
        return dataList;
    }
}
