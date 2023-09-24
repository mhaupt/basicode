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
private final int jjStopStringLiteralDfa_0(int pos, long active0){
   switch (pos)
   {
      case 0:
         if ((active0 & 0x7a00000000L) != 0L)
         {
            jjmatchedKind = 7;
            return 6;
         }
         if ((active0 & 0x1ff81ffffff00L) != 0L)
         {
            jjmatchedKind = 7;
            return -1;
         }
         return -1;
      case 1:
         if ((active0 & 0x1a00000000L) != 0L)
         {
            if (jjmatchedPos == 0)
            {
               jjmatchedKind = 7;
               jjmatchedPos = 0;
            }
            return 4;
         }
         if ((active0 & 0x1ffe1ffffff00L) != 0L)
         {
            if (jjmatchedPos == 0)
            {
               jjmatchedKind = 7;
               jjmatchedPos = 0;
            }
            return -1;
         }
         return -1;
      case 2:
         if ((active0 & 0x17ffb3fdfff00L) != 0L)
         {
            if (jjmatchedPos == 0)
            {
               jjmatchedKind = 7;
               jjmatchedPos = 0;
            }
            return -1;
         }
         return -1;
      case 3:
         if ((active0 & 0x4c3b18cc5000L) != 0L)
         {
            if (jjmatchedPos == 0)
            {
               jjmatchedKind = 7;
               jjmatchedPos = 0;
            }
            return -1;
         }
         return -1;
      case 4:
         if ((active0 & 0x3900c40000L) != 0L)
         {
            if (jjmatchedPos == 0)
            {
               jjmatchedKind = 7;
               jjmatchedPos = 0;
            }
            return -1;
         }
         return -1;
      case 5:
         if ((active0 & 0x3800000000L) != 0L)
         {
            if (jjmatchedPos == 0)
            {
               jjmatchedKind = 7;
               jjmatchedPos = 0;
            }
            return -1;
         }
         return -1;
      default :
         return -1;
   }
}
private final int jjStartNfa_0(int pos, long active0){
   return jjMoveNfa_0(jjStopStringLiteralDfa_0(pos, active0), pos + 1);
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
      case 42:
         return jjStopAtPos(0, 51);
      case 43:
         return jjStopAtPos(0, 49);
      case 45:
         return jjStopAtPos(0, 50);
      case 47:
         return jjStopAtPos(0, 52);
      case 58:
         return jjStopAtPos(0, 3);
      case 60:
         jjmatchedKind = 55;
         return jjMoveStringLiteralDfa1_0(0x600000000000000L);
      case 61:
         return jjStopAtPos(0, 54);
      case 62:
         jjmatchedKind = 56;
         return jjMoveStringLiteralDfa1_0(0x800000000000000L);
      case 94:
         return jjStopAtPos(0, 53);
      case 65:
      case 97:
         return jjMoveStringLiteralDfa1_0(0xf00L);
      case 67:
      case 99:
         return jjMoveStringLiteralDfa1_0(0x3000L);
      case 68:
      case 100:
         return jjMoveStringLiteralDfa1_0(0xc000L);
      case 69:
      case 101:
         return jjMoveStringLiteralDfa1_0(0x10000L);
      case 70:
      case 102:
         return jjMoveStringLiteralDfa1_0(0x20000L);
      case 71:
      case 103:
         return jjMoveStringLiteralDfa1_0(0xc0000L);
      case 73:
      case 105:
         return jjMoveStringLiteralDfa1_0(0x700000L);
      case 76:
      case 108:
         return jjMoveStringLiteralDfa1_0(0x7800000L);
      case 77:
      case 109:
         return jjMoveStringLiteralDfa1_0(0x8000000L);
      case 78:
      case 110:
         return jjMoveStringLiteralDfa1_0(0x30000000L);
      case 79:
      case 111:
         return jjMoveStringLiteralDfa1_0(0xc0000000L);
      case 80:
      case 112:
         return jjMoveStringLiteralDfa1_0(0x100000000L);
      case 82:
      case 114:
         return jjMoveStringLiteralDfa1_0(0x7a00000000L);
      case 83:
      case 115:
         return jjMoveStringLiteralDfa1_0(0xf8000000000L);
      case 84:
      case 116:
         return jjMoveStringLiteralDfa1_0(0xf00000000000L);
      case 86:
      case 118:
         return jjMoveStringLiteralDfa1_0(0x1000000000000L);
      default :
         return jjMoveNfa_0(0, 0);
   }
}
private int jjMoveStringLiteralDfa1_0(long active0){
   try { curChar = input_stream.readChar(); }
   catch(java.io.IOException e) {
      jjStopStringLiteralDfa_0(0, active0);
      return 1;
   }
   switch(curChar)
   {
      case 61:
         if ((active0 & 0x400000000000000L) != 0L)
            return jjStopAtPos(1, 58);
         else if ((active0 & 0x800000000000000L) != 0L)
            return jjStopAtPos(1, 59);
         break;
      case 62:
         if ((active0 & 0x200000000000000L) != 0L)
            return jjStopAtPos(1, 57);
         break;
      case 65:
      case 97:
         return jjMoveStringLiteralDfa2_0(active0, 0x1300000004000L);
      case 66:
      case 98:
         return jjMoveStringLiteralDfa2_0(active0, 0x100L);
      case 69:
      case 101:
         return jjMoveStringLiteralDfa2_0(active0, 0x1a13800000L);
      case 70:
      case 102:
         if ((active0 & 0x200000L) != 0L)
            return jjStopAtPos(1, 21);
         break;
      case 71:
      case 103:
         return jjMoveStringLiteralDfa2_0(active0, 0x8000000000L);
      case 72:
      case 104:
         return jjMoveStringLiteralDfa2_0(active0, 0x400000001000L);
      case 73:
      case 105:
         return jjMoveStringLiteralDfa2_0(active0, 0x12008008000L);
      case 78:
      case 110:
         if ((active0 & 0x40000000L) != 0L)
            return jjStopAtPos(1, 30);
         return jjMoveStringLiteralDfa2_0(active0, 0x510200L);
      case 79:
      case 111:
         if ((active0 & 0x800000000000L) != 0L)
            return jjStopAtPos(1, 47);
         return jjMoveStringLiteralDfa2_0(active0, 0x240e2000L);
      case 81:
      case 113:
         return jjMoveStringLiteralDfa2_0(active0, 0x20000000000L);
      case 82:
      case 114:
         if ((active0 & 0x80000000L) != 0L)
            return jjStopAtPos(1, 31);
         return jjMoveStringLiteralDfa2_0(active0, 0x100000000L);
      case 83:
      case 115:
         return jjMoveStringLiteralDfa2_0(active0, 0x400L);
      case 84:
      case 116:
         return jjMoveStringLiteralDfa2_0(active0, 0xc0000000800L);
      case 85:
      case 117:
         return jjMoveStringLiteralDfa2_0(active0, 0x4000000000L);
      default :
         break;
   }
   return jjStartNfa_0(0, active0);
}
private int jjMoveStringLiteralDfa2_0(long old0, long active0){
   if (((active0 &= old0)) == 0L)
      return jjStartNfa_0(0, old0);
   try { curChar = input_stream.readChar(); }
   catch(java.io.IOException e) {
      jjStopStringLiteralDfa_0(1, active0);
      return 2;
   }
   switch(curChar)
   {
      case 65:
      case 97:
         return jjMoveStringLiteralDfa3_0(active0, 0x200000000L);
      case 66:
      case 98:
         if ((active0 & 0x100000000000L) != 0L)
            return jjStopAtPos(2, 44);
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
         else if ((active0 & 0x10000L) != 0L)
            return jjStopAtPos(2, 16);
         return jjMoveStringLiteralDfa3_0(active0, 0x8000000L);
      case 69:
      case 101:
         return jjMoveStringLiteralDfa3_0(active0, 0x440000000000L);
      case 70:
      case 102:
         return jjMoveStringLiteralDfa3_0(active0, 0x800000L);
      case 71:
      case 103:
         if ((active0 & 0x4000000L) != 0L)
            return jjStopAtPos(2, 26);
         return jjMoveStringLiteralDfa3_0(active0, 0x2000000000L);
      case 73:
      case 105:
         return jjMoveStringLiteralDfa3_0(active0, 0x100000000L);
      case 76:
      case 108:
         if ((active0 & 0x1000000000000L) != 0L)
            return jjStopAtPos(2, 48);
         break;
      case 77:
      case 109:
         if ((active0 & 0x8000L) != 0L)
            return jjStopAtPos(2, 15);
         break;
      case 78:
      case 110:
         if ((active0 & 0x800L) != 0L)
            return jjStopAtPos(2, 11);
         else if ((active0 & 0x1000000L) != 0L)
            return jjStopAtPos(2, 24);
         else if ((active0 & 0x4000000000L) != 0L)
            return jjStopAtPos(2, 38);
         else if ((active0 & 0x8000000000L) != 0L)
            return jjStopAtPos(2, 39);
         else if ((active0 & 0x10000000000L) != 0L)
            return jjStopAtPos(2, 40);
         else if ((active0 & 0x200000000000L) != 0L)
            return jjStopAtPos(2, 45);
         break;
      case 79:
      case 111:
         return jjMoveStringLiteralDfa3_0(active0, 0x80000000000L);
      case 80:
      case 112:
         return jjMoveStringLiteralDfa3_0(active0, 0x400000L);
      case 82:
      case 114:
         if ((active0 & 0x20000L) != 0L)
            return jjStopAtPos(2, 17);
         else if ((active0 & 0x20000000000L) != 0L)
            return jjStopAtPos(2, 41);
         return jjMoveStringLiteralDfa3_0(active0, 0x1000L);
      case 83:
      case 115:
         if ((active0 & 0x100L) != 0L)
            return jjStopAtPos(2, 8);
         else if ((active0 & 0x2000L) != 0L)
            return jjStopAtPos(2, 13);
         return jjMoveStringLiteralDfa3_0(active0, 0x800040000L);
      case 84:
      case 116:
         if ((active0 & 0x100000L) != 0L)
            return jjStopAtPos(2, 20);
         else if ((active0 & 0x2000000L) != 0L)
            return jjStopAtPos(2, 25);
         else if ((active0 & 0x20000000L) != 0L)
            return jjStopAtPos(2, 29);
         return jjMoveStringLiteralDfa3_0(active0, 0x1000084000L);
      case 88:
      case 120:
         return jjMoveStringLiteralDfa3_0(active0, 0x10000000L);
      default :
         break;
   }
   return jjStartNfa_0(1, active0);
}
private int jjMoveStringLiteralDfa3_0(long old0, long active0){
   if (((active0 &= old0)) == 0L)
      return jjStartNfa_0(1, old0);
   try { curChar = input_stream.readChar(); }
   catch(java.io.IOException e) {
      jjStopStringLiteralDfa_0(2, active0);
      return 3;
   }
   switch(curChar)
   {
      case 36:
         if ((active0 & 0x1000L) != 0L)
            return jjStopAtPos(3, 12);
         else if ((active0 & 0x8000000L) != 0L)
            return jjStopAtPos(3, 27);
         break;
      case 65:
      case 97:
         if ((active0 & 0x4000L) != 0L)
            return jjStopAtPos(3, 14);
         break;
      case 68:
      case 100:
         if ((active0 & 0x200000000L) != 0L)
            return jjStopAtPos(3, 33);
         break;
      case 72:
      case 104:
         return jjMoveStringLiteralDfa4_0(active0, 0x2000000000L);
      case 78:
      case 110:
         if ((active0 & 0x400000000000L) != 0L)
            return jjStopAtPos(3, 46);
         return jjMoveStringLiteralDfa4_0(active0, 0x100000000L);
      case 79:
      case 111:
         if ((active0 & 0x80000L) != 0L)
            return jjStopAtPos(3, 19);
         break;
      case 80:
      case 112:
         if ((active0 & 0x40000000000L) != 0L)
            return jjStopAtPos(3, 42);
         else if ((active0 & 0x80000000000L) != 0L)
            return jjStopAtPos(3, 43);
         break;
      case 84:
      case 116:
         if ((active0 & 0x10000000L) != 0L)
            return jjStopAtPos(3, 28);
         return jjMoveStringLiteralDfa4_0(active0, 0x800800000L);
      case 85:
      case 117:
         return jjMoveStringLiteralDfa4_0(active0, 0x1000440000L);
      default :
         break;
   }
   return jjStartNfa_0(2, active0);
}
private int jjMoveStringLiteralDfa4_0(long old0, long active0){
   if (((active0 &= old0)) == 0L)
      return jjStartNfa_0(2, old0);
   try { curChar = input_stream.readChar(); }
   catch(java.io.IOException e) {
      jjStopStringLiteralDfa_0(3, active0);
      return 4;
   }
   switch(curChar)
   {
      case 36:
         if ((active0 & 0x800000L) != 0L)
            return jjStopAtPos(4, 23);
         break;
      case 66:
      case 98:
         if ((active0 & 0x40000L) != 0L)
            return jjStopAtPos(4, 18);
         break;
      case 79:
      case 111:
         return jjMoveStringLiteralDfa5_0(active0, 0x800000000L);
      case 82:
      case 114:
         return jjMoveStringLiteralDfa5_0(active0, 0x1000000000L);
      case 84:
      case 116:
         if ((active0 & 0x400000L) != 0L)
            return jjStopAtPos(4, 22);
         else if ((active0 & 0x100000000L) != 0L)
            return jjStopAtPos(4, 32);
         return jjMoveStringLiteralDfa5_0(active0, 0x2000000000L);
      default :
         break;
   }
   return jjStartNfa_0(3, active0);
}
private int jjMoveStringLiteralDfa5_0(long old0, long active0){
   if (((active0 &= old0)) == 0L)
      return jjStartNfa_0(3, old0);
   try { curChar = input_stream.readChar(); }
   catch(java.io.IOException e) {
      jjStopStringLiteralDfa_0(4, active0);
      return 5;
   }
   switch(curChar)
   {
      case 36:
         if ((active0 & 0x2000000000L) != 0L)
            return jjStopAtPos(5, 37);
         break;
      case 78:
      case 110:
         if ((active0 & 0x1000000000L) != 0L)
            return jjStopAtPos(5, 36);
         break;
      case 82:
      case 114:
         return jjMoveStringLiteralDfa6_0(active0, 0x800000000L);
      default :
         break;
   }
   return jjStartNfa_0(4, active0);
}
private int jjMoveStringLiteralDfa6_0(long old0, long active0){
   if (((active0 &= old0)) == 0L)
      return jjStartNfa_0(4, old0);
   try { curChar = input_stream.readChar(); }
   catch(java.io.IOException e) {
      jjStopStringLiteralDfa_0(5, active0);
      return 6;
   }
   switch(curChar)
   {
      case 69:
      case 101:
         if ((active0 & 0x800000000L) != 0L)
            return jjStopAtPos(6, 35);
         break;
      default :
         break;
   }
   return jjStartNfa_0(5, active0);
}
static final long[] jjbitVec0 = {
   0x0L, 0x0L, 0xffffffffffffffffL, 0xffffffffffffffffL
};
private int jjMoveNfa_0(int startState, int curPos)
{
   int startsAt = 0;
   jjnewStateCnt = 9;
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
               case 0:
                  if ((0x3ff000000000000L & l) != 0L)
                  {
                     if (kind > 4)
                        kind = 4;
                     { jjCheckNAdd(8); }
                  }
                  else if (curChar == 34)
                     { jjCheckNAddTwoStates(1, 2); }
                  break;
               case 1:
                  if ((0xfffffffbfffffbffL & l) != 0L)
                     { jjCheckNAddTwoStates(1, 2); }
                  break;
               case 2:
                  if (curChar == 34 && kind > 6)
                     kind = 6;
                  break;
               case 5:
                  if ((0xfffffffffffffbffL & l) == 0L)
                     break;
                  if (kind > 34)
                     kind = 34;
                  jjstateSet[jjnewStateCnt++] = 5;
                  break;
               case 8:
                  if ((0x3ff000000000000L & l) == 0L)
                     break;
                  if (kind > 4)
                     kind = 4;
                  { jjCheckNAdd(8); }
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
               case 0:
                  if ((0x7fffffe07fffffeL & l) != 0L)
                  {
                     if (kind > 7)
                        kind = 7;
                  }
                  if ((0x4000000040000L & l) != 0L)
                     jjstateSet[jjnewStateCnt++] = 6;
                  break;
               case 1:
                  { jjAddStates(0, 1); }
                  break;
               case 3:
                  if ((0x7fffffe07fffffeL & l) != 0L && kind > 7)
                     kind = 7;
                  break;
               case 4:
                  if ((0x200000002000L & l) == 0L)
                     break;
                  if (kind > 34)
                     kind = 34;
                  { jjCheckNAdd(5); }
                  break;
               case 5:
                  if (kind > 34)
                     kind = 34;
                  { jjCheckNAdd(5); }
                  break;
               case 6:
                  if ((0x2000000020L & l) != 0L)
                     jjstateSet[jjnewStateCnt++] = 4;
                  break;
               case 7:
                  if ((0x4000000040000L & l) != 0L)
                     jjstateSet[jjnewStateCnt++] = 6;
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
               case 1:
                  if ((jjbitVec0[i2] & l2) != 0L)
                     { jjAddStates(0, 1); }
                  break;
               case 5:
                  if ((jjbitVec0[i2] & l2) == 0L)
                     break;
                  if (kind > 34)
                     kind = 34;
                  jjstateSet[jjnewStateCnt++] = 5;
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
      if ((i = jjnewStateCnt) == (startsAt = 9 - (jjnewStateCnt = startsAt)))
         return curPos;
      try { curChar = input_stream.readChar(); }
      catch(java.io.IOException e) { return curPos; }
   }
}

/** Token literal values. */
public static final String[] jjstrLiteralImages = {
"", null, "\12", "\72", null, null, null, null, null, null, null, null, null, 
null, null, null, null, null, null, null, null, null, null, null, null, null, null, 
null, null, null, null, null, null, null, null, null, null, null, null, null, null, 
null, null, null, null, null, null, null, null, "\53", "\55", "\52", "\57", "\136", 
"\75", "\74", "\76", "\74\76", "\74\75", "\76\75", };
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
   1, 2, 
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
    for (i = 9; i-- > 0;)
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
   -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 
};
static final long[] jjtoToken = {
   0xffffffffffffffdL, 
};
static final long[] jjtoSkip = {
   0x2L, 
};
static final long[] jjtoSpecial = {
   0x0L, 
};
static final long[] jjtoMore = {
   0x0L, 
};
    protected SimpleCharStream  input_stream;

    private final int[] jjrounds = new int[9];
    private final int[] jjstateSet = new int[2 * 9];
    private final StringBuilder jjimage = new StringBuilder();
    private StringBuilder image = jjimage;
    private int jjimageLen;
    private int lengthOfMatch;
    protected int curChar;
}
