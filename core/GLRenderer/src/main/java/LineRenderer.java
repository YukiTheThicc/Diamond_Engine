import org.joml.Vector2f;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL11.glLineWidth;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL15.GL_DYNAMIC_DRAW;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL20.glDisableVertexAttribArray;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

/**
 * LineRenderer
 *
 * @author: Santiago Barreiro
 */
public class LineRenderer {

    /**
     * Auxiliary class to store line information
     */
    public static class Line {

        // ATTRIBUTES
        private Vector2f from;
        private Vector2f to;
        private Vector3f color;
        private int lifetime;

        // CONSTRUCTORS
        public Line(Vector2f form, Vector2f to, Vector3f color, int lifetime) {
            this.from = form;
            this.to = to;
            this.color = color;
            this.lifetime = lifetime;
        }

        // METHODS
        public int beginFrame() {
            this.lifetime--;
            return this.lifetime;
        }
    }

    // CONSTANTS
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
    private static final Shader shader = new Shader("debugLine2D", vertex, fragment);
    private static final float[] vertexArray = new float[MAX_LINES * 6 * 2];
    private static int vaoID;
    private static int vboID;
    private static boolean started = false;
    private static final List<Line> lines = new ArrayList<>();

    // CONSTRUCTORS

    // GETTERS & SETTERS

    // METHODS
    public static void addLine(Vector2f from, Vector2f to) {
        addLine(from, to, new Vector3f(0, 1, 0), 1);
    }

    public static void addLine(Vector2f from, Vector2f to, Vector3f color) {
        addLine(from, to, color, 1);
    }

    public static void addLine(Vector2f from, Vector2f to, Vector3f color, int lifetime) {
        if (lines.size() >= MAX_LINES) {
            return;
        }
        LineRenderer.lines.add(new Line(from, to, color, lifetime));
    }

    public static void draw(Camera2D camera) {

        if (lines.isEmpty()) return;

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

        // Remove 'dead' lines from the array before iterating to draw
        for (int i = 0; i < lines.size(); i++) {
            if (lines.get(i).beginFrame() < 0) {
                lines.remove(i);
                i--;
            }
        }

        // Add line data to the vertex array
        int index = 0;
        for (Line line : lines) {
            for (int i = 0; i < 2; i++) {
                Vector2f position = i == 0 ? line.from : line.to;
                Vector3f color = line.color;

                // Load position
                vertexArray[index] = position.x;
                vertexArray[index + 1] = position.y;
                vertexArray[index + 2] = -10.0f;

                // Load the color
                vertexArray[index + 3] = color.x;
                vertexArray[index + 4] = color.y;
                vertexArray[index + 5] = color.z;
                index += 6;
            }
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
        glDrawArrays(GL_LINES, 0, lines.size() * 2);

        // Disable Location
        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);
        glBindVertexArray(0);

        // Unbind shader
        shader.detach();
    }
}
