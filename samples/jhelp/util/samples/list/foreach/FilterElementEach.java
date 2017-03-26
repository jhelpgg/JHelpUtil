package jhelp.util.samples.list.foreach;

import jhelp.util.list.foreach.FilterEach;

/**
 * Filter element accept if for each
 *
 * @author JHelp <br>
 */
public class FilterElementEach
      implements FilterEach<ElementEach>
{
   /**
    * Create a new instance of FilterElementEach
    */
   public FilterElementEach()
   {
   }

   /**
    * Indicates if given element is filtered <br>
    * <br>
    * <b>Parent documentation:</b><br>
    * {@inheritDoc}
    *
    * @param element
    *           Tested element
    * @return {@code true} if given element is filtered
    * @see jhelp.util.list.foreach.FilterEach#isFiltered(java.lang.Object)
    */
   @Override
   public boolean isFiltered(final ElementEach element)
   {
      return (element.getID() % 2) == 0;
   }
}