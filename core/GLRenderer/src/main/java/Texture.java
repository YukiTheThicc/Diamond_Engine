import api.DiaLogger;
import org.lwjgl.BufferUtils;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL11.*;

/**
 * Texture
 *
 * @author: Santiago Barreiro
 */
public class Texture {

    // CONSTANTS

    // ATTRIBUTES
    private int id;
    private int width;
    private int height;

    // CONTRUCTORS
    public Texture() {
        this.id = -1;
        this.width = -1;
        this.height = -1;
    }

    /**
     *
     * @param width
     * @param height
     */
    public Texture(int width, int height) {
        this.id = -1;
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

    /**
     * Loads a byte buffer as a texture. Returns false in case of
     * @param bytes
     * @return
     */
    public boolean load(ByteBuffer bytes) {

        this.id = glGenTextures();
        glBindTexture(GL_TEXTURE_2D, id);

        // Texture Parameters
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);

        IntBuffer x = BufferUtils.createIntBuffer(1);
        IntBuffer y = BufferUtils.createIntBuffer(1);
        IntBuffer channels = BufferUtils.createIntBuffer(1);

        if (bytes != null) {
            this.width = x.get(0);
            this.height = y.get(0);
            if (channels.get(0) == 4) {
                // RGBA Image
                glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, x.get(0), y.get(0), 0, GL_RGBA, GL_UNSIGNED_BYTE, bytes);
            } else if (channels.get(0) == 3) {
                // RGB Image
                glTexImage2D(GL_TEXTURE_2D, 0, GL_RGB, x.get(0), y.get(0), 0, GL_RGB, GL_UNSIGNED_BYTE, bytes);
            } else {
                //DiaLogger.log(Texture.class, "Failed to load texture, unexpected number of channels");
                return false;
            }
        } else {
            return false;
        }
        return true;
    }
}
