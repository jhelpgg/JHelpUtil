package jhelp.util.gui;

import jhelp.util.math.UtilMath;

/**
 * Gradient that change color influence if we are far or near a given line.<br>
 * More a point is near the line, more it is the given color<br>
 * More we are far, more the color used is transparent
 * 
 * @author JHelp
 */
public class JHelpGradientLightLine
      implements JHelpPaint
{
   /** a² (From aX+bY+c=0) */
   private final double aa;
   /** a*b (From aX+bY+c=0) */
   private final double ab;
   /** a*c (From aX+bY+c=0) */
   private final double ac;
   /** alpha to use on the line */
   private final int    alpha;
   /** Attenuation factor for accelerate the become transparent */
   private final int    attenuation;
   /** b² (From aX+bY+c=0) */
   private final double bb;
   /** b*c (From aX+bY+c=0) */
   private final double bc;

   /** Blue to use on the line */
   private final int    blue;
   /** a²+b² (From aX+bY+c=0) */
   private final double divisor;
   /** Green to use on the line */
   private final int    green;
   /** Red to use on the line */
   private final int    red;

   /**
    * Create a new instance of JHelpGradientAlphaLine.<br>
    * Line is define by 2 points
    * 
    * @param x1
    *           First point x
    * @param y1
    *           First point y
    * @param x2
    *           Second point x
    * @param y2
    *           Second point y
    * @param color
    *           Color to use
    * @param attenuation
    *           Attenuation factor to accelerate the got transparent
    */
   public JHelpGradientLightLine(final int x1, final int y1, final int x2, final int y2, final int color, final int attenuation)
   {
      if((x1 == x2) && (y1 == y2))
      {
         throw new IllegalArgumentException("The given points must be different !");
      }

      this.alpha = (color >> 24) & 0xFF;
      this.red = (color >> 16) & 0xFF;
      this.green = (color >> 8) & 0xFF;
      this.blue = color & 0xFF;
      this.attenuation = Math.max(1, attenuation);

      // ax+by+c=0
      double a, b, c;

      if(x1 == x2)
      {
         a = 1d;
         b = 0d;
         c = -(double) x1;
      }
      else
      {
         a = (double) (y2 - y1) / (double) (x2 - x1);
         b = -1d;
         c = y1 - ((double) (x1 * (y2 - y1)) / (double) (x2 - x1));
      }

      this.ab = a * b;
      this.ac = a * c;
      this.bc = b * c;
      this.aa = a * a;
      this.bb = b * b;
      this.divisor = this.aa + this.bb;
   }

   /**
    * Called when paint initialized before be used <br>
    * <br>
    * <b>Parent documentation:</b><br>
    * {@inheritDoc}
    * 
    * @param width
    *           Area width
    * @param height
    *           Area height
    * @see jhelp.util.gui.JHelpPaint#initializePaint(int, int)
    */
   @Override
   public void initializePaint(final int width, final int height)
   {
   }

   /**
    * Obtain a color <br>
    * <br>
    * <b>Parent documentation:</b><br>
    * {@inheritDoc}
    * 
    * @param x
    *           X
    * @param y
    *           Y
    * @return Computed color
    * @see jhelp.util.gui.JHelpPaint#obtainColor(int, int)
    */
   @Override
   public int obtainColor(final int x, final int y)
   {
      final double xx = ((this.bb * x) - (this.ab * y) - this.ac) / this.divisor;
      final double yy = ((this.aa * y) - (this.ab * x) - this.bc) / this.divisor;
      final double dist = Math.sqrt(UtilMath.square(x - xx) + UtilMath.square(y - yy));
      final int minus = (int) (dist * this.attenuation);
      return (Math.max(0, this.alpha - minus) << 24//
            )
            | (Math.max(0, this.red - minus) << 16//
            ) | (Math.max(0, this.green - minus) << 8//
            ) | Math.max(0, this.blue - minus);
   }
}