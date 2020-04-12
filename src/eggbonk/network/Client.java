package eggbonk.network;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import eggbonk.core.GameState;
import eggbonk.core.Gui;
import eggbonk.core.Gui.Screen;
import eggbonk.core.Player;

public class Client implements AutoCloseable {
    private Socket socket;
 //   private BufferedReader in;
    private ObjectInputStream in;
    private ObjectOutputStream out;
    
    private GameState gameState;
    
    private Player me;
    
    /**
     * Make a Client and connect it to a server running on
     * hostname at the specified port.
     * @throws IOException if can't connect
     */
    public Client(String hostname, int port) throws IOException {
        socket = new Socket(hostname, port);
        out = new ObjectOutputStream(socket.getOutputStream());
        socket.getInputStream();
        in = new ObjectInputStream(socket.getInputStream());
    }
    
    /**
     * Send a new player to the server. Requires this is "open".
     * @param player player to send
     * @throws IOException if network or server failure
     */
    public void sendPlayer(Player player) throws IOException {
        out.writeObject(player);
        out.flush(); // important! make sure x actually gets sent
        me = player;
    }
    
    /**
     * Send ready message to the server. Requires this is "open".
     * @throws IOException if network or server failure
     */
    public void sendReady() throws IOException {
        out.writeObject("ready");
        out.flush(); // important! make sure x actually gets sent
    }
    
    /**
     * Send UNready message to the server. Requires this is "open".
     * @throws IOException if network or server failure
     */
    public void sendUnReady() throws IOException {
        out.writeObject("not ready");
        out.flush(); // important! make sure x actually gets sent
    }
    
    /**
     * Get a reply from the next request that was submitted.
     * Requires this is "open".
     * @return square of requested number
     * @throws IOException if network or server failure
     */
    public GameState getState() throws IOException {
        Object reply = null;
		try {
			reply = in.readObject();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
        if (reply == null) {
            throw new IOException("connection terminated unexpectedly");
        }

        return (GameState) reply;
    }

    /**
     * Closes the client's connection to the server.
     * This client is now "closed". Requires this is "open".
     * @throws IOException if close fails
     */
    @Override public void close() throws IOException {
        in.close();
        out.close();
        socket.close();
    }
    
    public void startClient() throws IOException {
        Gui.startGUI(this);
 
        do {
            gameState = getState();
            Gui.switchToScreen(Gui.Screen.WAITING, gameState);

        } while(gameState.getPhase() == GameState.Phase.SETUP);

        do {
            if (gameState.getWinner().equals(me) || gameState.getLoser().equals(me))
                Gui.switchToScreen(Screen.EGG_BONK_READY, gameState);
            else
                Gui.switchToScreen(Screen.EGG_BONK_SETUP, gameState);
            
            // wait until bonkers are ready
            while(gameState.getPhase() != GameState.Phase.BONKING) { gameState = getState(); }
            Gui.switchToScreen(Screen.EGG_BONK_ANIMATION, gameState);
            
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            
            gameState = getState();
        } while (gameState.getPhase() != GameState.Phase.TOTAL_VICTORY);
        
        Gui.switchToScreen(Screen.FINAL_RESULT, gameState);
    }
    
    
    /**
     * Use a Client to open the GUI and communicate with the server.
     * @param args unused
     */
    public static void main(String[] args) {
        try (
                Client client = new Client("98.229.165.182", 1234); // katya: 192.168.2.82
        ) {
            client.startClient();
            
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }
}
