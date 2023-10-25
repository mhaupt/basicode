package de.haupz.basicode.array;

public class BasicArray2D extends BasicArray {

    private final int dim1;

    private final int dim2;

    public BasicArray2D(ArrayType type, int d1, int d2) {
        super(type, (d1 + 1) * (d2 + 1));
        dim1 = d1 + 1;
        dim2 = d2 + 1;
    }

    @Override
    public int getDim1() {
        return dim1;
    }

    @Override
    public int getDim2() {
        return dim2;
    }

    @Override
    public boolean is2D() {
        return true;
    }

    @Override
    public Object at(int a, int b) {
        return data[a * dim1 + b];
    }

}
