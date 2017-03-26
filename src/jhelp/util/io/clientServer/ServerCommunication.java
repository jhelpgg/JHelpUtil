package jhelp.util.io.clientServer;

import java.net.Socket;

/**
 * Communication with server
 *
 * @author JHelp <br>
 */
public interface ServerCommunication
{
   /**
    * Called when client connect
    *
    * @param socket
    *           Client socket
    * @throws ServerException
    *            On issue
    */
   public void clientConnected(Socket socket) throws ServerException;
}