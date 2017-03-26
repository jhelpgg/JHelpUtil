package jhelp.util.math;

import org.junit.Assert;
import org.junit.Test;

import jhelp.util.debug.Debug;
import jhelp.util.debug.DebugLevel;

/**
 * Unit tests for {@link Base}
 *
 * @author JHelp <br>
 */
public class BaseTest
{
   /**
    * {@link Base#convert(long)} tests
    */
   @Test
   public void testConvert()
   {
      Base base = new Base('0', '1');
      Assert.assertEquals("101", base.convert(5));
      Assert.assertEquals("-110", base.convert(-6));

      base = new Base('.', '+', '*');
      Assert.assertEquals(".", base.convert(0));
      Assert.assertEquals("+", base.convert(1));
      Assert.assertEquals("*", base.convert(2));
      Assert.assertEquals("+.", base.convert(3));
      Assert.assertEquals("++", base.convert(4));
      Assert.assertEquals("+*", base.convert(5));
      Assert.assertEquals("*.", base.convert(6));
      Assert.assertEquals("*+", base.convert(7));
      Assert.assertEquals("**", base.convert(8));
      Assert.assertEquals("+..", base.convert(9));
      Assert.assertEquals("+.+", base.convert(10));
      Assert.assertEquals("-+.*", base.convert(-11));

      base = new Base(16);
      Assert.assertEquals("cafe", base.convert(0xCAFE));
      Assert.assertEquals("-face", base.convert(-0xFACE));
   }

   /**
    * {@link Base#parse(String)} tests
    */
   @Test
   public void testParse()
   {
      Base base = new Base('0', '1');
      Assert.assertEquals(5, base.parse("101"));
      Assert.assertEquals(-6, base.parse("-110"));

      base = new Base('.', '+', '*');
      Assert.assertEquals(0, base.parse("."));
      Assert.assertEquals(1, base.parse("+"));
      Assert.assertEquals(2, base.parse("*"));
      Assert.assertEquals(3, base.parse("+."));
      Assert.assertEquals(4, base.parse("++"));
      Assert.assertEquals(5, base.parse("+*"));
      Assert.assertEquals(6, base.parse("*."));
      Assert.assertEquals(7, base.parse("*+"));
      Assert.assertEquals(8, base.parse("**"));
      Assert.assertEquals(9, base.parse("+.."));
      Assert.assertEquals(10, base.parse("+.+"));
      Assert.assertEquals(-11, base.parse("-+.*"));

      base = new Base(16);
      Assert.assertEquals(0xCAFE, base.parse("cafe"));
      Assert.assertEquals(-0xFACE, base.parse("-face"));
   }

   /**
    * {@link Base#convert(long)} and {@link Base#parse(String)} combination tests
    */
   @Test
   public void testParseConvert()
   {
      final Base base = new Base(123);
      Assert.assertEquals(123456789, base.parse(base.convert(123456789)));
      Assert.assertEquals("HelloWord", base.convert(base.parse("HelloWord")));

      Debug.println(DebugLevel.INFORMATION, "123456789 -> ", base.convert(123456789));
      Debug.println(DebugLevel.INFORMATION, "HelloWord <- ", base.parse("HelloWord"));
   }
}