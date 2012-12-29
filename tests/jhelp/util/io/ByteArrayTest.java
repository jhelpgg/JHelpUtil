package jhelp.util.io;

import org.junit.Assert;
import org.junit.Test;

/**
 * Test of {@link ByteArray}
 * 
 * @author JHelp
 */
public class ByteArrayTest
{
   /**
    * Do some simple tests
    */
   @Test
   public void simpleTest()
   {
      final ByteArray byteArray = new ByteArray();

      Assert.assertEquals(0, byteArray.getSize());
      Assert.assertEquals(0, byteArray.available());

      byteArray.write(1);
      byteArray.write(2);
      byteArray.write(3);
      byteArray.write(4);
      byteArray.write(5);

      Assert.assertEquals(5, byteArray.getSize());
      Assert.assertEquals(5, byteArray.available());
      Assert.assertArrayEquals(new byte[]
      {
            1, 2, 3, 4, 5
      }, byteArray.toArray());
      Assert.assertEquals(1, byteArray.read());
      Assert.assertEquals(2, byteArray.read());
      Assert.assertEquals(5, byteArray.getSize());
      Assert.assertEquals(3, byteArray.available());

      byteArray.mark();
      Assert.assertEquals(3, byteArray.read());
      Assert.assertEquals(4, byteArray.read());
      Assert.assertEquals(5, byteArray.read());
      Assert.assertEquals(5, byteArray.getSize());
      Assert.assertEquals(0, byteArray.available());

      byteArray.reset();
      Assert.assertEquals(5, byteArray.getSize());
      Assert.assertEquals(3, byteArray.available());
      Assert.assertEquals(3, byteArray.read());
      Assert.assertEquals(4, byteArray.read());
      Assert.assertEquals(5, byteArray.read());

      byteArray.write(new byte[]
      {
            6, 7, 8, 9
      }, 1, 2);
      Assert.assertEquals(7, byteArray.getSize());
      Assert.assertEquals(2, byteArray.available());
      Assert.assertEquals(7, byteArray.read());
      Assert.assertEquals(8, byteArray.read());

      byteArray.write(new byte[]
      {
            6, 7, 8, 9
      });
      Assert.assertEquals(11, byteArray.getSize());
      Assert.assertEquals(4, byteArray.available());
      Assert.assertEquals(6, byteArray.read());
      Assert.assertEquals(7, byteArray.read());
      Assert.assertEquals(8, byteArray.read());
      Assert.assertEquals(9, byteArray.read());

      byteArray.readFromStart();
      Assert.assertEquals(11, byteArray.getSize());
      Assert.assertEquals(11, byteArray.available());

      byte[] temp = new byte[4];
      Assert.assertEquals(4, byteArray.read(temp));
      Assert.assertEquals(1, temp[0]);
      Assert.assertEquals(2, temp[1]);
      Assert.assertEquals(3, temp[2]);
      Assert.assertEquals(4, temp[3]);

      Assert.assertEquals(2, byteArray.read(temp, 1, 2));
      Assert.assertEquals(5, temp[1]);
      Assert.assertEquals(7, temp[2]);

      temp = new byte[16];
      Assert.assertEquals(5, byteArray.read(temp, 2, 200));
      Assert.assertEquals(8, temp[2]);
      Assert.assertEquals(6, temp[3]);
      Assert.assertEquals(7, temp[4]);
      Assert.assertEquals(8, temp[5]);
      Assert.assertEquals(9, temp[6]);
   }
}