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

            window.flushFrame();
            et = (float) glfwGetTime();
            dt = et - bt;
            bt = et;
            running = !glfwWindowShouldClose(window.getGlfwWindow());
        }

        window.close();
    }
}
