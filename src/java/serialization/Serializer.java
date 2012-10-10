package serialization;

import java.io.*;
import javax.xml.bind.*;

public class Serializer<T> {
    /**
     * Test method used to demonstrate how the generic serializer works with User, Task and Cal.
     * @param args Not used.
     */
    public static void main(String[] args) {
        serialization.common.User u = new serialization.common.User();
        u.name = "Oliver";
        u.password = "Password";        
        System.out.printf("name = %s, password = %s\n", u.name, u.password);
        
        Serializer<serialization.common.User> su = new Serializer<serialization.common.User>(u);
        
        String xml = su.serialize(u);
        System.out.println(xml);
        
        u = su.deSerialize(xml);
        System.out.printf("name = %s, password = %s\n", u.name, u.password);
        
        
        serialization.common.Task t = new serialization.common.Task();
        t.attendants = "Oliver";
        t.date = "10-10-2012";
        t.description = "No description";
        t.id = "test";
        t.name = "A test task.";
        t.status = "not-executed";
        System.out.printf("attendtands = %s, date = %s, description = %s, id = %s, name = %s, status = %s\n", t.attendants, t.date, t.description, t.id, t.name, t.status);
        
        Serializer<serialization.common.Task> st = new Serializer<serialization.common.Task>(t);
        
        xml = st.serialize(t);
        System.out.println(xml);
        
        t = st.deSerialize(xml);
        System.out.printf("attendtands = %s, date = %s, description = %s, id = %s, name = %s, status = %s\n", t.attendants, t.date, t.description, t.id, t.name, t.status);
        
        
        
        serialization.common.Cal c = new serialization.common.Cal();
        java.util.List<serialization.common.Task> lt = new java.util.ArrayList<serialization.common.Task>();
        lt.add(t);        
        java.util.List<serialization.common.User> lu = new java.util.ArrayList<serialization.common.User>();
        lu.add(u);
        c.tasks = lt;
        c.users = lu;
        System.out.printf("tasks = %d, users = %d", c.tasks.size(), c.users.size());        
        
        Serializer<serialization.common.Cal> sc = new Serializer<serialization.common.Cal>(c);
        
        xml = sc.serialize(c);
        System.out.println(xml);
        
        c = sc.deSerialize(xml);
        System.out.printf("tasks = %d, users = %d", c.tasks.size(), c.users.size());        
    }    
    

    private final Class<? extends Object> genericClass;   
    
    /**
     * Get the generic serializer ready.
     * 
     * @param classSupplier Any object of type T. Used for get the Class for future use.
     */
    public Serializer(T classSupplier) {
        genericClass = classSupplier.getClass();
    }
    
    /**
     * Serializes an object of type T into XML contained in a string.
     * @param object The object of type T.
     * @return The string representation of the serialized object.
     */
    public String serialize(T object) {
        String result = null;
        
        try {
            // Create an instance context class, to serialize.
            JAXBContext context = JAXBContext.newInstance(genericClass);

            StringWriter writer = new StringWriter();

            // Serialize generic object into XML.
            context.createMarshaller().marshal(object, writer);
            result = writer.toString();
        } catch (JAXBException e) {
            System.out.println("JAXB: " + e.getMessage());
        }
        
        return result;
    }
    
    /**
     * Deserializes a string containing XML into an object of type T.
     * @param xml The string containing XML.
     * @return The object of type T.
     */
    public T deSerialize(String xml) {
        T result = null;
                
        try {
            // Create an instance context class, to serialize.
            JAXBContext context = JAXBContext.newInstance(genericClass);
            
            result = (T) context.createUnmarshaller().unmarshal(new StringReader(xml));
        } catch (JAXBException e) {
            System.out.println("JAXB: " + e.getMessage());
        }
        
        return result;
    }
    
    /**
     * Deserializes a stream containing XML into an object of type T.
     * @param fsi The stream containing XML.
     * @return The object of type T.
     */
    public T deSerialize(FileInputStream fsi) {
        T result = null;
                
        try {
            // Create an instance context class, to serialize.
            JAXBContext context = JAXBContext.newInstance(genericClass);
            
            result = (T) context.createUnmarshaller().unmarshal(fsi);
        } catch (JAXBException e) {
            System.out.println("JAXB: " + e.getMessage());
        }
        
        return result;
    }    
}
