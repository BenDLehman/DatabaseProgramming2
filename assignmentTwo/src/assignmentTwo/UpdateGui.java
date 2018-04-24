package assignmentTwo;

import java.awt.EventQueue;
import java.awt.Font;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

public class UpdateGui extends State
{

	private JTextField textField;
	private JTextField textField_1;
	private JTextField textField_2;
	private JTextField textField_3;
	private JTextField textField_4;
	private JTextField textField_5;
	private JTextField textField_6;
	private JTextField textField_7;
	private JTextField textField_8;
	private JTextField textField_9;
	private JTextField textField_10;
	private JTextField textField_11;
	private JButton btnWhere;
	private JButton btnUpdate;
	private Gui gui;

	/**
	 * Create the application.
	 */
	public UpdateGui(Gui gui)
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
		JLabel lblUpdating = new JLabel();
		if (table != null)
		{
			lblUpdating.setText("Updating " + table);
		}
		else
		{
			lblUpdating.setText("Please select a table to modify on previous screen");
		}
				
		lblUpdating.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblUpdating.setBounds(10, 25, 192, 20);
		getContentPane().add(lblUpdating);
		
		textField = new JTextField();
		textField.setBounds(10, 145, 86, 20);
		getContentPane().add(textField);
		textField.setColumns(10);
		
		textField_1 = new JTextField();
		textField_1.setBounds(106, 145, 86, 20);
		getContentPane().add(textField_1);
		textField_1.setColumns(10);
		
		textField_2 = new JTextField();
		textField_2.setBounds(202, 145, 86, 20);
		getContentPane().add(textField_2);
		textField_2.setColumns(10);
		
		textField_3 = new JTextField();
		textField_3.setBounds(298, 145, 86, 20);
		getContentPane().add(textField_3);
		textField_3.setColumns(10);
		
		textField_4 = new JTextField();
		textField_4.setBounds(394, 145, 86, 20);
		getContentPane().add(textField_4);
		textField_4.setColumns(10);
		
		textField_5 = new JTextField();
		textField_5.setBounds(490, 145, 86, 20);
		getContentPane().add(textField_5);
		textField_5.setColumns(10);
				
		textField_6 = new JTextField();
		textField_6.setBounds(10, 314, 86, 20);
		getContentPane().add(textField_6);
		textField_6.setColumns(10);
		
		textField_7 = new JTextField();
		textField_7.setBounds(106, 314, 86, 20);
		getContentPane().add(textField_7);
		textField_7.setColumns(10);
		
		textField_8 = new JTextField();
		textField_8.setBounds(202, 314, 86, 20);
		getContentPane().add(textField_8);
		textField_8.setColumns(10);
		
		textField_9 = new JTextField();
		textField_9.setBounds(298, 314, 86, 20);
		getContentPane().add(textField_9);
		textField_9.setColumns(10);
		
		textField_10 = new JTextField();
		textField_10.setBounds(394, 314, 86, 20);
		getContentPane().add(textField_10);
		textField_10.setColumns(10);
		
		textField_11 = new JTextField();
		textField_11.setBounds(490, 314, 86, 20);
		getContentPane().add(textField_11);
		textField_11.setColumns(10);
		
		btnWhere = new JButton("WHERE");
		btnWhere.setBounds(586, 144, 89, 23);
		getContentPane().add(btnWhere);
		
		btnUpdate = new JButton("UPDATE");
		btnUpdate.setBounds(586, 313, 89, 23);
		getContentPane().add(btnUpdate);

	}

	@Override
	public void handle()
	{
		gui.setState(gui.getState("table"));
	}

}
