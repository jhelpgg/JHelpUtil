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
 * Result of {@link SimplexTable} after a {@link SimplexTable#resolveTable()}.<br>
 * It contains : the objective maximum value, the variables values and slack/surplus values
 *
 * @author JHelp <br>
 */
public class SimplexResult
{
   /** Objective name */
   private final char     objective;
   /** Variables and slack/surplus names */
   private final char[]   parameters;
   /** Variables and slack/surplus values */
   private final double[] values;

   /**
    * Create a new instance of SimplexResult
    *
    * @param parameters
    *           Variables and slack/surplus names
    * @param objective
    *           Objective name
    * @param values
    *           Variables and slack/surplus values
    */
   SimplexResult(final char[] parameters, final char objective, final double[] values)
   {
      this.parameters = parameters;
      this.objective = objective;
      this.values = values;
   }

   /**
    * Objective name
    *
    * @return Objective name
    */
   public char objective()
   {
      return this.objective;
   }

   /**
    * Objective maximum value
    *
    * @return Objective maximum value
    */
   public double objectiveValue()
   {
      return this.values[this.values.length - 1];
   }

   /**
    * Obtain value of variable or slack/surplus for reach the objective maximum value
    *
    * @param name
    *           Variable or slack/surplus name
    * @return Variable or slack/surplus value
    */
   public double obtainValue(final char name)
   {
      if(name == this.objective)
      {
         return this.values[this.values.length - 1];
      }

      for(int i = this.parameters.length - 1; i >= 0; i--)
      {
         if(this.parameters[i] == name)
         {
            return this.values[i];
         }
      }

      throw new IllegalArgumentException("name '" + name + "' not a parameter name nor objective name");
   }

   /**
    * Names of variables and slack/surplus
    *
    * @return Names of variables and slack/surplus
    */
   public char[] parameters()
   {
      return Utilities.createCopy(this.parameters);
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
      final int length = this.parameters.length;

      for(int i = 0; i < length; i++)
      {
         stringBuilder.append(this.parameters[i]);
         stringBuilder.append('=');
         stringBuilder.append(this.values[i]);
         stringBuilder.append(" | ");
      }

      stringBuilder.append(this.objective);
      stringBuilder.append('=');
      stringBuilder.append(this.values[length]);

      return stringBuilder.toString();
   }
}