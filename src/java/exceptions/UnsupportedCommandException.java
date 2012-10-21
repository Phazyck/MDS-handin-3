package exceptions;

public class UnsupportedCommandException extends Exception {   
    
    public UnsupportedCommandException(String command) {
        super("Command " + command + " not supported.");        
    }
}
