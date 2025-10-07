import api.DiaLogger;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Logger
 *
 * @author: Santiago Barreiro
 */
public class Logger implements DiaLogger {

    private Calendar cal = Calendar.getInstance();
    private SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");

    @Override
    public void log(String message) {
        System.out.println("[" + levels.DEBUG + "][" + sdf.format(cal.getTime()) + "] -> " + message);
    }

    @Override
    public void log(String message, Enum<?> level) {
        System.out.println("[" + level + "][" + sdf.format(cal.getTime()) + "] -> " + message);
    }

    @Override
    public void log(Class<?> callerClass, String message) {
        System.out.println("[" + levels.DEBUG + "][" + sdf.format(cal.getTime()) + "][" + callerClass.getName() + "] -> " + message);
    }

    @Override
    public void log(Class<?> callerClass, String message, Enum<?> level) {
        System.out.println("[" + level + "][" + sdf.format(cal.getTime()) + "][" + callerClass.getName() + "] -> " + message);
    }
}
