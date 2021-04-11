package js.mybatis.config;

import org.apache.ibatis.datasource.DataSourceFactory;
import org.apache.ibatis.datasource.pooled.PooledDataSource;
import org.apache.ibatis.datasource.pooled.PooledDataSourceFactory;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.sql.Connection;
import java.sql.Statement;

@Configuration
public class MyBatisConfig {

    @Bean
    public PooledDataSource dataSource(){
        PooledDataSource pooledDataSource = new PooledDataSource();
//        pooledDataSource.setDriver("com.mysql.jdbc.Driver");
        pooledDataSource.setDriver("com.mysql.cj.jdbc.Driver");
        pooledDataSource.setUsername("root");
        pooledDataSource.setPassword("123546");
        pooledDataSource.setUrl("jdbc:mysql://127.0.0.1:3306/basic_frame?serverTimezone=UTC");
//        pooledDataSource.setDefaultAutoCommit(false);
        return pooledDataSource;

    }


    @Bean
    public SqlSessionFactory sqlSessionFactory(PooledDataSource dataSource) throws Exception {
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        sqlSessionFactoryBean.setDataSource(dataSource);
        return sqlSessionFactoryBean.getObject();
    }

//    public void test(){
//        try {
//            SqlSession sqlSession = sqlSessionFactory(dataSource()).openSession();
//            Connection connection = sqlSession.getConnection();
//            Statement statement = connection.createStatement();
//            statement.execute("sql");
//            statement.close();
//            connection.commit();
//            connection.close();
//            sqlSession.close();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
}
