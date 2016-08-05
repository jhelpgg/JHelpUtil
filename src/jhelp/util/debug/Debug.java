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
package jhelp.util.debug;

import java.io.PrintStream;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.locks.ReentrantLock;

import jhelp.util.reflection.Reflector;

/**
 * Class for debugging and thread safe.<br>
 * We avoid dummy printings
 *
 * @author JHelp
 */
public final class Debug
{
   /** Actual debug level */
   private static DebugLevel          debugLevel     = DebugLevel.VERBOSE;
   /** For synchronize the printing (To be thread safe) */
   private static final ReentrantLock REENTRANT_LOCK = new ReentrantLock(true);

   /**
    * Print an integer
    *
    * @param stream
    *           Stream where write
    * @param integer
    *           Integer to print
    * @param characterNumber
    *           Number of character must show
    */
   private static void printInteger(final PrintStream stream, int integer, final int characterNumber)
   {
      int ten = 1;

      for(int i = 1; i < characterNumber; i++)
      {
         ten *= 10;
      }

      while(ten > 0)
      {
         stream.print(integer / ten);

         integer %= ten;
         ten /= 10;
      }
   }

   /**
    * Print a message
    *
    * @param stream
    *           Stream where write
    * @param debugLevel
    *           Debug level
    * @param stackTraceElement
    *           Trace of the source
    * @param message
    *           Message to print
    */
   private static void printMessage(final PrintStream stream, final DebugLevel debugLevel, final StackTraceElement stackTraceElement, final Object... message)
   {
      // Level test
      if(debugLevel.getLevel() > Debug.debugLevel.getLevel())
      {
         return;
      }

      // Print time
      final GregorianCalendar gregorianCalendar = new GregorianCalendar();

      Debug.printInteger(stream, gregorianCalendar.get(Calendar.DAY_OF_MONTH), 2);
      stream.print('/');
      Debug.printInteger(stream, gregorianCalendar.get(Calendar.MONTH) + 1, 2);
      stream.print('/');
      Debug.printInteger(stream, gregorianCalendar.get(Calendar.YEAR), 4);
      stream.print(" : ");
      Debug.printInteger(stream, gregorianCalendar.get(Calendar.HOUR_OF_DAY), 2);
      stream.print('h');
      Debug.printInteger(stream, gregorianCalendar.get(Calendar.MINUTE), 2);
      stream.print('m');
      Debug.printInteger(stream, gregorianCalendar.get(Calendar.SECOND), 2);
      stream.print('s');
      Debug.printInteger(stream, gregorianCalendar.get(Calendar.MILLISECOND), 3);
      stream.print("ms : ");

      // Print level
      stream.print(debugLevel.getHeader());

      // Print code location
      stream.print(stackTraceElement.getClassName());
      stream.print('.');
      stream.print(stackTraceElement.getMethodName());
      stream.print(" at ");
      stream.print(stackTraceElement.getLineNumber());
      stream.print(" (");
      stream.print(stackTraceElement.getFileName());
      stream.print(":");
      stream.print(stackTraceElement.getLineNumber());
      stream.print(") : ");

      // Print message
      for(final Object element : message)
      {
         Debug.printObject(stream, element);
      }

      stream.println();
   }

