package assignmentTwo;

import java.util.ArrayList;

/**
 * Stores all metadata for a table.
 * @author Trevor Kelly
 *
 */
public class TableData
{
	private ArrayList<String> columnLabels;
	private ArrayList<String> columnTypes;
	private ArrayList<String> columnValues;
	private ArrayList<String> nullValues;
	private ArrayList<String> pkValues;
	private ArrayList<String> fkValues;
	private int numColumns;
	
	/**
	 * Create the object and initialize lists
	 */
	public TableData ()
	{
		columnLabels = new ArrayList<String>();
		columnTypes = new ArrayList<String>();
		columnValues = new ArrayList<String>();
		nullValues = new ArrayList<String>();
		pkValues = new ArrayList<String>();
		fkValues = new ArrayList<String>();
	}
	
	/**
	 * Get the column label at a specific index
	 * @param index the index for which to return
	 * @return column label at index
	 */
	public String getColumnLabel(int index)
	{
		return columnLabels.get(index);
	}
	
	/**
	 * Get the column type at a specific index
	 * @param index the index for which to return
	 * @return type at index
	 */
	public String getType(int index)
	{
		return columnTypes.get(index);
	}
	
	/**
	 * Get the column value at a specific index
	 * @param index the index for which to return
	 * @return column value at index
	 */
	public String getValue(int index)
	{
		return columnValues.get(index);
	}
	
	/**
	 * Get the column null value at a specific index
	 * @param index the index for which to return
	 * @return column null value at index
	 */
	public String getNullValue(int index)
	{
		return nullValues.get(index);
	}
	
	/**
	 * Get the column primary key value at a specific index
	 * @param index the index for which to return
	 * @return column primary key value at index
	 */
	public String getPkValue(int index)
	{
		return pkValues.get(index);
	}
	
	/**
	 * Get the column foreign key value at a specific index
	 * @param index the index for which to return
	 * @return column foreign key value at index
	 */
	public String getFkValue(int index)
	{
		return fkValues.get(index);
	}
	
	/**
	 * Get number of columns in table
	 * @return number of columns in table
	 */
	public int getNumColums()
	{
		return numColumns;
	}
	
	/**
	 * Set the number of columns in table
	 * @param num Number of columns
	 */
	public void setNumColumns(int num)
	{
		numColumns = num;
	}
	
	/**
	 * Adds a column label
	 * @param columnLabel Column Label
	 */
	public void addColumLabel(String columnLabel)
	{
		columnLabels.add(columnLabel);
	}
	
	/**
	 * Adds a column type
	 * @param type Column type
	 */
	public void addType(String type)
	{
		columnTypes.add(type);
	}
	
	/**
	 * Adds a column value
	 * @param value Column value
	 */
	public void addValue(String value)
	{
		columnValues.add(value);
	}
	
	/**
	 * Adds a columns null value
	 * @param nullValue null value
	 */
	public void addNullValue(String nullValue)
	{
		nullValues.add(nullValue);
	}
	
	/**
	 * Adds a columns primary key value
	 * @param pkValue Primary Key value
	 */
	public void addPkValue(String pkValue)
	{
		pkValues.add(pkValue);
	}
	
	/**
	 * Adds a columns foreign key value
	 * @param fkValue Foreign key value
	 */
	public void addFkValue(String fkValue)
	{
		fkValues.add(fkValue);
	}
}
