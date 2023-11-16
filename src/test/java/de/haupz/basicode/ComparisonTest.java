package de.haupz.basicode;

import org.junit.jupiter.api.Test;

public class ComparisonTest extends ExpressionTest {

    @Test
    public void testEqualsTrue() {
        testExpression("1=1", -1.0, Double.class);
        testExpression("1.1=1.1", -1.0, Double.class);
        testExpression("\"A\"=\"A\"", -1.0, Double.class);
    }

    @Test
    public void testEqualsFalse() {
        testExpression("1=2", 0.0, Double.class);
        testExpression("1.1=1.2", 0.0, Double.class);
        testExpression("\"A\"=\"B\"", 0.0, Double.class);
    }

    @Test
    public void testNotEqualsTrue() {
        testExpression("1<>2", -1.0, Double.class);
        testExpression("1.1<>1.2", -1.0, Double.class);
        testExpression("\"A\"<>\"B\"", -1.0, Double.class);
    }

    @Test
    public void testNotEqualsFalse() {
        testExpression("1<>1", 0.0, Double.class);
        testExpression("1.1<>1.1", 0.0, Double.class);
        testExpression("\"A\"<>\"A\"", 0.0, Double.class);
    }

    @Test
    public void testLtTrue() {
        testExpression("1<2", -1.0, Double.class);
        testExpression("1.1<1.2", -1.0, Double.class);
    }

    @Test
    public void testLtFalse() {
        testExpression("1<1", 0.0, Double.class);
        testExpression("1.1<1.1", 0.0, Double.class);
    }

    @Test
    public void testLtString() {
        testExpression("\"A\"<\"B\"", -1.0, Double.class);
        testExpression("\"A\"<\"AA\"", -1.0, Double.class);
        testExpression("\"AA\"<\"AB\"", -1.0, Double.class);
        testExpression("\"AA\"<\"BA\"", -1.0, Double.class);
        testExpression("\"B\"<\"A\"", 0.0, Double.class);
        testExpression("\"B\"<\"AA\"", 0.0, Double.class);
    }

    @Test
    public void testLeqTrue() {
        testExpression("1<=2", -1.0, Double.class);
        testExpression("1.1<=1.2", -1.0, Double.class);
        testExpression("1<=1", -1.0, Double.class);
        testExpression("1.1<=1.1", -1.0, Double.class);
    }

    @Test
    public void testLeqFalse() {
        testExpression("1<=0", 0.0, Double.class);
        testExpression("1.1<=1.0", 0.0, Double.class);
    }

    @Test
    public void testLeqString() {
        testExpression("\"A\"<=\"B\"", -1.0, Double.class);
        testExpression("\"A\"<=\"AA\"", -1.0, Double.class);
        testExpression("\"AA\"<=\"AB\"", -1.0, Double.class);
        testExpression("\"AA\"<=\"BA\"", -1.0, Double.class);
        testExpression("\"AA\"<=\"AA\"", -1.0, Double.class);
        testExpression("\"B\"<=\"A\"", 0.0, Double.class);
        testExpression("\"B\"<=\"AA\"", 0.0, Double.class);
    }

    @Test
    public void testGtTrue() {
        testExpression("1>0", -1.0, Double.class);
        testExpression("1.1>1.0", -1.0, Double.class);
    }

    @Test
    public void testGtFalse() {
        testExpression("1>1", 0.0, Double.class);
        testExpression("1.1>1.1", 0.0, Double.class);
    }

    @Test
    public void testGtString() {
        testExpression("\"B\">\"A\"", -1.0, Double.class);
        testExpression("\"AA\">\"A\"", -1.0, Double.class);
        testExpression("\"AB\">\"AA\"", -1.0, Double.class);
        testExpression("\"BA\">\"AA\"", -1.0, Double.class);
        testExpression("\"A\">\"B\"", 0.0, Double.class);
        testExpression("\"AA\">\"B\"", 0.0, Double.class);
    }

    @Test
    public void testGeqTrue() {
        testExpression("2>=1", -1.0, Double.class);
        testExpression("1.2>=1.1", -1.0, Double.class);
        testExpression("1>=1", -1.0, Double.class);
        testExpression("1.1>=1.1", -1.0, Double.class);
    }

