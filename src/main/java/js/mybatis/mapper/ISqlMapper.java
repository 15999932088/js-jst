package js.mybatis.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;


import java.util.List;
import java.util.Map;
import java.util.Set;

public interface ISqlMapper {

//    @Select("select * from `user`")---用代理添加注解
    List<Map<String,Object>> selectMapper(String tableName, Set<String> tableField);
}
