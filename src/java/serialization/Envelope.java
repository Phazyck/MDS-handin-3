package serialization;

import java.io.*;
import javax.xml.bind.*;
import javax.xml.bind.annotation.*;

@XmlRootElement
public class Envelope implements Serializable {

    public Envelope() {
        this(null, null);
    }

    public Envelope(String command, String data) {
        this.command = command;
        this.data = data;
    }
    @XmlElement
    public String command;
    @XmlElement
    public String data;

    public static Envelope deSerialize(String xml) throws JAXBException {
        JAXBContext context = JAXBContext.newInstance(Envelope.class);
        return (Envelope) context.createUnmarshaller().unmarshal(new StringReader(xml));
    }

    public static String serialize(Envelope object) throws JAXBException {
        JAXBContext context = JAXBContext.newInstance(Envelope.class);
        StringWriter writer = new StringWriter();
        context.createMarshaller().marshal(object, writer);
        return writer.toString();
    }
}