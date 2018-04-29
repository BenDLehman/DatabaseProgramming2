package assignmentTwo;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.awt.event.ActionEvent;

/**
 * Allows the user to insert new data into the table/relation that was selected
 * in the TableGui. 
 * @author Trevor Kelly
 *
 */
public class InsertGui extends State implements ActionListener
{
	private ArrayList<JTextField> fields;
	private ArrayList<JLabel> labels;
	private JButton btnInsert;
	private Gui gui;
	private JDBC jdbc;
	private ArrayList<DBRow> data;
	private String tableName;
	private int numColumns;
	private JPanel pnlResults;
	private JLabel lblResults;

	/**
	 * Create the application.
	 */
	public InsertGui(Gui gui)
	{
		this.gui = gui;
		jdbc = new JDBC();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	public void initialize()
	{		
		tableName = gui.getActiveTable();
		this.setTitle("Inserting into " + tableName);
		labels = new ArrayList<JLabel>();
		fields = new ArrayList<JTextField>();

		// Get the columns from the table that was selected
		try
		{
			data = jdbc.select("*", tableName, null, null);
			numColumns = data.get(0).getNumColums();
		}
		catch (SQLException | IOException e1)
		{
			e1.printStackTrace();
		}
		
		getContentPane().setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.weightx = 0;
		c.gridx = 0;
		c.insets = new Insets(10,10,10,10);	

		// Add the columns and text fields to the gui
		try
		{
			c.gridy=0;
			getContentPane().add(createFields(), c);
		}
		catch (SQLException | IOException e)
		{
			e.printStackTrace();
		}
		
		// Add the insert button and the results panel
		btnInsert = new JButton("INSERT");
		btnInsert.addActionListener(this);
		c.gridy = 1;
		getContentPane().add(btnInsert, c);
		
		pnlResults = new JPanel();
		pnlResults.setLayout(new GridLayout(2,0,0,4));
		c.gridy = 2;
		getContentPane().add(pnlResults, c);
		
		JLabel results = new JLabel("Results:"); // label for results
		pnlResults.add(results);
		
		lblResults = new JLabel(); // where user will be updated with success or fail
		lblResults.setBorder(new EmptyBorder(10,10,10,10));
		lblResults.setOpaque(true);
		lblResults.setBackground(Color.GRAY);
		
		pnlResults.add(lblResults);
	}

	/**
	 * Dynamically creates the fields and places them in the gui based off of how
	 * many columns/attributes are in the table/relation.
	 * @throws IOException
	 * @throws SQLException
	 */
	public JPanel createFields() throws SQLException, IOException
	{
		JPanel content = new JPanel(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;
		c.weightx = 0.5;
		c.insets = new Insets(1,10,1,10);

		// Add the column labels
		for (int j = 0; j < numColumns; j++)
		{
			JLabel l = new JLabel(data.get(0).getColumnLabel(j),SwingConstants.CENTER);
			c.gridx = j;
			c.gridy = 0;
			l.setPreferredSize(new Dimension(100,20));
			labels.add(l);
			content.add(l,c);
		}
		
		c.weightx = 0.0;
		
		// Add the text fields
		for (int i = 0; i < numColumns; i ++)
		{
			JTextField j = new JTextField();
			c.gridx=i;
			c.gridy=1;
			fields.add(j);
			content.add(j,c);
		}
		
		return content;
	}
	
	/**
	 * Updates the gui with a failure or success message
	 * @param success Success or fail messages based off this boolean
	 */
	public void updateResult(Boolean success)
	{
		if(success)
		{
			lblResults.setForeground(Color.GREEN);
			lblResults.setText("Insert was successful");
		}
		else
		{
			lblResults.setForeground(Color.RED);
			lblResults.setText("Insert failed");
		}
		
		refresh(); // refresh the screen
	}

	/**
	 * Change the state of the machine
	 */
	@Override
	public void handle()
	{
		gui.setState(gui.getState("table"));
	}

	/**
	 * Listens for when the Insert button is pressed and sends the
	 * information to JDBC for processing. Once finished, updates the
	 * gui with a success or failure message, and refreshed the TableGui
	 * screen to include the new row.
	 */
	@Override
	public void actionPerformed(ActionEvent event)
	{
		System.out.println("Insert button was pressed");
		
		ArrayList<String> columns = new ArrayList<String>();
		ArrayList<String> values = new ArrayList<String>();
		
		// Collect the strings from labels and fields
		for(JLabel j : labels)
		{
			columns.add(j.getText());
		}
		for(JTextField t : fields)
		{
			values.add(t.getText());
		}
		
		// Call jdbc insert method
		try
		{
			jdbc.insert(tableName, columns, values);
		}
		catch (SQLException | IOException e)
		{
			e.printStackTrace();
		}
		
		// Update the guis
		TableGui table = (TableGui) gui.getState("table");
		table.select.run();
		updateResult(jdbc.wasLastQuerySuccessful());
	}
}
