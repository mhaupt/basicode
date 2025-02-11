package de.haupz.basicode.ast;

import de.haupz.basicode.array.ArrayType;
import de.haupz.basicode.array.BasicArray;
import de.haupz.basicode.array.BasicArray1D;
import de.haupz.basicode.array.BasicArray2D;
import de.haupz.basicode.interpreter.InterpreterState;

/**
 * A helper for {@code DIM}, representing a single array creation. When executed, this node will evaluate the one or two
 * dimension expressions, create the array accordingly, and store it in the
 * {@linkplain InterpreterState#setArray(String, BasicArray) interpreter state}.
 */
public class DimCreateNode extends StatementNode {

    /**
     * The name of the array.
     */
    private final String id;

    /**
     * An expression yielding the size of the array's first dimension.
     */
    private final ExpressionNode dim1;

    /**
     * An expression yielding the size of the array's second dimension. This can be {@code null}, in which case the node
     * will generate a one-dimensional array.
     */
    private final ExpressionNode dim2;

    public DimCreateNode(String id, ExpressionNode dim1, ExpressionNode dim2) {
        this.id = id.toUpperCase();
        this.dim1 = dim1;
        this.dim2 = dim2;
    }

    public String getId() {
        return id;
    }

    public ExpressionNode getDim1() {
        return dim1;
    }

    public ExpressionNode getDim2() {
        return dim2;
    }

    @Override
    public void run(InterpreterState state) {
        ArrayType type = id.endsWith("$") ? ArrayType.STRING : ArrayType.NUMBER;
        Object dim1value = dim1.eval(state);
        if (dim1value instanceof Number dim1num) {
            int dim1int = dim1num.intValue();
            if (dim1int < 0) {
                throw new IllegalStateException("negative first dimension: " + dim1int);
            }
            if (dim2 == null) {
                state.setArray(id, new BasicArray1D(type, dim1int));
            } else {
                Object dim2value = dim2.eval(state);
                if (dim2value instanceof Number dim2num) {
                    int dim2int = dim2num.intValue();
                    if (dim2int < 0) {
                        throw new IllegalStateException("negative second dimension: " + dim2int);
                    }
                    state.setArray(id, new BasicArray2D(type, dim1int, dim2int));
                } else {
                    throw new IllegalStateException("second dimension must be a number: " + dim2value);
                }
            }
        } else {
            throw new IllegalStateException("first dimension must be a number: " + dim1value);
        }
    }

}
