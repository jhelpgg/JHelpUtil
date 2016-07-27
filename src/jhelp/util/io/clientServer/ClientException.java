package jhelp.util.io.clientServer;

import jhelp.util.text.UtilText;

public class ClientException
      extends Exception
{

   public ClientException(final Object... message)
   {
      super(UtilText.concatenate(message));
   }

   public ClientException(final Throwable cause, final Object... message)
   {
      super(UtilText.concatenate(message), cause);
   }
}