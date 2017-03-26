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
package jhelp.util.image.colorMap;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import jhelp.util.gui.JHelpImage;
import jhelp.util.io.UtilIO;

/**
 * Binary (2 bits) image
 *
 * @author JHelp
 */
public class BinaryImage
{
    /** Image data */
    private final byte[] data;
    /** Image height */
    private final int    height;
    /** Image width */
    private final int    width;
    /**
     * Create a new instance of BinaryImage
     *
     * @param width
     *           Width
     * @param height
     *           Height
     * @param data
     *           Data
     */
    private BinaryImage(final int width, final int height, final byte[] data)
    {
        this.width = width;
        this.height = height;
        this.data = data;
    }

    /**
     * Create a new instance of BinaryImage based on JHelp image.<br>
     * Every "white" color will be consider as bit 1, and "black" consider as 0
     *
     * @param image
     *           Image to convert
     */
    public BinaryImage(final JHelpImage image)
    {
        this.width = (image.getWidth() >> 3) << 3;
        this.height = image.getHeight();
        this.data = new byte[(this.width * this.height) >> 3];
        final int   length = this.data.length;
        final int[] pixels = image.getPixels(0, 0, this.width, this.height);

        if (this.asAlpha(pixels))
        {
            this.extractByAlpha(pixels, length);
        }
        else
        {
            this.extractByColors(pixels, length);
        }
    }

    /**
     * Indicates if image has alpha
     *
     * @param pixels
     *           Image pixels
     * @return {@code true} if image has alpha
     */
    private boolean asAlpha(final int[] pixels)
    {
        for (final int pixel : pixels)
        {
            if ((pixel >>> 24) < 255)
            {
                return true;
            }
        }

        return false;
    }

    /**
     * Create binary image using image alphas
     *
     * @param pixels
     *           Image pixels
     * @param length
     *           Data length
     */
    private void extractByAlpha(final int[] pixels, final int length)
    {
        int pix = 0;
        int color;
        int value;
        int mask;

        for (int index = 0; index < length; index++)
        {
            value = 0;
            mask = 0x80;

            for (int i = 0; i < 8; i++)
            {
                color = pixels[pix++];

                if ((color >>> 24) > 127)
                {
                    value |= mask;
                }

                mask >>= 1;
            }

            this.data[index] = (byte) value;
        }
    }

    /**
     * Create binary image using image colors
     *
     * @param pixels
     *           Image pixels
     * @param length
     *           Data length
     */
    private void extractByColors(final int[] pixels, final int length)
    {
        int    pix = 0;
        int    color;
        double yy;
        int    value;
        int    mask;

        for (int index = 0; index < length; index++)
        {
            value = 0;
            mask = 0x80;

            for (int i = 0; i < 8; i++)
            {
                color = pixels[pix++];
                yy = JHelpImage.computeY((color >> 16) & 0xFF, (color >> 8) & 0xFF, color & 0xFF);

                if (yy > 127)
                {
                    value |= mask;
                }

                mask >>= 1;
            }

            this.data[index] = (byte) value;
        }
    }

    /**
     * Read binary image from stream.<br>
     * Stream not close by the method
     *
     * @param inputStream
     *           Stream to read
     * @return Read binary image
     * @throws IOException
     *            On reading issue
     */
    public static BinaryImage read(final InputStream inputStream) throws IOException
    {
        final int    width  = UtilIO.readInteger(inputStream);
        final int    height = UtilIO.readInteger(inputStream);
        final byte[] data   = new byte[(width * height) >> 3];
        UtilIO.readStream(inputStream, data);
        return new BinaryImage(width, height, data);
    }

    /**
     * Image height
     *
     * @return Image height
     */
    public int getHeight()
    {
        return this.height;
    }

    /**
     * Image width
     *
     * @return Image width
     */
    public int getWidth()
    {
        return this.width;
    }

    /**
     * Convert to JHelp image on using given color map
     *
     * @param color0
     *           Color for bit 0
     * @param color1
     *           Color for bit 1
     * @return Converted image
     */
    public JHelpImage toJHelpImage(final int color0, final int color1)
    {
        final int[] pixels = new int[this.width * this.height];
        int         pix    = 0;
        final int   length = this.data.length;
        int         value;
        int         mask;

        for (byte aData : this.data)
        {
            value = aData & 0xFF;
            mask = 0x80;

            for (int i = 0; i < 8; i++)
            {
                if ((value & mask) == 0)
                {
                    pixels[pix] = color0;
                }
                else
                {
                    pixels[pix] = color1;
                }

                pix++;
                mask >>= 1;
            }
        }

        return new JHelpImage(this.width, this.height, pixels);
    }

    /**
     * Write image to stream.<br>
     * Stream not close by the method
     *
     * @param outputStream
     *           Stream where write
     * @throws IOException
     *            On writing issue
     */
    public void write(final OutputStream outputStream) throws IOException
    {
        UtilIO.writeInteger(this.width, outputStream);
        UtilIO.writeInteger(this.height, outputStream);
        outputStream.write(this.data);
        outputStream.flush();
    }
}