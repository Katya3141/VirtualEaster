package eggbonk.network;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import java.util.List;
import java.util.Scanner;
import java.util.ArrayList;

import eggbonk.core.Game;
import eggbonk.core.GameState;
import eggbonk.core.Player;

import static eggbonk.core.GameState.Phase.*;


public class Server {
	/** Default port number where the server listens for connections. */
	public static final int PORT = 1234;
	static final int NUM_PLAYERS = 5;
	static Game game;

	private static boolean waitingForPlayers = true;

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

		new Thread(() -> {
			System.out.println("Type \"ready\" when everyone's in, or \"l\" to list the current players: ");
			Scanner console = new Scanner(System.in);
			String l;
			while(!(l = console.nextLine()).equals("ready")) {
				if(l.equals("l")) {
					System.out.println(players + " (" + players.size() + " players total)");
				}
			}
			console.close();
			waitingForPlayers = false;
			game = new Game(players);
			runGame();
		}).start();

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
		}
	}

	private static int readyToBonk;

	private static GameState gameState;

	private static void runGame() {
		
		System.err.println("the game has begun!");
		// loop until list is empty
		while(players.size() > 1) {
			
			System.err.println("internally bonking...");
			//internally bonk
			Player winner = game.nextTurn();
			Player loser = game.getLoser();
	
			// before transition, reset ready-to-bonk
			readyToBonk = 0;
	
			System.err.println("sending clients transition state...");
			// send the clients the transition state
			gameState = new GameState(TRANSITION, players, winner, loser);
	
			System.err.println("waiting for bonkers to be ready...");
			// wait until the two bonkers-to-be are ready
			while(readyToBonk < 2) {
				try {
					Thread.sleep(10);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			
			System.err.println("removing players who are out...");
			// remove potential out player from the list
			game.removeExtraneousPlayers();
	
			System.err.println("sending clients bonking state...");
			// send the client an updated state saying you can animate now
			gameState = new GameState(BONKING, players, winner, loser);
			
			System.err.println("waiting for animations...");
			// wait for animation
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		// end loop
		}
		System.err.println("game is over!");
		gameState = new GameState(TOTAL_VICTORY, players);
	
	}

	/**
	 * Handle one client connection. Returns when client disconnects.
	 * @param socket socket where client is connected
	 * @throws IOException if connection encounters an error
	 * @throws InterruptedException 
	 */
	private static void handle(Socket socket) throws IOException, InterruptedException {
		System.err.println("client connected: " + socket);



		try (ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
				ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());) {

			Player player = (Player) in.readObject(); // exception here if client sends incorrect data type
			int currentPlayerNum = players.size();

			players.add(player);

			// wait until everyone has connected
			while (waitingForPlayers) {  
				if (currentPlayerNum != players.size()) {
					out.writeUnshared(new GameState(GameState.Phase.SETUP, List.copyOf(players)));
					out.flush();
					currentPlayerNum = players.size();
				}
				Thread.sleep(1000);
			}


			System.out.println("starting game loop...");
			// loop while game is not over
			while(gameState.getPhase() != TOTAL_VICTORY) {

				System.out.println("waiting for transition phase...");
				// wait until transition phase
				while(gameState.getPhase() != TRANSITION) {Thread.sleep(10);}
	
				System.out.println("sending the client the transition state");
				// send the client the transition state
				out.writeUnshared(gameState);
	
				System.out.println("waiting for client to respond...");
				// notify if client sends ready message
				if(in.readObject().equals("ready")) readyToBonk++;
	
				System.out.println("waiting for all other clients to respond...");
				// wait until all ready messages have been sent
				while(gameState.getPhase() == TRANSITION) {Thread.sleep(10);}
	
				System.out.println("sending bonking state to client...");
				// phase is now BONKING, send the new state
				out.writeUnshared(gameState);
				
				System.out.println("waiting for the game state to change...");
				// wait for the game state to change
				while(gameState.getPhase() == BONKING) {Thread.sleep(10);}

			// end loop
			}
			
			// game is over
			out.writeUnshared(gameState);


		} catch (ClassNotFoundException | ClassCastException e) {
			System.err.println("input from client was not of the expected type"); // see comment about exception above
			e.printStackTrace();
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