/* BasicParserTokenManager.java */
/* Generated By:JavaCC: Do not edit this line. BasicParserTokenManager.java */
package de.haupz.basicode.parser;
import java.util.ArrayList;
import java.util.List;
import de.haupz.basicode.ast.*;

/** Token Manager. */
@SuppressWarnings ("unused")
public class BasicParserTokenManager implements BasicParserConstants {

  /** Debug output. */
  public  java.io.PrintStream debugStream = System.out;
  /** Set debug output. */
  public  void setDebugStream(java.io.PrintStream ds) { debugStream = ds; }
private final int jjStopStringLiteralDfa_0(int pos, long active0, long active1){
   switch (pos)
   {
      case 0:
         if ((active0 & 0x1e800000000L) != 0L)
         {
            jjmatchedKind = 51;
            return 7;
         }
         if ((active0 & 0x7fe07fff3ff00L) != 0L)
         {
            jjmatchedKind = 51;
            return 20;
         }
         if ((active0 & 0xc0000L) != 0L)
         {
            jjmatchedKind = 51;
            return 12;
         }
         return -1;
      case 1:
         if ((active0 & 0x40000L) != 0L)
            return 13;
         if ((active0 & 0x2000300800000L) != 0L)
            return 11;
         if ((active0 & 0x5ff84ff7bff00L) != 0L)
         {
            jjmatchedKind = 51;
            jjmatchedPos = 1;
            return 11;
         }
         if ((active0 & 0x6800000000L) != 0L)
         {
            jjmatchedKind = 51;
            jjmatchedPos = 1;
            return 5;
         }
         return -1;
      case 2:
         if ((active0 & 0x130ec63305000L) != 0L)
         {
            if (jjmatchedPos < 1)
            {
               jjmatchedKind = 51;
               jjmatchedPos = 1;
            }
            return -1;
         }
         return -1;
      case 3:
         if ((active0 & 0x130ec63305000L) != 0L)
         {
            if (jjmatchedPos < 1)
            {
               jjmatchedKind = 51;
               jjmatchedPos = 1;
            }
            return -1;
         }
         return -1;
      case 4:
         if ((active0 & 0xe403100000L) != 0L)
         {
            if (jjmatchedPos < 1)
            {
               jjmatchedKind = 51;
               jjmatchedPos = 1;
            }
            return -1;
         }
         return -1;
      case 5:
         if ((active0 & 0xe000000000L) != 0L)
         {
            if (jjmatchedPos < 1)
            {
               jjmatchedKind = 51;
               jjmatchedPos = 1;
            }
            return -1;
         }
         return -1;
      default :
         return -1;
   }
}
private final int jjStartNfa_0(int pos, long active0, long active1){
   return jjMoveNfa_0(jjStopStringLiteralDfa_0(pos, active0, active1), pos + 1);
}
private int jjStopAtPos(int pos, int kind)
{
   jjmatchedKind = kind;
   jjmatchedPos = pos;
   return pos + 1;
}
private int jjMoveStringLiteralDfa0_0(){
   switch(curChar)
   {
      case 10:
         return jjStopAtPos(0, 2);
      case 40:
         return jjStopAtPos(0, 57);
      case 41:
         return jjStopAtPos(0, 58);
      case 42:
         return jjStopAtPos(0, 66);
      case 43:
         return jjStopAtPos(0, 65);
      case 44:
         return jjStopAtPos(0, 54);
      case 45:
         return jjStopAtPos(0, 55);
      case 47:
         return jjStopAtPos(0, 67);
      case 58:
         return jjStopAtPos(0, 53);
      case 59:
         return jjStopAtPos(0, 59);
      case 60:
         jjmatchedKind = 61;
         return jjMoveStringLiteralDfa1_0(0x5000000000000000L, 0x0L);
      case 61:
         return jjStopAtPos(0, 56);
      case 62:
         jjmatchedKind = 63;
         return jjMoveStringLiteralDfa1_0(0x0L, 0x1L);
      case 94:
         return jjStopAtPos(0, 68);
      case 65:
      case 97:
         return jjMoveStringLiteralDfa1_0(0xf00L, 0x0L);
      case 67:
      case 99:
         return jjMoveStringLiteralDfa1_0(0x3000L, 0x0L);
      case 68:
      case 100:
         return jjMoveStringLiteralDfa1_0(0x1c000L, 0x0L);
      case 69:
      case 101:
         return jjMoveStringLiteralDfa1_0(0x20000L, 0x0L);
      case 70:
      case 102:
         return jjMoveStringLiteralDfa1_0(0xc0000L, 0x0L);
      case 71:
      case 103:
         return jjMoveStringLiteralDfa1_0(0x300000L, 0x0L);
      case 73:
      case 105:
         return jjMoveStringLiteralDfa1_0(0x1c00000L, 0x0L);
      case 76:
      case 108:
         return jjMoveStringLiteralDfa1_0(0x1e000000L, 0x0L);
      case 77:
      case 109:
         return jjMoveStringLiteralDfa1_0(0x20000000L, 0x0L);
      case 78:
      case 110:
         return jjMoveStringLiteralDfa1_0(0xc0000000L, 0x0L);
      case 79:
      case 111:
         return jjMoveStringLiteralDfa1_0(0x300000000L, 0x0L);
      case 80:
      case 112:
         return jjMoveStringLiteralDfa1_0(0x400000000L, 0x0L);
      case 82:
      case 114:
         return jjMoveStringLiteralDfa1_0(0x1e800000000L, 0x0L);
      case 83:
      case 115:
         return jjMoveStringLiteralDfa1_0(0x3e0000000000L, 0x0L);
      case 84:
      case 116:
         return jjMoveStringLiteralDfa1_0(0x3c00000000000L, 0x0L);
      case 86:
      case 118:
         return jjMoveStringLiteralDfa1_0(0x4000000000000L, 0x0L);
      default :
         return jjMoveNfa_0(2, 0);
   }
}
private int jjMoveStringLiteralDfa1_0(long active0, long active1){
   try { curChar = input_stream.readChar(); }
   catch(java.io.IOException e) {
      jjStopStringLiteralDfa_0(0, active0, active1);
      return 1;
   }
   switch(curChar)
   {
      case 61:
         if ((active0 & 0x4000000000000000L) != 0L)
            return jjStopAtPos(1, 62);
         else if ((active1 & 0x1L) != 0L)
            return jjStopAtPos(1, 64);
         break;
      case 62:
         if ((active0 & 0x1000000000000000L) != 0L)
            return jjStopAtPos(1, 60);
         break;
      case 65:
      case 97:
         return jjMoveStringLiteralDfa2_0(active0, 0x4c00000004000L, active1, 0L);
      case 66:
      case 98:
         return jjMoveStringLiteralDfa2_0(active0, 0x100L, active1, 0L);
      case 69:
      case 101:
         return jjMoveStringLiteralDfa2_0(active0, 0x684e008000L, active1, 0L);
      case 70:
      case 102:
         if ((active0 & 0x800000L) != 0L)
            return jjStartNfaWithStates_0(1, 23, 11);
         break;
      case 71:
      case 103:
         return jjMoveStringLiteralDfa2_0(active0, 0x20000000000L, active1, 0L);
      case 72:
      case 104:
         return jjMoveStringLiteralDfa2_0(active0, 0x1000000001000L, active1, 0L);
      case 73:
      case 105:
         return jjMoveStringLiteralDfa2_0(active0, 0x48020010000L, active1, 0L);
      case 78:
      case 110:
         if ((active0 & 0x40000L) != 0L)
            return jjStartNfaWithStates_0(1, 18, 13);
         else if ((active0 & 0x100000000L) != 0L)
            return jjStartNfaWithStates_0(1, 32, 11);
         return jjMoveStringLiteralDfa2_0(active0, 0x1420200L, active1, 0L);
      case 79:
      case 111:
         if ((active0 & 0x2000000000000L) != 0L)
            return jjStartNfaWithStates_0(1, 49, 11);
         return jjMoveStringLiteralDfa2_0(active0, 0x90382000L, active1, 0L);
      case 81:
      case 113:
         return jjMoveStringLiteralDfa2_0(active0, 0x80000000000L, active1, 0L);
      case 82:
      case 114:
         if ((active0 & 0x200000000L) != 0L)
            return jjStartNfaWithStates_0(1, 33, 11);
         return jjMoveStringLiteralDfa2_0(active0, 0x400000000L, active1, 0L);
      case 83:
      case 115:
         return jjMoveStringLiteralDfa2_0(active0, 0x400L, active1, 0L);
      case 84:
      case 116:
         return jjMoveStringLiteralDfa2_0(active0, 0x300000000800L, active1, 0L);
      case 85:
      case 117:
         return jjMoveStringLiteralDfa2_0(active0, 0x10000000000L, active1, 0L);
      default :
         break;
   }
   return jjStartNfa_0(0, active0, active1);
}
private int jjMoveStringLiteralDfa2_0(long old0, long active0, long old1, long active1){
   if (((active0 &= old0) | (active1 &= old1)) == 0L)
      return jjStartNfa_0(0, old0, old1);
   try { curChar = input_stream.readChar(); }
   catch(java.io.IOException e) {
      jjStopStringLiteralDfa_0(1, active0, 0L);
      return 2;
   }
   switch(curChar)
   {
      case 65:
      case 97:
         return jjMoveStringLiteralDfa3_0(active0, 0x800000000L);
      case 66:
      case 98:
         if ((active0 & 0x400000000000L) != 0L)
            return jjStopAtPos(2, 46);
         break;
      case 67:
      case 99:
         if ((active0 & 0x400L) != 0L)
            return jjStopAtPos(2, 10);
         break;
      case 68:
      case 100:
         if ((active0 & 0x200L) != 0L)
            return jjStopAtPos(2, 9);
         else if ((active0 & 0x20000L) != 0L)
            return jjStopAtPos(2, 17);
         return jjMoveStringLiteralDfa3_0(active0, 0x20000000L);
      case 69:
      case 101:
         return jjMoveStringLiteralDfa3_0(active0, 0x1100000000000L);
      case 70:
      case 102:
         if ((active0 & 0x8000L) != 0L)
            return jjStopAtPos(2, 15);
         return jjMoveStringLiteralDfa3_0(active0, 0x2000000L);
      case 71:
      case 103:
         if ((active0 & 0x10000000L) != 0L)
            return jjStopAtPos(2, 28);
         return jjMoveStringLiteralDfa3_0(active0, 0x8000000000L);
      case 73:
      case 105:
         return jjMoveStringLiteralDfa3_0(active0, 0x400000000L);
      case 76:
      case 108:
         if ((active0 & 0x4000000000000L) != 0L)
            return jjStopAtPos(2, 50);
         break;
      case 77:
      case 109:
         if ((active0 & 0x10000L) != 0L)
            return jjStopAtPos(2, 16);
         break;
      case 78:
      case 110:
         if ((active0 & 0x800L) != 0L)
            return jjStopAtPos(2, 11);
         else if ((active0 & 0x4000000L) != 0L)
            return jjStopAtPos(2, 26);
         else if ((active0 & 0x10000000000L) != 0L)
            return jjStopAtPos(2, 40);
         else if ((active0 & 0x20000000000L) != 0L)
            return jjStopAtPos(2, 41);
         else if ((active0 & 0x40000000000L) != 0L)
            return jjStopAtPos(2, 42);
         else if ((active0 & 0x800000000000L) != 0L)
            return jjStopAtPos(2, 47);
         break;
      case 79:
      case 111:
         return jjMoveStringLiteralDfa3_0(active0, 0x200000000000L);
      case 80:
      case 112:
         return jjMoveStringLiteralDfa3_0(active0, 0x1000000L);
      case 82:
      case 114:
         if ((active0 & 0x80000L) != 0L)
            return jjStopAtPos(2, 19);
         else if ((active0 & 0x80000000000L) != 0L)
            return jjStopAtPos(2, 43);
         return jjMoveStringLiteralDfa3_0(active0, 0x1000L);
      case 83:
      case 115:
         if ((active0 & 0x100L) != 0L)
            return jjStopAtPos(2, 8);
         else if ((active0 & 0x2000L) != 0L)
            return jjStopAtPos(2, 13);
         return jjMoveStringLiteralDfa3_0(active0, 0x2000100000L);
      case 84:
      case 116:
         if ((active0 & 0x400000L) != 0L)
            return jjStopAtPos(2, 22);
         else if ((active0 & 0x8000000L) != 0L)
            return jjStopAtPos(2, 27);
         else if ((active0 & 0x80000000L) != 0L)
            return jjStopAtPos(2, 31);
         return jjMoveStringLiteralDfa3_0(active0, 0x4000204000L);
      case 88:
      case 120:
         return jjMoveStringLiteralDfa3_0(active0, 0x40000000L);
      default :
         break;
   }
   return jjStartNfa_0(1, active0, 0L);
}
private int jjMoveStringLiteralDfa3_0(long old0, long active0){
   if (((active0 &= old0)) == 0L)
      return jjStartNfa_0(1, old0, 0L);
   try { curChar = input_stream.readChar(); }
   catch(java.io.IOException e) {
      jjStopStringLiteralDfa_0(2, active0, 0L);
      return 3;
   }
   switch(curChar)
   {
      case 36:
         if ((active0 & 0x1000L) != 0L)
            return jjStopAtPos(3, 12);
         else if ((active0 & 0x20000000L) != 0L)
            return jjStopAtPos(3, 29);
         break;
      case 65:
      case 97:
         if ((active0 & 0x4000L) != 0L)
            return jjStopAtPos(3, 14);
         break;
      case 68:
      case 100:
         if ((active0 & 0x800000000L) != 0L)
            return jjStopAtPos(3, 35);
         break;
      case 72:
      case 104:
         return jjMoveStringLiteralDfa4_0(active0, 0x8000000000L);
      case 78:
      case 110:
         if ((active0 & 0x1000000000000L) != 0L)
            return jjStopAtPos(3, 48);
         return jjMoveStringLiteralDfa4_0(active0, 0x400000000L);
      case 79:
      case 111:
         if ((active0 & 0x200000L) != 0L)
            return jjStopAtPos(3, 21);
         break;
      case 80:
      case 112:
         if ((active0 & 0x100000000000L) != 0L)
            return jjStopAtPos(3, 44);
         else if ((active0 & 0x200000000000L) != 0L)
            return jjStopAtPos(3, 45);
         break;
      case 84:
      case 116:
         if ((active0 & 0x40000000L) != 0L)
            return jjStopAtPos(3, 30);
         return jjMoveStringLiteralDfa4_0(active0, 0x2002000000L);
      case 85:
      case 117:
         return jjMoveStringLiteralDfa4_0(active0, 0x4001100000L);
      default :
         break;
   }
   return jjStartNfa_0(2, active0, 0L);
}
private int jjMoveStringLiteralDfa4_0(long old0, long active0){
   if (((active0 &= old0)) == 0L)
      return jjStartNfa_0(2, old0, 0L);
   try { curChar = input_stream.readChar(); }
   catch(java.io.IOException e) {
      jjStopStringLiteralDfa_0(3, active0, 0L);
      return 4;
   }
   switch(curChar)
   {
      case 36:
         if ((active0 & 0x2000000L) != 0L)
            return jjStopAtPos(4, 25);
         break;
      case 66:
      case 98:
         if ((active0 & 0x100000L) != 0L)
            return jjStopAtPos(4, 20);
         break;
      case 79:
      case 111:
         return jjMoveStringLiteralDfa5_0(active0, 0x2000000000L);
      case 82:
      case 114:
         return jjMoveStringLiteralDfa5_0(active0, 0x4000000000L);
      case 84:
      case 116:
         if ((active0 & 0x1000000L) != 0L)
            return jjStopAtPos(4, 24);
         else if ((active0 & 0x400000000L) != 0L)
            return jjStopAtPos(4, 34);
         return jjMoveStringLiteralDfa5_0(active0, 0x8000000000L);
      default :
         break;
   }
   return jjStartNfa_0(3, active0, 0L);
}
private int jjMoveStringLiteralDfa5_0(long old0, long active0){
   if (((active0 &= old0)) == 0L)
      return jjStartNfa_0(3, old0, 0L);
   try { curChar = input_stream.readChar(); }
   catch(java.io.IOException e) {
      jjStopStringLiteralDfa_0(4, active0, 0L);
      return 5;
   }
   switch(curChar)
   {
      case 36:
         if ((active0 & 0x8000000000L) != 0L)
            return jjStopAtPos(5, 39);
         break;
      case 78:
      case 110:
         if ((active0 & 0x4000000000L) != 0L)
            return jjStopAtPos(5, 38);
         break;
      case 82:
      case 114:
         return jjMoveStringLiteralDfa6_0(active0, 0x2000000000L);
      default :
         break;
   }
   return jjStartNfa_0(4, active0, 0L);
}
private int jjMoveStringLiteralDfa6_0(long old0, long active0){
   if (((active0 &= old0)) == 0L)
      return jjStartNfa_0(4, old0, 0L);
   try { curChar = input_stream.readChar(); }
   catch(java.io.IOException e) {
      jjStopStringLiteralDfa_0(5, active0, 0L);
      return 6;
   }
   switch(curChar)
   {
      case 69:
      case 101:
         if ((active0 & 0x2000000000L) != 0L)
            return jjStopAtPos(6, 37);
         break;
      default :
         break;
   }
   return jjStartNfa_0(5, active0, 0L);
}
private int jjStartNfaWithStates_0(int pos, int kind, int state)
{
   jjmatchedKind = kind;
   jjmatchedPos = pos;
   try { curChar = input_stream.readChar(); }
   catch(java.io.IOException e) { return pos + 1; }
   return jjMoveNfa_0(state, pos + 1);
}
static final long[] jjbitVec0 = {
   0x0L, 0x0L, 0xffffffffffffffffL, 0xffffffffffffffffL
};
private int jjMoveNfa_0(int startState, int curPos)
{
   int startsAt = 0;
   jjnewStateCnt = 20;
   int i = 1;
   jjstateSet[0] = startState;
   int kind = 0x7fffffff;
   for (;;)
   {
      if (++jjround == 0x7fffffff)
         ReInitRounds();
      if (curChar < 64)
      {
         long l = 1L << curChar;
         do
         {
            switch(jjstateSet[--i])
            {
               case 5:
               case 11:
                  if (curChar == 36 && kind > 51)
                     kind = 51;
                  break;
               case 13:
                  if (curChar == 36 && kind > 51)
                     kind = 51;
                  break;
               case 20:
                  if ((0x3ff000000000000L & l) != 0L)
                  {
                     if (kind > 51)
                        kind = 51;
                     jjstateSet[jjnewStateCnt++] = 11;
                  }
                  else if (curChar == 36)
                  {
                     if (kind > 51)
                        kind = 51;
                  }
                  break;
               case 12:
                  if ((0x3ff000000000000L & l) != 0L)
                  {
                     if (kind > 51)
                        kind = 51;
                     jjstateSet[jjnewStateCnt++] = 11;
                  }
                  else if (curChar == 36)
                  {
                     if (kind > 51)
                        kind = 51;
                  }
                  break;
               case 2:
                  if ((0x3ff000000000000L & l) != 0L)
                  {
                     if (kind > 3)
                        kind = 3;
                     { jjCheckNAddStates(0, 3); }
                  }
                  else if (curChar == 34)
                     { jjCheckNAddTwoStates(3, 4); }
                  else if (curChar == 46)
                     { jjCheckNAdd(1); }
                  break;
               case 7:
                  if ((0x3ff000000000000L & l) != 0L)
                  {
                     if (kind > 51)
                        kind = 51;
                     jjstateSet[jjnewStateCnt++] = 11;
                  }
                  else if (curChar == 36)
                  {
                     if (kind > 51)
                        kind = 51;
                  }
                  break;
               case 0:
                  if (curChar == 46)
                     { jjCheckNAdd(1); }
                  break;
               case 1:
                  if ((0x3ff000000000000L & l) == 0L)
                     break;
                  if (kind > 5)
                     kind = 5;
                  { jjCheckNAdd(1); }
                  break;
               case 3:
                  if ((0xfffffffbfffffbffL & l) != 0L)
                     { jjCheckNAddTwoStates(3, 4); }
                  break;
               case 4:
                  if (curChar == 34 && kind > 7)
                     kind = 7;
                  break;
               case 6:
                  if ((0xfffffffffffffbffL & l) == 0L)
                     break;
                  if (kind > 36)
                     kind = 36;
                  jjstateSet[jjnewStateCnt++] = 6;
                  break;
               case 10:
                  if ((0x3ff000000000000L & l) == 0L)
                     break;
                  if (kind > 51)
                     kind = 51;
                  jjstateSet[jjnewStateCnt++] = 11;
                  break;
               case 14:
                  if ((0x3ff000000000000L & l) != 0L && kind > 52)
                     kind = 52;
                  break;
               case 16:
                  if ((0x3ff000000000000L & l) == 0L)
                     break;
                  if (kind > 3)
                     kind = 3;
                  { jjCheckNAddStates(0, 3); }
                  break;
               case 17:
                  if ((0x3ff000000000000L & l) == 0L)
                     break;
                  if (kind > 3)
                     kind = 3;
                  { jjCheckNAdd(17); }
                  break;
               case 18:
                  if ((0x3ff000000000000L & l) == 0L)
                     break;
                  if (kind > 4)
                     kind = 4;
                  { jjCheckNAdd(18); }
                  break;
               case 19:
                  if ((0x3ff000000000000L & l) != 0L)
                     { jjCheckNAddTwoStates(19, 0); }
                  break;
               default : break;
            }
         } while(i != startsAt);
      }
      else if (curChar < 128)
      {
         long l = 1L << (curChar & 077);
         do
         {
            switch(jjstateSet[--i])
            {
               case 5:
                  if ((0x200000002000L & l) == 0L)
                     break;
                  if (kind > 36)
                     kind = 36;
                  { jjCheckNAdd(6); }
                  break;
               case 13:
                  if ((0x7fffffe07fffffeL & l) == 0L)
                     break;
                  if (kind > 52)
                     kind = 52;
                  jjstateSet[jjnewStateCnt++] = 14;
                  break;
               case 20:
               case 10:
                  if ((0x7fffffe07fffffeL & l) == 0L)
                     break;
                  if (kind > 51)
                     kind = 51;
                  { jjCheckNAdd(11); }
                  break;
               case 12:
                  if ((0x7fffffe07fffffeL & l) != 0L)
                  {
                     if (kind > 51)
                        kind = 51;
                     { jjCheckNAdd(11); }
                  }
                  if ((0x400000004000L & l) != 0L)
                     jjstateSet[jjnewStateCnt++] = 13;
                  break;
               case 2:
                  if ((0x7fffffe07fffffeL & l) != 0L)
                  {
                     if (kind > 51)
                        kind = 51;
                     { jjCheckNAddTwoStates(10, 11); }
                  }
                  if ((0x4000000040L & l) != 0L)
                     jjstateSet[jjnewStateCnt++] = 12;
                  else if ((0x4000000040000L & l) != 0L)
                     jjstateSet[jjnewStateCnt++] = 7;
                  break;
               case 7:
                  if ((0x7fffffe07fffffeL & l) != 0L)
                  {
                     if (kind > 51)
                        kind = 51;
                     { jjCheckNAdd(11); }
                  }
                  if ((0x2000000020L & l) != 0L)
                     jjstateSet[jjnewStateCnt++] = 5;
                  break;
               case 3:
                  { jjAddStates(4, 5); }
                  break;
               case 6:
                  if (kind > 36)
                     kind = 36;
                  { jjCheckNAdd(6); }
                  break;
               case 8:
                  if ((0x4000000040000L & l) != 0L)
                     jjstateSet[jjnewStateCnt++] = 7;
                  break;
               case 9:
                  if ((0x7fffffe07fffffeL & l) == 0L)
                     break;
                  if (kind > 51)
                     kind = 51;
                  { jjCheckNAddTwoStates(10, 11); }
                  break;
               case 14:
                  if ((0x7fffffe07fffffeL & l) != 0L && kind > 52)
                     kind = 52;
                  break;
               case 15:
                  if ((0x4000000040L & l) != 0L)
                     jjstateSet[jjnewStateCnt++] = 12;
                  break;
               default : break;
            }
         } while(i != startsAt);
      }
      else
      {
         int i2 = (curChar & 0xff) >> 6;
         long l2 = 1L << (curChar & 077);
         do
         {
            switch(jjstateSet[--i])
            {
               case 3:
                  if ((jjbitVec0[i2] & l2) != 0L)
                     { jjAddStates(4, 5); }
                  break;
               case 6:
                  if ((jjbitVec0[i2] & l2) == 0L)
                     break;
                  if (kind > 36)
                     kind = 36;
                  jjstateSet[jjnewStateCnt++] = 6;
                  break;
               default : break;
            }
         } while(i != startsAt);
      }
      if (kind != 0x7fffffff)
      {
         jjmatchedKind = kind;
         jjmatchedPos = curPos;
         kind = 0x7fffffff;
      }
      ++curPos;
      if ((i = jjnewStateCnt) == (startsAt = 20 - (jjnewStateCnt = startsAt)))
         return curPos;
      try { curChar = input_stream.readChar(); }
      catch(java.io.IOException e) { return curPos; }
   }
}

/** Token literal values. */
public static final String[] jjstrLiteralImages = {
"", null, "\12", null, null, null, null, null, null, null, null, null, null, 
null, null, null, null, null, null, null, null, null, null, null, null, null, null, 
null, null, null, null, null, null, null, null, null, null, null, null, null, null, 
null, null, null, null, null, null, null, null, null, null, null, null, "\72", 
"\54", "\55", "\75", "\50", "\51", "\73", "\74\76", "\74", "\74\75", "\76", "\76\75", 
"\53", "\52", "\57", "\136", };
protected Token jjFillToken()
{
   final Token t;
   final String curTokenImage;
   final int beginLine;
   final int endLine;
   final int beginColumn;
   final int endColumn;
   String im = jjstrLiteralImages[jjmatchedKind];
   curTokenImage = (im == null) ? input_stream.GetImage() : im;
   beginLine = input_stream.getBeginLine();
   beginColumn = input_stream.getBeginColumn();
   endLine = input_stream.getEndLine();
   endColumn = input_stream.getEndColumn();
   t = Token.newToken(jjmatchedKind, curTokenImage);

   t.beginLine = beginLine;
   t.endLine = endLine;
   t.beginColumn = beginColumn;
   t.endColumn = endColumn;

   return t;
}
static final int[] jjnextStates = {
   17, 18, 19, 0, 3, 4, 
};

int curLexState = 0;
int defaultLexState = 0;
int jjnewStateCnt;
int jjround;
int jjmatchedPos;
int jjmatchedKind;

/** Get the next Token. */
public Token getNextToken() 
{
  Token matchedToken;
  int curPos = 0;

  EOFLoop :
  for (;;)
  {
   try
   {
      curChar = input_stream.BeginToken();
   }
   catch(Exception e)
   {
      jjmatchedKind = 0;
      jjmatchedPos = -1;
      matchedToken = jjFillToken();
      return matchedToken;
   }

   try { input_stream.backup(0);
      while (curChar <= 32 && (0x100000000L & (1L << curChar)) != 0L)
         curChar = input_stream.BeginToken();
   }
   catch (java.io.IOException e1) { continue EOFLoop; }
   jjmatchedKind = 0x7fffffff;
   jjmatchedPos = 0;
   curPos = jjMoveStringLiteralDfa0_0();
   if (jjmatchedKind != 0x7fffffff)
   {
      if (jjmatchedPos + 1 < curPos)
         input_stream.backup(curPos - jjmatchedPos - 1);
      if ((jjtoToken[jjmatchedKind >> 6] & (1L << (jjmatchedKind & 077))) != 0L)
      {
         matchedToken = jjFillToken();
         return matchedToken;
      }
      else
      {
         continue EOFLoop;
      }
   }
   int error_line = input_stream.getEndLine();
   int error_column = input_stream.getEndColumn();
   String error_after = null;
   boolean EOFSeen = false;
   try { input_stream.readChar(); input_stream.backup(1); }
   catch (java.io.IOException e1) {
      EOFSeen = true;
      error_after = curPos <= 1 ? "" : input_stream.GetImage();
      if (curChar == '\n' || curChar == '\r') {
         error_line++;
         error_column = 0;
      }
      else
         error_column++;
   }
   if (!EOFSeen) {
      input_stream.backup(1);
      error_after = curPos <= 1 ? "" : input_stream.GetImage();
   }
   throw new TokenMgrError(EOFSeen, curLexState, error_line, error_column, error_after, curChar, TokenMgrError.LEXICAL_ERROR);
  }
}

void SkipLexicalActions(Token matchedToken)
{
   switch(jjmatchedKind)
   {
      default :
         break;
   }
}
void MoreLexicalActions()
{
   jjimageLen += (lengthOfMatch = jjmatchedPos + 1);
   switch(jjmatchedKind)
   {
      default :
         break;
   }
}
void TokenLexicalActions(Token matchedToken)
{
   switch(jjmatchedKind)
   {
      default :
         break;
   }
}
private void jjCheckNAdd(int state)
{
   if (jjrounds[state] != jjround)
   {
      jjstateSet[jjnewStateCnt++] = state;
      jjrounds[state] = jjround;
   }
}
private void jjAddStates(int start, int end)
{
   do {
      jjstateSet[jjnewStateCnt++] = jjnextStates[start];
   } while (start++ != end);
}
private void jjCheckNAddTwoStates(int state1, int state2)
{
   jjCheckNAdd(state1);
   jjCheckNAdd(state2);
}

private void jjCheckNAddStates(int start, int end)
{
   do {
      jjCheckNAdd(jjnextStates[start]);
   } while (start++ != end);
}

