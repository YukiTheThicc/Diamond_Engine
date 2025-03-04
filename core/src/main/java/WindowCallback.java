import java.util.Arrays;

import static org.lwjgl.glfw.GLFW.GLFW_PRESS;
import static org.lwjgl.glfw.GLFW.GLFW_RELEASE;

/**
 * KeyListener
 *
 * @author: Santiago Barreiro
 */
public class WindowCallback {

    // CONSTANTS

    // ATTRIBUTES
    private static final int NUM_KEYS = 350;                    // Number of key bindings supported by GLFW
    private static WindowCallback listener;
    private final boolean[] keyPressed = new boolean[NUM_KEYS];
    private final boolean[] keyBeginPressed = new boolean[NUM_KEYS];

    // CONSTRUCTORS
    private WindowCallback() {

    }

    // GETTERS & SETTERS

    // METHODS
    public static WindowCallback get() {
        if (listener == null) {
            listener = new WindowCallback();
        }
        return listener;
    }

    public static void endFrame() {
        Arrays.fill(get().keyBeginPressed, false);
    }

    public static void keyCallback(long window, int key, int scancode, int action, int mods) {
        if (action == GLFW_PRESS) {
            get().keyPressed[key] = true;
            get().keyBeginPressed[key] = true;
        } else if (action == GLFW_RELEASE) {
            get().keyPressed[key] = false;
            get().keyBeginPressed[key] = false;
        }
    }

    public static boolean isKeyPressed(int keyCode) {
        return get().keyPressed[keyCode];
    }

    public static boolean keyBeginPress(int keyCode) {
        return get().keyBeginPressed[keyCode];
    }
}
