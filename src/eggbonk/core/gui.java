package eggbonk.core;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.*;


public class gui {
	static JTextField text;
	static String name = "";
	static GridBagConstraints c = new GridBagConstraints();
	static JFrame frame;
	static JPanel panel;
	public static List<Player> players = new ArrayList<Player>();

	static Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

	public static void main(String[] args) {
		//creates new JFrame
		frame = new JFrame("Virtual Easter");
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		frame.setSize(screenSize.width, screenSize.height);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		//creates new JPanel and adds to the JFrame
		panel = new JPanel();    
		frame.add(panel);

		//places all the different components into the panel and sets the frame visible
		placeStuffStart(panel);
		frame.setVisible(true);

		while(name == "") {
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {}
		}
		System.out.println("Done waiting");
		gui.waitingScreen();
	}

	public static String startGUI() {

		frame = new JFrame("Virtual Easter");
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		frame.setSize(screenSize.width, screenSize.height);

		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);


		panel = new JPanel();    
		panel.setPreferredSize(screenSize);

		frame.add(panel);
		placeStuffStart(panel);

		frame.setVisible(true);

		//wait for a name to be inputed and return it
		while(name == "") {
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {}
		}
		System.out.println("Done waiting");
		gui.waitingScreen();
		return name;
	}
	public static void waitingScreen() {
		panel.removeAll();
		panel.setPreferredSize(screenSize);
		placeStuffWaiting(panel);
		frame.pack();
		frame.repaint();

	}

	/**
	 * Places all of the different components inside the JPanel
	 * @param the JPanel
	 */
	private static void placeStuffStart(JPanel panel) {
		panel.setLayout(new GridBagLayout());
		//add JLabels to panel
		addJLabel("WELCOME TO", false, 20, new Color(0x4287f5), 40, 3, 0, 0, panel);
		addJLabel("VIRTUAL EASTER 2020", true, 50, new Color(0x4287f5), 40, 3, 0, 1, panel);
		addJLabel("To get started, enter your name: ", false, 20, new Color(0xfaa0fa), -1, 4, 0, 3, panel);

		//Textfield to enter name
		text = new JTextField("E.B.");
		text.setFont(new Font("Futura", Font.PLAIN, 30));
		text.setHorizontalAlignment(JTextField.CENTER);
		c.fill = GridBagConstraints.HORIZONTAL;
		c.weightx = 0.0;
		c.gridwidth = 4;
		c.gridx = 0;
		c.gridy = 4;
		panel.add(text, c);

		//Go button
		JButton button = new JButton("GO!");
		button.setFont(new Font("Futura", Font.PLAIN, 30));
		c.fill = GridBagConstraints.HORIZONTAL;
		c.weightx = 0.0;
		c.gridwidth = 3;
		c.gridx = 3;
		c.gridy = 4;
		panel.add(button, c);

		//button listener to set name
		button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				name = text.getText();
				System.out.println(name);
			}          
		});

		panel.setBackground(Color.WHITE);
	}
	private static void placeStuffWaiting(JPanel panel) {
		panel.setBackground(Color.YELLOW);
		System.out.println(players);
		addJLabel("Waiting for players...", true, 20, Color.BLACK, -1, 3, 2, 0, panel);
		for(int i = 0; i < players.size(); i++)
			addJLabel(players.get(i).name, false, 20, Color.BLACK, -1, 3, 2, i+1, panel);
		frame.pack();
	}


private static void addJLabel(String text, boolean bold, int size, Color color, int ipy, int gw, int x, int y, JPanel panel) {
	JLabel label = new JLabel(text);
	if(bold)
		label.setFont(new Font("Futura", Font.BOLD, size));
	else
		label.setFont(new Font("Futura", Font.PLAIN, size));
	label.setForeground(color);
	c.fill = GridBagConstraints.HORIZONTAL;
	if(ipy != -1)
		c.ipady = ipy;
	c.weightx = 0.0;
	c.gridwidth = gw;
	c.gridx = x;
	c.gridy = y;
	panel.add(label, c);
}

}

