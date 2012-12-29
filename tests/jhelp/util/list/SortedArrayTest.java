package jhelp.util.list;

import org.junit.Assert;
import org.junit.Test;

/**
 * Test of {@link SortedArray}
 * 
 * @author JHelp
 */
public class SortedArrayTest
{
   /**
    * Simple tests
    */
   @Test
   public void simpleTest()
   {
      SortedArray<Integer> arraySorted = new SortedArray<Integer>(Integer.class);

      Assert.assertTrue(arraySorted.isEmpty());
      Assert.assertFalse(arraySorted.contains(123));
      Assert.assertEquals(-1, arraySorted.indexOf(123));
      Assert.assertEquals(-1, arraySorted.indexOf(12));
      Assert.assertEquals(0, arraySorted.getSize());

      arraySorted.add(123);
      Assert.assertFalse(arraySorted.isEmpty());
      Assert.assertTrue(arraySorted.contains(123));
      Assert.assertEquals(0, arraySorted.indexOf(123));
      Assert.assertEquals(-1, arraySorted.indexOf(12));
      Assert.assertEquals(1, arraySorted.getSize());

      arraySorted.add(125);
      Assert.assertFalse(arraySorted.isEmpty());
      Assert.assertTrue(arraySorted.contains(123));
      Assert.assertEquals(0, arraySorted.indexOf(123));
      Assert.assertEquals(-1, arraySorted.indexOf(12));
      Assert.assertEquals(2, arraySorted.getSize());

      arraySorted.add(12);
      Assert.assertFalse(arraySorted.isEmpty());
      Assert.assertTrue(arraySorted.contains(123));
      Assert.assertEquals(1, arraySorted.indexOf(123));
      Assert.assertEquals(0, arraySorted.indexOf(12));
      Assert.assertEquals(3, arraySorted.getSize());

      //

      arraySorted = new SortedArray<Integer>(Integer.class, true);

      arraySorted.add(12);
      arraySorted.add(123);
      arraySorted.add(12);
      arraySorted.add(12);
      arraySorted.add(123);
      arraySorted.add(125);
      arraySorted.add(123);
      Assert.assertFalse(arraySorted.isEmpty());
      Assert.assertTrue(arraySorted.contains(123));
      Assert.assertEquals(1, arraySorted.indexOf(123));
      Assert.assertEquals(0, arraySorted.indexOf(12));
      Assert.assertEquals(3, arraySorted.getSize());

      arraySorted.clear();
      Assert.assertTrue(arraySorted.isEmpty());
      Assert.assertFalse(arraySorted.contains(123));
      Assert.assertEquals(-1, arraySorted.indexOf(123));
      Assert.assertEquals(-1, arraySorted.indexOf(12));
      Assert.assertEquals(0, arraySorted.getSize());
   }
}