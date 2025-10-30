package core;

import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;

/**
 * Camera2D
 *
 * @author: Santiago Barreiro
 */
public class Camera {

    // ATTRIBUTES
    private Vector3f pos;
    private Vector3f front;
    private Vector3f up;
    private float zoom, maxZoom, minZoom;

    // CONSTRUCTORS

    /**
     * Creates a camera on 3D space with a specific location, target and up vectors
     * @param pos Starting position of the camera
     * @param front Position at which the camera will be pointing at
     * @param up The up vector of the camera
     */
    public Camera(Vector3f pos, Vector3f front, Vector3f up, float maxZoom, float minZoom) {
        this.pos = pos;
        this.front = front;
        this.up = up;
        this.maxZoom = maxZoom;
        this.minZoom = minZoom;
        this.zoom = 1f;
    }

    // METHODS
    public Matrix4f getViewMatrix() {
        return new Matrix4f().lookAt(pos, new Vector3f(pos).add(front), up);
    }

    public void moveTo(Vector3f destination) {
        this.pos = destination;
    }

    public void move(float displacementX, float displacementY, float displacementZ) {
        this.pos.add(displacementX, displacementY, displacementZ);
    }

    public void moveX(float displacement) {
        this.pos.x += displacement;
    }

    public void moveY(float displacement) {
        this.pos.y += displacement;
    }

    public void moveZ(float displacement) {
        this.pos.z += displacement;
    }

    public void zoomIn(float increase) {
        zoom += increase;
        if (zoom > maxZoom) zoom = maxZoom;
    }

    public void zoomOut(float decrease) {
        zoom -= decrease;
        if (zoom < minZoom) zoom = minZoom;
    }
}
