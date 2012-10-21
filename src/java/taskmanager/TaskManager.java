package taskmanager;

import exceptions.UnsupportedCommandException;
import java.io.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.bind.*;
import serialization.*;

public class TaskManager implements ITaskManager {

    private Cal cal;
    private String path;

    public TaskManager() throws JAXBException, FileNotFoundException {
        this("./web/WEB-INF/task-manager-xml.xml");
    }

    public TaskManager(String path) throws JAXBException, FileNotFoundException {
        this.path = path;
        JAXBContext context = JAXBContext.newInstance(Cal.class);
        FileInputStream stream = new FileInputStream(path);
        cal = (Cal) context.createUnmarshaller().unmarshal(stream);
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
            System.out.println(e.getMessage());
        }
    }

    private void put(String taskXml) {        
        delete(taskXml);
        post(taskXml);
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
            System.out.println(e.getMessage());
        } finally {
            try {
                output.close();
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
        
    }    

    @Override
    public String fetch(Envelope envelope) {
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
    public void alter(Envelope envelope) {        
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