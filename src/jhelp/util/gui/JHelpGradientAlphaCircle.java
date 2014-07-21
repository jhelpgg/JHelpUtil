package jhelp.util.gui;

import jhelp.util.math.UtilMath;

/**
 * Gradient on circle.<br>
 * More we are near the center, more the given color is present.<br>
 * More we are far from the center, more the color become transparent
 * 
 * @author JHelp
 */
public class JHelpGradientAlphaCircle
      implements JHelpPaint
{
   /** Multiplier that give a normal impact */
   public static final double MULTIPLIER_NORMAL     = 1;
   /** Multiplier that give a thick impact */
   public static final double MULTIPLIER_THICK      = 1.5;
   /** Multiplier that give a thin impact */
   public static final double MULTIPLIER_THIN       = 0.75;
   /** Multiplier that give a very thick impact */
   public static final double MULTIPLIER_VERY_THICK = 2;
   /** Multiplier that give a very thin impact */
   public static final double MULTIPLIER_VERY_THIN  = 0.5;
   /** Color maximum alpha (At center) */
   private final int          alpha;
   /** Color part (without alpha) */
   private final int          colorPart;
   /** Center X */
   private double             cx;
   /** Center Y */
   private double             cy;
   /** Factor used for compute */
   private double             factor;
   /** Multiplier for modify impact */
   private final double       multiplier;

   /**
    * Create a new instance of JHelpGradientAlphaCircle
    * 
    * @param color
    *           Color on center
    * @param multiplier
    *           Multiplier for modify impact
    */
   public JHelpGradientAlphaCircle(final int color, final double multiplier)
   {
      this.alpha = (color >> 24) & 0xFF;
      this.colorPart = color & 0x00FFFFFF;
      this.multiplier = multiplier;
   }

   /**
    * Called when paint is initialized <br>
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
      this.cx = width / 2d;
      this.cy = height / 2d;
      final double ray = this.multiplier * Math.max(width, height);
      this.factor = -this.alpha / ray;
   }

   /**
    * Obtain a color during painting <br>
    * <br>
    * <b>Parent documentation:</b><br>
    * {@inheritDoc}
    * 
    * @param x
    *           X
    * @param y
    *           Y
    * @return Color
    * @see jhelp.util.gui.JHelpPaint#obtainColor(int, int)
    */
   @Override
   public int obtainColor(final int x, final int y)
   {
      final double dist = Math.sqrt(UtilMath.square(this.cx - x) + UtilMath.square(this.cy - y));
      final int alpha = Math.min(this.alpha, Math.max(0, (int) ((this.factor * dist) + this.alpha)));
      return (alpha << 24) | this.colorPart;
   }
}