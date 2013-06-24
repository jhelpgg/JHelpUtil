package jhelp.util.samples.io.pipe;

import java.io.File;
import java.util.Map.Entry;
import java.util.Properties;

import jhelp.util.debug.Debug;
import jhelp.util.debug.DebugLevel;
import jhelp.util.io.pipe.PipeWriter;

public class SampleWriter
{

   /**
    * TODO Explains what does the method main in jhelp.util.samples.io.pipe [JHelpUtil]
    * 
    * @param args
    */
   public static void main(final String[] args)
   {
      final Properties properties = System.getProperties();
      for(final Entry<Object, Object> entry : properties.entrySet())
      {
         Debug.println(DebugLevel.INFORMATION, entry.getKey(), '=', entry.getValue(), " [", entry.getValue() != null
               ? entry.getValue().getClass().getName()
               : "NULL", ']');
      }

      final PipeWriter pipeWriter = new PipeWriter(new File("/home/jhelp/pipeSample"));

      for(int i = 0; i < 2048; i++)
      {
         try
         {
            pipeWriter.write(("Message numéro " + i).getBytes());
         }
         catch(final Exception exception)
         {
            Debug.printException(exception, "Issue on writing message : ", i);
         }
      }

      try
      {
         pipeWriter.write("--END--".getBytes());
      }
      catch(final Exception exception)
      {
         Debug.printException(exception, "Issue on writing message : ", "--END--");
      }
   }
}