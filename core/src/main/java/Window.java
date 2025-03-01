import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.opengl.GL;

import java.nio.IntBuffer;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.glfw.GLFW.glfwCreateWindow;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryStack.*;
import static org.lwjgl.system.MemoryUtil.NULL;

/**
 * Window
 *
 * @author: Santiago Barreiro
 */
public class Window {

    // CONSTANTS

    // ATTRIBUTES
    private String title;
    private static int width, height;
    private IntBuffer posX, posY;
    private long glfwWindow;                    // GL Window address
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

    public static int getWidth() {
        return width;
    }

    public static void setWidth(int width) {
        Window.width = width;
    }

    public static int getHeight() {
        return height;
    }

    public static void setHeight(int height) {
        Window.height = height;
    }

    public long getGlfwWindow() {
        return glfwWindow;
    }

    public void setGlfwWindow(long glfwWindow) {
        this.glfwWindow = glfwWindow;
    }

    // METHODS

    /**
     * Main window singleton access method
     * @return Instance of the window. If null creates a window instance and returns it
     */
    public static Window get() {
        if (window == null) {
            window = new Window();
        }
        return window;
    }

    public void init() {
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

        // Setup context and show window
        glfwMakeContextCurrent(glfwWindow);
        glfwSwapInterval(1);
        GL.createCapabilities();
        glDisable(GL_BLEND);
        glBlendFunc(GL_ONE, GL_ONE_MINUS_SRC_ALPHA);
        glfwShowWindow(glfwWindow);
    }

    /**
     * Polls window events
     */
    public void pollEvents() {
        glfwPollEvents();
    }

    /**
     * Flushes the current frame (by swapping buffers)
     */
    public void flushFrame() {
        glfwSwapBuffers(glfwWindow);
    }

    public void close() {
        glfwFreeCallbacks(glfwWindow);
        glfwDestroyWindow(glfwWindow);
        posX = null;
        posY = null;

        // Termination of GLFW
        glfwTerminate();
        glfwSetErrorCallback(null).free();
    }
}
