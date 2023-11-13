package de.haupz.basicode;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class LetTest extends StatementTest {

    @Test
    public void setVarInt() {
        run("AA=10");
        Optional<Object> v = state.getVar("AA");
        assertTrue(v.isPresent());
        assertEquals(Double.class, v.get().getClass());
        assertEquals(10.0, v.get());
    }

    @Test
    public void letVarInt() {
        run("LET AA=10");
        Optional<Object> v = state.getVar("AA");
        assertTrue(v.isPresent());
        assertEquals(Double.class, v.get().getClass());
        assertEquals(10.0, v.get());
    }

    @Test
    public void setVarDouble() {
        run("AA=10.5");
        Optional<Object> v = state.getVar("AA");
        assertTrue(v.isPresent());
        assertEquals(Double.class, v.get().getClass());
        assertEquals(10.5, v.get());
    }

    @Test
    public void setVarDoubleNoLeadingZero() {
        run("AA=.5");
        Optional<Object> v = state.getVar("AA");
        assertTrue(v.isPresent());
        assertEquals(Double.class, v.get().getClass());
        assertEquals(0.5, v.get());
    }

    @Test
    public void setVarString() {
        run("AA$=\"Hello.\"");
        Optional<Object> v = state.getVar("AA$");
        assertTrue(v.isPresent());
        assertEquals(String.class, v.get().getClass());
        assertEquals("Hello.", v.get());
    }

    @Test
    public void setVarExpression() {
        run("AA=2+3*4-5");
        Optional<Object> v = state.getVar("AA");
        assertTrue(v.isPresent());
        assertEquals(Double.class, v.get().getClass());
        assertEquals(9.0, v.get());
    }

    @Test
    public void wrongAssignment() {
        assertThrows(IllegalStateException.class, () -> run("A = \"oops\""));
        assertThrows(IllegalStateException.class, () -> run("A$ = 23"));
    }

    @Test
    public void multipleAssignments() {
        run(List.of("A=7", "B=8"));
        Optional<Object> a = state.getVar("A");
        Optional<Object> b = state.getVar("B");
        assertTrue(a.isPresent());
        assertTrue(b.isPresent());
        assertEquals(7.0, a.get());
        assertEquals(8.0, b.get());
    }

    @Test
    public void testSamePrefix() {
        run(List.of("A=7", "AA=8"));
        Optional<Object> a = state.getVar("A");
        Optional<Object> aa = state.getVar("AA");
        assertTrue(a.isPresent());
        assertTrue(aa.isPresent());
        assertEquals(7.0, a.get());
        assertEquals(8.0, aa.get());
    }

    @Test
    public void testLetInitialisesVariableIfNotPresentNumber() {
        run("A=A+2");
        Optional<Object> a = state.getVar("A");
        assertTrue(a.isPresent());
        assertEquals(2.0, a.get());
    }

    @Test
    public void testLetInitialisesVariableIfNotPresentString() {
        run("A$=A$+\"whoa\"");
        Optional<Object> a = state.getVar("A$");
        assertTrue(a.isPresent());
        assertEquals("whoa", a.get());
    }

}
