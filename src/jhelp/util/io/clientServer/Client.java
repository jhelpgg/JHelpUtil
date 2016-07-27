package jhelp.util.io.clientServer;

import java.net.InetAddress;
import java.net.Socket;

import jhelp.util.debug.Debug;
import jhelp.util.thread.ThreadManager;
import jhelp.util.thread.ThreadedVerySimpleTask;

public class Client
{
   class TaskConnectedToServer
         extends ThreadedVerySimpleTask
   {
      TaskConnectedToServer()
      {
      }

      @Override
      protected void doVerySimpleAction()
      {
         Client.this.doTaskConnectedToServer();
      }
   }

   private final ClientCommunication   clientCommunication;
   private Socket                      socket;
   private final TaskConnectedToServer taskConnectedToServer;

   public Client(final ClientCommunication clientCommunication)
   {
      if(clientCommunication == null)
      {
         throw new NullPointerException("clientCommunication musn't be null");
      }

      this.clientCommunication = clientCommunication;
      this.taskConnectedToServer = new TaskConnectedToServer();
   }

   void doTaskConnectedToServer()
   {
      try
      {
         this.clientCommunication.connectToServer(this.socket);
      }
      catch(final Exception exception)
      {
         Debug.printException(exception);
      }
   }

   public void connect(final String serverAddress, final int port) throws ClientException
   {
      if(this.socket != null)
      {
         return;
      }

      try
      {
         this.socket = new Socket(InetAddress.getByName(serverAddress), port);
         ThreadManager.THREAD_MANAGER.doThread(this.taskConnectedToServer, null);
      }
      catch(final Exception exception)
      {
         throw new ClientException(exception, "Failed to connect to server");
      }
   }

   public void disconnect() throws ClientException
   {
      if(this.socket == null)
      {
         return;
      }

      try
      {
         this.socket.close();
      }
      catch(final Exception exception)
      {
         throw new ClientException(exception, "Failed to disconnect client");
      }
      finally
      {
         this.socket = null;
      }
   }

   public boolean isConnected()
   {
      return this.socket != null;
   }
}