package serialization.common;

import javax.xml.bind.annotation.*;

@XmlRootElement(name = "user")
public class User {
    public String name;
    public String password;
}
