package jhelp.util.math.oldSystem;

/**
 * Tribal numbers.<br>
 * The String representation can be correctly seen on using {@link MayaNumber#FONT_NUMBERS}.<br>
 * Tribal numbers don't know negative numbers.<br>
 * It can be "0" (Represents by a big dot), or their have one | per value, but for 5, a group
 * representation is used instead of
 * 5 |. Same thing happens for 25 a 25 representation is used instead of 5 "5 symbol". To follow
 * this logic, there also a
 * representation for 125 and 625.<br>
 * After is bit more complicated, the symbols before the 625 symbol (*) are multiplier of 625
 * (The much time 625) By example
 * 1250 is represented : ||* (Means 2 times 625), but numbers after the 625 symbol are just add,
 * by example 1252 is ||*||
 * (2*625+2). For a better understanding try SampleTribal (Type numbers and then enter to
 * see tribal representation)
 *
 * @author JHelp
 */
public class TribalNumber
{
    /**
     * 0 digit (Use only for represents 0)
     */
    public static final char DIGIT_0   = (char) 148;
    /**
     * 1 digit
     */
    public static final char DIGIT_1   = (char) 149;
    /**
     * 2 digit
     */
    public static final char DIGIT_2   = (char) 150;
    /**
     * 3 digit
     */
    public static final char DIGIT_3   = (char) 151;
    /**
     * 4 digit
     */
    public static final char DIGIT_4   = (char) 152;
    /**
     * 5 digit
     */
    public static final char DIGIT_5   = (char) 153;
    /**
     * Symbol for 125
     */
    public static final char GROUP_125 = (char) 155;
    /**
     * Symbol for 25
     */
    public static final char GROUP_25  = (char) 154;
    /**
     * Symbol for 625
     */
    public static final char GROUP_625 = (char) 156;
    /**
     * Value in decimal
     */
    private final long   number;
    /**
     * String representation
     */
    private final String string;

    /**
     * Create a new instance of TribalNumber
     *
     * @param number Number to convert
     * @throws IllegalArgumentException If number is negative
     */
    public TribalNumber(long number)
    {
        if (number < 0)
        {
            throw new IllegalArgumentException(
                    "Tribal didn't know negative numbers like " + number);
        }

        this.number = number;

        if (number == 0)
        {
            this.string = String.valueOf(TribalNumber.DIGIT_0);
            return;
        }

        final StringBuilder stringBuilder = new StringBuilder();
        final long          rest          = number % 625;

        while (number >= 625)
        {
            stringBuilder.insert(0, TribalNumber.GROUP_625);
            long time = number / 625;
            number = time;
            time %= 625;

            if ((time > 1) || ((time > 0) && (number > 625)))
            {
                stringBuilder.insert(0, this.computeRest(time));
            }
        }

        stringBuilder.append(this.computeRest(rest));
        this.string = stringBuilder.toString();
    }

    /**
     * Compute String for numbers in [0, 624]
     *
     * @param number Number to convert
     * @return String representation
     */
    private String computeRest(long number)
    {
        final StringBuilder restString = new StringBuilder();
        int                 time       = (int) (number / 125);

        for (int i = 0; i < time; i++)
        {
            restString.append(TribalNumber.GROUP_125);
        }

        number = number % 125;
        time = (int) (number / 25);

        for (int i = 0; i < time; i++)
        {
            restString.append(TribalNumber.GROUP_25);
        }

        number = number % 25;
        time = (int) (number / 5);

        for (int i = 0; i < time; i++)
        {
            restString.append(TribalNumber.DIGIT_5);
        }

        number = number % 5;

        if (number > 0)
        {
            restString.append((char) (TribalNumber.DIGIT_0 + number));
        }

        return restString.toString();
    }

    /**
     * Parse a String of tribal characters
     *
     * @param string String to parse
     * @return Tribal number
     */
    public static TribalNumber parse(final String string)
    {
        return TribalNumber.parse(string.toCharArray());
    }

    /**
     * Parse a list a tribal characters.
     *
     * @param characters Characters to create tribal number
     * @return Created tribal number
     * @throws IllegalArgumentException If one of character is not a tribal number character
     */
    public static TribalNumber parse(final char... characters)
    {
        long number = 0;

        for (final char character : characters)
        {
            switch (character)
            {
                case DIGIT_0:
                case DIGIT_1:
                case DIGIT_2:
                case DIGIT_3:
                case DIGIT_4:
                case DIGIT_5:
                    number += character - TribalNumber.DIGIT_0;
                    break;
                case GROUP_25:
                    number += 25;
                    break;
                case GROUP_125:
                    number += 125;
                    break;
                case GROUP_625:
                    if (number == 0)
                    {
                        number = 625;
                    }
                    else
                    {
                        number *= 625;
                    }
                    break;
                default:
                    throw new IllegalArgumentException("Invalid characters");
            }
        }

        return new TribalNumber(number);
    }

    /**
     * Number in decimal
     *
     * @return Number in decimal
     */
    public long getNumber()
    {
        return this.number;
    }

    /**
     * String representation.<br>
     * We recommend to use {@link MayaNumber#FONT_NUMBERS} to have a good representation <br>
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
        return this.string;
    }
}