   /**
    * Print an object
    *
    * @param stream
    *           Stream where write
    * @param object
    *           Object to print
    */
   @SuppressWarnings(
   {
         "rawtypes", "unchecked"
   })
   private static void printObject(final PrintStream stream, final Object object)
   {
      if((object == null) || (object.getClass().isArray() == false))
      {
         if(object != null)
         {
            if(object instanceof Iterable)
            {
               stream.print('{');
               boolean first = true;

               for(final Object obj : (Iterable) object)
               {
                  if(first == false)
                  {
                     stream.print("; ");
                  }

                  Debug.printObject(stream, obj);
                  first = false;
               }

               stream.print('}');
               return;
            }

            if(object instanceof Map)
            {
               stream.print('{');
               boolean first = true;

               for(final Entry entry : (Set<Entry>) ((Map) object).entrySet())
               {
                  if(first == false)
                  {
                     stream.print(" | ");
                  }

                  Debug.printObject(stream, entry.getKey());
                  stream.print("=");
                  Debug.printObject(stream, entry.getValue());
                  first = false;
               }

               stream.print('}');
               return;
            }
         }

         stream.print(object);

         return;
      }

      stream.print('[');

      if(object.getClass().getComponentType().isPrimitive() == true)
      {
         final String name = object.getClass().getComponentType().getName();

         if(Reflector.PRIMITIVE_BOOLEAN.equals(name) == true)
         {
            final boolean[] array = (boolean[]) object;
            final int length = array.length;

            if(length > 0)
            {
               stream.print(array[0]);

               for(int i = 1; i < length; i++)
               {
                  stream.print(", ");

                  stream.print(array[i]);
               }
            }
         }
         else if(Reflector.PRIMITIVE_BYTE.equals(name) == true)
         {
            final byte[] array = (byte[]) object;
            final int length = array.length;

            if(length > 0)
            {
               stream.print(array[0]);

               for(int i = 1; i < length; i++)
               {
                  stream.print(", ");

                  stream.print(array[i]);
               }
            }
         }
         else if(Reflector.PRIMITIVE_CHAR.equals(name) == true)
         {
            final char[] array = (char[]) object;
            final int length = array.length;

            if(length > 0)
            {
               stream.print(array[0]);

               for(int i = 1; i < length; i++)
               {
                  stream.print(", ");

                  stream.print(array[i]);
               }
            }
         }
         else if(Reflector.PRIMITIVE_DOUBLE.equals(name) == true)
         {
            final double[] array = (double[]) object;
            final int length = array.length;

            if(length > 0)
            {
               stream.print(array[0]);

               for(int i = 1; i < length; i++)
               {
                  stream.print(", ");

                  stream.print(array[i]);
               }
            }
         }
         else if(Reflector.PRIMITIVE_FLOAT.equals(name) == true)
         {
            final float[] array = (float[]) object;
            final int length = array.length;

            if(length > 0)
            {
               stream.print(array[0]);

               for(int i = 1; i < length; i++)
               {
                  stream.print(", ");

                  stream.print(array[i]);
               }
            }
         }
         else if(Reflector.PRIMITIVE_INT.equals(name) == true)
         {
            final int[] array = (int[]) object;
            final int length = array.length;

            if(length > 0)
            {
               stream.print(array[0]);

               for(int i = 1; i < length; i++)
               {
                  stream.print(", ");

                  stream.print(array[i]);
               }
            }
         }
         else if(Reflector.PRIMITIVE_LONG.equals(name) == true)
         {
            final long[] array = (long[]) object;
            final int length = array.length;

            if(length > 0)
            {
               stream.print(array[0]);

               for(int i = 1; i < length; i++)
               {
                  stream.print(", ");

                  stream.print(array[i]);
               }
            }
         }
         else if(Reflector.PRIMITIVE_SHORT.equals(name) == true)
         {
            final short[] array = (short[]) object;
            final int length = array.length;

            if(length > 0)
            {
               stream.print(array[0]);

               for(int i = 1; i < length; i++)
               {
                  stream.print(", ");

                  stream.print(array[i]);
               }
            }
         }
      }
      else
      {
         final Object[] array = (Object[]) object;
         final int length = array.length;

         if(length > 0)
         {
            Debug.printObject(stream, array[0]);

            for(int i = 1; i < length; i++)
            {
               stream.print(", ");

               Debug.printObject(stream, array[i]);
            }
         }
      }

      stream.print(']');
   }

