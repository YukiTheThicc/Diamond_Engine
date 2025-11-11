package core;

import org.joml.Matrix4f;

/**
 * core.RenderTarget
 *
 * @author Santiago Barreiro
 */
public class RenderTarget {

    // ATTRIBUTES
    private Camera camera;
    private float near;
    private float far;
    private float width;
    private float height;

    // CONSTRUCTORS

    public RenderTarget(Camera camera, float width, float height, float near, float far) {
        this.camera = camera;
        this.near = near;
        this.far = far;
        this.width = width;
        this.height = height;
    }


    // GETTERS & SETTERS
    public float getNear() {
        return near;
    }

    public void setNear(float near) {
        this.near = near;
    }

    public float getFar() {
        return far;
    }

    public void setFar(float far) {
        this.far = far;
    }

    public float getWidth() {
        return width;
    }

    public void setWidth(float width) {
        this.width = width;
    }

    public float getHeight() {
        return height;
    }

    public void setHeight(float height) {
        this.height = height;
    }

    public Camera getCamera() {
        return camera;
    }

    // METHODS
    public Matrix4f getTargetView() {
        return camera.getViewMatrix();
    }

    public Matrix4f getTargetProjection() {
        return camera.getPerspectiveProjection(width/height, near, far);
    }
}
