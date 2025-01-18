package de.haupz.basicode;

import de.haupz.basicode.array.BasicArray1D;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class InputTest extends StatementTest {

    @Test
    public void testInteger() {
        run("INPUT A", "23");
        Optional<Object> v = state.getVar("A");
        assertTrue(v.isPresent());
        assertEquals(Double.class, v.get().getClass());
        assertEquals(23.0, v.get());
    }

    @Test
    public void testDouble() {
        run("INPUT A", "23.42");
        Optional<Object> v = state.getVar("A");
        assertTrue(v.isPresent());
        assertEquals(Double.class, v.get().getClass());
        assertEquals(23.42, v.get());
    }

    @Test
    public void testString() {
        run("INPUT A$", "Hello.");
        Optional<Object> v = state.getVar("A$");
        assertTrue(v.isPresent());
        assertEquals(String.class, v.get().getClass());
        assertEquals("Hello.", v.get());
    }

    @Test
    public void testNumberAsString() {
        run(List.of("INPUT A$", "INPUT B$"), "23\n42.23");
        Optional<Object> a = state.getVar("A$");
        Optional<Object> b = state.getVar("B$");
        assertTrue(a.isPresent());
        assertTrue(b.isPresent());
        assertEquals(String.class, a.get().getClass());
        assertEquals(String.class, b.get().getClass());
        assertEquals("23", a.get());
        assertEquals("42.23", b.get());
    }

    @Test
    public void testWrongType() {
        assertThrows(IllegalStateException.class, () -> run("INPUT A", "oops"));
    }

}
