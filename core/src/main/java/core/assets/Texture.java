package core.assets;

/**
 * Texture asset. Implements no functionality. Stores its corresponding API id, width, height, and origin path. Should
 * not be created and used on its own, as it needs to be loaded through an DiaAssetLoader to be functional
 *
 * @author: Santiago Barreiro
 */
public class Texture {

    // ATTRIBUTES
    private final int id;
    private final int width;
    private final int height;
    private final String origin;

    // CONSTRUCTOR
    public Texture(int id, int width, int height, String origin) {
        this.id = id;
        this.width = width;
        this.height = height;
        this.origin = origin;
    }

    // GETTERS & SETTERS
    public int getId() {
        return id;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public String getOrigin() {
        return origin;
    }
}
