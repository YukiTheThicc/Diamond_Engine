package core.glRenderer;

import api.DiaInputMapper;
import api.DiaWindow;
import core.InputController;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.opengl.GL;

import java.nio.IntBuffer;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.glfw.GLFW.glfwSetErrorCallback;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryStack.*;
import static org.lwjgl.system.MemoryStack.stackPop;
import static org.lwjgl.system.MemoryUtil.NULL;

/**
 * GLWindow
 *
 * @author Santiago Barreiro
 */
public class GLWindow implements DiaWindow {

    // CONSTANTS
    // ATTRIBUTES
    private String title;
    private static int width, height;
    private IntBuffer posX, posY;
    private long glfwWindow;                    // GL core.Window address
    private static GLWindow window = null;        // Unique window instance

    // CONSTRUCTORS
    private GLWindow() {
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

    public static int getWidth() {
        return width;
    }

    public static void setWidth(int width) {
        GLWindow.width = width;
    }

    public static int getHeight() {
        return height;
    }

    public static void setHeight(int height) {
        GLWindow.height = height;
    }

    // METHODS
    public static DiaWindow get() {
        if (window == null) {
            window = new GLWindow();
        }
        return window;
    }

    @Override
    public void init(DiaInputMapper inputMapper) {
        GLFWErrorCallback.createPrint(System.err).set();

        if (!glfwInit()) {
            throw new IllegalStateException("Failed to initialize GLFW");
        }

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
        glfwSetKeyCallback(glfwWindow, inputMapper::keyCallback);
        glfwSetFramebufferSizeCallback(glfwWindow, GLWindow::frameBufferSizeCallback);

        // Setup context and show window
        glfwMakeContextCurrent(glfwWindow);
        glfwSwapInterval(1);
        GL.createCapabilities();
        glDisable(GL_BLEND);
        glBlendFunc(GL_ONE, GL_ONE_MINUS_SRC_ALPHA);
        glfwShowWindow(glfwWindow);
    }

    @Override
    public void pollEvents() {
        glfwPollEvents();
        glClearColor(0.1f, 0.1f, 0.1f, 1.0f);
        glClear(GL_COLOR_BUFFER_BIT);
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

    private static void frameBufferSizeCallback(long window, int width, int height) {
        glViewport(0, 0, width, height);
    }
}
