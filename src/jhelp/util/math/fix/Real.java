package jhelp.util.math.fix;

import java.text.NumberFormat;

import jhelp.util.io.Binarizable;
import jhelp.util.io.ByteArray;
import jhelp.util.math.UtilMath;

/**
 * Fixed comma decimal
 * 
 * @author JHelp
 */
public class Real
      extends Number
      implements Comparable<Real>, Binarizable
{
   /** Comma place */
   private static final long         COMMA;
   /** Fraction value */
   private static final long         FRACTION;
   /** Format to use for print number */
   private static final NumberFormat NUMBER_FORMAT;
   /** Shift to use for the comma */
   private static final long         SHIFT;
   /** Number to multiply to have an integer */
   private static final long         TEN;
   static
   {
      SHIFT = 32L;
      COMMA = 1L << Real.SHIFT;
      FRACTION = Real.COMMA - 1L;

      long ten = 1L;
      int time = 0;

      while((ten * 10L) < Real.COMMA)
      {
         ten *= 10L;
         time++;
      }

      TEN = ten;
      NUMBER_FORMAT = NumberFormat.getNumberInstance();
      Real.NUMBER_FORMAT.setMinimumFractionDigits(0);
      Real.NUMBER_FORMAT.setMaximumFractionDigits(time);
   }

   /**
    * Convert a double to its long fix comma representation
    * 
    * @param value
    *           Value to convert
    * @return Converted value
    */
   private static long toLong(double value)
   {
      final int sign = UtilMath.sign(value);
      value = Math.abs(value);
      final long integer = (long) value;

      return sign * ((integer << Real.SHIFT) | Math.round((value - integer) * Real.TEN));
   }

   /**
    * Parse a String to real fix comma
    * 
    * @param string
    *           String to parse
    * @return Parsed value
    */
   public static Real parse(final String string)
   {
      return new Real(Double.parseDouble(string));
   }

   /** Long representation value */
   private long value;

   /**
    * Create a new instance of Real zero
    */
   public Real()
   {
      this(0);
   }

   /**
    * Create a new instance of Real
    * 
    * @param value
    *           Double value to convert
    */
   public Real(final double value)
   {
      this.value = Real.toLong(value);
   }

   /**
    * Create a new instance of Real
    * 
    * @param value
    *           Integer to convert
    */
   public Real(final int value)
   {
      this.value = UtilMath.sign(value) * (((long) Math.abs(value)) << Real.SHIFT);
   }

   /**
    * Create a new instance of Real
    * 
    * @param real
    *           Real to copy
    */
   public Real(final Real real)
   {
      this.value = real.value;
   }

   /**
    * Add an other real
    * 
    * @param real
    *           Real to add
    */
   public void addition(final Real real)
   {
      this.value += real.value;
   }

   /**
    * Add this real with an other
    * 
    * @param real
    *           Real to add
    * @param result
    *           Real who receive the result (If {@code null} a new Real is created)
    * @return Addition result
    */
   public Real addition(final Real real, Real result)
   {
      if(result == null)
      {
         result = new Real();
      }

      result.value = this.value + real.value;

      return result;
   }

   /**
    * Compare with an other Real <br>
    * <br>
    * <b>Parent documentation:</b><br>
    * {@inheritDoc}
    * 
    * @param real
    *           Real to compare
    * @return Comparison
    * @see java.lang.Comparable#compareTo(java.lang.Object)
    */
   @Override
   public int compareTo(final Real real)
   {
      return UtilMath.sign(this.value - real.value);
   }

   /**
    * Copy the Real
    * 
    * @return Copy
    */
   public Real copy()
   {
      return new Real(this);
   }

   public void division(final Real real)
   {
      if(real.value == 0)
      {
         throw new ArithmeticException("Not divide by zero");
      }

      this.division(real, this);
   }

   public Real division(final Real real, Real result)
   {
      if(real.value == 0)
      {
         throw new ArithmeticException("Not divide by zero");
      }

      if(result == null)
      {
         result = new Real();
      }

      result.value = Real.toLong(this.value() / real.value());

      return result;
   }

   @Override
   public double doubleValue()
   {
      return this.value();
   }

   @Override
   public boolean equals(final Object obj)
   {
      if(this == obj)
      {
         return true;
      }
      if(obj == null)
      {
         return false;
      }
      if(this.getClass() != obj.getClass())
      {
         return false;
      }
      final Real other = (Real) obj;
      return this.value == other.value;
   }

   @Override
   public float floatValue()
   {
      return (float) this.value();
   }

   @Override
   public int hashCode()
   {
      final int prime = 31;
      int result = 1;
      result = (prime * result) + (int) (this.value ^ (this.value >>> 32));
      return result;
   }

   @Override
   public int intValue()
   {
      return (int) this.value();
   }

   @Override
   public long longValue()
   {
      return (long) this.value();
   }

   public void multiplication(final Real real)
   {
      this.multiplication(real, this);
   }

   public Real multiplication(final Real real, Real result)
   {
      if(result == null)
      {
         result = new Real();
      }

      result.value = Real.toLong(this.value() * real.value());

      return result;
   }

   @Override
   public void parseBinary(final ByteArray byteArray)
   {
      this.value = byteArray.readLong();
   }

   @Override
   public void serializeBinary(final ByteArray byteArray)
   {
      byteArray.writeLong(this.value);
   }

   public int sign()
   {
      if(this.value > 0)
      {
         return 1;
      }

      if(this.value < 0)
      {
         return -1;
      }

      return 0;
   }

   public void subtraction(final Real real)
   {
      this.value -= real.value;
   }

   public Real subtraction(final Real real, Real result)
   {
      if(result == null)
      {
         result = new Real();
      }

      result.value = this.value - real.value;

      return result;
   }

   @Override
   public String toString()
   {
      return Real.NUMBER_FORMAT.format(this.value());
   }

   public double value()
   {
      if(this.value == 0)
      {
         return 0;
      }

      final long val = Math.abs(this.value);
      return UtilMath.sign(this.value) * ((val >> Real.SHIFT) + ((double) (val & Real.FRACTION) / (double) Real.TEN));
   }
}