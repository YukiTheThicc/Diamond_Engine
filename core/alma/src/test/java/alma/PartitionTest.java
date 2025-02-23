package alma;

import alma.api.IClassIndex;
import alma.api.IComponent;
import alma.architecture.Partition;
import alma.utils.AlmaException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import utils.TestComponent;
import utils.TestUtils;

import java.util.Arrays;
import java.util.Iterator;

import static org.junit.jupiter.api.Assertions.*;

class PartitionTest {

    private Partition sut1;
    private Partition sut2;
    private Partition sut3;
    private final IComponent[] c1 = new IComponent[]{new C1()};
    private final IComponent[] c1c2 = new IComponent[]{new C1(), new C2()};
    private final IClassIndex index = new IClassIndex() {
        private int index = 1;
        // Used to map each class to an Integer value
        private final ClassValue<Integer> classIndex = new ClassValue<>() {
            @Override
            protected Integer computeValue(Class<?> type) {
                return index++;
            }
        };

        @Override
        public int get(Class<?> type) {
            return classIndex.get(type);
        }

        @Override
        public int[] getIndexArray(Class<?>[] types) {
            return new int[0];
        }

        @Override
        public Class<?>[] getComponentClasses(Object[] components) {
            return new Class[0];
        }

        @Override
        public IntKey getCompositionHash(Class<?>[] components) {
            return null;
        }

        @Override
        public IntKey getCompositionHash(Object[] components) {
            return null;
        }
    };

    static class C1 extends TestComponent {
        public C1(int value) {
            super(value);
        }

        public C1() {
            super();
        }
    }

    static class C2 extends TestComponent {
        public C2(int value) {
            super(value);
        }

        public C2() {
            super();
        }
    }

    static class C3 extends TestComponent {
        public C3(int value) {
            super(value);
        }
    }

    enum TestState {
        STATE1,
        STATE2
    }

    @BeforeEach
    void setUp() {
        sut1 = new Partition(1, new IdHandler(), index, 1, new int[]{-1, 0});
        sut2 = new Partition(2, new IdHandler(), index, 2, new int[]{-1, 0, 1});
        sut3 = new Partition(3, new IdHandler(), index, 3, new int[]{-1, 0, 1, 2});
    }

    @AfterEach
    void tearDown() {
        TestUtils.printTestEnd();
    }

    @Test
    void testAddEntity() {

        TestUtils.printTestHeader("testAddEntity");
        int expectedId1 = 1 << 19;
        int expectedId2 = 1 | 1 << 19;
        int expectedId3 = 2 | 1 << 19;
        int expectedId4 = 2 << 19;
        int expectedId5 = 1 | 2 << 19;
        int expectedId6 = 2 | 2 << 19;
        int actualId1 = sut1.addEntityUnsafe(c1);
        int actualId2 = sut1.addEntityUnsafe(c1);
        int actualId3 = sut1.addEntityUnsafe(c1);
        int actualId4 = sut2.addEntityUnsafe(c1c2);
        int actualId5 = sut2.addEntityUnsafe(c1c2);
        int actualId6 = sut2.addEntityUnsafe(c1c2);

        TestUtils.printTestIteration("P1 Added ID 1", TestUtils.intToBinaryString(expectedId1), TestUtils.intToBinaryString(actualId1));
        TestUtils.printTestIteration("P1 Added ID 2", TestUtils.intToBinaryString(expectedId2), TestUtils.intToBinaryString(actualId2));
        TestUtils.printTestIteration("P1 Added ID 3", TestUtils.intToBinaryString(expectedId3), TestUtils.intToBinaryString(actualId3));
        TestUtils.printTestIteration("P2 Added ID 4", TestUtils.intToBinaryString(expectedId4), TestUtils.intToBinaryString(actualId4));
        TestUtils.printTestIteration("P2 Added ID 5", TestUtils.intToBinaryString(expectedId5), TestUtils.intToBinaryString(actualId5));
        TestUtils.printTestIteration("P2 Added ID 6", TestUtils.intToBinaryString(expectedId6), TestUtils.intToBinaryString(actualId6));
        assertEquals(expectedId1, actualId1);
        assertEquals(expectedId2, actualId2);
        assertEquals(expectedId3, actualId3);
        assertEquals(expectedId4, actualId4);
        assertEquals(expectedId5, actualId5);
        assertEquals(expectedId6, actualId6);
    }

    @Test
    void testSize() {

        TestUtils.printTestHeader("testSize");
        sut1.addEntityUnsafe(c1);
        sut1.addEntityUnsafe(c1);
        int expected1 = 2;
        int actual1 = sut1.size();
        sut1.addEntityUnsafe(c1);
        sut1.addEntityUnsafe(c1);
        sut1.addEntityUnsafe(c1);
        sut1.addEntityUnsafe(c1);
        int expected2 = 6;
        int actual2 = sut1.size();
        TestUtils.printTestIteration("Add 2 size", expected1, actual1);
        TestUtils.printTestIteration("Add 6 size", expected2, actual2);
        assertEquals(expected1, actual1);
        assertEquals(expected2, actual2);
    }

