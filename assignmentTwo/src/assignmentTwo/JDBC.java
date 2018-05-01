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
	public String lastQueryWarning;

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
	
	public static void main(String[] args)
	{
		JDBC j = new JDBC();
		j.activateJDBC();
		try
		{
			j.select("*", "TEST_DELETE", null, null);
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
	}
	
	public ArrayList<TableData> select(String what, String from, String whereKey, String whereValue) throws SQLException, IOException
	{
		checkConnection();
		
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
		
		// Keeps returning null currently
		lastQueryWarning = new String();

		// Execute the query
		results = stmt.executeQuery();
		m_dbConn.commit();
		
		lastQuerySuccessful = (results!=null) ? true : false;
		
		if(results.getWarnings()!=null)
		{
			lastQueryWarning += "Warnings\n";
			lastQueryWarning += results.getWarnings().getMessage();
			lastQueryWarning += results.getWarnings().getSQLState();
			lastQueryWarning += results.getWarnings().getErrorCode();
			System.out.println(lastQueryWarning);
		}
		
		ArrayList<TableData> data = parseResultsSet(results, from);

		// End the 'timer' and calculate time
		long endTime = System.nanoTime();
		calculateElapsedTime(startTime, endTime);
		System.out.println();
		
		// Close the statement and finish up
		stmt.close();
		System.out.println("done");
		return data;
	}
	
 	public void insert(String table, ArrayList<String> columnList, ArrayList<String> valueList) throws  IOException
	{
		try {
			checkConnection();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
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
		
		try
		{
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
		int results;
		
		// Start the 'timer'
		long startTime = System.nanoTime();

		// Execute the query
		results = stmt.executeUpdate();
		m_dbConn.commit();
		
		lastQuerySuccessful = true;
		
		// End the 'timer' and calculate time
		long endTime = System.nanoTime();
		calculateElapsedTime(startTime, endTime);
		System.out.println();
				
		// Close the statement and finish up
		stmt.close();
		}
		catch (SQLException e)
		{
			lastQuerySuccessful=false;
			
		}
		System.out.println("Done");
		
		
	}
 	
	public void delete( String from, String whereKey, String whereValue) throws SQLException, IOException 
	{
		checkConnection();
		ArrayList<TableData> data = new ArrayList<TableData>();
		boolean whereClause = (whereKey!=null && whereValue!=null) ? true : false;
		
		// Prepare the statements.
		String sql = "DELETE FROM " + from;
		
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

		System.out.println("deleting data...");
		int results;
		
		// Start the 'timer'
		long startTime = System.nanoTime();

		// Execute the query
		results = stmt.executeUpdate();
		m_dbConn.commit();
		
		lastQuerySuccessful = (results==1) ? true : false;
		
		// End the 'timer' and calculate time
		long endTime = System.nanoTime();
		calculateElapsedTime(startTime, endTime);
		System.out.println();
				
		// Close the statement and finish up
		stmt.close();
		System.out.println("Done");
	}
	
	public void update(String tableName,String setKey, String setValue, String whereKey, String whereValue) 
	{
		try {
			checkConnection();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		String query ="UPDATE " + tableName + " SET " + setKey + " = "  + "?" + " WHERE " + whereKey + " = " + "?";
		try {
		PreparedStatement stmt = m_dbConn.prepareStatement(query);
		m_dbConn.setAutoCommit(false);
	
		int setValueInt;
		boolean setValueIsInt = isNumeric(setValue);
		int whereValueInt;
		boolean whereValueIsInt = isNumeric(whereValue);

		if (setValueIsInt)
		{
			setValueInt = Integer.parseInt(setValue);
			stmt.setInt(1, setValueInt);
		}
		else
		{
			stmt.setString(1, setValue);
		}
		if (whereValueIsInt)
		{
			whereValueInt = Integer.parseInt(whereValue);
			stmt.setInt(2, whereValueInt);
		}
		else
		{
			stmt.setString(2, whereValue);
		}
		
		System.out.println(stmt.toString());
		stmt.executeUpdate();
		
		m_dbConn.commit();
		
		
		lastQueryWarning = "Warnings\n";
		System.out.println(lastQueryWarning);
		if(stmt.getWarnings()!=null)
		{
			
			lastQueryWarning += stmt.getWarnings().getMessage();
			lastQueryWarning += stmt.getWarnings().getSQLState();
			lastQueryWarning += stmt.getWarnings().getErrorCode();
			System.out.println(lastQueryWarning);
		}
		
		lastQuerySuccessful= true;
		System.out.println(setKey + "Has been updated to " + setValue);	
		stmt.close();
		}catch (SQLException e)
		{
			lastQuerySuccessful= false;
		}
		
		
		
	}

	public void customQuery(String query) throws SQLException, IOException
	{
		checkConnection();
		
		PreparedStatement stmt = m_dbConn.prepareStatement(query);
		m_dbConn.setAutoCommit(false);
		long startTime;
		ResultSet selectResults;
		int otherResults = 0;
		
		
		if(query.contains("SELECT"))
		{
			startTime = System.nanoTime();
			selectResults = stmt.executeQuery();
			m_dbConn.commit();
			lastQuerySuccessful = (selectResults!=null) ? true : false;
		}
		else 
		{
			startTime = System.nanoTime();
			otherResults = stmt.executeUpdate();
			m_dbConn.commit();
			lastQuerySuccessful = (otherResults==1) ? true : false;
		}
		
		// End the 'timer' and calculate time
		long endTime = System.nanoTime();
		calculateElapsedTime(startTime, endTime);
		System.out.println();
				
		// Close the statement and finish up
		stmt.close();
		System.out.println("Done");
	}
	
	public ArrayList<TableData> parseResultsSet(ResultSet results, String table) throws SQLException
	{
		System.out.println("Parsing results set");
		ArrayList<TableData> data = new ArrayList<TableData>();
		DatabaseMetaData connMD = m_dbConn.getMetaData();
		ResultSetMetaData metadata = results.getMetaData();
		ResultSet pkColumns = m_dbConn.getMetaData().getPrimaryKeys(null, null, table);
		ResultSet fkColumns = m_dbConn.getMetaData().getExportedKeys(null, null, table);
		
		int count= 0;
		while(results.next())
		{
			data.add(new TableData());
			data.get(count).setNumColumns(metadata.getColumnCount());
			
			for(int x = 1; x < metadata.getColumnCount()+1; x++)
			{
				Object object = results.getObject(x);
				String size = Integer.toString(metadata.getColumnDisplaySize(x));
				String type = object.getClass().getSimpleName()+"("+size+")";
				String value = object.toString();
				String name = metadata.getColumnName(x);
				String nullValue = new String("");
				String pkValue = new String("");
				String fkValue = new String("");
				
				if(metadata.isNullable(x) == connMD.columnNoNulls || metadata.isNullable(x) == connMD.columnNullableUnknown)
				{
					nullValue += "NOT NULL";				
				}
				
				while(pkColumns.next())
				{
					if(pkColumns.getString("COLUMN_NAME").equals(name))
					{
						pkValue += "Primary Key";	
					}
				}
				
				while(fkColumns.next())
				{
					if(fkColumns.getString("FKCOLUMN_NAME").equals(name))
					{
						fkValue += "("+fkColumns.getString("FKTABLE_NAME") + ":" + fkColumns.getString("FKCOLUMN_NAME")+"),";
					}
					System.out.println(fkValue);
				}
				
				data.get(count).addType(type);
				data.get(count).addValue(value);
				data.get(count).addColumLabel(name);
				data.get(count).addPkValue(pkValue);
				data.get(count).addNullValue(nullValue);
				data.get(count).addFkValue(fkValue);
			}
			count++;
		}
		
		return data;
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
	
	/*
	 * Creates an ArrayList of SQL strings, is passed to
	 * buildDataTables();
	 * @return - the arraylist filled with sql create strings
	 */
	public ArrayList<String> buildTableList() throws SQLException
	{
		checkConnection();
		ArrayList<String> tables = new ArrayList<String>();
		tables.add("CREATE TABLE MODERATOR(Login VARCHAR(50) NOT NULL, Email VARCHAR(50) NOT NULL CHECK(LEN(Email) >= 7),PRIMARY KEY (Login));");
		tables.add("CREATE TABLE SPECIAL_MODERATOR_ABILITIES (AbilityID INT NOT NULL CHECK (AbilityID >= 0 AND AbilityID <= 3 ), PRIMARY KEY (AbilityID));");
		tables.add("CREATE TABLE MANAGER(Login VARCHAR(50) NOT NULL, Email VARCHAR(50) NOT NULL CHECK(LEN(Email) >= 7),PRIMARY KEY (Login));");
		tables.add("CREATE TABLE SPECIAL_MANAGER_ABILITIES (AbilityID INT NOT NULL CHECK (AbilityID >= 0 AND AbilityID <= 2),PRIMARY KEY (AbilityID));");
		tables.add("CREATE TABLE MODERATOR_ABILITIES (Login VARCHAR(50) NOT NULL, AbilityID INT NOT NULL CHECK (AbilityID >= 0 AND AbilityID <= 3), PRIMARY KEY(Login, AbilityID), FOREIGN KEY(Login) REFERENCES MODERATOR(Login), FOREIGN KEY(AbilityID) REFERENCES SPECIAL_MODERATOR_ABILITIES(AbilityID) ON DELETE CASCADE ON UPDATE CASCADE);");
		tables.add("CREATE TABLE MANAGER_ABILITIES (Login VARCHAR(50) NOT NULL, AbilityID INT NOT NULL CHECK(AbilityID >= 0 AND AbilityID <= 2 ),PRIMARY KEY(Login, AbilityID),FOREIGN KEY(Login) REFERENCES MANAGER (Login),FOREIGN KEY(AbilityID) REFERENCES SPECIAL_MANAGER_ABILITIES(AbilityID) ON DELETE CASCADE ON UPDATE CASCADE);");
		tables.add("CREATE TABLE PLAYER(Login VARCHAR(50) NOT NULL, PlayerID INT NOT NULL UNIQUE CHECK( 0 <=  PlayerID <= 50000000), Password VARCHAR(25) NOT NULL, Email VARCHAR(50) NOT NULL CHECK(LEN(Email) >= 7),PRIMARY KEY(Login, PlayerID));");
		tables.add("CREATE TABLE PLAYS_AS(PlayerID INT NOT NULL CHECK( 0<=  PlayerID <= 50000000),CharacterName VARCHAR(25) NOT NULL UNIQUE CHECK(LEN(CharacterName)>= 8), Email VARCHAR(50) NOT NULL CHECK(LEN(Email) >=7),PRIMARY KEY(PlayerID),FOREIGN KEY(PlayerID) REFERENCES PLAYER (PlayerID) ON DELETE CASCADE ON UPDATE CASCADE);");
		tables.add("CREATE TABLE PLAYABLE_CHARACTERS(PlayerID INT NOT NULL CHECK( 0<=  PlayerID <= 50000000),CharacterName VARCHAR(25) NOT NULL UNIQUE CHECK(LEN(CharacterName) >= 8),PRIMARY KEY(PlayerID),FOREIGN KEY( PlayerID) REFERENCES PLAYER(PlayerID) ON DELETE CASCADE ON UPDATE CASCADE);");
		tables.add("CREATE TABLE HEROS(Name VARCHAR(50) NOT NULL CHECK(LEN(Name) >= 7),Stamina INT NOT NULL CHECK(0 < Stamina <= 100), MaxStrength INT NOT NULL CHECK(0 < MaxStrength <= 100),Strength INT NOT NULL CHECK(0 < Strength <= 100), MaxHP INT NOT NULL CHECK(0 < MaxHP <= 100), PRIMARY KEY (Name),FOREIGN KEY (Name) REFERENCES PLAYS_AS (CharacterName),FOREIGN KEY (Name) REFERENCES PLAYABLE_CHARACTERS(CharacterName) ON DELETE CASCADE ON UPDATE CASCADE);");
		tables.add("CREATE TABLE LOCATION(LocationID INT NOT NULL CHECK (100000001 <= LocationID <=150000000),Size INT NOT NULL CHECK (10 <= Size <= 1000),Type INT NOT NULL CHECK ( 0 <= Type <=2), PRIMARY KEY (LocationID));");
		tables.add("CREATE TABLE CREATURE (CreatureID INT NOT NULL CHECK(50000001 <= CreatureID <= 100000000),DamageProtection INT NOT NULL CHECK(1 <= DamageProtection <= 100),CurrentHP INT NOT NULL CHECK(1 <= CurrentHP <= 100), MaxHP INT NOT NULL CHECK(1 <= MaxHP <= 100), MaxStrength INT NOT NULL CHECK(1 <= MaxStrength <= 100),Strength INT NOT NULL CHECK(1 <= Strength <= 100),Stamina INT NOT NULL CHECK(1 <= Stamina <= 100),PRIMARY KEY (CreatureID));");
		tables.add("CREATE TABLE LIKES( CreatureID INT NOT NULL CHECK(50000001<= CreatureID <=100000000), PlayerID INT NOT NULL CHECK( 0<=  PlayerID <= 50000000), PRIMARY KEY(CreatureID, PlayerID), FOREIGN KEY (CreatureID) References CREATURE(CreatureID), FOREIGN KEY (PlayerID) REFERENCES PLAYER(PlayerID) ON DELETE CASCADE ON UPDATE CASCADE);");
		tables.add("CREATE TABLE HATES(CreatureID INT NOT NULL CHECK(50000001<= CreatureID <=100000000),PlayerID INT NOT NULL CHECK( 0<=  PlayerID <= 50000000),PRIMARY KEY(CreatureID, PlayerID),FOREIGN KEY (CreatureID) References CREATURE(CreatureID),FOREIGN KEY (PlayerID) REFERENCES PLAYER(PlayerID) ON DELETE CASCADE ON UPDATE CASCADE);");
		tables.add("CREATE TABLE NEUTRAL_TO (CreatureID INT NOT NULL CHECK(50000001<= CreatureID <= 100000000),PlayerID INT NOT NULL CHECK( 0<=  PlayerID <= 50000000),PRIMARY KEY(CreatureID, PlayerID), FOREIGN KEY (CreatureID) REFERENCES CREATURE(CreatureID),FOREIGN KEY (PlayerID) REFERENCES PLAYER(PlayerID) ON DELETE CASCADE ON UPDATE CASCADE);");
		tables.add("CREATE TABLE LIKED_BY (CharacterName VARCHAR(50) NOT NULL CHECK(LEN(Name) >= 7),CreatureID INT NOT NULL CHECK(50000001<= CreatureID <= 100000000),PRIMARY KEY(CharacterName, CreatureID),FOREIGN KEY(CharacterName) REFERENCES HEROS(Name),FOREIGN KEY(CreatureID) REFERENCES CREATURE(CreatureID) ON DELETE CASCADE ON UPDATE CASCADE);");
		tables.add("CREATE TABLE HATED_BY (CharacterName VARCHAR(50) NOT NULL CHECK(LEN(Name) >= 7),CreatureID INT NOT NULL CHECK(50000001<= CreatureID <= 100000000),PRIMARY KEY(CharacterName, CreatureID),FOREIGN KEY(CharacterName) REFERENCES HEROS(Name),FOREIGN KEY(CreatureID) REFERENCES CREATURE(CreatureID) ON DELETE CASCADE ON UPDATE CASCADE);");
		tables.add("CREATE TABLE ACCESSIBLE_AREAS (CreatureID INT NOT NULL CHECK(50000001<= CreatureID <= 100000000),LocationID INT NOT NULL CHECK (100000001 <= LocationID <= 150000000),PRIMARY KEY(CreatureID, LocationID),FOREIGN KEY( CreatureID) REFERENCES CREATURE(CreatureID),FOREIGN KEY( LocationID) REFERENCES LOCATION(LocationID) ON DELETE CASCADE ON UPDATE CASCADE);");
		tables.add("CREATE TABLE ABILITY(AbilityID INT NOT NULL CHECK(200000001 <= AbilityID <= 250000000),TargetedStat INT NOT NULL CHECK (0 <= TargetedStat <= 3),AffectAmmount INT NOT NULL CHECK (1 <= AffectAmmount <= 100),ExecutionTime INT NOT NULL CHECK ( 1 <= ExecutionTime <= 5), PRIMARY KEY(AbilityID));");
		tables.add("CREATE TABLE HAS_ABILITY(CreatureID INT NOT NULL CHECK(50000001<= CreatureID <= 100000000),AbilityID INT NOT NULL CHECK(200000001 <= AbilityID <= 250000000), PRIMARY KEY(CreatureID, AbilityID),FOREIGN KEY(CreatureID) REFERENCES CREATURE(CreatureID),FOREIGN KEY(AbilityID) REFERENCES ABILITY(AbilityID) ON DELETE CASCADE ON UPDATE CASCADE);");
		tables.add("CREATE TABLE GENERIC_ITEM (ItemID INT NOT NULL CHECK(150000001 <= ItemID <= 200000000),Weight INT NOT NULL CHECK (1 <= Weight <= 25),Volume INT NOT NULL CHECK (1 <= Volume <= 25),Description VARCHAR(100) NOT NULL CHECK(LEN(Description) >= 5),3DModel VARCHAR(30) NOT NULL CHECK(LEN(3DModel) >= 5),PRIMARY KEY(ItemID));");
		tables.add("CREATE TABLE CONTAINER (ItemID INT NOT NULL CHECK(150000001 <= ItemID <= 200000000),WeightLimit INT NOT NULL CHECK (1 <= WeightLimit <= 200),VolumeLimit INT NOT NULL CHECK (1 <= VolumeLimit <= 200),PRIMARY KEY(ItemID),FOREIGN KEY(ItemID) REFERENCES GENERIC_ITEM(ItemID) ON DELETE CASCADE ON UPDATE CASCADE);");
		tables.add("CREATE TABLE WEAPON(ItemID INT NOT NULL CHECK(150000001 <= ItemID <= 200000000),Ability INT NOT NULL CHECK(200000001 <= Ability <= 200000000),StoringLocation INT NOT NULL CHECK (0 <= StoringLocation <=2),PRIMARY KEY(ItemID, Ability),FOREIGN KEY(ItemID) REFERENCES GENERIC_ITEM(ItemID),FOREIGN KEY(Ability)REFERENCES ABILITY(AbilityID) ON DELETE CASCADE ON UPDATE CASCADE);");
		tables.add("CREATE TABLE ARMOR (ItemID INT NOT NULL CHECK(150000001 <= ItemID <= 200000000),WearLocation INT NOT NULL CHECK(0 <= WearLocation <=3),DamageProtection INT NOT NULL CHECK (1 <= DamageProtection <= 99),PRIMARY KEY (ItemID),FOREIGN KEY(ItemID) REFERENCES GENERIC_ITEM(ItemID) ON DELETE CASCADE ON UPDATE CASCADE);");
		tables.add("CREATE TABLE CONTAINS(LocationID INT NOT NULL CHECK (100000001 <= LocationID <=150000000),PlayerID INT NOT NULL CHECK( 0<=  PlayerID <= 50000000),ItemID INT NOT NULL CHECK(150000001 <= ItemID <= 200000000),CreatureID INT NOT NULL CHECK(50000001<= CreatureID <= 100000000),PRIMARY KEY(LocationID, PlayerID, ItemID, CreatureID),FOREIGN KEY (LocationID) REFERENCES LOCATION(LocationID),FOREIGN KEY (PlayerID) REFERENCES PLAYER(PlayerID),FOREIGN KEY (ItemID) REFERENCES GENERIC_ITEM (ItemID),FOREIGN KEY (CreatureID) REFERENCES CREATURE(CreatureID));");
		return tables;
	}
	
	/*
	 * Builds tables in the database
	 */
	public void buildDataTables(ArrayList<String> tables) throws SQLException
	{
		for(int i = 0; i < tables.size(); i++)
		{
			PreparedStatement statement2 = m_dbConn.prepareStatement(tables.get(i));
			System.out.println(statement2.toString());
			m_dbConn.setAutoCommit(false);
			statement2.execute();
			m_dbConn.commit();
		}
	}
	
	/*
	 * Drops tables in the database. Used for testing purposes
	 */
	public void dropDataTables() throws SQLException
	{
		checkConnection();
		ArrayList<String> tableNames = new ArrayList<String>();
		tableNames.add("MODERATOR");
		tableNames.add("SPECIAL_MODERATOR_ABILITIES");
		tableNames.add("MANAGER");
		tableNames.add("SPECIAL_MANAGER_ABILITIES");
		tableNames.add("MODERATOR_ABILITIES");
		tableNames.add("MANAGER_ABILITIES");
		tableNames.add("PLAYER");
		tableNames.add("PLAYS_AS");
		tableNames.add("PLAYABLE_CHARACTERS");
		tableNames.add("HEROS");
		tableNames.add("LOCATION");
		tableNames.add("CREATURE");
		tableNames.add("LIKES");
		tableNames.add("HATES");
		tableNames.add("NEUTRAL_TO");
		tableNames.add("LIKED_BY");
		tableNames.add("HATED_BY");
		tableNames.add("ACCESSIBLE_AREAS");
		tableNames.add("ABILITY");
		tableNames.add("HAS_ABILITY");
		tableNames.add("GENERIC_ITEM");
		tableNames.add("CONTAINER");
		tableNames.add("WEAPON");
		tableNames.add("ARMOR");
		tableNames.add("CONTAINS");
		
		String drop = "DROP TABLE";
		for (int i = tableNames.size()-1; i > -1; i--)
		{
			try {
				Statement statement = m_dbConn.createStatement();
				statement.execute(drop + " " + tableNames.get(i));
				}
			catch(Exception e)
			{
				
			}
		}
	}
	/*
	 * Populates tables in the database
	 */
	public void insertData() throws SQLException
	{
		checkConnection();
		ArrayList<String> insertStatements = new ArrayList<String>();
		insertStatements.add("INSERT INTO MODERATOR VALUES (\"SwagDaddy69\", \"SwagDaddy69@gmail.com\");");
		insertStatements.add("INSERT INTO MODERATOR VALUES (\"BigPapiMcgee\", \"BigPapiMcgee@gmail.com\");");
		insertStatements.add("INSERT INTO MODERATOR VALUES (\"TheZucc\", \"Robot@gmail.com\");");
		insertStatements.add("INSERT INTO SPECIAL_MODERATOR_ABILITIES VALUES(0);");
		insertStatements.add("INSERT INTO SPECIAL_MODERATOR_ABILITIES VALUES(1);");
		insertStatements.add("INSERT INTO SPECIAL_MODERATOR_ABILITIES VALUES(2);");
		insertStatements.add("INSERT INTO MANAGER VALUES(\"Mike Oxlong\", \"mikeoxlong@gmail.com\");");
		insertStatements.add("INSERT INTO MANAGER VALUES(\"Phil McCrackin\", \"philmccrackin@gmail.com\");");
		insertStatements.add("INSERT INTO MANAGER VALUES(\"Nick Cage\", \"notthebees@gmail.com\");");
		insertStatements.add("INSERT INTO SPECIAL_MANAGER_ABILITIES VALUES(0);");
		insertStatements.add("INSERT INTO SPECIAL_MANAGER_ABILITIES VALUES(1);");
		insertStatements.add("INSERT INTO SPECIAL_MANAGER_ABILITIES VALUES(2);");
		insertStatements.add("INSERT INTO MODERATOR_ABILITIES VALUES(\"SwagDaddy69\", 0 );");
		insertStatements.add("INSERT INTO MODERATOR_ABILITIES VALUES(\"BigPapiMcgee\", 1 );");
		insertStatements.add("INSERT INTO MODERATOR_ABILITIES VALUES(\"TheZucc\", 2 );");
		insertStatements.add("INSERT INTO MANAGER_ABILITIES VALUES(\"Mike Oxlong\",0 );");
		insertStatements.add("INSERT INTO MANAGER_ABILITIES VALUES(\"Phil McCrackin\",1 );");
		insertStatements.add("INSERT INTO MANAGER_ABILITIES VALUES(\"Nick Cage\", 2 );");
		insertStatements.add("INSERT INTO PLAYER VALUES(\"BadAss\", 1, \"secretpassword\", \"Badassmfr@gmail.com\");");
		insertStatements.add("INSERT INTO PLAYER VALUES(\"CreepyOldMan\", 2, \"imcreepy\", \"serialkiller@gmail.com\");");
		insertStatements.add("INSERT INTO PLAYER VALUES(\"HungryTeen\", 3, \"gimmedatfood\", \"cerealkiller@gmail.com\");");
		insertStatements.add("INSERT INTO PLAYS_AS VALUES(1, \"BAMF\", \"crunchatizeme@gmail.com\");");
		insertStatements.add("INSERT INTO PLAYS_AS VALUES(2, \"FreeCandyMan\", \"FreeCandy@gmail.com\");");
		insertStatements.add("INSERT INTO PLAYS_AS VALUES(3, \"IEatFreeCandy\", \"FreeCandyPlz@gmail.com\");");
		insertStatements.add("INSERT INTO PLAYABLE_CHARACTERS VALUES(1, \"BAMF\");");
		insertStatements.add("INSERT INTO PLAYABLE_CHARACTERS VALUES(2, \"FreeCandyMan\");");
		insertStatements.add("INSERT INTO PLAYABLE_CHARACTERS VALUES(3, \"IEatFreeCandy\");");
		insertStatements.add("INSERT INTO HEROS VALUES(\"BAMF\", 100, 100, 100, 100);");
		insertStatements.add("INSERT INTO HEROS VALUES(\"FreeCandyMan\", 100, 100, 100, 100);");
		insertStatements.add("INSERT INTO HEROS VALUES(\"IEatFreeCandy\", 100, 100, 100, 100);");
		insertStatements.add("INSERT INTO LOCATION VALUES(100000005, 100, 0);");
		insertStatements.add("INSERT INTO LOCATION VALUES(100000006, 100, 1);");
		insertStatements.add("INSERT INTO LOCATION VALUES(100000007, 100, 2);");
		insertStatements.add("INSERT INTO CREATURE VALUES(50000005, 50, 100, 100, 100, 100, 100);");
		insertStatements.add("INSERT INTO CREATURE VALUES(50000006, 60, 100, 100, 100, 100, 100);");
		insertStatements.add("INSERT INTO CREATURE VALUES(50000007, 70, 100, 100, 100, 100, 100);");
		insertStatements.add("INSERT INTO LIKES VALUES(50000005, 1);");
		insertStatements.add("INSERT INTO LIKES VALUES(50000006, 1);");
		insertStatements.add("INSERT INTO LIKES VALUES(50000007, 1);");
		insertStatements.add("INSERT INTO HATES VALUES(50000005, 2);");
		insertStatements.add("INSERT INTO HATES VALUES(50000006, 2);");
		insertStatements.add("INSERT INTO HATES VALUES(50000007, 2);");
		insertStatements.add("INSERT INTO NEUTRAL_TO VALUES(50000005, 3);");
		insertStatements.add("INSERT INTO NEUTRAL_TO VALUES(50000006, 3);");
		insertStatements.add("INSERT INTO NEUTRAL_TO VALUES(50000007, 3);");
		insertStatements.add("INSERT INTO LIKED_BY VALUES(\"BAMF\", 50000005);");
		insertStatements.add("INSERT INTO LIKED_BY VALUES(\"BAMF\", 50000006);");
		insertStatements.add("INSERT INTO LIKED_BY VALUES(\"BAMF\", 50000007);");
		insertStatements.add("INSERT INTO HATED_BY VALUES(\"FreeCandyMan\", 50000005);");
		insertStatements.add("INSERT INTO HATED_BY VALUES(\"FreeCandyMan\", 50000006);");
		insertStatements.add("INSERT INTO HATED_BY VALUES(\"FreeCandyMan\", 50000007);");
		insertStatements.add("INSERT INTO ACCESSIBLE_AREAS VALUES(50000005, 100000005);");
		insertStatements.add("INSERT INTO ACCESSIBLE_AREAS VALUES(50000006, 100000006);");
		insertStatements.add("INSERT INTO ACCESSIBLE_AREAS VALUES(50000007, 100000007);");
		insertStatements.add("INSERT INTO ABILITY VALUES(250000000, 0, 50, 1);");
		insertStatements.add("INSERT INTO ABILITY VALUES(250000001, 1, 50, 2);");
		insertStatements.add("INSERT INTO ABILITY VALUES(250000002, 2, 50, 3);");
		insertStatements.add("INSERT INTO HAS_ABILITY VALUES(50000005, 250000000);");
		insertStatements.add("INSERT INTO HAS_ABILITY VALUES(50000006, 250000001);");
		insertStatements.add("INSERT INTO HAS_ABILITY VALUES(50000007, 250000002);");
		insertStatements.add("INSERT INTO GENERIC_ITEM VALUES(150000001, 20, 20, \"generic1\", \"jawn.dxf\");");
		insertStatements.add("INSERT INTO GENERIC_ITEM VALUES(150000002, 20, 20, \"generic2\", \"poo.dxf\");");
		insertStatements.add("INSERT INTO GENERIC_ITEM VALUES(150000003, 20, 20, \"generic3\", \"shoe.dxf\");");
		insertStatements.add("INSERT INTO GENERIC_ITEM VALUES(150000004, 20, 20, \"generic4\", \"sock.dxf\");");
		insertStatements.add("INSERT INTO GENERIC_ITEM VALUES(150000005, 20, 20, \"generic5\", \"lemon.dxf\");");
		insertStatements.add("INSERT INTO GENERIC_ITEM VALUES(150000006, 20, 20, \"generic6\", \"beer.dxf\");");
		insertStatements.add("INSERT INTO GENERIC_ITEM VALUES(150000007, 20, 20, \"generic7\", \"dillydilly.dxf\");");
		insertStatements.add("INSERT INTO GENERIC_ITEM VALUES(150000008, 20, 20, \"generic8\", \"coffee.dxf\");");
		insertStatements.add("INSERT INTO GENERIC_ITEM VALUES(150000009, 20, 20, \"generic9\", \"balls.dxf\");");
		insertStatements.add("INSERT INTO CONTAINER VALUES(150000001, 150,150);");
		insertStatements.add("INSERT INTO CONTAINER VALUES(150000002, 150,150);");
		insertStatements.add("INSERT INTO CONTAINER VALUES(150000003, 150,150);");
		insertStatements.add("INSERT INTO WEAPON VALUES(150000004, 250000000, 1);");
		insertStatements.add("INSERT INTO WEAPON VALUES(150000005, 250000001, 2);");
		insertStatements.add("INSERT INTO WEAPON VALUES(150000006, 250000002, 3);");
		insertStatements.add("INSERT INTO ARMOR VALUES(150000007, 1, 50);");
		insertStatements.add("INSERT INTO ARMOR VALUES(150000008, 2, 50);");
		insertStatements.add("INSERT INTO ARMOR VALUES(150000009, 3, 50);");
		insertStatements.add("INSERT INTO CONTAINS VALUES(100000005, 1, 150000004,50000005);");
		insertStatements.add("INSERT INTO CONTAINS VALUES(100000006, 2, 150000005,50000006);");
		insertStatements.add("INSERT INTO CONTAINS VALUES(100000007, 3, 150000006,50000007);");

		for (int i =0; i < insertStatements.size(); i++)
		{
			PreparedStatement statement2 = m_dbConn.prepareStatement(insertStatements.get(i));
			System.out.println(statement2.toString());
			m_dbConn.setAutoCommit(false);
			statement2.execute();
			m_dbConn.commit();
		}
	}


}