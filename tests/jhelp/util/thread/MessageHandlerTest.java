package jhelp.util.thread;

import jhelp.util.Utilities;
import jhelp.util.debug.Debug;
import jhelp.util.debug.DebugLevel;

import org.junit.Assert;
import org.junit.Test;

public class MessageHandlerTest
      extends MessageHandler<String>
{
   private boolean     inDelivery;
   private final Mutex mutex = new Mutex();

   @Override
   protected void messageArrived(final String message)
   {
      boolean delivery = false;

      this.mutex.lock();
      delivery = this.inDelivery;
      this.inDelivery = true;
      this.mutex.unlock();

      if(delivery == true)
      {
         Assert.fail("2 message in same time");
      }

      Debug.println(DebugLevel.VERBOSE, "BEFORE : ", message);
      Utilities.sleep(4096);
      Debug.println(DebugLevel.VERBOSE, "AFTER : ", message);

      this.mutex.lock();
      this.inDelivery = false;
      this.mutex.unlock();
   }

   @Test
   public void testTwoMessagesInSameTime()
   {
      this.postMessage("ONE");
      this.postMessage("TWO");
      this.postMessage("THREE");

      Utilities.sleep(16384);
   }
}