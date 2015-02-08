package jhelp.util.gui.dynamic;

import jhelp.util.math.UtilMath;

/**
 * Sinusoids interpolation : <br>
 * <table border=1 bgcolor=#F0F0FF>
 * <tr>
 * <td><font face="courier" color=#000000> <b>
 * 
 * <pre>
 * 
 *         1       ( 2x&pi; - &pi; )
 * f(x) = --- + sin(---------)
 *         2       (    4    )
 * </pre>
 * 
 * </b></font></td>
 * </tr>
 * </table>
 * 
 * @author JHelp
 */
public class SinusInterpolation
      implements Interpolation
{
   /** Sinusoids interpolation singleton */
   public static final SinusInterpolation SINUS_INTERPOLATION = new SinusInterpolation();

   /**
    * Create a new instance of SinusInterpolation
    */
   private SinusInterpolation()
   {
   }

   /**
    * Do sinusoids interpolation <br>
    * <br>
    * <b>Parent documentation:</b><br>
    * {@inheritDoc}
    * 
    * @param percent
    *           To interpolate
    * @return Interpolated
    * @see jhelp.util.gui.dynamic.Interpolation#interpolation(float)
    */
   @Override
   public float interpolation(final float percent)
   {
      return (float) UtilMath.interpolationSinus(percent);
   }
}