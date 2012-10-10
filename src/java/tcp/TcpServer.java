package tcp;

import java.io.*;
import java.net.*;
import java.util.*;
import serialization.Serializer;
import serialization.common.*;

public class TcpServer {

    private static Socket socket;

    public static void main(String args[]) {
        try {            
            Cal cal = new Cal();
            cal.tasks = new ArrayList<Task>();
            cal.users = new ArrayList<User>();
            
            Serializer<Cal> s = new Serializer<Cal>(cal);
            
            cal = s.deSerialize(new FileInputStream("./web/WEB-INF/task-manager-xml.xml"));

            // Establish connection.
            socket = getSocket();

            // Receive the request string.
            String request = receiveString();

            // Confirm request.
            sendString(request);

            if (request.equals("POST")) {
                Task task = (Task) receiveObject();
                cal.tasks.add(task);

                sendString("Task posted.");
            } else if (request.equals("PUT")) {
                Task task = (Task) receiveObject();

                boolean updated = false;
                for (Task t : cal.tasks) {
                    if (t.id.equals(task.id)) {
                        cal.tasks.remove(t);
                        cal.tasks.add(task);
                        updated = true;
                        break;
                    }
                }
                if (updated) {
                    sendString("Task updated.");
                } else {
                    sendString("No task with matching id.");
                }
            } else if (request.equals("GET")) {
                String attendant = receiveString();
                List<Task> result = new ArrayList<Task>();
                for (Task t : cal.tasks) {
                    if (t.attendants.contains(attendant)) {
                        result.add(t);
                    }
                }

                sendObject(result);
            } else if (request.equals("DELETE")) {
                String task = receiveString();

                Task removeThis = null;
                for (Task t : cal.tasks) {
                    if (t.id.equals(task)) {
                        removeThis = t;
                        break;
                    }
                }

                if (removeThis == null) {
                    sendString("No task with matching id.");
                } else {
                    cal.tasks.remove(removeThis);
                    sendString("Task deleted.");
                }
            }

            socket.close();
        } catch (FileNotFoundException e) {
            System.out.println("File not found: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("IO: " + e.getMessage());
        } catch (ClassNotFoundException e) {
            System.out.println("Clas not found: " + e.getMessage());
        }
    }

    private static Socket getSocket() throws IOException {
        int serverPort = 7896;
        // create a server socket listening at port 7896
        ServerSocket serverSocket = new ServerSocket(serverPort);
        System.out.println("Server started at 7896");
        // Server starts accepting requests.
        // This is blocking call, and it wont return, until there is request from a client.
        return serverSocket.accept();
    }

    private static String receiveString() throws IOException {
        // Get the inputstream to receive data sent by client. 
        InputStream is = socket.getInputStream();

        // based on the type of data we want to read, we will open suitbale input stream.  
        DataInputStream dis = new DataInputStream(is);

        // Read the String data sent by client at once using readUTF,
        // Note that read calls also blocking and wont return until we have some data sent by client. 
        String message = dis.readUTF(); // blocking call

        // Print the message.
        System.out.println("Message from Client: " + message);

        return message;
    }

    private static Object receiveObject() throws IOException, ClassNotFoundException {
        // Get the inputstream to receive data sent by client. 
        InputStream is = socket.getInputStream();

        // based on the type of data we want to read, we will open suitbale input stream.  
        ObjectInputStream dis = new ObjectInputStream(is);

        // Read the String data sent by client at once using readUTF,
        // Note that read calls also blocking and wont return until we have some data sent by client. 
        return dis.readObject(); // blocking call
    }

    private static void sendString(String s) throws IOException {
        // Now the server switches to output mode delivering some message to client.
        DataOutputStream outputStream = new DataOutputStream(socket.getOutputStream());

        outputStream.writeUTF(s);

        System.out.println("Message to Client: " + s);

        outputStream.flush();
    }

    private static void sendObject(Object o) throws IOException {
        // Now the server switches to output mode delivering some message to client.
        ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());

        outputStream.writeObject(o);

        System.out.println("Sending tasks to Client.");

        outputStream.flush();
    }
}