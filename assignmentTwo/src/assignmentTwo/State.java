package assignmentTwo;

import javax.swing.JFrame;

/**
 * Allows classes that extend it to get the same functionality of JFrame as well
 * as some other functions that are consistent with the state pattern and help
 * consistently display and close windows.
 * 
 * @author Trevor Kelly
 *
 */
public abstract class State extends JFrame
{
	/**
	 * The machine/context in the state pattern
	 */
	private Gui gui;

	/*
	 * Allows the states to change
	 */
	public abstract void handle();

	/**
	 * returns the machine
	 * 
	 * @return gameUI
	 */
	public Gui getGui()
	{
		return gui;
	}

	/**
	 * Sets the machine
	 * 
	 * @param gameUI The machine
	 */
	public void setGui(Gui gui)
	{
		this.gui = gui;
	}

	/**
	 * Allows a screen to be displayed. If a screen should be maximized, maximize is
	 * true
	 * 
	 * @param maximize Whether the screen should be maximized or not
	 */
	public void display(boolean maximize)
	{
		if (maximize)
		{
			this.setExtendedState(JFrame.MAXIMIZED_BOTH);
		}
		this.setLocationRelativeTo(null);
		this.pack();
		this.setVisible(true);
	}

	/**
	 * Used for filling the screen after the state is created
	 */
	public void initialize()
	{
		this.initialize();
	}
}
