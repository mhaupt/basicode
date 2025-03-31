## upcoming

*   Support colours in text mode (GOSUB 110).
*   Add a splash screen (playable with the `-intro` command line parameter).
*   Adopt MIDI for better sound.
*   Bug fixes:
    *   GOSUB 450 should modify interpreter state (`SD`, `IN`, `IN$`).

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
