package js.mybatis.join.sql;

import java.util.Set;

public class SpliceSqlContext {

    ISpliceSql iSpliceSql;

    public SpliceSqlContext(ISpliceSql iSpliceSql){
        this.iSpliceSql = iSpliceSql;
    }

    public String spliceSql(String tableName, Set<String> tableField){
        return iSpliceSql.spliceSql(tableName,tableField);
    }

}
