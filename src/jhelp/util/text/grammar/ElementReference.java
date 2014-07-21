package jhelp.util.text.grammar;

import jhelp.util.text.UtilText;

/**
 * Element reference to a rule
 * 
 * @author JHelp
 */
public class ElementReference
      extends Element
{
   /** Referenced rule */
   private final String reference;

   /**
    * Create a new instance of ElementReference
    * 
    * @param reference
    *           Referenced rule
    */
   public ElementReference(final String reference)
   {
      this.reference = reference;
   }

   /**
    * Capture group name <br>
    * <br>
    * <b>Parent documentation:</b><br>
    * {@inheritDoc}
    * 
    * @return Capture group name
    * @see jhelp.util.text.grammar.Element#groupName()
    */
   @Override
   public String groupName()
   {
      return this.reference;
   }

   /**
    * Indicates if this element refer to a rule <br>
    * <br>
    * <b>Parent documentation:</b><br>
    * {@inheritDoc}
    * 
    * @return {@code true}
    * @see jhelp.util.text.grammar.Element#isReference()
    */
   @Override
   public boolean isReference()
   {
      return true;
   }

   /**
    * Regular expression used to capture the element.<br>
    * Capturing group name is {@link #groupName()} <br>
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
      return UtilText.concatenate("\\s*(?<", this.reference, ">.*)\\s*");
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
      return this.reference;
   }
}