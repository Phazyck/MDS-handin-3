package serialization.common;

import java.io.*;
import javax.xml.bind.annotation.*;

@XmlRootElement(name = "task")
public class Task implements Serializable {
    @XmlAttribute
    public String status;
    
    @XmlAttribute
    public String date;
    
    @XmlAttribute
    public String name;
    
    @XmlAttribute
    public String id;
    
    public String description;
    
    public String attendants;
}
