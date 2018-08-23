package dbms;
import java.sql.*;
import com.mysql.cj.jdbc.MysqlDataSource;

public class DBConnector {
	private static DBConnector instance=null;
	private MysqlDataSource dataSource;

	// Singleton class
	private DBConnector() {
		dataSource = new MysqlDataSource();
		dataSource.setUrl ( "jdbc:mysql://localhost:3360/politics_app" );
		dataSource.setUser( "root" );
		dataSource.setPassword( "root" );
	}
	
	public static DBConnector getInstance() {
		if(DBConnector.instance == null)
			return new DBConnector();
		else
			return DBConnector.instance;
	}
	
	public PreparedStatement createPreparedStatement(String queryWithFormat) throws SQLException {
		Connection con = dataSource.getConnection();
		return con.prepareStatement( queryWithFormat );
	}
	
	// Note: this method is vulnerable to SQL injection! 
	private ResultSet execSQL(String query) throws SQLException {
		ResultSet rs = null;
		Connection con = dataSource.getConnection();
		Statement stmt = con.createStatement();
		rs = stmt.executeQuery(query);
		con.close();
		return rs;
	}
	
	// If used right, not vulnerable to SQL injection
	public ResultSet execSQL(PreparedStatement stmt) {
		ResultSet rs = null;
		try {
			Connection con = dataSource.getConnection();
//			PreparedStatement statement = connection.prepareStatement("INSERT INTO table (field1) VALUES (?)");
//			statement.setString(1, args);
//			statement.executeQuery();
			rs = stmt.executeQuery();
			con.close();
		} catch (Exception e) {
			System.out.println(e);
		}
		return rs;
	}

	public static void main(String[] args) {
		System.out.println("starting");
		DBConnector connector = new DBConnector();
		System.out.println("initialized");
		try {
			connector.execSQL("select * from test");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("done stuff");
	}
	
}
