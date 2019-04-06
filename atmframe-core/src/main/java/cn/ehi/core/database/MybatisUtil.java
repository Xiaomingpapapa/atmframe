package cn.ehi.core.database;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * @author 33053
 * @create 2018/12/18
 * 〈mybatis工具类〉
 */
public class MybatisUtil {
    private static final Logger LOG = LoggerFactory.getLogger(MybatisUtil.class);
    private static SqlSession session;

    public static SqlSession getSession() {
        if (null == session) {
            session = createSession();
        }
        return session;
    }

    public static void commitSession() {
        if (null != session) {
            session.commit();
        }
    }

    private static SqlSession createSession() {
        SqlSessionFactory sqlSessionFactory = null;
        try {
            sqlSessionFactory = new SqlSessionFactoryBuilder().build(Resources.getResourceAsStream("mybatis/mybatis_config.xml"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        SqlSession session = sqlSessionFactory.openSession();
        return session;
    }

    public static void closeSession() {
        if (null != session) {
            session.close();
        }
    }

}
