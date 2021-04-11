package js.mybatis.join.sql;

import java.util.Set;

public class SelectSpliceSql implements ISpliceSql{

    @Override
    public String spliceSql(String tableName, Set<String> tableField) {
        StringBuilder sqlBuilder = new StringBuilder();
        sqlBuilder.append("SELECT ");
        for (String s : tableField) {
            sqlBuilder.append(tableName).append(".").append(s).append(",");
        }
        String newSql = sqlBuilder.substring(0,sqlBuilder.length()-1);
        sqlBuilder = new StringBuilder(newSql);
        sqlBuilder.append(" FROM ");
        sqlBuilder.append(tableName);
        return sqlBuilder.toString();
    }
}
