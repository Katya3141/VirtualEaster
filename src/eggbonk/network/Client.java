package eggbonk.network;

import static java.nio.charset.StandardCharsets.UTF_8;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

import eggbonk.core.gui;

public class Client implements AutoCloseable {
    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;
    
    /**
     * Make a Client and connect it to a server running on
     * hostname at the specified port.
     * @throws IOException if can't connect
     */
    public Client(String hostname, int port) throws IOException {
        socket = new Socket(hostname, port);
        in = new BufferedReader(new InputStreamReader(socket.getInputStream(), UTF_8));
        out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(), UTF_8));
    }
    
    /**
     * Send a request to the server. Requires this is "open".
     * @param request request to send
     * @throws IOException if network or server failure
     */
    public void sendRequest(String request) throws IOException {
        out.print(request + "\n");
        out.flush(); // important! make sure x actually gets sent
    }
    
    /**
     * Get a reply from the next request that was submitted.
     * Requires this is "open".
     * @return square of requested number
     * @throws IOException if network or server failure
     */
    public String getReply() throws IOException {
        String reply = in.readLine();
        if (reply == null) {
            throw new IOException("connection terminated unexpectedly");
        }
        
        if (reply.equals("err")) {
            throw new IOException("server reported request error");
        }
        
        try {
            return (String) reply;
        } catch (Exception e) {
            throw new IOException("misformatted reply: " + reply);
        }
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
                Client client = new Client("localhost", Server.PORT);
                BufferedReader systemIn = new BufferedReader(new InputStreamReader(System.in));
        ) {
            gui.startGUI();
            
//            System.out.println("Hello! Welcome to Virtual Easter 2020! What's your name?");
//            
//            final String input = systemIn.readLine();
//            
//            // send the request
//            client.sendRequest(input);
//            
//            while(true) {
//                // get the reply
//                String alreadyConnected = client.getReply();
//                System.out.println("Already connected: " + alreadyConnected);
//            }
            
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }
}
