package core.glRenderer;

import api.DiaAssetLoader;
import api.DiaLogger;
import assets.Texture;
import org.lwjgl.BufferUtils;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.stb.STBImage.stbi_load;
import static org.lwjgl.stb.STBImage.stbi_set_flip_vertically_on_load;

/**
 * AssetLoader
 *
 * @author Santiago Barreiro
 */
public class GLAssetLoader implements DiaAssetLoader {

    // ATTRIBUTES
    private final DiaLogger logger;

    // CONSTRUCTORS
    public GLAssetLoader(DiaLogger logger) {
        this.logger = logger;
    }

    // METHODS
    public Texture loadTexture(String from) {

        Texture texture = null;

        // Buffers to identify properties of the texture image
        IntBuffer x = BufferUtils.createIntBuffer(1);
        IntBuffer y = BufferUtils.createIntBuffer(1);
        IntBuffer channels = BufferUtils.createIntBuffer(1);

        // Load bytes from origin path
        stbi_set_flip_vertically_on_load(true);
        ByteBuffer bytes = stbi_load(from, x, y, channels, 0);

        if (bytes != null) {

            // Initialize texture
            int textureId = glGenTextures();
            glBindTexture(GL_TEXTURE_2D, textureId);

            // Set texture parameters
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);

            int width = x.get(0);
            int height = y.get(0);
            // Create texture based on if the texture has been loaded properly or not
            if (channels.get(0) == 4) {
                glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, x.get(0), y.get(0), 0, GL_RGBA, GL_UNSIGNED_BYTE, bytes);
                texture = new Texture(textureId, width, height, from);
            } else if (channels.get(0) == 3) {
                glTexImage2D(GL_TEXTURE_2D, 0, GL_RGB, x.get(0), y.get(0), 0, GL_RGB, GL_UNSIGNED_BYTE, bytes);
                texture = new Texture(textureId, width, height, from);
            } else {
                texture = new Texture(-1, -1, -1, from);
                logger.log(assets.Texture.class, "Failed to load texture, unexpected number of channels");
            }
        }
        return texture;
    }
}
