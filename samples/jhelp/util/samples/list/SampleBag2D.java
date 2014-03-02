package jhelp.util.samples.list;

import jhelp.util.list.Bag2D;
import jhelp.util.list.Scramble;
import jhelp.util.list.SizedObject;

public class SampleBag2D
{
   public static class Sized
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

   /**
    * TODO Explains what does the method main in jhelp.util.samples.list [JHelpUtil]
    * 
    * @param args
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