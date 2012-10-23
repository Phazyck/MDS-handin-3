package taskmanager;

import serialization.Envelope;

public interface TaskManager {
    public String get(Envelope envelope);
    public void set (Envelope envelope);   
}
