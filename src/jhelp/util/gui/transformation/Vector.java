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