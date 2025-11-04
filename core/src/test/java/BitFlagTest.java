import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import utils.BitFlag;

import static org.junit.jupiter.api.Assertions.assertEquals;

class BitFlagTest {

    BitFlag sut;

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void close() {
    }

    @Test
    void testGetFlag() {
        sut = new BitFlag(new int[]{3, 14, 31, 44});
        boolean expectedPos3 = true;
        boolean expectedPos10 = false;
        boolean expectedPos31 = true;
        assertEquals(expectedPos3, sut.getFlag(3));
        assertEquals(expectedPos10, sut.getFlag(10));
        assertEquals(expectedPos31, sut.getFlag(31));
    }

    @Test
    void testSetFlag() {

        sut = new BitFlag(new int[]{3});
        sut.setFlag(3, false);
        sut.setFlag(33, true);
        sut.setFlag(65, true);
        boolean expectedPos3 = false;
        boolean expectedPos33 = true;
        boolean expectedPos65 = true;
        assertEquals(expectedPos3, sut.getFlag(3));
        assertEquals(expectedPos33, sut.getFlag(33));
        assertEquals(expectedPos65, sut.getFlag(65));
    }

    @Test
    void testFlipFlag() {

        sut = new BitFlag(new int[]{3, 65});
        sut.flipFlag(3);
        sut.flipFlag(33);
        boolean expectedPos3 = false;
        boolean expectedPos33 = true;
        boolean expectedPos65 = true;
        assertEquals(expectedPos3, sut.getFlag(3));
        assertEquals(expectedPos33, sut.getFlag(33));
        assertEquals(expectedPos65, sut.getFlag(65));
    }

    @Test
    void getNext() {
        int[] expected = new int[]{3, 18, 65, 125};
        int[] actual = new int[4];
        sut = new BitFlag(expected);
        final int expectedIterations = 8;
        int iterations = 0;

/*
        int i = 0;
        int next = 0;
        int segment = sut.getSegment(i);
        int previous = 0;
        while (i < sut.getSegmentCount()) {
            next = Integer.numberOfLeadingZeros(segment);
            if (next == 32) {
                i++;
                previous = 0;
                if (i < sut.getSegmentCount()) segment = sut.getSegment(i);
            } else {
                segment = segment << (next + 1);
                System.out.println(i * 32 + next + previous);
                previous += next + 1;
            }
            iterations++;
        }*/

        int i = 0;
        int next = 0;
        int segment = sut.getSegment(i);
        int previous = 0;
        while (i < sut.getSegmentCount()) {
            next = Integer.numberOfLeadingZeros(segment);
            if (next == 32) {
                i++;
                previous = 0;
                if (i < sut.getSegmentCount()) segment = sut.getSegment(i);
            } else {
                segment = segment << (next + 1);
                System.out.println(i * 32 + next + previous);
                previous += next + 1;
            }
            iterations++;
        }

        assertEquals(expectedIterations, iterations);
    }

    @Test
    void testClearSegment() {
        sut = new BitFlag(new int[]{33, 45, 55});
        sut.clearSegment(1);
        int actual = sut.getSegment(1);
        assertEquals(0, actual);
    }
}