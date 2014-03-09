package jhelp.util.list.foreach;

/**
 * Action to do on each element in {@link ForEach}
 * 
 * @author JHelp
 * @param <ELEMENT>
 *           Element type
 */
public interface ActionEach<ELEMENT>
{
   /**
    * Do the action on element
    * 
    * @param element
    *           Element where do the action
    */
   public void doAction(ELEMENT element);

   /**
    * Called if any error is throw while doing the task
    * 
    * @param element
    *           Element with error happen
    * @param error
    *           Error throw
    */
   public void report(ELEMENT element, Error error);

   /**
    * Called if any execption is throw while doing the task
    * 
    * @param element
    *           Element with execption happen
    * @param exception
    *           Exception throw
    */
   public void report(ELEMENT element, Exception exception);
}