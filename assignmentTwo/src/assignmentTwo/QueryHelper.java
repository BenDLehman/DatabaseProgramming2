	package assignmentTwo;

	import java.util.ArrayList;
/**
 * Class whose objects are used for storing column names and the data
 * within that column for easy retrieval within update() and delete
 * jdbc methods
 */
	
	
	public class QueryHelper 
	{
	    String columns; //column that data resides in.
	    String data; // the data within the column.
	    public QueryHelper(String columns, String data )
	    {
	        this.columns = columns;
	        this.data = data;
	    }
	    

	}