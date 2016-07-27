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
package jhelp.util.gui.transformation;

import jhelp.util.math.UtilMath;
import jhelp.util.text.UtilText;

/**
 * Transform an image while drawing it.<br>
 * It translates some pixels in other place
 *
 * @author JHelp
 */
public class Transformation
{
   /** Transformation height */
   private final int      height;
   /** Transformation size */
   private final int      size;
   /** Transformation vectors */
   private final Vector[] transformation;
   /** Transformation width */
   private final int      width;

   /**
    * Create a new instance of Transformation that change nothing by default
    *
    * @param width
    *           Width
    * @param height
    *           Height
    */
   public Transformation(final int width, final int height)
   {
      this.width = Math.max(1, width);
      this.height = Math.max(1, height);
      this.size = this.width * this.height;
      this.transformation = new Vector[this.size];

      for(int i = 0; i < this.size; i++)
      {
         this.transformation[i] = new Vector();
      }
   }

   /**
    * Check if a position inside the transformation
    *
    * @param x
    *           Position X
    * @param y
    *           Position Y
    * @throws IllegalArgumentException
    *            if point outside the transformation
    */
   private void check(final int x, final int y)
   {
      if((x < 0) || (x >= this.width) || (y < 0) || (y >= this.height))
      {
         throw new IllegalArgumentException(UtilText.concatenate('(', x, ", ", y, ") is outside the transformation ", this.width, 'x', this.height));
      }
   }

   /**
    * Combine actual transformation with horizontal sinusoids
    *
    * @param numberOfWave
    *           Number of wave
    * @param amplitude
    *           Wave size
    */
   public void combineHorizontalSin(final int numberOfWave, final int amplitude)
   {
      this.combineHorizontalSin(numberOfWave, amplitude, 0);
   }

   /**
    * Combine actual transformation with horizontal sinusoids
    *
    * @param numberOfWave
    *           Number of wave
    * @param amplitude
    *           Wave size
    * @param angleStart
    *           Start angle in radiant
    */
   public void combineHorizontalSin(int numberOfWave, int amplitude, final double angleStart)
   {
      numberOfWave = Math.max(1, numberOfWave);
      amplitude = Math.max(0, amplitude);

      final double angleStep = (2 * Math.PI * numberOfWave) / this.width;
      double angle = angleStart;
      int vy;
      int pos;
      Vector vector;

      for(int x = 0; x < this.width; x++)
      {
         vy = (int) (amplitude * Math.sin(angle));
         angle += angleStep;

         pos = x;

         for(int y = 0; y < this.height; y++)
         {
            vector = this.transformation[pos];
            vector.vy += vy;
            pos += this.width;
         }
      }
   }

   /**
    * Combine actual transformation with vertical sinusoids
    *
    * @param numberOfWave
    *           Number of wave
    * @param amplitude
    *           Wave size
    */
   public void combineVerticalSin(final int numberOfWave, final int amplitude)
   {
      this.combineVerticalSin(numberOfWave, amplitude, 0);
   }

