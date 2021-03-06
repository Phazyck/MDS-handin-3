package tcp;

import java.io.*;
import java.net.*;

public class Client extends Actor {

    public static void main(String[] args) throws Exception {
        String postData = serialization.Task.serialize(new serialization.Task("test", "Update Me", "22-10-2012", "not-executed", "Update this task using update.", "TheHitmen"));
        String putData = serialization.Task.serialize(new serialization.Task("test", "Delete Me", "22-10-2012", "executed", "Delete this task using delete.", "TheHitmen"));
        String saveData = serialization.Task.serialize(new serialization.Task("test", "Save me", "22-10-2012", "executed", "This task should be saved to .xml-file.", "TheHitmen"));
        String getData = "TheHitmen";
        String deleteData = "test";
        
        Client client = new Client();
        
        client.printRequest("GET", getData);
        client.printRequest("DELETE", deleteData);
        client.printRequest("GET", getData);
        client.printRequest("POST", postData);
        client.printRequest("GET", getData);
        client.printRequest("PUT", putData);
        client.printRequest("GET", getData);
        client.printRequest("DELETE", deleteData);
        client.printRequest("GET", getData);
        client.printRequest("POST", saveData);
        client.printRequest("GET", getData);
    }    
    
    /**
     * Initialize the Client with the host name "localhost".
     */
    public Client() throws UnknownHostException, IOException {
        this("localhost");
    }

    /**
     * Initialize the Client with a custom host name.
     *
     * @param hostName The custom host name.
     */
    public Client(String hostName) throws UnknownHostException, IOException {
        super(new Socket(InetAddress.getByName(hostName), 7896));
    }
    
    public void printRequest(String command, String data) throws IOException {
        System.out.printf("Request: [%s] [%s]\n", command, data);
        System.out.printf("Reply  : [%s] \n\n", sendRequest(command, data));        
    }

    /**
     * Sends a request to the Server.
     *
     * @param command The command which defines how you want to interact with
     * the server.
     * @param data The data which is needed to execute the command.
     * @return The result of the interaction.
     */
    public String sendRequest(String command, String data) throws IOException {
        
        // Send the command to the Server.
        sendString(command);
        // Receive the echo from the Server.
        String echo = receiveString();

        // If the echo doesn't match the original command, something went wrong.
        if (!echo.equals(command)) {
            return echo;
        }

        // Send the data to the Server.
        sendString(data);
        // Receive and return the result.
        return receiveString();
    }
}