    /** Constructor. */
    public BasicParserTokenManager(SimpleCharStream stream){

      if (SimpleCharStream.staticFlag)
            throw new Error("ERROR: Cannot use a static CharStream class with a non-static lexical analyzer.");

    input_stream = stream;
  }

  /** Constructor. */
  public BasicParserTokenManager (SimpleCharStream stream, int lexState){
    ReInit(stream);
    SwitchTo(lexState);
  }

  /** Reinitialise parser. */
  
  public void ReInit(SimpleCharStream stream)
  {


    jjmatchedPos =
    jjnewStateCnt =
    0;
    curLexState = defaultLexState;
    input_stream = stream;
    ReInitRounds();
  }

  private void ReInitRounds()
  {
    int i;
    jjround = 0x80000001;
    for (i = 20; i-- > 0;)
      jjrounds[i] = 0x80000000;
  }

  /** Reinitialise parser. */
  public void ReInit(SimpleCharStream stream, int lexState)
  
  {
    ReInit(stream);
    SwitchTo(lexState);
  }

  /** Switch to specified lex state. */
  public void SwitchTo(int lexState)
  {
    if (lexState >= 1 || lexState < 0)
      throw new TokenMgrError("Error: Ignoring invalid lexical state : " + lexState + ". State unchanged.", TokenMgrError.INVALID_LEXICAL_STATE);
    else
      curLexState = lexState;
  }


/** Lexer state names. */
public static final String[] lexStateNames = {
   "DEFAULT",
};

/** Lex State array. */
public static final int[] jjnewLexState = {
   -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 
   -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 
   -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 
};
static final long[] jjtoToken = {
   0xfffffffffffffffdL, 0x1fL, 
};
static final long[] jjtoSkip = {
   0x2L, 0x0L, 
};
static final long[] jjtoSpecial = {
   0x0L, 0x0L, 
};
static final long[] jjtoMore = {
   0x0L, 0x0L, 
};
    protected SimpleCharStream  input_stream;

    private final int[] jjrounds = new int[20];
    private final int[] jjstateSet = new int[2 * 20];
    private final StringBuilder jjimage = new StringBuilder();
    private StringBuilder image = jjimage;
    private int jjimageLen;
    private int lengthOfMatch;
    protected int curChar;
}
