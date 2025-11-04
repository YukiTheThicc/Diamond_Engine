import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import utils.DiaMath;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * DiaMathTest
 *
 * @author Santiago Barreiro
 */
public class DiaMathTest {

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void close() {
    }

    @Test
    void testGeneration() {
        float random1_10 = DiaMath.randomFloat(-10f, 10f);
        System.out.println("===============> random1_10: " + random1_10);
        assertTrue(random1_10 > -10f && random1_10 < 10f);
    }
}
