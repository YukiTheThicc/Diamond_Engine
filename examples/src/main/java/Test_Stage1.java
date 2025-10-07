import api.DiaRenderer;
import core.Camera;
import core.Window;
import core.WindowCallback;
import org.joml.Vector2f;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.GL_FALSE;
import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.glDrawArrays;
import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_STATIC_DRAW;
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
public class Test_Stage1 {

    // ATTRIBUTES
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

    public static void main(String[] args) {

        Camera camera = new Camera();
        Window window = Window.get();
        window.init();

        DiaRenderer renderer = new GLRenderer();
        renderer.init();

        Shader triangleShader = new Shader(vertexShaderSource, fragmentShaderSource);
        triangleShader.compile();

        float[] vertices = {
                -0.5f,  -0.5f,   0.0f, 1f, 0f, 0f, // left
                 0.5f,  -0.5f,   0.0f, 0f, 1f, 0f, // right
                 0.0f,   0.5f,   0.0f, 0f, 0f, 1f  // top
        };

        int VBO, VAO;
        VAO = glGenVertexArrays();
        VBO = glGenBuffers();
        glBindVertexArray(VAO);

        glBindBuffer(GL_ARRAY_BUFFER, VBO);
        glBufferData(GL_ARRAY_BUFFER, vertices, GL_DYNAMIC_DRAW);
        glVertexAttribPointer(0, 3, GL_FLOAT, false, 3 * Float.BYTES, 0);
        glEnableVertexAttribArray(0);
        glVertexAttribPointer(1, 3, GL_FLOAT, false, 3 * Float.BYTES, 3 * Float.BYTES);
        glEnableVertexAttribArray(1);
        // note that this is allowed, the call to glVertexAttribPointer registered VBO as the vertex attribute's bound vertex buffer object so afterwards we can safely unbind
        glBindBuffer(GL_ARRAY_BUFFER, 0);
        // You can unbind the VAO afterwards so other VAO calls won't accidentally modify this VAO, but this rarely happens. Modifying other
        // VAOs requires a call to glBindVertexArray anyways so we generally don't unbind VAOs (nor VBOs) when it's not directly necessary.
        glBindVertexArray(0);

        boolean running = true;
        float dt = 0;
        float bt = (float) glfwGetTime();
        float et;
        while (running) {

            //glUseProgram(shaderProgram);
            triangleShader.use();
            glBindVertexArray(VAO); // seeing as we only have a single VAO there's no need to bind it every time, but we'll do so to keep things a bit more organized
            glDrawArrays(GL_TRIANGLES, 0, 3);
            triangleShader.detach();

            window.pollEvents();
            // HALF ASSED LINE COORDENATES SO THEY APPERAR JUST INSIDE THE FRAME
            renderer.addLine(new Vector2f(0f,  0f), new Vector2f(0f,3f));
            renderer.addLine(new Vector2f(0f, 3f), new Vector2f(4f,3f));
            renderer.addLine(new Vector2f(4f, 3f), new Vector2f(4,0f));
            renderer.addLine(new Vector2f(4f, 0f), new Vector2f(0f,0f));
            renderer.addLine(new Vector2f(0f, 0f), new Vector2f(4f,3f));
            renderer.renderFrame(camera);

            if (WindowCallback.isKeyPressed(GLFW_KEY_A)) {
                System.out.println("A is pressed");
            }

            window.flushFrame();
            et = (float) glfwGetTime();
            dt = et - bt;
            bt = et;
            running = !glfwWindowShouldClose(window.getGlfwWindow());
        }

        window.close();
    }
}
