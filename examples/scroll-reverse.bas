1000 GOTO 20
1010 SR$="reverse"
1011 FOR I=1 TO 30
1015 IF I/11=INT(I/11) THEN GOSUB 150
1020 PRINT I
1030 NEXT I
1040 SR$="Done.":GOSUB 150
1045 PRINT
1050 GOTO 950