package assignmentTwo;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Random;
import java.util.Scanner;

public class DBEditor
{
	public static final String DB_LOCATION = "jdbc:mysql://localhost:3306/csc371";
	public static final String LOGIN_NAME = "csc371-07";
	public static final String PASSWORD = "Password07";
	public static final int ROWS = 500000;
	protected Connection m_dbConn = null;
	public BufferedWriter out;
	private String[] charStrings = new String[ROWS];
	private String[] varcharStrings = new String[ROWS];

	/**
	 * Creates an instance of DBEditor, and creates 500,000 random strings
	 * to be inserted later.
	 * @throws IOException
	 */
	public DBEditor() throws IOException
	{
		try
		{
			out = new BufferedWriter(new FileWriter("C:/Users/Trevor/Documents/results.txt"));
		} catch (IOException e)
		{
			e.printStackTrace();
		}
		
		for(int x = 0; x<ROWS; x++)
		{
			charStrings[x] = createRandomString(10);
			varcharStrings[x] = createRandomString(11);
			System.out.println(x);
		}
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
	 * Check to see if the connection is null or closed and if so,
	 * create the connection.
	 * @throws SQLException
	 */
	public void checkConnection() throws SQLException
	{
		if(m_dbConn==null || m_dbConn.isClosed())
		{
			createConnection();	
		}
	}

	/**
	 * Register the the mysql jdbc driver
	 * @return true if registration is successful 
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
	 * Prepares and executes an insert statement in order to put data into the table specified.
	 * @throws Exception
	 */
	public void insertStatement(boolean deleteData) throws Exception
	{
		checkConnection();
		
		// Prepare the statement
		String insertData = new String("INSERT INTO TEST_KELLY (col1,col2,col3,col4,col5) VALUES (?,?,?,?,?)");		
		PreparedStatement stmt = m_dbConn.prepareStatement(insertData);
		m_dbConn.setAutoCommit(false);
		
		// Start the 'timer'
		System.out.print("Inserting data...");
		out.write("Inserting data...");
		long startTime = System.nanoTime();
		
		// Insert random data for the amount of rows we specified earlier
		for (int x = 1; x <= ROWS; x++)
		{
			stmt.setInt(1, x);
			stmt.setInt(2, x);
			stmt.setString(3, charStrings[x-1]);
			stmt.setString(4, varcharStrings[x-1]);
			stmt.setDouble(5, (double) x);
			stmt.addBatch();
		}
		
		stmt.executeBatch();
		m_dbConn.commit();
		stmt.close();
		
		// Stop the 'timer' and calculate elapsed time		
		long endTime = System.nanoTime();
		System.out.println("done");
		out.write("done\n");
		
		calculateElapsedTime(startTime, endTime);
		
		// Clear the data from the table if specified
		if(deleteData)
			deleteStatement();
	}
	
	/**
	 * Removes all data from the table with a simple TRUNCATE statement
	 * @throws Exception
	 */
	public void deleteStatement() throws Exception
	{
		checkConnection();
		
		String deleteData = new String("TRUNCATE TABLE TEST_KELLY");
		PreparedStatement stmt = m_dbConn.prepareStatement(deleteData);
		stmt.execute();
		stmt.close();
		m_dbConn.close();
		
        System.out.println("Data Deleted");
        out.write("Data Deleted\n");
	}
	
	/**
	 * Selects 100 rows of data from the data base. Once using the primary key column
	 * and once using the non primary key column
	 * @throws IOException
	 * @throws SQLException
	 */
	public void selectStatement() throws IOException, SQLException
	{
		checkConnection();
		
		// Prepare the statements. Cannot not change 'col1' out with 'col2' in a
		// prepared statement because you cannot insert them without the quotes,
		// so we use two different prepared statements 
		String selectData1 = new String("SELECT * FROM TEST_KELLY WHERE col1 BETWEEN ? AND ?");
		String selectData2 = new String("SELECT * FROM TEST_KELLY WHERE col2 BETWEEN ? AND ?");
		PreparedStatement stmt1 = m_dbConn.prepareStatement(selectData1);
		PreparedStatement stmt2 = m_dbConn.prepareStatement(selectData2);
		PreparedStatement stmt = null;
		m_dbConn.setAutoCommit(false);
		
		System.out.println("Selecting data...");
		out.write("Selecting data...\n");	
		ResultSet results;
		System.out.println("Results:");
		
		// Run the selects on col1 and then col2, 100 rows at a time
		// (0-99,100-199,200-299 etc...)
		for (int y = 0; y < 2; y++)
		{
			// Choose which prepared statement to use
			if(y==0)
			{
				stmt = stmt1;
				System.out.println("Query using col1 (primary key)");
				out.write("Query using col1 (primary key)\n");
			}
			else 
			{
				stmt = stmt2;
				System.out.println("Query using col2 (not primary key)");
				out.write("Query using col2 (not primary key)\n");
			}
			
			int start = 0;
			int end = 0;
			for (int x = 0; x < 10; x++)
			{
				// start the 'timer'
				long startTime = System.nanoTime();

				start = x * 100;
				end = ((x + 1) * 100) - 1;
				
				stmt.setInt(1, start);
				stmt.setInt(2, end);
				
				out.write(stmt +"\n");

				results = stmt.executeQuery();
				m_dbConn.commit();

				while (results.next())
				{
					System.out.print(results.getInt("col1") + " ");
					System.out.print(results.getInt("col2") + " ");
					System.out.print(results.getString("col3") + " ");
					System.out.print(results.getString("col4") + " ");
					System.out.println(results.getDouble("col5"));

					out.write(results.getInt("col1") + " ");
					out.write(results.getInt("col2") + " ");
					out.write(results.getString("col3") + " ");
					out.write(results.getString("col4") + " ");
					out.write(results.getDouble("col5") + "\n");
				}

				long endTime = System.nanoTime();
				System.out.println();
				out.write("\n");
				calculateElapsedTime(startTime, endTime);
				System.out.println();
				out.write("\n");
			}
		}
		stmt.close();
		
		System.out.println("done");
		out.write("done\n");
	}

	/**
	 * Computes the elapsed time between a start and stop time and prints to the console
	 * @param startTime Time the 'timer' started
	 * @param endTime Time the 'timer' ended
	 * @throws IOException
	 */
	public void calculateElapsedTime(long startTime, long endTime) throws IOException
	{
		long totalTime = endTime-startTime;
		double seconds = (double)totalTime / 1000000000.0;
		int minutes = (int)seconds / 60;
		double leftOver = seconds % 60;
		System.out.println("Total elapsed time: "+minutes+" minutes and "+leftOver+" seconds");
		out.write("Total elapsed time: "+minutes+" minutes and "+leftOver+" seconds\n");
	}
	
	/**
	 * Creates a string filled with random characters from the alphabet. For the purpose of
	 * this program, there are two types of strings we want to create. 10 characters exactly,
	 * and somewhere between 1 and 30 characters.
	 * @param length The length of string to return. If this is larger than 10, then we create
	 * a string of random characters with length somewhere between 1 and 30
	 * @return The randomly generated string
	 */
	public String createRandomString(int length)
	{
		String result = new String();
		
		// If length is greater then 10, then change length to be a random number between 1 and 30
		if(length>10)
		{
			Random rand = new Random();
			length = rand.nextInt(29)+1;
		}
		
		// Fill the string
		for(int x = 0; x < length; x++)
		{
			result+=randomChar();
		}
		
		return result;
	}
	
	/**
	 * Choose one random character from the alphabet, either lowercase or uppercase
	 * @return The random character
	 */
	public char randomChar()
	{
		String alphabet = new String("abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ");
		Random rand = new Random();
		
		return alphabet.charAt(rand.nextInt(52));
	}

	/**
	 * Main runner method
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception
	{
		DBEditor db = new DBEditor();
		db.activateJDBC();
		db.deleteStatement(); // Make sure the db is empty
		for(int x = 0; x<10; x++)
		{
			System.out.println("Executing "+(x+1)+" out of 10 executions");
			db.out.write("Executing "+(x+1)+" out of 10 executions\n");
			db.insertStatement(true);
		}
		System.out.println("All executions complete");
		db.out.write("All executions complete\n");
		
		System.out.println("Executing single insert of "+ROWS+" rows");
		db.out.write("Executing single insert of "+ROWS+" rows\n");
		db.insertStatement(false);
		
		// Stopping so that we can run the 5 manual select pairs before the 
		// 10 pairs of 100 with a loop
		Scanner scan = new Scanner(System.in);
		System.out.println("Press enter to continue");
		String answer = scan.nextLine();
		
		db.selectStatement();
		
		db.out.close();
	}

}
