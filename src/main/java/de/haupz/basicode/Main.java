package de.haupz.basicode;

import de.haupz.basicode.ast.ProgramNode;
import de.haupz.basicode.interpreter.InterpreterState;
import de.haupz.basicode.parser.BasicParser;

import java.io.StringReader;

public class Main {

    public static void run(String code) throws Throwable {
        System.out.println("================================================================================");
        System.out.println(code);
        System.out.println("--------------------------------------------------------------------------------");
        final var parser = new BasicParser(new StringReader(code));
        ProgramNode prog = parser.program();
        prog.run(new InterpreterState(System.out));
        System.out.println("================================================================================");
    }

    public static void main(String[] args) throws Throwable {
        run("10 PRINT \"Hello, world!\"");
    }

}