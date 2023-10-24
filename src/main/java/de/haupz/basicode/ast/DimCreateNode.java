package de.haupz.basicode.ast;

import de.haupz.basicode.array.ArrayType;
import de.haupz.basicode.array.BasicArray1D;
import de.haupz.basicode.array.BasicArray2D;
import de.haupz.basicode.interpreter.InterpreterState;

public class DimCreateNode extends StatementNode {

    private final String id;

    private final ExpressionNode dim1;

    private final ExpressionNode dim2;

    public DimCreateNode(String id, ExpressionNode dim1, ExpressionNode dim2) {
        this.id = id;
        this.dim1 = dim1;
        this.dim2 = dim2;
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
                state.setVar(id, new BasicArray1D(type, dim1int));
            } else {
                Object dim2value = dim2.eval(state);
                if (dim2value instanceof Number dim2num) {
                    int dim2int = dim2num.intValue();
                    if (dim2int < 0) {
                        throw new IllegalStateException("negative second dimension: " + dim2int);
                    }
                    state.setVar(id, new BasicArray2D(type, dim1int, dim2int));
                } else {
                    throw new IllegalStateException("second dimension must be a number: " + dim2value);
                }
            }
        } else {
            throw new IllegalStateException("first dimension must be a number: " + dim1value);
        }
    }

}
