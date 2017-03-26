package jhelp.util.samples.list;

import jhelp.util.list.Bag2D;
import jhelp.util.list.Scramble;
import jhelp.util.list.SizedObject;

/**
 * Sample of 2D bag
 *
 * @author JHelp <br>
 */
public class SampleBag2D
{
   /**
    * Object to put in bag
    *
    * @author JHelp <br>
    */
   public static class Sized
         implements SizedObject
   {
      /** Height */
      private final int height;
      /** Width */
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
    * Launch the sample
    *
    * @param args
    *           Unused
    */
   public static void main(final String[] args)
   {
      Bag2D<Sized> bag2d = new Bag2D<Sized>(5, 3);
      final Sized[] sizes =
      {
            new Sized(1, 1), new Sized(1, 1), new Sized(2, 2), new Sized(3, 3)
      };
      final int number = 10;

      for(int test = 1; test <= number; test++)
      {
         bag2d.clearBag();
         System.out.println("--- Test " + test + "/" + number + " ---");
         System.out.println(bag2d);

         for(final Sized sized : sizes)
         {
            System.out.println("Add " + sized);
            if(bag2d.put(sized) == null)
            {
               System.err.println("Warning we fall in a bad case !");
               return;
            }

            System.out.println(bag2d);
         }

         Scramble.scramble(sizes);
      }

      System.out.println("----------------------------");
      bag2d = new Bag2D<Sized>(4, 3);
      sizes[0] = new Sized(1, 1);
      sizes[1] = new Sized(3, 1);
      sizes[2] = new Sized(2, 2);
      sizes[3] = new Sized(1, 3);

      for(int test = 1; test <= number; test++)
      {
         bag2d.clearBag();
         System.out.println("--- Test " + test + "/" + number + " ---");
         System.out.println(bag2d);

         for(final Sized sized : sizes)
         {
            System.out.println("Add " + sized);
            if(bag2d.put(sized) == null)
            {
               System.err.println("Warning we fall in a bad case !");
               return;
            }

            System.out.println(bag2d);
         }

         Scramble.scramble(sizes);
      }

      System.out.println("----------------------------");
      bag2d = new Bag2D<Sized>(4, 3);
      sizes[0] = new Sized(2, 2);
      sizes[1] = new Sized(1, 3);
      sizes[2] = new Sized(3, 1);
      sizes[3] = new Sized(1, 2);

      for(int test = 1; test <= number; test++)
      {
         bag2d.clearBag();
         System.out.println("--- Test " + test + "/" + number + " ---");
         System.out.println(bag2d);

         for(final Sized sized : sizes)
         {
            System.out.println("Add " + sized);
            if(bag2d.put(sized) == null)
            {
               System.err.println("Warning we fall in a bad case !");
               return;
            }

            System.out.println(bag2d);
         }

         Scramble.scramble(sizes);
      }

      System.out.println("----------------------------");
      bag2d = new Bag2D<Sized>(5, 4);
      sizes[0] = new Sized(3, 3);
      sizes[1] = new Sized(1, 4);
      sizes[2] = new Sized(4, 1);
      sizes[3] = new Sized(1, 1);

      for(int test = 1; test <= number; test++)
      {
         bag2d.clearBag();
         System.out.println("--- Test " + test + "/" + number + " ---");
         System.out.println(bag2d);

         for(final Sized sized : sizes)
         {
            System.out.println("Add " + sized);
            if(bag2d.put(sized) == null)
            {
               System.err.println("Warning we fall in a bad case !");
               return;
            }

            System.out.println(bag2d);
         }

         Scramble.scramble(sizes);
      }

      System.out.println("----------------------------");
      bag2d = new Bag2D<Sized>(4, 5);
      sizes[0] = new Sized(3, 3);
      sizes[1] = new Sized(1, 4);
      sizes[2] = new Sized(4, 1);
      sizes[3] = new Sized(1, 1);

      for(int test = 1; test <= number; test++)
      {
         bag2d.clearBag();
         System.out.println("--- Test " + test + "/" + number + " ---");
         System.out.println(bag2d);

         for(final Sized sized : sizes)
         {
            System.out.println("Add " + sized);
            if(bag2d.put(sized) == null)
            {
               System.err.println("Warning we fall in a bad case !");
               return;
            }

            System.out.println(bag2d);
         }

         Scramble.scramble(sizes);
      }

      System.out.println("----------------------------");
      bag2d = new Bag2D<Sized>(3, 5);
      sizes[0] = new Sized(1, 1);
      sizes[1] = new Sized(2, 1);
      sizes[2] = new Sized(1, 2);
      sizes[3] = new Sized(3, 3);

      for(int test = 1; test <= number; test++)
      {
         bag2d.clearBag();
         System.out.println("--- Test " + test + "/" + number + " ---");
         System.out.println(bag2d);

         for(final Sized sized : sizes)
         {
            System.out.println("Add " + sized);
            if(bag2d.put(sized) == null)
            {
               System.err.println("Warning we fall in a bad case !");
               return;
            }

            System.out.println(bag2d);
         }

         Scramble.scramble(sizes);
      }
   }
}