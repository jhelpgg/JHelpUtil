package jhelp.util.text;

import org.junit.Assert;
import org.junit.Test;

/**
 * Test of {@link StringExtractor}
 * 
 * @author JHelp
 */
public class StringExtractorTest
{
   /**
    * Do some simple tests
    */
   @Test
   public void simpleTest()
   {
      final StringExtractor stringExtractor = new StringExtractor("J'ai vu l'éléphant \t\n rose \\'IGNORE IT\\'");

      Assert.assertEquals("J", stringExtractor.next());
      Assert.assertEquals("ai vu l", stringExtractor.next());
      Assert.assertEquals("éléphant", stringExtractor.next());
      Assert.assertEquals("rose", stringExtractor.next());
      Assert.assertEquals("\\'IGNORE", stringExtractor.next());
      Assert.assertEquals("IT\\'", stringExtractor.next());

      Assert.assertNull(stringExtractor.next());
   }

   /**
    * Do some other simple tests
    */
   @Test
   public void simpleTestReturn()
   {
      final StringExtractor stringExtractor = new StringExtractor("J'ai vu l'éléphant \t\n rose \\'IGNORE IT\\'", true);

      Assert.assertEquals("J", stringExtractor.next());
      Assert.assertEquals("ai vu l", stringExtractor.next());
      Assert.assertEquals("éléphant", stringExtractor.next());
      Assert.assertEquals(" ", stringExtractor.next());
      Assert.assertEquals("\t", stringExtractor.next());
      Assert.assertEquals("\n", stringExtractor.next());
      Assert.assertEquals(" ", stringExtractor.next());
      Assert.assertEquals("rose", stringExtractor.next());
      Assert.assertEquals(" ", stringExtractor.next());
      Assert.assertEquals("\\'IGNORE", stringExtractor.next());
      Assert.assertEquals(" ", stringExtractor.next());
      Assert.assertEquals("IT\\'", stringExtractor.next());

      Assert.assertNull(stringExtractor.next());
   }

   @Test
   public void testOpenClose1()
   {
      final StringExtractor stringExtractor = new StringExtractor("7 + (85 * 9) - 8");
      stringExtractor.addOpenCloseIgnore('(', ')');

      Assert.assertEquals("7", stringExtractor.next());
      Assert.assertEquals("+", stringExtractor.next());
      Assert.assertEquals("(85 * 9)", stringExtractor.next());
      Assert.assertEquals("-", stringExtractor.next());
      Assert.assertEquals("8", stringExtractor.next());

      Assert.assertNull(stringExtractor.next());
   }

   @Test
   public void testOpenClose2()
   {
      final StringExtractor stringExtractor = new StringExtractor("(85 * 9)");
      stringExtractor.addOpenCloseIgnore('(', ')');

      Assert.assertEquals("(85 * 9)", stringExtractor.next());

      Assert.assertNull(stringExtractor.next());
   }

   @Test
   public void testOpenClose3()
   {
      final StringExtractor stringExtractor = new StringExtractor("(85 * 9) - 8");
      stringExtractor.addOpenCloseIgnore('(', ')');

      Assert.assertEquals("(85 * 9)", stringExtractor.next());
      Assert.assertEquals("-", stringExtractor.next());
      Assert.assertEquals("8", stringExtractor.next());

      Assert.assertNull(stringExtractor.next());
   }

   @Test
   public void testOpenClose4()
   {
      final StringExtractor stringExtractor = new StringExtractor("7 + (85 * 9)");
      stringExtractor.addOpenCloseIgnore('(', ')');

      Assert.assertEquals("7", stringExtractor.next());
      Assert.assertEquals("+", stringExtractor.next());
      Assert.assertEquals("(85 * 9)", stringExtractor.next());

      Assert.assertNull(stringExtractor.next());
   }

   @Test
   public void testOpenClose5()
   {
      final StringExtractor stringExtractor = new StringExtractor("+(85 * 9)");
      stringExtractor.addOpenCloseIgnore('(', ')');

      Assert.assertEquals("+(85 * 9)", stringExtractor.next());

      Assert.assertNull(stringExtractor.next());
   }

   @Test
   public void testOpenClose6()
   {
      final StringExtractor stringExtractor = new StringExtractor("+(85 * 9)+");
      stringExtractor.addOpenCloseIgnore('(', ')');

      Assert.assertEquals("+(85 * 9)+", stringExtractor.next());

      Assert.assertNull(stringExtractor.next());
   }

   @Test
   public void testOpenClose7()
   {
      final StringExtractor stringExtractor = new StringExtractor("(85 * 9)+");
      stringExtractor.addOpenCloseIgnore('(', ')');

      Assert.assertEquals("(85 * 9)+", stringExtractor.next());

      Assert.assertNull(stringExtractor.next());
   }

   @Test
   public void testOpenClose8()
   {
      final StringExtractor stringExtractor = new StringExtractor("(85 * 9)+(t g b)");
      stringExtractor.addOpenCloseIgnore('(', ')');

      Assert.assertEquals("(85 * 9)+(t g b)", stringExtractor.next());

      Assert.assertNull(stringExtractor.next());
   }
}