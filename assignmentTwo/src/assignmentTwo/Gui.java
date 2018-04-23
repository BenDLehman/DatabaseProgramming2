package assignmentTwo;

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
	private State updateGui;
	private State deleteGui;
	private State currentScreen;
	private State previousScreen;
	boolean forcedSelect = false;

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
	 * Initialize all of the states and add the arena to the list of observers
	 */
	public Gui() throws Exception
	{
		tableGui = new TableGui();
		insertGui = new InsertGui();
		updateGui = new UpdateGui();
		deleteGui = new DeleteGui();
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
		boolean maximize = false; // certain screens should be maximized

		previousScreen = currentScreen;

		if (state == insertGui)
		{
			close = false;
			maximize = false;
		}

		if (state == deleteGui)
		{
			close = false;
			maximize = false;
		}
		if (state == updateGui)
		{
			close = false;
			maximize = false;
		}

		if (close && currentScreen != null)
		{
			currentScreen.dispose();
		}
		
		state.initialize();

		currentScreen = state;
		currentScreen.display(maximize);
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
		case "update":
			state = updateGui;
			break;
		case "delete":
			state = deleteGui;
			break;
		default:
			state = tableGui;
			break;
		}

		return state;
	}
}
