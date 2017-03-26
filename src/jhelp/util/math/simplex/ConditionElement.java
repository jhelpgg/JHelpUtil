/**
 * <h1>License :</h1> <br>
 * The following code is deliver as is. I take care that code compile and work, but I am not responsible about any
 * damage it may
 * cause.<br>
 * You can use, modify, the code as your need for any usage. But you can't do any action that avoid me or other person use,
 * modify this code. The code is free for usage and modification, you can't change that fact.<br>
 * <br>
 *
 * @author JHelp
 */
package jhelp.util.math.simplex;

/**
 * Pair of coefficient, variable
 *
 * @author JHelp <br>
 */
public class ConditionElement
        implements Comparable<ConditionElement>
{
    /**
     * Coefficient
     */
    private final int  coefficient;
    /**
     * Variable
     */
    private final char symbol;

    /**
     * Create a new instance of ConditionElement
     *
     * @param coefficient Coefficient
     * @param symbol      Variable name
     */
    public ConditionElement(final int coefficient, final char symbol)
    {
        this.coefficient = coefficient;
        this.symbol = symbol;
    }

    /**
     * Compare with given pair of coefficient, variable to know witch one is before the other <br>
     * <br>
     * <b>Parent documentation:</b><br>
     * {@inheritDoc}
     *
     * @param conditionElement Pair to compare with
     * @return Comparison result
     * @see java.lang.Comparable#compareTo(java.lang.Object)
     */
    @Override
    public int compareTo(final ConditionElement conditionElement)
    {
        return this.symbol - conditionElement.symbol;
    }

    /**
     * Coefficient
     *
     * @return Coefficient
     */
    public int getCoefficient()
    {
        return this.coefficient;
    }

    /**
     * Variable name
     *
     * @return Variable name
     */
    public char getSymbol()
    {
        return this.symbol;
    }
}