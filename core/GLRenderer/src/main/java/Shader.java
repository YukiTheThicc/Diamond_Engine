import org.joml.*;
import org.lwjgl.BufferUtils;
import java.nio.FloatBuffer;

import static org.lwjgl.opengl.GL11.GL_FALSE;
import static org.lwjgl.opengl.GL20.*;

/**
 * Shader
 *
 * @author: Santiago Barreiro
 */
public class Shader {

    // ATTRIBUTES
    private int programId;
    private boolean inUse = false;
    private String vertex;
    private String fragment;

    // CONSTRUCTORS
    /**
     * Creates a shader object from the vertex and fragment shaders, passed as strings. Shader compilation and therefore
     * error checking has to be done by calling hte compile() method
     *
     * @param name Name that identifies the shader
     * @param vertex String representing the vertex shader
     * @param fragment String representing the fragment shader
     */
    public Shader(String name, String vertex, String fragment) {

        this.programId = -1;
        this.vertex = vertex;
        this.fragment = fragment;
    }

    /**
     * Compile the shader. If compilation fails at some point, the program id will be set to -1.
     */
    public void compile() {

        int vertexId, fragmentId;
        boolean failed = false;

        // Vertex shader
        vertexId = glCreateShader(GL_VERTEX_SHADER);
        glShaderSource(vertexId, vertex);
        glCompileShader(vertexId);
        if (glGetShaderi(vertexId, GL_COMPILE_STATUS) == GL_FALSE) {
            int length = glGetShaderi(vertexId, GL_INFO_LOG_LENGTH);
            failed = true;
        }

        // Fragment shader
        fragmentId = glCreateShader(GL_FRAGMENT_SHADER);
        glShaderSource(fragmentId, fragment);
        glCompileShader(fragmentId);
        if (glGetShaderi(fragmentId, GL_COMPILE_STATUS) == GL_FALSE) {
            int length = glGetShaderi(fragmentId, GL_INFO_LOG_LENGTH);
            failed = true;
        }

        if (!failed) {
            programId = glCreateProgram();
            glAttachShader(programId, vertexId);
            glAttachShader(programId, fragmentId);
            glLinkProgram(programId);

            if (glGetProgrami(programId, GL_LINK_STATUS) == GL_FALSE) {
                programId = -1;
                int length = glGetProgrami(programId, GL_INFO_LOG_LENGTH);
            }
        }
    }

    /**
     * Use this shader. If the shader is not compiled or already in use it won't do anything.
     */
    public void use() {
        if (!inUse && this.programId > 0) {
            inUse = true;
            glUseProgram(this.programId);
        }
    }

    /**
     * Detach this shader.
     */
    public void detach() {
        inUse = false;
        glUseProgram(0);
    }

    public void uploadMat4f(String name, Matrix4f mat) {
        int location = glGetUniformLocation(programId, name);
        use();
        FloatBuffer matBuffer = BufferUtils.createFloatBuffer(16);
        mat.get(matBuffer);
        glUniformMatrix4fv(location, false, matBuffer);
    }

    public void uploadMat3f(String name, Matrix3f mat) {
        int location = glGetUniformLocation(programId, name);
        use();
        FloatBuffer matBuffer = BufferUtils.createFloatBuffer(9);
        mat.get(matBuffer);
        glUniformMatrix3fv(location, false, matBuffer);
    }

    public void uploadVec4f(String name, Vector4f vec) {
        int location = glGetUniformLocation(programId, name);
        use();
        glUniform4f(location, vec.x, vec.y, vec.z, vec.w);
    }

    public void uploadVec3f(String name, Vector3f vec) {
        int location = glGetUniformLocation(programId, name);
        use();
        glUniform3f(location, vec.x, vec.y, vec.z);
    }

    public void uploadVec2f(String name, Vector2f vec) {
        int location = glGetUniformLocation(programId, name);
        use();
        glUniform2f(location, vec.x, vec.y);
    }

    public void uploadFloat(String name, float value) {
        int location = glGetUniformLocation(programId, name);
        use();
        glUniform1f(location, value);
    }

    public void uploadInt(String name, int value) {
        int location = glGetUniformLocation(programId, name);
        use();
        glUniform1i(location, value);
    }

    public void uploadTexture(String name, int slot) {
        int location = glGetUniformLocation(programId, name);
        use();
        glUniform1i(location, slot);
    }

    public void uploadIntArray(String name, int[] array) {
        int location = glGetUniformLocation(programId, name);
        use();
        glUniform1iv(location, array);
    }
}
