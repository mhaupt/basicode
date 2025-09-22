package de.haupz.basicode.array;

import java.util.Arrays;

/**
 * The {@code BasicArray} class provides common abstractions for BASIC arrays. These can have one or two dimensions, and
 * are represented by the respective subclass, {@link BasicArray1D}, and {@link BasicArray2D}.
 */
public abstract class BasicArray {

    /**
     * The type of data (numbers or strings) this array contains.
     */
    private final ArrayType type;

    /**
     * The actual data storage.
     */
    protected final Object[] data;

    /**
     * Create a BASIC array and fill it with default values (0 for numbers, or empty strings).
     *
     * @param type the type of data stored in this array.
     * @param storageSize the number of elements this array will contain.
     */
    protected BasicArray(ArrayType type, int storageSize) {
        this.type = type;
        data = new Object[storageSize];
        switch (type) {
            case NUMBER -> Arrays.fill(data, Double.valueOf(0.0));
            case STRING -> Arrays.fill(data, "");
        }
    }

    /**
     * @return the type of data this array stores.
     */
    public ArrayType getType() {
        return type;
    }

    /**
     * @return the number of elements on this array's first dimension.
     */
    public abstract int getDim1();

    /**
     * @return the number of elements on this array's second dimension. This will throw an exception if called on
     * one-dimensional arrays.
     */
    public abstract int getDim2();

    /**
     * @return {@code true} if this is a one-dimensional array.
     */
    public boolean is1D() {
        return false;
    }

    /**
     * @return {@code true} if this is a two-dimensional array.
     */
    public boolean is2D() {
        return false;
    }

    /**
     * Retrieve an element from this array.
     *
     * @param a the index into the first dimension of this array.
     * @param b the index into the second dimension of this array. This will be ignored by one-dimensional arrays.
     * @return the element at the specified position.
     */
    public abstract Object at(int a, int b);

    /**
     * Store an element in this array.
     *
     * @param a the index into the first dimension of this array.
     * @param b the index into the second dimension of this array. This will be ignored by one-dimensional arrays.
     * @param v the value to store.
     */
    public abstract void setAt(int a, int b, Object v);

    /**
     * @return the raw data storage of this array.
     */
    public Object[] getRawData() {
        return data;
    }

}
