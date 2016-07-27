package jhelp.util.io.clientServer;

import java.net.Socket;

public interface ClientCommunication
{
   public void connectToServer(Socket socket) throws ClientException;
}