package jhelp.util.samples.list.foreach;

import java.util.concurrent.atomic.AtomicInteger;

import jhelp.util.Utilities;
import jhelp.util.debug.Debug;
import jhelp.util.debug.DebugLevel;

/**
 * Element each used in for each sample
 *
 * @author JHelp <br>
 */
public class ElementEach
{
   /** Next element ID */
   private static final AtomicInteger NEXT_ID = new AtomicInteger(1);
   /** Element ID */
   private final int                  id;

   /**
    * Create a new instance of ElementEach
    */
   public ElementEach()
   {
      this.id = ElementEach.NEXT_ID.getAndIncrement();
   }

   /**
    * Do an action
    */
   public void doSomething()
   {
      Debug.println(DebugLevel.INFORMATION, "Element ", this.id, " start do something");

      for(int i = 0; i < this.id; i++)
      {
         Utilities.sleep(256);

         Debug.println(DebugLevel.INFORMATION, "Element ", this.id, " doing something : ", i + 1, "/", this.id);

         Utilities.sleep(256);
      }

      Debug.println(DebugLevel.INFORMATION, "Element ", this.id, " end do something");
   }

   /**
    * Element ID
    *
    * @return Element ID
    */
   public int getID()
   {
      return this.id;
   }
}