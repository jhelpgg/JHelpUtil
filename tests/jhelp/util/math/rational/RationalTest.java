package jhelp.util.math.rational;

import org.junit.Assert;
import org.junit.Test;

import jhelp.util.debug.Debug;
import jhelp.util.debug.DebugLevel;

/**
 * Rational numbers test
 *
 * @author JHelp <br>
 */
public class RationalTest
{
   /**
    * Comparison
    */
   @Test
   public void testComparison()
   {
      Assert.assertTrue(Rational.createRational(1, 3).compareTo(Rational.createRational(1f / 3f)) == 0);
      Assert.assertTrue(Rational.createRational(35, 9).compareTo(Rational.createRational(35f / 9f)) == 0);
      Assert.assertTrue(Rational.createRational(100, 300).compareTo(Rational.createRational(1, 3)) == 0);
      Assert.assertTrue(Rational.createRational(1, 3).compareTo(Rational.createRational(1, 2)) < 0);
      Assert.assertTrue(Rational.createRational(2, 3).compareTo(Rational.createRational(1, 2)) > 0);
      Assert.assertTrue(Rational.createRational(-2, 3).compareTo(Rational.createRational(1, 2)) < 0);
   }

   /**
    * Create from float
    */
   @Test
   public void testCreateRationalFloat()
   {
      Assert.assertEquals(Rational.createRational(1, 3), Rational.createRational(1f / 3f));
      Assert.assertEquals(Rational.createRational(2, 5), Rational.createRational(2f / 5f));
      Assert.assertEquals(Rational.createRational(5, 9), Rational.createRational(5f / 9f));
      Assert.assertEquals(Rational.createRational(35, 9), Rational.createRational(35f / 9f));
      // Debug.println(DebugLevel.VERBOSE, "PI=", Rational.createRational((float) Math.PI));
      // Debug.println(DebugLevel.VERBOSE, "e=", Rational.createRational((float) Math.E));
      Debug.println(DebugLevel.VERBOSE, Rational.proportion(58, '+', 42, '-'));
   }
}