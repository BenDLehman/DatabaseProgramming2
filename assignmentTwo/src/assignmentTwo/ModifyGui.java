package assignmentTwo;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

public class ModifyGui extends State implements ActionListener
{
	private ArrayList<JTextField> whereFields;
	private ArrayList<JTextField> valueFields;
	private ArrayList<JLabel> labels;
	private JButton btnUpdate;
	private JButton btnDelete;
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
	public ModifyGui(Gui gui)
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
		this.setTitle("Updating into " + tableName);	
		labels = new ArrayList<JLabel>();
		valueFields = new ArrayList<JTextField>();
		whereFields = new ArrayList<JTextField>();
		
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

		JPanel controls = new JPanel(new GridBagLayout());
		
		// Add the update button and the results panel
		btnUpdate = new JButton("UPDATE");
		btnUpdate.addActionListener(this);
		c.gridx = 0;
		c.gridy = 0;
		controls.add(btnUpdate, c);
		
		btnDelete = new JButton("DELETE");
		btnDelete.addActionListener(this);
		c.gridx = 1;
		controls.add(btnDelete, c);
		
		c.gridx = 0;
		c.gridy = 1;
		getContentPane().add(controls, c);
		
		pnlResults = new JPanel();
		pnlResults.setLayout(new GridLayout(2,0,0,4));
		c.gridx = 0;
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
			c.gridx = j+1;
			l.setPreferredSize(new Dimension(100,20));
			labels.add(l);
			content.add(l,c);
		}
		
		c.weightx = 0.0;
		c.gridx = 0;
		c.gridy = 1;
		content.add(new JLabel("New Value: "),c);
		
		// Add the text fields
		for (int i = 0; i < numColumns; i ++)
		{
			JTextField j = new JTextField();
			c.gridx=i+1;
			c.gridy=1;
			valueFields.add(j);
			content.add(j,c);
		}
		
		c.gridx = 0;
		c.gridy = 2;
		content.add(new JLabel("Where: "),c);
		
		for (int x = 0; x < numColumns; x ++)
		{
			JTextField j = new JTextField();
			c.gridx=x+1;
			c.gridy=2;
			whereFields.add(j);
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
	 * Listens for when the Update button is pressed and sends the
	 * information to JDBC for processing. Once finished, updates the
	 * gui with a success or failure message, and refreshed the TableGui
	 * screen to include the updated rows.
	 */
	@Override
	public void actionPerformed(ActionEvent event)
	{
		String source = ((JButton)event.getSource()).getText();
		System.out.println(source + " button was pressed");
		
		// Call jdbc update method
		if(source.equals(btnUpdate.getText()))
		{
			
			ArrayList<String> columns = new ArrayList<String>();
			ArrayList<String> values = new ArrayList<String>();
			ArrayList<String> wheres = new ArrayList<String>();
			
			// Collect the strings from labels and fields
			for(JLabel j : labels)
			{
				columns.add(j.getText());
			}
			for(JTextField v : valueFields)
			{
				values.add(v.getText());
			}
			for(JTextField w : whereFields)
			{
				wheres.add(w.getText());
			}
			/*try
			{
				jdbc.update(tableName, columns, values, wheres);
			}
			catch (SQLException | IOException e)
			{
				e.printStackTrace();
			}*/
		}
		else if (source.equals(btnDelete.getText()))
		{
			String whereKey = new String();
			String whereValue = new String();
			int count = 0;
			
			/*for(JTextField w : whereFields)
			{
				if(!w.equals(""))
				{
					whereValue = w.getText();
					whereKey = labels.get(count).getText();
				}
				count++;
			}*/
			
			for(int x = 0; x < numColumns; x++)
			{
				if(!(whereFields.get(x).getText().equals("")))
				{
					whereValue = whereFields.get(x).getText();
					whereKey = labels.get(x).getText();
				}
			}
			
			try
			{
				jdbc.delete(tableName, whereKey, whereValue);
			}
			catch (SQLException | IOException e)
			{
				e.printStackTrace();
			}
		}
		
		// Update the guis
		TableGui table = (TableGui) gui.getState("table");
		// will uncomment once ToDo #4 is complete.
		table.select.run();
		updateResult(jdbc.wasLastQuerySuccessful());		
	}

}
