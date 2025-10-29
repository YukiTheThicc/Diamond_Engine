package stage_0;

import api.*;
import assets.Texture;
import core.Camera;
import core.GLFWWindow;
import core.GLFWInputController;
import core.glRenderer.*;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.opengl.GL;
import utils.DiaMath;
import utils.Logger;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL20.GL_ELEMENT_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL20.GL_STATIC_DRAW;
import static org.lwjgl.opengl.GL20.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL20.GL_UNSIGNED_INT;
import static org.lwjgl.opengl.GL20.glBindTexture;
import static org.lwjgl.opengl.GL20.glDrawElements;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

/**
 * Test_Version
 *
 * @author: Santiago Barreiro
 */
public class Test_Update2 {

    // ATTRIBUTES
    static DiaLogger logger;
    static GLShader textureShader;

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
        DiaWindow window = GLFWWindow.get();
        window.init(800, 600);

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
        Camera camera = new Camera(
                new Vector3f(0f, 0f, 3f),
                new Vector3f(0f, 0f, -1f),
                new Vector3f(0f, 1f, 0f)
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
        float cameraSpeed = 2.5f;
        while (running) {

            window.pollEvents();
            glClearColor(0.1f, 0.1f, 0.1f, 1.0f);
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

            if (GLFWInputController.isKeyPressed(GLFW_KEY_A)) {
                rotationZ += dt;
                System.out.println("A is pressed: " + rotationZ);
            }
            if (GLFWInputController.isKeyPressed(GLFW_KEY_D)) {
                rotationZ -= dt;
                System.out.println("D is pressed: " + rotationZ);
            }
            if (GLFWInputController.isKeyPressed(GLFW_KEY_W)) {
                rotationX += dt;
                System.out.println("W is pressed: " + rotationX);
            }
            if (GLFWInputController.isKeyPressed(GLFW_KEY_S)) {
                rotationX -= dt;
                System.out.println("S is pressed: " + rotationX);
            }

            if (GLFWInputController.isKeyPressed(GLFW_KEY_RIGHT)) camera.moveX(cameraSpeed * dt);
            if (GLFWInputController.isKeyPressed(GLFW_KEY_LEFT)) camera.moveX(-cameraSpeed * dt);
            if (GLFWInputController.isKeyPressed(GLFW_KEY_UP)) camera.moveZ(cameraSpeed * dt);
            if (GLFWInputController.isKeyPressed(GLFW_KEY_DOWN)) camera.moveZ(-cameraSpeed * dt);

            Matrix4f projection = new Matrix4f().identity();
            projection = projection.perspective((float) Math.toRadians(-90f), window.getAspectRatio(), 0.1f, 100f);
            Matrix4f view = camera.getViewMatrix();

            //model = model.rotate(rotationZ, new Vector3f(0f, 0f, 1f));
            //model = model.rotate(rotationX, new Vector3f(1f, 0f, 0f));
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
                    model.rotate(cRot * (float) i /10, 1f,0f,0f);
                    model.rotate(cRot * (float) i /10, 0f,0f,1f);
                }
                textureShader.uploadMat4f("model", model);
                glBindVertexArray(texturedVAO);
                //glDrawElements(GL_TRIANGLES, 6, GL_UNSIGNED_INT,0);
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

        int[] indices = {
                0, 1, 3, // first triangle
                1, 2, 3  // second triangle
        };

        int VBO, VAO, EBO;
        VAO = glGenVertexArrays();
        VBO = glGenBuffers();
        EBO = glGenBuffers();
        glBindVertexArray(VAO);

        glBindBuffer(GL_ARRAY_BUFFER, VBO);
        glBufferData(GL_ARRAY_BUFFER, vertices, GL_STATIC_DRAW);
        //glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, EBO);
        //glBufferData(GL_ELEMENT_ARRAY_BUFFER, indices, GL_STATIC_DRAW);

        glVertexAttribPointer(0, 3, GL_FLOAT, false, 5 * Float.BYTES, 0);
        glEnableVertexAttribArray(0);
        glVertexAttribPointer(1, 2, GL_FLOAT, false, 5 * Float.BYTES, 3 * Float.BYTES);
        glEnableVertexAttribArray(1);
//        glVertexAttribPointer(1, 3, GL_FLOAT, false, 8 * Float.BYTES, 3 * Float.BYTES);
//        glEnableVertexAttribArray(1);
//        glVertexAttribPointer(2, 2, GL_FLOAT, false, 8 * Float.BYTES, 6 * Float.BYTES);
//        glEnableVertexAttribArray(2);
        return VAO;
    }
}