   /**
    * Print a trace
    *
    * @param stream
    *           Stream where write
    * @param debugLevel
    *           Debug level
    * @param throwable
    *           Trace to print
    * @param start
    *           Offset to start reading the trace
    */
   private static void printTrace(final PrintStream stream, final DebugLevel debugLevel, Throwable throwable, int start)
   {
      // Level test
      if(debugLevel.getLevel() > Debug.debugLevel.getLevel())
      {
         return;
      }

      // Print trace
      StackTraceElement[] stackTraceElements;
      StackTraceElement stackTraceElement;
      int length;

      while(throwable != null)
      {
         stream.println(throwable.getMessage());
         stream.println(throwable.getLocalizedMessage());
         stream.println(throwable.toString());

         stackTraceElements = throwable.getStackTrace();
         length = stackTraceElements.length;

         for(int index = start; index < length; index++)
         {
            stackTraceElement = stackTraceElements[index];

            stream.print('\t');
            stream.print(stackTraceElement.getClassName());
            stream.print('.');
            stream.print(stackTraceElement.getMethodName());
            stream.print(" at ");
            stream.print(stackTraceElement.getLineNumber());
            stream.print(" (");
            stream.print(stackTraceElement.getFileName());
            stream.print(":");
            stream.print(stackTraceElement.getLineNumber());
            stream.println(")");
         }

         throwable = throwable.getCause();

         if(throwable != null)
         {
            stream.println("Caused by : ");
         }

         start = 0;
      }
   }

   /**
    * Actual debug level
    *
    * @return Actual debug level
    */
   public static DebugLevel getDebugLevel()
   {
      return Debug.debugLevel;
   }

   /**
    * Print information to know which part of code, called a method.<br>
    * Short version of {@link #printTrace(DebugLevel, Object...)}
    *
    * @param debugLevel
    *           Debug level
    */
   public static void printCalledFrom(final DebugLevel debugLevel)
   {
      // Level test
      if(debugLevel.getLevel() > Debug.debugLevel.getLevel())
      {
         return;
      }

      Debug.REENTRANT_LOCK.lock();

      try
      {
         final Throwable throwable = new Throwable();
         final StackTraceElement[] traces = throwable.getStackTrace();
         final StackTraceElement stackTraceElement = traces[2];

         Debug.printMessage(System.out, debugLevel, traces[1], "Called from ", stackTraceElement.getClassName(), '.', stackTraceElement.getMethodName(), " at ",
               stackTraceElement.getLineNumber());
      }
      finally
      {
         Debug.REENTRANT_LOCK.unlock();
      }
   }

   /**
    * Print an error with its trace
    *
    * @param error
    *           Error to print
    * @param message
    *           Message information
    */
   public static void printError(final Error error, final Object... message)
   {
      // Level test
      if(DebugLevel.ERROR.getLevel() > Debug.debugLevel.getLevel())
      {
         return;
      }

      Debug.REENTRANT_LOCK.lock();

      try
      {
         if((message != null) && (message.length > 0))
         {
            Debug.printMessage(System.err, DebugLevel.ERROR, (new Throwable()).getStackTrace()[1], message);
         }

         System.err.println("<-- ERROR");

         Debug.printTrace(System.err, DebugLevel.ERROR, error, 0);

         System.err.println("ERROR -->");
      }
      finally
      {
         Debug.REENTRANT_LOCK.unlock();
      }
   }

   /**
    * Print an exception with its trace
    *
    * @param exception
    *           Exception to print
    * @param message
    *           Message information
    */
   public static void printException(final Exception exception, final Object... message)
   {
      // Level test
      if(DebugLevel.WARNING.getLevel() > Debug.debugLevel.getLevel())
      {
         return;
      }

      Debug.REENTRANT_LOCK.lock();

      try
      {
         if((message != null) && (message.length > 0))
         {
            Debug.printMessage(System.err, DebugLevel.WARNING, (new Throwable()).getStackTrace()[1], message);
         }

         System.err.println("<-- EXCEPTION");

         Debug.printTrace(System.err, DebugLevel.WARNING, exception, 0);

         System.err.println("EXCEPTION -->");
      }
      finally
      {
         Debug.REENTRANT_LOCK.unlock();
      }
   }

