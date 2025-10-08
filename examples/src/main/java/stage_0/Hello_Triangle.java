package stage_0;

import api.*;
import core.*;
import core.glRenderer.GLRenderer;
import utils.Logger;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.glViewport;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;

/**
 * stage_0.Hello_Triangle
 *
 * @author Santiago Barreiro
 */
public class Hello_Triangle {

    static final String vertexShaderSource =
            "#version 330 core\n" +
            "layout (location = 0) in vec3 aPos;\n" +
            "void main()\n" +
            "{\n" +
            "   gl_Position = vec4(aPos.x, aPos.y, aPos.z, 1.0);\n" +
            "}\0";
    static final String fragmentShaderSource =
            "#version 330 core\n" +
            "out vec4 FragColor;\n" +
            "void main()\n" +
            "{\n" +
            "   FragColor = vec4(1.0f, 0.5f, 0.2f, 1.0f);\n" +
            "}\n\0";

    public static void main(String[] args) {

        DiaLogger logger = new Logger();
        Window window = Window.get();
        window.init();

        DiaRenderer renderer = new GLRenderer(logger);
        renderer.init();

        int vertexShader = glCreateShader(GL_VERTEX_SHADER);
        glShaderSource(vertexShader, vertexShaderSource);
        glCompileShader(vertexShader);
        // check for shader compile errors
        if (glGetShaderi(vertexShader, GL_COMPILE_STATUS) == GL_FALSE) {
            int length = glGetShaderi(vertexShader, GL_INFO_LOG_LENGTH);
            System.out.println(glGetShaderInfoLog(vertexShader, length));
        }

        // fragment shader
        int fragmentShader = glCreateShader(GL_FRAGMENT_SHADER);
        glShaderSource(fragmentShader, fragmentShaderSource);
        glCompileShader(fragmentShader);
        // check for shader compile errors
        if (glGetShaderi(fragmentShader, GL_COMPILE_STATUS) == GL_FALSE) {
            int length = glGetShaderi(fragmentShader, GL_INFO_LOG_LENGTH);
            System.out.println(glGetShaderInfoLog(fragmentShader, length));
        }

        // link shaders
        int shaderProgram = glCreateProgram();
        glAttachShader(shaderProgram, vertexShader);
        glAttachShader(shaderProgram, fragmentShader);
        glLinkProgram(shaderProgram);
        // check for linking errors
        if (glGetProgrami(shaderProgram, GL_LINK_STATUS) == GL_FALSE) {
            int length = glGetProgrami(shaderProgram, GL_INFO_LOG_LENGTH);
            System.out.println(glGetProgramInfoLog(shaderProgram, length));
        }
        glDeleteShader(vertexShader);
        glDeleteShader(fragmentShader);

        float[] vertices = {
                -0.5f, -0.5f, 0.0f, // left
                0.5f, -0.5f, 0.0f, // right
                0.0f,  0.5f, 0.0f  // top
        };

        int VBO, VAO;
        VAO = glGenVertexArrays();
        VBO = glGenBuffers();
        // bind the Vertex Array Object first, then bind and set vertex buffer(s), and then configure vertex attributes(s).
        glBindVertexArray(VAO);

        glBindBuffer(GL_ARRAY_BUFFER, VBO);
        glBufferData(GL_ARRAY_BUFFER, vertices, GL_STATIC_DRAW);

        glVertexAttribPointer(0, 3, GL_FLOAT, false, 3 * Float.BYTES, 0);
        glEnableVertexAttribArray(0);

        // note that this is allowed, the call to glVertexAttribPointer registered VBO as the vertex attribute's bound vertex buffer object so afterwards we can safely unbind
        glBindBuffer(GL_ARRAY_BUFFER, 0);

        // You can unbind the VAO afterwards so other VAO calls won't accidentally modify this VAO, but this rarely happens. Modifying other
        // VAOs requires a call to glBindVertexArray anyways so we generally don't unbind VAOs (nor VBOs) when it's not directly necessary.
        glBindVertexArray(0);

        while (!glfwWindowShouldClose(window.getGlfwWindow())) {
            // input
            // -----
            processInput(window.getGlfwWindow());

            // render
            // ------
            glClearColor(0.2f, 0.3f, 0.3f, 1.0f);
            glClear(GL_COLOR_BUFFER_BIT);

            // draw our first triangle
            glUseProgram(shaderProgram);
            glBindVertexArray(VAO); // seeing as we only have a single VAO there's no need to bind it every time, but we'll do so to keep things a bit more organized
            glDrawArrays(GL_TRIANGLES, 0, 3);
            // glBindVertexArray(0); // no need to unbind it every time

            // glfw: swap buffers and poll IO events (keys pressed/released, mouse moved etc.)
            // -------------------------------------------------------------------------------
            glfwSwapBuffers(window.getGlfwWindow());
            glfwPollEvents();
        }

        // optional: de-allocate all resources once they've outlived their purpose:
        // ------------------------------------------------------------------------
        glDeleteVertexArrays(VAO);
        glDeleteBuffers(VBO);
        glDeleteProgram(shaderProgram);

        // glfw: terminate, clearing all previously allocated GLFW resources.
        // ------------------------------------------------------------------
        window.close();
        return;
    }

    static void processInput(long window)  {
        if (glfwGetKey(window, GLFW_KEY_ESCAPE) == GLFW_PRESS)
            glfwSetWindowShouldClose(window, true);
    }

    static void framebuffer_size_callback(long window, int width, int height){
        glViewport(0, 0, width, height);
    }
}
