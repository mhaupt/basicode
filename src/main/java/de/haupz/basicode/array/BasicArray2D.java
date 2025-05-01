package de.haupz.basicode.array;

/**
 * The implementation of two-dimensional BASIC arrays.
 */
public class BasicArray2D extends BasicArray {

    /**
     * The amount of elements on the first dimension of this array. Note that it will be one greater than the value
     * given for initialisation, as a BASIC array with a size of {@code N} can have indices in the range 0 <= {@code i}
     * <= {@code N}.
     */
    private final int dim1;

    /**
     * The amount of elements on the second dimension of this array. Note that it will be one greater than the value
     * given for initialisation, as a BASIC array with a size of {@code N} can have indices in the range 0 <= {@code i}
     * <= {@code N}.
     */
    private final int dim2;

    /**
     * Construct a two-dimensional BASIC array.
     *
     * @param type the array's type.
     * @param d1 the {@linkplain BasicArray2D#dim1 first dimension} of the array.
     * @param d2 the {@linkplain BasicArray2D#dim2 second dimension} of the array.
     */
    public BasicArray2D(ArrayType type, int d1, int d2) {
        super(type, (d1 + 1) * (d2 + 1));
        dim1 = d1 + 1;
        dim2 = d2 + 1;
    }

    /**
     * @return the {@linkplain BasicArray2D#dim1 first dimension} of the array.
     */
    @Override
    public int getDim1() {
        return dim1;
    }

    /**
     * @return the {@linkplain BasicArray2D#dim2 second dimension} of the array.
     */
    @Override
    public int getDim2() {
        return dim2;
    }

    /**
     * @return {@code true}.
     */
    @Override
    public boolean is2D() {
        return true;
    }

    /**
     * Retrieve an element from this array.
     *
     * @param a the index into the first dimension of this array.
     * @param b the index into the second dimension of this array.
     * @return the element at the position indicated by {@code a} and {@code b} in this array.
     */
    @Override
    public Object at(int a, int b) {
        checkBoundaries(a, b);
        return data[a * dim2 + b];
    }

    /**
     * Store a value in this array.
     *
     * @param a the index into the first dimension of this array.
     * @param b the index into the second dimension of this array.
     * @param v the value to store.
     */
    @Override
    public void setAt(int a, int b, Object v) {
        checkBoundaries(a, b);
        data[a * dim2 + b] = v;
    }

    /**
     * Helper: check if the passed indices are within the acceptable boundaries of this array, and throw an exception if
     * that is not the case.
     *
     * @param a the index into the first dimension of this array.
     * @param b the index into the second dimension of this array.
     */
    private void checkBoundaries(int a, int b) {
        if (a >= dim1 || b >= dim2) {
            throw new IllegalStateException(
                    String.format("out of bounds access (%d,%d) in 2D array [%d,%d]", a, b, dim1, dim2));
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append('(').append(dim1).append(',').append(dim2).append(')');
        for (int d1 = 0; d1 < dim1; d1++) {
            for (int d2 = 0; d2 < dim2; d2++) {
                sb.append("\n (").append(d1).append(',').append(d2).append(") ").append(data[d1 * dim2 + d2]);
            }
        }
        return sb.toString();
    }

}
