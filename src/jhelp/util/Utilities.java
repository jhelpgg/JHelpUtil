package jhelp.util;

import java.lang.reflect.Array;

/**
 * Divers utilities
 * 
 * @author JHelp
 */
public final class Utilities
{
   /**
    * Indicates if a character is inside an array
    * 
    * @param character
    *           Character search
    * @param array
    *           Array where search
    * @return Character index
    */
   public static boolean contains(final char character, final char... array)
   {
      for(int i = array.length - 1; i >= 0; i--)
      {
         if(array[i] == character)
         {
            return true;
         }
      }

      return false;
   }

   /**
    * Create a int array copy
    * 
    * @param array
    *           Array to copy
    * @return Copy of array
    */
   public static int[] createCopy(final int[] array)
   {
      if(array == null)
      {
         return null;
      }

      final int length = array.length;

      final int[] clone = new int[length];

      System.arraycopy(array, 0, clone, 0, length);

      return clone;
   }

   /**
    * Create an array copy
    * 
    * @param <T>
    *           Type of array elements
    * @param array
    *           Array to copy
    * @return Array copy
    */
   @SuppressWarnings("unchecked")
   public static <T> T[] createCopy(final T[] array)
   {
      if(array == null)
      {
         return null;
      }

      final int length = array.length;

      final Class<T> classT = (Class<T>) array.getClass().getComponentType();

      final T[] clone = (T[]) Array.newInstance(classT, length);

      System.arraycopy(array, 0, clone, 0, length);

      return clone;
   }

   /**
    * Compute index of byte array inside an other byt array
    * 
    * @param container
    *           Byte array where search the content
    * @param content
    *           Searched array inside the container
    * @param offset
    *           Offset to start the search
    * @param length
    *           Number of byte to take care inside the content
    * @return Index where container starts contain the content or -1 if not found
    */
   public static int indexOf(final byte[] container, final byte[] content, int offset, int length)
   {
      if(offset < 0)
      {
         length += offset;
         offset = 0;
      }

      if(((offset + length) > container.length) || (length > content.length) || (length < 1))
      {
         return -1;
      }

      boolean found = false;
      final byte first = content[0];
      final int end = container.length - length;

      for(int i = offset; i <= end; i++)
      {
         if(container[i] == first)
         {
            found = true;

            for(int k = 1; k < length; k++)
            {
               if(container[i + k] != content[k])
               {
                  found = false;
                  break;
               }
            }

            if(found == true)
            {
               return i;
            }
         }
      }

      return -1;
   }

   /**
    * Obtain a character index inside a character array. If the character doesn't contains the searched charcter, -1 is return
    * 
    * @param array
    *           Array where search the character
    * @param element
    *           Searched character
    * @return Character index or -1 if character not inside the array
    */
   public static int indexOf(final char[] array, final char element)
   {
      if(array == null)
      {
         return -1;
      }

      final int length = array.length;

      for(int i = 0; i < length; i++)
      {

         if(array[i] == element)
         {
            return i;
         }
      }

      return -1;
   }

   /**
    * Index of an element inside an array
    * 
    * @param <T>
    *           Element type
    * @param array
    *           Array where search
    * @param element
    *           Element search
    * @return Index where is element or -1 if not found
    */
   public static <T> int indexOf(final T[] array, final T element)
   {
      if(array == null)
      {
         return -1;
      }

      final int length = array.length;

      for(int i = 0; i < length; i++)
      {
         if((array[i] == null) && (element == null))
         {
            return i;
         }
         else if((array[i] != null) && (array[i].equals(element) == true))
         {
            return i;
         }
      }

      return -1;
   }

   /**
    * Make thread call it sleep for specified time in milliseconds
    * 
    * @param milliseconds
    *           Time to sleep in milliseconds
    */
   public static final void sleep(final long milliseconds)
   {
      try
      {
         Thread.sleep(milliseconds);
      }
      catch(final Exception exception)
      {
      }
   }

   /**
    * To avoid instance
    */
   private Utilities()
   {
   }
}