package jhelp.util.io.clientServer;

import jhelp.util.text.UtilText;

public class ServerException
      extends Exception
{

   public ServerException(final Object... message)
   {
      super(UtilText.concatenate(message));
   }

   public ServerException(final Throwable cause, final Object... message)
   {
      super(UtilText.concatenate(message), cause);
   }
}