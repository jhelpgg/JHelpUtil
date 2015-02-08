package jhelp.util.gui.dynamic;

import java.awt.Dimension;
import java.util.List;

import jhelp.util.gui.JHelpFont;
import jhelp.util.gui.JHelpImage;
import jhelp.util.gui.JHelpTextAlign;
import jhelp.util.gui.JHelpTextLine;
import jhelp.util.list.Pair;
import jhelp.util.samples.gui.SampleDynamicText;

/**
 * Apply an alpha mask to an animation.<br>
 * The animation can only be seen throw alpha mask holes.<br>
 * See {@link SampleDynamicText} for an example of usage and effect
 * 
 * @author JHelp
 */
public class AplhaMaskAnimated
      implements JHelpDynamicImageListener
{
   /**
    * Create an alpha masked animated with alpha mask based on text
    * 
    * @param text
    *           Text used for alpha mask
    * @param font
    *           Text font
    * @param textAlign
    *           Text alignment
    * @param colorBackground
    *           Background color
    * @return Created alpha masked
    */
   public static AplhaMaskAnimated createTextAnimated(final String text, final JHelpFont font, final JHelpTextAlign textAlign, final int colorBackground)
   {
      final Pair<List<JHelpTextLine>, Dimension> pair = font.computeTextLines(text, textAlign);
      final JHelpImage alphaMask = new JHelpImage(pair.element2.width, pair.element2.height);
      alphaMask.startDrawMode();

      for(final JHelpTextLine textLine : pair.element1)
      {
         alphaMask.paintMask(textLine.getX(), textLine.getY(), textLine.getMask(), 0xFFFFFFFF, 0, false);
      }

      alphaMask.endDrawMode();
      return new AplhaMaskAnimated(alphaMask, colorBackground);
   }

   /** Alpha mask to use */
   private final JHelpImage        alphaMask;
   /** Dynamic image to play the animation */
   private final JHelpDynamicImage dynamicImage;
   /** Image to see the masked animation */
   private final JHelpImage        resultImage;

   /**
    * Create a new instance of AplhaMaskAnimated
    * 
    * @param alphaMask
    *           Alpha mask to use
    * @param colorBackground
    *           Background color
    */
   public AplhaMaskAnimated(final JHelpImage alphaMask, final int colorBackground)
   {
      this.alphaMask = alphaMask;
      final int width = alphaMask.getWidth();
      final int height = alphaMask.getHeight();
      this.dynamicImage = new JHelpDynamicImage(width, height);
      this.dynamicImage.dynamicImageListener = this;
      this.resultImage = new JHelpImage(width, height, colorBackground);
      this.refreshImage();
   }

   /**
    * Refresh the image
    */
   private void refreshImage()
   {
      this.resultImage.startDrawMode();
      this.resultImage.paintAlphaMask(0, 0, this.alphaMask, this.dynamicImage.getImage());
      this.resultImage.endDrawMode();
   }

   /**
    * Called each time dynamic image update <br>
    * <br>
    * <b>Parent documentation:</b><br>
    * {@inheritDoc}
    * 
    * @param dynamicImage
    *           Dynamic image
    * @see jhelp.util.gui.dynamic.JHelpDynamicImageListener#dynamicImageUpdate(jhelp.util.gui.dynamic.JHelpDynamicImage)
    */
   @Override
   public void dynamicImageUpdate(final JHelpDynamicImage dynamicImage)
   {
      this.refreshImage();
   }

   /**
    * Dynamic animation to play animation in it
    * 
    * @return Dynamic animation to play animation in it
    */
   public JHelpDynamicImage getDynamicImage()
   {
      return this.dynamicImage;
   }

   /**
    * Image to draw to see animation result
    * 
    * @return Image to draw to see animation result
    */
   public JHelpImage getImage()
   {
      return this.resultImage;
   }
}