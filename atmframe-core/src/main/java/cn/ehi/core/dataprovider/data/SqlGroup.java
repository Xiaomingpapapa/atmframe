package cn.ehi.core.dataprovider.data;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 33053
 * @create 2018/12/7
 * 〈测试用例sql组〉
 */
public class SqlGroup {
    String caseName;
    List<SqlData> sqlDataList = new ArrayList<>();

    public String getCaseName() {
        return caseName;
    }

    public void setCaseName(String caseName) {
        this.caseName = caseName;
    }

    public List<SqlData> getSqlDataList() {
        return sqlDataList;
    }

    public void addSqlData(SqlData sqlData) {
        this.sqlDataList.add(sqlData);
    }

    public SqlData getSqlData(String id) {
        return sqlDataList.stream().filter(a -> id.equals(a.id)).findFirst().get();
    }
}
