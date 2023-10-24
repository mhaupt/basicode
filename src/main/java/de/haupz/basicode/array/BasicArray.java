package de.haupz.basicode.array;

public abstract class BasicArray {

    private final ArrayType type;

    private final Object[] data;

    protected BasicArray(ArrayType type, int storageSize) {
        this.type = type;
        data = new Object[storageSize];
    }

    public ArrayType getType() {
        return type;
    }

    public abstract int getDim1();

    public abstract int getDim2();

}
