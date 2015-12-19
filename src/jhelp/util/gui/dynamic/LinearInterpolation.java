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
 * Linear interpolation : <br>
 * <table border=1 bgcolor=#F0F0FF>
 * <tr>
 * <td><font face="courier" color=#000000> <b>
 * 
 * <pre>
 * 
 * f(x) = x
 * </pre>
 * 
 * </b></font></td>
 * </tr>
 * </table>
 * 
 * @author JHelp
 */
public class LinearInterpolation
      implements Interpolation
{
   /** Linear interpolation singleton */
   public static final LinearInterpolation LINEAR_INTERPOLATION = new LinearInterpolation();

   /**
    * Create a new instance of LinearInterpolation
    */
   private LinearInterpolation()
   {
   }

   /**
    * Interpolate a value <br>
    * <br>
    * <b>Parent documentation:</b><br>
    * {@inheritDoc}
    * 
    * @param percent
    *           Value to interpolate
    * @return Interpolated value
    * @see jhelp.util.gui.dynamic.Interpolation#interpolation(float)
    */
   @Override
   public float interpolation(final float percent)
   {
      return percent;
   }
}
