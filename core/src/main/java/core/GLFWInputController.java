package core;

import utils.BitFlag;

import java.util.Arrays;

import static org.lwjgl.glfw.GLFW.*;

/**
 * InputController
 *
 * @author Santiago Barreiro
 */
public class GLFWInputController {

    // CONSTANTS
    private static final int NUM_KEYS = 350;                    // Number of key bindings supported by DiamondEngine

    // ATTRIBUTES
//    private static final BitFlag keyPressed = new BitFlag(NUM_KEYS >> 5);
//    private static final BitFlag keyBeginPressed = new BitFlag(NUM_KEYS >> 5);
    private static boolean keyPressed[] = new boolean[NUM_KEYS];
    private static boolean keyBeginPressed[] = new boolean[NUM_KEYS];

    // METHODS
    public static void registerKey(int key, int action) {
//        if (action == GLFW_PRESS) {
//            keyPressed.setFlag(key, true);
//            keyBeginPressed.setFlag(key, true);
//        } else if (action == GLFW_RELEASE) {
//            keyPressed.setFlag(key, false);
//            keyBeginPressed.setFlag(key, false);
//        }
        if (action == GLFW_PRESS) {
            keyPressed[key] = true;
            keyBeginPressed[key] = true;
        } else if (action == GLFW_RELEASE) {
            keyPressed[key] = false;
            keyBeginPressed[key] = false;
        }
    }

//    public static boolean isKeyPressed(int keyCode) {
//        return keyPressed.getFlag(keyCode);
//    }
//
//    public static boolean keyBeginPress(int keyCode) {
//        return keyBeginPressed.getFlag(keyCode);
//    }
//
//    public static void refresh() {
//        keyBeginPressed.clear();
//    }

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
