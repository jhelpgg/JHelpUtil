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
package jhelp.util.image.cur;

import java.io.IOException;
import java.io.InputStream;

import jhelp.util.image.bmp.BitmapHeader;
import jhelp.util.io.UtilIO;

/**
 * Cursor image
 *
 * @author JHelp
 */
public class CurImage
{
    /** Cursor image elements */
    private final CurElementImage[] curElementImages;

    /**
     * Create a new instance of CurImage
     *
     * @param inputStream
     *           Stream to parse
     * @throws IOException
     *            On reading issue
     */
    public CurImage(final InputStream inputStream)
            throws IOException
    {
        int info = BitmapHeader.read2bytes(inputStream);

        if (info != 0)
        {
            throw new IOException("First 2 bytes MUST be 0, not " + info);
        }

        info = BitmapHeader.read2bytes(inputStream);

        final int length = BitmapHeader.read2bytes(inputStream);
        this.curElementImages = new CurElementImage[length];

        // Just jump rest of header
        UtilIO.skip(inputStream, length << 4);

        for (int i = 0; i < length; i++)
        {
            this.curElementImages[i] = new CurElementImage(inputStream);
        }
    }

    /**
     * One cursor element
     *
     * @param index
     *           Element index
     * @return Element
     */
    public CurElementImage getElement(final int index)
    {
        return this.curElementImages[index];
    }

    /**
     * Number of elements
     *
     * @return Number of elements
     */
    public int numberOfElement()
    {
        return this.curElementImages.length;
    }
}