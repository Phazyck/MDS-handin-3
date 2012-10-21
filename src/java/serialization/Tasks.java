package serialization;

import java.io.*;
import java.util.*;
import javax.xml.bind.*;
import javax.xml.bind.annotation.*;

@XmlRootElement
public class Tasks implements Serializable {

    public Tasks() {
        this(new ArrayList<Task>());
    }

    public Tasks(List<Task> tasks) {
        this.task = tasks;
    }
    @XmlElement
    public List<Task> task;

    public static Tasks deSerialize(String xml) throws JAXBException {
        JAXBContext context = JAXBContext.newInstance(Tasks.class);
        return (Tasks) context.createUnmarshaller().unmarshal(new StringReader(xml));
    }

    public static String serialize(Tasks object) throws JAXBException {
        JAXBContext context = JAXBContext.newInstance(Tasks.class);
        StringWriter writer = new StringWriter();
        context.createMarshaller().marshal(object, writer);
        return writer.toString();
    }
}