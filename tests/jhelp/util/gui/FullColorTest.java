package jhelp.util.gui;

import org.junit.Assert;
import org.junit.Test;

import jhelp.util.debug.Debug;
import jhelp.util.debug.DebugLevel;

/**
 * Full color tests
 *
 * @author JHelp <br>
 */
public class FullColorTest
{
   /** Comparison epsilon */
   private static final double EPSILON = 0.01;

   /**
    * Convert tests
    */
   @Test
   public void testConvert()
   {
      FullColor fullColor = new FullColor(0.75, 0.75, 0);
      Assert.assertEquals(60.0, fullColor.getHue(), FullColorTest.EPSILON);
      Assert.assertEquals(1.0, fullColor.getSaturationHSV(), FullColorTest.EPSILON);
      Assert.assertEquals(0.75, fullColor.getValue(), FullColorTest.EPSILON);
      Debug.println(DebugLevel.INFORMATION, fullColor.getColorName());

      fullColor = new FullColor(0.75, 0.25, 0.75);
      Assert.assertEquals(300.0, fullColor.getHue(), FullColorTest.EPSILON);
      Assert.assertEquals(2.0 / 3.0, fullColor.getSaturationHSV(), FullColorTest.EPSILON);
      Assert.assertEquals(0.75, fullColor.getValue(), FullColorTest.EPSILON);
      Debug.println(DebugLevel.INFORMATION, fullColor.getColorName());
   }
}