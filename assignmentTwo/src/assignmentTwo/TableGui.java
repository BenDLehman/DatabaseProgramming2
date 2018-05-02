package assignmentTwo;

import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.awt.event.ActionEvent;

/**
 * The first gui that a user sees. Allows them to view tables, select tables to
 * work with, and open new windows for inserting, deleting, and updating.
 * 
 * @author Trevor Kelly, Andy Kim, Christopher Roadcap
 *
 */
public class TableGui extends State implements MouseListener, ActionListener, WindowListener
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
	private JPanel pnlInstructions;
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
		addWindowListener(this);
		contentPane = new JLabel(new ImageIcon("src\\assignmentTwo\\turtle.png"));
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		contentPane.setVisible(true);

		if (selected == null)
		{
			createActionButtons();
			createDisplayLabels();
		}
	}

	/**
	 * Creates all labels for the screen
	 */
	public void createDisplayLabels()
	{
		// Label for custom query
		lblEnterQueryTo = new JLabel("Enter query to see results below:");
		lblEnterQueryTo.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblEnterQueryTo.setBounds(10, 87, 218, 14);
		lblEnterQueryTo.setForeground(gui.LABEL_FG_LIGHT);
		contentPane.add(lblEnterQueryTo);

		// Field for custom query
		query = new JTextField();
		query.setBounds(10, 112, 590, 23);
		contentPane.add(query);
		query.setColumns(10);

		// Label for results
		lblResults = new JLabel("Results:");
		lblResults.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblResults.setBounds(10, 144, 63, 23);
		lblResults.setForeground(gui.LABEL_FG_LIGHT);
		contentPane.add(lblResults);

		// Informational label for tables, data, and instruction
		lblSelected = new JLabel();
		lblSelected.setBounds(80, 144, 550, 23);
		lblSelected.setOpaque(false);
		lblSelected.setForeground(gui.LABEL_FG_LIGHT);
		contentPane.add(lblSelected);

		// Panel where query results will be shown
		pnlResults = new JPanel();
		pnlResultsDefault = new JLabel("Click 'Show Tables' to view tables in the database");
		pnlResults.add(pnlResultsDefault);
		pnlResults.setBackground(gui.TRANSPARENT_WHITE);
		pnlResults.setBounds(10, 179, 685, 383);
		contentPane.add(pnlResults);

		// Instruction panel for users
		pnlInstructions = new JPanel();
		pnlInstructions.setBounds(702, 462, 300, 100);
		pnlInstructions.setBackground(gui.TRANSPARENT_WHITE);
		pnlInstructions.setBorder(new EmptyBorder(4, 4, 4, 4));
		pnlInstructions.setLayout(new GridLayout(4, 0));
		pnlInstructions.add(new JLabel("Show Tables - View all tables in database"));
		pnlInstructions.add(new JLabel("Select - View the data within the selected table"));
		pnlInstructions.add(new JLabel("Modify - Update/delete records in the selected table"));
		pnlInstructions.add(new JLabel("Insert - Insert new records into the selected table"));
		contentPane.add(pnlInstructions);
	}

	/**
	 * Creates buttons for the screen
	 */
	private void createActionButtons()
	{
		// Shows the tables in the db
		btnShowTables = new JButton("Show Tables");
		btnShowTables.addActionListener(this);
		btnShowTables.setBounds(10, 35, 108, 23);
		btnShowTables.setForeground(gui.LABEL_FG_LIGHT);
		btnShowTables.setBackground(gui.BACKGROUND_DARK);
		btnShowTables.setFocusable(false);
		contentPane.add(btnShowTables);

		// Shows the results of switching to a selected table
		btnSelect = new JButton("Select");
		btnSelect.setEnabled(false);
		btnSelect.addActionListener(this);
		btnSelect.setBounds(128, 35, 89, 23);
		btnSelect.setBackground(gui.BACKGROUND_DARK);
		btnSelect.setForeground(gui.LABEL_FG_LIGHT);
		btnSelect.setFocusable(false);
		contentPane.add(btnSelect);

		// Opens a new window for Updating/Deleting records
		btnModify = new JButton("Modify");
		btnModify.setEnabled(false);
		btnModify.addActionListener(this);
		btnModify.setBounds(509, 35, 89, 23);
		btnModify.setBackground(gui.BACKGROUND_DARK);
		btnModify.setForeground(gui.LABEL_FG_LIGHT);
		btnModify.setFocusable(false);
		contentPane.add(btnModify);

		// Opens a new window for Inserting new records
		btnInsert = new JButton("Insert");
		btnInsert.setEnabled(false);
		btnInsert.addActionListener(this);
		btnInsert.setBounds(608, 35, 89, 23);
		btnInsert.setBackground(gui.BACKGROUND_DARK);
		btnInsert.setForeground(gui.LABEL_FG_LIGHT);
		btnInsert.setFocusable(false);
		contentPane.add(btnInsert);

		// Submits the custom query from the custom query field
		btnCustomQuery = new JButton("Submit");
		btnCustomQuery.addActionListener(this);
		btnCustomQuery.setBounds(608, 112, 89, 23);
		btnCustomQuery.setBackground(gui.BACKGROUND_DARK);
		btnCustomQuery.setForeground(gui.LABEL_FG_LIGHT);
		btnCustomQuery.setFocusable(false);
		contentPane.add(btnCustomQuery);
	}

	/**
	 * Updates the results panel when a JDBC command is called which returns an
	 * ArrayList of strings
	 * @param list The ArrayList of strings to update the results panel with
	 */
	public void updateResultsStrings(ArrayList<String> list)
	{
		// If this isn't the first time it's been updated, remove old labels
		if (pnlResults.getComponentCount() > 0)
		{
			pnlResults.removeAll();
		}

		// Decide how many columns to display the results in
		if (list.size() < 11)
		{
			pnlResults.setLayout(new GridLayout(list.size(), 1));
		}
		else
		{
			pnlResults.setLayout(new GridLayout(list.size() / 2, 2));
		}

		// Add the table columns and rows to the results panel
		for (String s : list)
		{
			JLabel l = new JLabel(s, SwingConstants.CENTER);
			l.setForeground(gui.LABEL_BG_DARK);
			pnlResults.add(l);
			l.addMouseListener(this);
		}
		refresh(); // repaint the screen
	}

	/**
	 * Updates the results panel when a JDBC command is called
	 * @param list
	 */
	public void updateResultsData(ArrayList<TableData> list)
	{
		// remove old data from gui
		if (pnlResults.getComponentCount() > 0)
		{
			pnlResults.removeAll();
		}

		int numColumns = list.get(0).getNumColums();

		// reset the grid size
		pnlResults.setLayout(new GridLayout(0, numColumns));

		// get the data from the list and put it in the results
		for (int x = 0; x < numColumns; x++)
		{
			JLabel l = new JLabel(list.get(0).getColumnLabel(x), SwingConstants.CENTER);
			l.setForeground(gui.LABEL_BG_DARK);
			pnlResults.add(l, x);
		}
		for (int x = 0; x < list.size(); x++)
		{
			for (int y = 0; y < numColumns; y++)
			{
				TableData t = list.get(x);
				JLabel l = new JLabel(t.getValue(y).toString(), SwingConstants.CENTER);
				String constraints = new String();
				if (!(t.getType(y).equals("")))
				{
					constraints += t.getType(y) + ", ";
				}
				if (!(t.getPkValue(y).equals("")))
				{
					constraints += t.getPkValue(y) + ", ";
				}
				if (!(t.getNullValue(y).equals("")))
				{
					constraints += t.getNullValue(y) + ", ";
				}
				if (!(t.getFkValue(y).equals("")))
				{
					constraints += "Foreign Key";
				}

				// Cut off the last ',' that didn't get replaced because it was at the end
				if (constraints.charAt(constraints.length() - 2) == ',')
				{
					constraints = constraints.substring(0, constraints.length() - 2);
				}
				// and cut off the first character if it is a ','
				if (constraints.charAt(0) == ',')
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

	/**
	 * Updates the gui with which table/tuple was selected
	 * @param selected
	 */
	public void updateSelectedMessage(JLabel selected)
	{
		if (gui.getActiveTable() == null)
		{
			lblSelected.setText("Press 'Select' to view the contents of " + selected.getText());
		}
		else if (selected == null)
		{
			lblSelected.setText("Showing '" + gui.getActiveTable() + "'");
		}
		else
		{
			String details = new String("Details(" + selected.getText() + "): ");
			details += selected.getName();
			lblSelected.setText(details);
		}
	}

	/**
	 * Reaction when a JLabel in the results panel is pressed. Tells the user what
	 * they clicked.
	 */
	@Override
	public void mouseClicked(MouseEvent event)
	{
		// If there is a highlighted table already, make it normal again
		if (selected != null)
		{
			selected.setOpaque(false);
			selected = null;
		}

		// Store what was pressed and style it
		selected = (JLabel) event.getSource();
		selected.setBackground(Gui.TRANSPARENT_WHITE);
		selected.setOpaque(true);

		System.out.println(selected.getText() + " was pressed");

		// Update the gui with what was pressed
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
		if (event != null)
		{
			selection = (JButton) event.getSource();
			handle();
		}
	}

	/**
	 * Handles what should happen when a button is pressed and changes the state of
	 * the Gui
	 */
	@Override
	public void handle()
	{
		String text = selection.getText();
		System.out.println(text + " was pressed");

		// If Show Tables was pressed, show the tables and modify the gui
		if (text.equals(btnShowTables.getText()))
		{
			if (gui.getActiveTable() != null)
			{
				gui.setActiveTable(null);
			}
			lblSelected.setText("");
			btnModify.setEnabled(false);
			btnInsert.setEnabled(false);
			btnSelect.setEnabled(true);
			if (!(pnlResultsDefault.isOpaque()))
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
		// If select was pressed, show the table's data and modify the gui
		else if (text.equals(btnSelect.getText()))
		{
			btnSelect.setEnabled(false);
			gui.setActiveTable(selected.getText());
			pnlResultsDefault.setOpaque(false);
			select = new Runnable()
			{
				public void run()
				{
					try
					{
						updateResultsData(jdbc.select("*", gui.getActiveTable(), null, null));
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
		// If submit was pressed, execute the custom query
		else if (text.equals(btnCustomQuery.getText()))
		{
			try
			{
				updateResultsData(jdbc.customQuery(query.getText()));
			}
			catch (SQLException | IOException e)
			{
				e.printStackTrace();
			}

		}
		// If modify/insert was pressed, open the respective window
		else if (text.equals(btnModify.getText()) || text.equals(btnInsert.getText()))
		{
			gui.setState(gui.getState(text.toLowerCase()));
		}
	}

	@Override
	public void windowActivated(WindowEvent arg0)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void windowClosed(WindowEvent arg0)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void windowClosing(WindowEvent arg0)
	{
		// If this window is being closed, confirm closing
		int confirm = JOptionPane.showConfirmDialog(null, "Are You Sure You Want to Close Application?",
				"Exit Confirmation", JOptionPane.YES_NO_OPTION);
		if (confirm == JOptionPane.YES_OPTION)
		{
			System.exit(0);
		}
		else
		{
			this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		}
	}

	@Override
	public void windowDeactivated(WindowEvent arg0)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void windowDeiconified(WindowEvent arg0)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void windowIconified(WindowEvent arg0)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void windowOpened(WindowEvent arg0)
	{
		// TODO Auto-generated method stub

	}
}
