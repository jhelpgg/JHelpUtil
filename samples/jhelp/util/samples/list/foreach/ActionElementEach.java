package jhelp.util.samples.list.foreach;

import jhelp.util.list.foreach.AbstractActionEach;

public class ActionElementEach
      extends AbstractActionEach<ElementEach>
{

   public ActionElementEach()
   {
   }

   @Override
   public void doAction(final ElementEach element)
   {
      element.doSomething();
   }
}