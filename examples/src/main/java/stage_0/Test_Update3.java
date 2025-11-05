package stage_0;

import api.*;
import core.AssetPool;
import core.assets.Texture;
import core.Camera;
import core.GLFWWindow;
import core.GLFWInputController;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;
import core.glRenderer.GLAssetLoader;
import core.glRenderer.GLRenderer;
import core.glRenderer.GLShader;
import utils.DiaMath;
import utils.Logger;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.glViewport;

/**
 * Test_Version
 *
 * @author: Santiago Barreiro
 */
public class Test_Update3 {

    static final int NUM_GRID_LINES = 50;

    // ATTRIBUTES
    static DiaLogger logger;
    static DiaWindow window;
    static AssetPool assetPool;
    static GLRenderer glRenderer;
    static GLShader textureShader;
    static Camera camera;
    static float cameraSpeed = 2.5f;
    static float cameraYaw = 45f;
    static float cameraPitch = -30f;
    static float cursorSensitivity = 0.1f;
    static boolean captureMouse = true;

    static float[] vertices = {
            -0.5f, -0.5f, -0.5f,  0.0f, 0.0f,
            0.5f, -0.5f, -0.5f,  1.0f, 0.0f,
            0.5f,  0.5f, -0.5f,  1.0f, 1.0f,
            0.5f,  0.5f, -0.5f,  1.0f, 1.0f,
            -0.5f,  0.5f, -0.5f,  0.0f, 1.0f,
            -0.5f, -0.5f, -0.5f,  0.0f, 0.0f,

            -0.5f, -0.5f,  0.5f,  0.0f, 0.0f,
            0.5f, -0.5f,  0.5f,  1.0f, 0.0f,
            0.5f,  0.5f,  0.5f,  1.0f, 1.0f,
            0.5f,  0.5f,  0.5f,  1.0f, 1.0f,
            -0.5f,  0.5f,  0.5f,  0.0f, 1.0f,
            -0.5f, -0.5f,  0.5f,  0.0f, 0.0f,

            -0.5f,  0.5f,  0.5f,  1.0f, 0.0f,
            -0.5f,  0.5f, -0.5f,  1.0f, 1.0f,
            -0.5f, -0.5f, -0.5f,  0.0f, 1.0f,
            -0.5f, -0.5f, -0.5f,  0.0f, 1.0f,
            -0.5f, -0.5f,  0.5f,  0.0f, 0.0f,
            -0.5f,  0.5f,  0.5f,  1.0f, 0.0f,

            0.5f,  0.5f,  0.5f,  1.0f, 0.0f,
            0.5f,  0.5f, -0.5f,  1.0f, 1.0f,
            0.5f, -0.5f, -0.5f,  0.0f, 1.0f,
            0.5f, -0.5f, -0.5f,  0.0f, 1.0f,
            0.5f, -0.5f,  0.5f,  0.0f, 0.0f,
            0.5f,  0.5f,  0.5f,  1.0f, 0.0f,

            -0.5f, -0.5f, -0.5f,  0.0f, 1.0f,
            0.5f, -0.5f, -0.5f,  1.0f, 1.0f,
            0.5f, -0.5f,  0.5f,  1.0f, 0.0f,
            0.5f, -0.5f,  0.5f,  1.0f, 0.0f,
            -0.5f, -0.5f,  0.5f,  0.0f, 0.0f,
            -0.5f, -0.5f, -0.5f,  0.0f, 1.0f,

            -0.5f,  0.5f, -0.5f,  0.0f, 1.0f,
            0.5f,  0.5f, -0.5f,  1.0f, 1.0f,
            0.5f,  0.5f,  0.5f,  1.0f, 0.0f,
            0.5f,  0.5f,  0.5f,  1.0f, 0.0f,
            -0.5f,  0.5f,  0.5f,  0.0f, 0.0f,
            -0.5f,  0.5f, -0.5f,  0.0f, 1.0f
    };

