package assignmentTwo;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.awt.event.ActionEvent;

public class InsertGui extends State implements ActionListener
{

	private JTextField fields[];
	private JButton btnInsert;
	private Gui gui;
	private JLabel lblInsertingInto = null;
	private JDBC jdbc;
	private ArrayList<DBRow> data;
	private String tableName;
	private int numColumns;

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

		try
		{
			data = jdbc.select("*", tableName, null, null);
			numColumns = data.get(0).getNumColums();
		}
		catch (SQLException | IOException e1)
		{
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		
		getContentPane().setLayout(new GridLayout(2,0));
		JPanel controls = new JPanel(new GridLayout());
		controls.setMaximumSize(new Dimension(700,20));

		if (tableName != null)
		{
			if (lblInsertingInto != null)
			{
				controls.remove(lblInsertingInto);
			}
			lblInsertingInto = new JLabel("Inserting into " + tableName);
		}
		else
		{
			lblInsertingInto = new JLabel("Please select a table to modify on previous screen");
		}

		lblInsertingInto.setFont(new Font("Tahoma", Font.PLAIN, 14));
		controls.add(lblInsertingInto);
		
		btnInsert = new JButton("INSERT");
		btnInsert.addActionListener(this);
		btnInsert.setMaximumSize(new Dimension(100,20));
		controls.add(btnInsert);
		
		getContentPane().add(controls, 0);

		try
		{
			createFields();
		}
		catch (SQLException | IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Dynamically creates the fields and places them in the gui based off of how
	 * many columns/attributes are in the table/relation.
	 * @throws IOException
	 * @throws SQLException
	 */
	public void createFields() throws SQLException, IOException
	{
		JPanel content = new JPanel(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;
		c.weightx = 0.5;
		c.insets = new Insets(10,10,10,10);
		fields = new JTextField[numColumns];
		
		getContentPane().add(content, 1);

		for (int j = 0; j < numColumns; j++)
		{
			JLabel l = new JLabel(data.get(0).getColumnLabel(j),SwingConstants.CENTER);
			c.gridx = j;
			c.gridy = 0;
			content.add(l,c);
		}
		
		c.weightx = 0.0;
		
		for (int i = 0; i < numColumns; i ++)
		{
			fields[i] = new JTextField();
			c.gridx=i;
			c.gridy=1;
			content.add(fields[i],c);
		}
	}

	@Override
	public void handle()
	{
		gui.setState(gui.getState("table"));
	}

	@Override
	public void actionPerformed(ActionEvent event)
	{
		// call the jdbc method that prepares and inserts the changes
		System.out.println("Insert button was pressed");
	}

}
