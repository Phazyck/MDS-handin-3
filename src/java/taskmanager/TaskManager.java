package taskmanager;

import exceptions.UnsupportedCommandException;
import java.io.*;
import java.util.*;
import javax.xml.bind.*;
import serialization.*;

public class TaskManager implements ITaskManager {

    public static void main(String[] args) throws Exception {                
        String postData = Task.serialize(new Task("test", "Update Me", "22-10-2012", "not-executed", "Update this task using update.", "TheHitmen"));
        String putData = Task.serialize(new Task("test", "Delete Me", "22-10-2012", "executed", "Delete this task using delete.", "TheHitmen"));
        String getData = "TheHitmen";
        String deleteData = "test";
        
        Envelope postEnvelope = new Envelope("POST", postData);
        Envelope putEnvelope = new Envelope("PUT", putData);
        Envelope getEnvelope = new Envelope("GET", getData);
        Envelope deleteEnvelope = new Envelope("DELETE", deleteData);       
        
        TaskManager tm = new TaskManager();
        print(Cal.serialize(tm.getCal()));        
        tm.set(postEnvelope);
        
        tm = new TaskManager();
        print(tm.get(getEnvelope));
        
        print(Cal.serialize(tm.getCal()));        
        
        tm.set(putEnvelope);
        print(tm.get(getEnvelope));
        tm.set(deleteEnvelope);
        print(tm.get(getEnvelope));
    }       
    
    public static void print(String msg) {
        System.out.println("TSKMGR: " + msg);
    }
    
    private Cal cal;
    private String path;

    public TaskManager() {
        this("./web/WEB-INF/task-manager-xml.xml");
    }

    public TaskManager(String path) {
        this.path = path;
        try {
            JAXBContext context = JAXBContext.newInstance(Cal.class);
            FileInputStream stream = new FileInputStream(path);
            cal = (Cal) context.createUnmarshaller().unmarshal(stream);
        } catch (Exception e) {
            e.printStackTrace();
            cal = new Cal();
        }
    }

    public Cal getCal() {
        return cal;
    }

    public void setCal(Cal cal) {
        this.cal = cal;
        saveToFile();
    }

    private void post(String taskXml) {
        try {
            Task task = Task.deSerialize(taskXml);
            cal.tasks.add(task);
        } catch (Exception e) {
            print(e.getMessage());
        }
    }

    private void put(String taskXml) {        
        try {
            Task task = Task.deSerialize(taskXml);
            delete(task.id);
            post(taskXml);
        } catch (Exception e) {
            e.printStackTrace(System.out);
        }
    }

    private String get(String attendantId) throws Exception {
        List<Task> matches = new ArrayList<Task>();

        for (Task t : cal.tasks) {
            if (t.attendants.contains(attendantId)) {
                matches.add(t);
            }
        }

        return Tasks.serialize(new Tasks(matches));        
    }

    private void delete(String taskId) {
        List<Task> matches = new ArrayList<Task>();

        for (Task t : cal.tasks) {
            if (t.id.equals(taskId)) {
                matches.add(t);
            }
        }

        for (Task t : matches) {
            cal.tasks.remove(t);
        }
    }

    private void saveToFile() {
        BufferedWriter output = null;
        try {
            File file = new File(path);
            output = new BufferedWriter(new FileWriter(file));
            String xmlCal = Cal.serialize(cal);
            output.write(xmlCal);
        } catch (Exception e) {
            print(e.getMessage());
        } finally {
            try {
                output.close();
            } catch (Exception e) {
                print(e.getMessage());
            }
        }
        
    }    

    @Override
    public String get(Envelope envelope) {             
        String result = "";
        try {
            if(envelope.command.equalsIgnoreCase("GET")) {
                result = get(envelope.data);
            } else {
                throw new UnsupportedCommandException(envelope.command);
            }
        } catch (Exception e) {
            result = e.getMessage();
        } finally {
            return result;
        }
    }

    @Override
    public void set(Envelope envelope) {        
        if(envelope.command.equalsIgnoreCase("POST")) {
            post(envelope.data);
        } else if(envelope.command.equalsIgnoreCase("PUT")) {
            put(envelope.data);
        } else if(envelope.command.equalsIgnoreCase("DELETE")) {
            delete(envelope.data);
        }
               
        saveToFile();
    }
}