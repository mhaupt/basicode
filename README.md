# BASICODE

This is an implementation of a BASIC interpreter providing a [BASICODE](https://en.wikipedia.org/wiki/BASICODE)
runtime environment independent of any emulator (unless you count the Java VM as
one). The point of this project is to _have_ such a thing, rather than 
providing a highly optimised BASICODE run-time environment. Frankly, the JVM,
off the shelf, will do a good enough job at delivering run-time performance. 
This is also why the BASICODE implementation does not sport any translation 
to Java bytecodes. The whole thing should be _simple_, first and foremost.

## Building and Running

The project was built using Java 21 and Maven. Once those are installed, just 
running `mvn package` on the command line should be enough to build 
everything and run the tests.

The `mvn package` run will generate an executable JAR file in 
`targets/basicode.jar`. This file includes all relevant dependencies, so it 
can be used as a standalone "executable" for BASICODE as long as you still 
have Java 21 installed.

To run a BASIC file from the command line at the project root, run this (the 
`-hold` argument will make the BASICODE window stay after the program has 
run, until you press a key):

```
$ java -jar target/basicode.jar -hold examples/hello.bas 
```

This works for other BASICODE programs, as long as you point to the right 
`basicode.jar` and BASIC source file.

## Licence Information

This project is under the [MIT Licence](https://mit-license.org/), 
non-contagious, well-intentioned, and harmless.

The font used in the emulated console is "Amstrad CPC464 Regular" by Wesley 
Clarke. It's included here thanks to being licenced with [CC BY-SA 3.0](https://creativecommons.org/licenses/by-sa/3.0/).

## Credits

I'm immensely grateful to Thomas Rademacher of [basicode.de](https://basicode.de/)
for introducing me to BASICODE. Many thanks to Bernd Bock and other members of
[Joyce e.V.](https://joyce.de/) for encouragement and bug reports.
