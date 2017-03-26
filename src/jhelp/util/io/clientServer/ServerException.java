package jhelp.util.io.clientServer;

import jhelp.util.text.UtilText;

/**
 * Exception may happen in server side
 *
 * @author JHelp <br>
 */
public class ServerException
      extends Exception
{
   /**
    * Create a new instance of ServerException
    *
    * @param message
    *           Message
    */
   public ServerException(final Object... message)
   {
      super(UtilText.concatenate(message));
   }

   /**
    * Create a new instance of ServerException
    *
    * @param cause
    *           Exception cause
    * @param message
    *           Message
    */
   public ServerException(final Throwable cause, final Object... message)
   {
      super(UtilText.concatenate(message), cause);
   }
}