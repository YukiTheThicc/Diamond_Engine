package api;

import assets.Shader;
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
    Texture loadTexture(String from);

    /**
     * Loads a shader from 2 different sources, one for the vertex shader and one for the fragment shader
     * @param vsSource Source path for the vertex shader
     * @param fsSource Source path for the fragment shader
     * @return New shader from the provided sources. Non compiled
     */
    Shader loadShader(String vsSource, String fsSource);

    /**
     * Creates a shader from a single source. Expects to types of shader in one file, each being headed by the string
     * "#type vertex" for the vertex shader and "#type fragment" for the fragment shader
     * @param source Sin
     * @return New shader from the provided source. Non compiled
     */
    Shader loadShader(String source);
}
