package jhelp.util.text.grammar;

/**
 * Constants used by grammar parsing
 * 
 * @author JHelp
 */
public interface GrammarConstants
{
   /** Definition capturing group name */
   public static final String GROUP_DEFINITION  = "definition";
   /** Name capturing group name */
   public static final String GROUP_NAME        = "name";
   /**
    * Regular expression to obtain an affectation. If succeed it captures {@link #GROUP_NAME rule name} and
    * {@link #GROUP_DEFINITION rule definition}
    */
   public static final String REGEX_AFFECTATION = "<(?<name>[a-zA-Z][a-zA-Z0-9]*)>\\s*:=\\s*(?<definition>.*)";
   /** Capture a definition. If succeed it capture the {@link #GROUP_DEFINITION definition} */
   public static final String REGEX_CHOICE      = "\\|\\s*(?<definition>.*)";
   /** Indicates an empty set in definition */
   public static final String REGEX_EMPTY       = "0";
   /** Capture a rule reference. If succeed it captures then {@link #GROUP_NAME referenced rule name} */
   public static final String REGEX_REFERENCE   = "<(?<name>[a-zA-Z][a-zA-Z0-9]*)>";
   /** Capture a regular expression. If succeed it capture the {@link #GROUP_DEFINITION regular expression} */
   public static final String REGEX_REGEX       = "\"(?<definition>.*)\"";
}