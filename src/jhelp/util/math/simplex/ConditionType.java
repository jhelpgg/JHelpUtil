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

/**
 * Type of constraints : ≤, ≥, =
 *
 * @author JHelp <br>
 */
public enum ConditionType
{
   /** Equality = */
   EQUAL('='),
   /** Inequality ≥ */
   GREATER_OR_EQUAL(Condition.GREATER_OR_EQUAL_SYMBOL),
   /** Inequality ≤ */
   LOWER_OR_EQUAL(Condition.LOWER_OR_EQUAL_SYMBOL);

   /** Equality/inequality symbol */
   private final char symbol;

   /**
    * Create a new instance of ConditionType
    *
    * @param symbol
    *           Equality/inequality symbol
    */
   ConditionType(final char symbol)
   {
      this.symbol = symbol;
   }

   /**
    * Equality/inequality symbol
    *
    * @return Equality/inequality symbol
    */
   public char getSymbol()
   {
      return this.symbol;
   }
}