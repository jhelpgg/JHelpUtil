package jhelp.util.samples.list.foreach;

import java.util.concurrent.atomic.AtomicInteger;

import jhelp.util.Utilities;
import jhelp.util.debug.Debug;
import jhelp.util.debug.DebugLevel;

public class ElementEach
{
   private static final AtomicInteger NEXT_ID = new AtomicInteger(1);
   private final int                  id;

   public ElementEach()
   {
      this.id = ElementEach.NEXT_ID.getAndIncrement();
   }

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

   public int getID()
   {
      return this.id;
   }
}