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