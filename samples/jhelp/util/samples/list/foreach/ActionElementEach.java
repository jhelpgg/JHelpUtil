package jhelp.util.samples.list.foreach;

import jhelp.util.list.foreach.AbstractActionEach;

/**
 * Action to do in for each sample.
 *
 * @author JHelp <br>
 */
public class ActionElementEach
      extends AbstractActionEach<ElementEach>
{
   /**
    * Create a new instance of ActionElementEach.
    */
   public ActionElementEach()
   {
   }

   /**
    * Do the action on element <br>
    * <br>
    * <b>Parent documentation:</b><br>
    * {@inheritDoc}
    *
    * @param element
    *           Element where do the action
    * @see jhelp.util.list.foreach.ActionEach#doAction(java.lang.Object)
    */
   @Override
   public void doAction(final ElementEach element)
   {
      element.doSomething();
   }
}