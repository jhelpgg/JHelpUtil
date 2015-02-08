package jhelp.util.gui.dynamic;

import jhelp.util.gui.JHelpImage;

public class DrawImage
      extends ImmediateAnimation
{
   private final boolean    center;
   private final JHelpImage image;
   private final int        x;
   private final int        y;

   public DrawImage(final JHelpImage image)
   {
      if(image == null)
      {
         throw new NullPointerException("image musn't be null");
      }

      this.x = 0;
      this.y = 0;
      this.center = true;
      this.image = image;
   }

   public DrawImage(final JHelpImage image, final int x, final int y)
   {
      if(image == null)
      {
         throw new NullPointerException("image musn't be null");
      }

      this.x = x;
      this.y = y;
      this.image = image;
      this.center = false;
   }

   @Override
   public void doImmediately(final JHelpImage image)
   {
      int x = this.x;
      int y = this.y;

      if(this.center == true)
      {
         x = (image.getWidth() - this.image.getWidth()) >> 1;
         y = (image.getHeight() - this.image.getHeight()) >> 1;
      }

      image.drawImage(x, y, this.image);
   }
}