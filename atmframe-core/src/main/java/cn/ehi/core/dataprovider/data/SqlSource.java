package cn.ehi.core.dataprovider.data;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author 33053
 * @create 2018/12/7
 * 〈Sql数据解析〉
 */
public class SqlSource {
    Map<String, SqlGroup> sqlGroups = new HashMap<>();

    public void addSqlGroup(SqlGroup sqlGroup) {
        sqlGroups.put(sqlGroup.getCaseName(), sqlGroup);
    }

    public SqlGroup getSqlGroup(String key) {
        return sqlGroups.get(key);
    }

}
