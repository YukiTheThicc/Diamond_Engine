package assets;

import api.DiaLogger;

/**
 * Shader
 *
 * @author Santiago Barreiro
 */
public interface Shader {

    void compile(DiaLogger logger);

    void use();

    void detach();
}
