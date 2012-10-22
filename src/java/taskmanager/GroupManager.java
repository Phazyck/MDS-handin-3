package taskmanager;

import java.io.*;
import org.jgroups.JChannel;
import org.jgroups.Message;
import org.jgroups.ReceiverAdapter;
import org.jgroups.util.Util;
import serialization.*;

public class GroupManager extends ReceiverAdapter implements ITaskManager {
    
    private static void print(String msg) {
        System.out.println("GRPMGR: " + msg);
    }

    private JChannel channel = null;
    private final TaskManager state = new TaskManager();

    public GroupManager() throws Exception {
        this("TheHitmen");
    }

    public GroupManager(String clusterName) throws Exception {
        try {
            channel = new JChannel();
            channel.setReceiver(this);
            channel.setDiscardOwnMessages(true); // in 3.0, previously use 
            //channel.setOpt(Channel.LOCAL, false);   
            channel.connect(clusterName);
            channel.getState(null, 10000);

        } catch (Exception e) {
            if (channel != null) {
                channel.close();
            }
            throw e;
        }
    }

    @Override
    public void getState(OutputStream output) throws Exception {
        synchronized (state) {
            String calXml = Cal.serialize(state.getCal());

            Util.objectToStream(calXml, new DataOutputStream(output));
        }
    }

    @Override
    public void setState(InputStream input) throws Exception {
        String calXml = (String) Util.objectFromStream(new DataInputStream(input));
        synchronized (state) {
            state.setCal(Cal.deSerialize(calXml));
        }
    }

    public void send(String message) throws Exception {
        Message msg = new Message(null, null, message);
        channel.send(msg);
    }

    @Override
    public void receive(Message msg) {        
        try {
            String xmlEnvelope = msg.getObject().toString();
            
            Envelope envelope = Envelope.deSerialize(xmlEnvelope);

            print("DATA RECEIVED: " + envelope.data);

            synchronized (state) {
                state.set(envelope);
            }
        } catch (Exception e) {
            print(e.getMessage());
        }
    }

    @Override
    public String get(Envelope envelope) {
        return state.get(envelope);
    }

    @Override
    public void set(Envelope envelope) {
        try {
            state.set(envelope);
            print("DATA SENT: " + envelope.data);            
            String message = Envelope.serialize(envelope);
            send(message);
        } catch (Exception e) {
            print(e.getMessage());
        }
    }
}
