package eggbonk.core;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Toolkit;
import javax.swing.*;


public class gui {
	public static void main(String[] args) {
		//creates new JFrame
		JFrame frame = new JFrame("Virtual Easter");
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		frame.setSize(screenSize.width, screenSize.height);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		//creates new JPanel and adds to the JFrame
		JPanel panel = new JPanel();    
		frame.add(panel);

		//places all the different components into the panel and sets the frame visible
		placeStuff(panel);
		frame.setVisible(true);
	}
	
	public static void startGUI() {
	    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int screenHeight = screenSize.height;
        int screenWidth = screenSize.width;

        JFrame frame = new JFrame("Virtual Easter");

        frame.setSize(screenWidth, screenHeight);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel();    

        frame.add(panel);
        placeStuff(panel);

        frame.setVisible(true);
	}

	/**
	 * Places all of the different components inside the JPanel
	 * @param the JPanel
	 */
	private static void placeStuff(JPanel panel) {
		JLabel label;
		panel.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();

		//welcome JLabel
		label = new JLabel("WELCOME TO");
		label.setFont(new Font("Arial", Font.PLAIN, 20));
		label.setForeground(new Color(0x4287f5));
		c.fill = GridBagConstraints.HORIZONTAL;
		c.ipady = 40;
		c.weightx = 0.0;
		c.gridwidth = 3;
		c.gridx = 0;
		c.gridy = 0;
		panel.add(label, c);

		//Virtual Easter JLabel
		label = new JLabel("VIRTUAL EASTER 2020");
		label.setFont(new Font("Arial", Font.BOLD, 50));
		label.setForeground(new Color(0x4287f5));
		c.fill = GridBagConstraints.HORIZONTAL;
		c.ipady = 40;
		c.weightx = 0.0;
		c.gridwidth = 3;
		c.gridx = 0;
		c.gridy = 1;
		panel.add(label, c);

		//Enter your name JLabel
		label = new JLabel("To get started, enter your name: ");
		label.setFont(new Font("Arial", Font.PLAIN, 20));
		label.setForeground(new Color(0xfaa0fa));
		c.fill = GridBagConstraints.HORIZONTAL;
		c.weightx = 0.0;
		c.gridwidth = 4;
		c.gridx = 0;
		c.gridy = 3;
		panel.add(label, c);
		
		//Textfield to enter name
		JTextField text = new JTextField("E.B.");
		text.setFont(new Font("Arial", Font.PLAIN, 30));
		text.setHorizontalAlignment(JTextField.CENTER);
		c.fill = GridBagConstraints.HORIZONTAL;
		c.weightx = 0.0;
		c.gridwidth = 4;
		c.gridx = 0;
		c.gridy = 4;
		panel.add(text, c);
		
		//Go button
		JButton button = new JButton("GO!");
		button.setFont(new Font("Arial", Font.PLAIN, 30));
		c.fill = GridBagConstraints.HORIZONTAL;
		c.weightx = 0.0;
		c.gridwidth = 3;
		c.gridx = 3;
		c.gridy = 4;
		panel.add(button, c);


		panel.setBackground(Color.WHITE);
	}

}