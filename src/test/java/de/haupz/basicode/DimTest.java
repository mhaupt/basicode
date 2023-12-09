package de.haupz.basicode;

import de.haupz.basicode.array.ArrayType;
import de.haupz.basicode.array.BasicArray;
import de.haupz.basicode.array.BasicArray1D;
import de.haupz.basicode.array.BasicArray2D;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class DimTest extends StatementTest {

    @Test
    public void test1DNonString() {
        run("DIM A(7)");
        Optional<BasicArray> v = state.getArray("A");
        assertTrue(v.isPresent());
        assertEquals(BasicArray1D.class, v.get().getClass());
        BasicArray a = v.get();
        assertEquals(ArrayType.NUMBER, a.getType());
        assertEquals(8, a.getDim1());
        assertThrows(IllegalStateException.class, () -> a.getDim2());
    }

    @Test
    public void test1DString() {
        run("DIM A$(7)");
        Optional<BasicArray> v = state.getArray("A$");
        assertTrue(v.isPresent());
        assertEquals(BasicArray1D.class, v.get().getClass());
        BasicArray a = v.get();
        assertEquals(ArrayType.STRING, a.getType());
        assertEquals(8, a.getDim1());
        assertThrows(IllegalStateException.class, () -> a.getDim2());
    }

    @Test
    public void test2DNonString() {
        run("DIM A(2,3)");
        Optional<BasicArray> v = state.getArray("A");
        assertTrue(v.isPresent());
        assertEquals(BasicArray2D.class, v.get().getClass());
        BasicArray a = v.get();
        assertEquals(ArrayType.NUMBER, a.getType());
        assertEquals(3, a.getDim1());
        assertEquals(4, a.getDim2());
    }

    @Test
    public void test2DString() {
        run("DIM A$(2,3)");
        Optional<BasicArray> v = state.getArray("A$");
        assertTrue(v.isPresent());
        assertEquals(BasicArray2D.class, v.get().getClass());
        BasicArray a = v.get();
        assertEquals(ArrayType.STRING, a.getType());
        assertEquals(3, a.getDim1());
        assertEquals(4, a.getDim2());
    }

    @Test
    public void test1DDoubleDim() {
        run("DIM A(7.5)");
        Optional<BasicArray> v = state.getArray("A");
        assertTrue(v.isPresent());
        assertEquals(BasicArray1D.class, v.get().getClass());
        BasicArray a = v.get();
        assertEquals(ArrayType.NUMBER, a.getType());
        assertEquals(8, a.getDim1());
        assertThrows(IllegalStateException.class, () -> a.getDim2());
    }

    @Test
    public void test2DDoubleDim() {
        run("DIM A(2.1,3.4)");
        Optional<BasicArray> v = state.getArray("A");
        assertTrue(v.isPresent());
        assertEquals(BasicArray2D.class, v.get().getClass());
        BasicArray a = v.get();
        assertEquals(ArrayType.NUMBER, a.getType());
        assertEquals(3, a.getDim1());
        assertEquals(4, a.getDim2());
    }

    @Test
    public void testIllegalDim() {
        assertThrows(IllegalStateException.class, () -> run("DIM A(\"boop\")"));
        assertThrows(IllegalStateException.class, () -> run("DIM A(2,\"boop\")"));
        assertThrows(IllegalStateException.class, () -> run("DIM A(\"boop\",2)"));
        assertThrows(IllegalStateException.class, () -> run("DIM A(-1)"));
        assertThrows(IllegalStateException.class, () -> run("DIM A(-1,2)"));
        assertThrows(IllegalStateException.class, () -> run("DIM A(2,-1)"));
    }

    @Test
    public void testAccess1DNonString() {
        run(List.of("DIM A(7)", "AA=A(1)"));
        Optional<Object> aa = state.getVar("AA");
        assertTrue(aa.isPresent());
        assertEquals(0.0, aa.get());
    }

    @Test
    public void testAccess1DString() {
        run(List.of("DIM A$(7)", "AA$=A$(1)"));
        Optional<Object> aa = state.getVar("AA$");
        assertTrue(aa.isPresent());
        assertEquals("", aa.get());
    }

    @Test
    public void testAccess2DNonString() {
        run(List.of("DIM A(2,3)", "AA=A(1,1)"));
        Optional<Object> aa = state.getVar("AA");
        assertTrue(aa.isPresent());
        assertEquals(0.0, aa.get());
    }

    @Test
    public void testAccess2DString() {
        run(List.of("DIM A$(2,3)", "AA$=A$(1,1)"));
        Optional<Object> aa = state.getVar("AA$");
        assertTrue(aa.isPresent());
        assertEquals("", aa.get());
    }

    @Test
    public void testReadOutOfBounds1D() {
        assertThrows(IllegalStateException.class, () -> run(List.of("DIM A(7)", "AA=A(8)")));
    }

    @Test
    public void testReadOutOfBounds2D() {
        assertThrows(IllegalStateException.class, () -> run(List.of("DIM A(7,8)", "AA=A(4,9)")));
        assertThrows(IllegalStateException.class, () -> run(List.of("DIM A(7,8)", "AA=A(9,4)")));
        assertThrows(IllegalStateException.class, () -> run(List.of("DIM A(7,8)", "AA=A(9,9)")));
    }

    @Test
    public void testWriteNonString1D() {
        run(List.of("DIM A(7)", "A(1)=23", "AA=A(1)"));
        Optional<Object> aa = state.getVar("AA");
        assertTrue(aa.isPresent());
        assertEquals(23.0, aa.get());
    }

    @Test
    public void testWriteString1D() {
        run(List.of("DIM A$(7)", "A$(1)=\"Hello.\"", "AA$=A$(1)"));
        Optional<Object> aa = state.getVar("AA$");
        assertTrue(aa.isPresent());
        assertEquals("Hello.", aa.get());
    }

    @Test
    public void testWriteNonString2D() {
        run(List.of("DIM A(7,8)", "A(1,1)=23", "AA=A(1,1)"));
        Optional<Object> aa = state.getVar("AA");
        assertTrue(aa.isPresent());
        assertEquals(23.0, aa.get());
    }

    @Test
    public void testWriteString2D() {
        run(List.of("DIM A$(7,8)", "A$(1,1)=\"Hello.\"", "AA$=A$(1,1)"));
        Optional<Object> aa = state.getVar("AA$");
        assertTrue(aa.isPresent());
        assertEquals("Hello.", aa.get());
    }

    @Test
    public void testWriteOutOfBounds1D() {
        assertThrows(IllegalStateException.class, () -> run(List.of("DIM A(7)", "A(8)=23")));
    }

    @Test
    public void testWriteOutOfBounds2D() {
        assertThrows(IllegalStateException.class, () -> run(List.of("DIM A(7,8)", "A(4,9)=23")));
        assertThrows(IllegalStateException.class, () -> run(List.of("DIM A(7,8)", "A(9,4)=23")));
        assertThrows(IllegalStateException.class, () -> run(List.of("DIM A(7,8)", "A(9,9)=23")));
    }

    @Test
    public void testWriteWrongTypeNonString() {
        assertThrows(IllegalStateException.class, () -> run(List.of("DIM A(7)", "A(1)=\"boop\"")));
    }

    @Test
    public void testWriteWrongTypeString() {
        assertThrows(IllegalStateException.class, () -> run(List.of("DIM A$(7)", "A$(1)=23")));
    }

    @Test
    public void testSeparateNamespacesSameName() {
        run(List.of("A=1", "DIM A(1)"));
        assertTrue(state.getVar("A").isPresent());
        assertTrue(state.getArray("A").isPresent());
    }

    @Test
    public void testSeparateNamespacesDifferentNames() {
        run(List.of("A=1", "DIM B(1)"));
        assertTrue(state.getVar("A").isPresent());
        assertTrue(state.getArray("A").isEmpty());
        assertTrue(state.getArray("B").isPresent());
        assertTrue(state.getVar("B").isEmpty());
    }

}
