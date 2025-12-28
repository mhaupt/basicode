package de.haupz.basicode;

import org.junit.jupiter.api.Test;

public class BreakpointTest extends InterpreterTest {

    @Test
    public void testBreakpoint() {
        testInterpreter("""
                1000 GOTO 20
                1010 GOSUB 963
                1020 GOTO 950
                """, """
                at line 1010, statement 0
                1010 GOSUB 963
                -----^
                == variables ==
                CN = 0.0
                HG = 320.0
                HO = 39.0
                SV = 15.0
                VE = 24.0
                VG = 200.0
                == arrays ==
                CC = (3)
                 (0) 6.0
                 (1) 1.0
                 (2) 0.0\n
                """);
    }

    @Test
    public void testBreakpointGosub() {
        testInterpreter("""
                1000 GOTO 20
                1010 GOSUB 1100
                1020 GOTO 950
                1100 GOSUB 1200:RETURN
                1200 GOSUB 963:RETURN
                """,
                """
                at line 1200, statement 0
                1200 GOSUB 963:RETURN
                -----^
                at line 1100, statement 0
                1100 GOSUB 1200:RETURN
                -----^
                at line 1010, statement 0
                1010 GOSUB 1100
                -----^
                == variables ==
                CN = 0.0
                HG = 320.0
                HO = 39.0
                SV = 15.0
                VE = 24.0
                VG = 200.0
                == arrays ==
                CC = (3)
                 (0) 6.0
                 (1) 1.0
                 (2) 0.0\n
                """);
    }

    @Test
    public void testBreakpointSelectiveDisplay() {
        testInterpreter("""
                1000 GOTO 20
                1010 DIM OD$(2):OD$(1)="X":OD$(2)="Y$"
                1020 X=23:Y$="Hello.":GOSUB 964
                1030 GOTO 950
                """, """
                at line 1020, statement 2
                1020 X=23:Y$="Hello.":GOSUB 964
                ----------------------^
                == variables ==
                X = 23.0
                Y$ = Hello.\n
                """);
    }

    @Test
    public void testNormalBreakpointIsNotSelective() {
        testInterpreter("""
                1000 GOTO 20
                1010 DIM OD$(2):OD$(1)="X":OD$(2)="Y$"
                1020 X=23:Y$="Hello.":GOSUB 963
                1030 GOTO 950
                """, """
                at line 1020, statement 2
                1020 X=23:Y$="Hello.":GOSUB 963
                ----------------------^
                == variables ==
                CN = 0.0
                HG = 320.0
                HO = 39.0
                SV = 15.0
                VE = 24.0
                VG = 200.0
                X = 23.0
                Y$ = Hello.
                == arrays ==
                CC = (3)
                 (0) 6.0
                 (1) 1.0
                 (2) 0.0
                OD$ = (3)
                 (0)\s
                 (1) X
                 (2) Y$\n
                """);
    }

    @Test
    public void testDisplayVariableAndArray() {
        testInterpreter("""
                1000 GOTO 20
                1010 DIM OD$(2):OD$(1)="A":OD$(2)="A()"
                1020 A=23:DIM A(1):A(1)=42
                1030 GOSUB 964
                1040 GOTO 950
                """, """
                at line 1030, statement 0
                1030 GOSUB 964
                -----^
                == variables ==
                A = 23.0
                == arrays ==
                A = (2)
                 (0) 0.0
                 (1) 42.0\n
                """);
    }

    @Test
    public void testDisplayJustVariable() {
        testInterpreter("""
                1000 GOTO 20
                1010 DIM OD$(1):OD$(1)="A"
                1020 A=23:DIM A(1):A(1)=42
                1030 GOSUB 964
                1040 GOTO 950
                """, """
                at line 1030, statement 0
                1030 GOSUB 964
                -----^
                == variables ==
                A = 23.0\n
                """);
    }

    @Test
    public void testDisplayJustArray() {
        testInterpreter("""
                1000 GOTO 20
                1010 DIM OD$(1):OD$(1)="A()"
                1020 A=23:DIM A(1):A(1)=42
                1030 GOSUB 964
                1040 GOTO 950
                """, """
                at line 1030, statement 0
                1030 GOSUB 964
                -----^
                == arrays ==
                A = (2)
                 (0) 0.0
                 (1) 42.0\n
                """);
    }

