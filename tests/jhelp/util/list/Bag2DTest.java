package jhelp.util.list;

import org.junit.Assert;
import org.junit.Test;

/**
 * Test of Bag 2D
 *
 * @author JHelp <br>
 */
public class Bag2DTest
{
   /**
    * Object put in bag
    *
    * @author JHelp <br>
    */
   public class Sized
         implements SizedObject
   {
      /** Object height */
      private final int height;
      /** Object width */
      private final int width;

      /**
       * Create a new instance of Sized
       *
       * @param width
       *           Object width
       * @param height
       *           Object height
       */
      Sized(final int width, final int height)
      {
         this.width = width;
         this.height = height;
      }

      /**
       * Object height <br>
       * <br>
       * <b>Parent documentation:</b><br>
       * {@inheritDoc}
       *
       * @return Object height
       * @see jhelp.util.list.SizedObject#getHeight()
       */
      @Override
      public int getHeight()
      {
         return this.height;
      }

      /**
       * Object width <br>
       * <br>
       * <b>Parent documentation:</b><br>
       * {@inheritDoc}
       *
       * @return Object width
       * @see jhelp.util.list.SizedObject#getWidth()
       */
      @Override
      public int getWidth()
      {
         return this.width;
      }

      /**
       * String representation <br>
       * <br>
       * <b>Parent documentation:</b><br>
       * {@inheritDoc}
       *
       * @return String representation
       * @see java.lang.Object#toString()
       */
      @Override
      public String toString()
      {
         return this.width + "x" + this.height;
      }
   }

   /**
    * First case test
    */
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

   /**
    * Second case test
    */
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

   /**
    * Third case test
    */
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

   /**
    * Fourth case test
    */
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

   /**
    * Fifth case test
    */
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

   /**
    * Sixth case test
    */
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

   /**
    * Seventh case test
    */
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