package api;

import core.Camera;
import org.joml.Vector2f;

/**
 * DiaRenderer
 * Main engine renderer API. The engine has to be injected with a renderer implementing this class
 * @author Santiago Barreiro
 */
public interface DiaRenderer {

    /**
     * Initialization of the renderer. Renderer should not attempt to render without being initialized
     */
    public void init();

    /**
     * Add line (immediate mode) to render this frame
     * @param from First vertex of the line
     * @param to Second vertex of the line
     */
    public void addLine(Vector2f from, Vector2f to);

    /**
     * Render the current frame. Needs a camera
     */
    public void renderFrame(Camera camera);

    /**
     * Sets the size of the rendering viewport
     * @param width Width of the new viewport
     * @param height Height of the new viewport
     */
    public void setViewPort(int width, int height);
}
