package jhelp.util.samples.math.maya;

import jhelp.util.gui.UtilGUI;

/**
 * Launch sample for Maya numbers
 *
 * @author JHelp <br>
 */
public class SampleMaya
{

   /**
    * Launch the sample
    *
    * @param args
    *           Unused
    */
   public static void main(final String[] args)
   {
      UtilGUI.initializeGUI();
      final SampleMayaFrame sampleMayaFrame = new SampleMayaFrame();
      sampleMayaFrame.setVisible(true);
   }
}