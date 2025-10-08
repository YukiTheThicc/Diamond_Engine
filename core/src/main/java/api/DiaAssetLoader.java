package api;

import assets.Texture;

/**
 * DiaAssetLoader
 *
 * @author Santiago Barreiro
 */
public interface DiaAssetLoader {

    /**
     * Loads a texture to the current
     * @param from
     * @return
     */
    public Texture loadTexture(String from);
}
