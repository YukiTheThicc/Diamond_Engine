package core;

import java.util.Arrays;

import static org.lwjgl.glfw.GLFW.GLFW_PRESS;
import static org.lwjgl.glfw.GLFW.GLFW_RELEASE;

/**
 * InputController
 *
 * @author Santiago Barreiro
 */
public class InputController {

    // CONSTANTS
    private static final int NUM_KEYS = 350;                    // Number of key bindings supported by DiamondEngine

    // ATTRIBUTES
    private static final boolean[] keyPressed = new boolean[NUM_KEYS];
    private static final boolean[] keyBeginPressed = new boolean[NUM_KEYS];

    // METHODS


    public static void keyCallback(long window, int key, int scancode, int action, int mods) {
        if (action == GLFW_PRESS) {
            keyPressed[key] = true;
            keyBeginPressed[key] = true;
        } else if (action == GLFW_RELEASE) {
            keyPressed[key] = false;
            keyBeginPressed[key] = false;
        }
    }

    public static boolean isKeyPressed(int keyCode) {
        return keyPressed[keyCode];
    }

    public static boolean keyBeginPress(int keyCode) {
        return keyBeginPressed[keyCode];
    }

    public static void refresh() {
        Arrays.fill(keyBeginPressed, false);
    }
}
