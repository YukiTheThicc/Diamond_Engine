package core.components;

import api.DiaEntityPool;
import org.joml.Vector3f;

/**
 * Transform
 *
 * @author Santiago Barreiro
 */
public class Transform implements DiaEntityPool.IDiaComponent {

    // ATTRIBUTES
    public Vector3f pos;
    public Vector3f scale;
    public Vector3f rotation;

    // CONSTRUCTORS
    public Transform(Vector3f pos, Vector3f scale, Vector3f rotation) {
        this.pos = pos;
        this.scale = scale;
        this.rotation = rotation;
    }

    // GETTERS & SETTERS


    // METHODS

    @Override
    public DiaEntityPool.IDiaComponent copy() {
        return null;
    }
}
