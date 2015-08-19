package jhelp.util.image.raster;

import java.io.IOException;
import java.io.InputStream;

import jhelp.util.gui.JHelpImage;
import jhelp.util.io.UtilIO;

/**
 * Raster 8 bits image
 * 
 * @author JHelp
 */
public class Image8Bit
      implements RasterImage
{
   /** Color table size */
   public static final int COLOR_TABLE_SIZE = 256;
   /** Color table */
   private final int[]     colorTable;
   /** Image data */
   private final byte[]    data;
   /** Image height */
   private final int       height;
   /** Image width */
   private final int       width;

   /**
    * Create a new instance of Image8Bit
    * 
    * @param width
    *           Width
    * @param height
    *           Height
    */
   public Image8Bit(final int width, final int height)
   {
      if((width < 1) || (height < 1))
      {
         throw new IllegalArgumentException("width and height MUST be >0, but specified dimension was " + width + "x" + height);
      }

      this.width = width;
      this.height = height;
      this.data = new byte[this.width * this.height];
      this.colorTable = new int[256];
      this.toGrayColorTable();
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
    * Get color from color table
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
    * Get color table index of a pixel
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

      return this.data[x + (y * this.width)] & 0xFF;
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
      return RasterImageType.IMAGE_8_BITS;
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
      int x, line, index;

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
               this.data[x + line] = buffer[index];
               index++;
               x++;
            }
         }

         y--;
      }
   }

   /**
    * Parse bitmap compressed stream to data image
    * 
    * @param inputStream
    *           Stream to parse
    * @throws IOException
    *            On reading issue
    */
   public void parseBitmapStreamCompressed(final InputStream inputStream) throws IOException
   {
      this.clear();
      final byte[] buffer = new byte[4];
      int y = this.height - 1;
      int x, line, index, count, info, left, up, length;
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
                  for(int i = 0; i < count; i++)
                  {
                     this.data[x + line] = (byte) info;
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
                        length = info;

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

                        for(int i = 0; i < info; i++)
                        {
                           this.data[x + line] = internBuffer[i];
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
    * Change color table color
    * 
    * @param colorIndex
    *           Color table index
    * @param color
    *           Color to set
    */
   public void setColor(final int colorIndex, final int color)
   {
      this.colorTable[colorIndex] = color;
   }

   /**
    * Change color table index of one pixel
    * 
    * @param x
    *           X
    * @param y
    *           Y
    * @param colorIndex
    *           Color table index
    */
   public void setColorIndex(final int x, final int y, final int colorIndex)
   {
      if((x < 0) || (x >= this.width) || (y < 0) || (y >= this.height))
      {
         throw new IllegalArgumentException("x must be in [0, " + this.width + "[ and y in [0, " + this.height + "[ but specified point (" + x + ", " + y + ")");
      }

      if((colorIndex < 0) || (colorIndex >= 256))
      {
         throw new IllegalArgumentException("colorIndex MUST be in [0, 255], not " + colorIndex);
      }

      this.data[x + (y * this.width)] = (byte) colorIndex;
   }

   /**
    * Change several colors in color table
    * 
    * @param colorIndexStart
    *           Color table index where start write
    * @param colors
    *           Colors to write
    */
   public void setColors(final int colorIndexStart, final int... colors)
   {
      final int limit = Math.min(256 - colorIndexStart, colors.length);

      for(int i = 0; i < limit; i++)
      {
         this.colorTable[i + colorIndexStart] = colors[i];
      }
   }

   /**
    * Convert color table to gray table
    */
   public void toGrayColorTable()
   {
      for(int i = 0; i < 256; i++)
      {
         this.colorTable[i] = 0xFF000000 | (i << 16) | (i << 8) | i;
      }
   }

   /**
    * Convert to JHelp image <br>
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

      for(int pix = length - 1; pix >= 0; pix--)
      {
         pixels[pix] = this.colorTable[this.data[pix] & 0xFF];
      }

      return new JHelpImage(this.width, this.height, pixels);
   }
}