   /**
    * Print some information
    *
    * @param debugLevel
    *           Debug level
    * @param message
    *           Message to print
    */
   public static void println(final DebugLevel debugLevel, final Object... message)
   {
      // Level test
      if(debugLevel.getLevel() > Debug.debugLevel.getLevel())
      {
         return;
      }

      Debug.REENTRANT_LOCK.lock();

      try
      {
         Debug.printMessage(System.out, debugLevel, (new Throwable()).getStackTrace()[1], message);
      }
      finally
      {
         Debug.REENTRANT_LOCK.unlock();
      }
   }

   /**
    * Print a mark
    *
    * @param debugLevel
    *           Debug level
    * @param mark
    *           Mark to print
    */
   public static void printMark(final DebugLevel debugLevel, final String mark)
   {
      // Level test
      if(debugLevel.getLevel() > Debug.debugLevel.getLevel())
      {
         return;
      }

      Debug.REENTRANT_LOCK.lock();

      try
      {
         final int length = mark.length() + 12;
         final char[] headers = new char[length];

         for(int i = 0; i < length; i++)
         {
            headers[i] = '*';
         }

         final String header = new String(headers);

         final StackTraceElement stackTraceElement = (new Throwable()).getStackTrace()[1];

         Debug.printMessage(System.out, debugLevel, stackTraceElement, header);
         Debug.printMessage(System.out, debugLevel, stackTraceElement, "***   ", mark, "   ***");
         Debug.printMessage(System.out, debugLevel, stackTraceElement, header);
      }
      finally
      {
         Debug.REENTRANT_LOCK.unlock();
      }
   }

   /**
    * Print a to do message
    *
    * @param message
    *           Message to print
    */
   public static void printTodo(final Object... message)
   {
      // Level test
      if((DebugLevel.VERBOSE.getLevel() > Debug.debugLevel.getLevel()) || (message == null))
      {
         return;
      }

      Debug.REENTRANT_LOCK.lock();

      try
      {
         final Object[] todoMessage = new Object[message.length + 2];

         todoMessage[0] = "TODO --- ";
         System.arraycopy(message, 0, todoMessage, 1, message.length);
         todoMessage[todoMessage.length - 1] = " --- TODO";

         Debug.printMessage(System.out, DebugLevel.VERBOSE, (new Throwable()).getStackTrace()[1], todoMessage);
      }
      finally
      {
         Debug.REENTRANT_LOCK.unlock();
      }
   }

   /**
    * Print an informative trace (To know the execution stack)
    *
    * @param debugLevel
    *           Debug level
    * @param message
    *           Message to print
    */
   public static void printTrace(final DebugLevel debugLevel, final Object... message)
   {
      // Level test
      if(debugLevel.getLevel() > Debug.debugLevel.getLevel())
      {
         return;
      }

      Debug.REENTRANT_LOCK.lock();

      try
      {
         final Throwable throwable = new Throwable();

         if((message != null) && (message.length > 0))
         {
            Debug.printMessage(System.out, debugLevel, throwable.getStackTrace()[1], message);
         }

         System.out.println("<-- TRACE");

         Debug.printTrace(System.out, debugLevel, throwable, 1);

         System.out.println("TRACE -->");
      }
      finally
      {
         Debug.REENTRANT_LOCK.unlock();
      }
   }

   /**
    * Change debug level
    *
    * @param debugLevel
    *           New debug level
    */
   public static void setDebugLevel(final DebugLevel debugLevel)
   {
      if(debugLevel == null)
      {
         throw new NullPointerException("debugLevel musn't be null");
      }

      Debug.debugLevel = debugLevel;
   }

   /**
    * To avoid instance
    */
   private Debug()
   {
   }
}