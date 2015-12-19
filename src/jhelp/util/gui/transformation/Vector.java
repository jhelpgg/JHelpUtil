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
package jhelp.util.gui.transformation;

/**
 * Transformation vector
 * 
 * @author JHelp
 */
public class Vector
{
   /** Vector X */
   public int vx;
   /** Vector Y */
   public int vy;

   /**
    * Create a new instance of Vector zero
    */
   public Vector()
   {
      this(0, 0);
   }

   /**
    * Create a new instance of Vector
    * 
    * @param vx
    *           Vector X
    * @param vy
    *           Vector Y
    */
   public Vector(final int vx, final int vy)
   {
      this.vx = vx;
      this.vy = vy;
   }
}