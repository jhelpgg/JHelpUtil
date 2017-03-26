package jhelp.util.math;

import java.awt.geom.Point2D;

import org.junit.Assert;
import org.junit.Test;

/**
 * {@link UtilMath} unit tests
 *
 * @author JHelp <br>
 */
public class UtilMathTest
{
   /**
    * Test {@link UtilMath#Bernouilli(int, int, double)}
    */
   @Test
   public void testBernouilli()
   {
      Assert.assertEquals(0.3125, UtilMath.Bernouilli(3, 5, 0.5), UtilMath.EPSILON);
   }

   /**
    * Test {@link UtilMath#C(int, int)}
    */
   @Test
   public void testC()
   {
      Assert.assertEquals(56, UtilMath.C(5, 8));
      Assert.assertEquals(7503, UtilMath.C(2, 123));
      Assert.assertEquals(13983816, UtilMath.C(6, 49));
      Assert.assertEquals(353697121050L, UtilMath.C(24, 42));
      Assert.assertEquals(353697121050L, UtilMath.C(42 - 24, 42));
   }

   /**
    * Test {@link UtilMath#equals(double, double)}
    */
   @Test
   public void testEqualsDoubleDouble()
   {
      Assert.assertTrue(UtilMath.equals(Math.E, Math.exp(1)));
   }

   /**
    * Test {@link UtilMath#factorial(int)}
    */
   @Test
   public void testFactorial()
   {
      Assert.assertEquals(2 * 3 * 4 * 5 * 6 * 7 * 8 * 9, UtilMath.factorial(9));
   }

   /**
    * Test {@link UtilMath#greaterCommonDivisor(int, int)}
    */
   @Test
   public void testGcd()
   {
      Assert.assertEquals(2, UtilMath.greaterCommonDivisor(4, 6));
      Assert.assertEquals(9, UtilMath.greaterCommonDivisor(123456789, 987654321));
      Assert.assertEquals(9, UtilMath.greaterCommonDivisor(987654321, 123456789));
      Assert.assertEquals(85, UtilMath.greaterCommonDivisor(85, 85));
      Assert.assertEquals(1, UtilMath.greaterCommonDivisor(13, 850));
   }

   /**
    * Test {@link UtilMath#interpolationExponential(double)}
    */
   @Test
   public void testInterpolationExponential()
   {
      Assert.assertEquals(0.0, UtilMath.interpolationExponential(0), UtilMath.EPSILON);
      Assert.assertEquals(1.0, UtilMath.interpolationExponential(1), UtilMath.EPSILON);
   }

   /**
    * Test {@link UtilMath#interpolationLogarithm(double)}
    */
   @Test
   public void testInterpolationLogarithm()
   {
      Assert.assertEquals(0.0, UtilMath.interpolationLogarithm(0), UtilMath.EPSILON);
      Assert.assertEquals(1.0, UtilMath.interpolationLogarithm(1), UtilMath.EPSILON);
   }

   /**
    * Test {@link UtilMath#interpolationSinus(double)}
    */
   @Test
   public void testInterpolationSinus()
   {
      Assert.assertEquals(0.0, UtilMath.interpolationSinus(0), UtilMath.EPSILON);
      Assert.assertEquals(1.0, UtilMath.interpolationSinus(1), UtilMath.EPSILON);
   }

   /**
    * Test {@link UtilMath#isConvex(java.awt.geom.Point2D...)}
    */
   @Test
   public void testIsConvex()
   {
      final Point2D[] pointsRectangle =
      {
            new Point2D.Double(0, 0), new Point2D.Double(10, 0), new Point2D.Double(10, 10), new Point2D.Double(0, 10)
      };
      Assert.assertTrue(UtilMath.isConvex(pointsRectangle));

      final Point2D[] pointsRectangleReverse =
      {
            new Point2D.Double(0, 0), new Point2D.Double(0, 10), new Point2D.Double(10, 10), new Point2D.Double(10, 0)
      };
      Assert.assertTrue(UtilMath.isConvex(pointsRectangleReverse));

      final Point2D[] pointsHole =
      {
            new Point2D.Double(0, 0), new Point2D.Double(10, 0), new Point2D.Double(10, 10), new Point2D.Double(5, 5), new Point2D.Double(0, 10)
      };
      Assert.assertFalse(UtilMath.isConvex(pointsHole));

      final Point2D[] pointsTime =
      {
            new Point2D.Double(0, 0), new Point2D.Double(10, 0), new Point2D.Double(0, 10), new Point2D.Double(10, 10)
      };
      Assert.assertFalse(UtilMath.isConvex(pointsTime));
   }

   /**
    * Test {@link UtilMath#isNul(double)}
    */
   @Test
   public void testIsNul()
   {
      Assert.assertTrue(UtilMath.isNul(0));
      Assert.assertTrue(UtilMath.isNul(UtilMath.EPSILON));
      Assert.assertFalse(UtilMath.isNul(0.1));
   }

   /**
    * Test {@link UtilMath#lowerCommonMultiple(int, int)}
    */
   @Test
   public void testLcm()
   {
      Assert.assertEquals(12, UtilMath.lowerCommonMultiple(4, 6));
      Assert.assertEquals(12000000, UtilMath.lowerCommonMultiple(4000000, 6000000));
   }

