import api.DiaEntityPool;
import core.EventPool.*;
import core.EventPool;
import core.simpleEntities.SimpleEntities;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * EventPoolTest
 *
 * @author Santiago Barreiro
 */
public class EventPoolTest {

    // Two enums with the same value to test what happens with different enums with the same declared values
    private enum TestTypes1 {
        type1
    }

    private enum TestTypes2 {
        type1
    }

    @BeforeEach
    void setUp() {
        EventPool.init();
    }

    @AfterEach
    void close() {
    }

    @Test
    void test() {

        EventObserver ob1 = event -> {
            assertEquals(TestTypes1.type1, event.type);
            System.out.println("Event observer 1");
        };
        EventObserver ob2 = event -> {
            assertEquals(TestTypes2.type1, event.type);
            System.out.println("Event observer 2");
        };

        EventPool.throwEvent(new Event(TestTypes1.type1));
        EventPool.throwEvent(new Event(TestTypes1.type1));
        EventPool.throwEvent(new Event(TestTypes2.type1));
        EventPool.addObserver(TestTypes1.type1, ob1);
        EventPool.addObserver(TestTypes2.type1, ob2);
        EventPool.dispatchEvents();
        EventPool.removeObserver(ob2);
        EventPool.throwEvent(new Event(TestTypes1.type1));
        EventPool.throwEvent(new Event(TestTypes2.type1));
        EventPool.dispatchEvents();
    }
}
