package core;

import api.DiaRenderer;
import api.DiaWindow;

import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.opengl.GL;
import java.nio.IntBuffer;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.glfw.GLFW.glfwSetErrorCallback;
import static org.lwjgl.system.MemoryStack.*;
import static org.lwjgl.system.MemoryStack.stackPop;
import static org.lwjgl.system.MemoryUtil.NULL;

/**
 * GLWindow
 *
 * @author Santiago Barreiro
 */
public class Window implements DiaWindow {

    // CONSTANTS

    // ATTRIBUTES
    private String title;
    private IntBuffer posX, posY;
    private long glfwWindow;                    // GL core.Window address
    private static int width, height;
    private static InputController inputController;
    private static Window window = null;        // Unique window instance

    // CONSTRUCTORS
    private Window() {
        width = 800;
        height = 600;
        stackPush();
        this.posX = stackCallocInt(1);
        stackPush();
        this.posY = stackCallocInt(1);
        this.title = "DiamondEngine v0.0.0.1";
        stackPop();
        stackPop();
    }

    // GETTERS & SETTERS
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    // METHODS
    public static DiaWindow get() {
        if (window == null) {
            window = new Window();
        }
        return window;
    }

    @Override
    public void init(int width, int height, InputController inputController) {
        GLFWErrorCallback.createPrint(System.err).set();

        if (!glfwInit()) {
            throw new IllegalStateException("Failed to initialize GLFW");
        }

        Window.width = width;
        Window.height = height;
        Window.inputController = inputController;

        // Set default window state
        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);
        glfwWindowHint(GLFW_MAXIMIZED, GLFW_FALSE);

        // Create the window itself
        glfwWindow = glfwCreateWindow(width, height, title, NULL, NULL);
        if (glfwWindow == NULL) {
            throw new IllegalStateException("Failed to create GLFW window");
        }

        // Set up window input callbacks
        glfwSetKeyCallback(glfwWindow, Window::keyCallback);
        glfwSetFramebufferSizeCallback(glfwWindow, Window::frameBufferSizeCallback);

        // Setup context and show window
        glfwMakeContextCurrent(glfwWindow);
        glfwSwapInterval(1);

        glfwShowWindow(glfwWindow);
    }

    @Override
    public void pollEvents() {
        glfwPollEvents();
    }

    @Override
    public void refresh() {
        glfwSwapBuffers(glfwWindow);
    }

    @Override
    public boolean isOpen() {
        return !glfwWindowShouldClose(glfwWindow);
    }

    @Override
    public void close() {
        glfwFreeCallbacks(glfwWindow);
        glfwDestroyWindow(glfwWindow);
        posX = null;
        posY = null;

        // Termination of GLFW
        glfwTerminate();
        glfwSetErrorCallback(null).free();
    }

    @Override
    public void addResizeObserver(ResizeObserver observer) {

    }

    @Override
    public void removeResizeObserver(ResizeObserver observer) {

    }

    private static void frameBufferSizeCallback(long window, int width, int height) {

    }

    public static void keyCallback(long window, int key, int scancode, int action, int mods) {

    }
}
