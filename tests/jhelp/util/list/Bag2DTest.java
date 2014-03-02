package jhelp.util.list;

import org.junit.Assert;
import org.junit.Test;

public class Bag2DTest
{
   public class Sized
         implements SizedObject
   {
      private final int height;
      private final int width;

      Sized(final int width, final int height)
      {
         this.width = width;
         this.height = height;
      }

      @Override
      public int getHeight()
      {
         return this.height;
      }

      @Override
      public int getWidth()
      {
         return this.width;
      }

      @Override
      public String toString()
      {
         return this.width + "x" + this.height;
      }
   }

   @Test
   public void test1()
   {
      final Bag2D<Sized> bag2d = new Bag2D<Sized>(5, 3);
      final Sized[] sizes =
      {
            new Sized(1, 1), new Sized(1, 1), new Sized(2, 2), new Sized(3, 3)
      };
      final int number = 100;

      for(int test = 1; test <= number; test++)
      {
         bag2d.clearBag();

         for(final Sized sized : sizes)
         {
            Assert.assertNotNull("Can't add " + sized + " in " + bag2d, bag2d.put(sized));
         }

         Scramble.scramble(sizes);
      }
   }

   @Test
   public void test2()
   {
      final Bag2D<Sized> bag2d = new Bag2D<Sized>(4, 3);
      final Sized[] sizes =
      {
            new Sized(1, 1), new Sized(3, 1), new Sized(2, 2), new Sized(1, 3)
      };
      final int number = 100;

      for(int test = 1; test <= number; test++)
      {
         bag2d.clearBag();

         for(final Sized sized : sizes)
         {
            Assert.assertNotNull("Can't add " + sized + " in " + bag2d, bag2d.put(sized));
         }

         Scramble.scramble(sizes);
      }
   }

   @Test
   public void test3()
   {
      final Bag2D<Sized> bag2d = new Bag2D<Sized>(4, 3);
      final Sized[] sizes =
      {
            new Sized(2, 2), new Sized(1, 3), new Sized(3, 1), new Sized(1, 2)
      };
      final int number = 100;

      for(int test = 1; test <= number; test++)
      {
         bag2d.clearBag();

         for(final Sized sized : sizes)
         {
            Assert.assertNotNull("Can't add " + sized + " in " + bag2d, bag2d.put(sized));
         }

         Scramble.scramble(sizes);
      }
   }

   @Test
   public void test4()
   {
      final Bag2D<Sized> bag2d = new Bag2D<Sized>(5, 4);
      final Sized[] sizes =
      {
            new Sized(3, 3), new Sized(1, 4), new Sized(4, 1), new Sized(1, 1)
      };
      final int number = 100;

      for(int test = 1; test <= number; test++)
      {
         bag2d.clearBag();

         for(final Sized sized : sizes)
         {
            Assert.assertNotNull("Can't add " + sized + " in " + bag2d, bag2d.put(sized));
         }

         Scramble.scramble(sizes);
      }
   }

   @Test
   public void test5()
   {
      final Bag2D<Sized> bag2d = new Bag2D<Sized>(4, 5);
      final Sized[] sizes =
      {
            new Sized(3, 3), new Sized(1, 4), new Sized(4, 1), new Sized(1, 1)
      };
      final int number = 100;

      for(int test = 1; test <= number; test++)
      {
         bag2d.clearBag();

         for(final Sized sized : sizes)
         {
            Assert.assertNotNull("Can't add " + sized + " in " + bag2d, bag2d.put(sized));
         }

         Scramble.scramble(sizes);
      }
   }

   @Test
   public void test6()
   {
      final Bag2D<Sized> bag2d = new Bag2D<Sized>(3, 5);
      final Sized[] sizes =
      {
            new Sized(1, 1), new Sized(2, 1), new Sized(1, 2), new Sized(3, 3)
      };
      final int number = 100;

      for(int test = 1; test <= number; test++)
      {
         bag2d.clearBag();

         for(final Sized sized : sizes)
         {
            Assert.assertNotNull("Can't add " + sized + " in " + bag2d, bag2d.put(sized));
         }

         Scramble.scramble(sizes);
      }
   }

   @Test
   public void test7()
   {
      final Bag2D<Sized> bag2d = new Bag2D<Sized>(5, 3);
      final Sized[] sizes =
      {
            new Sized(2, 3), new Sized(3, 1), new Sized(3, 2)
      };
      final int number = 100;

      for(int test = 1; test <= number; test++)
      {
         bag2d.clearBag();

         for(final Sized sized : sizes)
         {
            Assert.assertNotNull("Can't add " + sized + " in " + bag2d, bag2d.put(sized));
         }

         Scramble.scramble(sizes);
      }
   }
}