    @Test
    void testRemove() {

        TestUtils.printTestHeader("testRemove");
        IComponent[] expectedC = new IComponent[]{new C1(4), new C2(5), new C3(6)};
        sut3.addEntityUnsafe(new IComponent[]{new C1(1), new C2(2), new C3(3)});
        int removed = sut3.addEntityUnsafe(expectedC);
        sut3.addEntityUnsafe(new IComponent[]{new C1(7), new C2(8), new C3(9)});
        sut3.removeEntity(removed);
        int pooled = sut3.addEntityUnsafe(new IComponent[]{new C1(10), new C2(11), new C3(12)});
        sut3.addEntityUnsafe(new IComponent[]{new C1(13), new C2(14), new C3(15)});
        int expectedSize = 4;
        int actualSize = sut3.size();
        IComponent[] actualC = sut3.fetchEntityComponents(pooled);
        TestUtils.printTestIteration("Add 5 remove 1", expectedSize, actualSize);
        TestUtils.printTestIteration("Component list", expectedC, actualC);
        assertEquals(expectedSize, actualSize);
        assertArrayEquals(expectedC, actualC);
    }

    @Test
    void testFetchEntityComponents() {
        TestUtils.printTestHeader("testFetchEntityComponents");
        IComponent[] expected = new IComponent[]{new C1(4), new C2(5), new C3(6)};
        sut3.addEntityUnsafe(new IComponent[]{new C1(1), new C2(2), new C3(3)});
        int expectedId = sut3.addEntityUnsafe(expected);
        sut3.addEntitySafe(new IComponent[]{new C2(4), new C3(5), new C1(6)});
        IComponent[] actual = sut3.fetchEntityComponents(expectedId);
        IComponent[] actualSelect2of3 = sut3.fetchEntityComponents(expectedId, new int[]{1, 2});

        TestUtils.printTestIteration("Fetched components", expected, actual);
        TestUtils.printTestIteration("Fetched components", expected, actualSelect2of3);
        assertArrayEquals(expected, actual);
        assertThrows(AlmaException.class, () -> sut2.fetchEntityComponents(expectedId));
    }

    @Test
    void testSetState() {
        TestUtils.printTestHeader("testAddEntity");
        sut1.addEntityUnsafe(c1);
        int added = sut1.addEntityUnsafe(c1);
        sut1.addEntityUnsafe(c1);
        sut1.addEntityState(TestState.STATE1, added);
        sut1.addEntityState(TestState.STATE2, added);
    }

    @Test
    void testAdding500000_2() {
        TestUtils.printTestHeader("testAdding500000_2 - STRESS TEST");

        final int ITERATIONS = 5;
        for (int i = 1; i <= ITERATIONS; i++) {

            sut2 = new Partition(2, new IdHandler(12, 12), index, 2, new int[]{1, 2});
            int partitionSize = (int) (Math.random() * 500000 + 2001);
            int removeInterval = (int) (Math.random() * 100 + 31);
            int expectedFinalSize = partitionSize - (partitionSize / removeInterval) - (partitionSize % removeInterval != 0 ? 1 : 0);
            int expectedChunksUsed = (int) Math.ceil((float) expectedFinalSize / 4096);

            int randomEntityComponentCheck = (int) (Math.random() * expectedFinalSize + 0);
            int randomComponentCheckEntity = -1;
            IComponent[] randomExpectedComponents = null;
            boolean randomEliminated = false;

            int previousEntity;
            for (int j = 0; j < partitionSize; j++) {
                IComponent[] components = new IComponent[]{new C1((int) (Math.random() * Integer.MAX_VALUE + 1)), new C2((int) (Math.random() * Integer.MAX_VALUE + 1))};
                previousEntity = sut2.addEntityUnsafe(components);
                if (j % removeInterval == 0) {
                    sut2.removeEntity(previousEntity);
                }
                if (j == randomEntityComponentCheck) {
                    if (j % removeInterval == 0) {
                        randomEliminated = true;
                    } else {
                        randomComponentCheckEntity = previousEntity;
                        randomExpectedComponents = Arrays.copyOf(components, 2);
                    }
                }
            }

            TestUtils.printTestIteration("Final size (ps:" + partitionSize + "|ri:" + removeInterval + ")", expectedFinalSize, sut2.size());
            TestUtils.printTestIteration("Chunks used (ps:" + partitionSize + "|ri:" + removeInterval + ")", expectedChunksUsed, sut2.usedChunks());
            assertEquals(expectedFinalSize, sut2.size());
            assertEquals(expectedChunksUsed, sut2.usedChunks());
            if (!randomEliminated) {
                TestUtils.printTestIteration("Random component check used (ps:" + partitionSize + "|ri:" + removeInterval + ")",
                        Arrays.toString(randomExpectedComponents), Arrays.toString(sut2.fetchEntityComponents(randomComponentCheckEntity)));
                assertArrayEquals(randomExpectedComponents, sut2.fetchEntityComponents(randomComponentCheckEntity));
            }
        }
    }

