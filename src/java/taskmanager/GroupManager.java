package taskmanager;

import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.bind.JAXBException;
import org.jgroups.JChannel;
import org.jgroups.Message;
import org.jgroups.ReceiverAdapter;
import org.jgroups.util.Util;
import serialization.*;

public class GroupManager extends ReceiverAdapter implements ITaskManager {

    private JChannel channel = null;
    private final TaskManager state = new TaskManager();

    public GroupManager(String clusterName) throws Exception {
        try {
            channel = new JChannel();
            channel.setReceiver(this);
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

            synchronized (state) {
                alter(envelope);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public String fetch(Envelope envelope) {
        return state.fetch(envelope);
    }

    @Override
    public void alter(Envelope envelope) {
        try {
            String message = Envelope.serialize(envelope);
            send(message);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
