package jhelp.util.image.bmp;

import java.io.IOException;
import java.io.InputStream;

import jhelp.util.image.raster.BinaryImage;
import jhelp.util.image.raster.Image4Bit;
import jhelp.util.image.raster.Image8Bit;
import jhelp.util.image.raster.RasterImage;
import jhelp.util.image.raster.RasterImageType;
import jhelp.util.io.UtilIO;

/**
 * Bitmap header
 * 
 * @author JHelp
 */
public class BitmapHeader
{
   /** Buffer of 2 bytes */
   private final static byte[] BUFFER_2      = new byte[2];
   /** Buffer of 4 bytes */
   private final static byte[] BUFFER_4      = new byte[4];
   /** Bitmap signature */
   private static final int    SIGNATURE     = (('B' & 0xFF) << 8) | ('M' & 0xFF);
   /** Image 16 bits type */
   public static final int     IMAGE_16_BITS = 16;
   /** Image 24 bits type */
   public static final int     IMAGE_24_BITS = 24;
   /** Image 32 bits type */
   public static final int     IMAGE_32_BITS = 32;
   /** Image 4 bits type */
   public static final int     IMAGE_4_BITS  = 4;
   /** Image 8 bits type */
   public static final int     IMAGE_8_BITS  = 8;
   /** Image 1 bit type */
   public static final int     IMAGE_BINARY  = 1;

   /**
    * Read 2 bytes integer from stream
    * 
    * @param inputStream
    *           Stream to read
    * @return Integer read
    * @throws IOException
    *            On reading issue
    */
   public static int read2bytes(final InputStream inputStream) throws IOException
   {
      UtilIO.readStream(inputStream, BitmapHeader.BUFFER_2);
      return ((BitmapHeader.BUFFER_2[1] & 0xFF) << 8) | (BitmapHeader.BUFFER_2[0] & 0xFF);
   }

   /**
    * Read 4 bytes integer from stream
    * 
    * @param inputStream
    *           Stream to read
    * @return Integer read
    * @throws IOException
    *            On reading issue
    */
   public static int read4bytes(final InputStream inputStream) throws IOException
   {
      UtilIO.readStream(inputStream, BitmapHeader.BUFFER_4);
      return ((BitmapHeader.BUFFER_4[3] & 0xFF) << 24) | ((BitmapHeader.BUFFER_4[2] & 0xFF) << 16) | ((BitmapHeader.BUFFER_4[1] & 0xFF) << 8)
            | (BitmapHeader.BUFFER_4[0] & 0xFF);
   }

   /** Color table */
   private final int[]     colorTable;
   /** Indicates if data are compressed */
   private boolean         compressed;
   /** File size */
   private final int       fileSize;
   /** Height */
   private final int       height;
   /** Number color used */
   private final int       numberColorUsed;
   /** Number of important colors */
   private final int       numberImportantColors;
   /** Pixels per meter in X */
   private final int       pixelsPerMeterX;
   /** Pixels per meter in Y */
   private final int       pixelsPerMeterY;
   /** Raster data offset */
   private final int       rasterDataOffset;
   /** Raster image type */
   private RasterImageType rasterImageType;
   /** Width */
   private final int       width;

