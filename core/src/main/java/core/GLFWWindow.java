package core;

import api.DiaWindow;

import org.lwjgl.glfw.GLFWErrorCallback;

import java.awt.*;
import java.nio.IntBuffer;
import java.util.ArrayList;

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
public class GLFWWindow implements DiaWindow {

    // CONSTANTS

    // ATTRIBUTES
    private String title;
    private IntBuffer posX, posY;
    private long glfwWindow;                        // GL core.Window address
    private int width, height;
    private static GLFWWindow window = null;        // Unique window instance
    private ArrayList<ResizeObserver> resizeObservers = new ArrayList<>();

    // CONSTRUCTORS
    private GLFWWindow() {
        width = 800;
        height = 600;
        stackPush();
        posX = stackCallocInt(1);
        stackPush();
        posY = stackCallocInt(1);
        title = "DiamondEngine v0.0.0.1";
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
            window = new GLFWWindow();
        }
        return window;
    }

    @Override
    public void init(int width, int height) {

        GLFWErrorCallback.createPrint(System.err).set();
        if (!glfwInit()) {
            throw new IllegalStateException("Failed to initialize GLFW");
        }

        this.width = width;
        this.height = height;

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
        glfwSetKeyCallback(glfwWindow, this::keyCallback);
        glfwSetFramebufferSizeCallback(glfwWindow, this::frameBufferSizeCallback);

        // Setup context and show window
        glfwMakeContextCurrent(glfwWindow);
        glfwSwapInterval(1);

        glfwShowWindow(glfwWindow);
    }

    @Override
    public float getAspectRatio() {
        return (float) width / height;
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
        if (observer != null) resizeObservers.add(observer);
    }

    @Override
    public void removeResizeObserver(ResizeObserver observer) {
        if (observer != null) resizeObservers.remove(observer);
    }

    private void frameBufferSizeCallback(long window, int width, int height) {
        for (ResizeObserver observer : resizeObservers) observer.adjustSize(width, height);
    }

    public void keyCallback(long window, int key, int scancode, int action, int mods) {
        GLFWInputController.registerKey(key, action);
    }
}
