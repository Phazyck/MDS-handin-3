package serialization;

import java.io.*;
import javax.xml.bind.*;
import javax.xml.bind.annotation.*;

@XmlRootElement
public class User implements Serializable {

    public User() {
        this(null, null, null);
    }

    public User(String id, String name, String password) {
        this.id = id;
        this.name = name;
        this.password = password;
    }
    @XmlAttribute
    public String id;
    @XmlElement
    public String name;
    @XmlElement
    public String password;

    public static User deSerialize(String xml) throws JAXBException {
        JAXBContext context = JAXBContext.newInstance(User.class);
        return (User) context.createUnmarshaller().unmarshal(new StringReader(xml));
    }

    public static String serialize(User object) throws JAXBException {
        JAXBContext context = JAXBContext.newInstance(User.class);
        StringWriter writer = new StringWriter();
        context.createMarshaller().marshal(object, writer);
        return writer.toString();
    }
}