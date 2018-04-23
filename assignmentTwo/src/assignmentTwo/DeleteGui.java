package assignmentTwo;

import java.awt.EventQueue;
import java.awt.Font;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class DeleteGui extends State
{

	private JFrame frame;
	private JTextField textField;
	private JTextField textField_1;
	private JTextField textField_2;
	private JTextField textField_3;
	private JTextField textField_4;
	private JTextField textField_5;
	private JButton btnDelete;
	private JButton btnDeleteTheWhole;
	private JTextField textField_6;
	private JTextField textField_7;
	private JTextField textField_8;
	private JTextField textField_9;
	private JTextField textField_10;
	private JTextField textField_11;
	private JButton btnWhere;
	private JButton btnDelete_1;
	/**
	 * Launch the application.
	 */
	public static void delete()
	{
		EventQueue.invokeLater(new Runnable()
		{
			public void run()
			{
				try
				{
					DeleteGui window = new DeleteGui();
					window.frame.setVisible(true);
				} catch (Exception e)
				{
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public DeleteGui()
	{
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	public void initialize()
	{
		frame = new JFrame();
		frame.setBounds(100, 100, 759, 516);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JLabel lblInsertingInto = new JLabel("Delete <table name>");
		lblInsertingInto.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblInsertingInto.setBounds(10, 25, 192, 20);
		frame.getContentPane().add(lblInsertingInto);
		
		textField = new JTextField();
		textField.setBounds(10, 145, 86, 20);
		frame.getContentPane().add(textField);
		textField.setColumns(10);
		
		textField_1 = new JTextField();
		textField_1.setBounds(106, 145, 86, 20);
		frame.getContentPane().add(textField_1);
		textField_1.setColumns(10);
		
		textField_2 = new JTextField();
		textField_2.setBounds(202, 145, 86, 20);
		frame.getContentPane().add(textField_2);
		textField_2.setColumns(10);
		
		textField_3 = new JTextField();
		textField_3.setBounds(298, 145, 86, 20);
		frame.getContentPane().add(textField_3);
		textField_3.setColumns(10);
		
		textField_4 = new JTextField();
		textField_4.setBounds(394, 145, 86, 20);
		frame.getContentPane().add(textField_4);
		textField_4.setColumns(10);
		
		textField_5 = new JTextField();
		textField_5.setBounds(490, 145, 86, 20);
		frame.getContentPane().add(textField_5);
		textField_5.setColumns(10);
		
		btnDeleteTheWhole = new JButton("DELETE THE WHOLE TABLE");
		btnDeleteTheWhole.setBounds(10, 56, 154, 23);
		frame.getContentPane().add(btnDeleteTheWhole);
		
		textField_6 = new JTextField();
		textField_6.setBounds(10, 314, 86, 20);
		frame.getContentPane().add(textField_6);
		textField_6.setColumns(10);
		
		textField_7 = new JTextField();
		textField_7.setBounds(106, 314, 86, 20);
		frame.getContentPane().add(textField_7);
		textField_7.setColumns(10);
		
		textField_8 = new JTextField();
		textField_8.setBounds(202, 314, 86, 20);
		frame.getContentPane().add(textField_8);
		textField_8.setColumns(10);
		
		textField_9 = new JTextField();
		textField_9.setBounds(298, 314, 86, 20);
		frame.getContentPane().add(textField_9);
		textField_9.setColumns(10);
		
		textField_10 = new JTextField();
		textField_10.setBounds(394, 314, 86, 20);
		frame.getContentPane().add(textField_10);
		textField_10.setColumns(10);
		
		textField_11 = new JTextField();
		textField_11.setBounds(490, 314, 86, 20);
		frame.getContentPane().add(textField_11);
		textField_11.setColumns(10);
		
		btnWhere = new JButton("WHERE");
		btnWhere.setBounds(586, 144, 89, 23);
		frame.getContentPane().add(btnWhere);
		
		btnDelete_1 = new JButton("DELETE");
		btnDelete_1.setBounds(586, 313, 89, 23);
		frame.getContentPane().add(btnDelete_1);
		

	}

	@Override
	public void handle()
	{
		// TODO Auto-generated method stub
		
	}

}
