package jhelp.util.io.base64;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import org.junit.Assert;
import org.junit.Test;

/**
 * Test of {@link Base64InputStream}
 * 
 * @author JHelp
 */
public class Base64InputStreamTest
{
   /**
    * Simple test
    * 
    * @throws IOException
    *            On I/O issue
    */
   @Test
   public void testSimple() throws IOException
   {
      final byte[] array =
      {
            (byte) 0x41, (byte) 0x41, (byte) 0x45, (byte) 0x43
      };

      final Base64InputStream base64InputStream = new Base64InputStream(new ByteArrayInputStream(array));

      Assert.assertEquals(0, base64InputStream.read());
      Assert.assertEquals(1, base64InputStream.read());
      Assert.assertEquals(2, base64InputStream.read());

      Assert.assertEquals(-1, base64InputStream.read());

      base64InputStream.close();
   }
}