package taskmanager;

import serialization.Envelope;

public interface ITaskManager {
    public String fetch(Envelope envelope);
    public void alter (Envelope envelope);   
}
