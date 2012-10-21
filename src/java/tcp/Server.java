package tcp;

import java.io.*;
import java.net.*;
import serialization.*;
import taskmanager.*;

public class Server extends Actor {

    private ITaskManager taskManager;

    public Server() throws Exception {
        this("TaskManagerGroup");
    }

    public Server(String clusterName) throws Exception {
        super((new ServerSocket(7896)).accept());
        this.taskManager = new GroupManager(clusterName);
    }

    // Receive the next request.
    public void handleRequest() throws IOException {
        // Receive a command.
        String command = receiveString();

        // Send back the appropriate echo to the client.
        String echo = "Exception: Command " + command + " not supported.";
        for(String s : new String[] {"POST", "PUT", "GET", "DELETE"}) {
            if(command.equalsIgnoreCase(s)) {
                echo = s;
            }
        }
        sendString(echo);
                
        // If the command doesn't equal the echo, it wasn't supported, and the request is ignored.
        if(!command.equalsIgnoreCase(echo)) {
            return;
        }
        
        // Else, we can continue to get the data and put it in an envelope.
        String data = receiveString();
        String result = "Success!";
        try {
            Envelope envelope = new Envelope(command, data);
            if(command.equalsIgnoreCase("GET")) {
                result = taskManager.fetch(envelope);
            } else {
                taskManager.alter(envelope);
            }
        } catch (Exception e) {
            result = "Exception: " + e.getMessage();
        } finally {
            sendString(result);
        }        
    }    
}
