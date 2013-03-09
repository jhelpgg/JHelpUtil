package jhelp.util.io;

import org.junit.Assert;
import org.junit.Test;

public class HashmapedBinarizableTest
{
   public static class ImageVersion1
         extends HashmapedBinarizable
   {
      public ImageVersion1()
      {
      }

      @Override
      protected Object convert(final String name, final Object value, final Type actual, final Type toConvert)
      {
         return null;
      }

      @Override
      protected void initializeFields()
      {
         this.put(HashmapedBinarizableTest.WIDTH, 0);
         this.put(HashmapedBinarizableTest.HEIGHT, 0);
         this.put(HashmapedBinarizableTest.VISIBILITY, true);
      }

      public int getHeight()
      {
         return this.forceGet(HashmapedBinarizableTest.HEIGHT, 0);
      }

      public int getWidth()
      {
         return this.forceGet(HashmapedBinarizableTest.WIDTH, 0);
      }

      public boolean isVisible()
      {
         return this.forceGet(HashmapedBinarizableTest.VISIBILITY, true);
      }

      public void setSize(final int width, final int heigth)
      {
         this.forcePut(HashmapedBinarizableTest.WIDTH, width);
         this.forcePut(HashmapedBinarizableTest.HEIGHT, heigth);
      }

      public void setVisible(final boolean visible)
      {
         this.forcePut(HashmapedBinarizableTest.VISIBILITY, visible);
      }
   }

   public static class ImageVersion2
         extends HashmapedBinarizable
   {
      public ImageVersion2()
      {
      }

      @Override
      protected Object convert(final String name, final Object value, final Type actual, final Type toConvert)
      {
         if((HashmapedBinarizableTest.VISIBILITY.equals(name) == true) && (actual == Type.BOOLEAN) && (toConvert == Type.INT))
         {
            if(((Boolean) value).booleanValue() == true)
            {
               return 255;
            }

            return 0;
         }

         return null;
      }

      @Override
      protected void initializeFields()
      {
         this.put(HashmapedBinarizableTest.X, 0);
         this.put(HashmapedBinarizableTest.Y, 0);
         this.put(HashmapedBinarizableTest.WIDTH, 0);
         this.put(HashmapedBinarizableTest.HEIGHT, 0);
         this.put(HashmapedBinarizableTest.VISIBILITY, 255);
      }

      public int getAlpha()
      {
         return this.forceGet(HashmapedBinarizableTest.VISIBILITY, 255);
      }

      public int getHeight()
      {
         return this.forceGet(HashmapedBinarizableTest.HEIGHT, 0);
      }

      public int getWidth()
      {
         return this.forceGet(HashmapedBinarizableTest.WIDTH, 0);
      }

      public int getX()
      {
         return this.forceGet(HashmapedBinarizableTest.X, 0);
      }

      public int getY()
      {
         return this.forceGet(HashmapedBinarizableTest.Y, 0);
      }

      public void setAlpha(final int alpha)
      {
         this.forcePut(HashmapedBinarizableTest.VISIBILITY, alpha);
      }

      public void setPosition(final int x, final int y)
      {
         this.forcePut(HashmapedBinarizableTest.X, x);
         this.forcePut(HashmapedBinarizableTest.Y, y);
      }

      public void setSize(final int width, final int heigth)
      {
         this.forcePut(HashmapedBinarizableTest.WIDTH, width);
         this.forcePut(HashmapedBinarizableTest.HEIGHT, heigth);
      }
   }

   public static final String HEIGHT     = "height";
   public static final String VISIBILITY = "visibility";
   public static final String WIDTH      = "width";
   public static final String X          = "x";
   public static final String Y          = "y";

   @Test
   public void test()
   {
      final ByteArray byteArray = new ByteArray();

      final ImageVersion1 imageVersion1 = new ImageVersion1();
      imageVersion1.setSize(123, 321);
      imageVersion1.setVisible(false);

      byteArray.clear();
      imageVersion1.serializeBinary(byteArray);

      final ImageVersion2 imageVersion2 = new ImageVersion2();
      imageVersion2.parseBinary(byteArray);

      Assert.assertEquals(123, imageVersion2.getWidth());
      Assert.assertEquals(321, imageVersion2.getHeight());
      Assert.assertEquals(0, imageVersion2.getAlpha());
      Assert.assertEquals(0, imageVersion2.getX());
      Assert.assertEquals(0, imageVersion2.getY());

      //

      imageVersion2.setPosition(42, 24);
      imageVersion2.setSize(987, 789);
      imageVersion2.setAlpha(128);

      byteArray.clear();
      imageVersion2.serializeBinary(byteArray);

      imageVersion1.parseBinary(byteArray);
      Assert.assertEquals(987, imageVersion1.getWidth());
      Assert.assertEquals(789, imageVersion1.getHeight());
      Assert.assertTrue(imageVersion1.isVisible());

      //

      byteArray.clear();
      imageVersion2.serializeBinary(byteArray);

      imageVersion2.setPosition(7842, 7824);
      imageVersion2.setSize(97, 7);
      imageVersion2.setAlpha(51);

      imageVersion2.parseBinary(byteArray);

      Assert.assertEquals(987, imageVersion2.getWidth());
      Assert.assertEquals(789, imageVersion2.getHeight());
      Assert.assertEquals(128, imageVersion2.getAlpha());
      Assert.assertEquals(42, imageVersion2.getX());
      Assert.assertEquals(24, imageVersion2.getY());
   }
}