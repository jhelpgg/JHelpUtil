package jhelp.util.list;

import org.junit.Assert;
import org.junit.Test;

public class HashIntTest
{
   @Test
   public void test()
   {
      final HashInt<String> hashInt = new HashInt<String>();
      Assert.assertEquals(0, hashInt.getSize());
      Assert.assertTrue(hashInt.isEmpty());

      final String[] array =
      {
            "Zero", "One", "Two", "Three", "Four", "Five", "Six"
      };
      final int length = array.length;
      final int[] indexes = new int[length];

      for(int i = 0; i < length; i++)
      {
         indexes[i] = i;
      }

      int index;
      Scramble.scramble(indexes);

      for(int i = 0; i < length; i++)
      {
         index = indexes[i];
         hashInt.put(index, array[index]);
      }

      Assert.assertEquals(7, hashInt.getSize());
      Assert.assertFalse(hashInt.isEmpty());
      Assert.assertTrue(hashInt.contains(5));
      Assert.assertFalse(hashInt.contains(9));
      Assert.assertEquals("Four", hashInt.get(4));

      index = 0;

      for(final String element : hashInt)
      {
         Assert.assertEquals("Element " + index, array[index], element);
         index++;
      }

      hashInt.put(4, "Flower");
      Assert.assertEquals(7, hashInt.getSize());
      Assert.assertFalse(hashInt.isEmpty());
      Assert.assertNotSame("Four", hashInt.get(4));
      Assert.assertEquals("Flower", hashInt.get(4));

      hashInt.remove(3);
      hashInt.remove(3);
      Assert.assertEquals(6, hashInt.getSize());
      Assert.assertFalse(hashInt.isEmpty());
      Assert.assertFalse(hashInt.contains(3));
      Assert.assertNull(hashInt.get(3));

      hashInt.clear();
      Assert.assertEquals(0, hashInt.getSize());
      Assert.assertTrue(hashInt.isEmpty());
   }
}