    @Test
    void testAdding500000_3() {
        TestUtils.printTestHeader("testAdding500000_3 - STRESS TEST");

        final int ITERATIONS = 5;
        for (int i = 1; i <= ITERATIONS; i++) {

            sut3 = new Partition(3, new IdHandler(12, 12), index, 3, new int[]{1, 2, 3});
            int partitionSize = (int) (Math.random() * 500000 + 2001);
            int removeInterval = (int) (Math.random() * 100 + 31);
            int expectedFinalSize = partitionSize - (partitionSize / removeInterval) - (partitionSize % removeInterval != 0 ? 1 : 0);
            int expectedChunksUsed = (int) Math.ceil((float) expectedFinalSize / 4096);

            int randomEntityComponentCheck = (int) (Math.random() * expectedFinalSize + 0);
            int randomComponentCheckEntity = -1;
            IComponent[] randomExpectedComponents = null;
            boolean randomEliminated = false;

            int previousEntity;
            for (int j = 0; j < partitionSize; j++) {
                IComponent[] components = new IComponent[]{
                        new C1((int) (Math.random() * Integer.MAX_VALUE + 1)),
                        new C2((int) (Math.random() * Integer.MAX_VALUE + 1)),
                        new C3((int) (Math.random() * Integer.MAX_VALUE + 1))
                };
                previousEntity = sut3.addEntityUnsafe(components);
                if (j % removeInterval == 0) {
                    sut3.removeEntity(previousEntity);
                }
                if (j == randomEntityComponentCheck) {
                    if (j % removeInterval == 0) {
                        randomEliminated = true;
                    } else {
                        randomComponentCheckEntity = previousEntity;
                        randomExpectedComponents = Arrays.copyOf(components, 3);
                    }
                }
            }

            TestUtils.printTestIteration("Final size (ps:" + partitionSize + "|ri:" + removeInterval + ")", expectedFinalSize, sut3.size());
            TestUtils.printTestIteration("Chunks used (ps:" + partitionSize + "|ri:" + removeInterval + ")", expectedChunksUsed, sut3.usedChunks());
            assertEquals(expectedFinalSize, sut3.size());
            assertEquals(expectedChunksUsed, sut3.usedChunks());
            if (!randomEliminated) {
                TestUtils.printTestIteration("Random component check used (ps:" + partitionSize + "|ri:" + removeInterval + ")",
                        Arrays.toString(randomExpectedComponents), Arrays.toString(sut3.fetchEntityComponents(randomComponentCheckEntity)));
                assertArrayEquals(randomExpectedComponents, sut3.fetchEntityComponents(randomComponentCheckEntity));
            }
        }
    }

    @Test
    void testPartitionIterator() {

        TestUtils.printTestHeader("testPartitionIterator");
        IComponent[] expectedArray = new IComponent[]{new C1(3), new C2(4)};
        sut2.addEntityUnsafe(new IComponent[]{new C1(1), new C2(2)});
        sut2.addEntityUnsafe(expectedArray);
        sut2.addEntityUnsafe(new IComponent[]{new C1(5), new C2(6)});

        Iterator<Entity> iTest2 = sut2.iterator(new int[]{1, 2});
        Iterator<Entity> iFailure = sut3.iterator(new int[]{1, 2});

        iTest2.next();
        Entity e = iTest2.next();

        TestUtils.printTestIteration("has next after 2", true, iTest2.hasNext());
        TestUtils.printTestIteration("2nd Entity components", expectedArray, e.components());
        assertTrue(iTest2.hasNext());
        assertArrayEquals(expectedArray, e.components());
        assertThrows(AlmaException.class, iFailure::next);
    }

    @Test
    void testPartitionIteratorWithState() {

        TestUtils.printTestHeader("testPartitionIterator");
        IComponent[] expectedArray = new IComponent[]{new C1(3), new C2(4)};
        sut2.addEntityUnsafe(new IComponent[]{new C1(1), new C2(2)});
        sut2.addEntityUnsafe(expectedArray);
        sut2.addEntityUnsafe(new IComponent[]{new C1(5), new C2(6)});

        Iterator<Entity> iTest2 = sut2.iterator(new int[]{1, 2});
        Iterator<Entity> iFailure = sut3.iterator(new int[]{1, 2});

        iTest2.next();
        Entity e = iTest2.next();

        TestUtils.printTestIteration("has next after 2", true, iTest2.hasNext());
        TestUtils.printTestIteration("2nd Entity components", expectedArray, e.components());
        assertTrue(iTest2.hasNext());
        assertArrayEquals(expectedArray, e.components());
        assertThrows(AlmaException.class, iFailure::next);
    }
}