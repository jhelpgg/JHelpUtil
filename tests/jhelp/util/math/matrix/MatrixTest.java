package jhelp.util.math.matrix;

import org.junit.Assert;
import org.junit.Test;

import jhelp.util.debug.Debug;
import jhelp.util.debug.DebugLevel;
import jhelp.util.math.UtilMath;
import jhelp.util.math.random.JHelpRandom;
import jhelp.util.time.TimeDebug;

/**
 * Matrix tests
 *
 * @author JHelp <br>
 */
@SuppressWarnings("SuspiciousNameCombination")
public class MatrixTest
{
   /**
    * Addition test
    */
   @Test
   public void testAddition()
   {
      final Matrix matrix = new Matrix(5, 4);

      try
      {
         matrix.addition(new Matrix(42, 12));
         Assert.fail("Must have same size for addition");
      }
      catch(final Exception exception)
      {
         // That's what we expect
      }

      for(int y = 0; y < 4; y++)
      {
         for(int x = 0; x < 5; x++)
         {
            matrix.set(x, y, x + y);
         }
      }

      final Matrix expected = new Matrix(5, 4);

      for(int y = 0; y < 4; y++)
      {
         for(int x = 0; x < 5; x++)
         {
            expected.set(x, y, 2 * (x + y));
         }
      }

      matrix.addition(matrix);
      Assert.assertEquals("Addition failed", expected, matrix);
   }

   /**
    * Big matrix tests
    */
   @Test
   public void testBigMatrix()
   {
      final int size = 100;
      final Matrix matrix = new Matrix(size, size);
      for(int y = 0; y < size; y++)
      {
         for(int x = 0; x < size; x++)
         {
            matrix.set(x, y, x < y
                  ? 0.5
                  : x);
         }
      }

      matrix.set(size >> 2, (3 * size) >> 2, size);

      final TimeDebug timeDebug = new TimeDebug("BIG matrix");
      final Matrix invert = matrix.invert();
      timeDebug.add("Invert");
      Assert.assertTrue(matrix.multiplication(invert).isIdentity());
      timeDebug.add("First mult");
      Assert.assertTrue(invert.multiplication(matrix).isIdentity());
      timeDebug.add("Second mult");
      timeDebug.dump();
   }

   /**
    * Copy tests
    */
   @Test
   public void testCopy()
   {
      final Matrix matrix = new Matrix(5, 4);
      matrix.setValues(2, 3, 42, Math.PI, Math.E, -123456789, 852, 0.1234567891011123141516171819, 9, -10, 11, 21, 13, 14, 1515, 16, 17, 18, 19, 20);
      final Matrix copy = matrix.copy();
      Assert.assertEquals("Copy different !", matrix, copy);
   }

   /**
    * Determinant tests
    */
   @Test
   public void testDeterminant()
   {
      final Matrix matrix = new Matrix(3, 3);

      int value = 0;
      for(int y = 0; y < 3; y++)
      {
         for(int x = 0; x < 3; x++)
         {
            matrix.set(x, y, value++);
         }
      }

      final double determinant = matrix.determinant();
      Assert.assertTrue("Determinant is not 0, but " + determinant, UtilMath.equals(0, determinant));
   }

   /**
    * Determinant and adjacent coherence test
    */
   @Test
   public void testDeterminantAdjacent()
   {
      final int size = 4;
      final Matrix matrix = new Matrix(size, size);

      for(int y = 0; y < size; y++)
      {
         for(int x = y; x < size; x++)
         {
            matrix.set(x, y, 1.1);
         }
      }

      final double determinant = matrix.determinant();
      final Matrix adjacent = matrix.adjacent();
      final double determinant1 = adjacent.determinant();
      adjacent.addition(new Matrix(size, size));
      final double determinant2 = adjacent.determinant();

      Debug.println(DebugLevel.VERBOSE, "determinant=", determinant, " determinant1=", determinant1, " determinant2=", determinant2);

      Assert.assertTrue(Matrix.equals(determinant1, determinant2));
   }

