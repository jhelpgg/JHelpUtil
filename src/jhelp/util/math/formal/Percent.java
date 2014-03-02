package jhelp.util.math.formal;

/**
 * Represents a percent
 * 
 * @author JHelp
 */
public class Percent
      extends UnaryOperator
{
   /**
    * Simplifer of percent
    * 
    * @author JHelp
    */
   class PercentSimplifier
         implements FunctionSimplifier
   {
      /**
       * Simplify the percent <br>
       * <br>
       * <b>Parent documentation:</b><br>
       * {@inheritDoc}
       * 
       * @return Simplified function
       * @see jhelp.util.math.formal.FunctionSimplifier#simplify()
       */
      @Override
      public Function simplify()
      {
         final Function function = Percent.this.parameter.simplify();

         if(function instanceof Constant)
         {
            final Constant constant = (Constant) function;

            if(constant.isUndefined() == true)
            {
               return Constant.UNDEFINED;
            }

            if(constant.isNegative() == true)
            {
               return new MinusUnary(new Percent(constant.absoluteValue()));
            }

            if(constant.isNul() == true)
            {
               return Constant.ZERO;
            }

            return new Percent(function);
         }

         if(function instanceof MinusUnary)
         {
            return new MinusUnary(new Percent(function));
         }

         return new Percent(function);
      }
   }

   /** Percent simplifier */
   private PercentSimplifier percentSimplifier;

   /**
    * Create a new instance of Percent
    * 
    * @param parameter
    *           Percent parmeter
    */
   public Percent(final Function parameter)
   {
      super("%", parameter);
   }

   /**
    * Indicates if function is equals to this percent in "fast" way <br>
    * <br>
    * <b>Parent documentation:</b><br>
    * {@inheritDoc}
    * 
    * @param function
    *           Function to compare with
    * @return {@code true} if there equals
    * @see jhelp.util.math.formal.Function#functionIsEqualsMoreSimple(jhelp.util.math.formal.Function)
    */
   @Override
   protected boolean functionIsEqualsMoreSimple(final Function function)
   {
      if(function == null)
      {
         return false;
      }

      if(function instanceof Percent)
      {
         return this.parameter.functionIsEqualsMoreSimple(((Percent) function).parameter);
      }

      return false;
   }

   /**
    * Derive the function <br>
    * <br>
    * <b>Parent documentation:</b><br>
    * {@inheritDoc}
    * 
    * @param variable
    *           Varaible to derive with
    * @return Derived
    * @see jhelp.util.math.formal.Function#derive(jhelp.util.math.formal.Variable)
    */
   @Override
   public Function derive(final Variable variable)
   {
      return new Percent(this.parameter.derive(variable));
   }

   /**
    * Indicates if a function is equals to this percent <br>
    * <br>
    * <b>Parent documentation:</b><br>
    * {@inheritDoc}
    * 
    * @param function
    *           Function to compare with
    * @return {@code true} if function is equals
    * @see jhelp.util.math.formal.Function#functionIsEquals(jhelp.util.math.formal.Function)
    */
   @Override
   public boolean functionIsEquals(final Function function)
   {
      if(function == null)
      {
         return false;
      }

      if((function instanceof Percent) == false)
      {
         return false;
      }

      final Percent percent = (Percent) function;

      return this.parameter.functionIsEqualsMoreSimple(percent.parameter);
   }

   /**
    * Copy the percent function <br>
    * <br>
    * <b>Parent documentation:</b><br>
    * {@inheritDoc}
    * 
    * @return Percent function copy
    * @see jhelp.util.math.formal.Function#getCopy()
    */
   @Override
   public Function getCopy()
   {
      return new Percent(this.parameter.getCopy());
   }

   /**
    * Obtain the percent simplifer <br>
    * <br>
    * <b>Parent documentation:</b><br>
    * {@inheritDoc}
    * 
    * @return Percent simplifier
    * @see jhelp.util.math.formal.Function#obtainFunctionSimplifier()
    */
   @Override
   public FunctionSimplifier obtainFunctionSimplifier()
   {
      if(this.percentSimplifier == null)
      {
         this.percentSimplifier = new PercentSimplifier();
      }

      return this.percentSimplifier;
   }

   /**
    * Replace a vraible by a function <br>
    * <br>
    * <b>Parent documentation:</b><br>
    * {@inheritDoc}
    * 
    * @param variable
    *           variable to replace
    * @param function
    *           Replacement function
    * @return Result function
    * @see jhelp.util.math.formal.Function#replace(jhelp.util.math.formal.Variable, jhelp.util.math.formal.Function)
    */
   @Override
   public Function replace(final Variable variable, final Function function)
   {
      return new Percent(this.parameter.replace(variable, function));
   }

   /**
    * String that represents the function
    * 
    * @return String representation
    * @see jhelp.util.math.formal.Function#toString()
    */
   @Override
   public String toString()
   {
      final StringBuffer sb = new StringBuffer(12);
      sb.append('(');
      sb.append(this.parameter.toString());
      sb.append(")%");
      return sb.toString();
   }
}