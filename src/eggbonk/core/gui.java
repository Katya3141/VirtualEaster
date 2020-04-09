package eggbonk.core;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;

import javax.swing.*;

public class gui {
	public static void main(String[] args) {
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

	private static void placeStuff(JPanel panel) {
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		int screenHeight = screenSize.height;
		int screenWidth = screenSize.width;
		
		panel.setLayout(null);

		JLabel welcome = new JLabel("WELCOME TO");
		welcome.setBounds(screenWidth/2-150,screenHeight/6,300,25);
		welcome.setFont(new Font("Arial Rounded MT Bold", Font.BOLD, 30));
		welcome.setForeground(new Color(0x03a5fc));
		panel.add(welcome);

		JLabel ve = new JLabel("VIRTUAL EASTER!");
		ve.setBounds(screenWidth/2-300,screenHeight/4,650,60);
		ve.setFont(new Font("Arial Rounded MT Bold", Font.BOLD, 70));
		ve.setForeground(new Color(0x03a5fc));
		panel.add(ve);

		JLabel name = new JLabel("Enter your name to start!");
		name.setBounds(screenWidth/2-300,screenHeight/3+50,500,25);
		name.setFont(new Font("Arial Rounded MT Bold", Font.BOLD, 25));
		name.setForeground(new Color(0xffa1f9));
		panel.add(name);

		JTextField userText = new JTextField(20);
		userText.setBounds(screenWidth/2+20,screenHeight/3+50,120,25);
		panel.add(userText);

		JButton go = new JButton("GO!");
		go.setBounds(screenWidth/2,screenHeight/2,140,80);
		go.setFont(new Font("Arial Rounded MT Bold", Font.BOLD, 50));
		go.setForeground(new Color(0xa9fcab));
		go.setBorder(null);
		panel.add(go);
		
		panel.setBackground(Color.WHITE);
	}

}
