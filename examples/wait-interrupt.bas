1000 GOTO 20
1010 PRINT "Waiting for 5 seconds."
1011 PRINT "Or press any key to interrupt."
1020 SD=50:GOSUB 450
1030 PRINT "The wait is over."
1040 PRINT "Remaining: ";SD
1050 PRINT "Interrupted by: ";IN,"<";IN$;">"
1060 GOTO 950