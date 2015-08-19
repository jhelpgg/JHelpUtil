package jhelp.util.image.raster;

import jhelp.util.image.bmp.BitmapHeader;

/**
 * Raster image type
 * 
 * @author JHelp
 */
public enum RasterImageType
{
   /** Raster image 16 bits */
   IMAGE_16_BITS,
   /** Raster image 24 bits */
   IMAGE_24_BITS,
   /** Raster image 32 bits */
   IMAGE_32_BITS,
   /** Raster image 4 bits */
   IMAGE_4_BITS,
   /** Raster image 8 bits */
   IMAGE_8_BITS,
   /** Raster image 2 bits / binary image */
   IMAGE_BINARY,
   /** JHelp image */
   JHELP_IMAGE;

   /**
    * Obtain raster image type by its number of bits : 2, 4, 8, 16, 24 OR 32
    * 
    * @param bitCount
    *           Number of bit
    * @return Raster image type OR {@code null} if not defined for given number of bit
    */
   public static RasterImageType getRasterImageType(final int bitCount)
   {
      switch(bitCount)
      {
         case BitmapHeader.IMAGE_BINARY:
            return IMAGE_BINARY;
         case BitmapHeader.IMAGE_4_BITS:
            return IMAGE_4_BITS;
         case BitmapHeader.IMAGE_8_BITS:
            return IMAGE_8_BITS;
         case BitmapHeader.IMAGE_16_BITS:
            return IMAGE_16_BITS;
         case BitmapHeader.IMAGE_24_BITS:
            return IMAGE_24_BITS;
         case BitmapHeader.IMAGE_32_BITS:
            return IMAGE_32_BITS;
      }

      return null;
   }
}