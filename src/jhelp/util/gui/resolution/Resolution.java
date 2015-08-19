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
}