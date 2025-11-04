package core;

import org.joml.Matrix4f;
import org.joml.Vector3f;

import static java.lang.Math.toRadians;
import static java.lang.Math.sin;
import static java.lang.Math.cos;

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
    private float fov = 45f;
    private float zoom;
    private final float maxZoom;
    private final float minZoom;

    // CONSTRUCTORS

    /**
     * Creates a camera on 3D space with a specific location, target and up vectors
     * @param front Position at which the camera will be pointing at
     * @param up The up vector of the camera
     */
    public Camera(Vector3f front, Vector3f up, float fov, float maxZoom, float minZoom) {
        this.pos = new Vector3f();
        this.front = front;
        this.up = up;
        this.fov = fov;
        this.maxZoom = maxZoom;
        this.minZoom = minZoom;
        this.zoom = 1f;
    }

    // METHODS

    /**
     * Returns the position of the camera
     * @return New vector containing the cameras current position
     */
    public Vector3f getPos() {
        return new Vector3f(pos);
    }

    /**
     * Calculates and returns the perspective matrix as calculated from this camera fov and zoom levels
     * @param aspectRatio Aspect ratio for the final projection
     * @param near Distance of the near plane
     * @param far Distance of the far plane
     * @return Projection matrix for this camera, taking into account its fov and zoom
     */
    public Matrix4f getPerspectiveProjection(float aspectRatio, float near, float far) {
        return new Matrix4f().identity().perspective((float) toRadians(fov) / zoom, aspectRatio, near, far);
    }

    /**
     * Calculates and returns the view matrix defined by the camera
     * @return View matrix for this camera
     */
    public Matrix4f getViewMatrix() {
        Vector3f direction = new Vector3f(pos).add(front);
        return new Matrix4f().lookAt(
                pos,
                direction,
                up);
    }

    /**
     * Sets the camera position to the one provided
     * @param destination New position of the camera
     */
    public void moveTo(Vector3f destination) {
        pos.x = destination.x;
        pos.y = destination.y;
        pos.z = destination.z;
    }

    /**
     * Moves the camera in the X axis taking into account its front vector
     * @param displacement Displacement of the camera
     */
    public void moveX(float displacement) {
        pos.add((front.cross(up)).normalize().mul(displacement));
    }

    /**
     * Moves the camera in the Y axis taking into account its front vector
     * @param displacement Displacement of the camera
     */
    public void moveY(float displacement) {
        pos.add(new Vector3f(0f, displacement, 0f));
    }

    /**
     * Moves the camera in the Z axis taking into account its front vector
     * @param displacement Displacement of the camera
     */
    public void moveZ(float displacement) {
        pos.add(front.mul(displacement));
    }

    /**
     * Rotates the camera in all 3 axis
     * @param yaw New angle for the yaw in degrees
     * @param pitch New angle for the pitch in degrees
     * @param roll New angle of the roll in degrees (non-functional yet)
     */
    public void rotate(float yaw, float pitch, float roll) {
        float radYaw = (float) toRadians(yaw);
        float radPitch = (float) toRadians(pitch);
        float radRoll = (float) toRadians(roll);
        Vector3f front = new Vector3f();
        front.x = (float) (cos(radYaw) * cos(radPitch));
        front.y = (float) sin(radPitch);
        front.z = (float) (sin(radYaw) * cos(radPitch));
        this.front = front.normalize();
    }

    /**
     * Zooms the camera by a given amount
     * @param increase Amount to increase the zoom by
     */
    public void zoom(float increase) {
        zoom += increase;
        if (zoom > maxZoom) zoom = maxZoom;
        if (zoom < minZoom) zoom = minZoom;
    }
}
