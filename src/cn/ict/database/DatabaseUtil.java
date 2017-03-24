package cn.ict.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import cn.ict.util.Constant;

public class DatabaseUtil {

	private static Connection connection = null;
	private static Statement statement = null;
	private static ResultSet result = null;
	private static boolean isInit = false;
	private static int totalSize = 0;

	public static int getTotalSize()
	{
		if(!isInit){
			init();
		}
		return totalSize;
	}
	public static void init() {
		try {
			Class.forName("org.sqlite.JDBC");
			connection = DriverManager.getConnection("jdbc:sqlite:/"
					+ Constant.DATAPATH);
			connection.setAutoCommit(false);
			statement = connection.createStatement();
			isInit = true;
			freshTotalSize();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public static  void freshTotalSize()
	{
		if (isInit == false)
			init();
		try {
			result = statement
			.executeQuery("SELECT count(*) FROM SportsNews " );
			result.next();
			totalSize = result.getInt(1);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public static void close() {
		try {
			isInit = false;
			if (statement != null)
				statement.close();
			if (connection != null)
				connection.close();
		} catch (Exception e) {
			// TODO: handle exception
		}

	}

	public static JavaBean getItemByUrl(String url) {
		JavaBean javaBean = null;
		if (isInit == false)
			init();
		try {
			result = statement
					.executeQuery("SELECT * FROM SportsNews WHERE url =  "
							+ "'" + url + "'");
			if (result.next()) {
				javaBean = new JavaBean(result.getString("id"),
						result.getString("url"), result.getString("title"),
						result.getString("content"), result.getLong("comment"),
						result.getString("keyword"), result.getString("time"));
			}
			result = null;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return javaBean;
	}

	public static JavaBean getItemById(String id) {
		JavaBean javaBean = null;
		if (isInit == false)
			init();
		try {
			result = statement
					.executeQuery("SELECT * FROM SportsNews WHERE id = " + "'"
							+ id + "'");
			if (result.next()) {
				javaBean = new JavaBean(result.getString("id"),
						result.getString("url"), result.getString("title"),
						result.getString("content"), result.getLong("comment"),
						result.getString("keyword"), result.getString("time"));
			}
			result = null;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//System.out.println(javaBean);
		return javaBean;
	}

	public static List<JavaBean> getAllItems() {
		if (!isInit)
			init();
		ArrayList<JavaBean> resultList = new ArrayList<JavaBean>();
		JavaBean javaBean = null;
		try {
			result = statement.executeQuery("SELECT * FROM SportsNews");
			while (result.next()) {
				javaBean = new JavaBean(result.getString("id"),
						result.getString("url"), result.getString("title"),
						result.getString("content"), result.getLong("comment"),
						result.getString("keyword"), result.getString("time"));
				resultList.add(javaBean);
			}
			result = null;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return resultList;
	}
}
