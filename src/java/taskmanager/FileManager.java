package taskmanager;

import java.io.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.bind.*;
import serialization.*;

public class FileManager implements TaskManager {

    public static void main(String[] args) throws Exception {
        String postData = Task.serialize(new Task("test", "Update Me", "22-10-2012", "not-executed", "Update this task using update.", "TheHitmen"));
        String putData = Task.serialize(new Task("test", "Delete Me", "22-10-2012", "executed", "Delete this task using delete.", "TheHitmen"));
        String getData = "TheHitmen";
        String deleteData = "test";

        Envelope postEnvelope = new Envelope("POST", postData);
        Envelope putEnvelope = new Envelope("PUT", putData);
        Envelope getEnvelope = new Envelope("GET", getData);
        Envelope deleteEnvelope = new Envelope("DELETE", deleteData);

        FileManager tm = new FileManager();
        System.out.println(Cal.serialize(tm.getCal()));
        tm.set(postEnvelope);

        tm = new FileManager();
        System.out.println(tm.get(getEnvelope));

        System.out.println(Cal.serialize(tm.getCal()));

        tm.set(putEnvelope);
        System.out.println(tm.get(getEnvelope));
        tm.set(deleteEnvelope);
        System.out.println(tm.get(getEnvelope));
    }
    private Cal cal;
    private String path;

    public FileManager() {
        this("./web/WEB-INF/task-manager-xml.xml");
    }

    public FileManager(String path) {
        this.path = path;
        cal = null;
        try (FileInputStream stream = new FileInputStream(path)) {
            JAXBContext context = JAXBContext.newInstance(Cal.class);
            cal = (Cal) context.createUnmarshaller().unmarshal(stream);

        } catch (JAXBException | IOException ex) {
            Logger.getLogger(FileManager.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (cal == null) {
                cal = new Cal();
            }
        }
    }

    public Cal getCal() {
        return cal;
    }

    public void setCal(Cal cal) {
        this.cal = cal;
        saveToFile();
    }

    private void post(String taskXml) throws JAXBException {
        cal.tasks.add(Task.deSerialize(taskXml));
    }

    private void put(String taskXml) throws JAXBException {
        Task task = Task.deSerialize(taskXml);
        delete(task.id);
        post(taskXml);
    }

    private String get(String attendantId) throws JAXBException {
        List<Task> matches = new ArrayList<>();

        for (Task t : cal.tasks) {
            if (t.attendants.contains(attendantId)) {
                matches.add(t);
            }
        }

        return Tasks.serialize(new Tasks(matches));
    }

    private void delete(String taskId) {
        List<Task> matches = new ArrayList<>();

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
        try (BufferedWriter output = new BufferedWriter(new FileWriter(new File(path)))) {
            output.write(Cal.serialize(cal));
        } catch (JAXBException | IOException ex) {
            Logger.getLogger(FileManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public String get(Envelope envelope) {
        try {
            if (envelope.command.equalsIgnoreCase("GET")) {
                return get(envelope.data);
            } else {
                throw new NoSuchMethodException(envelope.command);
            }
        } catch (NoSuchMethodException | JAXBException ex) {
            return ex.toString();
        }
    }

    @Override
    public void set(Envelope envelope) {
        try {
            if (envelope.command.equalsIgnoreCase("POST")) {
                post(envelope.data);
            } else if (envelope.command.equalsIgnoreCase("PUT")) {
                put(envelope.data);
            } else if (envelope.command.equalsIgnoreCase("DELETE")) {
                delete(envelope.data);
            } else {
                throw new NoSuchMethodException(envelope.command);
            }
        } catch (JAXBException | NoSuchMethodException ex) {
            Logger.getLogger(FileManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        saveToFile();
    }
}