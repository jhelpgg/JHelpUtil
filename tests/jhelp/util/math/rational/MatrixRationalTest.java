package jhelp.util.math.rational;

import jhelp.util.debug.Debug;
import jhelp.util.debug.DebugLevel;
import jhelp.util.math.random.JHelpRandom;
import jhelp.util.time.TimeDebug;

import org.junit.Assert;
import org.junit.Test;

public class MatrixRationalTest
{
   @Test
   public void testAddition()
   {
      final MatrixRational matrix = new MatrixRational(5, 4);

      try
      {
         matrix.addition(new MatrixRational(42, 12));
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
            matrix.set(x, y, Rational.createRational(x + y));
         }
      }

      final MatrixRational expected = new MatrixRational(5, 4);

      for(int y = 0; y < 4; y++)
      {
         for(int x = 0; x < 5; x++)
         {
            expected.set(x, y, Rational.createRational(2 * (x + y)));
         }
      }

      matrix.addition(matrix);
      Assert.assertEquals("Addition failed", expected, matrix);
   }

   @Test
   public void testBigMatrix()
   {
      final int size = 100;
      final MatrixRational matrix = new MatrixRational(size, size);
      for(int y = 0; y < size; y++)
      {
         for(int x = 0; x < size; x++)
         {
            matrix.set(x, y, x < y
                  ? Rational.createRational(0)
                  : Rational.createRational(1));
         }
      }

      matrix.set(size >> 2, (3 * size) >> 2, Rational.createRational(size));

      final TimeDebug timeDebug = new TimeDebug("BIG matrix");
      final MatrixRational invert = matrix.invert();
      timeDebug.add("Invert");
      Assert.assertTrue(matrix.multiplication(invert).isIdentity());
      timeDebug.add("First mult");
      Assert.assertTrue(invert.multiplication(matrix).isIdentity());
      timeDebug.add("Second mult");
      timeDebug.dump();
   }

   @Test
   public void testCopy()
   {
      final MatrixRational matrix = new MatrixRational(5, 4);
      matrix.setValues(Rational.createRational(2), Rational.createRational(3), Rational.createRational(42), Rational.createRational((float) Math.PI), Rational.createRational((float) Math.E), Rational.createRational(-123456789),
            Rational.createRational(852), Rational.createRational(0.1234567891011123141516171819f), Rational.createRational(9), Rational.createRational(-10), Rational.createRational(11), Rational.createRational(21),
            Rational.createRational(13), Rational.createRational(14), Rational.createRational(1515), Rational.createRational(16), Rational.createRational(17), Rational.createRational(18), Rational.createRational(19),
            Rational.createRational(20));
      final MatrixRational copy = matrix.copy();
      Assert.assertEquals("Copy different !", matrix, copy);
   }

   @Test
   public void testDeterminant()
   {
      final MatrixRational matrix = new MatrixRational(3, 3);

      int value = 0;
      for(int y = 0; y < 3; y++)
      {
         for(int x = 0; x < 3; x++)
         {
            matrix.set(x, y, Rational.createRational(value++));
         }
      }

      final Rational determinant = matrix.determinant();
      Assert.assertTrue("Determinant is not 0, but " + determinant, determinant == Rational.ZERO);
   }

   @Test
   public void testDeterminantAdjacent()
   {
      final int size = 4;
      final MatrixRational matrix = new MatrixRational(size, size);

      for(int y = 0; y < size; y++)
      {
         for(int x = y; x < size; x++)
         {
            matrix.set(x, y, Rational.createRational(1.1f));
         }
      }

      final Rational determinant = matrix.determinant();
      final MatrixRational adjacent = matrix.adjacent();
      final Rational determinant1 = adjacent.determinant();
      adjacent.addition(new MatrixRational(size, size));
      final Rational determinant2 = adjacent.determinant();

      Debug.println(DebugLevel.VERBOSE, "determinant=", determinant, " determinant1=", determinant1, " determinant2=", determinant2);

      Assert.assertTrue(determinant1.equals(determinant2));
   }

   @Test
   public void testEqualsObject()
   {
      final MatrixRational matrix = new MatrixRational(5, 4);
      Assert.assertNotEquals("MatrixRational with different size can't be equals", matrix, new MatrixRational(6, 9));
      final MatrixRational second = new MatrixRational(5, 4);
      Assert.assertEquals("First failed", matrix, second);
      matrix.set(2, 3, Rational.createRational(42));
      Assert.assertNotEquals("Second failed", matrix, second);
      second.set(2, 3, Rational.createRational(42));
      Assert.assertEquals("Third failed", matrix, second);
   }

   @Test
   public void testGet()
   {
      final MatrixRational matrix = new MatrixRational(5, 3);

      try
      {
         matrix.get(-1, 4);
         Assert.fail("Can't access outside the matrix");
      }
      catch(final Exception exception)
      {
         // That's we want !
      }

      matrix.set(2, 1, Rational.createRational(10));
      Assert.assertTrue("Value must be 10 not " + matrix.get(2, 1), Rational.createRational(10).equals(matrix.get(2, 1)));
   }

   @Test
   public void testGetHeight()
   {
      MatrixRational matrix;
      int size;

      for(int i = 0; i < 10; i++)
      {
         size = JHelpRandom.random(1, 100);
         matrix = new MatrixRational(size, size);

         Assert.assertEquals("Height must be " + size, size, matrix.getHeight());
      }
   }

   @Test
   public void testGetWidth()
   {
      MatrixRational matrix;
      int size;

      for(int i = 0; i < 10; i++)
      {
         size = JHelpRandom.random(1, 100);
         matrix = new MatrixRational(size, size);

         Assert.assertEquals("Width must be " + size, size, matrix.getWidth());
      }
   }

   @Test
   public void testInvert()
   {
      MatrixRational matrix = new MatrixRational(3, 3);

      int value = 0;
      for(int y = 0; y < 3; y++)
      {
         for(int x = 0; x < 3; x++)
         {
            matrix.set(x, y, Rational.createRational(value++));
         }
      }

      matrix.set(0, 0, Rational.createRational(42));

      MatrixRational invert = matrix.invert();

      Debug.println(DebugLevel.VERBOSE, "matrix=", matrix);
      Debug.println(DebugLevel.VERBOSE, "invert=", invert);

      final MatrixRational identity = new MatrixRational(3, 3);
      identity.toIdentity();

      Assert.assertEquals("Way 1", identity, matrix.multiplication(invert));
      Assert.assertEquals("Way 2", identity, invert.multiplication(matrix));

      matrix = new MatrixRational(2, 2);
      matrix.setValues(Rational.createRational(2), Rational.createRational(1), Rational.createRational(0), Rational.createRational(3));
      invert = matrix.invert();

      Debug.println(DebugLevel.VERBOSE, "matrix=", matrix);
      Debug.println(DebugLevel.VERBOSE, "invert=", invert);
      Assert.assertTrue("Way 3", matrix.multiplication(invert).isIdentity());
      Assert.assertTrue("Way 4", invert.multiplication(matrix).isIdentity());
   }

   @Test
   public void testIsIdentity()
   {
      MatrixRational matrix = new MatrixRational(4, 2);
      Assert.assertFalse(matrix.isIdentity());
      matrix.toIdentity();
      Assert.assertTrue(matrix.isIdentity());

      matrix = new MatrixRational(2, 4);
      Assert.assertFalse(matrix.isIdentity());
      matrix.toIdentity();
      Assert.assertTrue(matrix.isIdentity());

      matrix = new MatrixRational(4, 4);
      Assert.assertFalse(matrix.isIdentity());
      matrix.toIdentity();
      Assert.assertTrue(matrix.isIdentity());
   }

   @Test
   public void testMultiplication()
   {
      final MatrixRational first = new MatrixRational(1, 2);

      try
      {
         first.multiplication(new MatrixRational(1, 2));
         Assert.fail("Sizes mismatch");
      }
      catch(final Exception exception)
      {
         // That's what we expect
      }

      first.set(0, 0, Rational.createRational(1));
      first.set(0, 1, Rational.createRational(2));

      final MatrixRational second = new MatrixRational(2, 1);
      second.set(0, 0, Rational.createRational(3));
      second.set(1, 0, Rational.createRational(4));

      MatrixRational result = new MatrixRational(2, 2);
      result.set(0, 0, Rational.createRational(1 * 3));
      result.set(1, 0, Rational.createRational(1 * 4));
      result.set(0, 1, Rational.createRational(2 * 3));
      result.set(1, 1, Rational.createRational(2 * 4));

      Assert.assertEquals("Multiplication failed 1", result, first.multiplication(second));

      result = new MatrixRational(1, 1);
      result.set(0, 0, Rational.createRational((1 * 3) + (2 * 4)));

      Assert.assertEquals("Multiplication failed 2", result, second.multiplication(first));
   }

   @Test
   public void testSet()
   {
      final MatrixRational matrix = new MatrixRational(5, 3);

      try
      {
         matrix.set(-1, 4, Rational.createRational(42));
         Assert.fail("Can't access outside the matrix");
      }
      catch(final Exception exception)
      {
         // That's we want !
      }

      matrix.set(2, 1, Rational.createRational(10));
      Assert.assertTrue("Value must be 10 not " + matrix.get(2, 1), Rational.createRational(10).equals(matrix.get(2, 1)));
   }

   @Test
   public void testSubtraction()
   {
      final MatrixRational matrix = new MatrixRational(5, 4);

      try
      {
         matrix.subtraction(new MatrixRational(42, 12));
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
            matrix.set(x, y, Rational.createRational(x + y));
         }
      }

      matrix.subtraction(matrix);
      Assert.assertEquals("Subtraction failed", new MatrixRational(5, 4), matrix);
   }

   @Test
   public void testToIndentity()
   {
      MatrixRational matrix = new MatrixRational(3, 3);
      matrix.toIdentity();
      MatrixRational indentity = new MatrixRational(3, 3);
      indentity.set(0, 0, Rational.createRational(1));
      indentity.set(1, 1, Rational.createRational(1));
      indentity.set(2, 2, Rational.createRational(1));
      Assert.assertEquals("Identity easy", indentity, matrix);

      matrix = new MatrixRational(4, 3);
      matrix.toIdentity();
      indentity = new MatrixRational(4, 3);
      indentity.set(0, 0, Rational.createRational(1));
      indentity.set(1, 1, Rational.createRational(1));
      indentity.set(2, 2, Rational.createRational(1));
      Assert.assertEquals("Identity hard 1", indentity, matrix);

      matrix = new MatrixRational(3, 4);
      matrix.toIdentity();
      indentity = new MatrixRational(3, 4);
      indentity.set(0, 0, Rational.createRational(1));
      indentity.set(1, 1, Rational.createRational(1));
      indentity.set(2, 2, Rational.createRational(1));
      Assert.assertEquals("Identity hard 2", indentity, matrix);

   }

   @Test
   public void testToZero()
   {
      final MatrixRational matrix = new MatrixRational(5, 3);

      for(int i = 0; i < 35; i++)
      {
         matrix.set(JHelpRandom.random(5), JHelpRandom.random(3), Rational.createRational((float) Math.random()));
      }

      matrix.toZero();
      Assert.assertEquals("Zero failed", new MatrixRational(5, 3), matrix);
   }

   @Test
   public void testTranspose()
   {
      final MatrixRational matrix = new MatrixRational(5, 3);
      int value = 0;
      for(int y = 0; y < 3; y++)
      {
         for(int x = 0; x < 5; x++)
         {
            matrix.set(x, y, Rational.createRational(value++));
         }
      }

      final MatrixRational expected = new MatrixRational(3, 5);
      value = 0;
      for(int y = 0; y < 3; y++)
      {
         for(int x = 0; x < 5; x++)
         {
            expected.set(y, x, Rational.createRational(value++));
         }
      }

      Assert.assertEquals("Transpose incorrect", expected, matrix.transpose());
   }
}