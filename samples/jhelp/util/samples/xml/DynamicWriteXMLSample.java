package jhelp.util.samples.xml;

import java.io.IOException;

import jhelp.util.debug.Debug;
import jhelp.util.xml.DynamicWriteXML;

public class DynamicWriteXMLSample
{

   /**
    * TODO Explains what does the method main in jhelp.util.samples.xml [JHelpUtil]
    * 
    * @param args
    */
   public static void main(final String[] args)
   {
      try
      {
         final DynamicWriteXML dynamicWriteXML = new DynamicWriteXML(System.out, false, false);

         dynamicWriteXML.appendComment("The scene");
         dynamicWriteXML.openMarkup("Scene");
         for(int i = 0; i < 5; i++)
         {
            dynamicWriteXML.openMarkup("Object");
            dynamicWriteXML.appendParameter("id", i);
            dynamicWriteXML.appendParameter("name", "object" + i);
            dynamicWriteXML.closeMarkup();
            dynamicWriteXML.appendComment("This was the object " + i);
         }
         dynamicWriteXML.closeMarkup();
      }
      catch(final IOException exception)
      {
         Debug.printException(exception, "Issue while writing XML");
      }
   }
}