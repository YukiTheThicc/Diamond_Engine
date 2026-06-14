package stage_0;

import api.*;
import core.assets.Texture;
import core.Camera;
import core.GLFWWindow;
import core.GLFWInputController;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.lwjgl.opengl.GL;
import core.glRenderer.GLAssetLoader;
import core.glRenderer.GLShader;
import utils.DiaMath;
import utils.Logger;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL20.GL_STATIC_DRAW;
import static org.lwjgl.opengl.GL20.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL20.glBindTexture;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

/**
 * Test_Version
 * IMPLEMENTS A SIMPLE 3D SCENE WITH CUBES, SOME ROTATING SOME STATIC. THE CAMERA CAN BE MOVED WITH THE MOUSE AND WASD
 *
 * @author: Santiago Barreiro
 */
public class Test_Update2 {

    // ATTRIBUTES
    static DiaLogger logger;
    static DiaWindow window;
    static GLShader textureShader;
    static Camera camera;
    static float cameraSpeed = 2.5f;
    static float cameraYaw = -90f;
    static float cameraPitch = 0f;
    static float cursorSensitivity = 0.1f;
    static boolean captureMouse = true;

    static final String vertexShaderTextureSource =
            "#version 330 core\n" +
                    "layout (location = 0) in vec3 aPos;\n" +
                    "layout (location = 1) in vec2 aTexCoords;\n" +
                    "out vec2 texCoords;\n" +
                    "uniform mat4 model;\n" +
                    "uniform mat4 view;\n" +
                    "uniform mat4 projection;\n" +
                    "void main()\n" +
                    "{\n" +
                    "   gl_Position = projection * view * model * vec4(aPos, 1.0);\n" +
                    "   texCoords = vec2(aTexCoords.x, aTexCoords.y);\n" +
                    "}\0";
    static final String fragmentShaderTextureSource =
            "#version 330 core\n" +
                    "in vec2 texCoords;\n" +
                    "out vec4 FragColor;\n" +
                    "uniform sampler2D sampledTexture;\n" +
                    "void main()\n" +
                    "{\n" +
                    "   FragColor = texture(sampledTexture, texCoords);\n" +
                    "}\n\0";

    public static void main(String[] args) {

        logger = new Logger();
        window = GLFWWindow.get();
        window.init(800, 600);
        GLFWInputController.init(800, 600);

        GL.createCapabilities();
        glEnable(GL_DEPTH_TEST);
        glDisable(GL_BLEND);
        glBlendFunc(GL_ONE, GL_ONE_MINUS_SRC_ALPHA);

        DiaAssetLoader assetLoader = new GLAssetLoader(logger);
        int texturedVAO = setupTexturedExample();

        String exampleTexture = System.getProperty("user.dir") + "\\examples\\res\\top.png";
        System.out.println("Attempting to load example texture from = " + exampleTexture + "...");
        Texture texture = assetLoader.loadTexture(exampleTexture);

        // Add resize listener for the window to adjust viewport
        window.addResizeObserver(new DiaWindow.ResizeObserver() {
            @Override
            public void adjustSize(int width, int height) {
                glViewport(0, 0, width, height);
            }
        });


        boolean running = true;
        float rotationZ = 0f;
        float rotationX = 0f;
        camera = new Camera(
                new Vector3f(0f, 0f, -1f),
                new Vector3f(0f, 1f, 0f),
                90f,
                4f,
                0.75f
        );

        float bt = (float) glfwGetTime();
        float et = (float) glfwGetTime();
        float dt = 0f;
        Vector3f[] positions = {
            new Vector3f(DiaMath.randomFloat(-3f, 3f), DiaMath.randomFloat(-3f, 3f), DiaMath.randomFloat(-2f, -12f)),
            new Vector3f(DiaMath.randomFloat(-3f, 3f), DiaMath.randomFloat(-3f, 3f), DiaMath.randomFloat(-2f, -12f)),
            new Vector3f(DiaMath.randomFloat(-3f, 3f), DiaMath.randomFloat(-3f, 3f), DiaMath.randomFloat(-2f, -12f)),
            new Vector3f(DiaMath.randomFloat(-3f, 3f), DiaMath.randomFloat(-3f, 3f), DiaMath.randomFloat(-2f, -12f)),
            new Vector3f(DiaMath.randomFloat(-3f, 3f), DiaMath.randomFloat(-3f, 3f), DiaMath.randomFloat(-2f, -12f)),
            new Vector3f(DiaMath.randomFloat(-3f, 3f), DiaMath.randomFloat(-3f, 3f), DiaMath.randomFloat(-2f, -12f)),
            new Vector3f(DiaMath.randomFloat(-3f, 3f), DiaMath.randomFloat(-3f, 3f), DiaMath.randomFloat(-2f, -12f)),
            new Vector3f(DiaMath.randomFloat(-3f, 3f), DiaMath.randomFloat(-3f, 3f), DiaMath.randomFloat(-2f, -12f)),
            new Vector3f(DiaMath.randomFloat(-3f, 3f), DiaMath.randomFloat(-3f, 3f), DiaMath.randomFloat(-2f, -12f)),
            new Vector3f(DiaMath.randomFloat(-3f, 3f), DiaMath.randomFloat(-3f, 3f), DiaMath.randomFloat(-2f, -12f))
        };
        float cRot = 0f;

        while (running) {
            window.pollEvents();
            glClearColor(0.1f, 0.1f, 0.1f, 1.0f);
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

            processControls(dt);
            Matrix4f projection = camera.getPerspectiveProjection(window.getAspectRatio(), 0.1f, 100f);
            Matrix4f view = camera.getViewMatrix();
            cRot += dt;
            glBindTexture(GL_TEXTURE_2D, texture.getId());
            textureShader.use();
            textureShader.uploadMat4f("view", view);
            textureShader.uploadMat4f("projection", projection);
            for (int i = 0; i < positions.length; i++) {
                Matrix4f model = new Matrix4f().identity();
                model = model.translate(positions[i]);
                if (i == 0) {
                    model.rotate(rotationX, 1f,0f,0f);
                    model.rotate(rotationZ, 0f,0f,1f);
                }
                if (i % 3 == 1) {
                    model.rotate(cRot * (float) i / 9, 1f,0f,0f);
                    model.rotate(cRot * (float) i / 9, 0f,0f,1f);
                }
                textureShader.uploadMat4f("model", model);
                glBindVertexArray(texturedVAO);
                glDrawArrays(GL_TRIANGLES, 0, 36);
            }
            textureShader.detach();

            et = (float) glfwGetTime();
            dt = et - bt;
            bt = et;

            window.refresh();
            GLFWInputController.refresh();
            running = window.isOpen();
        }

        window.close();
    }

    public static void processControls(float dt) {
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

    private static int setupTexturedExample() {
        textureShader = new GLShader(vertexShaderTextureSource, fragmentShaderTextureSource);
        textureShader.compile(logger);

        float[] vertices = {
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

        int VBO, VAO;
        VAO = glGenVertexArrays();
        VBO = glGenBuffers();
        glBindVertexArray(VAO);

        glBindBuffer(GL_ARRAY_BUFFER, VBO);
        glBufferData(GL_ARRAY_BUFFER, vertices, GL_STATIC_DRAW);

        glVertexAttribPointer(0, 3, GL_FLOAT, false, 5 * Float.BYTES, 0);
        glEnableVertexAttribArray(0);
        glVertexAttribPointer(1, 2, GL_FLOAT, false, 5 * Float.BYTES, 3 * Float.BYTES);
        glEnableVertexAttribArray(1);
        return VAO;
    }
}
