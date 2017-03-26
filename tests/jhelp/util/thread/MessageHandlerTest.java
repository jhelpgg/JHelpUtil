package jhelp.util.thread;

import org.junit.Assert;
import org.junit.Test;

import jhelp.util.Utilities;
import jhelp.util.debug.Debug;
import jhelp.util.debug.DebugLevel;

/**
 * Tests of {@link MessageHandler}
 *
 * @author JHelp <br>
 */
public class MessageHandlerTest
      extends MessageHandler<String>
{
   /** Indicates if a message is delivering */
   private boolean     inDelivery;
   /** Synchronization mutex */
   private final Mutex mutex = new Mutex();

   /**
    * Called when a message arrived <br>
    * <br>
    * <b>Parent documentation:</b><br>
    * {@inheritDoc}
    *
    * @param message
    *           Message received
    * @see jhelp.util.thread.MessageHandler#messageArrived(java.lang.Object)
    */
   @Override
   protected void messageArrived(final String message)
   {
      boolean delivery = false;

      this.mutex.lock();
      delivery = this.inDelivery;
      this.inDelivery = true;
      this.mutex.unlock();

      if(delivery)
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

   /**
    * Called when handler about to be terminated <br>
    * <br>
    * <b>Parent documentation:</b><br>
    * {@inheritDoc}
    *
    * @see jhelp.util.thread.MessageHandler#willBeTerminated()
    */
   @Override
   protected void willBeTerminated()
   {
      // {@todo} TODO Implements willBeTerminated
      Debug.printTodo("Implements willBeTerminated");
   }

   /**
    * Test if two messages comes in same time
    */
   @Test
   public void testTwoMessagesInSameTime()
   {
      this.postMessage("ONE");
      this.postMessage("TWO");
      this.postMessage("THREE");

      Utilities.sleep(16384);
   }
}