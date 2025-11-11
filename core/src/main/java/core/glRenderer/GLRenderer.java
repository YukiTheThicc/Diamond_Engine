package core.glRenderer;

import api.*;
import core.exceptions.DiamondException;
import core.RenderTarget;
import core.assets.Texture;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;

import static org.lwjgl.opengl.GL.createCapabilities;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;

/**
 * glrenderer.GLRenderer
 *
 * @author Santiago Barreiro
 */
public class GLRenderer implements DiaRenderer, DiaWindow.ResizeObserver {

    // CONSTANTS
    static final int NUM_GRID_LINES = 50;

    // ATTRIBUTES
    boolean drawLines = false;
    boolean drawGrid = false;
    DiaLogger logger;

    // CONSTRUCTOR
    public GLRenderer(DiaLogger logger) {
        this.logger = logger;
    }

    // METHODS
    public void init() {
        LineRenderer.lineCount = 0;
        createCapabilities();                           // Initialize OpenGL functionalities
        glEnable(GL_BLEND);                             // Enable Alpha blending
        glEnable(GL_DEPTH_TEST);                        // Enable depth testing por correct z-index rendering
        glBlendFunc(GL_ONE, GL_ONE_MINUS_SRC_ALPHA);
    }

    public void drawLines() {
        if (!drawLines) drawLines = true;
    }

    public void noDrawLines() {
        if (drawLines) drawLines = false;
    }

    public void drawGrid(boolean draw) {
        this.drawGrid = draw;
    }

    /**
     * Renders the current frame
     * @param target Target for rendering
     */
    @Override
    public void renderFrame(RenderTarget target) {
        if (target == null) throw new DiamondException(this.getClass(), "Tried to render from null target");
        glClearColor(0.1f, 0.1f, 0.1f, 1.0f);
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        if (drawGrid) renderGrid();
        LineRenderer.draw(target.getTargetView(), target.getTargetProjection(), logger);

    }

    @Override
    public void renderFrame(Matrix4f view, Matrix4f projection) {
        glClearColor(0.1f, 0.1f, 0.1f, 1.0f);
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        if (drawGrid) renderGrid();
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

    public int bindMesh(float[] vertices) {
        int VBO, VAO;
        VAO = glGenVertexArrays();
        VBO = glGenBuffers();
        glBindVertexArray(VAO);

        glBindBuffer(GL_ARRAY_BUFFER, VBO);
        glBufferData(GL_ARRAY_BUFFER, vertices, GL_STATIC_DRAW);

        glVertexAttribPointer(0, 3, GL_FLOAT, false, 5 * Float.BYTES, 0);
        glEnableVertexAttribArray(0);
        glVertexAttribPointer(1, 2, GL_FLOAT, false, 5 * Float.BYTES, 3 * Float.BYTES);
        glEnableVertexAttribArray(1);
        return VAO;
    }

    public void renderModel(int VAO, int size, Matrix4f view, Matrix4f projection, Matrix4f transform, Texture texture, GLShader shader) {
        glBindTexture(GL_TEXTURE_2D, texture.getId());
        shader.uploadMat4f("view", view);
        shader.uploadMat4f("projection", projection);
        shader.uploadMat4f("model", transform);
        glBindVertexArray(VAO);
        glDrawArrays(GL_TRIANGLES, 0, size);
    }

    private void renderGrid() {
        Vector3f gridColor = new Vector3f(0.5f, 0.5f, 0.5f);
        Vector3f originColor = new Vector3f(0.9f, 0.9f, 0.9f);

        // Main origin Axis
        addLine(new Vector3f(-1000, 0, 0), new Vector3f(1000, 0, 0), originColor);
        addLine(new Vector3f(0, -1000, 0), new Vector3f(0, 1000, 0), originColor);
        addLine(new Vector3f(0, 0, -1000), new Vector3f(0, 0, 1000), originColor);

        // X-Z plane grid
        int half = NUM_GRID_LINES / 2;
        for (int i = 0; i < NUM_GRID_LINES; i++) {
            if (-half + i != 0) {
                addLine(new Vector3f(-half, 0, -half + i), new Vector3f(half, 0, -half + i), gridColor);
                addLine(new Vector3f(-half + i, 0, -half), new Vector3f(-half + i, 0, half), gridColor);
            }
        }
    }

    @Override
    public void setViewPort(int width, int height) {

    }

    @Override
    public void adjustSize(int width, int height) {
        glViewport(0, 0, width, height);
    }

    private static class LineRenderer {

        // CONSTANTS
        private static final int ATTR_PER_LINE = 12;                        // There are 12 floats worth of attributes per line: 6 per vertex, in each vertex 3 por position and 3 for color
        private static final int MAX_LINES = 10000;
        private static final String VERTEX =
                """
                        #version 330 core
                        layout (location=0) in vec3 attrPos;
                        layout (location=1) in vec3 attrColor;
                        uniform mat4 uProjection;
                        uniform mat4 uView;
                        uniform int uType;
                        out vec3 fragColor;
                        out vec3 fragPos;
                        out int type;
                        void main() {
                            fragColor = attrColor;
                            type = uType;
                            gl_Position = uProjection * uView * vec4(attrPos, 1.0);
                            fragPos = vec3(gl_Position.x,gl_Position.y,gl_Position.z);
                        }""";
        private static final String FRAGMENT =
                """
                        #version 330 core
                        in vec3 fragColor;
                        in vec3 fragPos;
                        out vec4 color;
                        void main() {
                            color = vec4(fragColor, 1);
                        }""";

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
                vaoID = glGenVertexArrays();
                glBindVertexArray(vaoID);

                vboID = glGenBuffers();
                glBindBuffer(GL_ARRAY_BUFFER, vboID);
                glBufferData(GL_ARRAY_BUFFER, (long) vertexArray.length * Float.BYTES, GL_DYNAMIC_DRAW);

                glVertexAttribPointer(0, 3, GL_FLOAT, false, 6 * Float.BYTES, 0);
                glEnableVertexAttribArray(0);
                glVertexAttribPointer(1, 3, GL_FLOAT, false, 6 * Float.BYTES, 3 * Float.BYTES);
                glEnableVertexAttribArray(1);
                glLineWidth(2f);
                started = true;
            }

            glBindBuffer(GL_ARRAY_BUFFER, vboID);
            glBufferData(GL_ARRAY_BUFFER, vertexArray, GL_DYNAMIC_DRAW);

            // Use our shader
            lineShader.use();
            lineShader.uploadMat4f("uProjection", projection);
            lineShader.uploadMat4f("uView", view);
            lineShader.uploadInt("uType", 0);

            // Bind the vao
            glBindVertexArray(vaoID);
            glEnableVertexAttribArray(0);
            glEnableVertexAttribArray(1);

            // Draw the batch
            glEnable(GL_BLEND);
            glDrawArrays(GL_LINES, 0, lineCount * 2);

            // Disable Location
            glDisableVertexAttribArray(0);
            glDisableVertexAttribArray(1);
            glBindVertexArray(0);

            // Unbind shader, and reset line index to O
            lineShader.detach();
            lineCount = 0;
        }
    }
}