    @Test
    public void testDisplayIfNothingGiven() {
        testInterpreter("""
                1000 GOTO 20
                1010 A=23:DIM A(1):A(1)=42
                1020 GOSUB 964
                1030 GOTO 950
                """, """
                at line 1020, statement 0
                1020 GOSUB 964
                -----^
                == variables ==
                A = 23.0
                CN = 0.0
                HG = 320.0
                HO = 39.0
                SV = 15.0
                VE = 24.0
                VG = 200.0
                == arrays ==
                A = (2)
                 (0) 0.0
                 (1) 42.0
                CC = (3)
                 (0) 6.0
                 (1) 1.0
                 (2) 0.0\n
                """);
    }

    @Test
    public void testConditionalBreakpoint() {
        testInterpreter("""
                1000 GOTO 20
                1010 X=1:GOSUB 963
                1020 OC$="X=2":GOSUB 963
                1030 X=2:GOSUB 963
                1040 GOTO 950
                """, """
                at line 1010, statement 1
                1010 X=1:GOSUB 963
                ---------^
                == variables ==
                CN = 0.0
                HG = 320.0
                HO = 39.0
                SV = 15.0
                VE = 24.0
                VG = 200.0
                X = 1.0
                == arrays ==
                CC = (3)
                 (0) 6.0
                 (1) 1.0
                 (2) 0.0
                
                at line 1030, statement 1
                1030 X=2:GOSUB 963
                ---------^
                == variables ==
                CN = 0.0
                HG = 320.0
                HO = 39.0
                OC$ = X=2
                SV = 15.0
                VE = 24.0
                VG = 200.0
                X = 2.0
                == arrays ==
                CC = (3)
                 (0) 6.0
                 (1) 1.0
                 (2) 0.0\n
                """);
    }

    @Test
    public void testConditionalSelectiveBreakpoint() {
        testInterpreter("""
                1000 GOTO 20
                1010 DIM OD$(1):OD$(1)="X"
                1020 X=1:GOSUB 964
                1030 OC$="X=2":GOSUB 964
                1040 X=2:GOSUB 964
                1050 GOTO 950
                """, """
                at line 1020, statement 1
                1020 X=1:GOSUB 964
                ---------^
                == variables ==
                X = 1.0
                
                at line 1040, statement 1
                1040 X=2:GOSUB 964
                ---------^
                == variables ==
                X = 2.0\n
                """);
    }

    @Test
    public void testRegisterBreakpoint() {
        testInterpreter("""
                1000 GOTO 20
                1010 OL=1030:OS=1:GOSUB 966
                1020 PRINT "breakpoint: ";OP
                1030 PRINT "before":PRINT "here":PRINT "after"
                1040 GOTO 950
                """, """
                breakpoint:  1\s
                before
                at line 1030, statement 1
                1030 PRINT "before":PRINT "here":PRINT "after"
                --------------------^
                == variables ==
                CN = 0.0
                HG = 320.0
                HO = 39.0
                OL = 1030.0
                OP = 1.0
                OS = 1.0
                SV = 15.0
                VE = 24.0
                VG = 200.0
                == arrays ==
                CC = (3)
                 (0) 6.0
                 (1) 1.0
                 (2) 0.0
                 
                here
                after
                """);
    }

    @Test
    public void testBreakpointActivation() {
        testInterpreter("""
                1000 GOTO 20
                1010 K=0:OL=1030:OS=3:GOSUB 966
                1020 PRINT "breakpoint: ";OP
                1030 K=K+1:PRINT K:PRINT "before":PRINT "here":PRINT "after"
                1040 ON K GOTO 1050,1060,1070
                1050 GOSUB 968:GOTO 1030
                1060 GOSUB 967:GOTO 1030
                1070 GOTO 950
                """, """
                breakpoint:  1\s
                 1\s
                before
                at line 1030, statement 3
                1030 K=K+1:PRINT K:PRINT "before":PRINT "here":PRINT "after"
                ----------------------------------^
                == variables ==
                CN = 0.0
                HG = 320.0
                HO = 39.0
                K = 1.0
                OL = 1030.0
                OP = 1.0
                OS = 3.0
                SV = 15.0
                VE = 24.0
                VG = 200.0
                == arrays ==
                CC = (3)
                 (0) 6.0
                 (1) 1.0
                 (2) 0.0
                 
                here
                after
                 2\s
                before
                here
                after
                 3\s
                before
                at line 1030, statement 3
                1030 K=K+1:PRINT K:PRINT "before":PRINT "here":PRINT "after"
                ----------------------------------^
                == variables ==
                CN = 0.0
                HG = 320.0
                HO = 39.0
                K = 3.0
                OL = 1030.0
                OP = 1.0
                OS = 3.0
                SV = 15.0
                VE = 24.0
                VG = 200.0
                == arrays ==
                CC = (3)
                 (0) 6.0
                 (1) 1.0
                 (2) 0.0
                 
                here
                after
                """);
    }

