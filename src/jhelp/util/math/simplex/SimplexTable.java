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
package jhelp.util.math.simplex;

import jhelp.util.Utilities;
import jhelp.util.debug.Debug;
import jhelp.util.debug.DebugLevel;
import jhelp.util.math.UtilMath;

/**
 * Table used for resolves NP inequality with simplex method.<br>
 * NP inequality have the form :<br>
 *
 * <pre>
 * Maximize <b>p</b> where :
 * p = v<sub>1</sub>x<sub>1</sub> + ... + v<sub>n</sub>x<sub>n</sub>
 * c<sub>1,1</sub>x<sub>1</sub> + ... + c<sub>n,1</sub>x<sub>n</sub> ≤ a<sub>1</sub>
 * ...
 * c<sub>1,m</sub>x<sub>1</sub> + ... + c<sub>n,m</sub>x<sub>n</sub> ≤ a<sub>m</sub>
 * </pre>
 *
 * Where <b>p</b> is the objective name, v<sub>i</sub>, c<sub>i,j</sub> and a<sub>j</sub> are fixed integer and x<sub>i</sub>
 * are parameters names<br>
 * It is also possible to use ≥ instead of ≤, just on take care of slack/surplus.<br>
 * For each inequality, you have to add a slack/surplus named s<sub>j</sub> and the equation become :
 *
 * <pre>
 * Maximize <b>p</b> where :
 * p = v<sub>1</sub>x<sub>1</sub> + ... + v<sub>n</sub>x<sub>n</sub>
 * c<sub>1,1</sub>x<sub>1</sub> + ... + c<sub>n,1</sub>x<sub>n</sub> ± s<sub>1</sub> = a<sub>1</sub>
 * ...
 * c<sub>1,m</sub>x<sub>1</sub> + ... + c<sub>n,m</sub>x<sub>n</sub> ± s<sub>m</sub> = a<sub>m</sub>
 * </pre>
 *
 * Use <b>+</b> for ≤ and <b>-</b> for ≥.<br>
 * With this the value table is :
 *
 * <pre>
 * c<sub>1,1</sub> ... c<sub>n,1</sub> ±1 0 ... 0
 * c<sub>1,2</sub> ... c<sub>n,2</sub> 0 ±1 0 ... 0
 * ...
 * c<sub>1,m</sub> ... c<sub>n,m</sub> 0...0 ±1 0
 * -v<sub>1</sub> ... -v<sub>n</sub> 0...0 1
 * </pre>
 *
 * The answer table is :
 *
 * <pre>
 * a<sub>1</sub> ... a<sub>m</sub> 0
 * </pre>
 *
 * @author JHelp <br>
 */
public class SimplexTable
{
   /** Enable/disable debug information */
   private static final boolean DEBUG = false;

   /** Basics values */
   private final char[]         basics;
   /** Table height */
   private final int            height;
   /** Objective symbol */
   private final char           objective;
   /** Parameters symbols */
   private final char[]         parameters;
   /** Table with answers at right */
   private final int[]          table;
   /** Table width */
   private final int            width;

   /**
    * Create a new instance of SimplexTable.<br>
    * See class comment to know how fill parameters.
    *
    * @param objective
    *           Objective name
    * @param variables
    *           Variables/parameters name
    * @param slackSurplus
    *           Slack/surplus name
    * @param values
    *           Table values
    * @param answers
    *           Table answers
    */
   public SimplexTable(final char objective, final char[] variables, final char[] slackSurplus, final int[][] values, final int[] answers)
   {
      // Basics check
      if(values.length != (slackSurplus.length + 1))
      {
         throw new IllegalArgumentException("Number of lines in values MUST be the same as number of slack/surplus +1 (for objective)");
      }

      if(answers.length != (slackSurplus.length + 1))
      {
         throw new IllegalArgumentException("Number of answers MUST be the same as number of slack/surplus +1(for objective)");
      }

      final int lineLength = variables.length + slackSurplus.length + 1;

      for(int i = values.length - 1; i >= 0; i--)
      {
         if(values[i].length != lineLength)
         {
            throw new IllegalArgumentException(
                  "The line " + i + " of values MUST have length equal to number of variable + number of slack/surplus +1(for objective)");
         }
      }

      if(Utilities.indexOf(variables, objective) >= 0)
      {
         throw new IllegalArgumentException("objective symbol is also use in variables !");
      }

      if(Utilities.indexOf(slackSurplus, objective) >= 0)
      {
         throw new IllegalArgumentException("objective symbol is also use in slackSurplus !");
      }

      for(int i = variables.length - 1; i >= 0; i--)
      {
         if(Utilities.indexOf(variables, variables[i]) != i)
         {
            throw new IllegalArgumentException(variables[i] + " duplicates in variables ! !");
         }

         if(Utilities.indexOf(slackSurplus, variables[i]) >= 0)
         {
            throw new IllegalArgumentException(variables[i] + " from variables found also is slackSurplus !");
         }
      }

      for(int i = slackSurplus.length - 1; i >= 0; i--)
      {
         if(Utilities.indexOf(slackSurplus, slackSurplus[i]) != i)
         {
            throw new IllegalArgumentException(slackSurplus[i] + " duplicates in slackSurplus ! !");
         }
      }

      // Initialize
      this.objective = objective;
      this.width = lineLength + 1;
      this.height = answers.length;
      this.table = new int[this.width * this.height];
      this.parameters = new char[variables.length + slackSurplus.length];
      this.basics = new char[slackSurplus.length];

      // Fill table
      final int length = values.length;
      int index = 0;

      for(int i = 0; i < length; i++)
      {
         System.arraycopy(values[i], 0, this.table, index, lineLength);
         index += lineLength;
         this.table[index] = answers[i];
         index++;
      }

      // Fill parameters
      System.arraycopy(variables, 0, this.parameters, 0, variables.length);
      System.arraycopy(slackSurplus, 0, this.parameters, variables.length, slackSurplus.length);

      // Fill basics
      System.arraycopy(slackSurplus, 0, this.basics, 0, slackSurplus.length);
   }

