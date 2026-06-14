package stage_0;

import api.*;
import core.*;
import core.assets.Texture;
import core.components.Mesh;
import core.components.Transform;
import core.glRenderer.GLShader;
import core.simpleEntities.SimpleEntities;
import org.joml.Vector3f;
import core.glRenderer.GLAssetLoader;
import core.glRenderer.GLRenderer;
import utils.DiaMath;
import utils.Logger;

import java.util.Iterator;

import static org.lwjgl.opengl.GL11.*;

/**
 * Test_Version
 * TEST IMPLEMENTING 3D TEXTURED RENDERING USING THE ENGINES OWN DATA ARCHITECTURE AND RENDERING PIPELINE
 *
 * @author: Santiago Barreiro
 */
public class Test_Update3 {

    // ATTRIBUTES
    static Diamond diamond;
    static DiaLogger logger;
    static DiaWindow window;
    static RenderTarget target;
    static AssetPool assetPool;
    static GLRenderer glRenderer;
    static DiaEntityPool alma;
    static DiaEntityPool simpleEntities;

    public static void main(String[] args) {

        logger = new Logger();
        window = GLFWWindow.get();
        target = new RenderTarget(
                new Camera(
                        new Vector3f(0f, 0f, -1f),
                        new Vector3f(0f, 1f, 0f),
                        90f,
                        4f,
                        0.75f
                ),
                4f, 3f, 0.1f, 1000f
        );
        assetPool = new AssetPool(logger, new GLAssetLoader(logger));
        glRenderer = new GLRenderer(logger);
        glRenderer.drawGrid(true);
        alma = new AlmaWrapper();
        simpleEntities = new SimpleEntities();

        diamond = new Diamond(
                window,
                glRenderer,
                simpleEntities,
                logger,
                target
        );
        window.init(800, 600);
        EventPool.init();
        simpleEntities.init();
        glRenderer.init();

        setup();

        diamond.start();
    }

    static void setup() {

        final String vertexShaderTextureSource =
                "#version 330 core\n" +
                        "layout (location = 0) in vec3 aPos;\n" +
                        "layout (location = 1) in vec2 aTexCoords;\n" +
                        "out vec2 texCoords;\n" +
                        "uniform mat4 model;\n" +
                        "uniform mat4 view;\n" +
                        "uniform mat4 projection;\n" +
                        "void main()\n" +
                        "{\n" +
                        "   gl_Position = projection * view * model * vec4(aPos, 1.0);\n" +
                        "   texCoords = vec2(aTexCoords.x, aTexCoords.y);\n" +
                        "}\0";
        final String fragmentShaderTextureSource =
                "#version 330 core\n" +
                        "in vec2 texCoords;\n" +
                        "out vec4 FragColor;\n" +
                        "uniform sampler2D sampledTexture;\n" +
                        "void main()\n" +
                        "{\n" +
                        "   FragColor = texture(sampledTexture, texCoords);\n" +
                        "}\n\0";

        GLShader textureShader;
        textureShader = new GLShader(vertexShaderTextureSource, fragmentShaderTextureSource);
        textureShader.compile(logger);
        float[] vertices = {
                -0.5f, -0.5f, -0.5f, 0.0f, 0.0f,
                0.5f, -0.5f, -0.5f, 1.0f, 0.0f,
                0.5f, 0.5f, -0.5f, 1.0f, 1.0f,
                0.5f, 0.5f, -0.5f, 1.0f, 1.0f,
                -0.5f, 0.5f, -0.5f, 0.0f, 1.0f,
                -0.5f, -0.5f, -0.5f, 0.0f, 0.0f,

                -0.5f, -0.5f, 0.5f, 0.0f, 0.0f,
                0.5f, -0.5f, 0.5f, 1.0f, 0.0f,
                0.5f, 0.5f, 0.5f, 1.0f, 1.0f,
                0.5f, 0.5f, 0.5f, 1.0f, 1.0f,
                -0.5f, 0.5f, 0.5f, 0.0f, 1.0f,
                -0.5f, -0.5f, 0.5f, 0.0f, 0.0f,

                -0.5f, 0.5f, 0.5f, 1.0f, 0.0f,
                -0.5f, 0.5f, -0.5f, 1.0f, 1.0f,
                -0.5f, -0.5f, -0.5f, 0.0f, 1.0f,
                -0.5f, -0.5f, -0.5f, 0.0f, 1.0f,
                -0.5f, -0.5f, 0.5f, 0.0f, 0.0f,
                -0.5f, 0.5f, 0.5f, 1.0f, 0.0f,

                0.5f, 0.5f, 0.5f, 1.0f, 0.0f,
                0.5f, 0.5f, -0.5f, 1.0f, 1.0f,
                0.5f, -0.5f, -0.5f, 0.0f, 1.0f,
                0.5f, -0.5f, -0.5f, 0.0f, 1.0f,
                0.5f, -0.5f, 0.5f, 0.0f, 0.0f,
                0.5f, 0.5f, 0.5f, 1.0f, 0.0f,

                -0.5f, -0.5f, -0.5f, 0.0f, 1.0f,
                0.5f, -0.5f, -0.5f, 1.0f, 1.0f,
                0.5f, -0.5f, 0.5f, 1.0f, 0.0f,
                0.5f, -0.5f, 0.5f, 1.0f, 0.0f,
                -0.5f, -0.5f, 0.5f, 0.0f, 0.0f,
                -0.5f, -0.5f, -0.5f, 0.0f, 1.0f,

                -0.5f, 0.5f, -0.5f, 0.0f, 1.0f,
                0.5f, 0.5f, -0.5f, 1.0f, 1.0f,
                0.5f, 0.5f, 0.5f, 1.0f, 0.0f,
                0.5f, 0.5f, 0.5f, 1.0f, 0.0f,
                -0.5f, 0.5f, 0.5f, 0.0f, 0.0f,
                -0.5f, 0.5f, -0.5f, 0.0f, 1.0f
        };

        target.getCamera().moveTo(new Vector3f(-5f, 3f, -5f));
        target.getCamera().rotate(45f, -30f, 0f);

        // Add resize listener for the window to adjust viewport
        window.addResizeObserver(new DiaWindow.ResizeObserver() {
            @Override
            public void adjustSize(int width, int height) {
                glViewport(0, 0, width, height);
            }
        });

        DiaAssetLoader assetLoader = new GLAssetLoader(logger);
        String exampleTexture = System.getProperty("user.dir") + "\\examples\\res\\top.png";
        System.out.println("Attempting to load example texture from = " + exampleTexture + "...");
        Texture texture = assetLoader.loadTexture(exampleTexture);

        for (int i = 0; i < 10; i++) {
            simpleEntities.createEntity(new Object[]{
                    new Transform(
                            new Vector3f(DiaMath.randomFloat(-3f, 3f), DiaMath.randomFloat(-3f, 3f), DiaMath.randomFloat(-2f, -12f)),
                            new Vector3f(1, 1, 1),
                            new Vector3f()),
                    new Mesh(vertices, texture)
            });
        }

        simpleEntities.scheduleSystem(new DiaSystem() {

            @Override
            public void execute(DiaEntityPool pool, float dt) {
                Iterator<SimpleEntities.Entity> i = pool.queryEntitiesWith(new Class[]{Transform.class, Mesh.class});
                while (i.hasNext()) {
                    SimpleEntities.Entity e =  i.next();
                    for (Object o : e.components()) {
                        if (o instanceof Transform) {
                            ((Transform) o).pos.x += 1;
                            System.out.println(((Transform) o).pos);
                        }
                    }
                }
            }
        });
    }
}
