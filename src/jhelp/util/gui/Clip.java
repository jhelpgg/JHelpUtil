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

import jhelp.util.HashCode;
import jhelp.util.text.UtilText;

/**
 * Represents a clip.<br>
 * A clip is a limit rectangle where draw, it is not allowed to draw outside this rectangle
 * 
 * @author JHelp
 */
public class Clip
      implements Cloneable
{
   /** X maximum. The X of bottom right corner of the rectangle */
   public int xMax;
   /** X minimum. The X of up left corner of the rectangle */
   public int xMin;
   /** Y maximum. The Y of bottom right corner of the rectangle */
   public int yMax;
   /** Y minimum. The Y of up left corner of the rectangle */
   public int yMin;

   /**
    * Create a new instance of Clip "infinite" (The biggest clip ever possible)
    */
   public Clip()
   {
      this.xMin = Integer.MIN_VALUE;
      this.xMax = Integer.MAX_VALUE;
      this.yMin = Integer.MIN_VALUE;
      this.yMax = Integer.MAX_VALUE;
   }

   /**
    * Create a new instance of Clip copy od an other one
    * 
    * @param clip
    *           Clip to copy
    */
   public Clip(final Clip clip)
   {
      this.xMin = clip.xMin;
      this.xMax = clip.xMax;
      this.yMin = clip.yMin;
      this.yMax = clip.yMax;
   }

   /**
    * Create a new instance of Clip
    * 
    * @param xMin
    *           X minimum. The X of up left corner of the rectangle
    * @param xMax
    *           X maximum. The X of bottom right corner of the rectangle
    * @param yMin
    *           Y minimum. The Y of up left corner of the rectangle
    * @param yMax
    *           Y maximum. The Y of bottom right corner of the rectangle
    */
   public Clip(final int xMin, final int xMax, final int yMin, final int yMax)
   {
      this.xMin = xMin;
      this.xMax = xMax;
      this.yMin = yMin;
      this.yMax = yMax;
   }

   /**
    * Create a clone of the clip <br>
    * <br>
    * <b>Parent documentation:</b><br>
    * {@inheritDoc}
    * 
    * @return Clip clone
    * @throws CloneNotSupportedException
    *            Not throw here
    * @see java.lang.Object#clone()
    */
   @Override
   public Object clone() throws CloneNotSupportedException
   {
      final Clip clip = (Clip) super.clone();
      clip.set(this);
      return clip;
   }

   /**
    * Create a clip copy
    * 
    * @return Clip copy
    */
   public Clip copy()
   {
      return new Clip(this);
   }

   /**
    * Indicates if an object is a clip of same area <br>
    * <br>
    * <b>Parent documentation:</b><br>
    * {@inheritDoc}
    * 
    * @param object
    *           Object to compare with
    * @return {@code true} if an object is a clip of same area
    * @see java.lang.Object#equals(java.lang.Object)
    */
   @Override
   public boolean equals(final Object object)
   {
      if(object == null)
      {
         return false;
      }

      if(object == this)
      {
         return true;
      }

      if((object instanceof Clip) == false)
      {
         return false;
      }

      final Clip clip = (Clip) object;

      return (this.xMin == clip.xMin) && (this.xMax == clip.xMax) && (this.yMin == clip.yMin) && (this.yMax == clip.yMax);
   }

   /**
    * Hash code of the clip <br>
    * <br>
    * <b>Parent documentation:</b><br>
    * {@inheritDoc}
    * 
    * @return Hash code of the clip
    * @see java.lang.Object#hashCode()
    */
   @Override
   public int hashCode()
   {
      return HashCode.computeHashCode(this.xMin, this.xMax, this.yMin, this.yMax);
   }

   /**
    * Modify the clip to be a copy of an other one
    * 
    * @param clip
    *           Clip to copy
    */
   public void set(final Clip clip)
   {
      this.xMin = clip.xMin;
      this.xMax = clip.xMax;
      this.yMin = clip.yMin;
      this.yMax = clip.yMax;
   }

   /**
    * String representation <br>
    * <br>
    * <b>Parent documentation:</b><br>
    * {@inheritDoc}
    * 
    * @return String representation
    * @see java.lang.Object#toString()
    */
   @Override
   public String toString()
   {
      return UtilText.concatenate("Clip ", this.xMin, "<->", this.xMax, " | ", this.yMin, "<->", this.yMax);
   }
}