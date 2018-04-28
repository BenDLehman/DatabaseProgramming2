package assignmentTwo;

import java.awt.EventQueue;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class JDBC
{
	public static final String DB_LOCATION = "jdbc:mysql://db.cs.ship.edu:3306/csc371-01";
	public static final String LOGIN_NAME = "csc371-01";
	public static final String PASSWORD = "Password01";
	protected Connection m_dbConn = null;
	private boolean lastQuerySuccessful;

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
		}
		catch (SQLException sqle)
		{
			sqle.printStackTrace();
		}

		return true;
	}

	/**
	 * Creates a connection to the database that you can then send commands to.
	 */
	public void createConnection()
	{
		try
		{
			m_dbConn = DriverManager.getConnection(DB_LOCATION, LOGIN_NAME, PASSWORD);
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * Check to see if the connection is null or closed and if so, create the
	 * connection.
	 * @throws SQLException
	 */
	public void checkConnection() throws SQLException
	{
		if (m_dbConn == null || m_dbConn.isClosed())
		{
			createConnection();
		}
	}
	
	public boolean wasLastQuerySuccessful()
	{
		return lastQuerySuccessful;
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

		// Using a PreparedStatement to insert a value (best option when providing
		// values
		// from variables).
		// Use place holders '?' to mark where I am going to provide the data.
		insertData = new String("INSERT INTO JOBS (Job,Fname) VALUES (?,?)");
		PreparedStatement stmt2 = m_dbConn.prepareStatement(insertData);
		stmt2.setString(1, "Jobs 'DROP");
		stmt2.setString(2, "Name");
		// When I need to set a primitive type as null.
		// stmt2.setNull(2, java.sql.Types.INTEGER);
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
			System.out.print(data + " : ");
			// Or by column name - advised:
			data = rs.getString("row3");
			System.out.println(data);
		}
	}
	
	/*public static void main(String[] args)
	{
		JDBC j = new JDBC();
		j.activateJDBC();
		try
		{
			j.select("*", "LOCATION", null, null);
		}
		catch (SQLException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}*/

	public ArrayList<DBRow> select(String what, String from, String whereKey, String whereValue) throws SQLException, IOException
	{
		checkConnection();
		
		ArrayList<DBRow> data = new ArrayList<DBRow>();
		boolean whereClause = (whereKey!=null && whereValue!=null) ? true : false;
		
		// Prepare the statements.
		String sql = "SELECT " + what + " FROM " + from;
		
		if(whereClause)
		{
			 sql += " WHERE " + whereKey + " = ?";
		}
		
		PreparedStatement stmt = m_dbConn.prepareStatement(sql);
		m_dbConn.setAutoCommit(false);
		
		if (whereClause)
		{
			// Decide whether whereValue is an int, and prepare statement appropriately
			int whereValueInt;
			boolean whereValueIsInt = isNumeric(whereValue);
			if (whereValueIsInt)
			{
				whereValueInt = Integer.parseInt(whereValue);
				stmt.setInt(1, whereValueInt);
			}
			else
			{
				stmt.setString(1, whereValue);
			}
		}
		
		System.out.println("Executing statement:\n\t"+stmt);

		System.out.println("Selecting data...");
		ResultSet results;
		
		// Start the 'timer'
		long startTime = System.nanoTime();

		// Execute the query
		results = stmt.executeQuery();
		m_dbConn.commit();
		
		lastQuerySuccessful = (results!=null) ? true : false;
		
		int count = 0;
		ResultSetMetaData metadata = results.getMetaData();
		while(results.next())
		{
			data.add(new DBRow());
			data.get(count).setNumColumns(metadata.getColumnCount());
			
			System.out.println(data.size());
			
			for(int x = 1; x < metadata.getColumnCount()+1; x++)
			{
				Object object = results.getObject(x);
				String type = object.getClass().getSimpleName();
				String value = object.toString();
				String name = metadata.getColumnName(x);
				
				System.out.println(type + " " + value + " " + name);
				data.get(count).addType(type);
				data.get(count).addValue(value);
				data.get(count).addColumLabel(name);
			}
			count++;
			System.out.println("");
		}
		
		
		// For testing. Will remove when done
		/*System.out.println("Column Labels");
		int index = 0;
		for(String s : data.get(index).getColumnLabels())
		{
			System.out.println(s);
			index++;
		}
		System.out.println("Values");
		index = 0;
		for(String s : data.get(index).getValues())
		{
			System.out.println(s);
			index++;
		}
		System.out.println("Types");
		index = 0;
		for(String s : data.get(index).getTypes())
		{
			System.out.println(s);
			index++;
		}*/

		// End the 'timer' and calculate time
		long endTime = System.nanoTime();
		calculateElapsedTime(startTime, endTime);
		System.out.println();
		
		// Close the statement and finish up
		stmt.close();
		System.out.println("done");
		return data;
	}
	
	public void insert(String table, ArrayList<String> columnList, ArrayList<String> valueList) throws SQLException, IOException
	{
		String insertStatement = "INSERT INTO " + table + " (";
		
		//Adds each column name to the statement
		for(int i = 0; i < columnList.size(); i++)
		{
			if(i == columnList.size()-1)
			{
				insertStatement += (columnList.get(i) + ") ");
			}
			else
			{
				insertStatement += (columnList.get(i) + ",");
			}
		}
		
		insertStatement += "VALUES (";
		
		//Adds a question mark for each value
		for(int i = 0; i < valueList.size(); i++)
		{
			if(i == valueList.size()-1)
			{
				insertStatement += ("?);");
			}
			else
			{
				insertStatement += ("?,");
			}
		}
		
		PreparedStatement stmt = m_dbConn.prepareStatement(insertStatement);
		m_dbConn.setAutoCommit(false);
		
		for(int i = 0; i < valueList.size(); i++)
		{
			// Decide whether whereValue is an int, and prepare statement appropriately
			int whereValueInt;
			boolean whereValueIsInt = isNumeric(valueList.get(i));
			if (whereValueIsInt)
			{
				whereValueInt = Integer.parseInt(valueList.get(i));
				stmt.setInt((i+1), whereValueInt);
			}
			else
			{
				stmt.setString((i+1), valueList.get(i));
			}
		}
		System.out.println("Executing statement:\n\t"+stmt);

		System.out.println("Inserting data...");
		ResultSet results;
		
		// Start the 'timer'
		long startTime = System.nanoTime();

		// Execute the query
		results = stmt.executeQuery();
		m_dbConn.commit();
		
		lastQuerySuccessful = (results!=null) ? true : false;
		
		// End the 'timer' and calculate time
		long endTime = System.nanoTime();
		calculateElapsedTime(startTime, endTime);
		System.out.println();
				
		// Close the statement and finish up
		stmt.close();
		System.out.println("Done");
		
	}

	public ArrayList<String> showTables() throws SQLException
	{
		checkConnection();
		
		ArrayList<String> tables = new ArrayList<String>();

		String selectData = new String("show tables");
		Statement stmt = m_dbConn.createStatement();
		ResultSet rs = stmt.executeQuery(selectData);

		while (rs.next())
		{
			tables.add(rs.getString("Tables_in_csc371-01"));
			System.out.println(rs.getString("Tables_in_csc371-01"));
		}

		return tables;
	}

	/**
	 * Computes the elapsed time between a start and stop time and prints to the
	 * console
	 * @param startTime Time the 'timer' started
	 * @param endTime Time the 'timer' ended
	 * @throws IOException
	 */
	public void calculateElapsedTime(long startTime, long endTime) throws IOException
	{
		long totalTime = endTime - startTime;
		double seconds = (double) totalTime / 1000000000.0;
		int minutes = (int) seconds / 60;
		double leftOver = seconds % 60;
		System.out.println("Total elapsed time: " + minutes + " minutes and " + leftOver + " seconds");
	}

	public boolean isNumeric(String str)
	{
		boolean result = true;
		
		try
		{
			Integer.parseInt(str);
		}
		catch(Exception e)
		{
			result = false;
		}
		
		return result;
	}
	
	/**
	 * This program make insert project and select project
	 * 
	 * @param args
	 * @throws Exception
	 */
	/*
	 * public static void main(String[] args) throws Exception {
	 * 
	 * JDBC object = new JDBC(); //object.testSelectStatements();
	 * 
	 * EventQueue.invokeLater(new Runnable() { public void run() { try { TableGui
	 * frame = new TableGui(); frame.setVisible(true); } catch (Exception e) {
	 * e.printStackTrace(); } } });
	 * 
	 * }
	 */

	public void buildTables() throws SQLException
	{
		String firstHalf = "CREATE TABLE TEST";
		String secondHalf = "(Num1 INT, Num2 INT, str1 CHAR(10), str2 VARCHAR(30), deci DOUBLE, PRIMARY KEY(Num1))";
		for (int i = 0; i < 10; i++)
		{
			Statement statement = m_dbConn.createStatement();
			statement.execute(firstHalf + i + secondHalf);
		}
	}

	public void dropTables() throws SQLException
	{
		String s = "DROP TABLE TEST";
		for (int i = 0; i < 10; i++)
		{
			Statement statement = m_dbConn.createStatement();
			statement.execute(" " + s + i);
		}
	}

	public void populateTables() throws SQLException
	{
		for (int j = 0; j < 10; j++)
		{
			String insertData = "INSERT INTO TEST" + j + " (Num1, Num2, str1, str2, deci)VALUES(?,?,?,?,?)";
			PreparedStatement stmt2 = m_dbConn.prepareStatement(insertData);

			for (int i = 0; i < 5; i++)
			{

				stmt2.setInt(1, i);
				stmt2.setInt(2, i + 1);
				stmt2.setString(3, "this" + i);
				stmt2.setString(4, "that" + i);
				stmt2.setFloat(5, i);
				stmt2.executeUpdate();
			}
		}
	}
}