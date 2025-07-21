import org.joml.Vector2f;

import static org.lwjgl.glfw.GLFW.*;

/**
 * Test_Version01
 *
 * @author: Santiago Barreiro
 */
public class Test_Stage1 {

    // ATTRIBUTES
    private static final String vertex =
            "#version 330 core\n" +
                    "layout (location=0) in vec3 attrPos;\n" +
                    "layout (location=1) in vec3 attrColor;\n" +
                    "uniform mat4 uProjection;\n" +
                    "uniform mat4 uView;\n" +
                    "uniform int uType;\n" +
                    "out vec3 fragColor;\n" +
                    "out int type;\n" +
                    "void main() {\n" +
                    "    fragColor = attrColor;\n" +
                    "    type = uType;\n" +
                    "    gl_Position = uProjection * uView * vec4(attrPos, 1.0);\n" +
                    "}";
    private static final String fragment =
            "#version 330 core\n" +
                    "in vec3 fragColor;\n" +
                    "out vec4 color;\n" +
                    "void main() {\n" +
                    "    color = vec4(fragColor, 1);\n" +
                    "}";

    // CONSTANTS

    // CONSTRUCTORS

    // GETTERS & SETTERS

    // METHODS
    public static void main(String[] args) {

        Camera2D camera = new Camera2D();
        Window window = Window.get();
        window.init();

        boolean running = true;
        float dt = 0;
        float bt = (float) glfwGetTime();
        float et;
        while (running) {

            window.pollEvents();
            // Half-assed test. Draws lines just inside the projection area
            LineRenderer.addLine(new Vector2f(0.01f, 0.01f), new Vector2f(0.01f,3));
            LineRenderer.addLine(new Vector2f(0.01f, 3), new Vector2f(4,3));
            LineRenderer.addLine(new Vector2f(4, 3), new Vector2f(4,0.01f));
            LineRenderer.addLine(new Vector2f(4, 0.01f), new Vector2f(0.01f,0.01f));
            LineRenderer.addLine(new Vector2f(0.01f, 0.01f), new Vector2f(8,6));


            // The added lines are drawn
            LineRenderer.draw(camera);

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
