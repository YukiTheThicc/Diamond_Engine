import api.DiaEntityPool.*;
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
        sut.createEntity(new IDiaComponent[]{new Transform(new Vector3f(1, 1, 1), new Vector3f(), new Vector3f())});
        IDiaComponent[] components = sut.retrieveEntity(0);
        assertNotNull(components);

    }
}
