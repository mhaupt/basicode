1000 GOTO 20
1010 FOR FG=0 TO 7
1020 FOR BG=0 TO 7
1030 HO=4*FG+1:VE=2*BG+1:CC(0)=FG:CC(1)=BG:GOSUB 110
1040 PRINT " "+CHR$(48+FG)+CHR$(48+BG)+" ";
1050 NEXT BG
1060 NEXT FG
1070 GOTO 950