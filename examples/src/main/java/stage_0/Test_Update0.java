package stage_0;

import api.DiaRenderer;
import api.DiaWindow;
import core.Camera;
import core.InputController;
import core.Window;
import core.glRenderer.GLRenderer;
import org.joml.Vector2f;
import utils.Logger;

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

        DiaWindow window = Window.get();
        window.init(800, 600, new InputController());
        Camera camera = new Camera();
        DiaRenderer renderer = new GLRenderer(new Logger());
        renderer.init();

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
        }
        window.close();
    }
}
