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
import jhelp.util.list.SortedArray;

/**
 * Represents an equation NP<br>
 * NP inequality have the form :<br>
 *
 * <pre>
 * Maximize/Minimize <b>p</b> where :
 * p = v<sub>1</sub>x<sub>1</sub> + ... + v<sub>n</sub>x<sub>n</sub>
 * c<sub>1,1</sub>x<sub>1</sub> + ... + c<sub>n,1</sub>x<sub>n</sub> ≤ a<sub>1</sub>
 * ...
 * c<sub>1,m</sub>x<sub>1</sub> + ... + c<sub>n,m</sub>x<sub>n</sub> ≤ a<sub>m</sub>
 * </pre>
 *
 * Where <b>p</b> is the objective name, v<sub>i</sub>, c<sub>i,j</sub> and a<sub>j</sub> are fixed integer and x<sub>i</sub>
 * are parameters names<br>
 * It is also possible to use ≥ or = instead of ≤.<br>
 * For :
 *
 * <pre>
 * Maximize <b>p</b> where :
 * p = 2x - 3y + z
 *  x + y +  z ≤ 30
 * 3x - y + 5z ≥ 10
 *     4y - 9z = 5
 * </pre>
 *
 * You will create EquationNP like this :
 *
 * <pre>
 * EquationNP equation = new EquationNP(true, "p = 2x - 3y + z", "x + y +  z ≤ 30", "3x - y + 5z ≥ 10", "4y - 9z = 5");
 * </pre>
 *
 * Notes :
 * <ul>
 * <li>The objective equation MUST be an equality with &lt;name of objective&gt; = &lt;equation part&gt;.</li>
 * <li>The constraints must be &lt;equation part&gt; &lt;equality symbol&gt; &lt;constant&gt;</li>
 * <li>&lt;equation part&gt; don't contains any constant alone</li>
 * <li>Objective name and variables names have one letter, are different, they are case sensitive, that means <b>a</b> is
 * different of <b>A</b>, they are alphabetic</li>
 * <li>&lt;equality symbol&gt; is ≤, ≥ or =</li>
 * </ul>
 * <br>
 * To get the result just call {@link #resolve()}
 *
 * @author JHelp <br>
 */
public class EquationNP
{
   /** Possibles slack/surplus names */
   private static final char[] NAMES;
   /** Number of possible slack/surplus names */
   private static final int    NAMES_LENGTH;

   static
   {
      NAMES_LENGTH = (('z' - 'a') + 1) << 1;
      NAMES = new char[EquationNP.NAMES_LENGTH];
      int index = 0;

      for(char character = 'a'; character <= 'z'; character++)
      {
         EquationNP.NAMES[index++] = character;
      }

      for(char character = 'A'; character <= 'Z'; character++)
      {
         EquationNP.NAMES[index++] = character;
      }
   }

   /** Constraints to respect */
   private final Condition[] constraints;
   /** Indicates if have to maximize or minimize the objective */
   private final boolean     maximize;
   /** Objective condition */
   private final Condition   objectiveCondition;
   /** Objective name */
   private final char        objectiveSymbol;

   /**
    * Create a new instance of EquationNP.<br>
    * See class documentation to know how fill the constructor
    *
    * @param maximize
    *           Indicates if objective have to be maximize ({@code true}) or minimize ({@code false})
    * @param objectiveEquality
    *           Objective equality
    * @param constraints
    *           Constraints to respect
    */
   public EquationNP(final boolean maximize, final String objectiveEquality, final String... constraints)
   {
      this.maximize = maximize;
      final int index = objectiveEquality.indexOf('=');

      if(index <= 0)
      {
         throw new IllegalArgumentException("Invalid objective equality : " + objectiveEquality);
      }

      final String before = objectiveEquality.substring(0, index).trim();

      if(before.length() != 1)
      {
         throw new IllegalArgumentException("Invalid objective equality : " + objectiveEquality);
      }

      this.objectiveSymbol = before.charAt(0);
      this.objectiveCondition = Condition.parse(objectiveEquality.substring(index + 1).trim() + "=0");
      final int length = constraints.length;
      this.constraints = new Condition[length];

      for(int i = 0; i < length; i++)
      {
         this.constraints[i] = Condition.parse(constraints[i]);
      }
   }

