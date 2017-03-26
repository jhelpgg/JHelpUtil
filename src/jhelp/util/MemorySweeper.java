package jhelp.util;

import java.util.concurrent.locks.ReentrantLock;

import jhelp.util.thread.ThreadManager;
import jhelp.util.thread.ThreadedSimpleTask;
import jhelp.util.thread.ThreadedVerySimpleTask;

/**
 * Clear the memory with regular time
 * 
 * @author JHelp
 */
public final class MemorySweeper
{
   /**
    * Do the cleaning in its own thread
    * 
    * @author JHelp
    */
   static class Sweeper
         extends ThreadedVerySimpleTask
   {
      /**
       * Do the cleaning <br>
       * <br>
       * <b>Parent documentation:</b><br>
       * {@inheritDoc}
       * 
       * @see jhelp.util.thread.ThreadedVerySimpleTask#doVerySimpleAction()
       */
      @Override
      protected void doVerySimpleAction()
      {
         System.gc();
      }
   }

   /** Exiting action */
   private static final ThreadedSimpleTask<Integer> EXIT_ACTION    = new ThreadedSimpleTask<Integer>()
                                                                   {
                                                                      /**
                                                                       * Do the exit action <br>
                                                                       * <br>
                                                                       * <b>Parent documentation:</b><br>
                                                                       * {@inheritDoc}
                                                                       * 
                                                                       * @param parameter
                                                                       *           Exit status
                                                                       * @see jhelp.util.thread.ThreadedSimpleTask#doSimpleAction(java.lang.Object)
                                                                       */
                                                                      @Override
                                                                      protected void doSimpleAction(final Integer parameter)
                                                                      {
                                                                         MemorySweeper.exit(parameter);
                                                                      }
                                                                   };

   /** Indicates if memory sweeper already launched */
   private static boolean                           launched       = false;
   /** For synchronized the launch status */
   private static final ReentrantLock               REENTRANT_LOCK = new ReentrantLock(true);
   /** Stop action */
   private static final ThreadedVerySimpleTask      STOP_ACTION    = new ThreadedVerySimpleTask()
                                                                   {
                                                                      /**
                                                                       * Do the stop action <br>
                                                                       * <br>
                                                                       * <b>Parent documentation:</b><br>
                                                                       * {@inheritDoc}
                                                                       * 
                                                                       * @see jhelp.util.thread.ThreadedVerySimpleTask#doVerySimpleAction()
                                                                       */
                                                                      @Override
                                                                      protected void doVerySimpleAction()
                                                                      {
                                                                         (new Thread(MemorySweeper::stop)).start();
                                                                      }
                                                                   };

   /** Thread link ID */
   private static int                               threadID       = -1;

   /**
    * Exit for real
    * 
    * @param status
    *           Exit status
    */
   static void realExit(final int status)
   {
      Utilities.sleep(1024);

      ThreadManager.THREAD_MANAGER.destroy();

      Utilities.sleep(1024);

      System.gc();

      Utilities.sleep(1024);

      System.exit(status);
   }

   /**
    * Program an automatic exit
    * 
    * @param status
    *           Exit status
    * @param millisecond
    *           Time before exit in millisecond
    */
   public static void automaticExitIn(final int status, final long millisecond)
   {
      ThreadManager.THREAD_MANAGER.delayedThread(MemorySweeper.EXIT_ACTION, status, millisecond);
   }

   /**
    * Program an automatic stop
    * 
    * @param millisecond
    *           Time before stop in millisecond
    */
   public static void automaticStopIn(final long millisecond)
   {
      ThreadManager.THREAD_MANAGER.delayedThread(MemorySweeper.STOP_ACTION, null, millisecond);
   }

   /**
    * Exit an application
    * 
    * @param status
    *           Status to give, usually 0
    */
   public static void exit(final int status)
   {
      (new Thread(() -> MemorySweeper.realExit(status))).start();

      Utilities.sleep(4096);

      System.exit(status);
   }

   /**
    * Launch, if need, the sweeper
    */
   public static void launch()
   {
      MemorySweeper.REENTRANT_LOCK.lock();

      try
      {
         if(MemorySweeper.launched)
         {
            return;
         }

         ThreadManager.THREAD_MANAGER.restart();

         MemorySweeper.launched = true;

         MemorySweeper.threadID = ThreadManager.THREAD_MANAGER.repeatThread(new Sweeper(), null, 1024, 1024);
      }
      finally
      {
         MemorySweeper.REENTRANT_LOCK.unlock();
      }
   }

   /**
    * Stop sweeping, can be restart with {@link #launch()}
    */
   public static void stop()
   {
      System.gc();
      MemorySweeper.REENTRANT_LOCK.lock();
      System.gc();

      try
      {
         System.gc();
         if((MemorySweeper.launched) && (MemorySweeper.threadID >= 0))
         {
            System.gc();
            ThreadManager.THREAD_MANAGER.cancelTask(MemorySweeper.threadID);
            System.gc();

            MemorySweeper.launched = false;
            MemorySweeper.threadID = -1;

            System.gc();
            ThreadManager.THREAD_MANAGER.destroy();
            System.gc();
         }
         System.gc();
      }
      finally
      {
         System.gc();
         MemorySweeper.REENTRANT_LOCK.unlock();
         System.gc();
      }
   }

   /**
    * To avoid someone instantiate it
    */
   private MemorySweeper()
   {
   }
}