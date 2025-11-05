package alma.utils;

import java.util.Arrays;

/**
 * BitIndex
 *
 * @author Santiago Barreiro
 */
public final class BitFlag {

    // CONSTANTS
    private final static int INT_SIZE = 31;
    private final static int LEFT_MOST_BIT = 1 << 31;
    private final static int SEGMENT_INDEX_SHIFT = 5;

    // ATTRIBUTES
    private int[] data;
    private int bitsSet;

    // CONSTRUCTORS

    /**
     * Creates a bit flag with the provided indexes set to true by default
     * @param indexes Indexes that will be set to 1
     */
    public BitFlag(int[] indexes) {
        data = new int[1];
        for (int index : indexes) {
            setFlag(index, true);
        }
    }

    /**
     * Creates a new bit flag with the provided size
     * @param size Size of the int array for the flags (number of total flags will be 32*size)
     */
    public BitFlag(int size) {
        data = new int[size];
    }

    // GETTER
    /**
     * @return Number of flags set to 1
     */
    public int getBitsSet() {
        return bitsSet;
    }

    // METHODS

    /**
     * Checks if the provided position is outside the current flag array possible size and expands it if necessary
     * @param pos Index to check if it is inside the flag array
     */
    private void checkSize(int pos) {
        if ((pos >> SEGMENT_INDEX_SHIFT) >= data.length) data = Arrays.copyOf(data, (pos >> SEGMENT_INDEX_SHIFT) + 1);
    }

    /**
     * @return Returns the length of the internal int array used for the bit flags
     */
    public int getSegmentCount() {
        return data.length;
    }

    /**
     * @param i Index of the segment to query
     * @return Returns the segment of the internal int array with the provided index. Does NOT check for null indexes
     */
    public int getSegment(int i) {
        return data[i];
    }

    /**
     * Resets a segment with all flags to 0
     * @param segment Index of the segment to reset
     */
    public void clearSegment(int segment) {
        data[segment] = 0;
    }

    /**
     * Clears all the flags and sets them to false
     */
    public void clear() {
        for (int i = 0; i < data.length; i++) clearSegment(i);
    }

    /**
     * Gets the value of one flag
     * @param pos Index of the flag to query
     * @return Returns if the flag is set to 1 and false if it is 0
     */
    public boolean getFlag(int pos) {
        checkSize(pos);
        return (data[pos >> SEGMENT_INDEX_SHIFT] & (LEFT_MOST_BIT >>> (pos & (INT_SIZE)))) != 0;
    }

    /**
     * Sets the value of a flag
     * @param pos Index of the flag to set
     * @param value Boolean value to set
     */
    public void setFlag(int pos, boolean value) {
        checkSize(pos);
        if (value) {
            data[pos >> SEGMENT_INDEX_SHIFT] |= LEFT_MOST_BIT >>> (pos & (INT_SIZE));
            bitsSet++;
        } else {
            data[pos >> SEGMENT_INDEX_SHIFT] &= ~LEFT_MOST_BIT >>> (pos & (INT_SIZE));
            bitsSet--;
        }
    }

    /**
     * Flips the value of the flag with the provided index
     * @param pos Index of the flag to flip
     */
    public void flipFlag(int pos) {
        checkSize(pos);
        int mask = LEFT_MOST_BIT >>> (pos & (INT_SIZE));
        if ((data[pos >> SEGMENT_INDEX_SHIFT] & mask) != 0) bitsSet--; else bitsSet++;
        data[pos >> SEGMENT_INDEX_SHIFT] ^= mask;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("BitIndex: { ");
        sb.append("\n\tdata: [");
        for (int fragment : data) {
            sb.append("\n\t\t ").append(String.format("%32s", Integer.toBinaryString(fragment)).replace(" ", "0"));
        }
        sb.append("\n\t]");
        sb.append("\n}");
        return sb.toString();
    }
}
