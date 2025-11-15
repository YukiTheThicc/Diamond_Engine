import api.DiaEntityPool.*;
import api.DiaSystem;
import core.assets.Texture;
import core.components.Mesh;
import core.components.Transform;
import core.simpleEntities.SimpleEntities;
import org.joml.Vector3f;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * SimpleEntitiesTest
 *
 * @author Santiago Barreiro
 */
public class SimpleEntitiesTest {

    SimpleEntities sut;

    @BeforeEach
    void setUp() {
        sut = new SimpleEntities();
    }

    @AfterEach
    void close() {
    }

    @Test
    void testCreate() {
        sut.createEntity(new Object[]{new Transform(new Vector3f(1, 1, 1), new Vector3f(), new Vector3f())});
        Object[] components = sut.retrieveEntity(0);
        assertNotNull(components);
    }

    @Test
    void testSystem() {
        sut.createEntity(new Object[]{new Transform(new Vector3f(1, 1, 1), new Vector3f(), new Vector3f())});
        sut.createEntity(new Object[]{new Transform(new Vector3f(2, 2, 2), new Vector3f(), new Vector3f())});
        sut.createEntity(new Object[]{new Mesh(new float[]{0f}, new Texture(0,0,0,"path"))});
        sut.createEntity(new Object[]{new Transform(new Vector3f(2, 2, 2), new Vector3f(), new Vector3f()),
                new Mesh(new float[]{0f}, new Texture(0,0,0,"path"))});
        // System that adds 2 * deltaTime to every component of the position vector of a transform
        final int[] count = {0,0};
        sut.scheduleSystem(new DiaSystem() {
            @Override
            public void execute(Object[] components, float dt) {
                for (Object component : components) {
                    if (component instanceof Transform) {
                        ((Transform) component).pos.add(2 * dt,2 * dt,2 * dt);
                        count[0]++;
                    }
                }
            }
        });

        sut.scheduleSystem(new DiaSystem() {

            @Override
            public void execute(Object[] components, float dt) {
                for (Object component : components) {
                    if (component instanceof Mesh) {
                        count[1]++;
                    }
                }
            }
        });
        sut.dispatchSystems(1);
        Object[] components = sut.retrieveEntity(0);
        assertEquals(3f, ((Transform) components[0]).pos.x);
        assertEquals(3, count[0]);
        assertEquals(2, count[1]);
    }
}
