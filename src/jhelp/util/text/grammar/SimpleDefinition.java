package jhelp.util.text.grammar;

import java.util.ArrayList;
import java.util.List;

/**
 * A definition
 * 
 * @author JHelp
 */
public class SimpleDefinition
{
   /** Definition elements */
   private final List<Element> elements;

   /**
    * Create a new instance of SimpleDefinition
    */
   public SimpleDefinition()
   {
      this.elements = new ArrayList<Element>();
   }

   /**
    * Add one element
    * 
    * @param element
    *           Element to add
    */
   public void addElement(final Element element)
   {
      if(element == null)
      {
         throw new NullPointerException("element musn't be null");
      }

      this.elements.add(element);
   }

   /**
    * Clear the definition.<br>
    * The definition will contains nothing
    */
   public void clear()
   {
      this.elements.clear();
   }

   /**
    * Obtain one element
    * 
    * @param index
    *           Element index
    * @return The element
    */
   public Element getElement(final int index)
   {
      return this.elements.get(index);
   }

   /**
    * Number of elements
    * 
    * @return Number of elements
    */
   public int getElementCount()
   {
      return this.elements.size();
   }

   /**
    * Regular expression to obtain the definition<br>
    * To have each element capturing group, ask to each element its group name with {@link Element#groupName()}
    * 
    * @return Regular expression to obtain the definition
    */
   public String toRegex()
   {
      final StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append('^');

      for(final Element element : this.elements)
      {
         stringBuilder.append(element.toRegex());
      }

      stringBuilder.append('$');
      return stringBuilder.toString();
   }
}