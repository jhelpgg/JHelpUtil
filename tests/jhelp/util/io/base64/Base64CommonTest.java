package jhelp.util.io.base64;

import org.junit.Assert;
import org.junit.Test;

import java.io.UnsupportedEncodingException;

/**
 * Base 64 tests
 *
 * @author JHelp <br>
 */
public class Base64CommonTest
{
    /**
     * Deactivated test
     *
     * @throws UnsupportedEncodingException On issue
     */
    // @ Test
    public void test() throws UnsupportedEncodingException
    {
        final String[] tests =
                {
                        "message", "MESSAGE", "" + "MessAge", "Message"
                };

        byte[] binary;
        String base64;
        for (final String test : tests)
        {
            binary = test.getBytes("UTF-8");
            base64 = Base64Common.toBase64(binary);
            binary = Base64Common.fromBase64(base64);
            Assert.assertNotNull(binary);
            Assert.assertEquals("TEST=" + test, test, new String(binary, "UTF-8"));
        }
    }

    /**
     * Symbol tests
     */
    @Test
    public void testSymbol()
    {
        for (char c = 'A'; c <= 'Z'; c++)
        {
            Assert.assertEquals("Char=" + c, c, Base64Common.getSymbol(Base64Common.getIndex(c)));
        }

        for (char c = 'a'; c <= 'z'; c++)
        {
            Assert.assertEquals("Char=" + c, c, Base64Common.getSymbol(Base64Common.getIndex(c)));
        }

        for (char c = '0'; c <= '9'; c++)
        {
            Assert.assertEquals("Char=" + c, c, Base64Common.getSymbol(Base64Common.getIndex(c)));
        }

        Assert.assertEquals("Char=+", '+', Base64Common.getSymbol(Base64Common.getIndex('+')));
        Assert.assertEquals("Char=/", '/', Base64Common.getSymbol(Base64Common.getIndex('/')));
    }
}