    public static void main(String[] args) {

        logger = new Logger();
        window = GLFWWindow.get();
        window.init(800, 600);
        assetPool = new AssetPool(logger, new GLAssetLoader(logger));
        glRenderer = new GLRenderer(logger);
        glRenderer.init();
        camera = new Camera(
                new Vector3f(0f, 0f, -1f),
                new Vector3f(0f, 1f, 0f),
                90f,
                4f,
                0.75f
        );
        camera.moveTo(new Vector3f(-5f,3f, -5f));
        camera.rotate(45f, -30f, 0f);
        GLFWInputController.init(800, 600);

        String vs = System.getProperty("user.dir") + "\\examples\\src\\main\\resources\\texturedVertex.glsl";
        String fs = System.getProperty("user.dir") + "\\examples\\src\\main\\resources\\texturedFragment.glsl";
        textureShader = (GLShader) assetPool.getShader(vs, fs);

        // Add resize listener for the window to adjust viewport
        window.addResizeObserver(new DiaWindow.ResizeObserver() {
            @Override
            public void adjustSize(int width, int height) {
                glViewport(0, 0, width, height);
            }
        });

        boolean running = true;
        float bt = (float) glfwGetTime();
        float et = (float) glfwGetTime();
        float dt = 0f;
        Vector3f[] positions = {
                new Vector3f(DiaMath.randomFloat(-3f, 3f), DiaMath.randomFloat(-3f, 3f), DiaMath.randomFloat(-5f, 5f)),
                new Vector3f(DiaMath.randomFloat(-3f, 3f), DiaMath.randomFloat(-3f, 3f), DiaMath.randomFloat(-5f, 5f)),
                new Vector3f(DiaMath.randomFloat(-3f, 3f), DiaMath.randomFloat(-3f, 3f), DiaMath.randomFloat(-5f, 5f)),
                new Vector3f(DiaMath.randomFloat(-3f, 3f), DiaMath.randomFloat(-3f, 3f), DiaMath.randomFloat(-5f, 5f)),
                new Vector3f(DiaMath.randomFloat(-3f, 3f), DiaMath.randomFloat(-3f, 3f), DiaMath.randomFloat(-5f, 5f)),
                new Vector3f(DiaMath.randomFloat(-3f, 3f), DiaMath.randomFloat(-3f, 3f), DiaMath.randomFloat(-5f, 5f)),
                new Vector3f(DiaMath.randomFloat(-3f, 3f), DiaMath.randomFloat(-3f, 3f), DiaMath.randomFloat(-5f, 5f)),
                new Vector3f(DiaMath.randomFloat(-3f, 3f), DiaMath.randomFloat(-3f, 3f), DiaMath.randomFloat(-5f, 5f)),
                new Vector3f(DiaMath.randomFloat(-3f, 3f), DiaMath.randomFloat(-3f, 3f), DiaMath.randomFloat(-5f, 5f)),
                new Vector3f(DiaMath.randomFloat(-3f, 3f), DiaMath.randomFloat(-3f, 3f), DiaMath.randomFloat(-5f, 5f))
        };

        while (running) {

            window.pollEvents();
            processControls(dt);
            Matrix4f projection = camera.getPerspectiveProjection(window.getAspectRatio(), 0.1f, 1000f);
            Matrix4f view = camera.getViewMatrix();

            renderGrid();
            glRenderer.renderFrame(view, projection);

            et = (float) glfwGetTime();
            dt = et - bt;
            bt = et;

            window.refresh();
            GLFWInputController.refresh();
            running = window.isOpen();
        }

        window.close();
    }

    private void generateEntities() {

    }

    private static void renderGrid() {
        Vector3f gridColor = new Vector3f(0.5f, 0.5f, 0.5f);
        Vector3f originColor = new Vector3f(0.9f, 0.9f, 0.9f);
        glRenderer.addLine(new Vector3f(-1000,0,0), new Vector3f(1000, 0, 0), originColor);
        glRenderer.addLine(new Vector3f(0,-1000,0), new Vector3f(0, 1000, 0), originColor);
        glRenderer.addLine(new Vector3f(0,0,-1000), new Vector3f(0, 0, 1000), originColor);

        int half = NUM_GRID_LINES / 2;
        for (int i = 0; i < NUM_GRID_LINES; i++) {
            if (-half + i != 0) {
                glRenderer.addLine(new Vector3f(-half,0, -half + i), new Vector3f(half, 0, -half + i), gridColor);
                glRenderer.addLine(new Vector3f(-half + i,0, -half), new Vector3f(-half + i, 0, half), gridColor);
            }
        }
    }

    private static void processControls(float dt) {
        // Cursor capture control
        if (GLFWInputController.keyBeginPress(GLFW_KEY_LEFT_ALT)) {
            captureMouse = !captureMouse;
            window.captureCursor(captureMouse);
        }

        // Camera movement
        if (GLFWInputController.isKeyPressed(GLFW_KEY_W)) camera.moveZ(cameraSpeed * dt);
        if (GLFWInputController.isKeyPressed(GLFW_KEY_S)) camera.moveZ(-cameraSpeed * dt);
        if (GLFWInputController.isKeyPressed(GLFW_KEY_D)) camera.moveX(cameraSpeed * dt);
        if (GLFWInputController.isKeyPressed(GLFW_KEY_A)) camera.moveX(-cameraSpeed * dt);

        if (GLFWInputController.isKeyPressed(GLFW_KEY_SPACE)) camera.moveY(cameraSpeed * dt);
        if (GLFWInputController.isKeyPressed(GLFW_KEY_LEFT_CONTROL)) camera.moveY(-cameraSpeed * dt);
        if (GLFWInputController.isKeyPressed(GLFW_KEY_LEFT_CONTROL) && GLFWInputController.isKeyPressed(GLFW_KEY_R)) camera.moveTo(new Vector3f());

        // Cursor controls
        Vector2f cursorOffset = GLFWInputController.getCursorOffset();
        cameraYaw += cursorOffset.x * cursorSensitivity;
        cameraPitch += cursorOffset.y * cursorSensitivity;
        if(cameraPitch > 89.0f) cameraPitch = 89.0f;
        if(cameraPitch < -89.0f) cameraPitch = -89.0f;
        camera.rotate(cameraYaw, cameraPitch, 0f);
        camera.zoom(GLFWInputController.getScroll()/10);
    }
}
