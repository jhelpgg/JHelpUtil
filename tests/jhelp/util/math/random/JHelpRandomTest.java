package jhelp.util.math.random;

import jhelp.util.debug.Debug;
import jhelp.util.debug.DebugLevel;

import org.junit.Assert;
import org.junit.Test;

public class JHelpRandomTest
{
   /**
    * Test of {@link JHelpRandom#random(int, int)}
    */
   @Test
   public void testRandom()
   {
      int rand;

      for(int i = 0; i < 128; i++)
      {
         rand = JHelpRandom.random(5, 8);

         Assert.assertTrue(rand + " not in [5, 8]", (rand >= 5) && (rand <= 8));
      }
   }

   /**
    * Test of {@link JHelpRandom#random(int)}
    */
   @Test
   public void testRandom2()
   {
      int rand;

      for(int i = 0; i < 128; i++)
      {
         rand = JHelpRandom.random(8);

         Assert.assertTrue(rand + " not in [0, 8[", (rand >= 0) && (rand < 8));
      }
   }

   @Test
   public void tests()
   {
      final JHelpRandom<Integer> random = new JHelpRandom<Integer>();

      try
      {
         random.addChoice(0, 0);

         Assert.fail("Should have exception for <=0 number");
      }
      catch(final Exception exception)
      {
         // Thats we want
      }

      Assert.assertNull("Nothing add so must be null", random.choose());

      random.addChoice(1, 1);
      random.addChoice(2, 2);
      random.addChoice(3, 3);
      random.addChoice(4, 4);
      random.addChoice(5, 5);

      int value;
      for(int i = 0; i < 128; i++)
      {
         value = random.choose();

         Assert.assertTrue(value + " not in [1, 5]", (value >= 1) && (value <= 5));
         Debug.println(DebugLevel.VERBOSE, "Choose=", value);
      }
   }
}