   /**
    * Extract all variables of all constraints (Include objective equality)
    *
    * @return All variables used in alphabetic order
    */
   private char[] extractVariables()
   {
      final SortedArray<Character> symbols = new SortedArray<Character>(Character.class, true);
      this.objectiveCondition.collectSymbols(symbols);

      for(final Condition constraint : this.constraints)
      {
         constraint.collectSymbols(symbols);
      }

      final Character[] variablesArray = symbols.toArray();
      final int numberVariables = variablesArray.length;
      final char[] variables = new char[numberVariables];

      for(int i = 0; i < numberVariables; i++)
      {
         variables[i] = variablesArray[i];
      }

      return variables;
   }

   /**
    * Resolve the equation
    *
    * @return Equation result
    */
   public ResultEquationNP resolve()
   {
      final SimplexTable simplexTable = this.toSimplexTable();
      final SimplexResult simplexResult = simplexTable.resolveTable();
      final char[] variables = this.extractVariables();
      final int length = variables.length;
      final double values[] = new double[length];

      for(int i = 0; i < length; i++)
      {
         values[i] = simplexResult.obtainValue(variables[i]);
      }

      int sign = 1;

      if(!this.maximize)
      {
         sign = -1;
      }

      return new ResultEquationNP(this.maximize, this.objectiveSymbol, sign * simplexResult.objectiveValue(), this.objectiveCondition, variables, values);
   }

   /**
    * Convert the equation to simplex table.<br>
    * If the aim is to minimize, just know if you call {@link SimplexTable#resolveTable()} to take the opposite of objective
    * value given by {@link SimplexResult#objectiveValue()}
    *
    * @return Converted table
    */
   public SimplexTable toSimplexTable()
   {
      final char[] variables = this.extractVariables();
      final int numberVariables = variables.length;
      int numberSlackSurplus = 0;

      for(final Condition constraint : this.constraints)
      {
         if(constraint.getConditionType() == ConditionType.EQUAL)
         {
            numberSlackSurplus += 2;
         }
         else
         {
            numberSlackSurplus++;
         }
      }

      final char[] slackSurplus = new char[numberSlackSurplus];
      int indexName = 0;
      char name = EquationNP.NAMES[0];

      for(int i = 0; i < numberSlackSurplus; i++)
      {
         while((name == this.objectiveSymbol) || (Utilities.indexOf(variables, name) >= 0))
         {
            indexName++;
            name = EquationNP.NAMES[indexName];
         }

         slackSurplus[i] = name;
         indexName++;
         name = EquationNP.NAMES[indexName];
      }

      final int[][] values = new int[numberSlackSurplus + 1][numberVariables + numberSlackSurplus + 1];
      final int[] answers = new int[numberSlackSurplus + 1];
      int line = 0;
      int column;
      ConditionType conditionType;

      for(final Condition constraint : this.constraints)
      {
         column = 0;
         conditionType = constraint.getConditionType();

         for(final char symbol : variables)
         {
            values[line][column] = constraint.obtainCoefficient(symbol);

            if(conditionType == ConditionType.EQUAL)
            {
               values[line + 1][column] = constraint.obtainCoefficient(symbol);
            }

            column++;
         }

         if((conditionType == ConditionType.EQUAL) || (conditionType == ConditionType.LOWER_OR_EQUAL))
         {
            values[line][column + line] = 1;
         }
         else
         {
            values[line][column + line] = -1;
         }

         if(conditionType == ConditionType.EQUAL)
         {
            values[line + 1][column + line + 1] = -1;
         }

         answers[line] = constraint.getLimit();
         line++;

         if(conditionType == ConditionType.EQUAL)
         {
            answers[line] = constraint.getLimit();
            line++;
         }
      }

      column = 0;
      int sign = -1;

      if(!this.maximize)
      {
         sign = 1;
      }

      for(final char symbol : variables)
      {
         values[line][column] = sign * this.objectiveCondition.obtainCoefficient(symbol);
         column++;
      }

      values[numberSlackSurplus][numberVariables + numberSlackSurplus] = 1;

      return new SimplexTable(this.objectiveSymbol, variables, slackSurplus, values, answers);
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
      final StringBuilder stringBuilder = new StringBuilder();

      if(this.maximize)
      {
         stringBuilder.append("MAXIMIZE:\n");
      }
      else
      {
         stringBuilder.append("MINIMIZE:\n");
      }

      stringBuilder.append(this.objectiveSymbol);
      stringBuilder.append(" = ");
      this.objectiveCondition.appendLeftPartInside(stringBuilder);
      stringBuilder.append('\n');

      for(final Condition condition : this.constraints)
      {
         condition.appendInside(stringBuilder);
         stringBuilder.append('\n');
      }

      return stringBuilder.toString();
   }
}