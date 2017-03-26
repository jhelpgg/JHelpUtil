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
package jhelp.util.gui;

import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Stack;

import jhelp.util.io.UtilIO;

/**
 * Represents a mask.<br>
 * A mask is image with light on (foreground) pixels and light off (background) pixels.<br>
 * Because there are only 2 possible value per pixels (on or off) the memory size is optimized
 *
 * @author JHelp
 */
public class JHelpMask
{
   /** Dummy 1x1 mask */
   public static final JHelpMask DUMMY = new JHelpMask(1, 1);

   /**
    * Load the mask from a stream
    *
    * @param inputStream
    *           Stream to read
    * @return Loaded mask
    * @throws IOException
    *            On reading issue
    */
   public static JHelpMask load(final InputStream inputStream) throws IOException
   {
      final int width = UtilIO.readInteger(inputStream);
      final int height = UtilIO.readInteger(inputStream);
      final byte[] data = UtilIO.readByteArray(inputStream);

      return new JHelpMask(width, height, data);
   }

   /** Data of mask */
   private final byte[] data;
   /** Mask height */
   private final int    height;
   /** Mask width */
   private final int    width;

   /**
    * Create a new instance of JHelpMask
    *
    * @param width
    *           Mask width
    * @param height
    *           Mask height
    * @param data
    *           Mask data
    */
   private JHelpMask(final int width, final int height, final byte[] data)
   {
      this.width = width;
      this.height = height;
      this.data = data;
   }

   /**
    * Create a new instance of JHelpMask
    *
    * @param width
    *           Mask width
    * @param height
    *           Mask height
    */
   public JHelpMask(final int width, final int height)
   {
      if((width < 1) || (height < 1))
      {
         throw new IllegalArgumentException("Size must be > 0 not " + width + "x" + height);
      }

      this.width = width;
      this.height = height;
      this.data = new byte[((width * height) + 7) >> 3];
   }

   /**
    * clear the mask. Put all pixels at off
    */
   public void clear()
   {
      for(int i = this.data.length - 1; i >= 0; i--)
      {
         this.data[i] = (byte) 0;
      }
   }

   /**
    * Use a character for mask
    *
    * @param character
    *           Character to use
    * @param family
    *           Font family name
    */
   public void drawCharacter(final char character, final String family)
   {
      final JHelpFont font = new JHelpFont(family, Math.min(this.width, this.height));
      final String string = String.valueOf(character);

      final Shape shape = font.computeShape(string, 0, 0);
      final Rectangle bounds = shape.getBounds();

      Area area = new Area(shape);

      area = area.createTransformedArea(AffineTransform.getTranslateInstance(-bounds.x, -bounds.y));

      final double factor = Math.min((double) this.width / (double) bounds.width, (double) this.height / (double) bounds.height);

      area = area.createTransformedArea(AffineTransform.getScaleInstance(factor, factor));

      for(int y = 0; y < this.height; y++)
      {
         for(int x = 0; x < this.width; x++)
         {
            this.setValue(x, y, area.contains(x, y));
         }
      }
   }

   /**
    * Light on a pixel and all pixels of around, the filling is stopped by pixels already on
    *
    * @param x
    *           X fill start
    * @param y
    *           Y fill start
    */
   public void fill(final int x, final int y)
   {
      int pix = x + (y * this.width);
      int index = pix >> 3;
      int shift = 7 - (pix & 0x7);
      int val = 1 << shift;

      if((this.data[index] & val) != 0)
      {
         return;
      }

      Point point = new Point(x, y);
      final Stack<Point> stack = new Stack<Point>();
      stack.push(point);

      while(!stack.isEmpty())
      {
         point = stack.pop();
         pix = point.x + (point.y * this.width);
         index = pix >> 3;
         shift = 7 - (pix & 0x7);
         val = 1 << shift;

         this.data[index] |= val;

         if((point.x > 0) && (!this.getValue(point.x - 1, point.y)))
         {
            stack.push(new Point(point.x - 1, point.y));
         }

         if((point.y > 0) && (!this.getValue(point.x, point.y - 1)))
         {
            stack.push(new Point(point.x, point.y - 1));
         }

         if((point.x < (this.width - 1)) && (!this.getValue(point.x + 1, point.y)))
         {
            stack.push(new Point(point.x + 1, point.y));
         }

         if((point.y < (this.height - 1)) && (!this.getValue(point.x, point.y + 1)))
         {
            stack.push(new Point(point.x, point.y + 1));
         }
      }
   }

   /**
    * Mask height
    *
    * @return Mask height
    */
   public int getHeight()
   {
      return this.height;
   }

   /**
    * Indicates if a pixel in on
    *
    * @param x
    *           Pixel x
    * @param y
    *           Pixel Y
    * @return {@code true} if the pixel in on
    */
   public boolean getValue(final int x, final int y)
   {
      final int pix = x + (y * this.width);
      final int index = pix >> 3;
      final int shift = 7 - (pix & 0x7);

      return (((this.data[index] & 0xFF) >> shift) & 0x1) == 1;
   }

   /**
    * Mask width
    *
    * @return Mask width
    */
   public int getWidth()
   {
      return this.width;
   }

   /**
    * Save the mask in a stream
    *
    * @param outputStream
    *           Stream where write the mask
    * @throws IOException
    *            On writing issue
    */
   public void save(final OutputStream outputStream) throws IOException
   {
      UtilIO.writeInteger(this.width, outputStream);
      UtilIO.writeInteger(this.height, outputStream);
      UtilIO.writeByteArray(this.data, outputStream);
   }

   /**
    * Change a pixel value
    *
    * @param x
    *           Pixel X
    * @param y
    *           Pixel Y
    * @param value
    *           {@code true} for light on, {@code false} for light off
    */
   public void setValue(final int x, final int y, final boolean value)
   {
      final int pix = x + (y * this.width);
      final int index = pix >> 3;
      final int shift = 7 - (pix & 0x7);

      final int val = 1 << shift;

      if(value)
      {
         this.data[index] |= val;
      }
      else
      {
         this.data[index] &= ~val;
      }
   }
}