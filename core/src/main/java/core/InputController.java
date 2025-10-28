package core;

import utils.BitFlag;

import java.util.Arrays;

/**
 * InputController
 *
 * @author Santiago Barreiro
 */
public class InputController {

    public enum a {

    }

    // CONSTANTS
    public static final int PRESS = 0;
    public static final int RELEASE = 1;
    private static final int NUM_KEYS = 350;                    // Number of key bindings supported by DiamondEngine

    // ATTRIBUTES
    private static final BitFlag keyPressed = new BitFlag(NUM_KEYS >> 5);
    private static final BitFlag keyBeginPressed = new BitFlag(NUM_KEYS >> 5);

    // METHODS
    public void registerKey(int key, int action) {
        if (action == PRESS) {
            keyPressed.setFlag(key, true);
            keyBeginPressed.setFlag(key, true);
        } else if (action == RELEASE) {
            keyPressed.setFlag(key, false);
            keyBeginPressed.setFlag(key, false);
        }
    }

    public boolean isKeyPressed(int keyCode) {
        return keyPressed.getFlag(keyCode);
    }

    public boolean keyBeginPress(int keyCode) {
        return keyBeginPressed.getFlag(keyCode);
    }

    public void refresh() {
        keyBeginPressed.clear();
    }
}
