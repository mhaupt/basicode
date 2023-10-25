package de.haupz.basicode;

import de.haupz.basicode.array.ArrayType;
import de.haupz.basicode.array.BasicArray;
import de.haupz.basicode.array.BasicArray1D;
import de.haupz.basicode.array.BasicArray2D;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class DimTest extends StatementTest {

    @Test
    public void test1DNonString() {
        run("DIM A(7)");
        Optional<Object> v = state.getVar("A");
        assertTrue(v.isPresent());
        assertEquals(BasicArray1D.class, v.get().getClass());
        BasicArray a = (BasicArray) v.get();
        assertEquals(ArrayType.NUMBER, a.getType());
        assertEquals(8, a.getDim1());
        assertThrows(IllegalStateException.class, () -> a.getDim2());
    }

    @Test
    public void test1DString() {
        run("DIM A$(7)");
        Optional<Object> v = state.getVar("A$");
        assertTrue(v.isPresent());
        assertEquals(BasicArray1D.class, v.get().getClass());
        BasicArray a = (BasicArray) v.get();
        assertEquals(ArrayType.STRING, a.getType());
        assertEquals(8, a.getDim1());
        assertThrows(IllegalStateException.class, () -> a.getDim2());
    }

    @Test
    public void test2DNonString() {
        run("DIM A(2,3)");
        Optional<Object> v = state.getVar("A");
        assertTrue(v.isPresent());
        assertEquals(BasicArray2D.class, v.get().getClass());
        BasicArray a = (BasicArray) v.get();
        assertEquals(ArrayType.NUMBER, a.getType());
        assertEquals(3, a.getDim1());
        assertEquals(4, a.getDim2());
    }

    @Test
    public void test2DString() {
        run("DIM A$(2,3)");
        Optional<Object> v = state.getVar("A$");
        assertTrue(v.isPresent());
        assertEquals(BasicArray2D.class, v.get().getClass());
        BasicArray a = (BasicArray) v.get();
        assertEquals(ArrayType.STRING, a.getType());
        assertEquals(3, a.getDim1());
        assertEquals(4, a.getDim2());
    }

    @Test
    public void test1DDoubleDim() {
        run("DIM A(7.5)");
        Optional<Object> v = state.getVar("A");
        assertTrue(v.isPresent());
        assertEquals(BasicArray1D.class, v.get().getClass());
        BasicArray a = (BasicArray) v.get();
        assertEquals(ArrayType.NUMBER, a.getType());
        assertEquals(8, a.getDim1());
        assertThrows(IllegalStateException.class, () -> a.getDim2());
    }

    @Test
    public void test2DDoubleDim() {
        run("DIM A(2.1,3.4)");
        Optional<Object> v = state.getVar("A");
        assertTrue(v.isPresent());
        assertEquals(BasicArray2D.class, v.get().getClass());
        BasicArray a = (BasicArray) v.get();
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
        run("DIM A(7)");
        run("AA=A(1)");
        Optional<Object> aa = state.getVar("AA");
        assertTrue(aa.isPresent());
        assertEquals(0, aa.get());
    }

    @Test
    public void testAccess1DString() {
        run("DIM A$(7)");
        run("AA$=A$(1)");
        Optional<Object> aa = state.getVar("AA$");
        assertTrue(aa.isPresent());
        assertEquals("", aa.get());
    }

    @Test
    public void testAccess2DNonString() {
        run("DIM A(2,3)");
        run("AA=A(1,1)");
        Optional<Object> aa = state.getVar("AA");
        assertTrue(aa.isPresent());
        assertEquals(0, aa.get());
    }

    @Test
    public void testAccess2DString() {
        run("DIM A$(2,3)");
        run("AA$=A$(1,1)");
        Optional<Object> aa = state.getVar("AA$");
        assertTrue(aa.isPresent());
        assertEquals("", aa.get());
    }

}
