package stage_0;

import api.*;
import assets.Texture;
import core.Window;
import core.InputController;
import core.glRenderer.*;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.opengl.GL;
import utils.Logger;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_A;
import static org.lwjgl.glfw.GLFW.glfwGetTime;
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
                    "layout (location = 1) in vec3 aCol;\n" +
                    "layout (location = 2) in vec2 aTexCoords;\n" +
                    "out vec3 fCol;\n" +
                    "out vec2 texCoords;\n" +
                    "uniform mat4 transform;\n" +
                    "void main()\n" +
                    "{\n" +
                    "   gl_Position = transform * vec4(aPos, 1.0);\n" +
                    "   fCol = aCol;\n" +
                    "   texCoords = vec2(aTexCoords.x, aTexCoords.y);\n" +
                    "}\0";
    static final String fragmentShaderTextureSource =
            "#version 330 core\n" +
                    "in vec3 fCol;\n" +
                    "in vec2 texCoords;\n" +
                    "out vec4 FragColor;\n" +
                    "uniform sampler2D sampledTexture;\n" +
                    "void main()\n" +
                    "{\n" +
                    "   FragColor = texture(sampledTexture, texCoords) * vec4(fCol, 1.0);\n" +
                    "}\n\0";

    public static void main(String[] args) {

        logger = new Logger();
        InputController inputcontroller = new InputController();
        DiaWindow window = Window.get();
        window.init(800, 600, inputcontroller);

        GL.createCapabilities();
        glDisable(GL_BLEND);
        glBlendFunc(GL_ONE, GL_ONE_MINUS_SRC_ALPHA);

        DiaAssetLoader assetLoader = new GLAssetLoader(logger);
        int texturedVAO = setupTexturedExample();

        String exampleTexture = System.getProperty("user.dir") + "\\examples\\res\\top.png";
        System.out.println("Attempting to load example texture from = " + exampleTexture + "...");
        Texture texture = assetLoader.loadTexture(exampleTexture);

        Matrix4f trans;

        boolean running = true;
        float time;

        while (running) {

            window.pollEvents();

            glClearColor(0.1f, 0.1f, 0.1f, 1.0f);
            glClear(GL_COLOR_BUFFER_BIT);

            time = (float) glfwGetTime();
            trans = new Matrix4f().identity();
            trans = trans.rotate(time, new Vector3f(0f,0f,1f));
            trans = trans.rotate(time, new Vector3f(0f,1f,0f));

            glBindTexture(GL_TEXTURE_2D, texture.getId());
            textureShader.use();
            textureShader.uploadMat4f("transform", trans);
            glBindVertexArray(texturedVAO);
            glDrawElements(GL_TRIANGLES, 6, GL_UNSIGNED_INT,0);
            textureShader.detach();

            if (inputcontroller.isKeyPressed(GLFW_KEY_A)) {
                System.out.println("A is pressed");
            }
            window.refresh();
            inputcontroller.refresh();
            running = window.isOpen();
        }

        window.close();
    }

    private static int setupTexturedExample() {
        textureShader = new GLShader(vertexShaderTextureSource, fragmentShaderTextureSource);
        textureShader.compile(logger);

        float[] vertices = {
                0.5f,  0.5f, 0.0f,   1.0f, 0.0f, 0.0f,   1.0f, 1.0f, // top right
                0.5f, -0.5f, 0.0f,   0.0f, 1.0f, 0.0f,   1.0f, 0.0f, // bottom right
                -0.5f, -0.5f, 0.0f,   0.0f, 0.0f, 1.0f,   0.0f, 0.0f, // bottom left
                -0.5f,  0.5f, 0.0f,   1.0f, 1.0f, 0.0f,   0.0f, 1.0f  // top left
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
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, EBO);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, indices, GL_STATIC_DRAW);

        glVertexAttribPointer(0, 3, GL_FLOAT, false, 8 * Float.BYTES, 0);
        glEnableVertexAttribArray(0);
        glVertexAttribPointer(1, 3, GL_FLOAT, false, 8 * Float.BYTES, 3 * Float.BYTES);
        glEnableVertexAttribArray(1);
        glVertexAttribPointer(2, 2, GL_FLOAT, false, 8 * Float.BYTES, 6 * Float.BYTES);
        glEnableVertexAttribArray(2);
        return VAO;
    }
}
