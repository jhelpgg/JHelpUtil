package jhelp.util.image.raster;

import java.io.IOException;
import java.io.InputStream;

import jhelp.util.gui.JHelpImage;
import jhelp.util.io.UtilIO;

/**
 * Raster 24 bits image
 * 
 * @author JHelp
 */
public class Image24Bit
      implements RasterImage
{
   /** Image data */
   private final int[] data;
   /** Image height */
   private final int   height;
   /** Image width */
   private final int   width;

   /**
    * Create a new instance of Image24Bit
    * 
    * @param width
    *           Width
    * @param height
    *           Height
    */
   public Image24Bit(final int width, final int height)
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
    * Clear the image <br>
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
    * Obtain one pixel color
    * 
    * @param x
    *           X
    * @param y
    *           Y
    * @return Pixel color
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
    * Image height <br>
    * <br>
    * <b>Parent documentation:</b><br>
    * {@inheritDoc}
    * 
    * @return Image height
    * @see jhelp.util.list.SizedObject#getHeight()
    */
   @Override
   public int getHeight()
   {
      return this.height;
   }

   /**
    * Image type <br>
    * <br>
    * <b>Parent documentation:</b><br>
    * {@inheritDoc}
    * 
    * @return Image type
    * @see jhelp.util.image.raster.RasterImage#getImageType()
    */
   @Override
   public RasterImageType getImageType()
   {
      return RasterImageType.IMAGE_24_BITS;
   }

   /**
    * Image width <br>
    * <br>
    * <b>Parent documentation:</b><br>
    * {@inheritDoc}
    * 
    * @return Image width
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
    *           Stream to read
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
            this.data[x + line] = 0xFF000000 | ((buffer[3] & 0xFF) << 16) | ((buffer[2] & 0xFF) << 12) | (buffer[1] & 0xFF);
            x++;
         }

         y--;
      }
   }

   /**
    * Change one pixel color
    * 
    * @param x
    *           X
    * @param y
    *           Y
    * @param color
    *           Color
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
    * Convert to JHelp Image <br>
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