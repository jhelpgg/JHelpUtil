package jhelp.util.text;

import org.junit.Assert;
import org.junit.Test;

/**
 * Tests of {@link UtilText}
 * 
 * @author JHelp
 */
public class UtilTextTest
{
   /**
    * Test of {@link UtilText#capture(String, String)}
    */
   @Test
   public void testCapture()
   {
      Assert.assertArrayEquals(new String[]
      {
         "TRUE"
      }, UtilText.capture("NOT(?)", "NOT(TRUE)"));

      Assert.assertArrayEquals(new String[]
      {
            "TRUE", "PROP('A')"
      }, UtilText.capture("AND(?,?)", "AND(TRUE,PROP('A'))"));
   }

   /**
    * Test of {@link UtilText#concatenate(Object...)}
    */
   @Test
   public void testConcatenate()
   {
      Assert.assertEquals("[12, 13, 85, 96]", UtilText.concatenate(new int[]
      {
            12, 13, 85, 96
      }, ""));
      Assert.assertEquals("[pomme, poire, prune]", UtilText.concatenate(new String[]
      {
            "pomme", "poire", "prune"
      }, ""));

      Assert.assertEquals("[[12, 13, 85, 96], [45]]", UtilText.concatenate(new int[][]
      {
            {
                  12, 13, 85, 96
            },
            {
               45
            }
      }, ""));
      Assert.assertEquals("[[pomme, poire, prune], [test]]", UtilText.concatenate(new String[][]
      {
            {
                  "pomme", "poire", "prune"
            },
            {
               "test"
            }
      }, ""));
   }
}