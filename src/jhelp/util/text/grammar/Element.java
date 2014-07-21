package jhelp.util.text.grammar;

/**
 * Element of {@link SimpleDefinition}.<br>
 * A regular expression OR a rule reference
 * 
 * @author JHelp
 */
public abstract class Element
{
   /** The special empty rule 0 */
   public static final Element EMPTY = new ElementRegularExpression("");

   /**
    * Create a new instance of Element
    */
   public Element()
   {
   }

   /**
    * Group name used during capturing string
    * 
    * @return Group name used during capturing string
    */
   public abstract String groupName();

   /**
    * Indicates if element is a rule reference
    * 
    * @return {@code true} if element is a rule reference
    */
   public abstract boolean isReference();

   /**
    * Regular expression to use to catch the element.<br>
    * The capturing group name is {@link #groupName()}
    * 
    * @return Regular expression to use to catch the element
    */
   public abstract String toRegex();

   /**
    * String representation <br>
    * <br>
    * <b>Parent documentation:</b><br>
    * {@inheritDoc}
    * 
    * @return String representation
    * @see java.lang.Object#toString()
    */
   @Override
   public abstract String toString();
}