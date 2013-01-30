package jhelp.util.gui;

import org.junit.Assert;
import org.junit.Test;

public class JHelpImageTest
{
   private JHelpImage createJHelpImage(final int width, final int height)
   {
      final int length = width * height;
      final int[] pixels = new int[length];

      for(int pix = length - 1; pix >= 0; pix--)
      {
         pixels[pix] = pix;
      }

      return new JHelpImage(width, height, pixels);
   }

   @Test
   public void testFlipBoth()
   {
      final JHelpImage image = this.createJHelpImage(3, 4);
      image.startDrawMode();
      image.flipBoth();
      image.endDrawMode();
      int n = 11;

      for(int y = 0; y < 4; y++)
      {
         for(int x = 0; x < 3; x++)
         {
            Assert.assertEquals("(" + x + ", " + y + ")", n, image.pickColor(x, y));
            n--;
         }
      }
   }

   @Test
   public void testFlipHorizontal()
   {
      final JHelpImage image = this.createJHelpImage(3, 4);
      image.startDrawMode();
      image.flipHorizontal();
      image.endDrawMode();
      int n = 0;

      for(int y = 0; y < 4; y++)
      {
         for(int x = 2; x >= 0; x--)
         {
            Assert.assertEquals("(" + x + ", " + y + ")", n, image.pickColor(x, y));
            n++;
         }
      }
   }

   @Test
   public void testFlipVertical()
   {
      final JHelpImage image = this.createJHelpImage(3, 4);
      image.startDrawMode();
      image.flipHorizontal();
      image.endDrawMode();
      int n = 11;

      for(int y = 3; y >= 0; y--)
      {
         for(int x = 0; x < 3; x++)
         {
            Assert.assertEquals("(" + x + ", " + y + ")", n, image.pickColor(x, y));
            n--;
         }
      }
   }

   @Test
   public void testRotate180()
   {
      final JHelpImage image = this.createJHelpImage(3, 4).rotate180();
      Assert.assertEquals(3, image.getWidth());
      Assert.assertEquals(4, image.getHeight());
      int n = 11;

      for(int y = 0; y < 4; y++)
      {
         for(int x = 0; x < 3; x++)
         {
            Assert.assertEquals("(" + x + ", " + y + ")", n, image.pickColor(x, y));
            n--;
         }
      }
   }

   @Test
   public void testRotate270()
   {
      final JHelpImage image = this.createJHelpImage(3, 4).rotate270();
      Assert.assertEquals(4, image.getWidth());
      Assert.assertEquals(3, image.getHeight());

      int n = 0;

      for(int x = 3; x >= 0; x--)
      {
         for(int y = 0; y < 3; y++)
         {
            Assert.assertEquals("(" + x + ", " + y + ")", n, image.pickColor(x, y));
            n++;
         }
      }
   }

   @Test
   public void testRotate90()
   {
      final JHelpImage image = this.createJHelpImage(3, 4).rotate90();
      Assert.assertEquals(4, image.getWidth());
      Assert.assertEquals(3, image.getHeight());

      int n = 0;

      for(int x = 0; x < 4; x++)
      {
         for(int y = 2; y >= 0; y--)
         {
            Assert.assertEquals("(" + x + ", " + y + ")", n, image.pickColor(x, y));
            n++;
         }
      }
   }
}