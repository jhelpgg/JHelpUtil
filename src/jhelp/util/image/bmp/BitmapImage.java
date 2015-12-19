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
package jhelp.util.image.bmp;

import java.awt.Dimension;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import jhelp.util.debug.Debug;
import jhelp.util.gui.JHelpImage;
import jhelp.util.image.raster.BinaryImage;
import jhelp.util.image.raster.Image16Bit;
import jhelp.util.image.raster.Image24Bit;
import jhelp.util.image.raster.Image32Bit;
import jhelp.util.image.raster.Image4Bit;
import jhelp.util.image.raster.Image8Bit;
import jhelp.util.image.raster.RasterImage;
import jhelp.util.image.raster.RasterImageType;

/**
 * Bitmap image
 * 
 * @author JHelp
 */
public class BitmapImage
{
   /**
    * Compute bitmap image size from file
    * 
    * @param file
    *           File to parse
    * @return Bitmap size OR {@code null} if file not a valid bitmap
    */
   public static Dimension computeBitmapDimension(final File file)
   {
      final BitmapHeader bitmapHeader = BitmapImage.obtainBitmapHeader(file);

      if(bitmapHeader == null)
      {
         return null;
      }

      return new Dimension(bitmapHeader.getWidth(), bitmapHeader.getHeight());
   }

   /**
    * Indicates if given file is a bitmap
    * 
    * @param file
    *           File to test
    * @return {@code true} if given file is a bitmap
    */
   public static boolean isBitmap(final File file)
   {
      return BitmapImage.obtainBitmapHeader(file) != null;
   }

   /**
    * Load a file as bitmap
    * 
    * @param file
    *           File to load
    * @return Read bitmap or {@code null} if file not valid bitmap
    */
   public static BitmapImage load(final File file)
   {
      if((file == null) || (file.exists() == false) || (file.isDirectory() == true) || (file.canRead() == false))
      {
         return null;
      }

      InputStream inputStream = null;

      try
      {
         inputStream = new FileInputStream(file);
         return BitmapImage.parse(inputStream);
      }
      catch(final Exception exception)
      {
         Debug.printException(exception, "Failed to read as bitmap : ", file.getAbsolutePath());
         return null;
      }
      finally
      {
         if(inputStream != null)
         {
            try
            {
               inputStream.close();
            }
            catch(final Exception exception)
            {
            }
         }
      }
   }

   /**
    * Read complete bitmap header from a file
    * 
    * @param file
    *           File to read
    * @return Bitmap header OR {@code null} if file not valid bitmap
    */
   public static BitmapHeader obtainBitmapHeader(final File file)
   {
      if((file == null) || (file.exists() == false) || (file.isDirectory() == true) || (file.canRead() == false))
      {
         return null;
      }

      InputStream inputStream = null;

      try
      {
         inputStream = new FileInputStream(file);
         return BitmapImage.readBitmapHeader(inputStream);
      }
      catch(final Exception exception)
      {
         Debug.printException(exception, "Failed to read as bitmap : ", file.getAbsolutePath());
         return null;
      }
      finally
      {
         if(inputStream != null)
         {
            try
            {
               inputStream.close();
            }
            catch(final Exception exception)
            {
            }
         }
      }
   }

