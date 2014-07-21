package jhelp.util.text.grammar;

import jhelp.util.text.UtilText;

/**
 * Exception happen on grammar parsing or validate a text to a grammar
 * 
 * @author JHelp
 */
public class GrammarException
      extends Exception
{
   /** Line where exception happen. -1 if unknown */
   private final int line;

   /**
    * Create a new instance of GrammarException
    * 
    * @param line
    *           Line where exception happen. -1 if unknown
    * @param message
    *           Exception message
    */
   public GrammarException(final int line, final String message)
   {
      super(UtilText.concatenate(message, " at line ", line));
      this.line = line;
   }

   /**
    * Create a new instance of GrammarException
    * 
    * @param line
    *           Line where exception happen. -1 if unknown
    * @param message
    *           Exception message
    * @param cause
    *           Cause of exception
    */
   public GrammarException(final int line, final String message, final Throwable cause)
   {
      super(UtilText.concatenate(message, " at line ", line), cause);

      this.line = line;
   }

   /**
    * Line where exception happen.<br>
    * -1 if unknown
    * 
    * @return Line where exception happen. -1 if unknown
    */
   public int getLine()
   {
      return this.line;
   }
}