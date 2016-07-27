package jhelp.util.io.clientServer;

import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

import jhelp.util.debug.Debug;
import jhelp.util.io.UtilIO;
import jhelp.util.thread.ThreadManager;
import jhelp.util.thread.ThreadedSimpleTask;
import jhelp.util.thread.ThreadedVerySimpleTask;

public class Server
{
   class TaskClientConnected
         extends ThreadedSimpleTask<Socket>
   {
      TaskClientConnected()
      {
      }

      @Override
      protected void doSimpleAction(final Socket socket)
      {
         Server.this.doTaskClientConnected(socket);
      }

   }

   class TaskWaitClient
         extends ThreadedVerySimpleTask
   {
      TaskWaitClient()
      {
      }

      @Override
      protected void doVerySimpleAction()
      {
         Server.this.doTaskWaitClient();
      }
   }

   private final InetAddress         inetAddress;
   private int                       port;
   private final ServerCommunication serverCommunication;
   private ServerSocket              serverSocket;
   private final TaskClientConnected taskClientConnected;
   private final TaskWaitClient      taskWaitClient;

   public Server(final boolean onlyIPv4, final ServerCommunication serverCommunication)
         throws ServerException
   {
      if(serverCommunication == null)
      {
         throw new NullPointerException("serverCommunication musn't be null");
      }

      this.serverCommunication = serverCommunication;
      this.inetAddress = UtilIO.obtainLocalInetAddress(onlyIPv4);

      if(this.inetAddress == null)
      {
         throw new ServerException("Can't obtain local IP");
      }

      this.port = -1;
      this.taskWaitClient = new TaskWaitClient();
      this.taskClientConnected = new TaskClientConnected();
   }

   void doTaskClientConnected(final Socket socket)
   {
      try
      {
         this.serverCommunication.clientConnected(socket);
      }
      catch(final ServerException exception)
      {
         Debug.printException(exception);
      }
   }

   void doTaskWaitClient()
   {
      try
      {
         final Socket socket = this.serverSocket.accept();
         ThreadManager.THREAD_MANAGER.doThread(this.taskClientConnected, socket);
      }
      catch(final Exception exception)
      {
         Debug.printException(exception);
      }

      if(this.serverSocket != null)
      {
         ThreadManager.THREAD_MANAGER.doThread(this.taskWaitClient, null);
      }
   }

   public void connect() throws ServerException
   {
      if(this.serverSocket != null)
      {
         return;
      }

      try
      {
         this.serverSocket = new ServerSocket(0, 50, this.inetAddress);
         this.port = this.serverSocket.getLocalPort();
         ThreadManager.THREAD_MANAGER.doThread(this.taskWaitClient, null);
      }
      catch(final Exception exception)
      {
         throw new ServerException(exception, "Failed to connect the server");
      }
   }

   public void disconnect() throws ServerException
   {
      if(this.serverSocket == null)
      {
         return;
      }

      this.port = -1;

      try
      {
         this.serverSocket.close();
      }
      catch(final Exception exception)
      {
         throw new ServerException(exception, "Failed to disconnect the server");
      }
      finally
      {
         this.serverSocket = null;
      }
   }

   public String getLocalIPName()
   {
      return this.inetAddress.getHostAddress();
   }

   public int getPort()
   {
      return this.port;
   }

   public boolean isConnected()
   {
      return this.serverSocket != null;
   }
}