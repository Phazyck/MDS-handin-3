package tcp;

import java.io.*;
import java.net.*;

public abstract class Actor {

    private InputStream i;
    private OutputStream o;

    /**
     * Initialize the Actor on the given socket.
     *
     * @param socket The socket on which the Actor will interact.
     * @throws IOException
     */
    public Actor(Socket socket) throws IOException {
        this.i = socket.getInputStream();
        this.o = socket.getOutputStream();
    }

    /**
     * Receive a String from the other side.
     *
     * @return The received String.
     * @throws IOException
     */
    protected String receiveString() throws IOException {
        DataInputStream in = new DataInputStream(i);
        return in.readUTF();
    }

    /**
     * Send a String to the other side.
     *
     * @param string The String which should be sent.
     * @throws IOException
     */
    protected void sendString(String string) throws IOException {
        DataOutputStream out = new DataOutputStream(o);
        out.writeUTF(string);
        out.flush();
    }
}