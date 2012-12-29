package jhelp.util.list;

import org.junit.Assert;
import org.junit.Test;

public class ArrayIntTest
{
   /**
    * Do some simple tests
    */
   @Test
   public void simpleTest()
   {
      final ArrayInt arrayInt = new ArrayInt();

      //

      Assert.assertTrue(arrayInt.isEmpty());
      Assert.assertTrue(arrayInt.isSortedFast());
      Assert.assertTrue(arrayInt.isSortedSlow());
      Assert.assertFalse(arrayInt.contains(123));

      Assert.assertEquals(0, arrayInt.getSize());

      Assert.assertEquals(-1, arrayInt.getIndex(123));
      Assert.assertEquals(-1, arrayInt.getIndex(12));
      Assert.assertEquals(0, arrayInt.getSize());

      //

      arrayInt.add(123);
      Assert.assertFalse(arrayInt.isEmpty());
      Assert.assertTrue(arrayInt.isSortedFast());
      Assert.assertTrue(arrayInt.isSortedSlow());
      Assert.assertTrue(arrayInt.contains(123));

      Assert.assertEquals(1, arrayInt.getSize());
      Assert.assertEquals(123, arrayInt.getInteger(0));

      Assert.assertEquals(0, arrayInt.getIndex(123));
      Assert.assertEquals(-1, arrayInt.getIndex(12));
      Assert.assertEquals(1, arrayInt.getSize());

      //

      arrayInt.add(125);
      Assert.assertFalse(arrayInt.isEmpty());
      Assert.assertTrue(arrayInt.isSortedFast());
      Assert.assertTrue(arrayInt.isSortedSlow());
      Assert.assertTrue(arrayInt.contains(123));

      Assert.assertEquals(2, arrayInt.getSize());
      Assert.assertEquals(123, arrayInt.getInteger(0));
      Assert.assertEquals(125, arrayInt.getInteger(1));

      Assert.assertEquals(0, arrayInt.getIndex(123));
      Assert.assertEquals(-1, arrayInt.getIndex(12));
      Assert.assertEquals(2, arrayInt.getSize());

      //

      arrayInt.add(0);
      Assert.assertFalse(arrayInt.isEmpty());
      Assert.assertFalse(arrayInt.isSortedFast());
      Assert.assertFalse(arrayInt.isSortedSlow());
      Assert.assertTrue(arrayInt.contains(123));

      Assert.assertEquals(3, arrayInt.getSize());
      Assert.assertEquals(123, arrayInt.getInteger(0));
      Assert.assertEquals(125, arrayInt.getInteger(1));

      Assert.assertEquals(0, arrayInt.getIndex(123));
      Assert.assertEquals(-1, arrayInt.getIndex(12));
      Assert.assertEquals(3, arrayInt.getSize());

      //

      arrayInt.remove(2);
      Assert.assertFalse(arrayInt.isEmpty());
      Assert.assertFalse(arrayInt.isSortedFast());
      Assert.assertTrue(arrayInt.isSortedSlow());
      Assert.assertTrue(arrayInt.contains(123));

      Assert.assertEquals(2, arrayInt.getSize());
      Assert.assertEquals(123, arrayInt.getInteger(0));
      Assert.assertEquals(125, arrayInt.getInteger(1));

      Assert.assertEquals(0, arrayInt.getIndex(123));
      Assert.assertEquals(-1, arrayInt.getIndex(12));
      Assert.assertEquals(2, arrayInt.getSize());

      //

      arrayInt.add(12);
      Assert.assertFalse(arrayInt.isEmpty());
      Assert.assertFalse(arrayInt.isSortedFast());
      Assert.assertFalse(arrayInt.isSortedSlow());
      Assert.assertTrue(arrayInt.contains(123));

      Assert.assertEquals(3, arrayInt.getSize());
      Assert.assertEquals(123, arrayInt.getInteger(0));
      Assert.assertEquals(125, arrayInt.getInteger(1));
      Assert.assertEquals(12, arrayInt.getInteger(2));

      Assert.assertEquals(0, arrayInt.getIndex(123));
      Assert.assertEquals(2, arrayInt.getIndex(12));
      Assert.assertEquals(3, arrayInt.getSize());

      //

      arrayInt.sort();
      Assert.assertFalse(arrayInt.isEmpty());
      Assert.assertTrue(arrayInt.isSortedFast());
      Assert.assertTrue(arrayInt.isSortedSlow());
      Assert.assertTrue(arrayInt.contains(123));

      Assert.assertEquals(3, arrayInt.getSize());
      Assert.assertEquals(12, arrayInt.getInteger(0));
      Assert.assertEquals(123, arrayInt.getInteger(1));
      Assert.assertEquals(125, arrayInt.getInteger(2));

      Assert.assertEquals(1, arrayInt.getIndex(123));
      Assert.assertEquals(0, arrayInt.getIndex(12));
      Assert.assertEquals(3, arrayInt.getSize());

      //

      arrayInt.add(12);
      arrayInt.add(123);
      arrayInt.add(12);
      arrayInt.add(12);
      arrayInt.add(123);
      arrayInt.add(125);
      arrayInt.add(123);
      arrayInt.sortUniq();
      Assert.assertFalse(arrayInt.isEmpty());
      Assert.assertTrue(arrayInt.isSortedFast());
      Assert.assertTrue(arrayInt.isSortedSlow());
      Assert.assertTrue(arrayInt.contains(123));

      Assert.assertEquals(3, arrayInt.getSize());
      Assert.assertEquals(12, arrayInt.getInteger(0));
      Assert.assertEquals(123, arrayInt.getInteger(1));
      Assert.assertEquals(125, arrayInt.getInteger(2));

      Assert.assertEquals(1, arrayInt.getIndex(123));
      Assert.assertEquals(0, arrayInt.getIndex(12));
      Assert.assertEquals(3, arrayInt.getSize());

      //

      arrayInt.insert(42, 1);
      Assert.assertFalse(arrayInt.isEmpty());
      Assert.assertTrue(arrayInt.isSortedFast());
      Assert.assertTrue(arrayInt.isSortedSlow());
      Assert.assertTrue(arrayInt.contains(123));

      Assert.assertEquals(4, arrayInt.getSize());
      Assert.assertEquals(12, arrayInt.getInteger(0));
      Assert.assertEquals(42, arrayInt.getInteger(1));
      Assert.assertEquals(123, arrayInt.getInteger(2));
      Assert.assertEquals(125, arrayInt.getInteger(3));

      Assert.assertEquals(2, arrayInt.getIndex(123));
      Assert.assertEquals(0, arrayInt.getIndex(12));
      Assert.assertEquals(4, arrayInt.getSize());

      //

      arrayInt.clear();
      Assert.assertTrue(arrayInt.isEmpty());
      Assert.assertTrue(arrayInt.isSortedFast());
      Assert.assertTrue(arrayInt.isSortedSlow());
      Assert.assertFalse(arrayInt.contains(123));
      Assert.assertEquals(-1, arrayInt.getIndex(123));
      Assert.assertEquals(-1, arrayInt.getIndex(12));
      Assert.assertEquals(0, arrayInt.getSize());
   }
}
