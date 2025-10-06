package api;

public interface DiaLogger {

    /**
     * Levels defined by the API
     */
    public enum levels {
        CRITICAL,
        ERROR,
        WARN,
        DEBUG,
        INFO,
    }

    public void log(String message);

    public void log(String message, Enum<?> level);

    public void log(Class<?> callerClass, String message);

    public void log(Class<?> callerClass, String message, Enum<?> level);
}
