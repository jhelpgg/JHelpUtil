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
package jhelp.util.image.gif;

import java.io.IOException;
import java.io.InputStream;

import jhelp.util.io.UtilIO;

/**
 * GIF decoder utilities
 *
 * @author JHelp
 */
class UtilGIF
{
    /**
     * Append ASCII byte to a string builder
     *
     * @param stringBuilder
     *           String builder where append
     * @param data
     *           ASCII bytes to append
     */
    public static void appendAsciiBytes(final StringBuilder stringBuilder, final byte[] data)
    {
        for (final byte b : data)
        {
            stringBuilder.append((char) (b & 0xFF));
        }
    }

    /**
     * Read a 2 byte integer
     *
     * @param inputStream
     *           Stream to read
     * @return Read integer
     * @throws IOException
     *            If stream close or reach end before the 2 bytes are read
     */
    public static int read2ByteInt(final InputStream inputStream) throws IOException
    {
        final byte[] data = new byte[2];
        final int    read = UtilIO.readStream(inputStream, data);

        if (read < 2)
        {
            throw new IOException("No enough data to read a 2 bytes int");
        }

        return (data[0] & 0xFF) | ((data[1] & 0xFF) << 8);
    }

    /**
     * Read ASCII String in stream
     *
     * @param size
     *           String size
     * @param inputStream
     *           Stream to read
     * @return String read
     * @throws IOException
     *            If stream close or end before read the all String
     */
    public static String readString(final int size, final InputStream inputStream) throws IOException
    {
        final byte[] data = new byte[size];
        final int    read = UtilIO.readStream(inputStream, data);

        if (read < size)
        {
            throw new IOException("Not enough data to read a String size " + size);
        }

        final char[] chars = new char[size];

        for (int i = 0; i < size; i++)
        {
            chars[i] = (char) (data[i] & 0xFF);
        }

        return new String(chars);
    }
}