package api;

import org.joml.Vector2f;

/**
 * DiaRenderer
 *
 * @author Santiago Barreiro
 */
public interface DiaRenderer {

    public void init();

    public void startFrame();

    public void finishFrame();

    public void addLine(Vector2f from, Vector2f to);
}
