package de.haupz.basicode.array;

import java.util.Arrays;

public abstract class BasicArray {

    private final ArrayType type;

    protected final Object[] data;

    protected BasicArray(ArrayType type, int storageSize) {
        this.type = type;
        data = new Object[storageSize];
        switch (type) {
            case NUMBER -> Arrays.fill(data, Integer.valueOf(0));
            case STRING -> Arrays.fill(data, "");
        }
    }

    public ArrayType getType() {
        return type;
    }

    public abstract int getDim1();

    public abstract int getDim2();

    public boolean is1D() {
        return false;
    }

    public boolean is2D() {
        return false;
    }

    public abstract Object at(int a, int b);

}
