package jhelp.util.math.rational;

import jhelp.util.HashCode;
import jhelp.util.math.UtilMath;
import jhelp.util.text.UtilText;

/**
 * Represents a rational number
 *
 * @author JHelp
 */
public class Rational
        implements Comparable<Rational>
{
    /**
     * Invalid rational. The rational have non meaning (like divide by 0)
     */
    public static final Rational INVALID          = new Rational(0, 0);
    /**
     * Invalid rational key string. Returned from {@link #toString()} if the rational is invalid
     */
    public static final String   INVALID_RATIONAL = "INVALID_RATIONAL";
    /**
     * -1 rational
     */
    public static final Rational MINUS_ONE        = new Rational(-1, 1);
    /**
     * 1 rational
     */
    public static final Rational ONE              = new Rational(1, 1);
    /**
     * 0 rational
     */
    public static final Rational ZERO             = new Rational(0, 1);
    /**
     * Epsilon precision
     */
    private static final float EPSILON = Float.MIN_NORMAL;
    /**
     * Denominator
     */
    private final int denominator;
    /**
     * Numerator
     */
    private final int numerator;

    /**
     * Create a new instance of Rational
     *
     * @param numerator   Numerator
     * @param denominator Denominator
     */
    private Rational(final int numerator, final int denominator)
    {
        this.numerator = numerator;
        this.denominator = denominator;
    }

    /**
     * Compute the rational just in the middle of 2 other rational
     *
     * @param rational1 First rational
     * @param rational2 Second rational
     * @return Middle computed
     */
    public static Rational middle(final Rational rational1, final Rational rational2)
    {
        if ((rational1 == Rational.INVALID) || (rational2 == Rational.INVALID))
        {
            return Rational.INVALID;
        }

        return Rational.createRational(
                (rational1.numerator * rational2.denominator) + (rational2.numerator * rational1.denominator), //
                rational1.denominator * rational2.denominator * 2);
    }

    /**
     * Create a rational
     *
     * @param numerator   Numerator
     * @param denominator Denominator
     * @return Created rational
     */
    public static Rational createRational(int numerator, int denominator)
    {
        if (denominator == 0)
        {
            return Rational.INVALID;
        }

        if (numerator == 0)
        {
            return Rational.ZERO;
        }

        if (numerator == denominator)
        {
            return Rational.ONE;
        }

        if (numerator == -denominator)
        {
            return Rational.MINUS_ONE;
        }

        if (denominator < 0)
        {
            numerator = -numerator;
            denominator = -denominator;
        }

        final int gcd = UtilMath.greaterCommonDivisor(numerator, denominator);

        return new Rational(numerator / gcd, denominator / gcd);
    }

    /**
     * Parse a String to be a rational.<br>
     * String must be {@link #INVALID_RATIONAL} or &lt;integer&gt; or &lt;integer&gt; &lt;space&gt;* / &lt;space&gt;*
     * &lt;integer&gt;<br>
     * Where &lt;integer&gt; := [0-9]+ AND &lt;space&gt; := {SPACE, \t, \n, \r, \f}.<br>
     * If the string is not well formatted {@link IllegalArgumentException} will be throw
     *
     * @param string String to parse
     * @return Parsed rational
     * @throws NullPointerException     If string is {@code null}
     * @throws IllegalArgumentException If string can't be parsed as a rational
     */
    public static Rational parse(String string)
    {
        string = string.trim();

        if (Rational.INVALID_RATIONAL.equals(string))
        {
            return Rational.INVALID;
        }

        final int index = string.indexOf('/');

        if (index < 0)
        {
            try
            {
                return Rational.createRational(Integer.parseInt(string));
            }
            catch (final Exception exception)
            {
                throw new IllegalArgumentException(string + " can't be parsed as a rational", exception);
            }
        }

        try
        {
            return Rational.createRational(Integer.parseInt(string.substring(0, index)
                                                                  .trim(), Integer.parseInt(string.substring(index + 1)
                                                                                                  .trim())));
        }
        catch (final Exception exception)
        {
            throw new IllegalArgumentException(string + " can't be parsed as a rational", exception);
        }
    }

    /**
     * Create rational from integer
     *
     * @param integer Integer source
     * @return Result rational
     */
    public static Rational createRational(final int integer)
    {
        return Rational.createRational(integer, 1);
    }

    /**
     * Compute a string representation of a proportion
     *
     * @param numberPositive Number of "positive" value
     * @param positiveSymbol Character used to represents "positive" values
     * @param numberNegative Number of "negative" value
     * @param negativeSymbol Character used to represents "negative" values
     * @return Computed proportion
     */
    public static String proportion(final int numberPositive, final char positiveSymbol, final int numberNegative,
                                    final char negativeSymbol)
    {
        if ((numberPositive < 0) || (numberNegative < 0))
        {
            throw new IllegalArgumentException(
                    UtilText.concatenate("Number of positive and negative can't be negative. Here we have numberPositive=",
                                         numberPositive, " and numberNegative=", numberNegative));
        }

        final Rational rational = Rational.createRational(numberPositive, numberPositive + numberNegative);

        if (rational == Rational.INVALID)
        {
            return "?";
        }

        final int total    = rational.getDenominator();
        final int positive = rational.getNumerator();
        final int negative = total - positive;

        final char[] proportion = new char[total];

        int index = 0;

        for (int i = 0; i < positive; i++)
        {
            proportion[index++] = positiveSymbol;
        }

        for (int i = 0; i < negative; i++)
        {
            proportion[index++] = negativeSymbol;
        }

        return new String(proportion);
    }

    /**
     * Denominator
     *
     * @return Denominator
     */
    public int getDenominator()
    {
        return this.denominator;
    }

    /**
     * Numerator
     *
     * @return Numerator
     */
    public int getNumerator()
    {
        return this.numerator;
    }

    /**
     * Add an other rational
     *
     * @param rational Rational to add
     * @return Addition result
     */
    public Rational addition(final Rational rational)
    {
        return Rational.addition(this, rational);
    }

    /**
     * Add 2 rational
     *
     * @param rational1 First rational
     * @param rational2 Second rational
     * @return Rational result
     */
    public static Rational addition(final Rational rational1, final Rational rational2)
    {
        if ((rational1 == Rational.INVALID) || (rational2 == Rational.INVALID))
        {
            return Rational.INVALID;
        }

        if (rational1 == Rational.ZERO)
        {
            return rational2;
        }

        if (rational2 == Rational.ZERO)
        {
            return rational1;
        }

        return Rational.createRational(
                (rational1.numerator * rational2.denominator) + (rational2.numerator * rational1.denominator),
                rational1.denominator * rational2.denominator);
    }

    /**
     * Compare&nbsp;with&nbsp;an&nbsp;other&nbsp;rational.&nbsp;<br>
     * It&nbsp;returns&nbsp;:<br>
     * <table border=1>
     * <tr>
     * <th>&lt;&nbsp;0<br>
     * </th>
     * <td>If&nbsp;<b>&nbsp;this&nbsp;</b>&nbsp;rational&nbsp;is&nbsp;<b>&nbsp;&lt;&nbsp;</b>&nbsp;the&nbsp;given&nbsp;
     * rational
     * <br>
     * </td>
     * </tr>
     * <tr>
     * <th>=&nbsp;0<br>
     * </th>
     * <td>If&nbsp;<b>&nbsp;this&nbsp;</b>&nbsp;rational&nbsp;is&nbsp;<b>&nbsp;equals&nbsp;</b>&nbsp;the&nbsp;given&nbsp;
     * rational
     * <br>
     * </td>
     * </tr>
     * <tr>
     * <th>&gt;&nbsp;0<br>
     * </th>
     * <td>If&nbsp;<b>&nbsp;this&nbsp;</b>&nbsp;rational&nbsp;is&nbsp;<b>&nbsp;&gt;&nbsp;</b>&nbsp;the&nbsp;given&nbsp;
     * rational
     * <br>
     * </td>
     * </tr>
     * </table>
     * <br>
     * <br>
     * <b>Parent documentation:</b><br>
     * {@inheritDoc}
     *
     * @param rational Rational to compare with
     * @return Comparison result
     * @see java.lang.Comparable#compareTo(java.lang.Object)
     */
    @Override
    public int compareTo(final Rational rational)
    {
        if (rational == null)
        {
            throw new NullPointerException("rational MUST NOT be null");
        }

        if (this.equals(rational))
        {
            return 0;
        }

        if (this.denominator == rational.denominator)
        {
            return this.numerator - rational.numerator;
        }

        if (this.denominator == 0)
        {
            return 1;
        }

        if (rational.denominator == 0)
        {
            return -1;
        }

        final Rational difference = this.subtract(rational);

        return difference.numerator;
    }

    /**
     * Subtract an other rational
     *
     * @param rational Rational to subtract
     * @return Subtraction result
     */
    public Rational subtract(final Rational rational)
    {
        return Rational.subtract(this, rational);
    }

    /**
     * Subtract 2 rational
     *
     * @param rational1 First rational
     * @param rational2 Second rational
     * @return Rational result
     */
    public static Rational subtract(final Rational rational1, final Rational rational2)
    {
        if ((rational1 == Rational.INVALID) || (rational2 == Rational.INVALID))
        {
            return Rational.INVALID;
        }

        if (rational1 == Rational.ZERO)
        {
            return rational2.opposite();
        }

        if (rational2 == Rational.ZERO)
        {
            return rational1;
        }

        if (rational1.equals(rational2))
        {
            return Rational.ZERO;
        }

        return Rational.createRational(
                (rational1.numerator * rational2.denominator) - (rational2.numerator * rational1.denominator),
                rational1.denominator * rational2.denominator);
    }

    /**
     * Rational opposite
     *
     * @return Opposite
     */
    public Rational opposite()
    {
        if (this.denominator == 0)
        {
            return Rational.INVALID;
        }

        if (this == Rational.ZERO)
        {
            return Rational.ZERO;
        }

        if (this == Rational.ONE)
        {
            return Rational.MINUS_ONE;
        }

        if (this == Rational.MINUS_ONE)
        {
            return Rational.ONE;
        }

        return Rational.createRational(-this.numerator, this.denominator);
    }

    /**
     * Divide an other rational
     *
     * @param rational Rational to divide
     * @return Division result
     */
    public Rational divide(final Rational rational)
    {
        return Rational.divide(this, rational);
    }

    /**
     * Divide 2 rational
     *
     * @param rational1 First rational
     * @param rational2 Second rational
     * @return Rational result
     */
    public static Rational divide(final Rational rational1, final Rational rational2)
    {
        if ((rational1 == Rational.INVALID) || (rational2 == Rational.INVALID) || (rational2 == Rational.ZERO))
        {
            return Rational.INVALID;
        }

        if (rational1 == Rational.ZERO)
        {
            return Rational.ZERO;
        }

        if (rational2 == Rational.ONE)
        {
            return rational1;
        }

        if (rational1.equals(rational2))
        {
            return Rational.ONE;
        }

        return Rational.createRational(rational1.numerator * rational2.denominator,
                                       rational1.denominator * rational2.numerator);
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
        return HashCode.computeHashCode(this.denominator, this.numerator);
    }

    /**
     * Indicates if an object is equals to this rational <br>
     * <br>
     * <b>Parent documentation:</b><br>
     * {@inheritDoc}
     *
     * @param obj Object to test
     * @return {@code true} in equality
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(final Object obj)
    {
        if (this == obj)
        {
            return true;
        }
        if (obj == null)
        {
            return false;
        }
        if (this.getClass() != obj.getClass())
        {
            return false;
        }
        final Rational other = (Rational) obj;
        if (this.denominator != other.denominator)
        {
            return false;
        }
        return this.numerator == other.numerator;
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
        if (this.denominator == 0)
        {
            return Rational.INVALID_RATIONAL;
        }

        if (this.denominator == 1)
        {
            return String.valueOf(this.numerator);
        }

        return UtilText.concatenate(this.numerator, '/', this.denominator);
    }

    /**
     * Inverse the rational
     *
     * @return Inverted
     */
    public Rational inverse()
    {
        if ((this.denominator == 0) || (this.numerator == 0))
        {
            return Rational.INVALID;
        }

        return Rational.createRational(this.denominator, this.numerator);
    }

    /**
     * Indicates if rational can be see as integer.<br>
     * Can get the value with the {@link #getNumerator() numerator}
     *
     * @return {@code true} if the rational is an integer
     */
    public boolean isInteger()
    {
        return this.denominator == 1;
    }

    /**
     * Indicates if rational is strictly positive
     *
     * @return {@code true} if ration is strictly positive
     */
    public boolean isPositive()
    {
        return (this.numerator > 0) && (this.denominator != 0);
    }

    /**
     * Multiply an other rational
     *
     * @param rational Rational to multiply
     * @return Multiplication result
     */
    public Rational multiply(final Rational rational)
    {
        return Rational.multiply(this, rational);
    }

    /**
     * Multiply 2 rational
     *
     * @param rational1 First rational
     * @param rational2 Second rational
     * @return Rational result
     */
    public static Rational multiply(final Rational rational1, final Rational rational2)
    {
        if ((rational1 == Rational.INVALID) || (rational2 == Rational.INVALID))
        {
            return Rational.INVALID;
        }

        if ((rational1 == Rational.ZERO) || (rational2 == Rational.ZERO))
        {
            return Rational.ZERO;
        }

        if (rational1 == Rational.ONE)
        {
            return rational2;
        }

        if (rational2 == Rational.ONE)
        {
            return rational1;
        }

        if (rational1 == Rational.MINUS_ONE)
        {
            return rational2.opposite();
        }

        if (rational2 == Rational.MINUS_ONE)
        {
            return rational1.opposite();
        }

        return Rational.createRational(rational1.numerator * rational2.numerator,
                                       rational1.denominator * rational2.denominator);
    }

    /**
     * Compute rational raise to power
     *
     * @param power Power to use (Warning not always accurate if power < 0)
     * @return The result
     */
    public Rational power(final int power)
    {
        return Rational.power(this, power);
    }

    /**
     * Compute rational raise to power
     *
     * @param rational Rational to raise to power
     * @param power    Power to use (Warning not always accurate if power < 0)
     * @return The result
     */
    public static Rational power(final Rational rational, final int power)
    {
        if (rational == Rational.INVALID)
        {
            return Rational.INVALID;
        }

        if (power == 0)
        {
            return Rational.ONE;
        }

        if (power == 1)
        {
            return rational;
        }

        if (rational == Rational.ZERO)
        {
            return Rational.ZERO;
        }

        if (rational == Rational.ONE)
        {
            return Rational.ONE;
        }

        if (power > 0)
        {
            if (rational == Rational.MINUS_ONE)
            {
                if ((power % 2) == 0)
                {
                    return Rational.ONE;
                }
                else
                {
                    return Rational.MINUS_ONE;
                }
            }

            int num = rational.numerator;
            int den = rational.denominator;

            for (int i = power - 1; i >= 0; i--)
            {
                num *= rational.numerator;
                den *= rational.denominator;
            }

            return Rational.createRational(num, den);
        }

        if (rational.isNegative())
        {
            return Rational.INVALID;
        }

        final float value = (float) Math.pow((double) rational.numerator / (double) rational.denominator, power);
        return Rational.createRational(value);
    }

    /**
     * Create rational from real.<br>
     * It founds the nearest rational at epsilon precision
     *
     * @param f Real to found nearest rational
     * @return Rational created
     */
    public static Rational createRational(float f)
    {
        if ((Float.isNaN(f)) || (Float.isInfinite(f)))
        {
            return Rational.INVALID;
        }

        final int sign = UtilMath.sign(f);

        if (sign == 0)
        {
            return Rational.ZERO;
        }

        if (sign < 0)
        {
            f = -f;
        }

        int   numerator   = (int) f;
        int   denominator = 1;
        float value       = (float) numerator / (float) denominator;

        while (!Rational.equals(value, f))
        {
            if (value < f)
            {
                numerator++;
            }
            else
            {
                denominator++;
            }

            value = (float) numerator / (float) denominator;
        }

        return Rational.createRational(numerator * sign, denominator);
    }

    /**
     * Indicates if 2 reals are equals in epsilon precision
     *
     * @param f1 First real
     * @param f2 Second real
     * @return {@code true} if they are equals at epsilon precision
     */
    private static boolean equals(final float f1, final float f2)
    {
        return Math.abs(f1 - f2) <= Rational.EPSILON;
    }

    /**
     * Indicates if rational is strictly negative
     *
     * @return {@code true} if rational is strictly negative
     */
    public boolean isNegative()
    {
        return (this.numerator < 0) && (this.denominator != 0);
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
        return UtilMath.sign(this.numerator);
    }

    /**
     * Real value
     *
     * @return Real value
     */
    public float value()
    {
        if (this.denominator == 0)
        {
            return Float.NaN;
        }

        if (this.denominator == 1)
        {
            return this.numerator;
        }

        return (float) this.numerator / (float) this.denominator;
    }
}