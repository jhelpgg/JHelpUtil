package jhelp.util.list.foreach;

import jhelp.util.debug.Debug;

/**
 * Abstract class that implements {@link ActionEach} for doing default exception and error catching : print them in console
 * 
 * @author JHelp
 * @param <ELEMENT>
 *           Element type
 */
public abstract class AbstractActionEach<ELEMENT>
      implements ActionEach<ELEMENT>
{
   /**
    * Create a new instance of AbstractActionEach
    */
   public AbstractActionEach()
   {
   }

   /**
    * Print the error append when doing action.<br>
    * Can be override to do something else <br>
    * <br>
    * <b>Parent documentation:</b><br>
    * {@inheritDoc}
    * 
    * @param element
    *           Element with the error happen
    * @param error
    *           Error happen
    * @see jhelp.util.list.foreach.ActionEach#report(java.lang.Object, java.lang.Error)
    */
   @Override
   public void report(final ELEMENT element, final Error error)
   {
      Debug.printError(error, "Issue on doing action with ", element);
   }

   /**
    * Print the exception append when doing action.<br>
    * Can be override to do something else <br>
    * <br>
    * <b>Parent documentation:</b><br>
    * {@inheritDoc}
    * 
    * @param element
    *           Element with the exception happen
    * @param exception
    *           Exception happen
    * @see jhelp.util.list.foreach.ActionEach#report(java.lang.Object, java.lang.Exception)
    */
   @Override
   public void report(final ELEMENT element, final Exception exception)
   {
      Debug.printException(exception, "Issue on doing action with ", element);
   }
}