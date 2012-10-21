package serialization;

import java.io.*;
import javax.xml.bind.*;
import javax.xml.bind.annotation.*;

@XmlRootElement
public class Task implements Serializable {

    public Task() {
        this(null, null, null, null, null, null);
    }

    public Task(String id, String name, String date, String status, String description, String attendants) {
        this.id = id;
        this.name = name;
        this.date = date;
        this.status = status;
        this.description = description;
        this.attendants = attendants;
    }
    @XmlAttribute
    public String id;
    @XmlAttribute
    public String name;
    @XmlAttribute
    public String date;
    @XmlAttribute
    public String status;
    @XmlElement
    public String description;
    @XmlElement
    public String attendants;

    public static Task deSerialize(String xml) throws JAXBException {
        JAXBContext context = JAXBContext.newInstance(Task.class);
        return (Task) context.createUnmarshaller().unmarshal(new StringReader(xml));
    }

    public static String serialize(Task object) throws JAXBException {
        JAXBContext context = JAXBContext.newInstance(Task.class);
        StringWriter writer = new StringWriter();
        context.createMarshaller().marshal(object, writer);
        return writer.toString();
    }
}