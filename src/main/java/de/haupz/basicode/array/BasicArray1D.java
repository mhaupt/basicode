package de.haupz.basicode.array;

public class BasicArray1D extends BasicArray {

    private final int dim;

    public BasicArray1D(ArrayType type, int d) {
        super(type, d + 1);
        dim = d + 1;
    }

    @Override
    public int getDim1() {
        return dim;
    }

    @Override
    public int getDim2() {
        throw new IllegalStateException("1D array has no second dimension");
    }

    @Override
    public boolean is1D() {
        return true;
    }

    @Override
    public Object at(int a, int b) {
        // ignore b
        return data[a];
    }

}
