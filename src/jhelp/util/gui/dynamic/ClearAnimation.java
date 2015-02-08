package jhelp.util.gui.dynamic;

import jhelp.util.gui.JHelpImage;

/**
 * Clear image with one color
 * 
 * @author JHelp
 */
public class ClearAnimation
      extends ImmediateAnimation
{
   /** Color to use */
   private final int color;

   /**
    * Create a new instance of ClearAnimation
    * 
    * @param color
    *           Color to use
    */
   public ClearAnimation(final int color)
   {
      this.color = color;
   }

   /**
    * Do the clearing <br>
    * <br>
    * <b>Parent documentation:</b><br>
    * {@inheritDoc}
    * 
    * @param image
    *           Image to clear
    * @see jhelp.util.gui.dynamic.ImmediateAnimation#doImmediately(jhelp.util.gui.JHelpImage)
    */
   @Override
   public void doImmediately(final JHelpImage image)
   {
      image.clear(this.color);
   }
}