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
		
		getContentPane().setLayout(new GridBagLayout());
		//JPanel controls = new JPanel(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		//c.fill = GridBagConstraints.HORIZONTAL;
		c.weightx = 0;
		c.insets = new Insets(10,10,10,10);	

		/*if (tableName != null)
		{
			if (lblInsertingInto != null)
			{
				getContentPane().remove(lblInsertingInto);
			}
			lblInsertingInto = new JLabel("Inserting into " + tableName);
		}
		else
		{
			lblInsertingInto = new JLabel("Please select a table to modify on previous screen");
		}*/
		
		this.setTitle("Inserting into " + tableName);

		/*lblInsertingInto.setFont(new Font("Tahoma", Font.PLAIN, 14));
		c.gridx = 0;
		c.gridy = 0;
		getContentPane().add(lblInsertingInto, c);*/
		
		
		//System.out.println(controls);
		//getContentPane().add(controls, 0);

		try
		{
			c.gridx=0;
			c.gridy=0;
			getContentPane().add(createFields(), c);
		}
		catch (SQLException | IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		btnInsert = new JButton("INSERT");
		btnInsert.addActionListener(this);
		c.gridx = 0;
		c.gridy = 1;
		getContentPane().add(btnInsert, c);
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
		fields = new JTextField[numColumns];

		for (int j = 0; j < numColumns; j++)
		{
			JLabel l = new JLabel(data.get(0).getColumnLabel(j),SwingConstants.CENTER);
			c.gridx = j;
			c.gridy = 0;
			l.setPreferredSize(new Dimension(100,20));
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
		
		return content;
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