   /**
    * Equals tests
    */
   @Test
   public void testEqualsObject()
   {
      final Matrix matrix = new Matrix(5, 4);
      Assert.assertNotEquals("Matrix with different size can't be equals", matrix, new Matrix(6, 9));
      final Matrix second = new Matrix(5, 4);
      Assert.assertEquals("First failed", matrix, second);
      matrix.set(2, 3, 42);
      Assert.assertNotEquals("Second failed", matrix, second);
      second.set(2, 3, 42);
      Assert.assertEquals("Third failed", matrix, second);
   }

   /**
    * Get/set tests
    */
   @Test
   public void testGet()
   {
      final Matrix matrix = new Matrix(5, 3);

      try
      {
         matrix.get(-1, 4);
         Assert.fail("Can't access outside the matrix");
      }
      catch(final Exception exception)
      {
         // That's we want !
      }

      matrix.set(2, 1, 10);
      Assert.assertTrue("Value must be 10 not " + matrix.get(2, 1), UtilMath.equals(10, matrix.get(2, 1)));
   }

   /**
    * getHeight test
    */
   @Test
   public void testGetHeight()
   {
      Matrix matrix;
      int size;

      for(int i = 0; i < 10; i++)
      {
         size = JHelpRandom.random(1, 100);
         matrix = new Matrix(size, size);

         Assert.assertEquals("Height must be " + size, size, matrix.getHeight());
      }
   }

   /**
    * getWidth test
    */
   @Test
   public void testGetWidth()
   {
      Matrix matrix;
      int size;

      for(int i = 0; i < 10; i++)
      {
         size = JHelpRandom.random(1, 100);
         matrix = new Matrix(size, size);

         Assert.assertEquals("Width must be " + size, size, matrix.getWidth());
      }
   }

   /**
    * Invert tests
    */
   @Test
   public void testInvert()
   {
      Matrix matrix = new Matrix(3, 3);

      int value = 0;
      for(int y = 0; y < 3; y++)
      {
         for(int x = 0; x < 3; x++)
         {
            matrix.set(x, y, value++);
         }
      }

      matrix.set(0, 0, 42);

      Matrix invert = matrix.invert();

      Debug.println(DebugLevel.VERBOSE, "matrix=", matrix);
      Debug.println(DebugLevel.VERBOSE, "invert=", invert);

      final Matrix identity = new Matrix(3, 3);
      identity.toIdentity();

      Assert.assertEquals("Way 1", identity, matrix.multiplication(invert));
      Assert.assertEquals("Way 2", identity, invert.multiplication(matrix));

      matrix = new Matrix(2, 2);
      matrix.setValues(2, 1, 0, 3);
      invert = matrix.invert();

      Debug.println(DebugLevel.VERBOSE, "matrix=", matrix);
      Debug.println(DebugLevel.VERBOSE, "invert=", invert);
      Assert.assertTrue("Way 3", matrix.multiplication(invert).isIdentity());
      Assert.assertTrue("Way 4", invert.multiplication(matrix).isIdentity());
   }

   /**
    * Identity tests
    */
   @Test
   public void testIsIdentity()
   {
      Matrix matrix = new Matrix(4, 2);
      Assert.assertFalse(matrix.isIdentity());
      matrix.toIdentity();
      Assert.assertTrue(matrix.isIdentity());

      matrix = new Matrix(2, 4);
      Assert.assertFalse(matrix.isIdentity());
      matrix.toIdentity();
      Assert.assertTrue(matrix.isIdentity());

      matrix = new Matrix(4, 4);
      Assert.assertFalse(matrix.isIdentity());
      matrix.toIdentity();
      Assert.assertTrue(matrix.isIdentity());
   }

