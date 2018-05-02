package assignmentTwo;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.ImageIcon;
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
	private JLabel contentPane;
	private JButton btnInsert;
	private Gui gui;
	private JDBC jdbc;
	private ArrayList<TableData> data;
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
		
		contentPane = new JLabel(new ImageIcon("src\\assignmentTwo\\turtle2.png"));
		setContentPane(contentPane);
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
		btnInsert.setBackground(gui.BACKGROUND_DARK);
		btnInsert.setForeground(gui.LABEL_FG_LIGHT);
		c.gridy = 1;
		getContentPane().add(btnInsert, c);
		
		pnlResults = new JPanel();
		pnlResults.setLayout(new GridLayout(2,0,0,4));
		pnlResults.setBackground(gui.TRANSPARENT_WHITE);
		pnlResults.setOpaque(true);
		c.gridy = 2;
		getContentPane().add(pnlResults, c);
		
		JLabel results = new JLabel("Results:"); // label for results
		results.setForeground(gui.LABEL_BG_DARK);
		results.setPreferredSize(new Dimension(300,20));
		results.setBorder(new EmptyBorder(10,10,10,10));
		pnlResults.add(results);
		
		lblResults = new JLabel(); // where user will be updated with success or fail
		lblResults.setBorder(new EmptyBorder(10,10,10,10));
		lblResults.setForeground(gui.LABEL_BG_DARK);
		lblResults.setOpaque(false);
		lblResults.setBackground(Color.GRAY);
		
		pnlResults.add(lblResults);
		
		
		
		
		
		JPanel pnlInstructions = new JPanel();
		pnlInstructions.setBounds(702, 462, 300, 100);
		pnlInstructions.setBackground(gui.TRANSPARENT_WHITE);
		pnlInstructions.setBorder(new EmptyBorder(4,10,10,4));
		pnlInstructions.setLayout(new GridLayout(4,0));
		pnlInstructions.add(new JLabel("New Value - Enter the appropriate value in the correct box"));
		pnlInstructions.add(new JLabel("Insert - Inserts the data into the selected table"));
		c.anchor = GridBagConstraints.SOUTH;
		c.gridx = 2;
		c.gridy =2;
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
		content.setBorder(new EmptyBorder(10,10,10,10));
		content.setBackground(gui.TRANSPARENT_WHITE);
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;
		c.weightx = 0.5;
		c.gridy = 0;
		c.insets = new Insets(1,10,1,10);

		// Add the column labels
		for (int j = 0; j < numColumns; j++)
		{
			JLabel l = new JLabel(data.get(0).getColumnLabel(j),SwingConstants.CENTER);
			c.gridx = j+1;
			l.setPreferredSize(new Dimension(100,20));
			l.setForeground(gui.LABEL_BG_DARK);
			labels.add(l);
			content.add(l,c);
		}
		
		c.weightx = 0.0;
		c.gridx = 0;
		c.gridy = 1;
		JLabel valuesLabel = new JLabel("New Value: ");
		valuesLabel.setForeground(gui.LABEL_BG_DARK);
		content.add(valuesLabel,c);
		
		// Add the text fields
		for (int i = 0; i < numColumns; i ++)
		{
			JTextField j = new JTextField();
			c.gridx=i+1;
			fields.add(j);
			content.add(j,c);
		}
		
		// Add the constraints labels
		for (int j = 0; j < numColumns; j++)
		{
			String constraints = new String("<html>");

			if (!(data.get(0).getType(j).equals("")))
			{
				constraints += data.get(0).getType(j) + "<br>";
			}
			if (!(data.get(0).getPkValue(j).equals("")))
			{
				constraints += data.get(0).getPkValue(j) + "<br>";
			}
			if (!(data.get(0).getNullValue(j).equals("")))
			{
				constraints += data.get(0).getNullValue(j) + "<br>";
			}

			if (!(data.get(0).getFkValue(j).equals("")))
			{
				constraints += "Foreign Keys:<br>";
				String fk = new String("");
				fk = "  " + data.get(0).getFkValue(j).replace(",", "<br>");
				constraints += fk;
			}

			constraints += "</html>";

			JLabel l = new JLabel(constraints, SwingConstants.CENTER);
			c.anchor = GridBagConstraints.NORTHWEST;
			c.gridx = j + 1;
			c.gridy = 3;
			l.setForeground(gui.LABEL_BG_DARK);
			content.add(l, c);
		}
		
		return content;
	}
	
	/**
	 * Updates the gui with a failure or success message
	 * @param success Success or fail messages based off this boolean
	 */
	public void updateResult(Boolean success)
	{
		lblResults.setOpaque(true);
		if(success)
		{
			lblResults.setBackground(new Color(164,245,121,70));
			lblResults.setText("Insert was successful");
		}
		else
		{
			lblResults.setBackground(new Color(245,121,121,70));
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
		catch (IOException e)
		{
			e.printStackTrace();
		}
		
		// Update the guis
		TableGui table = (TableGui) gui.getState("table");
		table.select.run();
		updateResult(jdbc.wasLastQuerySuccessful());
	}
}
