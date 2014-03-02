package jhelp.util.samples.list.foreach;

import java.util.ArrayList;
import java.util.List;

import jhelp.util.MemorySweeper;
import jhelp.util.debug.Debug;
import jhelp.util.debug.DebugLevel;
import jhelp.util.list.foreach.ForEach;
import jhelp.util.time.LapsTime;

public class ForEachSample
{
   /**
    * @param args
    */
   public static void main(final String[] args)
   {
      MemorySweeper.launch();

      final List<ElementEach> elementEachs = new ArrayList<ElementEach>();
      for(int i = 0; i < 10; i++)
      {
         elementEachs.add(new ElementEach());
      }

      Debug.printMark(DebugLevel.INFORMATION, "START");
      LapsTime.startMeasure();

      ForEach.forEach(elementEachs, new FilterElementEach(), new ActionElementEach());

      final LapsTime lapsTime = LapsTime.endMeasure();
      Debug.printMark(DebugLevel.INFORMATION, "END in " + lapsTime);

      MemorySweeper.exit(0);
   }
}