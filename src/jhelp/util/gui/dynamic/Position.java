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

/**
 * Describe an element position
 * 
 * @author JHelp
 */
public class Position
{
   /**
    * Compute position interpolation between 2 given positions
    * 
    * @param before
    *           Start position
    * @param after
    *           End position
    * @param percent
    *           Percent progression between start and end
    * @return Interpolated position computed
    */
   public static Position computeInterpolation(final Position before, final Position after, final float percent)
   {
      final float anti = 1f - percent;
      final int x = (int) ((before.x * anti) + (after.x * percent));
      final int y = (int) ((before.y * anti) + (after.y * percent));
      return new Position(x, y);
   }

   /** X */
   private final int x;
   /** Y */
   private final int y;

   /**
    * Create a new instance of Position at (0, 0)
    */
   public Position()
   {
      this(0, 0);
   }

   /**
    * Create a new instance of Position
    * 
    * @param x
    *           X
    * @param y
    *           Y
    */
   public Position(final int x, final int y)
   {
      this.x = x;
      this.y = y;
   }

   /**
    * X
    * 
    * @return X
    */
   public int getX()
   {
      return this.x;
   }

   /**
    * Y
    * 
    * @return Y
    */
   public int getY()
   {
      return this.y;
   }
}