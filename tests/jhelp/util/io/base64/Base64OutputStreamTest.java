package jhelp.util.io.base64;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.junit.Assert;
import org.junit.Test;

/**
 * Test of {@link Base64OutputStream}
 * 
 * @author JHelp
 */
public class Base64OutputStreamTest
{
   /**
    * Do some simple tests
    * 
    * @throws IOException
    *            On writing issue
    */
   @Test
   public void testSimple() throws IOException
   {
      final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
      final Base64OutputStream base64OutputStream = new Base64OutputStream(byteArrayOutputStream);

      base64OutputStream.write(0);
      base64OutputStream.write(1);
      base64OutputStream.write(2);

      base64OutputStream.flush();
      base64OutputStream.close();

      final byte[] array = byteArrayOutputStream.toByteArray();

      Assert.assertEquals((byte) 0x41, array[0]);
      Assert.assertEquals((byte) 0x41, array[1]);
      Assert.assertEquals((byte) 0x45, array[2]);
      Assert.assertEquals((byte) 0x43, array[3]);
   }
}