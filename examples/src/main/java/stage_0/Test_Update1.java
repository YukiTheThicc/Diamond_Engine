package stage_0;

import api.*;
import assets.Texture;
import core.Window;
import core.InputController;
import core.glRenderer.*;
import org.lwjgl.opengl.GL;
import utils.Logger;

import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.glDrawArrays;
import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.glBindBuffer;
import static org.lwjgl.opengl.GL15.glBufferData;
import static org.lwjgl.opengl.GL15.glGenBuffers;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

/**
 * Test_Version
 *
 * @author: Santiago Barreiro
 */
public class Test_Update1 {

    // ATTRIBUTES
    static DiaLogger logger;
    static GLShader triangleShader;
    static GLShader textureShader;

    static final String vertexShaderSource =
            "#version 330 core\n" +
                    "layout (location = 0) in vec3 aPos;\n" +
                    "layout (location = 1) in vec3 aCol;\n" +
                    "out vec3 fCol;\n" +
                    "void main()\n" +
                    "{\n" +
                    "   fCol = aCol;\n" +
                    "   gl_Position = vec4(aPos.x, aPos.y, aPos.z, 1.0);\n" +
                    "}\0";
    static final String fragmentShaderSource =
            "#version 330 core\n" +
                    "in vec3 fCol;\n" +
                    "out vec4 FragColor;\n" +
                    "void main()\n" +
                    "{\n" +
                    "   FragColor = vec4(fCol, 1);\n" +
                    "}\n\0";

    static final String vertexShaderTextureSource =
            "#version 330 core\n" +
                    "layout (location = 0) in vec3 aPos;\n" +
                    "layout (location = 1) in vec3 aCol;\n" +
                    "layout (location = 2) in vec2 aTexCoords;\n" +
                    "out vec3 fCol;\n" +
                    "out vec2 texCoords;\n" +
                    "void main()\n" +
                    "{\n" +
                    "   gl_Position = vec4(aPos, 1.0);\n" +
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


        InputController inputcontroller = new InputController();
        DiaWindow window = Window.get();
        window.init(800, 600, inputcontroller);

        GL.createCapabilities();
        glDisable(GL_BLEND);
        glBlendFunc(GL_ONE, GL_ONE_MINUS_SRC_ALPHA);

        DiaLogger logger = new Logger();
        DiaAssetLoader assetLoader = new GLAssetLoader(logger);
        int VAO = setupTriangle();
        int texturedVAO = setupTexturedExample();

        String exampleTexture = System.getProperty("user.dir") + "\\examples\\res\\top.png";
        System.out.println("Attempting to load example texture from = " + exampleTexture + "...");
        Texture texture = assetLoader.loadTexture(exampleTexture);

        boolean running = true;
        while (running) {

            window.pollEvents();

            glClearColor(0.1f, 0.1f, 0.1f, 1.0f);
            glClear(GL_COLOR_BUFFER_BIT);

            triangleShader.use();
            glBindVertexArray(VAO); // seeing as we only have a single VAO there's no need to bind it every time, but we'll do so to keep things a bit more organized
            glDrawArrays(GL_TRIANGLES, 0, 3);
            triangleShader.detach();

            glBindTexture(GL_TEXTURE_2D, texture.getId());
            textureShader.use();
            glBindVertexArray(texturedVAO);
            glDrawElements(GL_TRIANGLES, 6, GL_UNSIGNED_INT,0);
            textureShader.detach();

            window.refresh();
            inputcontroller.refresh();
            running = window.isOpen();
        }

        window.close();
    }

    private static int setupTriangle() {
        triangleShader = new GLShader(vertexShaderSource, fragmentShaderSource);
        triangleShader.compile(logger);

        float[] vertices = {
                -1f,    -0.5f,   0.0f, 1f, 0f, 0f,  // left
                0f,     -0.5f,   0.0f, 0f, 1f, 0f,  // right
                -0.5f,   0.5f,   0.0f, 0f, 0f, 1f  // top
        };

        int VBO, VAO;
        VAO = glGenVertexArrays();
        VBO = glGenBuffers();
        glBindVertexArray(VAO);

        glBindBuffer(GL_ARRAY_BUFFER, VBO);
        glBufferData(GL_ARRAY_BUFFER, vertices, GL_STATIC_DRAW);
        glVertexAttribPointer(0, 3, GL_FLOAT, false, 6 * Float.BYTES, 0);
        glEnableVertexAttribArray(0);
        glVertexAttribPointer(1, 3, GL_FLOAT, false, 6 * Float.BYTES, 3 * Float.BYTES);
        glEnableVertexAttribArray(1);
        return VAO;
    }

    private static int setupTexturedExample() {
        textureShader = new GLShader(vertexShaderTextureSource, fragmentShaderTextureSource);
        textureShader.compile(logger);

        float[] vertices = {
                1f,  0.5f, 0.0f,   1.0f, 0.0f, 0.0f,   1.0f, 1.0f, // top right
                1f, -0.5f, 0.0f,   0.0f, 1.0f, 0.0f,   1.0f, 0.0f, // bottom right
                0f, -0.5f, 0.0f,   0.0f, 0.0f, 1.0f,   0.0f, 0.0f, // bottom left
                0f,  0.5f, 0.0f,   1.0f, 1.0f, 0.0f,   0.0f, 1.0f  // top left
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
