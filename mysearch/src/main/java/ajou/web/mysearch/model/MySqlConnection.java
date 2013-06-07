package ajou.web.mysearch.model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class MySqlConnection {
	private String dburl = "";
	private String dbuser = "";
	private String dbpass = "";
	
	public MySqlConnection()
	{
		this.setDbUrl("jdbc:mysql://ec2-54-249-102-156.ap-northeast-1.compute.amazonaws.com:3306/nutch?useUnicode=true&amp;characterEncoding=UTF-8");
		this.setDbUser("webclass");
		this.setDbPass("webclass");
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (Exception e) {
			e.printStackTrace();
		}
		/**
		 * Function for opening connection to the database
		 */
	}
	
	private void setDbUrl(String url) {
		dburl = url;
	}
	
	private String getDbUrl() {
		return dburl;
	}
	
	private void setDbUser(String user) {
		dbuser = user;
	}
	
	private String getDbUser() {
		return dbuser;
	}
	
	private void setDbPass(String pass) {
		dbpass = pass;
	}
	
	private String getDbPass () {
		return dbpass;
	}

	private Connection getConnection() {
		
		Connection conn = null;
		try {
			conn = DriverManager.getConnection(getDbUrl(),getDbUser(),getDbPass());
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return conn;
	}
	
	/**
	 * Obtain data of a single user
	 * Simple mode
	 * @throws SQLException 
	 */
	
	public void insertDB(String query)
	{
		Connection con = null;
		try
		{
			con = getConnection();
			Statement st = con.createStatement();
			
			st.executeUpdate(query);

		}
		catch (SQLException e)
		{
			e.getStackTrace();
		}
		finally
		{
			try{
			if(con != null)
				con.close();
			}
			catch (SQLException e)
			{
				e.getStackTrace();
			}
		}
	}
	
	public ArrayList<String> selectDb(String query)
	{
		Connection con = null;
		ResultSet rs = null;
		PreparedStatement ps = null;
		ArrayList<String> result = new ArrayList<String>();
		
		try {
			con = getConnection();
			ps = con.prepareStatement(query);
			rs = ps.executeQuery();
			while(rs.next()) {
				result.add(new String(rs.getString(1)));
			}
		}
		catch (SQLException e)
		{
			e.getStackTrace();
		}
		finally
		{
			try{
			if(con != null)
				con.close();
			if(rs != null)
				rs.close();
			if(ps != null)
				ps.close();
			}
			
			catch (SQLException e)
			{
				e.getStackTrace();
			}
		}
		if(result.isEmpty())
			return null;
		return result;
	}
	
	public boolean getBookmarkUrl(String userId, String bookmarkUrl)
	{
		Connection con = null;
		ResultSet rs = null;
		PreparedStatement ps = null;
		String result = new String();
		
		try {
			con = getConnection();
			ps = con.prepareStatement("SELECT user_id FROM bookmark WHERE user_id='" + userId + "' AND url='" + bookmarkUrl + "'");
			rs = ps.executeQuery();
			if(rs.next()) {
				return true;
			}
			else
				return false;
		}
		catch (SQLException e)
		{
			e.getStackTrace();
		}
		finally
		{
			try{
			if(con != null)
				con.close();
			if(rs != null)
				rs.close();
			if(ps != null)
				ps.close();
			}
			
			catch (SQLException e)
			{
				e.getStackTrace();
			}
		}
		return false;
	}
	
	public String selectUserPasswod(String userId)
	{
		Connection con = null;
		ResultSet rs = null;
		PreparedStatement ps = null;
		String result = new String();
		
		try {
			con = getConnection();
			ps = con.prepareStatement("SELECT password FROM user WHERE user_id='" + userId + "'");
			rs = ps.executeQuery();
			while(rs.next()) {
				result = rs.getString(1);
			}
		}
		catch (SQLException e)
		{
			e.getStackTrace();
		}
		finally
		{
			try{
			if(con != null)
				con.close();
			if(rs != null)
				rs.close();
			if(ps != null)
				ps.close();
			}
			
			catch (SQLException e)
			{
				e.getStackTrace();
			}
		}

		return result;
	}
	
	public ArrayList<String> getAutoComplete(String name) {
		
		ArrayList<String> list = new ArrayList<String>();
		Connection conn = getConnection();
		if(conn != null) {
			ResultSet rs = null;
			PreparedStatement ps = null;
			try {
				String sqlQuery = "SELECT custname FROM custdata WHERE custname LIKE '"+name+"%'";
				System.out.println(sqlQuery);
				ps = conn.prepareStatement(sqlQuery);
				rs = ps.executeQuery();
				while (rs.next()) {
					list.add(rs.getString("custname"));
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return list;
	}
}
