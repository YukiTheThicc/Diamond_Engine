package api;

/**
 * DiaWindow
 *
 * @author Santiago Barreiro
 */
public interface DiaWindow {

    public void init(DiaInputMapper inputMapper);

    public void pollEvents();

    public void refresh();

    public boolean isOpen();

    public void close();
}
