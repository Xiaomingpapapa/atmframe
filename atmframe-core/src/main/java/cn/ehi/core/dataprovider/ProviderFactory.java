package cn.ehi.core.dataprovider;

import cn.ehi.core.annotation.DataFactory;
import cn.ehi.core.annotation.SqlExecute;
import cn.ehi.core.config.AppiumConfigUtil;
import cn.ehi.core.database.SqlUtil;
import cn.ehi.core.dataprovider.data.*;
import cn.ehi.core.dataprovider.reader.XmlReader;
import org.testng.annotations.DataProvider;

import java.lang.reflect.Method;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class ProviderFactory {
    private static ThreadLocal<DataSource> dataSourceThreadLocal = new ThreadLocal<>();
    private static ThreadLocal<SqlSource> sqlSourceThreadLocal = new ThreadLocal<>();

    @DataProvider(name = "TestData")
    public static Object[][] getRowsDataByTestMethodName(Method method) {
        if (null == dataSourceThreadLocal.get()) {
            DataSource dataSource = XmlReader.getDataList(AppiumConfigUtil.getAppiumConfig().getDataFile());
            dataSourceThreadLocal.set(dataSource);
        }
        if (null == sqlSourceThreadLocal.get()) {
            SqlSource sqlSource = XmlReader.getSqlSource(AppiumConfigUtil.getAppiumConfig().getSqlFile());
            sqlSourceThreadLocal.set(sqlSource);
        }

        DataFactory methoddf =method.getAnnotation(DataFactory.class);
        SqlExecute methodSqlExecute = method.getAnnotation(SqlExecute.class);
        String caseName = "";
        if (null != methoddf) {
            caseName = methoddf.caseName();
        }
        if (null != methodSqlExecute) {
            SqlGroup sqlGroup = sqlSourceThreadLocal.get().getSqlGroup(methodSqlExecute.caseName());
            String[] sqlId = methodSqlExecute.id().split(",");
            List<SqlData> sqlDataList = new ArrayList<>();
            for (String id : sqlId) {
                sqlDataList.add(sqlGroup.getSqlData(id));
            }
            sqlExcutor(sqlDataList);
        }
        List<BaseData> baseDataList = dataSourceThreadLocal.get().getBaseDataList(caseName);
        return DataSource.getBaseData(baseDataList);
    }

    public static void sqlExcutor(List<SqlData> sqlDataList) {
        sqlDataList.forEach(sqlData -> {
            if ("update".equals(sqlData.getType())) {
                SqlUtil.execUpdateSql(sqlData.getSql(), Statement.NO_GENERATED_KEYS);
            }
        });

    }
}

