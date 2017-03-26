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

import java.util.ArrayList;
import java.util.List;

import jhelp.util.list.SortedArray;

/**
 * Describe a {@link EquationNP} constraint.<br>
 * It is an inequality on the form :
 * <p>
 * <pre>
 * c<sub>1</sub>x<sub>1</sub> + c<sub>2</sub>x<sub>2</sub> + ... + c<sub>n</sub>x<sub>n</sub>  &lt;inequality&gt; a
 * </pre>
 * <p>
 * Where :
 * <ul>
 * <li><b>c<sub>i</sub></b> and <b>a</b> are constant integer</li>
 * <li><b>x<sub>i</sub></b> are distinct alphabetic letter</b></li>
 * <li><b>&lt;inequality&gt;</b> is : <b>≤</b>, <b>≥</b> or <b>=</b></li>
 * </ul>
 *
 * @author JHelp <br>
 */
public class Condition
{
    /**
     * Symbol ≥. Tip : In my key board : AltGr+Shift+>
     */
    public static final char GREATER_OR_EQUAL_SYMBOL = '≥';
    /**
     * Symbol ≤. Tip : In my key board : AltGr+<
     */
    public static final char LOWER_OR_EQUAL_SYMBOL   = '≤';
    /**
     * Type of condition : ≤, ≥, =
     */
    private final ConditionType                 conditionType;
    /**
     * Pairs of coefficient, variable
     */
    private final SortedArray<ConditionElement> elements;
    /**
     * Limit value
     */
    private final int                           limit;
    /**
     * Create a new instance of Condition
     *
     * @param elements      Pairs of coefficient, variable
     * @param conditionType Type of condition : ≤, ≥, =
     * @param limit         Limit value
     */
    public Condition(final ConditionElement[] elements, final ConditionType conditionType, final int limit)
    {
        if (conditionType == null)
        {
            throw new NullPointerException("conditionType MUST NOT be null");
        }

        this.conditionType = conditionType;
        this.elements = new SortedArray<ConditionElement>(ConditionElement.class, true);
        this.limit = limit;

        for (final ConditionElement conditionElement : elements)
        {
            if ((conditionElement.getCoefficient() != 0) && (!this.elements.add(conditionElement)))
            {
                throw new IllegalArgumentException(
                        "Two elements refer to same variable : " + conditionElement.getSymbol());
            }
        }
    }

    /**
     * Parse an inequality String like :
     * <p>
     * <pre>
     * 3x + 8r - 4A ≤ 85
     * -8q -6h +z-y≥ -123
     * p+14o -7f = 12
     * ...
     * </pre>
     * <p>
     * To a condition constraints
     *
     * @param condition String to parse
     * @return Parsed constraints
     */
    public static Condition parse(final String condition)
    {
        final char[]                 characters             = condition.toCharArray();
        final int                    length                 = characters.length;
        final List<ConditionElement> elements               = new ArrayList<ConditionElement>();
        ConditionType                conditionType          = null;
        boolean                      beforeInequalitySymbol = true;
        boolean                      negative               = false;
        int                          coefficient            = 0;
        char                         character;

        for (int index = 0; index < length; index++)
        {
            character = characters[index];

            //Ignore white spaces
            if (character > 32)
            {
                if (character == '-')
                {
                    negative = true;
                    coefficient = 0;
                }
                else if (character == '+')
                {
                    negative = false;
                    coefficient = 0;
                }
                else if ((character >= '0') && (character <= '9'))
                {
                    coefficient = (10 * coefficient) + (character - '0');
                }
                else if (((character >= 'a') && (character <= 'z')) || ((character >= 'A') && (character <= 'Z')))
                {
                    if (!beforeInequalitySymbol)
                    {
                        throw new IllegalArgumentException("Find character after inequality in : " + condition);
                    }

                    if (coefficient == 0)
                    {
                        coefficient = 1;
                    }

                    if (negative)
                    {
                        coefficient *= -1;
                    }

                    elements.add(new ConditionElement(coefficient, character));
                    negative = false;
                    coefficient = 0;
                }
                else if (character == '=')
                {
                    if (!beforeInequalitySymbol)
                    {
                        throw new IllegalArgumentException("Find two inequality symbols in : " + condition);
                    }

                    beforeInequalitySymbol = false;
                    conditionType = ConditionType.EQUAL;
                    negative = false;
                    coefficient = 0;
                }
                else if (character == Condition.LOWER_OR_EQUAL_SYMBOL)
                {
                    if (!beforeInequalitySymbol)
                    {
                        throw new IllegalArgumentException("Find two inequality symbols in : " + condition);
                    }

                    beforeInequalitySymbol = false;
                    conditionType = ConditionType.LOWER_OR_EQUAL;
                    negative = false;
                    coefficient = 0;
                }
                else if (character == Condition.GREATER_OR_EQUAL_SYMBOL)
                {
                    if (!beforeInequalitySymbol)
                    {
                        throw new IllegalArgumentException("Find two inequality symbols in : " + condition);
                    }

                    beforeInequalitySymbol = false;
                    conditionType = ConditionType.GREATER_OR_EQUAL;
                    negative = false;
                    coefficient = 0;
                }
                else
                {
                    throw new IllegalArgumentException(
                            "Invalid character '" + character + "' at index " + index + " in " + condition);
                }
            }
        }

        if (beforeInequalitySymbol)
        {
            throw new IllegalArgumentException("No inequality in " + condition);
        }

        if (negative)
        {
            coefficient *= -1;
        }

        return new Condition(elements.toArray(new ConditionElement[elements.size()]), conditionType, coefficient);
    }

