package core;

import api.DiaAssetLoader;
import api.DiaLogger;
import core.assets.Shader;
import core.assets.Texture;

import java.io.File;
import java.util.HashMap;

/**
 * AssetManager
 *
 * @author Santiago Barreiro
 */
public class AssetPool {

    // CONSTANTS


    // ATTRIBUTES
    private final DiaLogger logger;
    private final DiaAssetLoader assetLoader;
    private final HashMap<String, Texture> textures;
    private final HashMap<String, Shader> shaders;

    // CONSTRUCTORS
    public AssetPool(DiaLogger logger, DiaAssetLoader assetLoader) {
        this.logger = logger;
        this.assetLoader = assetLoader;
        this.textures = new HashMap<>();
        this.shaders = new HashMap<>();
    }


    // GETTERS & SETTERS


    // METHODS
    public Texture getTexture(String source) {
        File file = new File(source);
        if (textures.containsKey(file.getAbsolutePath())) {
            return textures.get(file.getAbsolutePath());
        } else {
            Texture texture = assetLoader.loadTexture(file.getAbsolutePath());
            textures.put(file.getAbsolutePath(), texture);
            return texture;
        }
    }

    public Shader getShader(String source) {
        File file = new File(source);
        if (shaders.containsKey(file.getAbsolutePath())) {
            return shaders.get(file.getAbsolutePath());
        } else {
            Shader shader = assetLoader.loadShader(file.getAbsolutePath());
            shaders.put(file.getAbsolutePath(), shader);
            return shader;
        }
    }

    public Shader getShader(String vs, String fs) {
        File vsFile = new File(vs);
        File fsFile = new File(fs);
        if (shaders.containsKey(vsFile.getAbsolutePath())) {
            return shaders.get(vsFile.getAbsolutePath());
        } else if (shaders.containsKey(fsFile.getAbsolutePath())) {
            return shaders.get(fsFile.getAbsolutePath());
        } else {
            Shader shader = assetLoader.loadShader(vsFile.getAbsolutePath(), fsFile.getAbsolutePath());
            shaders.put(vsFile.getAbsolutePath(), shader);
            shaders.put(fsFile.getAbsolutePath(), shader);
            return shader;
        }
    }
}
