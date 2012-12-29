package jhelp.util.thread;

import jhelp.util.Utilities;
import jhelp.util.debug.Debug;
import jhelp.util.debug.DebugLevel;

import org.junit.Test;

public class MutexTest
{
   public class TestThrhread
         implements Runnable
   {
      private final int number;

      public TestThrhread(final int number)
      {
         this.number = number;
      }

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

   public Mutex mutex;

   @Test
   public void test()
   {
      this.mutex = new Mutex();

      for(int i = 0; i < 5; i++)
      {
         (new Thread(new TestThrhread(i))).start();
      }

      Utilities.sleep(16386);
   }
}