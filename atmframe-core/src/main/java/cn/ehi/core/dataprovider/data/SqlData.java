package cn.ehi.core.dataprovider.data;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author 33053
 * @create 2018/12/7
 * 〈Sql信息〉
 */
public class SqlData {
    String id;
    String sql;
    String type;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSql() {
        return sql;
    }

    public void setSql(String sql) {
        this.sql = sql;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
