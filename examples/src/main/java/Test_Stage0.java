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

        Camera2D camera = new Camera2D();
        Window window = Window.get();
        window.init();

        boolean running = true;
        float dt = 0;
        float bt = (float) glfwGetTime();
        float et;
        while (running) {

            window.pollEvents();
            // HALF ASSED LINE COORDENATES SO THEY APPERAR JUST INSIDE THE FRAME
            GLRenderer.addLine(new Vector2f(0f,  0f), new Vector2f(0f,3f));
            GLRenderer.addLine(new Vector2f(0f, 3f), new Vector2f(4f,3f));
            GLRenderer.addLine(new Vector2f(4f, 3f), new Vector2f(4,0f));
            GLRenderer.addLine(new Vector2f(4f, 0f), new Vector2f(0f,0f));
            GLRenderer.addLine(new Vector2f(0f, 0f), new Vector2f(4f,3f));
            GLRenderer.draw(camera);

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
