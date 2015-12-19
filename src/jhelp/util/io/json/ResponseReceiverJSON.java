/**
 * <h1>License :</h1> <br>
 * The following code is deliver as is. I take care that code compile and work, but I am not responsible about any damage it may
 * cause.<br>
 * You can use, modify, the code as your need for any usage. But you can't do any action that avoid me or other person use,
 * modify this code. The code is free for usage and modification, you can't change that fact.<br>
 * <br>
 * 
 * @author JHelp
 */
package jhelp.util.io.json;

import java.net.URL;

import jhelp.util.io.net.ResponseReceiver;

/**
 * Response receiver of server formated on JSON
 * 
 * @author JHelp
 */
public class ResponseReceiverJSON
      implements ResponseReceiver
{
   /** Listener that received formated response */
   private final ResponseReceiverJSONListener listener;
   /** Synchronization lock */
   private final Object                       lock = new Object();
   /** Actual response part */
   private StringBuilder                      stringBuilder;

   /**
    * Create a new instance of ResponseReceiverJSON
    * 
    * @param listener
    *           Listener that received formated response
    */
   public ResponseReceiverJSON(final ResponseReceiverJSONListener listener)
   {
      if(listener == null)
      {
         throw new NullPointerException("listener musn't be null");
      }

      this.listener = listener;
   }

   /**
    * Called if connection failed <br>
    * <br>
    * <b>Parent documentation:</b><br>
    * {@inheritDoc}
    * 
    * @param exception
    *           Exception happen
    * @param url
    *           URL tried to reach
    * @see jhelp.util.io.net.ResponseReceiver#connectionFailed(java.lang.Exception, java.net.URL)
    */
   @Override
   public void connectionFailed(final Exception exception, final URL url)
   {
      this.listener.connectionFailed(exception, url);
   }

   /**
    * Called when server response is complete <br>
    * <br>
    * <b>Parent documentation:</b><br>
    * {@inheritDoc}
    * 
    * @see jhelp.util.io.net.ResponseReceiver#endOfResponse()
    */
   @Override
   public void endOfResponse()
   {
      synchronized(this.lock)
      {
         final ObjectJSON objectJSON = ObjectJSON.parse(this.stringBuilder.toString());
         this.listener.responseReceived(objectJSON);
      }
   }

   /**
    * Called if server error message is filled <br>
    * <br>
    * <b>Parent documentation:</b><br>
    * {@inheritDoc}
    * 
    * @param message
    *           Error message
    * @see jhelp.util.io.net.ResponseReceiver#serverError(java.lang.String)
    */
   @Override
   public void serverError(final String message)
   {
      this.listener.serverError(message);
   }

   /**
    * Called when received server message part <br>
    * <br>
    * <b>Parent documentation:</b><br>
    * {@inheritDoc}
    * 
    * @param message
    *           Message part data
    * @param offset
    *           Offset start read data
    * @param length
    *           Message size
    * @see jhelp.util.io.net.ResponseReceiver#serverMessage(byte[], int, int)
    */
   @Override
   public void serverMessage(final byte[] message, final int offset, final int length)
   {
      synchronized(this.lock)
      {
         this.stringBuilder.append(new String(message, offset, length));
      }
   }

   /**
    * Called just before server start its response <br>
    * <br>
    * <b>Parent documentation:</b><br>
    * {@inheritDoc}
    * 
    * @see jhelp.util.io.net.ResponseReceiver#startOfResponse()
    */
   @Override
   public void startOfResponse()
   {
      synchronized(this.lock)
      {
         this.stringBuilder = new StringBuilder();
      }
   }
}