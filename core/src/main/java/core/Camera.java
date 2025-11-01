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
    public Matrix4f getPerspectiveProjection(float aspectRatio, float near, float far) {
        return new Matrix4f().identity().perspective((float) toRadians(fov) / zoom, aspectRatio, near, far);
    }
    
    public Matrix4f getViewMatrix() {
        Vector3f direction = new Vector3f(pos).add(front);
        return new Matrix4f().lookAt(
                pos,
                direction,
                up);
    }

    public void moveTo(Vector3f destination) {
        this.pos = destination;
    }

    public void move(float displacementX, float displacementY, float displacementZ) {
        pos.add(displacementX, displacementY, displacementZ);
    }

    public void moveX(float displacement) {
        Vector3f movement = (front.cross(up)).normalize().mul(displacement);
        pos = pos.add(movement);
    }

    public void moveY(float displacement) {
        pos = pos.add(new Vector3f(0f, displacement, 0f));
    }

    public void moveZ(float displacement) {
        pos = pos.add(front.mul(displacement));
    }

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

    public void zoom(float increase) {
        zoom += increase;
        if (zoom > maxZoom) zoom = maxZoom;
        if (zoom < minZoom) zoom = minZoom;
    }
}