    @Test
    public void testBreakpointActivationSelectiveDisplay() {
        testInterpreter("""
                1000 GOTO 20
                1010 K=0:DIM OD$(1):OD$(1)="K":OL=1030:OS=3:GOSUB 966
                1020 PRINT "breakpoint: ";OP
                1030 K=K+1:PRINT K:PRINT "before":PRINT "here":PRINT "after"
                1040 ON K GOTO 1050,1060,1070
                1050 GOSUB 968:GOTO 1030
                1060 GOSUB 967:GOTO 1030
                1070 GOTO 950
                """, """
                breakpoint:  1\s
                 1\s
                before
                at line 1030, statement 3
                1030 K=K+1:PRINT K:PRINT "before":PRINT "here":PRINT "after"
                ----------------------------------^
                == variables ==
                K = 1.0
                 
                here
                after
                 2\s
                before
                here
                after
                 3\s
                before
                at line 1030, statement 3
                1030 K=K+1:PRINT K:PRINT "before":PRINT "here":PRINT "after"
                ----------------------------------^
                == variables ==
                K = 3.0
                 
                here
                after
                """);
    }

    @Test
    public void testBreakpointActivationSelectiveDisplayAndCondition() {
        testInterpreter("""
                1000 GOTO 20
                1010 K=0:OC$="K=3":DIM OD$(1):OD$(1)="K":OL=1030:OS=3:GOSUB 966
                1020 PRINT "breakpoint: ";OP
                1030 K=K+1:PRINT K:PRINT "before":PRINT "here":PRINT "after"
                1040 ON K GOTO 1050,1060,1070
                1050 GOSUB 968:GOTO 1030
                1060 GOSUB 967:GOTO 1030
                1070 GOTO 950
                """, """
                breakpoint:  1\s
                 1\s
                before
                here
                after
                 2\s
                before
                here
                after
                 3\s
                before
                at line 1030, statement 3
                1030 K=K+1:PRINT K:PRINT "before":PRINT "here":PRINT "after"
                ----------------------------------^
                == variables ==
                K = 3.0
                 
                here
                after
                """);
    }

    @Test
    public void testBreakpointActivationNoOP() {
        testInterpreter("""
                1000 GOTO 20
                1010 GOSUB 967
                1020 PRINT OP;OE$
                1030 GOTO 950
                """, """
                -1 OP is undefined
                """);
    }

    @Test
    public void testBreakpointActivationIllegalOP() {
        testInterpreter("""
                1000 GOTO 20
                1010 OP=3:GOSUB 967
                1020 PRINT OP;OE$
                1030 GOTO 950
                """, """
                -1 breakpoint 3 does not exist
                """);
    }

    @Test
    public void testSetBreakpointNoDetails() {
        testInterpreter("""
                1000 GOTO 20
                1010 GOSUB 966
                1020 PRINT OP;OE$
                1030 GOTO 950
                """, """
                -1 both OL and OS must be defined
                """);
    }

    @Test
    public void testSetBreakpointMissingDetails() {
        testInterpreter("""
                1000 GOTO 20
                1010 OL=1000:GOSUB 966
                1020 PRINT OP;OE$
                1030 GOTO 950
                """, """
                -1 both OL and OS must be defined
                """);
        testInterpreter("""
                1000 GOTO 20
                1010 OS=1000:GOSUB 966
                1020 PRINT OP;OE$
                1030 GOTO 950
                """, """
                -1 both OL and OS must be defined
                """);
    }

    @Test
    public void testSetBreakpointWrongLine() {
        testInterpreter("""
                1000 GOTO 20
                1010 OL=2000:OS=0:GOSUB 966
                1020 PRINT OP;OE$
                1030 GOTO 950
                """, """
                -1 line number 2000 does not exist
                """);
    }

    @Test
    public void testSetBreakpointWrongStatement() {
        testInterpreter("""
                1000 GOTO 20
                1010 OL=1000:OS=1:GOSUB 966
                1020 PRINT OP;OE$
                1030 GOTO 950
                """, """
                -1 statement number 1 does not exist on line 1000
                """);
    }

}
