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
package jhelp.util.image.gif;

import java.io.IOException;
import java.io.InputStream;

import jhelp.util.io.UtilIO;

/**
 * GIF color table<br>
 * 
 * @see <a href="http://www.w3.org/Graphics/GIF/spec-gif89a.txt">GIF specification</a>
 * @author JHelp
 */
class GIFColorTable
      implements GIFConstants
{
   /** Color resolution */
   private final int     colorResolution;
   /** Colors table */
   private final int[]   colors;
   /** Indicates if table is ordered */
   private final boolean ordered;
   /** Table size */
   private final int     size;

   /***
    * Create a new instance of GIFColorTable
    * 
    * @param colorResolution
    *           Color resolution
    * @param ordered
    *           Indicates if table is ordered
    * @param size
    *           Table size
    */
   GIFColorTable(final int colorResolution, final boolean ordered, final int size)
   {
      this.colorResolution = colorResolution;
      this.ordered = ordered;
      this.size = Math.max(size, 2);
      this.colors = new int[this.size];
   }

   /**
    * Obtain a color
    * 
    * @param index
    *           Color index
    * @return Color at specified index
    */
   public int getColor(final int index)
   {
      return this.colors[index % this.size];
   }

   /**
    * Initialize color table to default values
    */
   public void initializeDefault()
   {
      this.colors[0] = 0xFF000000;
      this.colors[1] = 0xFFFFFFFF;

      final int step = 256 / (1 << this.colorResolution);
      int red = 0;
      int green = 0;
      int blue = step;

      for(int i = 2; i < this.size; i++)
      {
         this.colors[i] = 0xFF000000 | (red << 16) | (green << 8) | blue;
         blue += step;

         if(blue > 255)
         {
            blue = 0;
            green += step;

            if(green > 255)
            {
               green = 0;
               red = (red + step) & 0xFF;
            }
         }
      }
   }

   /**
    * Indicates if table is ordered
    * 
    * @return {@code true} if table is ordered
    */
   public boolean isOrdered()
   {
      return this.ordered;
   }

   /**
    * Read color table from stream
    * 
    * @param inputStream
    *           Stream to read
    * @throws IOException
    *            If stream not contains a valid color table
    */
   public void read(final InputStream inputStream) throws IOException
   {
      final int total = this.size * 3;
      final byte[] data = new byte[total];
      final int read = UtilIO.readStream(inputStream, data);

      if(read < total)
      {
         throw new IOException("No enough data to read the color table");
      }

      for(int pix = 0, part = 0; pix < this.size; pix++, part += 3)
      {
         this.colors[pix] = 0xFF000000 | ((data[part] & 0xFF) << 16) | ((data[part + 1] & 0xFF) << 8) | (data[part + 2] & 0xFF);
      }
   }
}