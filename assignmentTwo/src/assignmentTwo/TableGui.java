package assignmentTwo;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import com.mysql.jdbc.DatabaseMetaData;

import javax.swing.ImageIcon;
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
	private JLabel contentPane;
	private JTextField query;
	boolean tablesExist = true;
	private JButton btnShowTables;
	private JButton btnSelect;
	private JButton btnModify;
	private JButton btnInsert;
	private JButton selection;
	private JButton btnCustomQuery;
	private JLabel lblEnterQueryTo;
	private JLabel lblResults;
	private JPanel pnlResults;
	private JLabel pnlResultsDefault;
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
		jdbc = new JDBC();
	}
	
	/**
	 * Fill the screen with content
	 */
	public void initialize()
	{
		this.setTitle("Flaming Turtle SQL Software");
		contentPane = new JLabel(new ImageIcon("src\\assignmentTwo\\turtle.png"));
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		contentPane.setVisible(true);
		
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
	 * Creates labels for the screen
	 */
	public void createDisplayLabels()
	{
		lblEnterQueryTo = new JLabel("Enter query to see results below:");
		lblEnterQueryTo.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblEnterQueryTo.setBounds(10, 87, 218, 14);
		lblEnterQueryTo.setForeground(gui.LABEL_FG_LIGHT);
		contentPane.add(lblEnterQueryTo);

		query = new JTextField();
		query.setBounds(10, 112, 601, 23);
		contentPane.add(query);
		query.setColumns(10);

		lblResults = new JLabel("Results:");
		lblResults.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblResults.setBounds(10, 144, 63, 23);
		lblResults.setForeground(gui.LABEL_FG_LIGHT);
		contentPane.add(lblResults);
		
		lblSelected = new JLabel();
		lblSelected.setBounds(80, 144, 550, 23);
		lblSelected.setOpaque(false);
		lblSelected.setForeground(gui.LABEL_FG_LIGHT);
		contentPane.add(lblSelected);
		
		pnlResults = new JPanel();
		pnlResultsDefault = new JLabel("Click 'Show Tables' to view tables in the database");
		pnlResults.add(pnlResultsDefault);
		pnlResults.setBackground(gui.TRANSPARENT_WHITE);
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
		btnShowTables.setForeground(gui.LABEL_FG_LIGHT);
		btnShowTables.setBackground(gui.BACKGROUND_DARK);
		btnShowTables.setFocusable(false);
		contentPane.add(btnShowTables);

		btnSelect = new JButton("Select");
		btnSelect.setEnabled(false);
		btnSelect.addActionListener(this);
		btnSelect.setBounds(128, 35, 89, 23);
		btnSelect.setBackground(gui.BACKGROUND_DARK);
		btnSelect.setForeground(gui.LABEL_FG_LIGHT);
		btnSelect.setFocusable(false);
		contentPane.add(btnSelect);

		btnModify = new JButton("Modify");
		btnModify.setEnabled(false);
		btnModify.addActionListener(this);
		btnModify.setBounds(616, 35, 89, 23);
		btnModify.setBackground(gui.BACKGROUND_DARK);
		btnModify.setForeground(gui.LABEL_FG_LIGHT);
		btnModify.setFocusable(false);
		contentPane.add(btnModify);

		btnInsert = new JButton("Insert");
		btnInsert.setEnabled(false);
		btnInsert.addActionListener(this);
		btnInsert.setBounds(711, 35, 89, 23);
		btnInsert.setBackground(gui.BACKGROUND_DARK);
		btnInsert.setForeground(gui.LABEL_FG_LIGHT);
		btnInsert.setFocusable(false);
		contentPane.add(btnInsert);
		
		btnCustomQuery = new JButton("Submit");
		btnCustomQuery.addActionListener(this);
		btnCustomQuery.setBounds(616, 112, 89, 23);
		btnCustomQuery.setBackground(gui.BACKGROUND_DARK);
		btnCustomQuery.setForeground(gui.LABEL_FG_LIGHT);
		btnCustomQuery.setFocusable(false);
		contentPane.add(btnCustomQuery);
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
			l.setForeground(gui.LABEL_BG_DARK);
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
		
		// reset the grid size
		pnlResults.setLayout(new GridLayout(0,numColumns));
		
		// get the data from the list
		for(int x = 0; x < numColumns; x++)
		{
			JLabel l = new JLabel(list.get(0).getColumnLabel(x), SwingConstants.CENTER);
			l.setForeground(gui.LABEL_BG_DARK);
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
				l.setForeground(gui.LABEL_BG_DARK);
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
			lblSelected.setText("Showing '"+gui.getActiveTable()+"'");
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
		selected.setBackground(Gui.TRANSPARENT_WHITE);
		selected.setOpaque(true);
		
		System.out.println(selected.getText() + " was pressed");	
		
		updateSelectedMessage(selected);
		
		btnSelect.setEnabled(true);
		
		refresh();
	}

	@Override
	public void mouseEntered(MouseEvent event)
	{
		
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
			btnSelect.setEnabled(true);
			if(!(pnlResultsDefault.isOpaque()))
			{
				lblSelected.setText(pnlResultsDefault.getText());
			}
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
			btnSelect.setEnabled(false);
			gui.setActiveTable(selected.getText());
			pnlResultsDefault.setOpaque(false);
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
		else if (text.equals(btnCustomQuery.getText()))
		{
			try
			{
				updateResultsData(jdbc.customQuery("SELECT * FROM TEST_DELETE"));
			}
			catch (SQLException | IOException e)
			{
				e.printStackTrace();
			}
			
		}		
		else if (text.equals(btnModify.getText()) || text.equals(btnInsert.getText()))
		{
			gui.setState(gui.getState(text.toLowerCase()));
		}		
	}
}
