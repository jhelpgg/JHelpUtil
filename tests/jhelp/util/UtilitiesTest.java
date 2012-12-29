package jhelp.util;

import org.junit.Assert;
import org.junit.Test;

/**
 * Test of {@link Utilities}
 * 
 * @author JHelp
 */
public class UtilitiesTest
{
   /**
    * Test of {@link Utilities#contains(char, char...)}
    */
   @Test
   public void testContains()
   {
      Assert.assertTrue(Utilities.contains('d', 'a', 'b', 'c', 'd', 'e', 'f'));
      Assert.assertFalse(Utilities.contains('x', 'a', 'b', 'c', 'd', 'e', 'f'));
   }
}