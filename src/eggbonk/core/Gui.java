package eggbonk.core;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.List;
import static java.util.concurrent.TimeUnit.SECONDS;

import org.jdesktop.core.animation.timing.Animator;
import org.jdesktop.core.animation.timing.PropertySetter;
import org.jdesktop.core.animation.timing.interpolators.AccelerationInterpolator;
import org.jdesktop.swing.animation.timing.sources.SwingTimerTimingSource;

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
	    //System.out.println(frame.getComponents());
	    Container content = frame.getContentPane();
	    //Creates a new container
	    content.setLayout(new BorderLayout());
	    //sets the layout

	    final PadDraw drawPad = new PadDraw();
	    //creates a new padDraw, which is pretty much the paint program

	    content.add(drawPad, BorderLayout.CENTER);
	    //sets the padDraw in the center
	    
	    JButton done = new JButton("Done coloring!");
	    
	    content.add(done, BorderLayout.SOUTH);
	    
	    content.setPreferredSize(screenSize);
	    
	    //button: done with drawing on eggs -> myClient.sendPlayer(new Player(name, egg1, egg2));
	    
	    //button listener to complete drawings
        done.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                frame.setContentPane(new Container());
                frame.add(panel);
                frame.setPreferredSize(screenSize);
                try {
                    myClient.sendPlayer(new Player(name, new Egg(), new Egg()));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }          
        });
	}
	
	private static void eggBonkSetupScreen(List<Player> players) {
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
	
	static private class ImagePanel extends JPanel {

		private static final long serialVersionUID = 3849958772549333929L;
		
		private BufferedImage image;
//		Animator animator;
		
		private ImagePanel(BufferedImage img) {
			image = img;
			this.setPreferredSize(new Dimension(img.getWidth(), img.getHeight()));
		}
		
		
		@Override
	    protected void paintComponent(Graphics g) {
	        super.paintComponent(g);
	        g.drawImage(image, 0, 0, this);          
	    }
	}
	
	private static void eggBonkAnimationScreen(GameState gameState) {
		
	    panel.setVisible(false);

		JPanel animationPanel = new JPanel();
		animationPanel.setPreferredSize(screenSize);

		frame.add(animationPanel);
		
	    int random = (int) (Math.random() * 2);
	    ImagePanel a, b;
	    if(random == 0) {
	    	a = new ImagePanel(gameState.getWinner().currentEgg().getImage());
	    	b = new ImagePanel(gameState.getLoser().currentEgg().getImage());
	    } else {
	    	a = new ImagePanel(gameState.getLoser().currentEgg().getImage());
	    	b = new ImagePanel(gameState.getWinner().currentEgg().getImage());
	    }
	    
	    a.setLocation(animationPanel.getWidth() / 4, animationPanel.getHeight() / 2);
	    b.setLocation(animationPanel.getWidth() * 3 / 4, animationPanel.getHeight() / 2);
	    
	    animationPanel.add(a);
	    animationPanel.add(b);
	    
	    //TODO do the animation
	    
	    /*
	    SwingTimerTimingSource animationTimer = new SwingTimerTimingSource();
	    animationTimer.init();
	    
	    a.animator = new Animator.Builder(animationTimer).setDuration(2, SECONDS).setDisposeTimingSource(true).build();
	    b.animator = new Animator.Builder(animationTimer).setDuration(2, SECONDS).setDisposeTimingSource(true).build();
	    
	    Point target = new Point(panel.getWidth() / 2, panel.getHeight() / 2);
	    
	    a.animator.addTarget(PropertySetter.getTargetTo(a, "location", new AccelerationInterpolator(0.5,0.5), target));
	    b.animator.addTarget(PropertySetter.getTargetTo(b, "location", new AccelerationInterpolator(0.5,0.5), target));

	    a.animator.start();
	    b.animator.start();
	    */
	    
	    // display a on the left
	    // display b on the right
	    // zooom them at each other
	    // KAPOW
	    
	    Gui.switchToScreen(Screen.EGG_BONK_RESULT, gameState);
	}
	
	private static void eggBonkResultScreen(Player winner, Player loser) {
	    //TODO display the results
		addJLabel(loser.getName() + " CRACKED, " + winner.getName() + " WINS!", true, 50, new Color(0x4287f5), 40, 3, 0, 0, 0);
		JLabel winnerLabel = new JLabel(new ImageIcon(winner.currentEgg().getImage()));
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

