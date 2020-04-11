package eggbonk.network;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import java.util.List;
import java.util.ArrayList;

import eggbonk.core.Game;
import eggbonk.core.GameState;
import eggbonk.core.Player;


public class Server {
    /** Default port number where the server listens for connections. */
    public static final int PORT = 1234;
    static final int NUM_PLAYERS = 3;
    static Game game;
    
    static List<Player> players = new ArrayList<Player>();
    
    private ServerSocket serverSocket;
    
    /**
     * Make a Server that listens for connections on port.
     * @param port port number, requires 0 <= port <= 65535
     * @throws IOException if there is an error listening on port
     */
    public Server(int port) throws IOException {
        serverSocket = new ServerSocket(port);
    }
    
    /**
     * Run the server, listening for connections and handling them.
     * @throws IOException if the main server socket is broken
     */
    public void serve() throws IOException {
        System.err.println("serving");

        while (true) {
            // block until a client connects
            final Socket clientSocket = serverSocket.accept();
            new Thread(new Runnable() {
                public void run() {
                    try {
                        handle(clientSocket);  
                    } catch (Exception ioe) {
                        ioe.printStackTrace(); // but don't terminate serve()
                    } finally {
                        try {
                            clientSocket.close();
                        }
                        catch (IOException ioe) {
                            ioe.printStackTrace(); // but don't terminate serve()
                        }
                    }
                }
            }).start();
            if (players.size() == NUM_PLAYERS)
                game = new Game((Player[])(players.toArray()));
        }
    }
    
    /**
     * Handle one client connection. Returns when client disconnects.
     * @param socket socket where client is connected
     * @throws IOException if connection encounters an error
     * @throws InterruptedException 
     */
    private static void handle(Socket socket) throws IOException, InterruptedException {
        System.err.println("client connected: " + socket);
        
        ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
        ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());

        try {
            Player player = (Player) in.readObject(); // exception here if client sends incorrect data type
            int currentPlayerNum = players.size();

            players.add(player);
            
            // wait until everyone has connected
            while (players.size() < NUM_PLAYERS) {  // TODO change this condition
            		if (currentPlayerNum != players.size()) {
            			out.writeUnshared(new GameState(GameState.Phase.SETUP, List.copyOf(players)));
            			out.flush();
            			currentPlayerNum = players.size();
            		}
                Thread.sleep(1000);
            }
            
            int currentIndex = 0;
            while (players.size() > 1) {
                // choose which players are bonking
                Player currentPlayer1 = players.get(currentIndex);
                Player currentPlayer2 = currentIndex + 1 < players.size() ? players.get(currentIndex + 1) : players.get(0);
                
                // choose winner and loser
                Player winner = (int) Math.random() == 0 ? currentPlayer1 : currentPlayer2;
                Player loser = winner == currentPlayer1 ? currentPlayer2 : currentPlayer1;

                // send state to client
                out.writeUnshared(new GameState(GameState.Phase.TRANSITION, List.copyOf(players), winner, loser));
                
                // change index and remove loser if they got out
                if (loser.isOut())
                    players.remove(players.indexOf(loser));
                else
                    currentIndex++;
                if (currentIndex >= players.size()) {
                    currentIndex = 0;
                }
            }
            
            out.writeUnshared(new GameState(GameState.Phase.TOTAL_VICTORY, List.copyOf(players)));
            
        } catch (ClassNotFoundException | ClassCastException e) {
        		System.err.println("input from client was not of the expected type"); // see comment about exception above
			e.printStackTrace();
		} finally {
            out.close();
            in.close();
        }
    }
    
    /**
     * Start a Server running on the default port.
     * @param args unused
     */
    public static void main(String[] args) {
        try {
            Server server = new Server(PORT);
            server.serve();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