   /**
    * Compute column for eliminate phase
    *
    * @return Column to use OR -1 if no more column to eliminate
    */
   private int columnForEliminate()
   {
      final int length = this.basics.length;
      final int limit = this.width - 1;
      int max = 0;
      int index, column;

      for(int i = 0; i < length; i++)
      {
         column = Utilities.indexOf(this.parameters, this.basics[i]);

         if(this.table[column + (i * this.width)] < 0)
         {
            index = i * this.width;
            column = -1;

            for(int j = 0; j < limit; j++, index++)
            {
               if(this.table[index] > max)
               {
                  max = this.table[index];
                  column = j;
               }
            }

            if(column < 0)
            {
               Debug.println(DebugLevel.WARNING, "Column for eliminate not found since their a negative basics : ", this.basics[i]);
            }

            return column;
         }
      }

      return -1;
   }

   /**
    * Compute column for optimize phase
    *
    * @return Column to use or -1 if no more optimization are possible
    */
   private int columnForOptimize()
   {
      int column = -1;
      int min = 0;
      final int length = this.width - 1;
      int index = this.table.length - this.width;
      int value;

      for(int i = 0; i < length; i++)
      {
         value = this.table[index];
         index++;

         if(value < min)
         {
            min = value;
            column = i;
         }
      }

      return column;
   }

   /**
    * Pivot the table at given pivot position
    *
    * @param pivot
    *           Pivot position
    */
   private void doPivot(final int pivot)
   {
      final int column = pivot % this.width;
      final int line = pivot / this.width;
      final int valuePivot = this.table[pivot];
      final int signPivot = UtilMath.sign(valuePivot);

      int index = column + (this.width * this.basics.length);
      final int startPivot = line * this.width;
      int coefOther, coefPivot, valueOther, indexOther, indexPivot, gcd;

      for(int i = this.basics.length; i >= 0; i--)
      {
         if(i != line)
         {
            valueOther = this.table[index];

            if(valueOther != 0)
            {
               coefOther = Math.abs(valuePivot);
               coefPivot = -valueOther * signPivot;
               gcd = UtilMath.greaterCommonDivisor(coefOther, coefPivot);
               coefOther /= gcd;
               coefPivot /= gcd;
               indexOther = i * this.width;
               indexPivot = startPivot;

               for(int j = 0; j < this.width; j++, indexOther++, indexPivot++)
               {
                  this.table[indexOther] = (coefOther * this.table[indexOther]) + (coefPivot * this.table[indexPivot]);
               }
            }
         }

         index -= this.width;
      }

      this.basics[line] = this.parameters[column];

      if(SimplexTable.DEBUG)
      {
         System.out.println("Pivot : (" + column + ", " + line + ")");
      }
   }

   /**
    * Elimination phase
    */
   private void eliminateNegativeBasics()
   {
      int pivot = this.pivotForEliminate();

      while(pivot >= 0)
      {
         this.doPivot(pivot);
         pivot = this.pivotForEliminate();

         if(SimplexTable.DEBUG)
         {
            System.out.println("*********");
            System.out.println(this);
         }
      }
   }

   /**
    * Optimization phase
    */
   private void optimizeResult()
   {
      int pivot = this.pivotForOptimize();

      while(pivot >= 0)
      {
         this.doPivot(pivot);
         pivot = this.pivotForOptimize();

         if(SimplexTable.DEBUG)
         {
            System.out.println("--------");
            System.out.println(this);
         }
      }
   }

   /**
    * Compute pivot to use for elimination phase
    *
    * @return Pivot to use OR -1 if no more to eliminate
    */
   private int pivotForEliminate()
   {
      final int column = this.columnForEliminate();

      if(column < 0)
      {
         return -1;
      }

      return this.pivotInColumn(column);
   }

