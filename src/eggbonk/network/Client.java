package eggbonk.network;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.List;

import eggbonk.core.gui;
import eggbonk.core.Egg;
import eggbonk.core.Player;

public class Client implements AutoCloseable {
    private Socket socket;
 //   private BufferedReader in;
    private ObjectInputStream in;
    private ObjectOutputStream out;
    public List<Player> alreadyConnected;
    
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
     * Send a request to the server. Requires this is "open".
     * @param request request to send
     * @throws IOException if network or server failure
     */
    public void sendRequest(Player request) throws IOException {
        out.writeObject(request);
        out.flush(); // important! make sure x actually gets sent
    }
    
    /**
     * Get a reply from the next request that was submitted.
     * Requires this is "open".
     * @return square of requested number
     * @throws IOException if network or server failure
     */
    public Object getReply() throws IOException {
        Object reply = null;
		try {
			reply = in.readObject();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
        if (reply == null) {
            throw new IOException("connection terminated unexpectedly");
        }
        
        if (reply.equals("err")) {
            throw new IOException("server reported request error");
        }
        return reply;
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
    
    
    /**
     * Use a Client to open the GUI and communicate with the server.
     * @param args unused
     */
    public static void main(String[] args) {
        try (
                Client client = new Client("localhost", 4949);
        ) {
            String name = gui.startGUI();
            System.out.println("got the name " + name);
            
            	client.sendRequest(new Player(name, new Egg(), new Egg()));
//            
            while(true) {
//                // get the reply
            		try {
            			@SuppressWarnings("unchecked")
					List<Player> alreadyConnected = (List<Player>) client.getReply(); 
            			gui.players = alreadyConnected;
            			gui.waitingScreen();
            			System.out.println("Already connected: " + alreadyConnected);
            		} catch (ClassCastException e) {
            			System.err.println("data recieved from server was not of type List<Player>");
            		}
                
                
            }
            
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }
}
