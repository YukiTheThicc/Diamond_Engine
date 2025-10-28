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

    /**
     * Logs a massage with the default level
     * @param message String to log
     */
    public void log(String message);

    /**
     * Logs a massage with the provided level
     * @param message String to log
     * @param level Level of the log entry
     */
    public void log(String message, Enum<?> level);

    /**
     * Logs a massage with the default level that specified the caller class
     * @param callerClass Class of the caller
     * @param message String to log
     */
    public void log(Class<?> callerClass, String message);

    /**
     * Logs a String with the provided level and indicates the class that called the log
     * @param callerClass Class of the caller
     * @param message String to log
     * @param level Level of the log entry
     */
    public void log(Class<?> callerClass, String message, Enum<?> level);
}
