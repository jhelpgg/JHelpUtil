package jhelp.util.image.raster;

import java.io.IOException;
import java.io.InputStream;

import jhelp.util.gui.JHelpImage;
import jhelp.util.io.UtilIO;

/**
 * Raster image defined in 32 bits
 * 
 * @author JHelp
 */
public class Image32Bit
      implements RasterImage
{
   /** Raster image data */
   private final int[] data;
   /** Raster image height */
   private final int   height;
   /** Raster image width */
   private final int   width;

   /**
    * Create a new instance of Image32Bit
    * 
    * @param width
    *           Width
    * @param height
    *           Height
    */
   public Image32Bit(final int width, final int height)
   {
      if((width < 1) || (height < 1))
      {
         throw new IllegalArgumentException("width and height MUST be >0, but specified dimension was " + width + "x" + height);
      }

      this.width = width;
      this.height = height;
      this.data = new int[this.width * this.height];
   }

   /**
    * Clear the raster image <br>
    * <br>
    * <b>Parent documentation:</b><br>
    * {@inheritDoc}
    * 
    * @see jhelp.util.image.raster.RasterImage#clear()
    */
   @Override
   public void clear()
   {
      for(int i = this.data.length - 1; i >= 0; i--)
      {
         this.data[i] = (byte) 0;
      }
   }

   /**
    * Obtain a color at given coordinate
    * 
    * @param x
    *           X
    * @param y
    *           Y
    * @return Color at given coordinate
    */
   public int getColor(final int x, final int y)
   {
      if((x < 0) || (x >= this.width) || (y < 0) || (y >= this.height))
      {
         throw new IllegalArgumentException("x must be in [0, " + this.width + "[ and y in [0, " + this.height + "[ but specified point (" + x + ", " + y + ")");
      }

      return this.data[x + (y * this.width)];
   }

   /**
    * Raster image height <br>
    * <br>
    * <b>Parent documentation:</b><br>
    * {@inheritDoc}
    * 
    * @return Raster image height
    * @see jhelp.util.list.SizedObject#getHeight()
    */
   @Override
   public int getHeight()
   {
      return this.height;
   }

   /**
    * Raster image type <br>
    * <br>
    * <b>Parent documentation:</b><br>
    * {@inheritDoc}
    * 
    * @return Raster image type
    * @see jhelp.util.image.raster.RasterImage#getImageType()
    */
   @Override
   public RasterImageType getImageType()
   {
      return RasterImageType.IMAGE_32_BITS;
   }

   /**
    * Raster image width <br>
    * <br>
    * <b>Parent documentation:</b><br>
    * {@inheritDoc}
    * 
    * @return Raster image width
    * @see jhelp.util.list.SizedObject#getWidth()
    */
   @Override
   public int getWidth()
   {
      return this.width;
   }

   /**
    * Parse bitmap stream to image data
    * 
    * @param inputStream
    *           Stream to parse
    * @throws IOException
    *            On reading issue
    */
   public void parseBitmapStream(final InputStream inputStream) throws IOException
   {
      this.clear();
      final byte[] buffer = new byte[4];
      int y = this.height - 1;
      int x, line;
      while(y >= 0)
      {
         line = y * this.width;
         x = 0;

         while(x < this.width)
         {
            UtilIO.readStream(inputStream, buffer);
            this.data[x + line] = (((buffer[3] & 0xFF)) << 24) | ((buffer[2] & 0xFF) << 16) | ((buffer[1] & 0xFF) << 12) | (buffer[0] & 0xFF);
            x++;
         }

         y--;
      }
   }

   /**
    * Change an color of image
    * 
    * @param x
    *           X
    * @param y
    *           Y
    * @param color
    *           New color
    */
   public void setColor(final int x, final int y, final int color)
   {
      if((x < 0) || (x >= this.width) || (y < 0) || (y >= this.height))
      {
         throw new IllegalArgumentException("x must be in [0, " + this.width + "[ and y in [0, " + this.height + "[ but specified point (" + x + ", " + y + ")");
      }

      this.data[x + (y * this.width)] = color;
   }

   /**
    * Convert raster image to JHelpImage <br>
    * <br>
    * <b>Parent documentation:</b><br>
    * {@inheritDoc}
    * 
    * @return Converted image
    * @see jhelp.util.image.raster.RasterImage#toJHelpImage()
    */
   @Override
   public JHelpImage toJHelpImage()
   {
      return new JHelpImage(this.width, this.height, this.data);
   }
}