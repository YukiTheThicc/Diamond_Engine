package stage_0;

import api.*;
import core.*;
import org.joml.Vector3f;
import core.glRenderer.GLAssetLoader;
import core.glRenderer.GLRenderer;
import utils.Logger;

import static org.lwjgl.opengl.GL11.glViewport;

/**
 * Test_Version
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

    public static void main(String[] args) {

        logger = new Logger();
        window = GLFWWindow.get();
        window.init(800, 600);
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
        glRenderer.init();
        glRenderer.drawGrid(true);
        alma = new Alma();

        diamond = new Diamond(
                window,
                glRenderer,
                alma,
                logger,
                target
        );

        target.getCamera().moveTo(new Vector3f(-5f, 3f, -5f));
        target.getCamera().rotate(45f, -30f, 0f);

        // Add resize listener for the window to adjust viewport
        window.addResizeObserver(new DiaWindow.ResizeObserver() {
            @Override
            public void adjustSize(int width, int height) {
                glViewport(0, 0, width, height);
            }
        });

        diamond.start();
    }


}
