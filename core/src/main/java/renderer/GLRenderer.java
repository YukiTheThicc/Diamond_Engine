package renderer;

import api.*;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.lwjgl.opengl.*;

/**
 * glrenderer.GLRenderer
 *
 * @author Santiago Barreiro
 */
public class GLRenderer implements DiaRenderer, DiaWindow.ResizeObserver {

    // CONSTANTS

    // ATTRIBUTES
    boolean drawLines = false;
    DiaLogger logger;

    // CONSTRUCTOR
    public GLRenderer(DiaLogger logger) {
        this.logger = logger;
    }

    // METHODS
    public void init() {
        LineRenderer.lineCount = 0;
        GL.createCapabilities();
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_ONE, GL11.GL_ONE_MINUS_SRC_ALPHA);
    }

    public void drawLines() {
        if (!drawLines) drawLines = true;
    }

    public void noDrawLines() {
        if (drawLines) drawLines = false;
    }


    /**
     * Renders the current frame
     * @param view View matrix to render the frame from
     * @param projection Projection matrix to use for rendering
     */
    @Override
    public void renderFrame(Matrix4f view, Matrix4f projection) {
        GL11.glClearColor(0.1f, 0.1f, 0.1f, 1.0f);
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
        LineRenderer.draw(view, projection, logger);
    }

    @Override
    public void addLine(Vector2f from, Vector2f to) {
        LineRenderer.addLine(from, to);
    }

    @Override
    public void addLine(Vector2f from, Vector2f to, Vector3f color) {
        LineRenderer.addLine(from, to, color);
    }

    @Override
    public void addLine(Vector3f from, Vector3f to) {
        LineRenderer.addLine(from, to);
    }

    @Override
    public void addLine(Vector3f from, Vector3f to, Vector3f color) {
        LineRenderer.addLine(from, to, color);
    }

    @Override
    public void setViewPort(int width, int height) {

    }

    private static class LineRenderer {

        // CONSTANTS
        private static final int ATTR_PER_LINE = 12;                        // There are 12 floats worth of attributes per line: 6 per vertex, in each vertex 3 por position and 3 for color
        private static final int MAX_LINES = 10000;
        private static final String VERTEX =
                "#version 330 core\n" +
                        "layout (location=0) in vec3 attrPos;\n" +
                        "layout (location=1) in vec3 attrColor;\n" +
                        "uniform mat4 uProjection;\n" +
                        "uniform mat4 uView;\n" +
                        "uniform int uType;\n" +
                        "out vec3 fragColor;\n" +
                        "out vec3 fragPos;\n" +
                        "out int type;\n" +
                        "void main() {\n" +
                        "    fragColor = attrColor;\n" +
                        "    type = uType;\n" +
                        "    gl_Position = uProjection * uView * vec4(attrPos, 1.0);\n" +
                        "    fragPos = vec3(gl_Position.x,gl_Position.y,gl_Position.z);\n" +
                        "}";
        private static final String FRAGMENT =
                "#version 330 core\n" +
                        "in vec3 fragColor;\n" +
                        "in vec3 fragPos;\n" +
                        "out vec4 color;\n" +
                        "void main() {\n" +
                        "    color = vec4(fragColor, 1);\n" +
                        "}";

        // ATTRIBUTES
        private static final GLShader lineShader = new GLShader(VERTEX, FRAGMENT);
        private static final float[] vertexArray = new float[MAX_LINES * 6 * 2];
        private static int vaoID;
        private static int vboID;
        private static int lineCount = 0;
        private static boolean started = false;

        // METHODS
        public static void addLine(Vector2f from, Vector2f to) {
            addLine(from, to, new Vector3f(1, 0, 1));
        }

        public static void addLine(Vector2f from, Vector2f to, Vector3f color) {

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

        public static void addLine(Vector3f from, Vector3f to) {
            addLine(from, to, new Vector3f(1, 0, 1));
        }

        public static void addLine(Vector3f from, Vector3f to, Vector3f color) {

            // First vertex of the line
            vertexArray[lineCount * ATTR_PER_LINE] = from.x;
            vertexArray[lineCount * ATTR_PER_LINE + 1] = from.y;
            vertexArray[lineCount * ATTR_PER_LINE + 2] = from.z;
            vertexArray[lineCount * ATTR_PER_LINE + 3] = color.x;
            vertexArray[lineCount * ATTR_PER_LINE + 4] = color.y;
            vertexArray[lineCount * ATTR_PER_LINE + 5] = color.z;

            // Second vertex for the line
            vertexArray[lineCount * ATTR_PER_LINE + 6] = to.x;
            vertexArray[lineCount * ATTR_PER_LINE + 7] = to.y;
            vertexArray[lineCount * ATTR_PER_LINE + 8] = to.z;
            vertexArray[lineCount * ATTR_PER_LINE + 9] = color.x;
            vertexArray[lineCount * ATTR_PER_LINE + 10] = color.y;
            vertexArray[lineCount * ATTR_PER_LINE + 11] = color.z;
            lineCount++;
        }

        /**
         * Immediate mode rendering of the lines currently buffered. Should be called only once per frame
         */
        public static void draw(Matrix4f view, Matrix4f projection, DiaLogger logger) {

            if (lineCount == 0) return;

            // Lazily compile the shaders in case they weren't compiled before and bind buffers
            if (!started) {
                lineShader.compile(logger);
                vaoID = GL30.glGenVertexArrays();
                GL30.glBindVertexArray(vaoID);

                vboID = GL15.glGenBuffers();
                GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboID);
                GL15.glBufferData(GL15.GL_ARRAY_BUFFER, (long) vertexArray.length * Float.BYTES, GL15.GL_DYNAMIC_DRAW);

                GL20.glVertexAttribPointer(0, 3, GL11.GL_FLOAT, false, 6 * Float.BYTES, 0);
                GL20.glEnableVertexAttribArray(0);
                GL20.glVertexAttribPointer(1, 3, GL11.GL_FLOAT, false, 6 * Float.BYTES, 3 * Float.BYTES);
                GL20.glEnableVertexAttribArray(1);
                GL11.glLineWidth(2f);
                started = true;
            }

            GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboID);
            GL15.glBufferData(GL15.GL_ARRAY_BUFFER, vertexArray, GL15.GL_DYNAMIC_DRAW);

            // Use our shader
            lineShader.use();
            lineShader.uploadMat4f("uProjection", projection);
            lineShader.uploadMat4f("uView", view);
            lineShader.uploadInt("uType", 0);

            // Bind the vao
            GL30.glBindVertexArray(vaoID);
            GL20.glEnableVertexAttribArray(0);
            GL20.glEnableVertexAttribArray(1);

            // Draw the batch
            GL11.glEnable(GL11.GL_BLEND);
            GL11.glDrawArrays(GL11.GL_LINES, 0, lineCount * 2);

            // Disable Location
            GL20.glDisableVertexAttribArray(0);
            GL20.glDisableVertexAttribArray(1);
            GL30.glBindVertexArray(0);

            // Unbind shader, and reset line index to O
            lineShader.detach();
            lineCount = 0;
        }
    }

    @Override
    public void adjustSize(int width, int height) {
        GL11.glViewport(0, 0, width, height);
    }
}
