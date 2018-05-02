package assignmentTwo;

import java.awt.Color;

import javax.swing.JFrame;

/**
 * Serves as the machine in the state pattern. Allows the user to view different
 * screens based on the current state.
 * 
 * @author Trevor Kelly
 */
public class Gui // implements TurnObserver
{
	private State tableGui;
	private State insertGui;
	private State modifyGui;
	private State currentScreen;
	private State previousScreen;
	private String activeTable;
	boolean forcedSelect = false;
	public static final Color TRANSPARENT_WHITE = new Color(255,255,255,175);
	public static final Color LABEL_FG_LIGHT = Color.WHITE;
	public static final Color LABEL_BG_DARK = Color.BLACK;
	public static final Color BACKGROUND_DARK = Color.BLACK;

	/**
	 * For testing purposes
	 * 
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception
	{
		Gui gui = new Gui();
	}

	/*
	 * Initialize all of the states
	 */
	public Gui() throws Exception
	{
		tableGui = new TableGui(this);
		insertGui = new InsertGui(this);
		modifyGui = new ModifyGui(this);
		setState(tableGui);
	}

	/**
	 * Sets a state as the currentScreen. If there is already a screen visible then
	 * we hide it before setting the currentScreen and making it visible
	 * 
	 * @param state The state that will be the currentScreen
	 */
	public void setState(State state)
	{
		boolean close = true; // certain screens shouldn't close the application
		int xPos = 50;
		int yPos = 50; // starting x and y positions of the screen
		int width = 1024; //  default width of a screen
		int height = 624; // default height of a screen

		previousScreen = currentScreen;
		
		
		if (state == insertGui || state == modifyGui )
		{
			close = false;
			xPos += width-10;
			width = 800;
			height = 500;
		}

		if (close && currentScreen != null)
		{
			currentScreen.dispose();
		}
		
		state.clear();
		state.initialize();
		
		if(close)
		{
			state.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		}
		else
		{
			state.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		}

		currentScreen = state;
		currentScreen.display(xPos, yPos, width,height);
	}
	
	/**
	 * Get the active table that was selected by mouseclick fron results
	 * @return the active table
	 */
	public String getActiveTable()
	{
		return activeTable;
	}
	
	/**
	 * Sets the gui's current active table
	 * @param table Active table
	 */
	public void setActiveTable(String table)
	{
		activeTable = table;
	}

	/**
	 * Gets the current state of the machine
	 * 
	 * @return currentScreen
	 */
	public State getCurrentState()
	{
		return currentScreen;
	}

	/**
	 * Gets the state based off of a string name
	 * 
	 * @param stateName The name of the state
	 * @return the state that matches stateName
	 */
	public State getState(String stateName)
	{
		State state = null;

		switch (stateName)
		{
		case "table":
			state = tableGui;
			break;
		case "insert":
			state = insertGui;
			break;
		case "modify":
			state = modifyGui;
			break;
		default:
			state = tableGui;
			break;
		}

		return state;
	}
}
