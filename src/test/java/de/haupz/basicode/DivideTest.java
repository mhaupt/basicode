package de.haupz.basicode;

import org.junit.jupiter.api.Test;

public class DivideTest extends ExpressionTest {

    @Test
    public void test4div2() {
        testExpression("4 / 2", 2.0, Double.class);
    }

    @Test
    public void test4div2NoSpace() {
        testExpression("4/2", 2.0, Double.class);
    }

    @Test
    public void test4div3() {
        testExpression("4/3", 1.333333, Double.class);
    }

    @Test
    public void testCoercion() {
        testExpression("4/2.0", 2.0, Double.class);
        testExpression("4.0/2", 2.0, Double.class);
    }

    @Test
    public void testNegativeNumbers() {
        testExpression("-4/2", -2.0, Double.class);
        testExpression("4/-2", -2.0, Double.class);
        testExpression("-6.2/3.1", -2.0, Double.class);
        testExpression("6.2/-3.1", -2.0, Double.class);
    }

    @Test
    public void testNegativeCoercion() {
        testExpression("-4/2.0", -2.0, Double.class);
        testExpression("-4.0/2", -2.0, Double.class);
        testExpression("4/-2.0", -2.0, Double.class);
        testExpression("4.0/-2", -2.0, Double.class);
    }

    @Test
    public void testStringError() {
        testExpressionThrows("\"A\"/\"B\"", IllegalStateException.class);
    }

    @Test
    public void testTypeMismatches() {
        testExpressionThrows("3/\"A\"", IllegalStateException.class);
        testExpressionThrows("\"A\"/3", IllegalStateException.class);
        testExpressionThrows("3.2/\"A\"", IllegalStateException.class);
        testExpressionThrows("\"A\"/3.2", IllegalStateException.class);
    }

    @Test
    public void testDivisionByZero() {
        testExpressionThrows("3/0", ArithmeticException.class);
        testExpressionThrows("3.5/0", ArithmeticException.class);
        testExpressionThrows("3/0.0", ArithmeticException.class);
        testExpressionThrows("3.0/0", ArithmeticException.class);
        testExpressionThrows("-3/0", ArithmeticException.class);
        testExpressionThrows("-3.5/0", ArithmeticException.class);
    }

}