    @Test
    public void testGeqFalse() {
        testExpression("0>=1", 0.0, Double.class);
        testExpression("1.0>=1.1", 0.0, Double.class);
    }

    @Test
    public void testGeqString() {
        testExpression("\"B\">=\"A\"", -1.0, Double.class);
        testExpression("\"AA\">=\"A\"", -1.0, Double.class);
        testExpression("\"AB\">=\"AA\"", -1.0, Double.class);
        testExpression("\"BA\">=\"AA\"", -1.0, Double.class);
        testExpression("\"AA\">=\"AA\"", -1.0, Double.class);
        testExpression("\"A\">=\"B\"", 0.0, Double.class);
        testExpression("\"AA\">=\"B\"", 0.0, Double.class);
    }

    @Test
    public void testCoercion() {
        testExpression("1=1.0", -1.0, Double.class);
        testExpression("1.0=1", -1.0, Double.class);
        testExpression("1=1.1", 0.0, Double.class);
        testExpression("1.1=1", 0.0, Double.class);
        testExpression("1<>1.1", -1.0, Double.class);
        testExpression("1.1<>1", -1.0, Double.class);
        testExpression("1<>1.0", 0.0, Double.class);
        testExpression("1.0<>1", 0.0, Double.class);
        testExpression("1<1.1", -1.0, Double.class);
        testExpression("1.1<2", -1.0, Double.class);
        testExpression("1<1.0", 0.0, Double.class);
        testExpression("1.0<1", 0.0, Double.class);
        testExpression("1<=1.1", -1.0, Double.class);
        testExpression("1<=1.0", -1.0, Double.class);
        testExpression("1.1<=2", -1.0, Double.class);
        testExpression("1.1<=1.1", -1.0, Double.class);
        testExpression("2<=1.0", 0.0, Double.class);
        testExpression("1.1<=1", 0.0, Double.class);
        testExpression("2>1.1", -1.0, Double.class);
        testExpression("1.1>1", -1.0, Double.class);
        testExpression("1>1.1", 0.0, Double.class);
        testExpression("1.0>2", 0.0, Double.class);
        testExpression("1.1>=1", -1.0, Double.class);
        testExpression("1>=1.0", -1.0, Double.class);
        testExpression("2.1>=2", -1.0, Double.class);
        testExpression("1.1>=1.1", -1.0, Double.class);
        testExpression("1>=2.0", 0.0, Double.class);
        testExpression("1.1>=2", 0.0, Double.class);
    }

    @Test
    public void testTypeMismatch() {
        testExpressionThrows("\"A\"=1", IllegalStateException.class);
        testExpressionThrows("1=\"A\"", IllegalStateException.class);
        testExpressionThrows("\"A\"=1.0", IllegalStateException.class);
        testExpressionThrows("1.0=\"A\"", IllegalStateException.class);
        testExpressionThrows("\"A\"<>1", IllegalStateException.class);
        testExpressionThrows("1<>\"A\"", IllegalStateException.class);
        testExpressionThrows("\"A\"<>1.0", IllegalStateException.class);
        testExpressionThrows("1.0<>\"A\"", IllegalStateException.class);
        testExpressionThrows("\"A\"<1", IllegalStateException.class);
        testExpressionThrows("1<\"A\"", IllegalStateException.class);
        testExpressionThrows("\"A\"<1.0", IllegalStateException.class);
        testExpressionThrows("1.0<\"A\"", IllegalStateException.class);
        testExpressionThrows("\"A\"<=1", IllegalStateException.class);
        testExpressionThrows("1<=\"A\"", IllegalStateException.class);
        testExpressionThrows("\"A\"<=1.0", IllegalStateException.class);
        testExpressionThrows("1.0<=\"A\"", IllegalStateException.class);
        testExpressionThrows("\"A\">1", IllegalStateException.class);
        testExpressionThrows("1>\"A\"", IllegalStateException.class);
        testExpressionThrows("\"A\">1.0", IllegalStateException.class);
        testExpressionThrows("1.0>\"A\"", IllegalStateException.class);
    }

}
