package jhelp.util.samples.gui;

import java.awt.BorderLayout;

import javax.swing.JFrame;

import jhelp.util.debug.Debug;
import jhelp.util.debug.DebugLevel;
import jhelp.util.gui.JHelpImage;
import jhelp.util.gui.UtilGUI;
import jhelp.util.gui.dynamic.AnimationPosition;
import jhelp.util.gui.dynamic.ClearAnimation;
import jhelp.util.gui.dynamic.FlagAnimation;
import jhelp.util.gui.dynamic.ImageTransition;
import jhelp.util.gui.dynamic.JHelpDynamicImage;
import jhelp.util.gui.dynamic.Position;
import jhelp.util.gui.dynamic.SinusInterpolation;
import jhelp.util.gui.dynamic.font.AnimationFontGif;
import jhelp.util.gui.dynamic.font.FontGif;
import jhelp.util.math.random.JHelpRandom;
import jhelp.util.samples.common.gui.SampleLabelJHelpImage;

public class SampleDynamicAnimation
{
   private static int TIME_ACROSS_SCREEN = 10;
   private static int TIME_CHANGE_IMAGE  = 20;

   /**
    * @param args
    */
   public static void main(final String[] args)
   {
      try
      {
         UtilGUI.initializeGUI();
         final JHelpImage flag = JHelpImage.loadImageThumb(SampleDynamicAnimation.class.getResourceAsStream("001-Fog01.png"), 512, 256);
         final JHelpImage flag2 = JHelpImage.loadImageThumb(SampleDynamicAnimation.class.getResourceAsStream("floor068.jpg"), 512, 256);
         final ImageTransition imageTransition = new ImageTransition(JHelpDynamicImage.FPS * SampleDynamicAnimation.TIME_CHANGE_IMAGE, flag, flag2,
               Integer.MAX_VALUE, true);
         final FlagAnimation flagAnimation = new FlagAnimation(0, 128, imageTransition.getInterpolated(), 2, 10, JHelpDynamicImage.FPS / 2f);
         final AnimationPosition animationPosition = new AnimationPosition(flagAnimation, Integer.MAX_VALUE, SinusInterpolation.SINUS_INTERPOLATION);
         animationPosition.addFrame(0, new Position(0, 128));
         animationPosition.addFrame(JHelpDynamicImage.FPS * SampleDynamicAnimation.TIME_ACROSS_SCREEN, new Position(2048 - 512, 128));
         animationPosition.addFrame(JHelpDynamicImage.FPS * SampleDynamicAnimation.TIME_ACROSS_SCREEN * 2, new Position(0, 128));
         final JHelpDynamicImage dynamicImage = new JHelpDynamicImage(2048, 1024);
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
         dynamicImage.playAnimation(flagAnimation);
         dynamicImage.playAnimation(animationPosition);

         final FontGif fontGif = new FontGif(JHelpRandom.random(FontGif.FONT_GIF_NAMES));
         final AnimationFontGif animationFontGif = new AnimationFontGif(0, 98, "ABCDFEGHIJKLMNOPQRSTUVWXYZ ?\nabcdfeghijklmnopqrstuvwxyz !\n1234567890",
               fontGif);
         dynamicImage.playAnimation(animationFontGif);
         dynamicImage.playAnimation(imageTransition);

         for(final String name : FontGif.FONT_GIF_NAMES)
         {
            Debug.println(DebugLevel.INFORMATION, name);
         }
      }
      catch(final Exception exception)
      {
         Debug.printException(exception, "Failed to create the sample");
      }
   }
}