   /**
    * Parse stream to bitmap.<br>
    * Stream not close by this method
    * 
    * @param inputStream
    *           Stream to parse
    * @return Bitmap read
    * @throws IOException
    *            If stream not contains a valid bitmap at its current read position
    */
   public static BitmapImage parse(final InputStream inputStream) throws IOException
   {
      final BitmapHeader bitmapHeader = BitmapImage.readBitmapHeader(inputStream);
      RasterImage rasterImage = null;
      final int width = bitmapHeader.getWidth();
      final int heigth = bitmapHeader.getHeight();
      final RasterImageType rasterImageType = bitmapHeader.getRasterImageType();
      final boolean compressed = bitmapHeader.isCompressed();

      switch(rasterImageType)
      {
         case IMAGE_BINARY:
            rasterImage = new BinaryImage(width, heigth);
            ((BinaryImage) rasterImage).parseBitmapStream(inputStream);
            bitmapHeader.applyColorTable(rasterImage);
         break;
         case IMAGE_4_BITS:
            rasterImage = new Image4Bit(width, heigth);

            if(compressed == true)
            {
               ((Image4Bit) rasterImage).parseBitmapStreamCompressed(inputStream);
            }
            else
            {
               ((Image4Bit) rasterImage).parseBitmapStream(inputStream);
            }

            bitmapHeader.applyColorTable(rasterImage);
         break;
         case IMAGE_8_BITS:
            rasterImage = new Image8Bit(width, heigth);

            if(compressed == true)
            {
               ((Image8Bit) rasterImage).parseBitmapStreamCompressed(inputStream);
            }
            else
            {
               ((Image8Bit) rasterImage).parseBitmapStream(inputStream);
            }

            bitmapHeader.applyColorTable(rasterImage);
         break;
         case IMAGE_16_BITS:
            rasterImage = new Image16Bit(width, heigth);
            ((Image16Bit) rasterImage).parseBitmapStream(inputStream);
         break;
         case IMAGE_24_BITS:
            rasterImage = new Image24Bit(width, heigth);
            ((Image24Bit) rasterImage).parseBitmapStream(inputStream);
         break;
         case IMAGE_32_BITS:
            rasterImage = new Image32Bit(width, heigth);
            ((Image32Bit) rasterImage).parseBitmapStream(inputStream);
         break;
         default:
            throw new RuntimeException("Shouldn't arrive there : rasterImageType=" + rasterImageType);
      }

      return new BitmapImage(bitmapHeader, rasterImage);
   }

   /**
    * Read complete bitmap header from stream.<br>
    * Stream not close by this method
    * 
    * @param inputStream
    *           Stream to read
    * @return Bitmap header
    * @throws IOException
    *            On reading issue
    */
   public static BitmapHeader readBitmapHeader(final InputStream inputStream) throws IOException
   {
      return BitmapImage.readBitmapHeader(inputStream, false);
   }

   /**
    * Read complete or partial bitmap header from stream.<br>
    * Stream not close by this method
    * 
    * @param inputStream
    *           Stream to read
    * @param jumpHeader
    *           Indicates if have to read to first part ({@code false}) of header (so all header read) OR not read the first
    *           part of header ({@code true}) missing information complete by an other way
    * @return Bitmap header
    * @throws IOException
    *            On reading issue
    */
   public static BitmapHeader readBitmapHeader(final InputStream inputStream, final boolean jumpHeader) throws IOException
   {
      return new BitmapHeader(inputStream, jumpHeader);
   }

   /** Bitmap header */
   private final BitmapHeader bitmapHeader;
   /** Image height */
   private final int          height;
   /** Embed raster image */
   private final RasterImage  rasterImage;
   /** Image width */
   private final int          width;

   /**
    * Create a new instance of BitmapImage
    * 
    * @param bitmapHeader
    *           Bitmap header
    * @param rasterImage
    *           Raster image
    */
   private BitmapImage(final BitmapHeader bitmapHeader, final RasterImage rasterImage)
   {
      this.bitmapHeader = bitmapHeader;
      this.rasterImage = rasterImage;
      this.width = this.rasterImage.getWidth();
      this.height = this.rasterImage.getHeight();
   }

   /**
    * Bitmap header
    * 
    * @return Bitmap header
    */
   public BitmapHeader getBitmapHeader()
   {
      return this.bitmapHeader;
   }

   /**
    * Image height
    * 
    * @return Image height
    */
   public int getHeight()
   {
      return this.height;
   }

   /**
    * Bitmap header
    * 
    * @return Bitmap header
    */
   public RasterImage getRasterImage()
   {
      return this.rasterImage;
   }

   /**
    * Image width
    * 
    * @return Image width
    */
   public int getWitdh()
   {
      return this.width;
   }

   /**
    * Convert to JHelp image
    * 
    * @return Converted image
    */
   public JHelpImage toJHelpImage()
   {
      return this.rasterImage.toJHelpImage();
   }
}