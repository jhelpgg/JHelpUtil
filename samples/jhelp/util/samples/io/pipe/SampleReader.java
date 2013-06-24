package jhelp.util.samples.io.pipe;

import java.io.File;

import jhelp.util.debug.Debug;
import jhelp.util.debug.DebugLevel;
import jhelp.util.io.pipe.PipeReader;

public class SampleReader
{

   public static void main(final String[] args)
   {
      final PipeReader pipeReader = new PipeReader(new File("/home/jhelp/pipeSample"));

      String message = null;
      do
      {
         try
         {
            message = new String(pipeReader.read());
            Debug.println(DebugLevel.INFORMATION, "READ:", message);
         }
         catch(final Exception exception)
         {
            Debug.printException(exception, "Issue on reading");
         }
      }
      while("--END--".equals(message) == false);
   }
}