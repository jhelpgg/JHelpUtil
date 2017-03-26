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

/**
 * Result of {@link EquationNP} after a {@link EquationNP#resolve()}.<br>
 * It contains information that indicates if its the minimum or maximum.<br>
 * The objective value and values of variables.
 *
 * @author JHelp <br>
 */
public class ResultEquationNP
{
   /** Indicates if its a maximum or minimum */
   private final boolean   maximize;
   /** Objective name */
   private final char      objective;
   /** Initial objective condition */
   private final Condition objectiveCondition;
   /** Objective value */
   private final double    objectiveValue;
   /** Parameters/variables names */
   private final char[]    parameters;
   /** Parameters/variables values */
   private final double[]  values;

   /**
    * Create a new instance of ResultEquationNP
    *
    * @param maximize
    *           Indicates if its a maximum or minimum
    * @param objective
    *           Objective name
    * @param objectiveValue
    *           Objective value
    * @param objectiveCondition
    *           Initial objective condition
    * @param parameters
    *           Parameters/variables names
    * @param values
    *           Parameters/variables values
    */
   ResultEquationNP(final boolean maximize, final char objective, final double objectiveValue, final Condition objectiveCondition, final char[] parameters,
         final double[] values)
   {
      this.maximize = maximize;
      this.objective = objective;
      this.objectiveValue = objectiveValue;
      this.objectiveCondition = objectiveCondition;
      this.parameters = parameters;
      this.values = values;
   }

   /**
    * Objective name
    *
    * @return Objective name
    */
   public char getObjective()
   {
      return this.objective;
   }

   /**
    * Objective value
    *
    * @return Objective value
    */
   public double getObjectiveValue()
   {
      return this.objectiveValue;
   }

   /**
    * Parameters names
    *
    * @return Parameters names
    */
   public char[] getParameters()
   {
      return Utilities.createCopy(this.parameters);
   }

   /**
    * Value of a parameter
    *
    * @param variable
    *           Variable name
    * @return Parameter/variable name
    */
   public double getValue(final char variable)
   {
      final int index = Utilities.indexOf(this.parameters, variable);

      if(index < 0)
      {
         throw new IllegalArgumentException(variable + " not found !");
      }

      return this.values[index];
   }

   /**
    * Parameters values
    *
    * @return Parameters values
    */
   public double[] getValues()
   {
      return Utilities.createCopy(this.values);
   }

   /**
    * Indicates if objective is maximized or minimized
    *
    * @return {@code true} if objective is maximized. {@code false} if objective is minimized
    */
   public boolean isMaximize()
   {
      return this.maximize;
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
         stringBuilder.append("Maximum of \"");
      }
      else
      {
         stringBuilder.append("Minimum of \"");
      }

      stringBuilder.append(this.objective);
      stringBuilder.append("=");
      this.objectiveCondition.appendLeftPartInside(stringBuilder);
      stringBuilder.append("\" is ");
      stringBuilder.append(this.objective);
      stringBuilder.append("=");
      stringBuilder.append(this.objectiveValue);
      stringBuilder.append(" where");
      final int length = this.parameters.length;

      for(int i = 0; i < length; i++)
      {
         stringBuilder.append(' ');
         stringBuilder.append(this.parameters[i]);
         stringBuilder.append('=');
         stringBuilder.append(this.values[i]);
      }

      return stringBuilder.toString();
   }
}