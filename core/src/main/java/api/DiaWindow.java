package api;

import core.InputController;

/**
 * DiaWindow
 * Main engine Window API.
 * @author Santiago Barreiro
 */
public interface DiaWindow {

    public interface ResizeObserver {
        public void adjustSize();
    }

    /**
     * Initializes the window. Implementing classes should take care of all needed initializations of the window here,
     * including callbacks, window properties, etc.
     * @param width Width of the window
     * @param height Height of the window
     * @param inputController Input controller
     */
    public void init(int width, int height, InputController inputController);

    /**
     * Polls the input events triggered within the window
     */
    public void pollEvents();

    /**
     * Refreshes the window by clearing/flushing it, swapping buffers
     */
    public void refresh();

    /**
     * Returns if the window is still open
     * @return True if window is still open, false if not
     */
    public boolean isOpen();

    /**
     * Closes the window and frees allocated resource
     * s
     */
    public void close();

    /**
     * Adds a ResizeListener so it can listen for window resizes
     */
    public void addResizeObserver(ResizeObserver observer);

    /**
     * Removes a ResizeListener
     */
    public void removeResizeObserver(ResizeObserver observer);
}
