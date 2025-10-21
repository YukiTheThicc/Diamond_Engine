package api;

import assets.Texture;
import core.Camera;
import org.joml.Vector2f;

/**
 * api.DiaRenderer
 *
 * @author Santiago Barreiro
 */
public interface DiaRenderer {

    /**
     * Initialization of the renderer. Renderer should not attempt to render without being initialized
     */
    public void init();

    /**
     * Add line to render this frame
     * @param from First vertex of the line
     * @param to Second vertex of the line
     */
    public void addLine(Vector2f from, Vector2f to);

    /**
     * Frame rendering process
     */
    public void renderFrame(Camera camera);
}