   /**
    * Compute pivot to use for optimization phase
    *
    * @return Pivot to use OR -1 if no more optimization
    */
   private int pivotForOptimize()
   {
      final int column = this.columnForOptimize();

      if(column < 0)
      {
         return -1;
      }

      return this.pivotInColumn(column);
   }

   /**
    * Compute pivot to use for given column
    *
    * @param column
    *           Column
    * @return Pivot to use OR -1 if not found
    */
   private int pivotInColumn(final int column)
   {
      int pivot = -1;
      float minimumRate = Float.MAX_VALUE;
      final int length = this.basics.length;
      int value;
      float rate;
      int index = column;

      for(int i = 0; i < length; i++)
      {
         value = this.table[index];

         if(value > 0)
         {
            rate = (float) this.table[((i + 1) * this.width) - 1] / (float) value;

            if(rate < minimumRate)
            {
               pivot = index;
               minimumRate = rate;
            }
         }

         index += this.width;
      }

      if(pivot < 0)
      {
         Debug.println(DebugLevel.WARNING, "pivot not found, may be their an issue ! Or for the current table may be have no solution");
      }

      return pivot;
   }

   /**
    * Number of character used for represents given integer in 10 base
    *
    * @param value
    *           Integer to print
    * @return Number of character used for represents given integer in 10 base
    */
   private int sizeOfNumber(final int value)
   {
      if(value == 0)
      {
         return 1;
      }

      return (int) Math.log10(Math.abs(value)) + (value < 0
            ? 2
            : 1);
   }

   /**
    * Resolve the table
    *
    * @return Result of maximum and each variable/parameter and slack/surplus value
    */
   public SimplexResult resolveTable()
   {
      this.eliminateNegativeBasics();
      this.optimizeResult();

      // Collect the answer
      final double[] result = new double[this.parameters.length + 1];
      int index;

      for(int i = this.basics.length - 1; i >= 0; i--)
      {
         index = Utilities.indexOf(this.parameters, this.basics[i]);
         result[index] = (double) this.table[((i + 1) * this.width) - 1] / (double) this.table[index + (i * this.width)];
      }

      result[this.parameters.length] = (double) this.table[this.table.length - 1] / (double) this.table[this.table.length - 2];
      return new SimplexResult(this.parameters, this.objective, result);
   }

   /**
    * String representation <br>
    * <br>
    * <b>Parent documentation:</b><br>
    * {@inheritDoc}
    *
    * @return String representation
    * @see java.lang.Object#toString()
    */
   @Override
   public String toString()
   {
      int size = 1;

      for(int i = this.table.length - 1; i >= 0; i--)
      {
         size = Math.max(size, this.sizeOfNumber(this.table[i]));
      }

      int left, before, after, value;
      final StringBuilder stringBuilder = new StringBuilder();

      // First line
      stringBuilder.append("  ");

      for(final char name : this.parameters)
      {
         left = size - 1;
         after = left >> 1;
         before = left - after;

         for(int i = 0; i < before; i++)
         {
            stringBuilder.append(' ');
         }

         stringBuilder.append(name);

         for(int i = 0; i < after; i++)
         {
            stringBuilder.append(' ');
         }

         stringBuilder.append(' ');
      }

      left = size - 1;
      after = left >> 1;
      before = left - after;

      for(int i = 0; i < before; i++)
      {
         stringBuilder.append(' ');
      }

      stringBuilder.append(this.objective);

      stringBuilder.append('\n');

      // Table
      int index = 0;
      final int length = this.basics.length;

       for (char basic : this.basics)
       {
           stringBuilder.append(basic);
           stringBuilder.append(' ');

           for (int c = 0; c < this.width; c++)
           {
               value = this.table[index];
               index++;
               left = size - this.sizeOfNumber(value);
               after = left >> 1;
               before = left - after;

               for (int i = 0; i < before; i++)
               {
                   stringBuilder.append(' ');
               }

               stringBuilder.append(value);

               for (int i = 0; i < after; i++)
               {
                   stringBuilder.append(' ');
               }

               stringBuilder.append(' ');
           }

           stringBuilder.append('\n');
       }

      // Last line
      stringBuilder.append(this.objective);
      stringBuilder.append(' ');

      for(int c = 0; c < this.width; c++)
      {
         value = this.table[index];
         index++;
         left = size - this.sizeOfNumber(value);
         after = left >> 1;
         before = left - after;

         for(int i = 0; i < before; i++)
         {
            stringBuilder.append(' ');
         }

         stringBuilder.append(value);

         for(int i = 0; i < after; i++)
         {
            stringBuilder.append(' ');
         }

         stringBuilder.append(' ');
      }

      return stringBuilder.toString();
   }
}