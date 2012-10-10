package jgroup;

import java.io.*;
import java.util.*;
import org.jgroups.*;
import org.jgroups.util.*;

public class JGroupClient extends ReceiverAdapter {

    private JChannel channel = null;
    final List<String> state = new LinkedList<String>();

    @Override
    public void getState(OutputStream output) throws Exception {
        synchronized (state) {
            Util.objectToStream(state, new DataOutputStream(output));
        }
    }

    @Override
    public void setState(InputStream input) throws Exception {
        List<String> list = (List<String>) Util.objectFromStream(new DataInputStream(input));
        synchronized (state) {
            state.clear();
            state.addAll(list);
        }
        System.out.println("received state (" + list.size() + " messages in chat history):");
        for (String str : list) {
            System.out.println(str);
        }
    }

    /**
     * Constructs the client and connects to the cluster.
     * @param clusterName The name of the cluster.
     */
    public JGroupClient(String clusterName) {
        try {
            channel = new JChannel();
            channel.setReceiver(this);
            channel.connect(clusterName);
            channel.getState(null, 10000);
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            if(channel != null) {
                channel.close();
            }
        }
    }

    /**
     * Send a message.
     * 
     * @param message The message to be sent.
     */
    public void send(String message) {
        Message msg = new Message(null, null, message);
        try {
            channel.send(msg);
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
    
    @Override
    public void receive(Message msg) {
        String line = msg.getObject().toString();
        
        synchronized (state) {
            state.add(line);
        }
    }    
}
