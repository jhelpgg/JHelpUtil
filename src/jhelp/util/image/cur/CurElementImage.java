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
package jhelp.util.image.cur;

import java.io.IOException;
import java.io.InputStream;

import jhelp.util.gui.JHelpImage;
import jhelp.util.gui.PixelCombination;
import jhelp.util.image.bmp.BitmapHeader;
import jhelp.util.image.bmp.BitmapImage;
import jhelp.util.image.raster.BinaryImage;

/**
 * Cursor element image
 * 
 * @author JHelp
 */
public class CurElementImage
{
   /** Image height */
   private final int         height;
   /** AND image */
   private JHelpImage        imageAnd;
   /** XOR image */
   private JHelpImage        imageXor;
   /** AND raster image */
   private final BinaryImage rasterAnd;
   /** XOR raster image */
   private final BinaryImage rasterXor;
   /** Image width */
   private final int         width;

   /**
    * Create a new instance of CurElementImage
    * 
    * @param inputStream
    *           Stream where read cursor element image
    * @throws IOException
    *            On reading issue
    */
   public CurElementImage(final InputStream inputStream)
         throws IOException
   {
      final BitmapHeader bitmapHeader = BitmapImage.readBitmapHeader(inputStream, true);
      this.width = bitmapHeader.getWidth();
      this.height = bitmapHeader.getHeight() >> 1;
      this.rasterXor = new BinaryImage(this.width, this.height);
      bitmapHeader.applyColorTable(this.rasterXor);
      this.rasterXor.parseBitmapStream(inputStream);
      this.rasterAnd = new BinaryImage(this.width, this.height);
      bitmapHeader.applyColorTable(this.rasterAnd);
      this.rasterAnd.parseBitmapStream(inputStream);
   }

   /**
    * Draw cursor element on an image
    * 
    * @param parent
    *           Image where draw the cursor element
    * @param x
    *           X on image where place the up-left cursor corner
    * @param y
    *           Y on image where place the up-left cursor corner
    */
   public void draw(final JHelpImage parent, final int x, final int y)
   {
      if(this.imageAnd == null)
      {
         this.imageAnd = this.rasterAnd.toJHelpImage();
      }

      if(this.imageXor == null)
      {
         this.imageXor = this.rasterXor.toJHelpImage();
      }

      parent.drawImage(x, y, this.imageAnd, PixelCombination.AND);
      parent.drawImage(x, y, this.imageXor, PixelCombination.XOR);
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
    * Image width
    * 
    * @return Image width
    */
   public int getWidth()
   {
      return this.width;
   }
}