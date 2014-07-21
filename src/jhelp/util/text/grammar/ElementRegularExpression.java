package jhelp.util.text.grammar;

import java.util.concurrent.atomic.AtomicInteger;

import jhelp.util.text.UtilText;

/**
 * Element represents a string by a regular expression to catch it
 * 
 * @author JHelp
 */
public class ElementRegularExpression
      extends Element
{
   /** Next capturing group ID */
   private static AtomicInteger NEXT_ID;
   /** Capturing group name */
   private final String         groupName;
   /** Regular expression */
   private final String         regex;

   /**
    * Create a new instance of ElementRegularExpression
    * 
    * @param regex
    *           Regular expression
    */
   public ElementRegularExpression(final String regex)
   {
      if(ElementRegularExpression.NEXT_ID == null)
      {
         ElementRegularExpression.NEXT_ID = new AtomicInteger(0);
      }

      final int id = ElementRegularExpression.NEXT_ID.getAndIncrement();
      this.groupName = "JHelpElementCharacterReference" + id;
      this.regex = regex;
   }

   /**
    * Group name where element is captured <br>
    * <br>
    * <b>Parent documentation:</b><br>
    * {@inheritDoc}
    * 
    * @return Group name where element is captured
    * @see jhelp.util.text.grammar.Element#groupName()
    */
   @Override
   public String groupName()
   {
      return this.groupName;
   }

   /**
    * Indicates if element is reference to an other rule <br>
    * <br>
    * <b>Parent documentation:</b><br>
    * {@inheritDoc}
    * 
    * @return {@code false}
    * @see jhelp.util.text.grammar.Element#isReference()
    */
   @Override
   public boolean isReference()
   {
      return false;
   }

   /**
    * Regular expression used to capture the element.<br>
    * The capture group name is {@link #groupName()} <br>
    * <br>
    * <b>Parent documentation:</b><br>
    * {@inheritDoc}
    * 
    * @return Regular expression used to capture the element
    * @see jhelp.util.text.grammar.Element#toRegex()
    */
   @Override
   public String toRegex()
   {
      if(this.regex.isEmpty() == true)
      {
         return "";
      }

      return UtilText.concatenate("\\s*(?<", this.groupName, ">", this.regex, ")\\s*");
   }

   /**
    * String representation <br>
    * <br>
    * <b>Parent documentation:</b><br>
    * {@inheritDoc}
    * 
    * @return String representation
    * @see jhelp.util.text.grammar.Element#toString()
    */
   @Override
   public String toString()
   {
      return this.regex;
   }
}