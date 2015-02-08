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
