package core.components;

import api.DiaEntityPool.IDiaComponent;
import core.assets.Texture;

/**
 * Model
 *
 * @author Santiago Barreiro
 */
public class Mesh implements IDiaComponent {

    // ATTRIBUTES
    private float[] vertices;
    private Texture texture;

    // CONSTRUCTORS
    public Mesh(float[] vertices, Texture texture) {
        this.vertices = vertices;
        this.texture = texture;
    }

    public float[] getVertices() {
        return vertices;
    }

    public void setVertices(float[] vertices) {
        this.vertices = vertices;
    }

    public Texture getTexture() {
        return texture;
    }

    public void setTexture(Texture texture) {
        this.texture = texture;
    }

    @Override
    public IDiaComponent copy() {
        return null;
    }
}
