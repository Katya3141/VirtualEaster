package eggbonk.core;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.List;

import javax.swing.*;

import eggbonk.network.Client;

public class Gui {
    
    public enum Screen
    {
        START, WAITING, DRAWING, EGG_BONK_SETUP, EGG_BONK_READY, EGG_BONK_ANIMATION, EGG_BONK_RESULT, FINAL_RESULT;
    }
    
    private static Client myClient;
    
	static String name = "";
	
	static GridBagConstraints c = new GridBagConstraints();
	static JFrame frame;
	static JPanel panel;

	static Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

	public static void startGUI(Client client) {
	    myClient = client;

		frame = new JFrame("Virtual Easter");
		frame.setSize(screenSize.width, screenSize.height);

		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);


		panel = new JPanel();    
		panel.setPreferredSize(screenSize);
		panel.setLayout(new GridBagLayout());

		frame.add(panel);
		switchToScreen(Screen.START, null);

		frame.setVisible(true);
	}
	
	public static void switchToScreen(Screen screen, GameState gameState) {
	    panel.removeAll();
        panel.setPreferredSize(screenSize);
        
        switch(screen) {
        case DRAWING:
            drawingScreen();
            break;
        case EGG_BONK_ANIMATION:
            eggBonkAnimationScreen(gameState);
            break;
        case EGG_BONK_READY:
            eggBonkReadyScreen(gameState.getWinner(), gameState.getLoser());
            break;
        case EGG_BONK_RESULT:
            eggBonkResultScreen(gameState.getWinner(), gameState.getLoser());
            break;
        case EGG_BONK_SETUP:
            eggBonkSetupScreen(gameState.getPlayers());
            break;
        case FINAL_RESULT:
            finalResultScreen(gameState.getPlayers().get(0));
            break;
        case START:
            startScreen();
            break;
        case WAITING:
            waitingScreen(gameState.getPlayers());
            break;
        default:
            break;
        }
        
        frame.pack();
        frame.repaint();
	}
	
	private static void startScreen() {
	  //add JLabels to panel
        addJLabel("WELCOME TO", false, 20, new Color(0x4287f5), 40, 3, 0, 0, 0);
        addJLabel("VIRTUAL EASTER 2020", true, 50, new Color(0x4287f5), 40, 3, 0, 0, 1);
        addJLabel("To get started, enter your name: ", false, 20, new Color(0xfaa0fa), -1, 4, 0, 0, 3);

        //Text field to enter name
        JTextField text = addTextField("E.B.", 4, 0, 4);

        //Go button
        JButton button = addButton("GO!", 3, 3, 4);
        
        //button listener to set name
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                name = text.getText();
                System.out.println(name);
                switchToScreen(Gui.Screen.DRAWING, null);
            }          
        });

        panel.setBackground(Color.WHITE);
	}
	
	private static void waitingScreen(List<Player> players) {
	    panel.setBackground(new Color(0xfff178));
	    
        System.out.println(players);
        addJLabel("Waiting for players...", true, 20, Color.BLACK, -1, 3, 0, 2, 0);
        
        for(int i = 0; i < players.size(); i++)
            addJLabel(players.get(i).getName(), false, 20, Color.BLACK, -1, 3, 0, 2, i+1);
	}
	
	private static void drawingScreen() {
	    
	    //button: done with drawing on eggs -> myClient.sendPlayer(new Player(name, egg1, egg2));
	    
	    JButton button = addButton("test!", 3, 3, 4);
	    
	    //button listener to set name
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                try {
                    myClient.sendPlayer(new Player(name, new Egg(), new Egg()));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }          
        });
	}
	
	private static void eggBonkSetupScreen(List<Player> players) {
	    //TODO setup screen
		panel.setBackground(new Color(0xfae6be));

		int bigHalf;
		if(players.size() % 2 == 1)
			bigHalf = (int)(players.size() / 2) + 1;
		else
			bigHalf = players.size() / 2;
		
		for(int i = 0; i < bigHalf; i++)
			addJLabel(players.get(i).getName(), false, 40, Color.BLACK, 70, 1, 50, i * 2, 1);

		for(int i = bigHalf; i < players.size(); i++) {
			if(players.size() % 2 == 0)
				addJLabel(players.get(i).getName(), false, 40, Color.BLACK, 70, 1, 50, (i-bigHalf) * 2, 2);
			else
				addJLabel(players.get(i).getName(), false, 40, Color.BLACK, 70, 1, 50, (i-bigHalf) * 2+1, 2);
		}

	}
	
	private static void eggBonkReadyScreen(Player winner, Player loser) {
	    //TODO display self (saved in this.name) and other bonker)
	    //TODO ready button -> myClient.sendReady();
		panel.setBackground(new Color(0xff9e91));
		addJLabel("YOUR EGG HAS BEEN CALLED TO BONK!", true, 40, Color.BLACK, -1, 3, 0, 1, 0);
		JButton readyButton = addButton("READY!", 3, 3, 2);
		readyButton.addActionListener(new ActionListener() {
			@Override
            public void actionPerformed(ActionEvent arg0) {
                try {
                    readyButton.setEnabled(false);
                		myClient.sendReady();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
		});
	}
	
	private static void eggBonkAnimationScreen(GameState gameState) {
	    //TODO do the animation, make sure to randomize positions
	    
	    Gui.switchToScreen(Screen.EGG_BONK_RESULT, gameState);
	}
	
	private static void eggBonkResultScreen(Player winner, Player loser) {
	    //TODO display the results
	}
	
	private static void finalResultScreen(Player winner) {
	    addJLabel("THE EGG BONK CHAMPION 2020 IS", false, 20, new Color(0x4287f5), 40, 3, 0, 0, 0);
        addJLabel(winner.getName(), true, 50, new Color(0x4287f5), 40, 3, 0, 0, 1);
        
        //TODO display egg pictures?
	}

	
	
    private static void addJLabel(String text, boolean bold, int size, Color color, int ipy, int gw, int ipx, int x, int y) {
    	JLabel label = new JLabel(text);
    	if(bold)
    		label.setFont(new Font("Futura", Font.BOLD, size));
    	else
    		label.setFont(new Font("Futura", Font.PLAIN, size));
    	label.setForeground(color);
    	c.fill = GridBagConstraints.HORIZONTAL;
    	if(ipy != -1)
    		c.ipady = ipy;
    	if(ipx != -1)
    		c.ipadx = ipx;
    	c.gridwidth = gw;
    	c.gridx = x;
    	c.gridy = y;
    	panel.add(label, c);
    }
    
    private static JTextField addTextField(String defaultText, int gw, int x, int y) {
        JTextField text = new JTextField(defaultText);
        text.setFont(new Font("Futura", Font.PLAIN, 30));
        text.setHorizontalAlignment(SwingConstants.CENTER);
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 0.0;
        c.gridwidth = gw;
        c.gridx = x;
        c.gridy = y;
        panel.add(text, c);
        
        //return the text field so the value is accessible in button listeners
        return text;
    }

    private static JButton addButton(String buttonText, int gw, int x, int y) {
        JButton button = new JButton(buttonText);
        button.setFont(new Font("Futura", Font.PLAIN, 30));
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 0.0;
        c.gridwidth = gw;
        c.gridx = x;
        c.gridy = y;
        panel.add(button, c);
        
        //return the button so that it can be used in button listeners
        return button;
    }
}

