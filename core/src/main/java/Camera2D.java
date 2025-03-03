import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;

/**
 * Camera2D
 *
 * @author: Santiago Barreiro
 */
public class Camera2D {

    // ATTRIBUTES
    private final Vector3f front;
    private final Vector3f up;
    private final Vector2f pSize;
    public Vector2f pos;
    public float zoom = 1.0f;

    // RUNTIME ATTRIBUTES
    private transient final Vector2f pSizeActual;
    private transient Matrix4f pMatrix;
    private transient final Matrix4f vMatrix;
    private transient Matrix4f invProj;
    private transient Matrix4f invView;

    // CONSTRUCTORS

    /**
     * Default constructor for the Camera2D object. Uses a 4 wide by 3 high projection and starts at position (0,0)
     */
    public Camera2D() {
        this.pos = new Vector2f(0,0);
        this.pSize = new Vector2f(4, 3);
        this.pSizeActual = new Vector2f(pSize.x, pSize.y);
        this.pMatrix = new Matrix4f();
        this.vMatrix = new Matrix4f();
        this.invProj = new Matrix4f();
        this.invView = new Matrix4f();
        this.front = new Vector3f(0.0f, 0.0f, -1.0f);
        this.up = new Vector3f(0.0f, 1.0f, 0.0f);
        this.updateProjection();
    }

    /**
     * Complete constructor for the 2D camera object
     * @param pos Initial position of the camera
     * @param pWidth Camera projection width
     * @param pHeight Camera projection height
     */
    public Camera2D(Vector2f pos, float pWidth, float pHeight) {
        this.pos = pos;
        this.pSize = new Vector2f(pWidth, pHeight);
        this.pSizeActual = new Vector2f(pSize.x, pSize.y);
        this.pMatrix = new Matrix4f();
        this.vMatrix = new Matrix4f();
        this.invProj = new Matrix4f();
        this.invView = new Matrix4f();
        this.front = new Vector3f(0.0f, 0.0f, -1.0f);
        this.up = new Vector3f(0.0f, 1.0f, 0.0f);
        this.updateProjection();
    }

    // GETTERS & SETTERS
    public Matrix4f getProjMatrix() {
        return this.pMatrix;
    }

    public Matrix4f getViewMatrix() {
        vMatrix.identity();
        Vector3f cameraFront = new Vector3f(front);
        vMatrix.lookAt(new Vector3f(pos.x, pos.y, 20.0f), cameraFront.add(pos.x, pos.y, 0.0f), up);
        vMatrix.invert(invView);
        return vMatrix;
    }

    public Matrix4f getInvProj() {
        return invProj;
    }

    public Matrix4f getInvView() {
        return invView;
    }

    public Vector2f getPSize() {
        return pSize;
    }

    public Vector3f getFront() {
        return front;
    }

    public Vector3f getUp() {
        return up;
    }

    // METHODS
    public void updateProjection() {
        pMatrix.identity();

        // Calculate actual projection size from zoom and aspect ratio
        this.pSizeActual.x = this.zoom * pSize.x;
        this.pSizeActual.y = this.zoom * pSize.y;

        pMatrix.ortho(0.0f, pSizeActual.x, 0.0f, pSizeActual.y, 0.0f, 100.0f);
        pMatrix.invert(invProj);
    }
}
