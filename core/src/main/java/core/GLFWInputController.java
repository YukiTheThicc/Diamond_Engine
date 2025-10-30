package core;

import org.joml.Vector2f;
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
    private static final int NUM_KEYS = 350;
    private static final int NUM_MOUSE_BUTTONS = 9;

    // ATTRIBUTES
//    private static final BitFlag keyPressed = new BitFlag(NUM_KEYS >> 5);
//    private static final BitFlag keyBeginPressed = new BitFlag(NUM_KEYS >> 5);
    private static final boolean[] keyPressed = new boolean[NUM_KEYS];
    private static final boolean[] keyBeginPressed = new boolean[NUM_KEYS];
    private static final boolean[] mouseButtonPressed = new boolean[NUM_MOUSE_BUTTONS];
    private static double offsetX = 0;
    private static double offsetY = 0;
    private static double lastCursorX = 0;
    private static double lastCursorY = 0;
    private static double scroll = 0;

    public static void init(float width, float height) {
        lastCursorX = width / 2;
        lastCursorY = height / 2;
    }

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

    public static void cursorPosCallback(double x, double y) {
        offsetX = x - lastCursorX;
        offsetY = lastCursorY - y;
        lastCursorX = x;
        lastCursorY = y;
    }

    public static void scrollCallback(double x, double y) {
        scroll = y;
    }

    public static void mouseButtonCallback(long window, int button, int action, int mods) {
        if (action == GLFW_PRESS) {
            mouseButtonPressed[button] = true;
        } else if (action == GLFW_RELEASE) {
            mouseButtonPressed[button] = false;
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

    public static Vector2f getCursorOffset() {
        return new Vector2f((float) offsetX, (float) offsetY);
    }

    public static float getScroll() {
        return (float) scroll;
    }

    public static void refresh() {
        Arrays.fill(keyBeginPressed, false);
        offsetX = 0f;
        offsetY = 0f;
        scroll = 0f;
    }
}
