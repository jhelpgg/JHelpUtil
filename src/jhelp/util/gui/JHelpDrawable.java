package jhelp.util.gui;

import java.awt.Shape;

public class JHelpDrawable
{
   private final int        color;
   private final JHelpImage image;
   private final JHelpPaint paint;

   public JHelpDrawable(final int color)
   {
      this.color = color;
      this.image = null;
      this.paint = null;
   }

   public JHelpDrawable(final JHelpImage image)
   {
      if(image == null)
      {
         throw new NullPointerException("image musn't be null");
      }

      this.color = 0;
      this.image = image;
      this.paint = null;
   }

   public JHelpDrawable(final JHelpPaint paint)
   {
      if(paint == null)
      {
         throw new NullPointerException("paint musn't be null");
      }

      this.color = 0;
      this.image = null;
      this.paint = paint;
   }

   public void fillEllipse(final int x, final int y, final int width, final int height, final JHelpImage image)
   {
      final boolean draw = image.isDrawMode();

      if(draw == false)
      {
         image.startDrawMode();
      }

      if(this.paint != null)
      {
         image.fillEllipse(x, y, width, height, this.paint);
      }
      else if(this.image != null)
      {
         image.fillEllipse(x, y, width, height, this.image);
      }
      else
      {
         image.fillEllipse(x, y, width, height, this.color);
      }

      if(draw == false)
      {
         image.endDrawMode();
      }
   }

   public void fillPolygon(final int[] xs, final int offsetX, final int[] ys, final int offsetY, final int length, final JHelpImage image)
   {
      final boolean draw = image.isDrawMode();

      if(draw == false)
      {
         image.startDrawMode();
      }

      if(this.paint != null)
      {
         image.fillPolygon(xs, offsetX, ys, offsetY, length, this.paint);
      }
      else if(this.image != null)
      {
         image.fillPolygon(xs, offsetX, ys, offsetY, length, this.image);
      }
      else
      {
         image.fillPolygon(xs, offsetX, ys, offsetY, length, this.color);
      }

      if(draw == false)
      {
         image.endDrawMode();
      }
   }

   public void fillRectangle(final int x, final int y, final int width, final int height, final JHelpImage image)
   {
      final boolean draw = image.isDrawMode();

      if(draw == false)
      {
         image.startDrawMode();
      }

      if(this.paint != null)
      {
         image.fillRectangle(x, y, width, height, this.paint);
      }
      else if(this.image != null)
      {
         image.fillRectangle(x, y, width, height, this.image);
      }
      else
      {
         image.fillRectangle(x, y, width, height, this.color);
      }

      if(draw == false)
      {
         image.endDrawMode();
      }
   }

   public void fillRoundRectangle(final int x, final int y, final int width, final int height, final int arcWidth, final int arcHeight, final JHelpImage image)
   {
      final boolean draw = image.isDrawMode();

      if(draw == false)
      {
         image.startDrawMode();
      }

      if(this.paint != null)
      {
         image.fillRoundRectangle(x, y, width, height, arcWidth, arcHeight, this.paint);
      }
      else if(this.image != null)
      {
         image.fillRoundRectangle(x, y, width, height, arcWidth, arcHeight, this.image);
      }
      else
      {
         image.fillRoundRectangle(x, y, width, height, arcWidth, arcHeight, this.color);
      }

      if(draw == false)
      {
         image.endDrawMode();
      }
   }

   public void fillShape(final Shape shape, final JHelpImage image)
   {
      final boolean draw = image.isDrawMode();

      if(draw == false)
      {
         image.startDrawMode();
      }

      if(this.paint != null)
      {
         image.fillShape(shape, this.paint);
      }
      else if(this.image != null)
      {
         image.fillShape(shape, this.image);
      }
      else
      {
         image.fillShape(shape, this.color);
      }

      if(draw == false)
      {
         image.endDrawMode();
      }
   }
}