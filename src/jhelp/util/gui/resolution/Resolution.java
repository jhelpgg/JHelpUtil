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
package jhelp.util.gui.resolution;

import jhelp.util.math.UtilMath;

/**
 * Screen resolution
 * 
 * @author JHelp
 */
public final class Resolution
{
   /** Number of pixels per inch */
   private final int pixelPerInch;

   /**
    * Create a new instance of Resolution
    * 
    * @param value
    *           Screen resolution
    * @param resolutionUnit
    *           Resolution unit of given value
    */
   public Resolution(final int value, final ResolutionUnit resolutionUnit)
   {
      if(resolutionUnit == null)
      {
         throw new NullPointerException("resolutionUnit musn't be null");
      }

      switch(resolutionUnit)
      {
         case PIXEL_PER_INCH:
            this.pixelPerInch = value;
         break;
         case PIXEL_PER_CENTIMETER:
            this.pixelPerInch = (int) UtilMath.centimeterToInch(value);
         break;
         default:
            throw new IllegalArgumentException("resolutionUnit not managed : " + resolutionUnit);
      }
   }

   /**
    * Obtain resolution in given resolution unit
    * 
    * @param resolutionUnit
    *           Resolution unit
    * @return Resolution in given resolution unit
    */
   public int getResolution(final ResolutionUnit resolutionUnit)
   {
      if(resolutionUnit == null)
      {
         throw new NullPointerException("resolutionUnit musn't be null");
      }

      switch(resolutionUnit)
      {
         case PIXEL_PER_INCH:
            return this.pixelPerInch;
         case PIXEL_PER_CENTIMETER:
            return (int) UtilMath.inchToCentimeter(this.pixelPerInch);
         default:
            throw new IllegalArgumentException("resolutionUnit not managed : " + resolutionUnit);
      }
   }

   /**
    * Number of pixels inside a distance
    * 
    * @param value
    *           Distance value
    * @param measureUnit
    *           Distance unit
    * @return Number of Pixels
    */
   public int numberOfPixels(double value, final MeasureUnit measureUnit)
   {
      if(measureUnit == null)
      {
         throw new NullPointerException("measureUnit musn't be null");
      }

      switch(measureUnit)
      {
         case CENTIMETER:
            value = UtilMath.centimeterToInch(value);
         break;
         case INCH:
         // Nothing to change already in good measure
         break;
         case MILLIMETER:
            value = UtilMath.millimeterToInch(value);
         break;
         case PICA:
            value = UtilMath.picaToInch(value);
         break;
         case POINT:
            value = UtilMath.pointToInch(value);
         break;
         default:
            throw new IllegalArgumentException("measureUnit not managed : " + measureUnit);
      }

      return (int) (this.pixelPerInch * value);
   }

   /**
    * Convert a number of pixels to a measure unit
    * 
    * @param pixels
    *           Number of pixels
    * @param measureUnit
    *           Measure to convert
    * @return Converted value
    */
   public double pixelsToMeasure(final double pixels, final MeasureUnit measureUnit)
   {
      final double inch = pixels / this.pixelPerInch;

      switch(measureUnit)
      {
         case CENTIMETER:
            return UtilMath.inchToCentimeter(inch);
         case INCH:
            return inch;
         case MILLIMETER:
            return UtilMath.inchToMillimeter(inch);
         case PICA:
            return UtilMath.inchToPica(inch);
         case POINT:
            return UtilMath.inchToPoint(inch);
         default:
            throw new IllegalArgumentException("measureUnit not managed : " + measureUnit);
      }
   }
}