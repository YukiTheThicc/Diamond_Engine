package core;

import api.*;
import core.exceptions.DiamondCriticalException;

import static org.lwjgl.glfw.GLFW.glfwGetTime;

/**
 * Diamond
 *
 * @author Santiago Barreiro
 */
public final class Diamond {

    // CONSTANTS
    public enum DiamondEvents {
        ENGINE_START
    }

    // ATTRIBUTES
    private final DiaWindow window;
    private final DiaRenderer renderer;
    private final DiaEntityPool entityPool;
    private final DiaLogger logger;
    private RenderTarget currentTarget;
    private float dt = 0f;
    private boolean running;

    // CONSTRUCTORS
    public Diamond(DiaWindow window, DiaRenderer renderer, DiaEntityPool entityPool, DiaLogger logger,
                   RenderTarget target) {
        this.window = window;
        this.renderer = renderer;
        this.entityPool = entityPool;
        this.logger = logger;
        this.currentTarget = target;
    }

    // GETTERS
    public RenderTarget getCurrentTarget() {
        return currentTarget;
    }

    public void setCurrentTarget(RenderTarget currentTarget) {
        this.currentTarget = currentTarget;
    }

    // METHODS
    public void start() {

        float et;
        float bt = 0f;
        EventPool.throwEvent(new EventPool.Event(DiamondEvents.ENGINE_START));
        try {
            running = true;
            while (running) {

                window.pollEvents();
                entityPool.dispatchSystems(dt);
                EventPool.dispatchEvents();

                et = (float) glfwGetTime();
                dt = et - bt;
                bt = et;

                window.refresh();
                GLFWInputController.refresh();
                running = window.isOpen();
            }
        } catch (DiamondCriticalException e) {
            logger.log(e.origin, e.getMessage());
        } finally {
            close();
        }
    }

    public void close() {
        window.close();
    }
}
