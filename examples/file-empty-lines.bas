1000 GOTO 20
1010 NF$="examples/file.txt":NF=0:GOSUB 500
1020 PRINT IN
1022 FOR X=1 TO 6
1030 GOSUB 540
1035 K$=IN$:IF IN$="" THEN K$="empty"
1040 PRINT IN,"<"+K$+">"
1042 NEXT X
1050 GOSUB 580
1060 PRINT IN
1070 GOTO 950