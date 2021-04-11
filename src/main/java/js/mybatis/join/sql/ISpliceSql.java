package js.mybatis.join.sql;

import java.util.Set;

public interface ISpliceSql {
    String spliceSql(String tableName, Set<String> tableField);
}
