/**
 * <h1>License :</h1> <br>
 * The following code is deliver as is. I take care that code compile and work, but I am not responsible about any damage it may
 * cause.<br>
 * You can use, modify, the code as your need for any usage. But you can't do any action that avoid me or other person use,
 * modify this code. The code is free for usage and modification, you can't change that fact.<br>
 * <br>
 * 
 * @author JHelp
 */
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