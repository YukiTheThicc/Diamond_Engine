package api;

import core.InputController;

/**
 * DiaWindow
 *
 * @author Santiago Barreiro
 */
public interface DiaWindow {

    public void init(InputController inputController);

    public void pollEvents();

    public void refresh();

    public boolean isOpen();

    public void close();
}
