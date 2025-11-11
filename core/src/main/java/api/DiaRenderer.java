package api;

import core.Camera;
import core.RenderTarget;
import core.glRenderer.GLRenderer;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;

/**
 * DiaRenderer
 * Main engine renderer API. The engine has to be injected with a renderer implementing this class
 * @author Santiago Barreiro
 */
public interface DiaRenderer {

    /**
     * Initialization of the renderer. Renderer should not attempt to render without being initialized
     */
    void init();

    /**
     * Add line (immediate mode) to render this frame
     * @param from First vertex of the line
     * @param to Second vertex of the line
     */
    void addLine(Vector2f from, Vector2f to);
    void addLine(Vector2f from, Vector2f to, Vector3f color);
    void addLine(Vector3f from, Vector3f to);
    void addLine(Vector3f from, Vector3f to, Vector3f color);
    
    /**
     * Render the current frame from the provided render target
     */
    void renderFrame(RenderTarget target);

    void renderFrame(Matrix4f view, Matrix4f projection);

    /**
     * Sets the size of the rendering viewport
     * @param width Width of the new viewport
     * @param height Height of the new viewport
     */
    void setViewPort(int width, int height);
}
