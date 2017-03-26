package jhelp.util.samples.io.pipe;

import java.io.File;

import jhelp.util.debug.Debug;
import jhelp.util.debug.DebugLevel;
import jhelp.util.io.pipe.PipeWriter;

/**
 * Launch pipe writer in pipe sample
 *
 * @author JHelp <br>
 */
public class SampleWriter
{

   /**
    * Launch pipe writer in pipe sample
    *
    * @param args
    *           Unused
    */
   public static void main(final String[] args)
   {
      // final Properties properties = System.getProperties();
      // for(final Entry<Object, Object> entry : properties.entrySet())
      // {
      // Debug.println(DebugLevel.INFORMATION, entry.getKey(), '=', entry.getValue(), " [", entry.getValue() != null
      // ? entry.getValue().getClass().getName()
      // : "NULL", ']');
      // }

      final PipeWriter pipeWriter = new PipeWriter(new File("/home/jhelp/pipeSample"));

      for(int i = 0; i < 2048; i++)
      {
         try
         {
            Debug.println(DebugLevel.INFORMATION, "WRITE:", "Message numéro ", i);
            pipeWriter.write(("Message numéro " + i).getBytes());
         }
         catch(final Exception exception)
         {
            Debug.printException(exception, "Issue on writing message : ", i);
         }
      }

      try
      {
         Debug.println(DebugLevel.INFORMATION, "WRITE:--END--");
         pipeWriter.write("--END--".getBytes());
      }
      catch(final Exception exception)
      {
         Debug.printException(exception, "Issue on writing message : ", "--END--");
      }
   }
}