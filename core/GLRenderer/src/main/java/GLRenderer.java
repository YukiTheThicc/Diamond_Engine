import api.DiaRenderer;
import org.joml.Vector2f;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL11.GL_LINES;
import static org.lwjgl.opengl.GL11.glDrawArrays;
import static org.lwjgl.opengl.GL11.glLineWidth;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL20.glDisableVertexAttribArray;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

/**
 * GLRenderer
 *
 * @author Santiago Barreiro
 */
public class GLRenderer implements DiaRenderer {

    // CONSTANTS


    // ATTRIBUTES
    public static int[] lines;

    // CONSTRUCTORS


    // GETTERS & SETTERS


    // METHODS
    public void init() {

    }

    @Override
    public void startFrame() {

    }

    @Override
    public void finishFrame() {

    }

    @Override
    public void addLine(Vector2f from, Vector2f to) {

    }

    private static class LineRenderer {

        // CONSTANTS
        private static final int ATTR_PER_LINE = 12;                        // There are 12 floats worth of attributes per line: 6 per vertex, in each vertex 3 por position and 3 for color
        private static final int MAX_LINES = 10000;
        private static final String vertex =
                "#version 330 core\n" +
                        "layout (location=0) in vec3 attrPos;\n" +
                        "layout (location=1) in vec3 attrColor;\n" +
                        "uniform mat4 uProjection;\n" +
                        "uniform mat4 uView;\n" +
                        "uniform int uType;\n" +
                        "out vec3 fragColor;\n" +
                        "out int type;\n" +
                        "void main() {\n" +
                        "    fragColor = attrColor;\n" +
                        "    type = uType;\n" +
                        "    gl_Position = uProjection * uView * vec4(attrPos, 1.0);\n" +
                        "}";
        private static final String fragment =
                "#version 330 core\n" +
                        "in vec3 fragColor;\n" +
                        "out vec4 color;\n" +
                        "void main() {\n" +
                        "    color = vec4(fragColor, 1);\n" +
                        "}";

        // ATTRIBUTES
        private static final Shader shader = new Shader(vertex, fragment);
        private static final float[] vertexArray = new float[MAX_LINES * 6 * 2];
        private static int vaoID;
        private static int vboID;
        private static int lineCount;
        private static boolean started = false;
        private static boolean isEmpty = true;

        // CONSTRUCTORS

        // GETTERS & SETTERS

        // METHODS
        public static void init() {
            lineCount = 0;
        }

        public static void addLine(Vector2f from, Vector2f to) {
            addLine(from, to, new Vector3f(1, 1, 1));
        }

        public static void addLine(Vector2f from, Vector2f to, Vector3f color) {

            // Whenever
            if (isEmpty) isEmpty = false;

            // First vertex of the line
            vertexArray[lineCount * ATTR_PER_LINE] = from.x;
            vertexArray[lineCount * ATTR_PER_LINE + 1] = from.y;
            vertexArray[lineCount * ATTR_PER_LINE + 2] = -10.0f;
            vertexArray[lineCount * ATTR_PER_LINE + 3] = color.x;
            vertexArray[lineCount * ATTR_PER_LINE + 4] = color.y;
            vertexArray[lineCount * ATTR_PER_LINE + 5] = color.z;

            // Second vertex for the line
            vertexArray[lineCount * ATTR_PER_LINE + 6] = to.x;
            vertexArray[lineCount * ATTR_PER_LINE + 7] = to.y;
            vertexArray[lineCount * ATTR_PER_LINE + 8] = -10.0f;
            vertexArray[lineCount * ATTR_PER_LINE + 9] = color.x;
            vertexArray[lineCount * ATTR_PER_LINE + 10] = color.y;
            vertexArray[lineCount * ATTR_PER_LINE + 11] = color.z;
            lineCount++;
        }

        public static void draw(Camera2D camera) {

            if (isEmpty) return;

            // Lazily compile the shaders in case they weren't compiled before and bind buffers
            if (!started) {
                shader.compile();
                vaoID = glGenVertexArrays();
                glBindVertexArray(vaoID);

                vboID = glGenBuffers();
                glBindBuffer(GL_ARRAY_BUFFER, vboID);
                glBufferData(GL_ARRAY_BUFFER, (long) vertexArray.length * Float.BYTES, GL_DYNAMIC_DRAW);

                glVertexAttribPointer(0, 3, GL_FLOAT, false, 6 * Float.BYTES, 0);
                glEnableVertexAttribArray(0);
                glVertexAttribPointer(1, 3, GL_FLOAT, false, 6 * Float.BYTES, 3 * Float.BYTES);
                glEnableVertexAttribArray(1);
                glLineWidth(1f);
                started = true;
            }

            glBindBuffer(GL_ARRAY_BUFFER, vboID);
            glBufferData(GL_ARRAY_BUFFER, vertexArray, GL_DYNAMIC_DRAW);

            // Use our shader
            shader.use();
            shader.uploadMat4f("uProjection", camera.getProjMatrix());
            shader.uploadMat4f("uView", camera.getViewMatrix());
            shader.uploadInt("uType", 0);

            // Bind the vao
            glBindVertexArray(vaoID);
            glEnableVertexAttribArray(0);
            glEnableVertexAttribArray(1);

            // Draw the batch
            glDrawArrays(GL_LINES, 0, lineCount * 2);

            // Disable Location
            glDisableVertexAttribArray(0);
            glDisableVertexAttribArray(1);
            glBindVertexArray(0);

            // Unbind shader
            shader.detach();
        }
    }
}
