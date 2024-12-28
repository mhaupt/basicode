package de.haupz.basicode.ast;

import de.haupz.basicode.array.ArrayType;
import de.haupz.basicode.array.BasicArray;
import de.haupz.basicode.interpreter.InterpreterState;

/**
 * <p>{@code LET}. This node implements assignments to variables and into arrays, for both numerical and string
 * types.</p>
 *
 * <p>The implementation performs an optional {@link LHS#checkPreInit(InterpreterState)} pre-initialisation of the
 * left-hand side, evaluates the right-hand side, and finally executes the actual assignment.</p>
 */
public class LetNode extends StatementNode {

    /**
     * {@code LHS} is the representation of the left-hand side of an assignment. This is necessary because assignments
     * to variables differ from assignments to array elements.
     */
    public static abstract class LHS {
        /**
         * The name of the left-hand side (the name of a variable or array).
         */
        protected String id;
        protected LHS(String id) {
            this.id = id.toUpperCase();
        }

        /**
         * Since the right-hand side expression of an assignment can contain the left-hand side, it needs to be
         * existent with a default value so that the right-hand side can be evaluated. This is what this method is
         * meant to ensure.
         *
         * @param state the interpreter state.
         */
        protected abstract void checkPreInit(InterpreterState state);

        /**
         * Execute the actual assignment of the value, depending on the nature of the left-hand side.
         *
         * @param state the interpreter state.
         * @param value the result of the evaluation of the right-hand side of the assignment, meant to be assigned to
         *              the variable or array element represented by the left-hand side.
         */
        protected abstract void assign(InterpreterState state, Object value);
    }

    /**
     * A representation of an assignment left-hand side that is a {@code Variable}, regardless of its type (number or
     * string). The implementation ensures the variable, if it does not yet exist, is
     * {@linkplain LHS#checkPreInit(InterpreterState) initialised with a default value}. It also performs a type check
     * when the {@linkplain LHS#assign(InterpreterState, Object) assignment is executed}.
     */
    public static class Variable extends LHS {
        public Variable(String id) {
            super(id);
        }
        @Override
        protected void checkPreInit(InterpreterState state) {
            boolean isString = id.endsWith("$");
            if (state.getVar(id).isEmpty()) {
                state.setVar(id, isString ? "" : Double.valueOf(0.0));
            }
        }
        @Override
        protected void assign(InterpreterState state, Object value) {
            boolean isString = id.endsWith("$");
            if (value instanceof String && !isString) {
                throw new IllegalStateException("can't assign a string to a variable named " + id);
            }
            if (!(value instanceof String) && isString) {
                throw new IllegalStateException("can't assign a non-string to a variable named " + id);
            }
            state.setVar(id, value);
        }
    }

    /**
     * A representation of an assignment left-hand side that is an {@code Array}, regardless of the type of its elements
     * (number or string). As arrays are pre-initialised when they are created, the implementation does not ensure
     * {@linkplain LHS#checkPreInit(InterpreterState) pre-initialisation}. It does, however, perform a type check when
     * the {@linkplain LHS#assign(InterpreterState, Object) assignment is executed}.
     */
    public static class Array extends LHS {
        private final VarNode getArray;
        private final ExpressionNode dim1;
        private final ExpressionNode dim2;
        public Array(String id, ExpressionNode dim1, ExpressionNode dim2) {
            super(id);
            this.getArray = new VarNode(id, true);
            this.dim1 = dim1;
            this.dim2 = dim2;
        }
        @Override
        protected void checkPreInit(InterpreterState state) {
            // ignore; arrays must have been pre-initialised
        }
        @Override
        protected void assign(InterpreterState state, Object value) {
            BasicArray array = (BasicArray) getArray.eval(state);
            if ((array.getType() == ArrayType.STRING && !(value instanceof String)) ||
                    (array.getType() == ArrayType.NUMBER && value instanceof String)) {
                throw new IllegalStateException("type mismatch in array assignment: assigning " +
                        value.getClass().getName() + " to a " + array.getType() + " array");
            }
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
                    array.setAt(dim1int, -1, value); // -1 will be ignored
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
                        array.setAt(dim1int, dim2int, value);
                    } else {
                        throw new IllegalStateException("second dimension must be a number: " + dim2value);
                    }
                }
            } else {
                throw new IllegalStateException("first dimension must be a number: " + dim1value);
            }
        }
    }

    /**
     * The left-hand side of this assignment.
     */
    private LHS lhs;

    /**
     * The right-hand side of this assignment.
     */
    private ExpressionNode expression;

    public LetNode(LHS lhs, ExpressionNode expression) {
        this.lhs = lhs;
        this.expression = expression;
    }

    @Override
    public void run(InterpreterState state) {
        lhs.checkPreInit(state);
        Object value = expression.eval(state);
        lhs.assign(state, value);
    }

}
