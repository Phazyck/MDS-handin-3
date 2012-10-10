package serialization.common;

import java.util.*;
import javax.xml.bind.annotation.*;

@XmlRootElement(name = "cal")
public class Cal {
    
    @XmlElementWrapper(name = "users")
    @XmlElement(name = "user")
    public List<User> users;
    
    @XmlElementWrapper(name = "tasks")
    @XmlElement(name = "task")
    public List<Task> tasks;
    
}
