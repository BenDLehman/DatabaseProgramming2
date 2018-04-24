package assignmentTwo;

import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JTextField;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class InsertGui extends State implements ActionListener
{

	private JTextField fields[];
	private JButton btnInsert;
	private Gui gui;

	/**
	 * Create the application.
	 */
	public InsertGui(Gui gui)
	{
		this.gui = gui;
	}

	/**
	 * Initialize the contents of the frame.
	 */
	public void initialize()
	{
		getContentPane().setLayout(null);
		
		// Get the table name and display the title or tell the user to select a table
		String table = gui.getActiveTable();
		JLabel lblInsertingInto = new JLabel();
		if(table!=null)
		{
			lblInsertingInto.setText("Inserting into "+table);
		}
		else
		{
			lblInsertingInto.setText("Please select a table to modify on previous screen");
		}
		
		lblInsertingInto.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblInsertingInto.setBounds(10, 25, 192, 20);
		getContentPane().add(lblInsertingInto);
		
		createFields();
		
		btnInsert = new JButton("INSERT");
		btnInsert.addActionListener(this);
		btnInsert.setBounds(607, 144, 89, 23);
		getContentPane().add(btnInsert);
	}
	
	/**
	 * Dynamically creates the fields and places them in the gui based off of how many
	 * columns/attributes are in the table/relation.
	 */
	public void createFields()
	{
		
		int cols = 5; // Needs to call the jdbc method returning metadata and get num cols
		fields = new JTextField[cols];
		// Positions and dimensions of the first field
		int xPos = 10;
		int yPos = 145;
		int fieldWidth = 86;
		int fieldHeight = 20;
		// Where the line should break
		int maxWidth = 575;
			
		for (int i = 0; i < cols; i++)
		{
			fields[i] = new JTextField();
			if(xPos+fieldWidth >= maxWidth) // line break
			{
				xPos = 50;
				yPos = 145;
			}
			fields[i].setBounds(xPos,yPos,fieldWidth,fieldHeight);
			getContentPane().add(fields[i]);
			fields[i].setColumns(10);
			xPos+=96; // move the positions over for the next field
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

