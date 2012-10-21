package tcp;

import java.io.*;
import java.net.*;

public class Client extends Actor {

    /**
     * Initialize the Client with the host name "localhost".
     *
     * @throws UnknownHostException
     * @throws IOException
     */
    public Client() throws UnknownHostException, IOException {
        this("localhost");
    }

    /**
     * Initialize the Client with a custom host name.
     *
     * @param hostName The custom host name.
     * @throws UnknownHostException
     * @throws IOException
     */
    public Client(String hostName) throws UnknownHostException, IOException {
        super(new Socket(InetAddress.getByName(hostName), 7896));
    }

    /**
     * Sends a request to the Server.
     *
     * @param command The command which defines how you want to interact with
     * the server.
     * @param data The data which is needed to execute the command.
     * @return The result of the interaction.
     * @throws IOException
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