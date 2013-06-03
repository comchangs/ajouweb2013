package ajou.web.mysearch.model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class ParseNaverDictionaryDB {
	private String dburl = "";
	private String dbuser = "";
	private String dbpass = "";
	
	public void setDbUrl(String url) {
		dburl = url;
	}
	
	public String getDbUrl() {
		return dburl;
	}
	
	public void setDbUser(String user) {
		dbuser = user;
	}
	
	public String getDbUser() {
		return dbuser;
	}
	
	public void setDbPass(String pass) {
		dbpass = pass;
	}
	
	public String getDbPass () {
		return dbpass;
	}
	
	/**
	 * Function for opening connection to the database
	 */
	public Connection getConnection() {
		
		Connection conn = null;
		try {
			conn = DriverManager.getConnection(getDbUrl(),getDbUser(),getDbPass());
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return conn;
	}
	
	/**
	 * Function to close open connection to database
	 */
	public void closeConnection(Connection conn) {
		if(conn != null) {
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
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
}
