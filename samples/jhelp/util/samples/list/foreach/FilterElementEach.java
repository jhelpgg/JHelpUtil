package jhelp.util.samples.list.foreach;

import jhelp.util.list.foreach.FilterEach;

public class FilterElementEach
      implements FilterEach<ElementEach>
{
   public FilterElementEach()
   {
   }

   @Override
   public boolean isFiltered(final ElementEach element)
   {
      return (element.getID() % 2) == 0;
   }
}