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
 * Raster 4 bits image
 * 
 * @author JHelp
 */
public class Image4Bit
      implements RasterImage
{
   /** Default color table if none given */
   private static final int[] DEFAULT_COLOR_TABLE =
                                                  {
         0xFF000000, 0xFFFFFFFF, 0xFF0000FF, 0xFF00FF00, 0xFFFF0000, 0xFF00FFFF, 0xFFFF00FF, 0xFFFFFF00, 0xFF808080, 0xFF000080, 0xFF008000, 0xFF800000,
         0xFF80FF80, 0xFFFF80FF, 0xFFFFFF80, 0xFFFFFF80
                                                  };
   /** Color table size */
   public static final int    COLOR_TABLE_SIZE    = 16;
   /** Color table */
   private final int[]        colorTable;
   /** Image data */
   private final byte[]       data;
   /** Image height */
   private final int          height;
   /** Image width */
   private final int          width;

   /**
    * Create a new instance of Image4Bit
    * 
    * @param width
    *           Width
    * @param height
    *           Height
    */
   public Image4Bit(final int width, final int height)
   {
      if((width < 1) || (height < 1))
      {
         throw new IllegalArgumentException("width and height MUST be >0, but specified dimension was " + width + "x" + height);
      }

      this.width = width;
      this.height = height;
      this.data = new byte[((this.width * this.height) + 1) >> 1];
      this.colorTable = new int[16];
      this.toDefaultTableColor();
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
    * Obtain color from color table
    * 
    * @param colorIndex
    *           Color table index
    * @return Color
    */
   public int getColor(final int colorIndex)
   {
      return this.colorTable[colorIndex];
   }

   /**
    * Obtain color table index of image pixel
    * 
    * @param x
    *           X
    * @param y
    *           Y
    * @return Color table index
    */
   public int getColorIndex(final int x, final int y)
   {
      if((x < 0) || (x >= this.width) || (y < 0) || (y >= this.height))
      {
         throw new IllegalArgumentException("x must be in [0, " + this.width + "[ and y in [0, " + this.height + "[ but specified point (" + x + ", " + y + ")");
      }

      final int p = x + (y * this.width);
      final int info = this.data[p >> 1] & 0xFF;

      if((p & 1) == 0)
      {
         return (info >> 4) & 0xF;
      }

      return info & 0xF;
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
      return RasterImageType.IMAGE_4_BITS;
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
    *           Stream to parse
    * @throws IOException
    *            On reading issue
    */
   public void parseBitmapStream(final InputStream inputStream) throws IOException
   {
      this.clear();
      final byte[] buffer = new byte[4];
      int y = this.height - 1;
      boolean highRead;
      int x, line, pix, index, colorIndex;

      while(y >= 0)
      {
         line = y * this.width;
         x = 0;

         while(x < this.width)
         {
            UtilIO.readStream(inputStream, buffer);
            index = 0;
            highRead = true;

            while((index < 4) && (x < this.width))
            {
               if(highRead == true)
               {
                  colorIndex = (buffer[index] & 0xF0) >> 4;
                  highRead = false;
               }
               else
               {
                  colorIndex = buffer[index] & 0xF;
                  index++;
                  highRead = true;
               }

               pix = x + line;

               if((pix & 1) == 0)
               {
                  this.data[pix >> 1] |= colorIndex << 4;
               }
               else
               {
                  this.data[pix >> 1] |= colorIndex;
               }

               x++;
            }
         }

         y--;
      }
   }

   /**
    * Parse bitmap compressed stream to image data
    * 
    * @param inputStream
    *           Stream to read
    * @throws IOException
    *            On reading issue
    */
   public void parseBitmapStreamCompressed(final InputStream inputStream) throws IOException
   {
      this.clear();
      final byte[] buffer = new byte[4];
      int y = this.height - 1;
      boolean highRead;
      int x, line, pix, index, colorIndex, count, info, left, up, length, internIndex;
      byte[] internBuffer;

      while(y >= 0)
      {
         line = y * this.width;
         x = 0;

         while(x < this.width)
         {
            UtilIO.readStream(inputStream, buffer);
            index = 0;

            while((index < 4) && (x < this.width))
            {
               count = buffer[index++] & 0xFF;
               info = buffer[index++] & 0xFF;

               if(count > 0)
               {
                  highRead = true;

                  for(int i = 0; i < count; i++)
                  {
                     if(highRead == true)
                     {
                        colorIndex = info >> 4;
                        highRead = false;
                     }
                     else
                     {
                        colorIndex = info & 0xF;
                        highRead = true;
                     }

                     pix = x + line;

                     if((pix & 1) == 0)
                     {
                        this.data[pix >> 1] |= colorIndex << 4;
                     }
                     else
                     {
                        this.data[pix >> 1] |= colorIndex;
                     }

                     x++;

                     if((x >= this.width) && ((i + 1) < count))
                     {
                        x = 0;
                        y--;
                        line = y * this.width;

                        if(y < 0)
                        {
                           return;
                        }
                     }
                  }
               }
               else
               {
                  switch(info)
                  {
                     case 0:
                        x = this.width;
                     break;
                     case 1:
                        return;
                     case 2:
                        if(index == 2)
                        {
                           left = buffer[2] & 0xFF;
                           up = buffer[3] & 0xFF;
                           index = 4;
                        }
                        else
                        {
                           UtilIO.readStream(inputStream, buffer);
                           left = buffer[0] & 0xFF;
                           up = buffer[1] & 0xFF;
                           index = 2;
                        }

                        x += left;
                        y -= up;

                        if(y < 0)
                        {
                           return;
                        }
                     break;
                     default:
                        length = (info + 1) >> 1;

                        if((length & 1) == 1)
                        {
                           length++;
                        }

                        internBuffer = new byte[length];

                        if(index == 2)
                        {
                           internBuffer[0] = buffer[2];
                           internBuffer[1] = buffer[3];
                           UtilIO.readStream(inputStream, internBuffer, 2);
                           index = 4;
                        }
                        else
                        {
                           UtilIO.readStream(inputStream, internBuffer);
                        }

                        highRead = true;
                        internIndex = 0;

                        for(int i = 0; i < info; i++)
                        {
                           if(highRead == true)
                           {
                              colorIndex = internBuffer[internIndex] >> 4;
                              highRead = false;
                           }
                           else
                           {
                              colorIndex = internBuffer[internIndex] & 0xF;
                              internIndex++;
                              highRead = true;
                           }

                           pix = x + line;

                           if((pix & 1) == 0)
                           {
                              this.data[pix >> 1] |= colorIndex << 4;
                           }
                           else
                           {
                              this.data[pix >> 1] |= colorIndex;
                           }

                           x++;

                           if((x >= this.width) && ((i + 1) < info))
                           {
                              x = 0;
                              y--;
                              line = y * this.width;

                              if(y < 0)
                              {
                                 return;
                              }
                           }
                        }
                     break;
                  }
               }
            }
         }

         y--;
      }
   }

   /**
    * Define color in color table
    * 
    * @param colorIndex
    *           Color index table
    * @param color
    *           Color to set
    */
   public void setColor(final int colorIndex, final int color)
   {
      this.colorTable[colorIndex] = color;
   }

   /**
    * Change color index in image pixel
    * 
    * @param x
    *           X
    * @param y
    *           Y
    * @param colorIndex
    *           Color index
    */
   public void setColorIndex(final int x, final int y, final int colorIndex)
   {
      if((x < 0) || (x >= this.width) || (y < 0) || (y >= this.height))
      {
         throw new IllegalArgumentException("x must be in [0, " + this.width + "[ and y in [0, " + this.height + "[ but specified point (" + x + ", " + y + ")");
      }

      if((colorIndex < 0) || (colorIndex >= 16))
      {
         throw new IllegalArgumentException("colorIndex MUST be in [0, 15], not " + colorIndex);
      }

      final int p = x + (y * this.width);
      final int pix = p >> 1;
      final int info = this.data[pix] & 0xFF;

      if((p & 1) == 0)
      {
         this.data[pix] = (byte) ((info & 0x0F) | (colorIndex << 4));
         return;
      }

      this.data[pix] = (byte) ((info & 0xF0) | colorIndex);
   }

   /**
    * Change several colors in color table
    * 
    * @param colorIndexStart
    *           Color index to start to override
    * @param colors
    *           Colors to set
    */
   public void setColors(final int colorIndexStart, final int... colors)
   {
      final int limit = Math.min(16 - colorIndexStart, colors.length);

      for(int i = 0; i < limit; i++)
      {
         this.colorTable[i + colorIndexStart] = colors[i];
      }
   }

   /**
    * Convert color table to default one
    */
   public void toDefaultTableColor()
   {
      System.arraycopy(Image4Bit.DEFAULT_COLOR_TABLE, 0, this.colorTable, 0, 16);
   }

   /**
    * Convert color table to gray table
    */
   public void toGrayTableColor()
   {
      this.colorTable[0] = 0xFF000000;
      this.colorTable[15] = 0xFFFFFFFF;
      int part;

      for(int i = 1; i < 15; i++)
      {
         part = i << 4;
         this.colorTable[i] = 0xFF000000 | (part << 16) | (part << 8) | part;
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
      boolean high = true;
      int pixData = 0;
      int info = this.data[0] & 0xFF;
      int colorIndex;

      for(int pix = 0; pix < length; pix++)
      {
         if(high == true)
         {
            colorIndex = info >> 4;
            high = false;
         }
         else
         {
            colorIndex = info & 0xF;
            pixData++;

            if(pixData < this.data.length)
            {
               info = this.data[pixData] & 0xFF;
            }

            high = true;
         }

         pixels[pix] = this.colorTable[colorIndex];
      }

      return new JHelpImage(this.width, this.height, pixels);
   }
}