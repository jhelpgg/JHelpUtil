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
package jhelp.util.image.ico;

import java.io.IOException;
import java.io.InputStream;

import jhelp.util.gui.JHelpImage;
import jhelp.util.gui.PixelCombination;
import jhelp.util.image.bmp.BitmapHeader;
import jhelp.util.image.bmp.BitmapImage;
import jhelp.util.image.raster.BinaryImage;
import jhelp.util.image.raster.Image16Bit;
import jhelp.util.image.raster.Image24Bit;
import jhelp.util.image.raster.Image32Bit;
import jhelp.util.image.raster.Image4Bit;
import jhelp.util.image.raster.Image8Bit;
import jhelp.util.image.raster.RasterImage;
import jhelp.util.image.raster.RasterImageType;

/**
 * Ico element image
 * 
 * @author JHelp
 */
public class IcoElementImage
{
   /** Height */
   private final int         height;
   /** AND image */
   private JHelpImage        imageAnd;
   /** XOR image */
   private JHelpImage        imageXor;
   /** AND raster image */
   private final BinaryImage rasterAnd;
   /** XOR raster image */
   private final RasterImage rasterXor;
   /** Width */
   private final int         width;

   /**
    * Create a new instance of IcoElementImage
    * 
    * @param inputStream
    *           Stream to parse
    * @throws IOException
    *            On reading issue
    */
   public IcoElementImage(final InputStream inputStream)
         throws IOException
   {
      this(inputStream, null);
   }

   /**
    * Create a new instance of IcoElementImage
    * 
    * @param inputStream
    *           Stream to parse
    * @param rasterImageType
    *           Raster image type
    * @throws IOException
    *            On reading issue
    */
   public IcoElementImage(final InputStream inputStream, RasterImageType rasterImageType)
         throws IOException
   {
      final BitmapHeader bitmapHeader = BitmapImage.readBitmapHeader(inputStream, true);
      this.width = bitmapHeader.getWidth();
      this.height = bitmapHeader.getHeight() >> 1;

      RasterImage xor = null;

      if(rasterImageType == null)
      {
         rasterImageType = bitmapHeader.getRasterImageType();
      }

      switch(rasterImageType)
      {
         case IMAGE_BINARY:
            xor = new BinaryImage(this.width, this.height);
            ((BinaryImage) xor).parseBitmapStream(inputStream);
         break;
         case IMAGE_4_BITS:
            xor = new Image4Bit(this.width, this.height);

            if(bitmapHeader.isCompressed())
            {
               ((Image4Bit) xor).parseBitmapStreamCompressed(inputStream);
            }
            else
            {
               ((Image4Bit) xor).parseBitmapStream(inputStream);
            }

         break;
         case IMAGE_8_BITS:
            xor = new Image8Bit(this.width, this.height);

            if(bitmapHeader.isCompressed())
            {
               ((Image8Bit) xor).parseBitmapStreamCompressed(inputStream);
            }
            else
            {
               ((Image8Bit) xor).parseBitmapStream(inputStream);
            }

         break;
         case IMAGE_16_BITS:
            xor = new Image16Bit(this.width, this.height);
            ((Image16Bit) xor).parseBitmapStream(inputStream);
         break;
         case IMAGE_24_BITS:
            xor = new Image24Bit(this.width, this.height);
            ((Image24Bit) xor).parseBitmapStream(inputStream);
         break;
         case IMAGE_32_BITS:
            xor = new Image32Bit(this.width, this.height);
            ((Image32Bit) xor).parseBitmapStream(inputStream);
         break;
         default:
         break;
      }

      this.rasterXor = xor;
      bitmapHeader.applyColorTable(this.rasterXor);

      this.rasterAnd = new BinaryImage(this.width, this.height);
      bitmapHeader.applyColorTable(this.rasterAnd);
      this.rasterAnd.parseBitmapStream(inputStream);
   }

   /**
    * Draw ico element on image
    * 
    * @param parent
    *           Image where draw
    * @param x
    *           X
    * @param y
    *           Y
    */
   public void draw(final JHelpImage parent, final int x, final int y)
   {
      if(this.imageXor == null)
      {
         this.imageXor = this.rasterXor.toJHelpImage();
      }

      if(this.rasterXor.getImageType() == RasterImageType.IMAGE_32_BITS)
      {
         parent.drawImage(x, y, this.imageXor);
      }
      else
      {
         if(this.imageAnd == null)
         {
            this.imageAnd = this.rasterAnd.toJHelpImage();
         }

         parent.drawImage(x, y, this.imageAnd, PixelCombination.AND);
         parent.drawImage(x, y, this.imageXor, PixelCombination.XOR);
      }
   }

   /**
    * Height
    * 
    * @return Height
    */
   public int getHeight()
   {
      return this.height;
   }

   /**
    * Width
    * 
    * @return Width
    */
   public int getWidth()
   {
      return this.width;
   }
}