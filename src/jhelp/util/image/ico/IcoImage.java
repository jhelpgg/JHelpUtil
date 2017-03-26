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
package jhelp.util.image.ico;

import java.io.IOException;
import java.io.InputStream;

import jhelp.util.image.bmp.BitmapHeader;
import jhelp.util.image.raster.RasterImageType;
import jhelp.util.io.UtilIO;

/**
 * Ico image
 *
 * @author JHelp
 */
public class IcoImage
{
    /** Ico image elements */
    private final IcoElementImage[] icoElementImages;

    /**
     * Create a new instance of IcoImage
     *
     * @param inputStream
     *           Stream to parse
     * @throws IOException
     *            On reading issue
     */
    public IcoImage(final InputStream inputStream)
            throws IOException
    {
        this(inputStream, null);
    }

    /**
     * Create a new instance of IcoImage
     *
     * @param inputStream
     *           Stream to parse
     * @param rasterImageType
     *           Known raster image type
     * @throws IOException
     *            On reading issue
     */
    public IcoImage(final InputStream inputStream, final RasterImageType rasterImageType)
            throws IOException
    {
        int info = BitmapHeader.read2bytes(inputStream);

        if (info != 0)
        {
            throw new IOException("First 2 bytes MUST be 0, not " + info);
        }

        info = BitmapHeader.read2bytes(inputStream);

        final int length = BitmapHeader.read2bytes(inputStream);
        this.icoElementImages = new IcoElementImage[length];

        // Just jump rest of header
        UtilIO.skip(inputStream, length << 4);

        for (int i = 0; i < length; i++)
        {
            this.icoElementImages[i] = new IcoElementImage(inputStream, rasterImageType);
        }
    }

    /**
     * Get element ico
     *
     * @param index
     *           Ico element index
     * @return Ico element
     */
    public IcoElementImage getElement(final int index)
    {
        return this.icoElementImages[index];
    }

    /**
     * Number of ico element
     *
     * @return Number of ico element
     */
    public int numberOfElement()
    {
        return this.icoElementImages.length;
    }
}