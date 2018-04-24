package assignmentTwo;

import java.awt.Color;

import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.sql.SQLException;
import java.util.ArrayList;
import java.awt.event.ActionEvent;
import javax.swing.JTextArea;

/**
 * The first gui that a user sees. Allows them to view tables, select tables to
 * work with, and open new windows for inserting, deleting, and updating. 
 * 
 * @author Trevor Kelly, Andy Kim, Christopher Roadcap
 *
 */
public class TableGui extends State implements MouseListener, ActionListener
{

	private JPanel contentPane;
	private JTextField query;
	boolean tablesExist = true;
	private JButton btnShowTables;
	private JButton btnSelect;
	private JButton btnDelete;
	private JButton btnUpdate;
	private JButton btnInsert;
	private JButton selection;
	private JLabel lblEnterQueryTo;
	private JLabel lblResults;
	private JPanel pnlResults;
	private JTextArea jtaSelected;
	private String selected;
	private JDBC jdbc;
	private Gui gui;

	/**
	 * Create the screen
	 * @param gui 
	 * @throws Exception
	 */
	public TableGui(Gui gui) throws Exception
	{
		this.gui = gui;
	}
	
	/**
	 * Fill the screen with content
	 */
	public void initialize()
	{
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		try
		{
			prepareJDBC();
		}
		catch (Exception e2)
		{
			e2.printStackTrace();
		}
		
		WindowListener exitListener = new WindowAdapter() {

		    @Override
		    public void windowClosing(WindowEvent e) {
		        int confirm = JOptionPane.showOptionDialog(
		             null, "Are You Sure You Want to Close Application?", 
		             "Exit Confirmation", JOptionPane.YES_NO_OPTION, 
		             JOptionPane.QUESTION_MESSAGE, null, null, null);
		        if (confirm == 0) 
		        {
		           try {
					jdbc.dropTables();
				} catch (SQLException e1) 
		           {
					e1.printStackTrace();
		           }
		           System.exit(0);
		        }
		    }
		    
		};
		this.addWindowListener(exitListener);
		
		createActionButtons();
		
		createDisplayLabels();
	}

	/**
	 * Allows JDBC methods to be called
	 * @throws Exception
	 */
	private void prepareJDBC() throws Exception
	{
		jdbc = new JDBC();
		if(tablesExist)
			try {
					jdbc.dropTables();
			}
		catch(SQLException e1) {
			
		}
		jdbc.buildTables();
		jdbc.populateTables();
	}
	
	/**
	 * Creates labels for the screen
	 */
	public void createDisplayLabels()
	{
		lblEnterQueryTo = new JLabel("Enter query to see results below:");
		lblEnterQueryTo.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblEnterQueryTo.setBounds(10, 87, 218, 14);
		contentPane.add(lblEnterQueryTo);

		query = new JTextField();
		query.setBounds(10, 112, 705, 20);
		contentPane.add(query);
		query.setColumns(10);

		lblResults = new JLabel("Results:");
		lblResults.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblResults.setBounds(10, 144, 63, 23);
		contentPane.add(lblResults);
		
		pnlResults = new JPanel();
		pnlResults.setBackground(Color.lightGray);
		pnlResults.setBounds(10, 179, 705, 383);
		contentPane.add(pnlResults);
		
		jtaSelected = new JTextArea();
		jtaSelected.setLineWrap(true);
		jtaSelected.setBounds(730, 179, 270, 150);
		jtaSelected.setOpaque(false);
		contentPane.add(jtaSelected);
	}
	
	/**
	 * Creates buttons for the screen
	 */
	private void createActionButtons()
	{
		btnShowTables = new JButton("Show Tables");
		btnShowTables.addActionListener(this);
		btnShowTables.setBounds(10, 35, 108, 23);
		contentPane.add(btnShowTables);

		btnSelect = new JButton("Select");
		btnSelect.addActionListener(this);
		btnSelect.setBounds(128, 35, 89, 23);
		contentPane.add(btnSelect);

		btnDelete = new JButton("Delete");
		btnDelete.addActionListener(this);
		btnDelete.setBounds(909, 35, 89, 23);
		contentPane.add(btnDelete);

		btnUpdate = new JButton("Update");
		btnUpdate.addActionListener(this);
		btnUpdate.setBounds(810, 35, 89, 23);
		contentPane.add(btnUpdate);

		btnInsert = new JButton("Insert");
		btnInsert.addActionListener(this);
		btnInsert.setBounds(711, 35, 89, 23);
		contentPane.add(btnInsert);
	}
	
	/**
	 * Updates the results panel when a JDBC command is called
	 * @param list
	 */
	public void updateResults(ArrayList<String> list)
	{
		pnlResults.setLayout(new GridLayout(list.size(),1));
		
		for (String s : list)
		{	
			JLabel l = new JLabel(s, SwingConstants.CENTER);
			pnlResults.add(l);
			l.addMouseListener(this);
		}
		revalidate();
		repaint();
	}
	
	

	/**
	 * Reaction when a JLabel in the results panel is pressed. Tells
	 * the user what they clicked.
	 */
	@Override
	public void mouseClicked(MouseEvent event)
	{
		JLabel source = (JLabel) event.getSource();
		selected = source.getText();
		
		System.out.println(selected + " was pressed");	
		jtaSelected.setText("Press 'Select' to select the "+selected+" table.");
		
		revalidate();
		repaint();
	}

	@Override
	public void mouseEntered(MouseEvent arg0)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent arg0)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent arg0)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent arg0)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void actionPerformed(ActionEvent event)
	{
		if(event!=null)
		{
			selection = (JButton) event.getSource();
			gui.setActiveTable(selection.getText());
			handle();
		}
	}

	/**
	 * Handles what should happen when a button is pressed and changes
	 * the state of the Gui
	 */
	@Override
	public void handle()
	{
		String text = selection.getText();
		
		if(text.equals(btnShowTables.getText()))
		{
			System.out.println("Show Tables was pressed");
			try
			{
				updateResults(jdbc.showTables2());
			}
			catch (SQLException e)
			{
				e.printStackTrace();
			}
		}
		else if (text.equals(btnSelect.getText()))
		{
			System.out.println("Select was pressed");
			// call the jdbc select function using the
			// class variable 'selected' which is a string
			// containing the name of the table clicked.
		}
		else if (text.equals(btnDelete.getText()))
		{
			System.out.println("Delete was pressed");
			gui.setState(gui.getState("delete"));
		}
		else if (text.equals(btnUpdate.getText()))
		{
			System.out.println("Update was pressed");
			gui.setState(gui.getState("update"));
		}
		else if (text.equals(btnInsert.getText()))
		{
			System.out.println("Insert was pressed");
			gui.setState(gui.getState("insert"));
		}
		else 
		{
			System.out.println("A button was not pressed");
		}
	}
}
