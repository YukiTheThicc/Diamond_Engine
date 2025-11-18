package alma;

import alma.architecture.QueryResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import utils.TestComponent;

import java.util.Iterator;

import static org.junit.jupiter.api.Assertions.assertEquals;

class AlmaPoolTest {

    static class C1 extends TestComponent {
        public C1(int value) {
            super(value);
        }
    }

    static class C2 extends TestComponent {
        public C2(int value) {
            super(value);
        }
    }

    static class C3 extends TestComponent {
        public C3(int value) {
            super(value);
        }
    }

    AlmaPool sut;

    @BeforeEach
    void setUp() {
        sut = new AlmaPool();
    }

    @Test
    void testCreateEntity() {
        Object[] composition1 = new Object[]{new C1(1)};
        Object[] composition12 = new Object[]{new C1(2), new C2(3)};
        int actual1 = sut.createEntity(composition1);
        int actual2 = sut.createEntity(composition12);
        assertEquals(0, actual1);
        assertEquals(1 << 19, actual2);
    }

    @Test
    void testQueryEntitiesFullJoin() {
        Object[] composition231 = new Object[]{new C2(12), new C3(13), new C1(14)};
        Object[] composition123 = new Object[]{new C1(4), new C2(5), new C3(6)};
        Object[] composition23 = new Object[]{new C2(7), new C3(8)};
        Object[] composition312 = new Object[]{new C3(9), new C1(10), new C2(11)};
        sut.createEntity(composition231);
        sut.createEntity(composition123);
        sut.createEntity(composition23);
        sut.createEntity(composition312);
        Iterator<Entity> results = sut.queryEntitiesWith(new Class<?>[]{C2.class, C3.class}).getResults();
        if (results != null) {
            while (results.hasNext()) {
                Entity result = results.next();
                for (int i = 0; i < result.components().length; i++) {
                    System.out.println(result.components()[i]);
                }
            }
        }
    }
}