   /**
    * Combine actual transformation with vertical sinusoids
    *
    * @param numberOfWave
    *           Number of wave
    * @param amplitude
    *           Wave size
    * @param angleStart
    *           Start angle in radiant
    */
   public void combineVerticalSin(int numberOfWave, int amplitude, final double angleStart)
   {
      numberOfWave = Math.max(1, numberOfWave);
      amplitude = Math.max(0, amplitude);

      final double angleStep = (2 * Math.PI * numberOfWave) / this.height;
      double angle = angleStart;
      int vx;
      int pos = 0;
      Vector vector;

      for(int y = 0; y < this.height; y++)
      {
         vx = (int) (amplitude * Math.sin(angle));
         angle += angleStep;

         for(int x = 0; x < this.width; x++)
         {
            vector = this.transformation[pos];
            vector.vx += vx;
            pos++;
         }
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
    * Obtain a vector transformation for a pixel
    *
    * @param x
    *           X
    * @param y
    *           Y
    * @return Vector transformation
    */
   public Vector getVector(final int x, final int y)
   {
      this.check(x, y);

      return this.transformation[x + (y * this.width)];
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
    * Change a vector transformation for one pixel
    *
    * @param x
    *           Pixel X
    * @param y
    *           Pixel Y
    * @param vx
    *           Vector X
    * @param vy
    *           Vector Y
    */
   public void setVector(final int x, final int y, final int vx, final int vy)
   {
      this.check(x, y);

      final Vector vector = this.transformation[x + (y * this.width)];
      vector.vx = vx;
      vector.vy = vy;
   }

   /**
    * Change a vector transformation for one pixel
    *
    * @param x
    *           Pixel X
    * @param y
    *           Pixel Y
    * @param vect
    *           Vector transformation
    */
   public void setVector(final int x, final int y, final Vector vect)
   {
      this.check(x, y);

      final Vector vector = this.transformation[x + (y * this.width)];
      vector.vx = vect.vx;
      vector.vy = vect.vy;
   }

   /**
    * Make transformation as a elliptic arc
    *
    * @param factor
    *           Arc factor
    */
   public void toHorizontalEllpticArc(final double factor)
   {
      if(UtilMath.isNul(factor) == true)
      {
         this.toIdentity();
         return;
      }

      final double center = this.width * 0.5;
      double max = 0;

      if(factor < 0)
      {
         max = factor * center;
      }

      final double ray = center * center;
      int vy;
      int pos;
      Vector vector;

      for(int x = 0; x < this.width; x++)
      {
         vy = (int) ((factor * Math.sqrt(ray - UtilMath.square(x - center))) - max);
         pos = x;

         for(int y = 0; y < this.height; y++)
         {
            vector = this.transformation[pos];
            vector.vx = 0;
            vector.vy = vy;
            pos += this.width;
         }
      }
   }

   /**
    * Make transformation to horizontal sinusoids
    *
    * @param numberOfWave
    *           Number of wave
    * @param amplitude
    *           Wave size
    */
   public void toHorizontalSin(final int numberOfWave, final int amplitude)
   {
      this.toHorizontalSin(numberOfWave, amplitude, 0);
   }

   /**
    * Make transformation to horizontal sinusoids
    *
    * @param numberOfWave
    *           Number of wave
    * @param amplitude
    *           Wave size
    * @param angleStart
    *           Start angle in radiant
    */
   public void toHorizontalSin(int numberOfWave, int amplitude, final double angleStart)
   {
      numberOfWave = Math.max(1, numberOfWave);
      amplitude = Math.max(0, amplitude);

      final double angleStep = (2 * Math.PI * numberOfWave) / this.width;
      double angle = angleStart;
      int vy;
      int pos;
      Vector vector;

      for(int x = 0; x < this.width; x++)
      {
         vy = (int) (amplitude * Math.sin(angle));
         angle += angleStep;

         pos = x;

         for(int y = 0; y < this.height; y++)
         {
            vector = this.transformation[pos];
            vector.vx = 0;
            vector.vy = vy;
            pos += this.width;
         }
      }
   }

   /**
    * Reset the transformation at zero, no pixels will moves
    */
   public void toIdentity()
   {
      for(int i = 0; i < this.size; i++)
      {
         this.transformation[i].vx = 0;
         this.transformation[i].vy = 0;
      }
   }

   /**
    * Make transformation to vertical sinusoids
    *
    * @param numberOfWave
    *           Number of wave
    * @param amplitude
    *           Wave size
    */
   public void toVerticalSin(final int numberOfWave, final int amplitude)
   {
      this.toVerticalSin(numberOfWave, amplitude, 0);
   }

   /**
    * Make transformation to vertical sinusoids
    *
    * @param numberOfWave
    *           Number of wave
    * @param amplitude
    *           Wave size
    * @param angleStart
    *           Start angle in radiant
    */
   public void toVerticalSin(int numberOfWave, int amplitude, final double angleStart)
   {
      numberOfWave = Math.max(1, numberOfWave);
      amplitude = Math.max(0, amplitude);

      final double angleStep = (2 * Math.PI * numberOfWave) / this.height;
      double angle = angleStart;
      int vx;
      int pos = 0;
      Vector vector;

      for(int y = 0; y < this.height; y++)
      {
         vx = (int) (amplitude * Math.sin(angle));
         angle += angleStep;

         for(int x = 0; x < this.width; x++)
         {
            vector = this.transformation[pos];
            vector.vx = vx;
            vector.vy = 0;
            pos++;
         }
      }
   }
}