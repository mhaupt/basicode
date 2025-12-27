## upcoming

*   Debugging support:
    *    Dump all or selected variables and array contents (GOSUB 960/961).
    *    Dump the call stack (GOSUB 962).
    *    Breakpoints (GOSUB 963).
    *    Breakpoints with selective display of variable values and array 
         contents (GOSUB 964).
    *    Watchpoints (GOSUB 965).
    *    Programmatic breakpoints (GOSUB 966).

## 1.0

*   Support colours in text mode (GOSUB 110).
*   Add a splash screen (playable with the `-intro` command line parameter).
*   Adopt MIDI for better sound.
*   Default prompt for INPUT.
*   Support for slowing down execution.
*   Show a cursor on INPUT.
*   Show more detailed stack traces and variable/array values when errors occur.
*   Display error information in a window instead of on the console only.
*   Open a file picker if a directory is given on the command line.
*   Support running subroutines with GOTO (implicit RETURN).
*   Bug fixes:
    *   GOSUB 450 should modify interpreter state (`SD`, `IN`, `IN$`).
    *   Support special characters (`STX`, `ETX`, `FS`) in 8-bit era files.
    *   Avoid deleting the entire line with backspace on INPUT.
    *   Treat variable names always as upper case internally.
    *   Address graphics quantisation gap.

## 0.12

*   Switched from generated to handwritten recursive descent parser.
*   Bug fixes: EXP/LOG, unary +, case sensitivity of variables, comma 
    separator in PRINT, VAL tolerates erroneous input and returns 0.

## 0.11

*   Passes BC3-Test, with one exception: "SR 270 berechnet nicht den
    Stringraum" ("subroutine 270 does not compute string space"): the test 
    expects memory to be less after allocating a long string. Since memory 
    management is entirely the JVM's job in this BASICODE implementation, we
    cannot honour the expectation of memory being utterly limited.

## 0.10

*   File picker pops up if no BASICODE source file is given on the command line.

## 0.9

First release. Haughtily high releaase number. Rather complete but "here be 
dragons".

*   Most well-behaved BASICODE programs should run.

*   Function keys likely don't work.

*   Not tested on any operating system other than macOS.
