package de.haupz.basicode.ast;

import de.haupz.basicode.array.BasicArray;
import de.haupz.basicode.interpreter.InterpreterState;

public class DimAccessNode extends ExpressionNode {

    private final VarNode getArray;

    private final ExpressionNode dim1;

    private final ExpressionNode dim2;

    public DimAccessNode(String id, ExpressionNode dim1, ExpressionNode dim2) {
        this.getArray = new VarNode(id, true);
        this.dim1 = dim1;
        this.dim2 = dim2;
    }

    @Override
    public Object eval(InterpreterState state) {
        BasicArray array = (BasicArray) getArray.eval(state);
        Object dim1value = dim1.eval(state);
        if (dim1value instanceof Number dim1num) {
            int dim1int = dim1num.intValue();
            if (dim1int < 0) {
                throw new IllegalStateException("negative first dimension: " + dim1int);
            }
            if (dim2 == null) {
                if (array.is2D()) {
                    throw new IllegalStateException("1D access to 2D array");
                }
                return array.at(dim1int, -1); // 1D array will ignore second dimension
            } else {
                Object dim2value = dim2.eval(state);
                if (dim2value instanceof Number dim2num) {
                    int dim2int = dim2num.intValue();
                    if (dim2int < 0) {
                        throw new IllegalStateException("negative second dimension: " + dim2int);
                    }
                    if (array.is1D()) {
                        throw new IllegalStateException("2D access to 1D array");
                    }
                    return array.at(dim1int, dim2int);
                } else {
                    throw new IllegalStateException("second dimension must be a number: " + dim2value);
                }
            }
        } else {
            throw new IllegalStateException("first dimension must be a number: " + dim1value);
        }
    }

}
