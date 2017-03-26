package jhelp.util.io.clientServer;

import jhelp.util.text.UtilText;

/**
 * Exception happen in client side
 *
 * @author JHelp <br>
 */
public class ClientException
      extends Exception
{
   /**
    * Create a new instance of ClientException
    *
    * @param message
    *           Message
    */
   public ClientException(final Object... message)
   {
      super(UtilText.concatenate(message));
   }

   /**
    * Create a new instance of ClientException
    *
    * @param cause
    *           Cause
    * @param message
    *           Message
    */
   public ClientException(final Throwable cause, final Object... message)
   {
      super(UtilText.concatenate(message), cause);
   }
}