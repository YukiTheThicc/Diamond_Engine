package core.glRenderer;

import api.DiaAssetLoader;
import api.DiaLogger;
import core.assets.Texture;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.stb.STBImage;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.nio.file.Files;
import java.nio.file.Paths;


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
    @Override
    public Texture loadTexture(String from) {

        Texture texture = null;

        // Buffers to identify properties of the texture image
        IntBuffer x = BufferUtils.createIntBuffer(1);
        IntBuffer y = BufferUtils.createIntBuffer(1);
        IntBuffer channels = BufferUtils.createIntBuffer(1);

        // Load bytes from origin path
        STBImage.stbi_set_flip_vertically_on_load(false);
        ByteBuffer bytes = STBImage.stbi_load(from, x, y, channels, 0);

        if (bytes != null) {

            // Initialize texture
            int textureId = GL11.glGenTextures();
            GL11.glBindTexture(GL11.GL_TEXTURE_2D, textureId);

            // Set texture parameters
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL11.GL_REPEAT);
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL11.GL_REPEAT);
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);

            int width = x.get(0);
            int height = y.get(0);
            // Create texture based on if the texture has been loaded properly or not
            if (channels.get(0) == 4) {
                GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA, x.get(0), y.get(0), 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, bytes);
                texture = new Texture(textureId, width, height, from);
            } else if (channels.get(0) == 3) {
                GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGB, x.get(0), y.get(0), 0, GL11.GL_RGB, GL11.GL_UNSIGNED_BYTE, bytes);
                texture = new Texture(textureId, width, height, from);
            } else {
                texture = new Texture(-1, -1, -1, from);
                logger.log(Texture.class, "Failed to load texture, unexpected number of channels");
            }
            STBImage.stbi_image_free(bytes);
        }
        return texture;
    }

    /**
     * Creates a shader from a single source. Expects to types of shader in one file, each being headed by the string
     * "#type vertex" for the vertex shader and "#type fragment" for the fragment shader
     * @param source Sin
     * @return New shader from the provided source. Non compiled
     */
    @Override
    public GLShader loadShader(String source) {
        GLShader shader = null;
        try {
            String sourceString = new String(Files.readAllBytes(Paths.get(source)));
            String[] sources = sourceString.split("(#type)( )+([a-zA-Z])+");

            // Find the first pattern after #type
            int index = sourceString.indexOf("#type") + 6;
            int eol = sourceString.indexOf("\r\n", index);
            String firstPattern = sourceString.substring(index, eol).trim();
            // Find the second pattern after #type
            index = sourceString.indexOf("#type", eol) + 6;
            eol = sourceString.indexOf("\r\n", index);
            String secondPattern = sourceString.substring(index, eol).trim();

            String vs = "", fs = "";
            if (firstPattern.equals("vertex")) {
                vs = sources[1];
            } else if (firstPattern.equals("fragment")) {
                fs = sources[1];
            } else {
                throw new IOException("Unexpected token '" + firstPattern + "'");
            }

            if (secondPattern.equals("vertex")) {
                vs = sources[2];
            } else if (secondPattern.equals("fragment")) {
                fs = sources[2];
            } else {
                throw new IOException("Unexpected token '" + secondPattern + "'");
            }
            shader = new GLShader(vs, fs);
        } catch (IOException e) {
            logger.log(GLAssetLoader.class, "Error while loading shader from \"" + source + "\": " + e.getMessage(), DiaLogger.levels.ERROR);
        }
        return shader;
    }

    /**
     * Loads a shader from 2 different sources, one for the vertex shader and one for the fragment shader
     * @param vsSource Source path for the vertex shader
     * @param fsSource Source path for the fragment shader
     * @return New shader from the provided sources. Non compiled
     */
    @Override
    public GLShader loadShader(String vsSource, String fsSource) {
        GLShader shader = null;
        try {
            String vsSourceString = new String(Files.readAllBytes(Paths.get(vsSource)));
            String fsSourceString = new String(Files.readAllBytes(Paths.get(fsSource)));
            shader = new GLShader(vsSourceString, fsSourceString);
        } catch (IOException e) {
            logger.log(GLAssetLoader.class, "Error while loading shader from \"" + vsSource + "\" and \"" + vsSource + "\": " + e.getMessage(), DiaLogger.levels.ERROR);
        }
        return shader;
    }
}