   /**
    * Test {@link UtilMath#max(double...)}
    */
   @Test
   public void testMax()
   {
      Assert.assertEquals(95.0, UtilMath.max(0, -8, 95, 8, -7, 2.3, Math.PI), UtilMath.EPSILON);
   }

   /**
    * Test {@link UtilMath#maxIntegers(int...)}
    */
   @Test
   public void testMaxIntegers()
   {
      Assert.assertEquals(95, UtilMath.maxIntegers(0, -8, 95, 8, -7, 2, 3));
   }

   /**
    * Test {@link UtilMath#min(double...)}
    */
   @Test
   public void testMin()
   {
      Assert.assertEquals(-8.0, UtilMath.min(0, -8, 95, 8, -7, 2.3, Math.PI), UtilMath.EPSILON);
   }

   /**
    * Test {@link UtilMath#minIntegers(int...)}
    */
   @Test
   public void testMinIntegers()
   {
      Assert.assertEquals(-8, UtilMath.minIntegers(0, -8, 95, 8, -7, 2, 3));
   }

   /**
    * Test {@link UtilMath#modulo(double, double)}
    */
   @Test
   public void testModuloDoubleDouble()
   {
      Assert.assertEquals(Math.PI / 2, UtilMath.modulo((4 * Math.PI) + (Math.PI / 2), Math.PI * 2), UtilMath.EPSILON);
   }

   /**
    * Test {@link UtilMath#modulo(int, int)}
    */
   @Test
   public void testModuloIntInt()
   {
      Assert.assertEquals(1, UtilMath.modulo(-1, 2));
      Assert.assertEquals(1, UtilMath.modulo(1, 2));
      Assert.assertEquals(1, UtilMath.modulo(-3, 2));
      Assert.assertEquals(1, UtilMath.modulo(3, 2));
   }

   /**
    * Test {@link UtilMath#modulo(long, long)}
    */
   @Test
   public void testModuloLongLong()
   {
      Assert.assertEquals(1L, UtilMath.modulo(-1L, 2L));
      Assert.assertEquals(1L, UtilMath.modulo(1L, 2L));
      Assert.assertEquals(1L, UtilMath.modulo(-3L, 2L));
      Assert.assertEquals(1L, UtilMath.modulo(3L, 2L));
   }

   /**
    * Test {@link UtilMath#PCubique(double, double, double, double, double)}
    */
   @Test
   public void testPCubique()
   {
      Assert.assertEquals(2.5, UtilMath.PCubique(1, 2, 3, 4, 0.5), UtilMath.EPSILON);
   }

   /**
    * Test {@link UtilMath#PCubiques(double, double, double, double, int, double[])}
    */
   @Test
   public void testPCubiques()
   {
      final double[] cub =
      {
            1, 2, 3, 4
      };
      Assert.assertArrayEquals(new double[]
      {
            1, 2.0, 3.0, 4.0
      }, UtilMath.PCubiques(1, 2, 3, 4, 4, cub), 1e-9);
   }

   /**
    * Test {@link UtilMath#pow(long, long)}
    */
   @Test
   public void testPow()
   {
      Assert.assertEquals(1024L, UtilMath.pow(2, 10));
      Assert.assertEquals(2048L, UtilMath.pow(2, 11));
   }

   /**
    * Test {@link UtilMath#PQuadrique(double, double, double, double)}
    */
   @Test
   public void testPQuadrique()
   {
      Assert.assertEquals(2.0, UtilMath.PQuadrique(1, 2, 3, 0.5), UtilMath.EPSILON);
   }

   /**
    * Test {@link UtilMath#PQuadriques(double, double, double, int, double[])}
    */
   @Test
   public void testPQuadriques()
   {
      final double[] cub =
      {
            1, 2, 3, 4
      };
      Assert.assertArrayEquals(new double[]
      {
            1, 1.6666666666666667, 2.333333333333333, 3
      }, UtilMath.PQuadriques(1, 2, 3, 4, cub), UtilMath.EPSILON);
   }

   /**
    * Test {@link UtilMath#sign(double)}
    */
   @Test
   public void testSignDouble()
   {
      Assert.assertEquals(0, UtilMath.sign(0.0));
      Assert.assertEquals(1, UtilMath.sign(10.0));
      Assert.assertEquals(-1, UtilMath.sign(-80.0));
   }

   /**
    * Test {@link UtilMath#sign(int)}
    */
   @Test
   public void testSignInt()
   {
      Assert.assertEquals(0, UtilMath.sign(0));
      Assert.assertEquals(1, UtilMath.sign(10));
      Assert.assertEquals(-1, UtilMath.sign(-80));
   }

   /**
    * Test {@link UtilMath#sign(long)}
    */
   @Test
   public void testSignLong()
   {
      Assert.assertEquals(0, UtilMath.sign(0L));
      Assert.assertEquals(1, UtilMath.sign(10L));
      Assert.assertEquals(-1, UtilMath.sign(-80L));
   }
}