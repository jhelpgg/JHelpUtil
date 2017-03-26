package jhelp.util.io.clientServer;

import java.net.Socket;

/**
 * Client talk to server
 *
 * @author JHelp <br>
 */
public interface ClientCommunication
{
   /**
    * Called when communication is established
    *
    * @param socket
    *           Server socket
    * @throws ClientException
    *            On communication issue
    */
   public void connectToServer(Socket socket) throws ClientException;
}