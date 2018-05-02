package assignmentTwo;

import java.awt.Color;

import javax.swing.JFrame;
import javax.swing.JRootPane;

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
	 * Returns the machine
	 * @return gui
	 */
	public Gui getGui()
	{
		return gui;
	}

	/**
	 * /**
	 * Sets the machine
	 * @param gui
	 */
	public void setGui(Gui gui)
	{
		this.gui = gui;
	}

	/**
	 * Allows a screen to be displayed in a specific size
	 * @param width The width of the screen
	 * @param height The height of the screen
	 */
	public void display(int xPos, int yPos, int width, int height)
	{
		this.setBounds(xPos,yPos,width,height);
		this.setUndecorated(true);
		this.getRootPane().setWindowDecorationStyle(5); 
		this.setVisible(true);
	}
	
	public void clear()
	{
		this.getContentPane().removeAll();
	}
	
	public void refresh()
	{
		this.revalidate();
		this.repaint();
	}

	/**
	 * Used for filling the screen after the state is created
	 */
	public void initialize()
	{
		this.initialize();
	}
}
