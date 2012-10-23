package taskmanager;

import java.io.*;
import javax.xml.bind.JAXBException;
import org.jgroups.*;
import org.jgroups.util.Util;
import serialization.*;

public class GroupManager extends ReceiverAdapter implements TaskManager {

    private JChannel channel = null;
    private final FileManager state = new FileManager();

    public GroupManager() throws Exception {
        this("TheHitmen");
    }

    public GroupManager(String clusterName) throws Exception {
        channel = new JChannel();
        channel.setReceiver(this);
        channel.setDiscardOwnMessages(true);
        channel.connect(clusterName);
        channel.getState(null, 10000);        
    }

    @Override
    public void getState(OutputStream output) throws JAXBException, Exception {
        synchronized (state) {
            String calXml = Cal.serialize(state.getCal());

            Util.objectToStream(calXml, new DataOutputStream(output));
        }
    }

    @Override
    public void setState(InputStream input) throws JAXBException, Exception {
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
            synchronized (state) {
                state.set(envelope);
            }
            System.out.println("LOCAL <- GROUP : [" + envelope.command + "] [" + envelope.data + "]");
        } catch (JAXBException ex) {
            System.out.println("EXCEPTION : " + ex.getMessage());
        }

    }

    @Override
    public String get(Envelope envelope) {
        return state.get(envelope);
    }

    @Override
    public void set(Envelope envelope) {
        synchronized (state) {
            state.set(envelope);
        }

        try {
            String message = Envelope.serialize(envelope);
            send(message);
            System.out.println("LOCAL -> GROUP : [" + envelope.command + "] [" + envelope.data + "]");
        } catch (Exception ex) {
            System.out.println("EXCEPTION : " + ex.getMessage());
        }
    }
}
