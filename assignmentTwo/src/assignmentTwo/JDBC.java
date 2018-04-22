package assignmentTwo;
import java.awt.EventQueue;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class JDBC
{
	// For a more in-depth tutorial see:
	// https://www.tutorialspoint.com/jdbc/index.htm

	// IMPORTANT: Make sure all imports for the SQL stuff use java.sql !!!

	/**
	 * This is the recommended way to activate the JDBC drivers, but is only setup
	 * to work with one specific driver. Setup to work with a MySQL JDBC driver.
	 *
	 * If the JDBC Jar file is not in your build path this will not work. I have the
	 * Jar file posted in D2L.
	 * 
	 * @return Returns true if it successfully sets up the driver.
	 */
	public boolean activateJDBC()
	{
		try
		{
			DriverManager.registerDriver(new com.mysql.jdbc.Driver());
		} catch (SQLException sqle)
		{
			sqle.printStackTrace();
		}

		return true;
	}

	/**
	 * You MUST change these values based on the DB you are assigned to work with.
	 */
	public static final String DB_LOCATION = "jdbc:mysql://db.cs.ship.edu:3306/csc371-01";
	public static final String LOGIN_NAME = "csc371-01";
	public static final String PASSWORD = "Password01";
	protected Connection m_dbConn = null;


	public JDBC() throws Exception
	{

		/**
		 * Creates a connection to the database that you can then send commands to.
		 */
		m_dbConn = DriverManager.getConnection(DB_LOCATION, LOGIN_NAME, PASSWORD);

		/**
		 * To get the meta data for the DB.
		 */
		DatabaseMetaData meta = m_dbConn.getMetaData();

	}
	
	   /**
	    * To execute an SQL statement that is not a SELECT statement.
	    */
	    public void testNonSelectStatements() throws Exception
	    {
	        // Using Statement to insert a value
	        // Best used when all values are hard coded.
	        Statement stmt = m_dbConn.createStatement();
	        String insertData = new String("INSERT INTO JOBS (Job,Fname) VALUES ('Jobs','Name')");
	        int rowsAffected = stmt.executeUpdate(insertData);
	        if (rowsAffected == 1)
	        {
	        	System.out.println("Added");
	        }

	        // Using a PreparedStatement to insert a value (best option when providing values
	        // from variables).
	        // Use place holders '?' to mark where I am going to provide the data.
	        insertData = new String("INSERT INTO JOBS (Job,Fname) VALUES (?,?)");
	        PreparedStatement stmt2 = m_dbConn.prepareStatement(insertData);
	        stmt2.setString(1, "Jobs 'DROP");
	        stmt2.setString(2, "Name");
	        // When I need to set a primitive type as null.
	        //stmt2.setNull(2, java.sql.Types.INTEGER);
	        int rowsAdded = stmt2.executeUpdate();
	        if (rowsAdded == 1)
	        {
	        	System.out.println("Added");
	        }
	    }


	    /**
	     * To execute an SQL statement that is a SELECT statement.
	     * @throws SQLException 
	     */
	     public void testSelectStatements() throws SQLException
	     {
	          String selectData = new String("SELECT * FROM TEST_amin WHERE row1=1");
	          Statement stmt = m_dbConn.createStatement();
	          ResultSet rs = stmt.executeQuery(selectData);
	          while (rs.next())
	          {
	          // You can access values from a ResultSet either by column number - not advised:
	          String data = rs.getString(1);
	          System.out.print(data+" : ");
	          // Or by column name - advised:
	          data = rs.getString("row3");
	          System.out.println(data);
	          }
	      }
	     
	     public void showTables() throws SQLException 
	     {
	          String selectData = new String("show tables");
	          Statement stmt = m_dbConn.createStatement();
	          ResultSet rs = stmt.executeQuery(selectData);
	          while (rs.next())
	          {
	          // You can access values from a ResultSet either by column number - not advised:
	          String data = rs.getString(1);
	          System.out.print(data+" : ");
	          // Or by column name - advised:
	          data = rs.getString("Tables_in_csc371-01");
	          System.out.println(data);
	          }
	     }

	/**
	 * This program make insert project and select project
	 * 
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception
	{

		JDBC object = new JDBC();
		//object.testSelectStatements();
		
		EventQueue.invokeLater(new Runnable()
		{
			public void run()
			{
				try
				{
					tableGui frame = new tableGui();
					frame.setVisible(true);
				} catch (Exception e)
				{
					e.printStackTrace();
				}
			}
		});
	
	}
	
	public void buildTables() throws SQLException
	{
		String firstHalf = "CREATE TABLE TEST"; 
		String secondHalf = "(Num1 INT, Num2 INT, str1 CHAR(10), str2 VARCHAR(30), deci DOUBLE, PRIMARY KEY(Num1))";
		for(int i = 0 ; i < 10; i++)
		{
			Statement statement = m_dbConn.createStatement();
			statement.execute(firstHalf + i +secondHalf);
		}
	}
	
	public void dropTables() throws SQLException
	{
		String s = "DROP TABLE TEST";
		for (int i = 0; i < 10; i ++)
		{
			Statement statement = m_dbConn.createStatement();
			statement.execute(" " + s + i);
		}
	}
	
	public void populateTables() throws SQLException
	{
		for(int j = 0; j < 10; j++)
		{
			String insertData = "INSERT INTO TEST" + j +" (Num1, Num2, str1, str2, deci)VALUES(?,?,?,?,?)";
			PreparedStatement stmt2 = m_dbConn.prepareStatement(insertData);

	    	 for (int i = 0 ; i < 5; i++)
	    	 {

		         stmt2.setInt(1, i);
		         stmt2.setInt(2, i+1);
		         stmt2.setString(3, "this" + i );
		         stmt2.setString(4, "that" + i);
		         stmt2.setFloat(5,i);
		         stmt2.executeUpdate();
	    	 }
		}
	}
}