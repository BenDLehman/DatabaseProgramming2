package assignmentTwo;

import java.util.ArrayList;

public class DBRow
{
	private ArrayList<String> columnLabels;
	private ArrayList<String> types;
	private ArrayList<String> values;
	
	public DBRow ()
	{
		columnLabels = new ArrayList<String>();
		types = new ArrayList<String>();
		values = new ArrayList<String>();
	}
	
	public ArrayList<String> getColumnLabels()
	{
		return columnLabels;
	}
	
	public ArrayList<String> getTypes()
	{
		return types;
	}
	
	public ArrayList<String> getValues()
	{
		return values;
	}
	
	public void addColumLabel(String columnLabel)
	{
		columnLabels.add(columnLabel);
	}
	
	public void addType(String type)
	{
		types.add(type);
	}
	
	public void addValue(String value)
	{
		values.add(value);
	}
}
