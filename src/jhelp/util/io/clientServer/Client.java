package jhelp.util.io.clientServer;

import java.net.InetAddress;
import java.net.Socket;

import jhelp.util.debug.Debug;
import jhelp.util.thread.ThreadManager;
import jhelp.util.thread.ThreadedVerySimpleTask;

/**
 * Client side
 *
 * @author JHelp <br>
 */
public class Client
{
    /**
     * Conversation with server
     */
    private final ClientCommunication   clientCommunication;
    /**
     * Task for connect
     */
    private final TaskConnectedToServer taskConnectedToServer;
    /**
     * Server socket
     */
    private       Socket                socket;
    /**
     * Create a new instance of Client
     *
     * @param clientCommunication Conversation with server
     */
    public Client(final ClientCommunication clientCommunication)
    {
        if (clientCommunication == null)
        {
            throw new NullPointerException("clientCommunication MUST NOT be null");
        }

        this.clientCommunication = clientCommunication;
        this.taskConnectedToServer = new TaskConnectedToServer();
    }

    /**
     * Connect to server
     */
    void doTaskConnectedToServer()
    {
        try
        {
            this.clientCommunication.connectToServer(this.socket);
        }
        catch (final Exception exception)
        {
            Debug.printException(exception);
        }
    }

    /**
     * Connect to server
     *
     * @param serverAddress Server address
     * @param port          Server port
     * @throws ClientException On connection issue
     */
    public void connect(final String serverAddress, final int port) throws ClientException
    {
        if (this.socket != null)
        {
            return;
        }

        try
        {
            this.socket = new Socket(InetAddress.getByName(serverAddress), port);
            ThreadManager.THREAD_MANAGER.doThread(this.taskConnectedToServer, null);
        }
        catch (final Exception exception)
        {
            throw new ClientException(exception, "Failed to connect to server");
        }
    }

    /**
     * Disconnect the client
     *
     * @throws ClientException On disconnection issue
     */
    public void disconnect() throws ClientException
    {
        if (this.socket == null)
        {
            return;
        }

        try
        {
            this.socket.close();
        }
        catch (final Exception exception)
        {
            throw new ClientException(exception, "Failed to disconnect client");
        }
        finally
        {
            this.socket = null;
        }
    }

    /**
     * Indicates if client is connected
     *
     * @return {code true} if client is connected
     */
    public boolean isConnected()
    {
        return this.socket != null;
    }

    /**
     * Task for connect to server
     *
     * @author JHelp <br>
     */
    class TaskConnectedToServer
            extends ThreadedVerySimpleTask
    {
        /**
         * Create a new instance of TaskConnectedToServer
         */
        TaskConnectedToServer()
        {
        }

        /**
         * Do the task : connect to server <br>
         * <br>
         * <b>Parent documentation:</b><br>
         * {@inheritDoc}
         *
         * @see jhelp.util.thread.ThreadedVerySimpleTask#doVerySimpleAction()
         */
        @Override
        protected void doVerySimpleAction()
        {
            Client.this.doTaskConnectedToServer();
        }
    }
}