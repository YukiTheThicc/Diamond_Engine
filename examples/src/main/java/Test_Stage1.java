import api.DiaLogger;
import api.DiaRenderer;
import core.Camera;
import core.Window;
import core.WindowCallback;
import org.joml.Vector2f;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;


/**
 * Test_Version
 *
 * @author: Santiago Barreiro
 */
public class Test_Stage1 {

    // ATTRIBUTES
    private static final String triangleVertex =
            "#version 330 core\n" +
                    "layout (location = 0) in vec3 aPos;\n" +
                    "uniform mat4 uProjection;\n" +
                    "uniform mat4 uView;\n" +
                    "void main()\n" +
                    "{\n" +
                    "    gl_Position = uProjection * uView * vec4(aPos, 1.0);\n" +
                    "}\0";
    private static final String triangleFragment =
            "#version 330 core\n" +
                    "out vec4 color;\n" +
                    "void main() {\n" +
                    "    color = vec4(1.0f, 0.5f, 0.2f, 1.0f);\n" +
                    "}\n\0";

    // CONSTANTS

    // CONSTRUCTORS

    // GETTERS & SETTERS

    // METHODS
    public static void main(String[] args) {

        Camera camera = new Camera();
        Window window = Window.get();
        window.init();

        DiaLogger logger = new Logger();
        DiaRenderer renderer = new GLRenderer(logger);
        renderer.init();

        // Set up hello triangle VAO
        float[] vertices = {
                2f, 2f, -11f,
                3f, 2f, -11f,
                2.5f, 3f, -11f
        };

        int triangleVboID;
        triangleVboID = glGenBuffers();
        int vao;
        vao = glGenVertexArrays();
        glBindVertexArray(vao);
        glBindBuffer(GL_ARRAY_BUFFER, triangleVboID);
        glBufferData(GL_ARRAY_BUFFER, vertices, GL_STATIC_DRAW);
        glVertexAttribPointer(0, 3, GL_FLOAT, false, 3*Float.BYTES, 0);

        Shader triangleShader = new Shader(triangleVertex, triangleFragment);
        triangleShader.uploadMat4f("uProjection", camera.getProjMatrix());
        triangleShader.uploadMat4f("uView", camera.getViewMatrix());
        triangleShader.compile(logger);

        glBindBuffer(GL_ARRAY_BUFFER, 0);
        glBindVertexArray(0);

        glPolygonMode(GL_FRONT_AND_BACK, GL_LINE);

        boolean running = true;
        float dt = 0;
        float bt = (float) glfwGetTime();
        float et;
        while (running) {

            window.pollEvents();
            glClearColor(0.1f, 0.15f, 0.15f, 1.0f);
            glClear(GL_COLOR_BUFFER_BIT);

            triangleShader.use();
            glBindVertexArray(vao);
            glDrawArrays(GL_TRIANGLES, 0,3);
            triangleShader.detach();

            // HALF ASSED LINE COORDENATES SO THEY APPERAR JUST INSIDE THE FRAME
            renderer.addLine(new Vector2f(0f, 0f), new Vector2f(0f, 3f));
            renderer.addLine(new Vector2f(0f, 3f), new Vector2f(4f, 3f));
            renderer.addLine(new Vector2f(4f, 3f), new Vector2f(4, 0f));
            renderer.addLine(new Vector2f(4f, 0f), new Vector2f(0f, 0f));
            renderer.addLine(new Vector2f(0f, 0f), new Vector2f(4f, 3f));

            //renderer.renderFrame(camera);

            if (WindowCallback.isKeyPressed(GLFW_KEY_A)) {
                System.out.println("A is pressed");
            }

            window.flushFrame();
            et = (float) glfwGetTime();
            dt = et - bt;
            bt = et;
            running = !glfwWindowShouldClose(window.getGlfwWindow());
        }

        glDeleteVertexArrays(vao);
        glDeleteVertexArrays(triangleVboID);


        window.close();
    }
}
