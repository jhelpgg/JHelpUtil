package jhelp.util.samples.image.ani;

import java.awt.BorderLayout;

import javax.swing.JFrame;

import jhelp.util.debug.Debug;
import jhelp.util.gui.UtilGUI;
import jhelp.util.gui.dynamic.ClearAnimation;
import jhelp.util.gui.dynamic.JHelpDynamicImage;
import jhelp.util.image.ani.AniImage;
import jhelp.util.samples.common.gui.SampleLabelJHelpImage;

public class SampleAni
{
   /**
    * @param args
    */
   public static void main(final String[] args)
   {
      try
      {
         final AniImage aniImage = new AniImage(SampleAni.class.getResourceAsStream("cur1103.ani"));
         final JHelpDynamicImage dynamicImage = new JHelpDynamicImage(512, 512);
         aniImage.setPosition(256 - (aniImage.getWidth() >> 1), 256 - (aniImage.getHeight() >> 1));

         final JFrame frame = new JFrame();
         frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
         frame.setLayout(new BorderLayout());
         final SampleLabelJHelpImage sampleLabelJHelpImage = new SampleLabelJHelpImage(dynamicImage.getImage());
         sampleLabelJHelpImage.setResize(true);
         frame.add(sampleLabelJHelpImage, BorderLayout.CENTER);
         UtilGUI.packedSize(frame);
         UtilGUI.centerOnScreen(frame);
         frame.setVisible(true);
         dynamicImage.playAnimation(new ClearAnimation(0xFFFFFFFF));
         dynamicImage.playAnimation(aniImage);
      }
      catch(final Exception exception)
      {
         Debug.printException(exception, "Failed to show the cursor");
      }
   }
}