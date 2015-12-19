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
package jhelp.util.io.net;

import java.net.URL;

/**
 * Listener of server response
 * 
 * @author JHelp
 */
public interface ResponseReceiver
{
   /**
    * Called if connection failed
    * 
    * @param exception
    *           Exception give by connection
    * @param url
    *           URL try to be connected
    */
   public void connectionFailed(Exception exception, URL url);

   /**
    * Called when server finished to send response
    */
   public void endOfResponse();

   /**
    * Called when error message as fill on connection
    * 
    * @param message
    *           Error message
    */
   public void serverError(String message);

   /**
    * Called each time a part of server message recive
    * 
    * @param message
    *           Message part data
    * @param offset
    *           Offset to start read data
    * @param length
    *           Number of byte to read
    */
   public void serverMessage(byte[] message, int offset, int length);

   /**
    * Called just before receive sever response
    */
   public void startOfResponse();
}