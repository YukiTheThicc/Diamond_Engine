package stage_0;

import api.DiaRenderer;
import api.DiaWindow;
import core.Camera;
import core.glRenderer.GLInputMapper;
import core.glRenderer.GLWindow;
import core.glRenderer.GLRenderer;
import org.joml.Vector2f;
import utils.Logger;

import static org.lwjgl.glfw.GLFW.*;

/**
 * Test_Version 0
 *
 * @author: Santiago Barreiro
 */
public class Test_Update0 {



    // ATTRIBUTES

    // CONSTANTS

    // CONSTRUCTORS

    // GETTERS & SETTERS

    // METHODS
    public static void main(String[] args) {

        Logger logger = new Logger();
        Camera camera = new Camera();
        DiaWindow window = GLWindow.get();
        window.init(new GLInputMapper());

        DiaRenderer renderer = new GLRenderer(logger);
        renderer.init();

        float dt = 0;
        float bt = (float) glfwGetTime();
        float et;
        while (window.isOpen()) {

            window.pollEvents();
            // HALF ASSED LINE COORDENATES SO THEY APPERAR JUST INSIDE THE FRAME
            renderer.addLine(new Vector2f(0f,  0f), new Vector2f(0f,3f));
            renderer.addLine(new Vector2f(0f, 3f), new Vector2f(4f,3f));
            renderer.addLine(new Vector2f(4f, 3f), new Vector2f(4,0f));
            renderer.addLine(new Vector2f(4f, 0f), new Vector2f(0f,0f));
            renderer.addLine(new Vector2f(0f, 0f), new Vector2f(4f,3f));
            renderer.renderFrame(camera);

            window.refresh();
            et = (float) glfwGetTime();
            dt = et - bt;
            bt = et;
        }
        window.close();
    }
}