    /**
     * Collect all variables names used by condition
     *
     * @param characters Array where store variables names
     */
    public void collectSymbols(final SortedArray<Character> characters)
    {
        for (final ConditionElement conditionElement : this.elements)
        {
            characters.add(conditionElement.getSymbol());
        }
    }

    /**
     * Condition type : ≤, ≥, =
     *
     * @return Condition type : ≤, ≥, =
     */
    public ConditionType getConditionType()
    {
        return this.conditionType;
    }

    /**
     * Obtain an element
     *
     * @param index Element index
     * @return The element
     */
    public ConditionElement getElement(final int index)
    {
        return this.elements.getElement(index);
    }

    /**
     * Inequality limit
     *
     * @return Inequality limit
     */
    public int getLimit()
    {
        return this.limit;
    }

    /**
     * Number of elements
     *
     * @return Number of elements
     */
    public int numberOfElements()
    {
        return this.elements.getSize();
    }

    /**
     * Coefficient of variable.<br>
     * Returns 0 for unknown variable
     *
     * @param symbol Variable name
     * @return Variable coefficient
     */
    public int obtainCoefficient(final char symbol)
    {
        final ConditionElement element = this.elements.obtainElement(new ConditionElement(0, symbol));

        if (element == null)
        {
            return 0;
        }

        return element.getCoefficient();
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
        this.appendInside(stringBuilder);
        return stringBuilder.toString();
    }

    /**
     * Append complete string description inside a string builder
     *
     * @param stringBuilder String builder where append
     */
    void appendInside(final StringBuilder stringBuilder)
    {
        this.appendLeftPartInside(stringBuilder);
        stringBuilder.append(' ');
        stringBuilder.append(this.conditionType.getSymbol());
        stringBuilder.append(' ');
        stringBuilder.append(this.limit);
    }

    /**
     * Append only left part description (Without inequality and limit) in string builder
     *
     * @param stringBuilder String builder where append
     */
    void appendLeftPartInside(final StringBuilder stringBuilder)
    {
        boolean first = true;
        int     coefficient;

        for (final ConditionElement conditionElement : this.elements)
        {
            coefficient = conditionElement.getCoefficient();

            if (!first)
            {
                stringBuilder.append(' ');
            }

            if ((!first) || (coefficient < 0))
            {
                if (coefficient < 0)
                {
                    stringBuilder.append('-');
                }
                else
                {
                    stringBuilder.append('+');
                }
            }

            if (!first)
            {
                stringBuilder.append(' ');
            }

            if ((coefficient > 1) || (coefficient < -1))
            {
                stringBuilder.append(Math.abs(coefficient));
            }

            stringBuilder.append(conditionElement.getSymbol());
            first = false;
        }
    }
}