   /**
    * Multiplication tests
    */
   @Test
   public void testMultiplication()
   {
      final Matrix first = new Matrix(1, 2);

      try
      {
         first.multiplication(new Matrix(1, 2));
         Assert.fail("Sizes mismatch");
      }
      catch(final Exception exception)
      {
         // That's what we expect
      }

      first.set(0, 0, 1);
      first.set(0, 1, 2);

      final Matrix second = new Matrix(2, 1);
      second.set(0, 0, 3);
      second.set(1, 0, 4);

      Matrix result = new Matrix(2, 2);
      result.set(0, 0, 3);
      result.set(1, 0, 4);
      result.set(0, 1, 2 * 3);
      result.set(1, 1, 2 * 4);

      Assert.assertEquals("Multiplication failed 1", result, first.multiplication(second));

      result = new Matrix(1, 1);
      result.set(0, 0, (3) + (2 * 4));

      Assert.assertEquals("Multiplication failed 2", result, second.multiplication(first));
   }

   /**
    * Get/set tests
    */
   @Test
   public void testSet()
   {
      final Matrix matrix = new Matrix(5, 3);

      try
      {
         matrix.set(-1, 4, 42);
         Assert.fail("Can't access outside the matrix");
      }
      catch(final Exception exception)
      {
         // That's we want !
      }

      matrix.set(2, 1, 10);
      Assert.assertTrue("Value must be 10 not " + matrix.get(2, 1), UtilMath.equals(10, matrix.get(2, 1)));
   }

   /**
    * subtraction tests
    */
   @Test
   public void testSubtraction()
   {
      final Matrix matrix = new Matrix(5, 4);

      try
      {
         matrix.subtraction(new Matrix(42, 12));
         Assert.fail("Must have same size for subtraction");
      }
      catch(final Exception exception)
      {
         // That's what we expect
      }

      for(int y = 0; y < 4; y++)
      {
         for(int x = 0; x < 5; x++)
         {
            matrix.set(x, y, x + y);
         }
      }

      matrix.subtraction(matrix);
      Assert.assertEquals("Subtraction failed", new Matrix(5, 4), matrix);
   }

   /**
    * Indentity tests
    */
   @Test
   public void testToIndentity()
   {
      Matrix matrix = new Matrix(3, 3);
      matrix.toIdentity();
      Matrix indentity = new Matrix(3, 3);
      indentity.set(0, 0, 1);
      indentity.set(1, 1, 1);
      indentity.set(2, 2, 1);
      Assert.assertEquals("Identity easy", indentity, matrix);

      matrix = new Matrix(4, 3);
      matrix.toIdentity();
      indentity = new Matrix(4, 3);
      indentity.set(0, 0, 1);
      indentity.set(1, 1, 1);
      indentity.set(2, 2, 1);
      Assert.assertEquals("Identity hard 1", indentity, matrix);

      matrix = new Matrix(3, 4);
      matrix.toIdentity();
      indentity = new Matrix(3, 4);
      indentity.set(0, 0, 1);
      indentity.set(1, 1, 1);
      indentity.set(2, 2, 1);
      Assert.assertEquals("Identity hard 2", indentity, matrix);

   }

   /**
    * Zero tests
    */
   @Test
   public void testToZero()
   {
      final Matrix matrix = new Matrix(5, 3);

      for(int i = 0; i < 35; i++)
      {
         matrix.set(JHelpRandom.random(5), JHelpRandom.random(3), Math.random());
      }

      matrix.toZero();
      Assert.assertEquals("Zero failed", new Matrix(5, 3), matrix);
   }

   /**
    * Transpose tests
    */
   @Test
   public void testTranspose()
   {
      final Matrix matrix = new Matrix(5, 3);
      int value = 0;
      for(int y = 0; y < 3; y++)
      {
         for(int x = 0; x < 5; x++)
         {
            matrix.set(x, y, value++);
         }
      }

      final Matrix expected = new Matrix(3, 5);
      value = 0;
      for(int y = 0; y < 3; y++)
      {
         for(int x = 0; x < 5; x++)
         {
            expected.set(y, x, value++);
         }
      }

      Assert.assertEquals("Adjacent incorrect", expected, matrix.transpose());
   }
}