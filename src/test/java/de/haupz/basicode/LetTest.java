package de.haupz.basicode;

import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class LetTest extends StatementTest {

    @Test
    public void setVarInt() {
        run("AA=10");
        Optional<Object> v = state.getVar("AA");
        assertTrue(v.isPresent());
        assertEquals(Integer.class, v.get().getClass());
        assertEquals(10, v.get());
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
        assertEquals(Integer.class, v.get().getClass());
        assertEquals(9, v.get());
    }

    @Test
    public void wrongAssignment() {
        assertThrows(IllegalStateException.class, () -> run("A = \"oops\""));
        assertThrows(IllegalStateException.class, () -> run("A$ = 23"));
    }

}
