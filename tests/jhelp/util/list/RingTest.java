package jhelp.util.list;

import org.junit.Assert;
import org.junit.Test;

/**
 * Test of {@link Ring}
 * 
 * @author JHelp
 */
public class RingTest
{
   /**
    * Do some tests
    */
   @Test
   public void testRing()
   {
      // []
      final Ring<String> ring = new Ring<String>();
      Assert.assertTrue(ring.isEmpty());
      Assert.assertEquals("Ring[]", ring.toString());

      // [Apple]
      ring.add("Apple");
      Assert.assertFalse(ring.isEmpty());
      Assert.assertEquals("Apple", ring.get());
      Assert.assertEquals("Ring[Apple]", ring.toString());

      ring.next();
      Assert.assertFalse(ring.isEmpty());
      Assert.assertEquals("Apple", ring.get());
      Assert.assertEquals("Ring[Apple]", ring.toString());

      ring.previous();
      Assert.assertFalse(ring.isEmpty());
      Assert.assertEquals("Apple", ring.get());
      Assert.assertEquals("Ring[Apple]", ring.toString());

      // [Apple, Tree]
      ring.add("Tree");
      Assert.assertFalse(ring.isEmpty());
      Assert.assertEquals("Apple", ring.get());
      Assert.assertEquals("Ring[Apple, Tree]", ring.toString());

      ring.next();
      Assert.assertFalse(ring.isEmpty());
      Assert.assertEquals("Tree", ring.get());
      Assert.assertEquals("Ring[Tree, Apple]", ring.toString());

      ring.previous();
      Assert.assertFalse(ring.isEmpty());
      Assert.assertEquals("Apple", ring.get());
      Assert.assertEquals("Ring[Apple, Tree]", ring.toString());

      ring.previous();
      Assert.assertFalse(ring.isEmpty());
      Assert.assertEquals("Tree", ring.get());
      Assert.assertEquals("Ring[Tree, Apple]", ring.toString());

      // [Apple]
      ring.remove();
      Assert.assertFalse(ring.isEmpty());
      Assert.assertEquals("Apple", ring.get());
      Assert.assertEquals("Ring[Apple]", ring.toString());

      ring.next();
      Assert.assertFalse(ring.isEmpty());
      Assert.assertEquals("Apple", ring.get());
      Assert.assertEquals("Ring[Apple]", ring.toString());

      ring.previous();
      Assert.assertFalse(ring.isEmpty());
      Assert.assertEquals("Apple", ring.get());
      Assert.assertEquals("Ring[Apple]", ring.toString());

      // []
      ring.remove();
      Assert.assertTrue(ring.isEmpty());
      Assert.assertEquals("Ring[]", ring.toString());
   }
}