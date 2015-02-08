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