package jhelp.util.io.clientServer;

import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

import jhelp.util.debug.Debug;
import jhelp.util.io.UtilIO;
import jhelp.util.thread.ThreadManager;
import jhelp.util.thread.ThreadedSimpleTask;
import jhelp.util.thread.ThreadedVerySimpleTask;

/**
 * Server side
 *
 * @author JHelp <br>
 */
public class Server
{
    /**
     * Server address
     */
    private final InetAddress         inetAddress;
    /**
     * Task for communicate with client
     */
    private final ServerCommunication serverCommunication;
    /**
     * Task for dialog with client
     */
    private final TaskClientConnected taskClientConnected;
    /**
     * Task for wait next client
     */
    private final TaskWaitClient      taskWaitClient;
    /**
     * Server port
     */
    private       int                 port;
    /**
     * Server socket
     */
    private       ServerSocket        serverSocket;
    /**
     * Create a new instance of Server
     *
     * @param onlyIPv4            Indicates if only IPV4 address
     * @param serverCommunication Task for communicate with a client
     * @throws ServerException On creation issue
     */
    public Server(final boolean onlyIPv4, final ServerCommunication serverCommunication)
            throws ServerException
    {
        if (serverCommunication == null)
        {
            throw new NullPointerException("serverCommunication MUST NOT be null");
        }

        this.serverCommunication = serverCommunication;
        this.inetAddress = UtilIO.obtainLocalInetAddress(onlyIPv4);

        if (this.inetAddress == null)
        {
            throw new ServerException("Can't obtain local IP");
        }

        this.port = -1;
        this.taskWaitClient = new TaskWaitClient();
        this.taskClientConnected = new TaskClientConnected();
    }

    /**
     * Called when a client connect
     *
     * @param socket Client socket
     */
    void doTaskClientConnected(final Socket socket)
    {
        try
        {
            this.serverCommunication.clientConnected(socket);
        }
        catch (final ServerException exception)
        {
            Debug.printException(exception);
        }
    }

    /**
     * Wait for next client
     */
    void doTaskWaitClient()
    {
        try
        {
            final Socket socket = this.serverSocket.accept();
            ThreadManager.THREAD_MANAGER.doThread(this.taskClientConnected, socket);
        }
        catch (final Exception exception)
        {
            Debug.printException(exception);
        }

        if (this.serverSocket != null)
        {
            ThreadManager.THREAD_MANAGER.doThread(this.taskWaitClient, null);
        }
    }

    /**
     * Connect the server
     *
     * @throws ServerException On connection issue
     */
    public void connect() throws ServerException
    {
        if (this.serverSocket != null)
        {
            return;
        }

        try
        {
            this.serverSocket = new ServerSocket(0, 50, this.inetAddress);
            this.port = this.serverSocket.getLocalPort();
            ThreadManager.THREAD_MANAGER.doThread(this.taskWaitClient, null);
        }
        catch (final Exception exception)
        {
            throw new ServerException(exception, "Failed to connect the server");
        }
    }

    /**
     * Disconnect the server
     *
     * @throws ServerException On disconnection issue
     */
    public void disconnect() throws ServerException
    {
        if (this.serverSocket == null)
        {
            return;
        }

        this.port = -1;

        try
        {
            this.serverSocket.close();
        }
        catch (final Exception exception)
        {
            throw new ServerException(exception, "Failed to disconnect the server");
        }
        finally
        {
            this.serverSocket = null;
        }
    }

    /**
     * Server IP
     *
     * @return Server IP
     */
    public String getLocalIPName()
    {
        return this.inetAddress.getHostAddress();
    }

    /**
     * Server port
     *
     * @return Server port
     */
    public int getPort()
    {
        return this.port;
    }

    /**
     * Indicates if server is connected
     *
     * @return {@code true} if server is connected
     */
    public boolean isConnected()
    {
        return this.serverSocket != null;
    }

    /**
     * Task used when client connect
     *
     * @author JHelp <br>
     */
    class TaskClientConnected
            extends ThreadedSimpleTask<Socket>
    {
        /**
         * Create a new instance of TaskClientConnected
         */
        TaskClientConnected()
        {
        }

        /**
         * Do the task : dialog with client in separate thread <br>
         * <br>
         * <b>Parent documentation:</b><br>
         * {@inheritDoc}
         *
         * @param socket Client socket
         * @see jhelp.util.thread.ThreadedSimpleTask#doSimpleAction(java.lang.Object)
         */
        @Override
        protected void doSimpleAction(final Socket socket)
        {
            Server.this.doTaskClientConnected(socket);
        }

    }

    /**
     * Task for wait next client
     *
     * @author JHelp <br>
     */
    class TaskWaitClient
            extends ThreadedVerySimpleTask
    {
        /**
         * Create a new instance of TaskWaitClient
         */
        TaskWaitClient()
        {
        }

        /**
         * Do the task : wait for next client <br>
         * <br>
         * <b>Parent documentation:</b><br>
         * {@inheritDoc}
         *
         * @see jhelp.util.thread.ThreadedVerySimpleTask#doVerySimpleAction()
         */
        @Override
        protected void doVerySimpleAction()
        {
            Server.this.doTaskWaitClient();
        }
    }
}