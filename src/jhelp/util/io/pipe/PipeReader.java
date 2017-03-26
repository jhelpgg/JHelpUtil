/**
 * <h1>License :</h1> <br>
 * The following code is deliver as is. I take care that code compile and work, but I am not responsible about any
 * damage it may
 * cause.<br>
 * You can use, modify, the code as your need for any usage. But you can't do any action that avoid me or other person use,
 * modify this code. The code is free for usage and modification, you can't change that fact.<br>
 * <br>
 *
 * @author JHelp
 */
package jhelp.util.io.pipe;

import java.io.File;
import java.io.FileInputStream;

import jhelp.util.Utilities;
import jhelp.util.io.Binarizable;
import jhelp.util.io.ByteArray;
import jhelp.util.io.UtilIO;

/**
 * Pipe reader, for read a pipe messages <br>
 * 1) What is a pipe ?<br>
 * <br>
 * A pipe is a way to communicate between two different java application, that not neccessary use the same main process,
 * but
 * runs on same computer, or two computer that shares a same shared directory.<br>
 * A pipe is here simply a one way communication their a one, and only one, writer (The guys who write/send messages)
 * and one,
 * and only one, reader (The guy who read/receive messages)<br>
 * For a two ways communication, have to create two pipes, one for A =&gt; B and one for B =&gt; A..<br>
 * The implementation here is 100% Java, and compatible in any system have file system with at least an area
 * readable/writable
 * by the program.<br>
 * The implementation use a directory and temporary files for exchange message, so no need socket, and avoid in that way
 * proxy
 * issue and other network issue.<br>
 * The security is the same as the security of the used directory for exchange.<br>
 * <br>
 * 2) How to use pipes ? <br>
 * <br>
 * Choose a dedicated directory, this directory will be change during time, so don't use this directory for other stuff.
 * Then
 * create the writer in application that will send messages, and reader in application that receive messages. <br>
 * If you create several readers (For same pipe), you will notice some lost messages (That why only one reader is highly
 * recommended) <br>
 * If you create several writers (For same pipe), you will notice some lost messages and/or some writing issue (That why
 * only
 * one writer is highly recommended) <br>
 * You can transfer byte[] or Binarizable. You will find a helper for manage sending message in queue and one helper for
 * receive
 * messages in listener.
 *
 * @author JHelp
 */
public class PipeReader
        extends PipeCommons
{
    /** Pipe directory where read messages */
    private final File    pipeDirectory;
    /** Indicate if reader is still waiting next message */
    private       boolean alive;

    /**
     * Create a new instance of PipeReader
     *
     * @param pipeDirectory
     *           Pipe directory where read messages
     */
    public PipeReader(final File pipeDirectory)
    {
        if (pipeDirectory == null)
        {
            throw new NullPointerException("pipeDirectory MUST NOT be null");
        }

        this.pipeDirectory = pipeDirectory;
        this.alive = true;
    }

    /**
     * Read next message in {@link Binarizable} way
     *
     * @param <B>
     *           Binarizable type
     * @param binarizableClass
     *           Binarizable calss to read
     * @return Read binarizable
     * @throws PipeException
     *            If reader is stopped or the message can't be cast to the desired binarizable
     */
    public <B extends Binarizable> B read(final Class<B> binarizableClass) throws PipeException
    {
        try
        {
            final ByteArray byteArray = new ByteArray();
            byteArray.write(this.read());
            return byteArray.readBinarizable(binarizableClass);
        }
        catch (final Exception exception)
        {
            throw new PipeException(exception, "Can't convert read byte array to desired binarizable");
        }
    }

    /**
     * Read next message.<br>
     * This message will block until theire are a new message or we stop the reader.<br>
     * If the reader is stopped, {@code null} is return
     *
     * @return The next message or {@code null} if reader is stoped
     * @throws PipeException
     *            On problem on reading next message
     */
    public synchronized byte[] read() throws PipeException
    {
        if (!this.alive)
        {
            return null;
        }

        final File config = new File(this.pipeDirectory, PipeCommons.FILE_CONFIG);
        while ((!config.exists()) || (!config.canRead()))
        {
            Utilities.sleep(128);

            if (!this.alive)
            {
                return null;
            }
        }

        if (!this.alive)
        {
            return null;
        }
        final int pipeSize = PipeCommons.readFileInteger(config);
        if (!this.alive)
        {
            return null;
        }

        int        actual = 0;
        final File read   = new File(this.pipeDirectory, PipeCommons.FILE_READ);
        if (read.exists())
        {
            actual = PipeCommons.readFileInteger(read);
        }

        if (actual >= pipeSize)
        {
            actual = 0;
        }

        final File file = new File(this.pipeDirectory, String.valueOf(actual));
        while ((!file.exists()) || (!file.canRead()))
        {
            Utilities.sleep(128);
            if (!this.alive)
            {
                return null;
            }
        }

        if (!this.alive)
        {
            return null;
        }

        FileInputStream fileInputStream = null;
        final ByteArray byteArray       = new ByteArray();
        final byte[]    temp            = new byte[4096];
        int             size;

        try
        {
            fileInputStream = new FileInputStream(file);

            size = fileInputStream.read(temp);

            if (!this.alive)
            {
                return null;
            }

            while (size >= 0)
            {
                byteArray.write(temp, 0, size);

                size = fileInputStream.read(temp);

                if (!this.alive)
                {
                    break;
                }
            }

            if (!this.alive)
            {
                return null;
            }
        }
        catch (final Exception exception)
        {
            throw new PipeException(exception, "Failed to get string from ", file.getAbsolutePath());
        }
        finally
        {
            if (fileInputStream != null)
            {
                try
                {
                    fileInputStream.close();
                }
                catch (final Exception ignored)
                {
                }
            }
        }

        UtilIO.delete(file);

        PipeCommons.writeFileInteger(read, actual + 1);

        return byteArray.toArray();
    }

    /**
     * Indicates if the reader can read next message
     *
     * @return {@code true} if the reader can read next message
     */
    public boolean readyToRead()
    {
        return this.alive;
    }

    /**
     * Allow to reader to restart reading.<br>
     * Can use again {@link #read()} or {@link #read(Class)} after this<br>
     * Do nothing if read is already active
     */
    public void restartRead()
    {
        this.alive = true;
    }

    /**
     * Stop current and future readings.<br>
     * Can't use {@link #read()} or {@link #read(Class)} after this, to be able again, call {@link #restartRead()} for
     * bea ble
     * read again<br>
     * Do nothing if read is already disabled
     */
    public void stopRead()
    {
        this.alive = false;
    }

    /**
     * String representation <br>
     * <br>
     * <b>Parent documentation:</b><br>
     * {@inheritDoc}
     *
     * @return String representation
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString()
    {
        return PipeReader.class.getName() + ":" + this.pipeDirectory.getAbsolutePath();
    }
}