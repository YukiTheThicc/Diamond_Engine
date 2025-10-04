import static org.lwjgl.opengl.GL11.*;

/**
 * Texture
 *
 * @author: Santiago Barreiro
 */
public class Texture {

    // CONSTANTS

    // ATTRIBUTES
    private final int id;
    private int width;
    private int height;

    // CONTRUCTORS
    public Texture() {
        this.id = -1;
        this.width = -1;
        this.height = -1;
    }

    public Texture(int width, int height) {
        this.id = glGenTextures();
        this.width = width;
        this.height = height;
        glBindTexture(GL_TEXTURE_2D, this.id);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGB, width, height, 0, GL_RGB, GL_UNSIGNED_BYTE, 0);
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

    public void setWidth(int width) {
        this.width = width;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    // METHODS

}
