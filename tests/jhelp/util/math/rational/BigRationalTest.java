package jhelp.util.math.rational;

import org.junit.Assert;
import org.junit.Test;

public class BigRationalTest
{

   @Test
   public void testComparison()
   {
      Assert.assertTrue(BigRational.createRational(1, 3).compareTo(BigRational.createRational(9, 27)) == 0);
      Assert.assertTrue(BigRational.createRational(35, 9).compareTo(BigRational.createRational(70, 18)) == 0);
      Assert.assertTrue(BigRational.createRational(100, 300).compareTo(BigRational.createRational(1, 3)) == 0);
      Assert.assertTrue(BigRational.createRational(100, 300).compareTo(BigRational.createRational(-1, -3)) == 0);
      Assert.assertTrue(BigRational.createRational(1, 3).compareTo(BigRational.createRational(1, 2)) < 0);
      Assert.assertTrue(BigRational.createRational(2, 3).compareTo(BigRational.createRational(1, 2)) > 0);
      Assert.assertTrue(BigRational.createRational(-2, 3).compareTo(BigRational.createRational(1, 2)) < 0);
   }
}