   /**
    * Create a new instance of BitmapHeader
    * 
    * @param inputStream
    *           Stream to parse
    * @param jumpHeader
    *           {@code true} for nor read first part of header. {@code false} for full read
    * @throws IOException
    *            On reading issue
    */
   BitmapHeader(final InputStream inputStream, final boolean jumpHeader)
         throws IOException
   {
      int info;

      if(jumpHeader == false)
      {
         // *** Header ***

         info = BitmapHeader.read2bytes(inputStream);

         if(info != BitmapHeader.SIGNATURE)
         {
            throw new IOException("Wrong signature : " + info);
         }

         this.fileSize = BitmapHeader.read4bytes(inputStream);
         // Unused: test if 0 ?
         info = BitmapHeader.read4bytes(inputStream);
         this.rasterDataOffset = BitmapHeader.read4bytes(inputStream);
      }
      else
      {
         this.fileSize = 0;
         this.rasterDataOffset = 0;
      }

      // *** Information header ***

      info = BitmapHeader.read4bytes(inputStream);

      if(info != 40)
      {
         throw new IOException("Wrong information size MUST be 40  but : " + info);
      }

      this.width = BitmapHeader.read4bytes(inputStream);
      this.height = BitmapHeader.read4bytes(inputStream);

      if((this.width <= 0) || (this.height <= 0))
      {
         throw new IOException("width and height MUST be >0  here read size : " + this.width + "x" + this.height);
      }

      info = BitmapHeader.read2bytes(inputStream);

      if(info != 1)
      {
         throw new IOException("Number of plane MUST be 1, not " + info);
      }

      info = BitmapHeader.read2bytes(inputStream);

      switch(info)
      {
         case IMAGE_BINARY:
            this.rasterImageType = RasterImageType.IMAGE_BINARY;
         break;
         case IMAGE_4_BITS:
            this.rasterImageType = RasterImageType.IMAGE_4_BITS;
         break;
         case IMAGE_8_BITS:
            this.rasterImageType = RasterImageType.IMAGE_8_BITS;
         break;
         case IMAGE_16_BITS:
            this.rasterImageType = RasterImageType.IMAGE_16_BITS;
         break;
         case IMAGE_24_BITS:
            this.rasterImageType = RasterImageType.IMAGE_24_BITS;
         break;
         case IMAGE_32_BITS:
            this.rasterImageType = RasterImageType.IMAGE_32_BITS;
         break;
         default:
            throw new IOException("Number of bits MUST be 1, 4, 8, 16, 24 or 32 not " + info);
      }

      info = BitmapHeader.read4bytes(inputStream);

      switch(info)
      {
         case 0:
            this.compressed = false;
         break;
         case 1:
            if(this.rasterImageType != RasterImageType.IMAGE_8_BITS)
            {
               throw new IOException("Compression 1 can be used only with 8 bits image not " + this.rasterImageType);
            }

            this.compressed = true;
         break;
         case 2:
            if(this.rasterImageType != RasterImageType.IMAGE_4_BITS)
            {
               throw new IOException("Compression 2 can be used only with 4 bits image not " + this.rasterImageType);
            }

            this.compressed = true;
         break;
         default:
            throw new IOException("Compression MUST be 0, 1 or 2 not " + info);
      }

      // Ignored
      info = BitmapHeader.read4bytes(inputStream);
      this.pixelsPerMeterX = BitmapHeader.read4bytes(inputStream);
      this.pixelsPerMeterY = BitmapHeader.read4bytes(inputStream);
      this.numberColorUsed = BitmapHeader.read4bytes(inputStream);
      this.numberImportantColors = BitmapHeader.read4bytes(inputStream);

      int numColors = 0;

      switch(this.rasterImageType)
      {
         case IMAGE_BINARY:
            numColors = 2;
         break;
         case IMAGE_4_BITS:
            numColors = 16;
         break;
         case IMAGE_8_BITS:
            numColors = 256;
         break;
      }

      this.colorTable = new int[numColors];
      int red, green, blue;

      for(int i = 0; i < numColors; i++)
      {
         red = inputStream.read();
         green = inputStream.read();
         blue = inputStream.read();
         // Just throw away this one
         inputStream.read();

         this.colorTable[i] = 0xFF000000 | (red << 16) | (green << 8) | blue;
      }
   }

   /**
    * Apply color table to given raster image
    * 
    * @param rasterImage
    *           Raster image to apply color table
    */
   public void applyColorTable(final RasterImage rasterImage)
   {
      if(this.colorTable.length == 0)
      {
         return;
      }

      switch(rasterImage.getImageType())
      {
         case IMAGE_BINARY:
            final BinaryImage binaryImage = (BinaryImage) rasterImage;
            binaryImage.setBackground(this.colorTable[0]);
            binaryImage.setForeground(this.colorTable[1]);
         break;
         case IMAGE_4_BITS:
            ((Image4Bit) rasterImage).setColors(0, this.colorTable);
         break;
         case IMAGE_8_BITS:
            ((Image8Bit) rasterImage).setColors(0, this.colorTable);
         break;
      }
   }

   /**
    * File size
    * 
    * @return File size
    */
   public int getFileSize()
   {
      return this.fileSize;
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
    * Number color used
    * 
    * @return Number color used
    */
   public int getNumberColorUsed()
   {
      return this.numberColorUsed;
   }

   /**
    * Number of important colors
    * 
    * @return Number of important colors
    */
   public int getNumberImportantColors()
   {
      return this.numberImportantColors;
   }

   /**
    * Pixels per meter in X
    * 
    * @return Pixels per meter in X
    */
   public int getPixelsPerMeterX()
   {
      return this.pixelsPerMeterX;
   }

   /**
    * Pixels per meter in Y
    * 
    * @return Pixels per meter in Y
    */
   public int getPixelsPerMeterY()
   {
      return this.pixelsPerMeterY;
   }

   /**
    * Raster data offset
    * 
    * @return Raster data offset
    */
   public int getRasterDataOffset()
   {
      return this.rasterDataOffset;
   }

   /**
    * Raster image type
    * 
    * @return Raster image type
    */
   public RasterImageType getRasterImageType()
   {
      return this.rasterImageType;
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

   /**
    * Indicates if data are compressed
    * 
    * @return {@code true} if data are compressed
    */
   public boolean isCompressed()
   {
      return this.compressed;
   }
}