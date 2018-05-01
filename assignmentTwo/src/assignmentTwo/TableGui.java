package assignmentTwo;

import java.awt.Color;

import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import com.mysql.jdbc.DatabaseMetaData;

import javax.swing.JButton;
import javax.swing.JEditorPane;
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
import java.io.IOException;
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
	private JButton btnModify;
	private JButton btnInsert;
	private JButton selection;
	private JLabel lblEnterQueryTo;
	private JLabel lblResults;
	private JPanel pnlResults;
	private JLabel lblSelected;
	private JLabel selected;
	private JDBC jdbc;
	private Gui gui;
	public Runnable select;

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
		        /*if (confirm == 0) 
		        {
		           try {
					jdbc.dropTables();
				} catch (SQLException e1) 
		           {
					e1.printStackTrace();
		           }
		           System.exit(0);
		        }*/
		    }
		    
		};
		this.addWindowListener(exitListener);
		
		if(selected==null) {
			createActionButtons();
			
			createDisplayLabels();
		}
		
	}

	/**
	 * Allows JDBC methods to be called
	 * @throws Exception
	 */
	private void prepareJDBC() throws Exception
	{
		jdbc = new JDBC();
		/*if(tablesExist)
			try {
					jdbc.dropTables();
			}
		catch(SQLException e1) {
			
		}
		jdbc.buildTables();
		jdbc.populateTables();*/
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
		
		lblSelected = new JLabel();
		lblSelected.setBounds(80, 144, 550, 23);
		lblSelected.setOpaque(false);
		contentPane.add(lblSelected);
		
		pnlResults = new JPanel();
		pnlResults.setBackground(Color.lightGray);
		pnlResults.setBounds(10, 179, 705, 383);
		contentPane.add(pnlResults);
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
		btnSelect.setEnabled(false);
		btnSelect.addActionListener(this);
		btnSelect.setBounds(128, 35, 89, 23);
		contentPane.add(btnSelect);

		btnModify = new JButton("Modify");
		btnModify.setEnabled(false);
		btnModify.addActionListener(this);
		btnModify.setBounds(810, 35, 89, 23);
		contentPane.add(btnModify);

		btnInsert = new JButton("Insert");
		btnInsert.setEnabled(false);
		btnInsert.addActionListener(this);
		btnInsert.setBounds(711, 35, 89, 23);
		contentPane.add(btnInsert);
	}
	
	/**
	 * Updates the results panel when a JDBC command is called which
	 * returns an ArrayList of strings
	 * @param list The ArrayList of strings to update the results panel with
	 */
	public void updateResultsStrings(ArrayList<String> list)
	{
		if(pnlResults.getComponentCount()>0)
		{
			pnlResults.removeAll();
		}
		
		if(list.size()<11)
		{
			pnlResults.setLayout(new GridLayout(list.size(),1));
		}
		else
		{
			pnlResults.setLayout(new GridLayout(list.size()/2,2));
		}
		
		
		for (String s : list)
		{	
			JLabel l = new JLabel(s, SwingConstants.CENTER);
			pnlResults.add(l);
			l.addMouseListener(this);
		}
		refresh();
	}
	
	/**
	 * Updates the results panel when a JDBC command is called
	 * @param list
	 */
	public void updateResultsData(ArrayList<TableData> list)
	{
		// remove old data from gui
		if(pnlResults.getComponentCount()>0)
		{
			pnlResults.removeAll();
		}
		
		int numColumns = list.get(0).getNumColums();
		System.out.println(numColumns);
		
		// reset the grid size
		pnlResults.setLayout(new GridLayout(0,numColumns));
		
		// get the data from the list
		for(int x = 0; x < numColumns; x++)
		{
			JLabel l = new JLabel(list.get(0).getColumnLabel(x), SwingConstants.CENTER);
			pnlResults.add(l, x);
		}
		for(int x = 0; x < list.size(); x++)
		{
			for(int y = 0; y < numColumns; y++)
			{
				TableData t = list.get(x);
				JLabel l = new JLabel(t.getValue(y).toString(), SwingConstants.CENTER);
				String constraints = new String();
				if(!(t.getType(y).equals("")))
				{
					constraints += t.getType(y) + ", ";
				}
				if(!(t.getPkValue(y).equals("")))
				{
					constraints += t.getPkValue(y) + ", ";
				}
				if(!(t.getNullValue(y).equals("")))
				{
					constraints += t.getNullValue(y) + ", ";
				}
				if(!(t.getFkValue(y).equals("")))
				{
					constraints += "Foreign Key";
				}
				
				// Cut off the last ',' that didn't get replaced because it was at the end
				if(constraints.charAt(constraints.length()-2)==',')
				{
					constraints = constraints.substring(0, constraints.length()-2);
				}
				// and cut off the first character if it is a ','
				if(constraints.charAt(0)==',')
				{
					constraints = constraints.substring(1, constraints.length());
				}
				
				l.setName(constraints);
				pnlResults.add(l);
				l.addMouseListener(this); 
			}
		}
		
		refresh();
	}
	
	public void updateSelectedMessage(JLabel selected)
	{
		if (gui.getActiveTable() == null)
		{
			lblSelected.setText("Press 'Select' to view the contents of " + selected.getText());
		}
		else if (selected == null)
		{
			lblSelected.setText("");
		}
		else
		{
			String details = new String("Details("+selected.getText() + "): ");
			details += selected.getName();
			lblSelected.setText(details);
		}
	}

	/**
	 * Reaction when a JLabel in the results panel is pressed. Tells
	 * the user what they clicked.
	 */
	@Override
	public void mouseClicked(MouseEvent event)
	{
		// If there is a highlighted table already, make it normal again
		if(selected!=null)
		{
			selected.setOpaque(false);
			selected = null;
		}
		
		selected = (JLabel) event.getSource();
		selected.setBackground(Color.white);
		selected.setOpaque(true);
		
		System.out.println(selected.getText() + " was pressed");	
		
		updateSelectedMessage(selected);
		
		btnSelect.setEnabled(true);
		
		refresh();
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
		System.out.println(text + " was pressed");
		
		if(text.equals(btnShowTables.getText()))
		{
			lblSelected.setText("");
			btnModify.setEnabled(false);
			btnInsert.setEnabled(false);
			try
			{
				updateResultsStrings(jdbc.showTables());
			}
			catch (SQLException e)
			{
				e.printStackTrace();
			}
		}
		else if (text.equals(btnSelect.getText()))
		{
			gui.setActiveTable(selected.getText());
			select = new Runnable() {
					public void run() {
						try
						{
							updateResultsData(jdbc.select("*", selected.getText(), null, null));
						}
						catch (SQLException | IOException e)
						{
							e.printStackTrace();
						}
					}
				};
			select.run();
			updateSelectedMessage(null);
			btnModify.setEnabled(true);
			btnInsert.setEnabled(true);
		}
		else if (text.equals(btnModify.getText()) || text.equals(btnInsert.getText()))
		{
			gui.setState(gui.getState(text.toLowerCase()));
		}		
	}
}
