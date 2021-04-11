package js.mybatis.mapper;

import com.alibaba.fastjson.JSON;
import js.mybatis.join.sql.SelectSpliceSql;
import js.mybatis.join.sql.SpliceSqlContext;
import org.apache.ibatis.executor.BatchResult;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.*;
import java.util.*;

@Component
public class SqlMapper extends AbsSqlMapper implements ISqlMapper, BeanFactoryAware {

//    @Autowired
    SqlSessionFactory sqlSessionFactory;

    public List<Map<String, Object>> selectMapper(String tableName, Set<String> tableField) {
        SqlSession sqlSession = null;
        Connection connection = null;
        try{
            List<Map<String, Object>> resultMapList = new ArrayList<>();
            String sql = new SpliceSqlContext(new SelectSpliceSql()).spliceSql(tableName,tableField);
            sqlSession = sqlSessionFactory.openSession(false);
            connection = sqlSession.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery();
            while(resultSet.next()){
                Map<String,Object> colMap = new HashMap<>();
                for (String field : tableField) {
                    colMap.put(field,resultSet.getObject(field));
                }
                resultMapList.add(colMap);
            }
            return resultMapList;
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if(connection!=null){
                try {
                    connection.close();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
            if(sqlSession!=null){
                sqlSession.close();
            }
        }
        return null;
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.sqlSessionFactory = beanFactory.getBean(SqlSessionFactory.class);
    }
}
