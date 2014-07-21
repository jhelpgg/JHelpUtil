package jhelp.util.io.json;

import java.net.URL;

/**
 * Receive server message JSON formated
 * 
 * @author JHelp
 */
public interface ResponseReceiverJSONListener
{
   /**
    * Called if connection failed
    * 
    * @param exception
    *           Exception happen
    * @param url
    *           URL tried to connect
    */
   public void connectionFailed(Exception exception, URL url);

   /**
    * Called when response finished to be received.<br>
    * It returns{@code null} if response is not JSON format
    * 
    * @param objectJSON
    *           Server response or {@code null} if response not JSON format
    */
   public void responseReceived(ObjectJSON objectJSON);

   /**
    * Called if server error message is fill
    * 
    * @param message
    *           Server error message
    */
   public void serverError(String message);
}