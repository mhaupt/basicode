1000 GOTO 20
1010 GOTO 3000:HO=1:VE=4
1015 REM A=CC(1):CC(1)=CC(0):CC(0)=A
1020 RESTORE:B=-1
1030 B=B+1:IF B=9 THEN 1060
1040 READ SR$
1050 GOSUB 110:VE=VE+1:PRINT SR$:GOTO 1030
1060 REM HO=0:VE=4:GOSUB 110:SR$="                                  ":GOSUB 150
1070 HO=15:VE=13:SR$=" version 3C ":GOSUB  110:GOSUB 150
2000 GOSUB 210:GOTO 950
3000 A=CC(1):CC(1)=CC(0):CC(0)=A:GOSUB 600
3010 READ SR$:HO=.042:VE=.1:GOSUB 650
3020 FOR I=1 TO 7
3030 READ ST$:VE=VE+1/24:HO=HO-1.5/320:SR$=""
3032 FOR J=1 TO LEN(ST$)
3034 IF MID$(ST$,J,1)=" " THEN SR$=SR$+" ":GOTO 3038
3036 SR$=SR$+CHR$(35)
3038 NEXT J:GOSUB 650:NEXT I
3040 REM FOR I=33 TO 126:PRINT CHR$(I);:NEXT I
3050 HO=.042:VE=.5:FOR I=1 TO 7
3060 READ ST$:VE=VE+1/24:HO=HO-1.5/320:SR$=""
3070 FOR J=1 TO LEN(ST$)
3080 IF MID$(ST$,J,1)=" " THEN SR$=SR$+" ":GOTO 3100
3090 SR$=SR$+CHR$(35)
3100 NEXT J:GOSUB 650:NEXT I
3110 SR$="version 3C":HO=.7:VE=.45:GOSUB 650
3120 SR$="(p) 2025":HO=.39:VE=.88:GOSUB 650
3130 SR$="https://github.com/mhaupt/basicode"
3140 HO=.07:VE=.95:GOSUB 650
3150 SR$="#":VE=.79:HO=.723:GOSUB 650
3160 GOSUB 210
20000 DATA ""
20010 DATA "BBB             I   CC          d"
20020 DATA "B  B               C  C         d "
20030 DATA "B  B  aa   ss  ii  C     oo   ddd  ee "
20040 DATA "BBB     a s     i  C    o  o d  d e  e"
20050 DATA "B  B  aaa  ss   i  C    o  o d  d eeee"
20060 DATA "B  B a  a    s  i  C  C o  o d  d e"
20070 DATA "BBB   aa a ss  iii  CC   oo   dd d ee"
20080 DATA "  ff            JJJJ"
20090 DATA " f                 J"
20100 DATA "fff  oo  r rr      J  aa   v  v  aa "
20110 DATA " f  o  o  r        J    a  v  v    a"
20120 DATA " f  o  o  r        J  aaa  v  v  aaa"
20130 DATA " f  o  o  r     J  J a  a   vv  a  a"
20140 DATA " f   oo   r      JJ   aa a       aa a"
20180 DATA "                                  "