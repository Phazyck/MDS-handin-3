package tcp;

import java.io.*;
import java.net.*;
import java.util.*;
import serialization.Serializer;
import serialization.common.Task;

public class TcpClient {

    private static Socket socket;
    
    /**
     * Test the server with this client's main method.
     * 
     * @param args Not used.
     */
    public static void main(String args[]) {
        try {
            // Make a new socket.
            socket = new Socket(InetAddress.getByName("localhost"), 7896);

            // Get the request from the user.
            String request = getInput("Please enter either 'POST', 'PUT', 'GET' or 'DELETE'.", new String[]{"POST", "PUT", "GET", "DELETE"});

            // Send the command to the server.                        
            sendString(request);

            // Receive the reply.
            String response = receiveString();

            if (!response.equals(request)) {
                System.out.println("FAILURE: Wrong reply received!");
                System.exit(0);
            }

            // Supply further data to server.
            if (request.equals("POST")) {
                Task t;
                t = new Task();
                t.id = getInput("Please enter a task id.", null);
                t.name = getInput("Please enter a task name.", null);
                t.date = getInput("Please enter a task date.", null);
                t.status = getInput("Please enter a task status.", null);
                t.description = getInput("Please enter a task description.", null);
                t.attendants = getInput("Please enter the task attendants seperated with ', '.", null);

                sendObject((Object) t);
                System.out.println("Message received: " + receiveString());
            } else if (request.equals("PUT")) {
                Task t = new Task();
                t.id = getInput("Please enter a task id.", null);
                t.name = getInput("Please enter a task name.", null);
                t.date = getInput("Please enter a task date.", null);
                t.status = getInput("Please enter a task status.", null);
                t.description = getInput("Please enter a task description.", null);
                t.attendants = getInput("Please enter the task attendants seperated with ', '.", null);

                sendObject(t);
                System.out.println("Message received: " + receiveString());
            } else if (request.equals("GET")) {
                sendString(getInput("Please enter an attendant.", null));
                List<Task> tasks = receiveTasks();
                
                System.out.println("Printing received tasks");
                for(Task t : tasks) {
                    Serializer<Task> s = new Serializer<Task>(t);
                    System.out.println(s.serialize(t));
                }
            } else if (request.equals("DELETE")) {
                sendString(getInput("Please enter a task id.", null));
                System.out.println("Message received: " + receiveString());
            }
        } catch (IOException e) {
            System.out.println("IO: " + e.getMessage());
        } finally {
            if(socket != null) {
                try {
                    socket.close();
                } catch (IOException e) {
                    System.out.println("IO: " + e.getMessage());
                }
            }
        }
    }    
    
    /**
     * Receive user input from terminal.
     *
     * @param summary A summary of what you expect the user to enter.
     * @param legal An array of legal input. If null, any input is legal.
     * @return The entered input.
     */
    public static String getInput(String summary, String[] legal) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        System.out.println(summary);
        System.out.print("> ");
        String input = in.readLine();
        if (legal == null) {
            return input;
        }
        for (String s : legal) {
            if (s.equals(input)) {
                return input;
            }
        }
        return getInput(summary, legal);
    }

    /**
     * Send a string to the server.
     * 
     * @param s The string.
     * @throws IOException 
     */
    private static void sendString(String s) throws IOException {
        System.out.printf("Sending message \"%s\" to Server.\n", s);        
        
        // Get data output stream to send a String message to server.
        DataOutputStream dos = new DataOutputStream(socket.getOutputStream());

        dos.writeUTF(s);

        dos.flush();
    }

    /**
     * Send an object to the server.
     * 
     * @param o The object.
     * @throws IOException 
     */
    private static void sendObject(Object o) throws IOException {
        System.out.println("Sending object to Server.");
        
        // Get data output stream to send a String message to server.
        ObjectOutputStream dos = new ObjectOutputStream(socket.getOutputStream());

        dos.writeObject(o);

        dos.flush();
    }

    /**
     * Receive a string from the server.
     * @return The string received from the server.
     * @throws IOException 
     */
    private static String receiveString() throws IOException {
        System.out.println("Receiving message from server.");
        
        // Now switch to listening mode for receiving message from server.
        DataInputStream dis = new DataInputStream(socket.getInputStream());        

        // Note that this is a blocking call,  
        return dis.readUTF();
    }

    /**
     * Receive a list of tasks from the server.
     * 
     * @return The list of tasks received from the server.
     * @throws IOException 
     */
    private static List<Task> receiveTasks() throws IOException {
        System.out.println("Receiving tasks from server.");
        
        List<Task> result = null;
        
        try {
            // Now switch to listening mode for receiving message from server.
            ObjectInputStream dis = new ObjectInputStream(socket.getInputStream());

            result = (List<Task>) dis.readObject();
        } catch (ClassNotFoundException e) {
            System.out.println("Class not found: " + e.getMessage());
        } 
        
        return result;
    }
}