1000 GOTO 20
1010 DIM CC(2):CC(0)=1:CC(1)=0
1020 GOSUB 600
1030 CC(0)=1:SR$="Hello,":HO=0:VE=0:GOSUB 650
1040 CC(0)=5:SR$="world.":HO=0.5:VE=0.5:GOSUB 650
1050 GOTO 950