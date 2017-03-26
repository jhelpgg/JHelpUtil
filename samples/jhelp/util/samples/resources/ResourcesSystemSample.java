package jhelp.util.samples.resources;

import java.io.IOException;
import java.util.Stack;

import jhelp.util.debug.Debug;
import jhelp.util.debug.DebugLevel;
import jhelp.util.resources.ResourceElement;
import jhelp.util.resources.Resources;
import jhelp.util.resources.ResourcesSystem;

/**
 * Sample that show access to resource file system
 *
 * @author JHelp <br>
 */
public class ResourcesSystemSample
{

   /**
    * Launch the sample that show access to resource file system
    *
    * @param args
    *           Unused
    */
   public static void main(final String[] args)
   {
      final Resources resources = new Resources(ResourcesSystemSample.class);

      try
      {
         final ResourcesSystem resourcesSystem = resources.obtainResourcesSystem();
         final Stack<ResourceElement> stack = new Stack<ResourceElement>();
         stack.push(ResourcesSystem.ROOT);
         ResourceElement resourceElement;

         while(!stack.empty())
         {
            resourceElement = stack.pop();
            Debug.printMark(DebugLevel.INFORMATION, resourceElement.getPath());

            for(final ResourceElement element : resourcesSystem.obtainList(resourceElement))
            {
               if(element.isDirectory())
               {
                  stack.push(element);
               }
               else
               {
                  Debug.println(DebugLevel.INFORMATION, element.getPath());
               }
            }
         }
      }
      catch(final IOException exception)
      {
         Debug.printException(exception);
      }
   }
}