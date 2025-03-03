import org.joml.Vector2f;

import static org.lwjgl.glfw.GLFW.*;

/**
 * Test_Version01
 *
 * @author: Santiago Barreiro
 */
public class Test_Stage0 {

    // ATTRIBUTES

    // CONSTANTS

    // CONSTRUCTORS

    // GETTERS & SETTERS

    // METHODS
    public static void main(String[] args) {

        Diamond diaInstance = new Diamond();
        Camera2D camera = new Camera2D();
        Window window = Window.get();
        window.init();

        boolean running = true;
        float dt = 0;
        float bt = (float) glfwGetTime();
        float et;
        while (running) {

            window.pollEvents();

            if (dt >= 0) {
                diaInstance.update(dt);
            }

            LineRenderer.addLine(new Vector2f(0.01f, 0.01f), new Vector2f(0.01f,3));
            LineRenderer.addLine(new Vector2f(0.01f, 3), new Vector2f(4,3));
            LineRenderer.addLine(new Vector2f(4, 3), new Vector2f(4,0.01f));
            LineRenderer.addLine(new Vector2f(4, 0.01f), new Vector2f(0.01f,0.01f));
            LineRenderer.draw(camera);

            window.flushFrame();
            et = (float) glfwGetTime();
            dt = et - bt;
            bt = et;
            running = !glfwWindowShouldClose(window.getGlfwWindow());
        }

        window.close();
    }
}
