/**
 * <h1>License :</h1> <br>
 * The following code is deliver as is. I take care that code compile and work, but I am not responsible about any damage it may
 * cause.<br>
 * You can use, modify, the code as your need for any usage. But you can't do any action that avoid me or other person use,
 * modify this code. The code is free for usage and modification, you can't change that fact.<br>
 * <br>
 *
 * @author JHelp
 */
package jhelp.util.samples.math.simplex;

import jhelp.util.math.simplex.Condition;
import jhelp.util.math.simplex.EquationNP;
import jhelp.util.math.simplex.ResultEquationNP;
import jhelp.util.math.simplex.SimplexResult;
import jhelp.util.math.simplex.SimplexTable;

/**
 * Simplex table test
 *
 * @author JHelp <br>
 */
public class TestSimplex
{
   /**
    * Test of simplex table :
    *
    * <pre>
      x  y  z  s  t  u  p  Ans
   s  1  1  1  1  0  0  0  10
   t  4  −3 1  0  1  0  0  3
   u  2  1  −1 0  0  1  0  10
   p  −2 3  −1 0  0  0  1  0
    * </pre>
    *
    * @param args
    *           Unused
    */
   public static void main(final String[] args)
   {
      final char objective = 'p';
      final char[] parameters =
      {
            'x', 'y', 'z'
      };
      final char[] slackSurplus =
      {
            's', 't', 'u'
      };
      final int[][] values =
      {
            {
                  1, 1, 1, 1, 0, 0, 0
            }, //
            {
                  4, -3, 1, 0, 1, 0, 0
            }, //
            {
                  2, 1, -1, 0, 0, 1, 0
            }, //
            {
                  -2, 3, -1, 0, 0, 0, 1
            },//
      };
      final int[] answers =
      {
            10, 3, 10, 0
      };
      SimplexTable simplexTable = new SimplexTable(objective, parameters, slackSurplus, values, answers);
      System.out.println(simplexTable);
      SimplexResult simplexResult = simplexTable.resolveTable();
      System.out.println("vvvvvvvvvvvvvvvvvvvv");
      System.out.println(simplexResult);

      System.out.println();
      System.out.println();
      System.out.println();

      final char objective2 = 'p';
      final char[] parameters2 =
      {
            'x', 'y', 'z'
      };
      final char[] slackSurplus2 =
      {
            's', 't', 'u'
      };
      final int[][] values2 =
      {
            { // x+y+z+s=40
                  1, 1, 1, 1, 0, 0, 0
            }, //
            { // 2x+y-z-t=10
                  2, 1, -1, 0, -1, 0, 0
            }, //
            { // -y+z-u=10
                  0, -1, 1, 0, 0, -1, 0
            }, //
            { // -2x-3y-z+p=0
                  -2, -3, -1, 0, 0, 0, 1
            }
      };
      final int[] answers2 =
      {
            40, 10, 10, 0
      };

      simplexTable = new SimplexTable(objective2, parameters2, slackSurplus2, values2, answers2);
      System.out.println(simplexTable);
      simplexResult = simplexTable.resolveTable();
      System.out.println("vvvvvvvvvvvvvvvvvvvv");
      System.out.println(simplexResult);

      System.out.println();
      System.out.println();
      System.out.println();

      final char objective3 = 'f';
      final char[] parameters3 =
      {
            'x', 'y'
      };
      final char[] slackSurplus3 =
      {
            's', 't', 'u'
      };
      final int[][] values3 =
      {
            { // x+s
                  1, 0, 1, 0, 0, 0
            }, //
            { // x-t
                  1, 0, 0, -1, 0, 0
            }, //
            { // y+u
                  0, 1, 0, 0, 1, 0
            }, //
            { // -x-y+p
                  -1, -1, 0, 0, 0, 1
            }
      };
      final int[] answers3 =
      {
            3, 3, 5, 0
      };

      simplexTable = new SimplexTable(objective3, parameters3, slackSurplus3, values3, answers3);
      System.out.println(simplexTable);
      simplexResult = simplexTable.resolveTable();
      System.out.println("vvvvvvvvvvvvvvvvvvvv");
      System.out.println(simplexResult);

      System.out.println();
      System.out.println();
      System.out.println();

      final char objective4 = 'f';
      final char[] parameters4 =
      {
            'x', 'y'
      };
      final char[] slackSurplus4 =
      {
            's', 't', 'u'
      };
      final int[][] values4 =
      {
            { // x+s
                  1, 0, 1, 0, 0, 0
            }, //
            { // x-t
                  1, 0, -1, 0, 0, 0
            }, //
            { // y+u
                  0, 1, 0, 1, 0, 0
            }, //
            { // -x-y+p
                  -1, -1, 0, 0, 0, 1
            }
      };
      final int[] answers4 =
      {
            3, 3, 5, 0
      };

      simplexTable = new SimplexTable(objective4, parameters4, slackSurplus4, values4, answers4);
      System.out.println(simplexTable);
      simplexResult = simplexTable.resolveTable();
      System.out.println("vvvvvvvvvvvvvvvvvvvv");
      System.out.println(simplexResult);

      System.out.println();
      final Condition condition = Condition.parse("z\t-   10x-5t   -   y≤ 85");
      System.out.println(condition);

      System.out.println();
      final EquationNP equationNP = new EquationNP(true, "p=2x-3y+z", "x+y+z≤10", "4x-3y+z≤3", "2x+y-z≤10");
      System.out.println(equationNP);

      System.out.println();
      simplexTable = equationNP.toSimplexTable();
      System.out.println(simplexTable);

      System.out.println();
      final ResultEquationNP resultEquationNP = equationNP.resolve();
      System.out.println(resultEquationNP);
   }
}