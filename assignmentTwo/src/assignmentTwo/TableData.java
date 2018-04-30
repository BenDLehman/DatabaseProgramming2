package assignmentTwo;

import java.sql.ResultSetMetaData;
import java.util.ArrayList;

public class TableData
{
	private ArrayList<String> columnLabels;
	private ArrayList<String> columnTypes;
	private ArrayList<String> columnValues;
	private ArrayList<String> nullValues;
	private ArrayList<String> pkValues;
	//private ArrayList<Object> test = new ArrayList<Object>();
	private ResultSetMetaData metadata;
	private int numColumns;
	
	public TableData ()
	{
		columnLabels = new ArrayList<String>();
		columnTypes = new ArrayList<String>();
		columnValues = new ArrayList<String>();
		nullValues = new ArrayList<String>();
		pkValues = new ArrayList<String>();
	}
	
	/*public void setMetaData(ResultSetMetaData metadata)
	{
		this.metadata = metadata;
		
		for(int x = 0; x < numColumns )
	}*/
	
	public ResultSetMetaData getMetadata()
	{
		return metadata;
	}
	
	public String getColumnLabel(int index)
	{
		return columnLabels.get(index);
	}
	
	public String getType(int index)
	{
		return columnTypes.get(index);
	}
	
	public String getValue(int index)
	{
		return columnValues.get(index);
	}
	
	public String getNullValue(int index)
	{
		return nullValues.get(index);
	}
	
	public String getPkValue(int index)
	{
		return pkValues.get(index);
	}
	
	public int getNumColums()
	{
		return numColumns;
	}
	
	public void setNumColumns(int num)
	{
		numColumns = num;
	}
	
	public void addColumLabel(String columnLabel)
	{
		columnLabels.add(columnLabel);
	}
	
	public void addType(String type)
	{
		columnTypes.add(type);
	}
	
	public void addValue(String value)
	{
		columnValues.add(value);
	}
	
	public void addNullValue(String nullValue)
	{
		nullValues.add(nullValue);
	}
	
	public void addPkValue(String pkValue)
	{
		pkValues.add(pkValue);
	}
}
