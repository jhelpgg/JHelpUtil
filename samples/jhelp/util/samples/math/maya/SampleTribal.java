package jhelp.util.samples.math.maya;

import jhelp.util.gui.UtilGUI;

/**
 * Sample of tribal numbers
 *
 * @author JHelp <br>
 */
public class SampleTribal
{
   /**
    * Launch the sample of tribal numbers frame
    *
    * @param args
    *           Unused
    */
   public static void main(final String[] args)
   {
      UtilGUI.initializeGUI();
      final SampleTribalFrame sampleTribalFrame = new SampleTribalFrame();
      sampleTribalFrame.setVisible(true);
   }
}