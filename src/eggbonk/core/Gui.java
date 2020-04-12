package eggbonk.core;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
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
	    frame.remove(panel);
	    
	    JPanel drawPanel = new JPanel();

	    drawPanel.setLayout(new BorderLayout(120, 100));


	    final PadDraw eggPad1 = new PadDraw();
	    eggPad1.setPreferredSize(new Dimension(700,300));
	    final PadDraw eggPad2 = new PadDraw();

	    //creates a new padDraw, which is pretty much the paint program
	    
	    eggPad1.setPreferredSize(new Dimension(600, 600));
	    eggPad1.setBorder(BorderFactory.createMatteBorder(10, 10, 10, 10, Color.pink)); // inner border
	    eggPad2.setPreferredSize(new Dimension(600, 600));
	    eggPad2.setBorder(BorderFactory.createMatteBorder(10, 10, 10, 10, Color.pink)); // inner border

	    
	    JPanel buttonPanel = new JPanel();
	    JButton black = new JButton("black");
	    JButton red = new JButton("red");
	    JButton magenta = new JButton("magenta");
	    JButton blue = new JButton("blue");
	    JButton green = new JButton("green");
	    JButton pink = new JButton("pink");
	    JButton yellow = new JButton("yellow");
	    JButton lightBlue = new JButton("light blue");
	    JButton lightGreen = new JButton("light green");
	    
	    buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
	    
	    red.addActionListener(new ActionListener() {

	        @Override
	        public void actionPerformed(ActionEvent event) {
	            eggPad1.red();
	            eggPad2.red();
	        }
	    });
	    
        black.addActionListener(new ActionListener() {
    
            @Override
            public void actionPerformed(ActionEvent event) {
                eggPad1.black();
                eggPad2.black();
            }
        });
       
        magenta.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent event) {
                eggPad1.magenta();
                eggPad2.magenta();
            }
        });
       
        blue.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent event) {
                eggPad1.blue();
                eggPad2.blue();
            }
        });
        
        green.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent event) {
                eggPad1.green();
                eggPad2.green();
            }
        });
        
        pink.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent event) {
                eggPad1.pink();
                eggPad2.pink();
            }
        });
        
        yellow.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent event) {
                eggPad1.paleYellow();
                eggPad2.paleYellow();
            }
        });
        
        lightBlue.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent event) {
                eggPad1.lightBlue();
                eggPad2.lightBlue();
            }
        });
        
        lightGreen.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent event) {
                eggPad1.lightGreen();
                eggPad2.lightGreen();
            }
        });
	    
	    buttonPanel.add(red);
	    buttonPanel.add(black);
	    buttonPanel.add(magenta);
	    buttonPanel.add(blue);
	    buttonPanel.add(green);
	    buttonPanel.add(yellow);
	    buttonPanel.add(pink);
	    buttonPanel.add(lightBlue);
	    buttonPanel.add(lightGreen);
	    
	    drawPanel.add(buttonPanel, BorderLayout.CENTER);

	    drawPanel.add(eggPad1, BorderLayout.EAST);
	    drawPanel.add(eggPad2, BorderLayout.WEST);
	    //sets the padDraw in the east
	    
	    JButton done = new JButton("Done coloring!");
	    done.setFont(new Font("Futura", Font.PLAIN, 40));
	    done.setPreferredSize(new Dimension(100, 100));
	    
	    drawPanel.add(done, BorderLayout.SOUTH);
	    
	    drawPanel.setPreferredSize(screenSize);
	    
	    frame.add(drawPanel);
	    
	    //button: done with drawing on eggs -> myClient.sendPlayer(new Player(name, egg1, egg2));
	    
	    //button listener to complete drawings
        done.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                frame.remove(drawPanel);
                frame.add(panel);
                frame.setPreferredSize(screenSize);
                try {

                    myClient.sendPlayer(new Player(name, new Egg(eggPad2.getImage()), new Egg(eggPad1.getImage())));

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }          
        });

	}

	private static void eggBonkSetupScreen(List<Player> players) {
	    try {
            myClient.sendUnReady();
        } catch (IOException e) {
            e.printStackTrace();
        }
	    
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

	static class AnimationPanel extends JPanel {

		private static final long serialVersionUID = -7889930119324580716L;

		BufferedImage a, b;
		GameState gameState;
		int up;
		int aX, bX;
		int velX = 1;

		AnimationPanel(BufferedImage a, BufferedImage b, GameState gameState, int up) {
			super();
			setLayout(null);
			this.setBackground(new Color(0xffb5f8));
			setPreferredSize(screenSize);
			this.a = a;
			this.b = b;
			this.gameState = gameState;
			this.up = up;
			setVisible(true);
			aX = 0;
			bX = screenSize.width - b.getWidth();
		}

		@Override
		protected void paintComponent(Graphics g) {
			super.paintComponent(g);
			g.setFont(new Font("Futura", Font.PLAIN, 40));
			
			if(up == 0) {
				g.drawString(this.gameState.getWinner().getName(), screenSize.width/2, screenSize.height/10);
				g.drawString(this.gameState.getLoser().getName(), screenSize.width/2, screenSize.height/10 * 9);
			}
			else {
				g.drawString(this.gameState.getLoser().getName(), screenSize.width/2, screenSize.height/10);
				g.drawString(this.gameState.getWinner().getName(), screenSize.width/2, screenSize.height/10 * 9);
			}
			g.drawString("VS", screenSize.width/2, screenSize.height/2);
			
			
			g.drawImage(a, aX, (screenSize.height / 2) - (a.getHeight() / 2), this);
			g.drawImage(b, bX, (screenSize.height / 2) - (b.getHeight() / 2), this);
			if(aX < screenSize.width / 3 - 10) {
				aX += 3;
				bX -= 3;
				this.repaint();
			}
			else {
				frame.remove(this);
				Gui.switchToScreen(Screen.EGG_BONK_RESULT, gameState);
			}
		}
	}

	private static void eggBonkAnimationScreen(GameState gameState) {

		panel.setVisible(false);



		int random = (int) (Math.random() * 2);
		BufferedImage a, b;
		int up;
		if(random == 0) {
			a = gameState.getWinner().currentEgg().getImage();
			b = gameState.getLoser().currentEgg().getImage();
			up = 0;
		} else {
			a = gameState.getLoser().currentEgg().getImage();
			b = gameState.getWinner().currentEgg().getImage();
			up = 1;
		}
		System.out.println("EGG INFO for: " + gameState.getLoser().getName());
        System.out.println(gameState.getLoser().currentEgg().getDamage() + " " + gameState.getLoser().currentEgg().isBroken());
        System.out.println(gameState.getLoser().backupEgg().getDamage() + " " + gameState.getLoser().backupEgg().isBroken());

		JPanel animationPanel = new AnimationPanel(a, b, gameState, up);
		frame.add(animationPanel);

	}

	private static void eggBonkResultScreen(Player winner, Player loser) {
		//TODO display the results
		panel.setVisible(true);
		addJLabel(loser.getName() + " CRACKED, " + winner.getName() + " WINS!", true, 50, new Color(0x4287f5), 40, 3, 0, 0, 0);
		JLabel winnerLabel = new JLabel(new ImageIcon(winner.currentEgg().getImage()));
		panel.add(winnerLabel);
		c.gridx = 0; c.gridy = 1;
		panel.add(winnerLabel, c);
	}

	private static void finalResultScreen(Player winner) {
		addJLabel(winner.getName() + " WINS EGG BONK 2020!", true, 50, new Color(0x4287f5), 40, 3, 0, 0, 1);
		JLabel picLabelWinner = new JLabel(new ImageIcon(winner.currentEgg().getImage()));
		c.gridx = 0; c.gridy = 2;
		panel.add(picLabelWinner, c);

		//TODO: add cracked loser
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

