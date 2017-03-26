package jhelp.util.math.random;

import org.junit.Assert;
import org.junit.Test;

import jhelp.util.debug.Debug;
import jhelp.util.debug.DebugLevel;

/**
 * Random tests
 *
 * @author JHelp <br>
 */
public class JHelpRandomTest
{
    // @Test
    // public void testProbability()
    // {
    // Debug.println(DebugLevel.INFORMATION, "START");
    // final int[] diceCount = new int[6];
    // final int total = 10000000;
    //
    // for(int i = 0; i < total; i++)
    // {
    // diceCount[JHelpRandom.random(6)]++;
    // }
    //
    // Rational rational;
    // for(int i = 0; i < 6; i++)
    // {
    // rational = Rational.createRational(diceCount[i], total);
    // Debug.println(DebugLevel.INFORMATION, i + 1, ':', rational, " ~=~ ", rational.value());
    // }
    // }

    /**
     * Test of {@link JHelpRandom#random(int, int)}
     */
    @Test
    public void testRandom()
    {
        int rand;

        for (int i = 0; i < 128; i++)
        {
            rand = JHelpRandom.random(5, 8);

            Assert.assertTrue(rand + " not in [5, 8]", (rand >= 5) && (rand <= 8));
        }
    }

    /**
     * Test of {@link JHelpRandom#random(int)}
     */
    @Test
    public void testRandom2()
    {
        int rand;

        for (int i = 0; i < 128; i++)
        {
            rand = JHelpRandom.random(8);

            Assert.assertTrue(rand + " not in [0, 8[", (rand >= 0) && (rand < 8));
        }
    }

    /**
     * Choice tests
     */
    @Test
    public void tests()
    {
        final JHelpRandom<Integer> random = new JHelpRandom<Integer>();

        try
        {
            random.addChoice(0, 0);

            Assert.fail("Should have exception for <=0 number");
        }
        catch (final Exception exception)
        {
            // That's what we want
        }

        Assert.assertNull("Nothing add so must be null", random.choose());

        random.addChoice(1, 1);
        random.addChoice(2, 2);
        random.addChoice(3, 3);
        random.addChoice(4, 4);
        random.addChoice(5, 5);

        int value;
        for (int i = 0; i < 128; i++)
        {
            //noinspection ConstantConditions
            value = random.choose();

            Assert.assertTrue(value + " not in [1, 5]", (value >= 1) && (value <= 5));
            Debug.println(DebugLevel.VERBOSE, "Choose=", value);
        }
    }
}