package jhelp.util.math.rational;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;

import jhelp.util.math.UtilMath;
import jhelp.util.text.UtilText;

/**
 * Represents a rational number in high definition
 * 
 * @author JHelp
 */
public class BigRational
      implements Comparable<BigRational>
{
   /** Context used when convert in decimal */
   private static MathContext      mathContext;
   /** Invalid rational. The rational have non meaning (like divide by 0) */
   public static final BigRational INVALID          = new BigRational(BigInteger.ZERO, BigInteger.ZERO);
   /** Invalid rational key string. Returned from {@link #toString()} if the rational is invalid */
   public static final String      INVALID_RATIONAL = "INVALID_RATIONAL";
   /** -1 rational */
   public static final BigRational MINUS_ONE        = new BigRational(UtilMath.BIG_MINUS_ONE, BigInteger.ONE);
   /** 1 rational */
   public static final BigRational ONE              = new BigRational(BigInteger.ONE, BigInteger.ONE);

   /** 0 rational */
   public static final BigRational ZERO             = new BigRational(BigInteger.ZERO, BigInteger.ONE);

   /**
    * Add 2 rational
    * 
    * @param rational1
    *           First rational
    * @param rational2
    *           Second rational
    * @return Rational result
    */
   public static BigRational addition(final BigRational rational1, final BigRational rational2)
   {
      if((rational1 == BigRational.INVALID) || (rational2 == BigRational.INVALID))
      {
         return BigRational.INVALID;
      }

      if(rational1 == BigRational.ZERO)
      {
         return rational2;
      }

      if(rational2 == BigRational.ZERO)
      {
         return rational1;
      }

      return BigRational.createRational(rational1.numerator.multiply(rational2.denominator).add(rational2.numerator.multiply(rational1.denominator)), rational1.denominator.multiply(rational2.denominator));
   }

   /**
    * Create rational from integer
    * 
    * @param integer
    *           Integer source
    * @return Result rational
    */
   public static BigRational createRational(final BigInteger integer)
   {
      return BigRational.createRational(integer, BigInteger.ONE);
   }

   /**
    * Create a rational
    * 
    * @param numerator
    *           Numerator
    * @param denominator
    *           Denominator
    * @return Created rational
    */
   public static BigRational createRational(BigInteger numerator, BigInteger denominator)
   {
      if(denominator.compareTo(BigInteger.ZERO) == 0)
      {
         return BigRational.INVALID;
      }

      if(numerator.compareTo(BigInteger.ZERO) == 0)
      {
         return BigRational.ZERO;
      }

      if(numerator.compareTo(denominator) == 0)
      {
         return BigRational.ONE;
      }

      if(numerator.compareTo(denominator.negate()) == 0)
      {
         return BigRational.MINUS_ONE;
      }

      if(denominator.signum() == -1)
      {
         numerator = numerator.negate();
         denominator = denominator.negate();
      }

      final BigInteger gcd = numerator.gcd(denominator);

      return new BigRational(numerator.divide(gcd), denominator.divide(gcd));
   }

   /**
    * Create rational from integer
    * 
    * @param integer
    *           Integer source
    * @return Result rational
    */
   public static BigRational createRational(final long integer)
   {
      return BigRational.createRational(UtilMath.createBigInteger(integer));
   }

   /**
    * Create a rational
    * 
    * @param numerator
    *           Numerator
    * @param denominator
    *           Denominator
    * @return Created rational
    */
   public static BigRational createRational(final long numerator, final long denominator)
   {
      return BigRational.createRational(UtilMath.createBigInteger(numerator), UtilMath.createBigInteger(denominator));
   }

   /**
    * Divide 2 rational
    * 
    * @param rational1
    *           First rational
    * @param rational2
    *           Second rational
    * @return Rational result
    */
   public static BigRational divide(final BigRational rational1, final BigRational rational2)
   {
      if((rational1 == BigRational.INVALID) || (rational2 == BigRational.INVALID) || (rational2 == BigRational.ZERO))
      {
         return BigRational.INVALID;
      }

      if(rational1 == BigRational.ZERO)
      {
         return BigRational.ZERO;
      }

      if(rational2 == BigRational.ONE)
      {
         return rational1;
      }

      if(rational1.equals(rational2) == true)
      {
         return BigRational.ONE;
      }

      return BigRational.createRational(rational1.numerator.multiply(rational2.denominator), rational1.denominator.multiply(rational2.numerator));
   }

   /**
    * Create (if need) and get the math context to use
    * 
    * @return Math context to use
    */
   public static MathContext mathContext()
   {
      if(BigRational.mathContext == null)
      {
         BigRational.mathContext = new MathContext(123);
      }

      return BigRational.mathContext;
   }

   /**
    * Compute the rational just in the middle of 2 other rational
    * 
    * @param rational1
    *           First rational
    * @param rational2
    *           Second rational
    * @return Middle computed
    */
   public static BigRational middle(final BigRational rational1, final BigRational rational2)
   {
      if((rational1 == BigRational.INVALID) || (rational2 == BigRational.INVALID))
      {
         return BigRational.INVALID;
      }

      return BigRational.createRational(rational1.numerator.multiply(rational2.denominator).add(rational2.numerator.multiply(rational1.denominator)), rational1.denominator.multiply(rational2.denominator.multiply(UtilMath.BIG_TWO)));
   }

   /**
    * Multiply 2 rational
    * 
    * @param rational1
    *           First rational
    * @param rational2
    *           Second rational
    * @return Rational result
    */
   public static BigRational multiply(final BigRational rational1, final BigRational rational2)
   {
      if((rational1 == BigRational.INVALID) || (rational2 == BigRational.INVALID))
      {
         return BigRational.INVALID;
      }

      if((rational1 == BigRational.ZERO) || (rational2 == BigRational.ZERO))
      {
         return BigRational.ZERO;
      }

      if(rational1 == BigRational.ONE)
      {
         return rational2;
      }

      if(rational2 == BigRational.ONE)
      {
         return rational1;
      }

      if(rational1 == BigRational.MINUS_ONE)
      {
         return rational2.opposite();
      }

      if(rational2 == BigRational.MINUS_ONE)
      {
         return rational1.opposite();
      }

      return BigRational.createRational(rational1.numerator.multiply(rational2.numerator), rational1.denominator.multiply(rational2.denominator));
   }

   /**
    * Parse a String to be a rational.<br>
    * String must be {@link #INVALID_RATIONAL} or &lt;integer&gt; or &lt;integer&gt; &lt;space&gt;* / &lt;space&gt;*
    * &lt;integer&gt;<br>
    * Where &lt;integer&gt; := [0-9]+ AND &lt;space&gt; := {SPACE, \t, \n, \r, \f}.<br>
    * If the string is not well dormated {@link IllegalArgumentException} will be throw
    * 
    * @param string
    *           String to parse
    * @return Parsed rational
    * @throws NullPointerException
    *            If string is {@code null}
    * @throws IllegalArgumentException
    *            If string can't be parsed as a rational
    */
   public static BigRational parse(String string)
   {
      string = string.trim();

      if(BigRational.INVALID_RATIONAL.equals(string) == true)
      {
         return BigRational.INVALID;
      }

      final int index = string.indexOf('/');

      if(index < 0)
      {
         try
         {
            return BigRational.createRational(new BigInteger(string));
         }
         catch(final Exception exception)
         {
            throw new IllegalArgumentException(string + " can't be parsed as a rational", exception);
         }
      }

      try
      {
         return BigRational.createRational(new BigInteger(string.substring(0, index).trim()), new BigInteger(string.substring(index + 1).trim()));
      }
      catch(final Exception exception)
      {
         throw new IllegalArgumentException(string + " can't be parsed as a rational", exception);
      }

   }

   /**
    * Put the rational at power of an integer.<br>
    * If the given integer is negative, {@link #INVALID} will be returned
    * 
    * @param rational
    *           Rational to powered
    * @param power
    *           Power to raise
    * @return Rational result
    */
   public static BigRational power(final BigRational rational, final int power)
   {
      if((rational == BigRational.INVALID) || (power < 0))
      {
         return BigRational.INVALID;
      }

      if(power == 0)
      {
         return BigRational.ONE;
      }

      if(power == 1)
      {
         return rational;
      }

      if(rational == BigRational.ZERO)
      {
         return BigRational.ZERO;
      }

      if(rational == BigRational.ONE)
      {
         return BigRational.ONE;
      }

      return BigRational.createRational(rational.numerator.pow(power), rational.denominator.pow(power));
   }

   /**
    * Subtract 2 rational
    * 
    * @param rational1
    *           First rational
    * @param rational2
    *           Second rational
    * @return Rational result
    */
   public static BigRational subtract(final BigRational rational1, final BigRational rational2)
   {
      if((rational1 == BigRational.INVALID) || (rational2 == BigRational.INVALID))
      {
         return BigRational.INVALID;
      }

      if(rational1 == BigRational.ZERO)
      {
         return rational2.opposite();
      }

      if(rational2 == BigRational.ZERO)
      {
         return rational1;
      }

      if(rational1.equals(rational2) == true)
      {
         return BigRational.ZERO;
      }

      return BigRational.createRational(rational1.numerator.multiply(rational2.denominator).subtract(rational2.numerator.multiply(rational1.denominator)), rational1.denominator.multiply(rational2.denominator));
   }

   /** Denominator */
   private final BigInteger denominator;

   /** Numerator */
   private final BigInteger numerator;

   /**
    * Create a new instance of Rational
    * 
    * @param numerator
    *           Numerator
    * @param denominator
    *           Denominator
    */
   private BigRational(final BigInteger numerator, final BigInteger denominator)
   {
      this.numerator = numerator;
      this.denominator = denominator;
   }

   /**
    * Add an other rational
    * 
    * @param rational
    *           Rational to add
    * @return Addition result
    */
   public BigRational addition(final BigRational rational)
   {
      return BigRational.addition(this, rational);
   }

   /**
    * Compare&nbsp;with&nbsp;an&nbsp;other&nbsp;rational.&nbsp;<br>
    * It&nbsp;returns&nbsp;:<br>
    * <table border=1>
    * <tr>
    * <th>&lt;&nbsp;0<br>
    * </th>
    * <td>If&nbsp;<b>&nbsp;this&nbsp;</b>&nbsp;rational&nbsp;is&nbsp;<b>&nbsp;&lt;&nbsp;</b>&nbsp;the&nbsp;given&nbsp;rational<br>
    * </td>
    * </tr>
    * <tr>
    * <th>=&nbsp;0<br>
    * </th>
    * <td>If&nbsp;<b>&nbsp;this&nbsp;</b>&nbsp;rational&nbsp;is&nbsp;<b>&nbsp;equals&nbsp;</b>&nbsp;the&nbsp;given&nbsp;rational
    * <br>
    * </td>
    * </tr>
    * <tr>
    * <th>&gt;&nbsp;0<br>
    * </th>
    * <td>If&nbsp;<b>&nbsp;this&nbsp;</b>&nbsp;rational&nbsp;is&nbsp;<b>&nbsp;&gt;&nbsp;</b>&nbsp;the&nbsp;given&nbsp;rational<br>
    * </td>
    * </tr>
    * </table>
    * <br>
    * <br>
    * <b>Parent documentation:</b><br>
    * {@inheritDoc}
    * 
    * @param rational
    *           Rational to compare with
    * @return Comparison result
    * @see java.lang.Comparable#compareTo(java.lang.Object)
    */
   @Override
   public int compareTo(final BigRational rational)
   {
      if(rational == null)
      {
         throw new NullPointerException("rational musn't be null");
      }

      if(this.equals(rational) == true)
      {
         return 0;
      }

      if(this.denominator.compareTo(rational.denominator) == 0)
      {
         return this.numerator.compareTo(rational.numerator);
      }

      if(this.denominator.signum() == 0)
      {
         return 1;
      }

      if(rational.denominator.signum() == 0)
      {
         return -1;
      }

      final BigRational difference = this.subtract(rational);

      return difference.numerator.signum();
   }

   /**
    * Divide an other rational
    * 
    * @param rational
    *           Rational to divide
    * @return Division result
    */
   public BigRational divide(final BigRational rational)
   {
      return BigRational.divide(this, rational);
   }

   /**
    * Indicates if an object is equals to this rational <br>
    * <br>
    * <b>Parent documentation:</b><br>
    * {@inheritDoc}
    * 
    * @param obj
    *           Object to test
    * @return {@code true} in equality
    * @see java.lang.Object#equals(java.lang.Object)
    */
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
      final BigRational other = (BigRational) obj;
      if(this.denominator.compareTo(other.denominator) != 0)
      {
         return false;
      }
      if(this.numerator.compareTo(other.numerator) != 0)
      {
         return false;
      }
      return true;
   }

   /**
    * Denominator
    * 
    * @return Denominator
    */
   public BigInteger getDenominator()
   {
      return this.denominator;
   }

   /**
    * Numerator
    * 
    * @return Numerator
    */
   public BigInteger getNumerator()
   {
      return this.numerator;
   }

   /**
    * Compute hash code for rational <br>
    * <br>
    * <b>Parent documentation:</b><br>
    * {@inheritDoc}
    * 
    * @return Hash code
    * @see java.lang.Object#hashCode()
    */
   @Override
   public int hashCode()
   {
      final int prime = 127;
      int result = 1;
      result = (prime * result) + this.denominator.hashCode();
      result = (prime * result) + this.numerator.hashCode();
      return result;
   }

   /**
    * Inverse the rational
    * 
    * @return Inverted
    */
   public BigRational inverse()
   {
      if((this.denominator.signum() == 0) || (this.numerator.signum() == 0))
      {
         return BigRational.INVALID;
      }

      return BigRational.createRational(this.denominator, this.numerator);
   }

   /**
    * Indicates if rational can be see as integer.<br>
    * Can get the value with the {@link #getNumerator() numerator}
    * 
    * @return {@code true} if the rational is an integer
    */
   public boolean isInteger()
   {
      return this.denominator.compareTo(BigInteger.ONE) == 0;
   }

   /**
    * Indicates if rational is strictly negative
    * 
    * @return {@code true} if rational is strictly negative
    */
   public boolean isNegative()
   {
      return (this.numerator.signum() == -1) && (this.denominator.signum() != 0);
   }

   /**
    * Indicates if rational is strictly positive
    * 
    * @return {@code true} if ration is strictly positive
    */
   public boolean isPositive()
   {
      return (this.numerator.signum() == 1) && (this.denominator.signum() != 0);
   }

   /**
    * Multiply an other rational
    * 
    * @param rational
    *           Rational to multiply
    * @return Multiplication result
    */
   public BigRational multiply(final BigRational rational)
   {
      return BigRational.multiply(this, rational);
   }

   /**
    * Rational opposite
    * 
    * @return Opposite
    */
   public BigRational opposite()
   {
      if(this.denominator.signum() == 0)
      {
         return BigRational.INVALID;
      }

      if(this == BigRational.ZERO)
      {
         return BigRational.ZERO;
      }

      if(this == BigRational.ONE)
      {
         return BigRational.MINUS_ONE;
      }

      if(this == BigRational.MINUS_ONE)
      {
         return BigRational.ONE;
      }

      return BigRational.createRational(this.numerator.negate(), this.denominator);
   }

   /**
    * Put the rational at power of an integer.<br>
    * If the given integer is negative, {@link #INVALID} will be returned
    * 
    * @param power
    *           Power to raise
    * @return Rational result
    */
   public BigRational power(final int power)
   {
      return BigRational.power(this, power);
   }

   /**
    * Sign of rational It returns
    * <table>
    * <tr>
    * <th>-1</th>
    * <td>If rational is <0</td>
    * </tr>
    * <tr>
    * <th>0</th>
    * <td>If rational is 0 or invalid</td>
    * </tr>
    * <tr>
    * <th>1</th>
    * <td>If rational is >0</td>
    * </tr>
    * </table>
    * 
    * @return Rational sign
    */
   public int sign()
   {
      return this.numerator.signum();
   }

   /**
    * Subtract an other rational
    * 
    * @param rational
    *           Rational to subtract
    * @return Subtraction result
    */
   public BigRational subtract(final BigRational rational)
   {
      return BigRational.subtract(this, rational);
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
      if(this.denominator.signum() == 0)
      {
         return BigRational.INVALID_RATIONAL;
      }

      if(this.denominator.compareTo(BigInteger.ONE) == 0)
      {
         return String.valueOf(this.numerator);
      }

      return UtilText.concatenate(this.numerator, '/', this.denominator);
   }

   /**
    * Real value
    * 
    * @return Real value
    */
   public BigDecimal value()
   {
      if(this.denominator.signum() == 0)
      {
         return null;
      }

      if(this.denominator.compareTo(BigInteger.ONE) == 0)
      {
         return UtilMath.bigIntegerToBigDecimal(this.numerator);
      }

      return UtilMath.bigIntegerToBigDecimal(this.numerator).divide(UtilMath.bigIntegerToBigDecimal(this.denominator), BigRational.mathContext());
   }
}