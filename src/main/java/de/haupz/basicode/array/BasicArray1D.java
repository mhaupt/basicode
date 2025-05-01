package de.haupz.basicode.array;

/**
 * The implementation of one-dimensional BASIC arrays.
 */
public class BasicArray1D extends BasicArray {

    /**
     * The size of this array. Note that it will be one greater than the value given for initialisation, as a BASIC
     * array with a size of {@code N} can have indices in the range 0 <= {@code i} <= {@code N}.
     */
    private final int dim;

    /**
     * Construct a one-dimensional BASIC array.
     *
     * @param type the array's type.
     * @param d the {@linkplain BasicArray1D#dim dimension} of the array.
     */
    public BasicArray1D(ArrayType type, int d) {
        super(type, d + 1);
        dim = d + 1;
    }

    /**
     * @return the {@linkplain BasicArray1D#dim dimension} of this array.
     */
    @Override
    public int getDim1() {
        return dim;
    }

    /**
     * This will throw an exception for one-dimensional arrays.
     */
    @Override
    public int getDim2() {
        throw new IllegalStateException("1D array has no second dimension");
    }

    /**
     * @return {@code true}.
     */
    @Override
    public boolean is1D() {
        return true;
    }

    /**
     * Retrieve an element from this array.
     *
     * @param a the index of the element to retrieve.
     * @param b this will be ignored.
     * @return the element at the position indicated by {@code a} in this array.
     */
    @Override
    public Object at(int a, int b) {
        // ignore b
        checkBoundary(a);
        return data[a];
    }

    /**
     * Store a value in this array.
     *
     * @param a the index at which to store the element.
     * @param b this will be ignored.
     * @param v the value to store.
     */
    @Override
    public void setAt(int a, int b, Object v) {
        // ignore b
        checkBoundary(a);
        data[a] = v;
    }

    /**
     * Helper: check if the passed index is within the acceptable boundaries of this array, and throw an exception if
     * that is not the case.
     *
     * @param a the index to check.
     */
    private void checkBoundary(int a) {
        if (a >= dim) {
            throw new IllegalStateException(String.format("out of bounds access (%d) in 1D array [%d]", a, dim));
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append('(').append(dim).append(')');
        for (int i = 0; i < dim; i++) {
            sb.append("\n (").append(i).append(") ").append(data[i]);
        }
        return sb.toString();
    }
}
