package jhelp.util.gui.dynamic;

import jhelp.util.gui.JHelpImage;

/**
 * Background based on image
 * 
 * @author JHelp
 */
public class BackgroundImage
      extends Background
{
   /** Image on background */
   private JHelpImage image;

   /**
    * Create a new instance of BackgroundImage
    * 
    * @param image
    *           Image on background
    */
   public BackgroundImage(final JHelpImage image)
   {
      if(image == null)
      {
         throw new NullPointerException("image musn't be null");
      }

      this.image = image;
   }

   /**
    * Draw the background <br>
    * <br>
    * <b>Parent documentation:</b><br>
    * {@inheritDoc}
    * 
    * @param absoluteFrame
    *           Absolute frame
    * @param image
    *           Image where draw background
    * @see jhelp.util.gui.dynamic.Background#drawBackground(float, jhelp.util.gui.JHelpImage)
    */
   @Override
   public void drawBackground(final float absoluteFrame, final JHelpImage image)
   {
      this.image = JHelpImage.createResizedImage(this.image, image.getWidth(), image.getHeight());
      image.drawImage(0, 0, this.image, false);
   }
}