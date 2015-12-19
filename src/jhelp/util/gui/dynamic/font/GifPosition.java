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
package jhelp.util.gui.dynamic.font;

import jhelp.util.gui.GIF;

/**
 * Describe a gif an its position
 * 
 * @author JHelp
 */
public class GifPosition
{
   /** GIF */
   private final GIF gif;
   /** X */
   private final int x;
   /** Y */
   private final int y;

   /**
    * Create a new instance of GifPosition
    * 
    * @param gif
    *           GIF
    * @param x
    *           X
    * @param y
    *           Y
    */
   public GifPosition(final GIF gif, final int x, final int y)
   {
      this.gif = gif;
      this.x = x;
      this.y = y;
   }

   /**
    * Actual gif value
    * 
    * @return Actual gif value
    */
   public GIF getGif()
   {
      return this.gif;
   }

   /**
    * Actual x value
    * 
    * @return Actual x value
    */
   public int getX()
   {
      return this.x;
   }

   /**
    * Actual y value
    * 
    * @return Actual y value
    */
   public int getY()
   {
      return this.y;
   }
}