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
package jhelp.util.gui;

/**
 * Represents a segment
 * 
 * @author JHelp
 */
public class Segment
{
   /** First point X */
   public final double x1;
   /** Second point X */
   public final double x2;
   /** First point Y */
   public final double y1;
   /** Second point Y */
   public final double y2;

   /**
    * Create a new instance of Segment
    * 
    * @param x1
    *           First point X
    * @param y1
    *           First point Y
    * @param x2
    *           Second point X
    * @param y2
    *           Second point Y
    */
   public Segment(final double x1, final double y1, final double x2, final double y2)
   {
      this.x1 = x1;
      this.y1 = y1;
      this.x2 = x2;
      this.y2 = y2;
   }
}