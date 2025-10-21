package core.glRenderer;

import api.DiaInputMapper;
import core.InputController;
import org.lwjgl.glfw.GLFW;

import java.util.HashMap;

/**
 * GLInputMapper
 * @author Santiago Barreiro
 */
public class GLInputMapper extends DiaInputMapper {

    private static final HashMap<Integer, Integer> inputMap = new HashMap<>();

    public GLInputMapper(InputController inputController) {
        super(inputController);
        inputMap.put(GLFW.GLFW_PRESS, InputController.PRESS);
        inputMap.put(GLFW.GLFW_RELEASE, InputController.RELEASE);
    }

    public void registerKeyAction(int glfwAction, int glfwKey) {
        this.inputController.registerKey(inputMap.get(glfwAction), glfwKey);
    }
}
