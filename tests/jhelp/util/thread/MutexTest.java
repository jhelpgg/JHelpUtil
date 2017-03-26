package jhelp.util.thread;

import org.junit.Test;

import jhelp.util.Utilities;
import jhelp.util.debug.Debug;
import jhelp.util.debug.DebugLevel;

/**
 * Mutex tests
 *
 * @author JHelp <br>
 */
public class MutexTest
{
   /**
    * Thread for do a job
    *
    * @author JHelp <br>
    */
   public class TestThread
         implements Runnable
   {
      /** Thread ID */
      private final int number;

      /**
       * Create a new instance of TestThread
       *
       * @param number
       *           Thread ID
       */
      public TestThread(final int number)
      {
         this.number = number;
      }

      /**
       * Do the thread job <br>
       * <br>
       * <b>Parent documentation:</b><br>
       * {@inheritDoc}
       *
       * @see java.lang.Runnable#run()
       */
      @Override
      public void run()
      {
         Utilities.sleep(this.number + 123);
         Debug.println(DebugLevel.INFORMATION, "Thread number ", this.number, " Want take lock");
         MutexTest.this.mutex.lock();
         Debug.println(DebugLevel.INFORMATION, "Thread number ", this.number, " Have take lock");
         Utilities.sleep(this.number + 123);
         Debug.println(DebugLevel.INFORMATION, "Thread number ", this.number, " Will free lock");
         MutexTest.this.mutex.unlock();
      }
   }

   /** Synchronization mutex */
   public Mutex mutex;

   /**
    * Launch the test
    */
   @Test
   public void test()
   {
      this.mutex = new Mutex();

      for(int i = 0; i < 5; i++)
      {
         (new Thread(new TestThread(i))).start();
      }

      Utilities.sleep(16386);
   }
}