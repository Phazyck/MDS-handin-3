package serialization;

import java.io.*;
import java.util.*;
import javax.xml.bind.*;
import javax.xml.bind.annotation.*;

@XmlRootElement
public class Cal implements Serializable {

    public Cal() {
        this(new ArrayList<User>(), new ArrayList<Task>());
    }

    public Cal(List<User> users, List<Task> tasks) {
        this.users = users;
        this.tasks = tasks;
    }
    @XmlElementWrapper(name = "users")
    @XmlElement(name = "user")
    public List<User> users;
    @XmlElementWrapper(name = "tasks")
    @XmlElement(name = "task")
    public List<Task> tasks;

    public static Cal deSerialize(String xml) throws JAXBException {
        JAXBContext context = JAXBContext.newInstance(Cal.class);
        return (Cal) context.createUnmarshaller().unmarshal(new StringReader(xml));
    }

    public static String serialize(Cal object) throws JAXBException {
        JAXBContext context = JAXBContext.newInstance(Cal.class);
        StringWriter writer = new StringWriter();
        context.createMarshaller().marshal(object, writer);
        return writer.toString();
    }
}