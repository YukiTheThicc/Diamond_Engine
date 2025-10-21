package core;

import java.util.Arrays;

/**
 * InputController
 *
 * @author Santiago Barreiro
 */
public class InputController {

    // CONSTANTS
    public static final int PRESS = 0;
    public static final int RELEASE = 1;
    private static final int NUM_KEYS = 350;                    // Number of key bindings supported by DiamondEngine

    // ATTRIBUTES
    private static final boolean[] keyPressed = new boolean[NUM_KEYS];
    private static final boolean[] keyBeginPressed = new boolean[NUM_KEYS];

    // METHODS
    public void registerKey(int key, int action) {
        if (action == PRESS) {
            keyPressed[key] = true;
            keyBeginPressed[key] = true;
        } else if (action == RELEASE) {
            keyPressed[key] = false;
            keyBeginPressed[key] = false;
        }
    }

    public boolean isKeyPressed(int keyCode) {
        return keyPressed[keyCode];
    }

    public boolean keyBeginPress(int keyCode) {
        return keyBeginPressed[keyCode];
    }

    public void refresh() {
        Arrays.fill(keyBeginPressed, false);
    }
}
