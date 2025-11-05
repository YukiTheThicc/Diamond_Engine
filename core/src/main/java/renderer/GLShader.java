package renderer;

import api.DiaLogger;
import assets.Shader;
import org.joml.*;
import org.lwjgl.BufferUtils;
import java.nio.FloatBuffer;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;

/**
 * Shader
 *
 * @author: Santiago Barreiro
 */
public class GLShader implements Shader {

    // ATTRIBUTES
    private int programId;
    private boolean inUse = false;
    private final String vertex;
    private final String fragment;

    // CONSTRUCTORS
    /**
     * Creates a shader object from the vertex and fragment shaders, passed as strings. Shader compilation and therefore
     * error checking has to be done by calling hte compile() method
     *
     * @param vertex String representing the vertex shader
     * @param fragment String representing the fragment shader
     */
    public GLShader(String vertex, String fragment) {
        this.programId = -1;
        this.vertex = vertex;
        this.fragment = fragment;
    }

    /**
     * Compile the shader. If compilation fails at some point, the program id will be set to -1.
     */
    @Override
    public void compile(DiaLogger logger) {

        int vertexId, fragmentId;
        boolean failed = false;

        // Vertex shader
        vertexId = GL20.glCreateShader(GL20.GL_VERTEX_SHADER);
        GL20.glShaderSource(vertexId, vertex);
        GL20.glCompileShader(vertexId);
        if (GL20.glGetShaderi(vertexId, GL20.GL_COMPILE_STATUS) == GL11.GL_FALSE) {
            failed = true;
            int len = GL20.glGetShaderi(vertexId, GL20.GL_INFO_LOG_LENGTH);
            logger.log(GLShader.class, "Error while compiling VERTEX SHADER: \n" + GL20.glGetShaderInfoLog(vertexId, len), DiaLogger.levels.ERROR);
        }

        // Fragment shader
        fragmentId = GL20.glCreateShader(GL20.GL_FRAGMENT_SHADER);
        GL20.glShaderSource(fragmentId, fragment);
        GL20.glCompileShader(fragmentId);
        if (GL20.glGetShaderi(fragmentId, GL20.GL_COMPILE_STATUS) == GL11.GL_FALSE) {
            failed = true;
            int len = GL20.glGetShaderi(fragmentId, GL20.GL_INFO_LOG_LENGTH);
            logger.log(GLShader.class, "Error while compiling FRAGMENT SHADER: \n" + GL20.glGetShaderInfoLog(fragmentId, len), DiaLogger.levels.ERROR);
        }

        if (!failed) {
            programId = GL20.glCreateProgram();
            GL20.glAttachShader(programId, vertexId);
            GL20.glAttachShader(programId, fragmentId);
            GL20.glLinkProgram(programId);
            if (GL20.glGetProgrami(programId, GL20.GL_LINK_STATUS) == GL11.GL_FALSE) {
                programId = -1;
            }
        }

        GL20.glDeleteShader(vertexId);
        GL20.glDeleteShader(fragmentId);
    }

    /**
     * Use this shader. If the shader is not compiled or already in use it won't do anything.
     */
    public void use() {
        if (!inUse && this.programId > 0) {
            inUse = true;
            GL20.glUseProgram(this.programId);
        }
    }

    /**
     * Detach this shader.
     */
    public void detach() {
        inUse = false;
        GL20.glUseProgram(0);
    }

    public void uploadMat4f(String name, Matrix4f mat) {
        int location = GL20.glGetUniformLocation(programId, name);
        use();
        FloatBuffer matBuffer = BufferUtils.createFloatBuffer(16);
        mat.get(matBuffer);
        GL20.glUniformMatrix4fv(location, false, matBuffer);
    }

    public void uploadMat3f(String name, Matrix3f mat) {
        int location = GL20.glGetUniformLocation(programId, name);
        use();
        FloatBuffer matBuffer = BufferUtils.createFloatBuffer(9);
        mat.get(matBuffer);
        GL20.glUniformMatrix3fv(location, false, matBuffer);
    }

    public void uploadVec4f(String name, Vector4f vec) {
        int location = GL20.glGetUniformLocation(programId, name);
        use();
        GL20.glUniform4f(location, vec.x, vec.y, vec.z, vec.w);
    }

    public void uploadVec3f(String name, Vector3f vec) {
        int location = GL20.glGetUniformLocation(programId, name);
        use();
        GL20.glUniform3f(location, vec.x, vec.y, vec.z);
    }

    public void uploadVec2f(String name, Vector2f vec) {
        int location = GL20.glGetUniformLocation(programId, name);
        use();
        GL20.glUniform2f(location, vec.x, vec.y);
    }

    public void uploadFloat(String name, float value) {
        int location = GL20.glGetUniformLocation(programId, name);
        use();
        GL20.glUniform1f(location, value);
    }

    public void uploadInt(String name, int value) {
        int location = GL20.glGetUniformLocation(programId, name);
        use();
        GL20.glUniform1i(location, value);
    }

    public void uploadTexture(String name, int slot) {
        int location = GL20.glGetUniformLocation(programId, name);
        use();
        GL20.glUniform1i(location, slot);
    }

    public void uploadIntArray(String name, int[] array) {
        int location = GL20.glGetUniformLocation(programId, name);
        use();
        GL20.glUniform1iv(location, array);
    }
}
