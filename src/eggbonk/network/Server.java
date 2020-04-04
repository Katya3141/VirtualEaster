package eggbonk.network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

import java.util.List;
import java.util.ArrayList;

import eggbonk.core.Egg;
import eggbonk.core.Game;
import eggbonk.core.Player;


public class Server {
    /** Default port number where the server listens for connections. */
    public static final int PORT = 4949;
    static final int NUM_PLAYERS = 8;
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
                    } catch (IOException ioe) {
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
     */
    private static void handle(Socket socket) throws IOException {
        System.err.println("client connected: " + socket);
        
        // get the socket's input stream, and wrap converters around it
        // that convert it from a byte stream to a character stream,
        // and that buffer it so that we can read a line at a time
        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
        
        // similarly, wrap character=>bytestream converter around the
        // socket output stream, and wrap a PrintWriter around that so
        // that we have more convenient ways to write Java primitive
        // types to it.
        PrintWriter out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8));

        try {
            // get the client's name
            String name = in.readLine();
            players.add(new Player(name, new Egg(), new Egg()));
            out.println(players);
            out.flush();
            // wait until everyone has connected
            while (players.size() < NUM_PLAYERS) {}

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
