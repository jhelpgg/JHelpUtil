package jhelp.util.math.fix;

import jhelp.util.debug.Debug;
import jhelp.util.debug.DebugLevel;

import org.junit.Assert;
import org.junit.Test;

public class RealTest
{

   @Test
   public void testAddition()
   {
      Real real1 = new Real(5.2);
      Real real2 = new Real(3.1);
      Real result = real1.addition(real2, null);
      Real expected = new Real(8.3);
      Assert.assertEquals(expected, result);

      real1 = new Real(5.2);
      real2 = new Real(-3.1);
      result = real1.addition(real2, null);
      expected = new Real(2.1);
      Assert.assertEquals(expected, result);

      real1 = new Real(-5.2);
      real2 = new Real(3.1);
      result = real1.addition(real2, null);
      expected = new Real(-2.1);
      Assert.assertEquals(expected, result);

      real1 = new Real(-5.2);
      real2 = new Real(-3.1);
      result = real1.addition(real2, null);
      expected = new Real(-8.3);
      Assert.assertEquals(expected, result);
   }

   @Test
   public void testDivision()
   {
      final Real real1 = new Real(5.2);
      final Real real2 = new Real(2);
      final Real result = real1.division(real2, null);
      final Real expected = new Real(2.6);
      Assert.assertEquals(expected, result);
   }

   @Test
   public void testMultiplication()
   {
      final Real real1 = new Real(5.2);
      final Real real2 = new Real(2);
      final Real result = real1.multiplication(real2, null);
      final Real expected = new Real(10.4);
      Assert.assertEquals(expected, result);
   }

   @Test
   public void testReal()
   {
      final Real real = new Real();
      Debug.println(DebugLevel.VERBOSE, "real=", real);
   }

   @Test
   public void testRealDouble()
   {
      Real real = new Real(Math.PI);
      Debug.println(DebugLevel.VERBOSE, "real=", real);

      real = new Real(0.01);
      Debug.println(DebugLevel.VERBOSE, "real=", real);

      real = new Real(5);
      Debug.println(DebugLevel.VERBOSE, "real=", real);

      real = new Real(-42.24);
      Debug.println(DebugLevel.VERBOSE, "real=", real);

      real = new Real(1.0 / 3.0);
      Debug.println(DebugLevel.VERBOSE, "real=", real);

      real.multiplication(new Real(3));
      Debug.println(DebugLevel.VERBOSE, "real=", real);

      real.multiplication(new Real(2));
      Debug.println(DebugLevel.VERBOSE, "real=", real);

      real.division(new Real(3));
      Debug.println(DebugLevel.VERBOSE, "real=", real);

      real.division(new Real(2));
      Debug.println(DebugLevel.VERBOSE, "real=", real);
   }

   @Test
   public void testSubtraction()
   {
      Real real1 = new Real(5.2);
      Real real2 = new Real(3.1);
      Real result = real1.subtraction(real2, null);
      Real expected = new Real(2.1);
      Assert.assertEquals(expected, result);

      real1 = new Real(5.2);
      real2 = new Real(-3.1);
      result = real1.subtraction(real2, null);
      expected = new Real(8.3);
      Assert.assertEquals(expected, result);

      real1 = new Real(-5.2);
      real2 = new Real(3.1);
      result = real1.subtraction(real2, null);
      expected = new Real(-8.3);
      Assert.assertEquals(expected, result);

      real1 = new Real(-5.2);
      real2 = new Real(-3.1);
      result = real1.subtraction(real2, null);
      expected = new Real(-2.1);
      Assert.assertEquals(expected, result);
   }
}