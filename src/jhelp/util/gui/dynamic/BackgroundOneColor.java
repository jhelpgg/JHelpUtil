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
 * Background with one color
 * 
 * @author JHelp
 */
public class BackgroundOneColor
      extends Background
{
   /** Color for background */
   private int color;

   /**
    * Create a new instance of BackgroundOneColor
    * 
    * @param color
    *           Color for background
    */
   public BackgroundOneColor(final int color)
   {
      this.color = color;
   }

   /**
    * Draw background <br>
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
      image.clear(this.color);
   }

   /**
    * Color to draw background
    * 
    * @return Color to draw background
    */
   public int getColor()
   {
      return this.color;
   }

   /**
    * Change background color
    * 
    * @param color
    *           Background color
    */
   public void setColor(final int color)
   {
      this.color = color;
   }
}