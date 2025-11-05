package stage_0;

import api.DiaRenderer;
import api.DiaWindow;
import core.GLFWWindow;
import renderer.GLRenderer;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import utils.Logger;

import static org.lwjgl.opengl.GL11.glViewport;

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

        DiaWindow window = GLFWWindow.get();
        window.init(800, 600);
        DiaRenderer renderer = new GLRenderer(new Logger());
        renderer.init();

        // Add resize listener for the window to adjust viewport
        window.addResizeObserver(new DiaWindow.ResizeObserver() {
            @Override
            public void adjustSize(int width, int height) {
                glViewport(0, 0, width, height);
            }
        });


        while (window.isOpen()) {
            window.pollEvents();
            // HALF ASSED LINE COORDENATES SO THEY APPERAR JUST INSIDE THE FRAME
            renderer.addLine(new Vector2f(0f,  0f), new Vector2f(0f,3f));
            renderer.addLine(new Vector2f(0f, 3f), new Vector2f(4f,3f));
            renderer.addLine(new Vector2f(4f, 3f), new Vector2f(4,0f));
            renderer.addLine(new Vector2f(4f, 0f), new Vector2f(0f,0f));
            renderer.addLine(new Vector2f(0f, 0f), new Vector2f(4f,3f));
            renderer.renderFrame(new Matrix4f().identity(), new Matrix4f().identity());
            window.refresh();
        }
        window.close();
    }
}
