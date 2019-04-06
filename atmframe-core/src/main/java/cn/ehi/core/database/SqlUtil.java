package cn.ehi.core.database;

import cn.ehi.core.config.Configuration;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SqlUtil {
    public final static String DB_DRVIER = "dbdriver";
    public final static String DB_URL = "dbURL";
    public final static String DB_USER = "user";
    public final static String DB_PASSWORD = "password";
	private static  Connection conn;
	private static Configuration conf = new Configuration();
	/**
	 * 连接数据库
	 * @return
	 */
	public static Connection getCon() {
		if (conn == null) {
			createConn();
		}
		return conn;
	}

	/**
	 * 创建连接，数据库用户名和密码从config里读取
	 */
	public static void createConn() {
		try {
			Class.forName(conf.get(DB_DRVIER));
			conn = DriverManager.getConnection(conf.get(DB_URL),conf.get(DB_USER), conf.get(DB_PASSWORD));
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 执行sql语句返回一个list
	 * @param sql
	 * @return
	 */
	public  static List<Map<String, Object>> execSql(String sql) {
		if(conn == null){
			createConn();
		}
		Statement stmt = null;
		ResultSet rs = null;
		List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
		
		try {
			stmt = conn.createStatement();
			rs = stmt.executeQuery(sql);
			while (rs.next()) {
				Map<String, Object> map = new HashMap<String, Object>();
				for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++) {
					map.put(rs.getMetaData().getColumnName(i), rs.getString(i));
				}
				result.add(map);
			}
		} catch (SQLException e) {
		} finally {
			if (stmt != null) {
				try {
					stmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}

		return result;
	}

	/**
	 * 根据名称来执行sql语句病返回一条数据
	 * @param sql
	 * @param column
	 * @return
	 */
	public  static String execSqlGetOne(String sql,String column) {
		String result=null;
		if(conn == null){
			createConn();
		}

		Statement stmt = null;
		ResultSet rs = null;
			try {
				stmt = conn.createStatement();
				rs = stmt.executeQuery(sql);
				while (rs.next()) {
					result=rs.getString(column);	
			      }
			    } catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
					if (stmt != null) {
						try {
							stmt.close();
						} catch (SQLException e) {
							e.printStackTrace();
						}
					}
					if (rs != null) {
						try {
							rs.close();
						} catch (SQLException e) {
							e.printStackTrace();
						}
					}
					
				}
			return result;
	}
	
	/**
	 * 根据sql表的名称执行sql并返回一个list
	 * @param sql
	 * @param column
	 * @return
	 */
	public static List<String> execSqlGetList(String sql,String column) {
		String result=null;
		List<String> list = new ArrayList<String>();
		if(conn == null){
			createConn();
		}

		Statement stmt = null;
		ResultSet rs = null;

			try {
				stmt = conn.createStatement();
				rs = stmt.executeQuery(sql);
				while (rs.next()) {
					result=rs.getString(column);
                    list.add(result);
					}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
					if (stmt != null) {
						try {
							stmt.close();
						} catch (SQLException e) {
							e.printStackTrace();
						}
					}
					if (rs != null) {
						try {
							rs.close();
						} catch (SQLException e) {
							e.printStackTrace();
						}
					}
				}
		return list;
	}

	/**
	 * 执行数据库更新操作
	 * @param sql
	 */
	public static void execUpdateSql(String sql,int i) {

		if(conn == null){
			createConn();
		}

		Statement stmt = null;
		ResultSet rs = null;

			try {
				stmt = conn.createStatement();
				stmt.executeUpdate(sql,i);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
					if (stmt != null) {
						try {
							stmt.close();
						} catch (SQLException e) {
							e.printStackTrace();
						}
					}
					if (rs != null) {
						try {
							rs.close();
						} catch (SQLException e) {
							e.printStackTrace();
						}
					}
				}

	}
	
	/**
	 * 返回数据库的记录数
	 */
	public  static int execSqlGetRowNum(String sql){
		if(conn == null){
			createConn();
		}
		Statement stmt = null;
		ResultSet rs = null;
        int RowNum = 0;
			try {
				stmt = conn.createStatement();
				rs = stmt.executeQuery(sql);
				while(rs.next()){
					RowNum++;
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
					if (stmt != null) {
						try {
							stmt.close();
						} catch (SQLException e) {
							e.printStackTrace();
						}
					}
					if (rs != null) {
						try {
							rs.close();
						} catch (SQLException e) {
							e.printStackTrace();
						}
					}
				}
		return RowNum;
	}
	
	/**
	 * 关闭数据库连接
	 */
	public  void close() {
		if (conn != null) {
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

}
