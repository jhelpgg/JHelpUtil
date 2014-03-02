package jhelp.util.gui;

/**
 * Represents a part of text
 * 
 * @author JHelp
 */
public class JHelpTextLine
{
   /** Indicates if the part is a end of line */
   private final boolean   endOfLine;
   /** Part height */
   private final int       height;
   /** Mask to use */
   private final JHelpMask mask;
   /** Text carry */
   private final String    text;
   /** Part width */
   private final int       width;
   /** Y location */
   private final int       y;
   /** X location */
   int                     x;

   /**
    * Create a new instance of JHelpTextLine
    * 
    * @param text
    *           Text to carry
    * @param x
    *           X position
    * @param y
    *           Y position
    * @param width
    *           Part width
    * @param height
    *           Part height
    * @param mask
    *           Mask to use
    * @param endOfLine
    *           Indicates if the part is a end of line
    */
   public JHelpTextLine(final String text, final int x, final int y, final int width, final int height, final JHelpMask mask, final boolean endOfLine)
   {
      this.text = text;
      this.x = x;
      this.y = y;
      this.width = width;
      this.height = height;
      this.mask = mask;
      this.endOfLine = endOfLine;
   }

   /**
    * Part height
    * 
    * @return Part height
    */
   public int getHeight()
   {
      return this.height;
   }

   /**
    * Mask to use
    * 
    * @return Mask to use
    */
   public JHelpMask getMask()
   {
      return this.mask;
   }

   /**
    * Text carry
    * 
    * @return Text carry
    */
   public String getText()
   {
      return this.text;
   }

   /**
    * Part width
    * 
    * @return Part width
    */
   public int getWidth()
   {
      return this.width;
   }

   /**
    * X location
    * 
    * @return X location
    */
   public int getX()
   {
      return this.x;
   }

   /**
    * Y location
    * 
    * @return Y location
    */
   public int getY()
   {
      return this.y;
   }

   /**
    * Indicates if the part is a end of line
    * 
    * @return {@code true} if the part is a end of line
    */
   public boolean isEndOfLine()
   {
      return this.endOfLine;
   }
}