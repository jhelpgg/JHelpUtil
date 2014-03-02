package jhelp.util.list.foreach;

/**
 * Filter of element in {@link ForEach}
 * 
 * @author JHelp
 * @param <ELEMENT>
 *           Element filtered type
 */
public interface FilterEach<ELEMENT>
{
   /**
    * Indicates if element is filtered
    * 
    * @param element
    *           Tested element
    * @return {@code true} if element is filtered
    */
   public boolean isFiltered(ELEMENT element);
}