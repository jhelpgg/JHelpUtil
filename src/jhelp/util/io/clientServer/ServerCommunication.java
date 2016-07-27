package jhelp.util.io.clientServer;

import java.net.Socket;

public interface ServerCommunication
{
   public void clientConnected(Socket socket) throws ServerException;
}