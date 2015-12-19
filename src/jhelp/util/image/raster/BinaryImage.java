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
package jhelp.util.image.raster;

import java.io.IOException;
import java.io.InputStream;

import jhelp.util.gui.JHelpImage;
import jhelp.util.io.UtilIO;

/**
 * Raster binary image
 * 
 * @author JHelp
 */
public class BinaryImage
      implements RasterImage
{
   /** Background color (Color for 0) */
   private int          background;
   /** Image data */
   private final byte[] data;
   /** Foreground color (Color for 1) */
   private int          foreground;
   /** Height */
   private final int    height;
   /** Width */
   private final int    width;

   /**
    * Create a new instance of BinaryImage
    * 
    * @param width
    *           Width
    * @param height
    *           Height
    */
   public BinaryImage(final int width, final int height)
   {
      if((width < 1) || (height < 1))
      {
         throw new IllegalArgumentException("width and height MUST be >0, but specified dimension was " + width + "x" + height);
      }

      this.width = width;
      this.height = height;
      this.foreground = 0xFFFFFFFF;
      this.background = 0xFF000000;
      final int mult = this.width * this.height;
      int length = mult >> 3;

      if((mult & 7) != 0)
      {
         length++;
      }

      this.data = new byte[length];
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
    * Background color / Color for 0
    * 
    * @return Background color / Color for 0
    */
   public int getBackground()
   {
      return this.background;
   }

   /**
    * Foreground color / Color for 1
    * 
    * @return Foreground color / Color for 1
    */
   public int getForeground()
   {
      return this.foreground;
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
      return RasterImageType.IMAGE_BINARY;
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
    * Indicates if image pixel bit is active or not
    * 
    * @param x
    *           X
    * @param y
    *           Y
    * @return {@code true} if image pixel bit is active or not
    */
   public boolean isOn(final int x, final int y)
   {
      if((x < 0) || (x >= this.width) || (y < 0) || (y >= this.height))
      {
         throw new IllegalArgumentException("x must be in [0, " + this.width + "[ and y in [0, " + this.height + "[ but specified point (" + x + ", " + y + ")");
      }

      final int p = x + (y * this.width);
      final int pix = p >> 3;
      final int mask = 1 << (p & 7);
      return (this.data[pix] & mask) != 0;
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
      int index, maskRead;
      int x, line, pix;

      while(y >= 0)
      {
         line = y * this.width;
         x = 0;

         while(x < this.width)
         {
            UtilIO.readStream(inputStream, buffer);
            index = 0;
            maskRead = 1 << 7;

            while((index < 4) && (x < this.width))
            {
               if((buffer[index] & maskRead) != 0)
               {
                  pix = x + line;
                  this.data[pix >> 3] |= 1 << (pix & 7);
               }

               x++;
               maskRead >>= 1;

               if(maskRead == 0)
               {
                  maskRead = 1 << 7;
                  index++;
               }
            }
         }

         y--;
      }
   }

   /**
    * Change "background" color (color for 0)
    * 
    * @param background
    *           New "background" color (color for 0)
    */
   public void setBackground(final int background)
   {
      this.background = background;
   }

   /**
    * Change "foreground" color (color for 1)
    * 
    * @param foreground
    *           New "foreground" color (color for 1)
    */
   public void setForeground(final int foreground)
   {
      this.foreground = foreground;
   }

   /**
    * Activate/deactivate one image pixel
    * 
    * @param x
    *           X
    * @param y
    *           Y
    * @param on
    *           New active status
    */
   public void setOn(final int x, final int y, final boolean on)
   {
      if((x < 0) || (x >= this.width) || (y < 0) || (y >= this.height))
      {
         throw new IllegalArgumentException("x must be in [0, " + this.width + "[ and y in [0, " + this.height + "[ but specified point (" + x + ", " + y + ")");
      }

      final int p = x + (y * this.width);
      final int pix = p >> 3;
      final int mask = 1 << (p & 7);

      if(on == true)
      {
         this.data[pix] |= mask;
      }
      else
      {
         this.data[pix] &= ~mask;
      }
   }

   /**
    * Change activation status of one pixel
    * 
    * @param x
    *           X
    * @param y
    *           Y
    */
   public void switchOnOff(final int x, final int y)
   {
      if((x < 0) || (x >= this.width) || (y < 0) || (y >= this.height))
      {
         throw new IllegalArgumentException("x must be in [0, " + this.width + "[ and y in [0, " + this.height + "[ but specified point (" + x + ", " + y + ")");
      }

      final int p = x + (y * this.width);
      final int pix = p >> 3;
      final int mask = 1 << (p & 7);

      if((this.data[pix] & mask) == 0)
      {
         this.data[pix] |= mask;
      }
      else
      {
         this.data[pix] &= ~mask;
      }
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
      final int length = this.width * this.height;
      final int[] pixels = new int[length];
      int mask = 1 << 7;
      int pixData = 0;
      byte info = this.data[0];

      for(int pix = 0; pix < length; pix++)
      {
         if((info & mask) != 0)
         {
            pixels[pix] = this.foreground;
         }
         else
         {
            pixels[pix] = this.background;
         }

         mask >>= 1;

         if(mask == 0)
         {
            mask = 1 << 7;
            pixData++;

            if(pixData < this.data.length)
            {
               info = this.data[pixData];
            }
         }
      }

      return new JHelpImage(this.width, this.height, pixels);
   }
}