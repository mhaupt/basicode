package de.haupz.basicode.ast;

import de.haupz.basicode.interpreter.InterpreterState;

/**
 * {@code RETURN}. When run, this node will simply set the interpreter's {@linkplain InterpreterState#requestReturn()
 * return flag}.
 */
public class ReturnNode extends StatementNode {

    public ReturnNode() {}

    @Override
    public void run(InterpreterState state) {
        state.requestReturn();
    }

}
