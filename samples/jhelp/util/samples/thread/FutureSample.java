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
package jhelp.util.samples.thread;

import jhelp.util.debug.Debug;
import jhelp.util.debug.DebugLevel;
import jhelp.util.list.Pair;
import jhelp.util.thread.ActionTask;
import jhelp.util.thread.BooleanTask;
import jhelp.util.thread.FutureTask;
import jhelp.util.thread.ThreadManager;
import jhelp.util.thread.VoidTask;

/**
 * Sample to show future usage
 *
 * @author JHelp <br>
 */
public class FutureSample
{
   /**
    * Action that that accept only odd numbers
    *
    * @author JHelp <br>
    */
   static class FilterAction
         implements BooleanTask<Pair<Integer, Boolean>>
   {
      /**
       * Create a new instance of FilterAction
       */
      FilterAction()
      {
      }

      /**
       * Called when result is computed and test if it is filtered or not <br>
       * <br>
       * <b>Parent documentation:</b><br>
       * {@inheritDoc}
       *
       * @param parameter
       *           Computed result
       * @return {@code true} if result is filtered
       * @see jhelp.util.thread.BooleanTask#doBooleanAction(java.lang.Object)
       */
      @Override
      public boolean doBooleanAction(final Pair<Integer, Boolean> parameter)
      {
         Debug.printTodo("Implements doAction : " + parameter);
         return (parameter.element1 & 1) != 0;
      }
   }

   /**
    * Action that indicates if a number is prime
    *
    * @author JHelp <br>
    */
   static class IsPrimeAction
         implements ActionTask<Integer, Pair<Integer, Boolean>>
   {
      /**
       * Create a new instance of IsPrimeAction
       */
      IsPrimeAction()
      {
      }

      /**
       * Test if given number is prime or not <br>
       * <br>
       * <b>Parent documentation:</b><br>
       * {@inheritDoc}
       *
       * @param parameter
       *           Number tested
       * @return Couple of tested number and prime test result
       * @see jhelp.util.thread.ActionTask#doAction(java.lang.Object)
       */
      @Override
      public Pair<Integer, Boolean> doAction(final Integer parameter)
      {
         Debug.println(DebugLevel.DEBUG, "parameter=" + parameter);
         int integer = parameter;

         if(integer < 0)
         {
            integer = -integer;
         }

         if((integer <= 2) || ((integer & 1) == 0))
         {
            return new Pair<Integer, Boolean>(integer, integer == 2);
         }

         final int root = (int) Math.sqrt(integer);

         for(int div = 3; div <= root; div += 2)
         {
            if((integer % div) == 0)
            {
               return new Pair<Integer, Boolean>(integer, integer == div);
            }
         }

         return new Pair<Integer, Boolean>(integer, true);
      }
   }

   /**
    * Action that print the prime result
    *
    * @author JHelp <br>
    */
   static class ResultAction
         implements VoidTask<Pair<Integer, Boolean>>
   {
      /**
       * Create a new instance of ResultAction
       */
      ResultAction()
      {
      }

      /**
       * Called when prime result is computed and pass the filter <br>
       * <br>
       * <b>Parent documentation:</b><br>
       * {@inheritDoc}
       *
       * @param parameter
       *           Prime result
       * @see jhelp.util.thread.VoidTask#doVoidAction(java.lang.Object)
       */
      @Override
      public void doVoidAction(final Pair<Integer, Boolean> parameter)
      {
         if(parameter.element2)
         {
            Debug.printMark(DebugLevel.INFORMATION, parameter.element1 + " is prime");
         }
         else
         {
            Debug.printMark(DebugLevel.INFORMATION, parameter.element1 + " is not prime");
         }
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
      final IsPrimeAction isPrimeAction = new IsPrimeAction();
      final ResultAction resultAction = new ResultAction();
      final FilterAction filterAction = new FilterAction();

      final FutureTask<Pair<Integer, Boolean>, Void> futureTask1 = //
            ThreadManager.THREAD_MANAGER.doAction(isPrimeAction, 982451653).filter(filterAction).then(resultAction);
      final FutureTask<Pair<Integer, Boolean>, Void> futureTask2 = //
            ThreadManager.THREAD_MANAGER.doAction(isPrimeAction, 987654321).filter(filterAction).then(resultAction);
      final FutureTask<Pair<Integer, Boolean>, Void> futureTask3 = //
            ThreadManager.THREAD_MANAGER.doAction(isPrimeAction, 2).filter(filterAction).then(resultAction);

      futureTask1.join();
      futureTask2.join();
      futureTask3.join();
      ThreadManager.THREAD_MANAGER.destroy();
   }
}