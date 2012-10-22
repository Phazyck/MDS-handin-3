package taskmanager;

import serialization.Envelope;

public interface ITaskManager {
    public String get(Envelope envelope);
    public void set (Envelope envelope);   
}
