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
package jhelp.util.image.raster;

import java.io.IOException;
import java.io.InputStream;

import jhelp.util.gui.JHelpImage;
import jhelp.util.io.UtilIO;

/**
 * Raster 16 bits image
 *
 * @author JHelp
 */
public class Image16Bit
        implements RasterImage
{
    /** Image data */
    private final int[] data;
    /** Image height */
    private final int   height;
    /** Image width */
    private final int   width;

    /**
     * Create a new instance of Image16Bit
     *
     * @param width
     *           Width
     * @param height
     *           Height
     */
    public Image16Bit(final int width, final int height)
    {
        if ((width < 1) || (height < 1))
        {
            throw new IllegalArgumentException(
                    "width and height MUST be >0, but specified dimension was " + width + "x" + height);
        }

        this.width = width;
        this.height = height;
        this.data = new int[this.width * this.height];
    }

    /**
     * Get one pixel color
     *
     * @param x
     *           X
     * @param y
     *           Y
     * @return Color
     */
    public int getColor(final int x, final int y)
    {
        if ((x < 0) || (x >= this.width) || (y < 0) || (y >= this.height))
        {
            throw new IllegalArgumentException(
                    "x must be in [0, " + this.width + "[ and y in [0, " + this.height + "[ but specified point (" + x +
                            ", " + y + ")");
        }

        return this.data[x + (y * this.width)];
    }

    /**
     * Image height <br>
     * <br>
     * <b>Parent documentation:</b><br>
     * {@inheritDoc}
     *
     * @return Image height
     * @see jhelp.util.list.SizedObject#getHeight()
     */
    @Override
    public int getHeight()
    {
        return this.height;
    }

    /**
     * Image width <br>
     * <br>
     * <b>Parent documentation:</b><br>
     * {@inheritDoc}
     *
     * @return Image width
     * @see jhelp.util.list.SizedObject#getWidth()
     */
    @Override
    public int getWidth()
    {
        return this.width;
    }

    /**
     * Parse bitmap stream to image data
     *
     * @param inputStream
     *           Stream to parse
     * @throws IOException
     *            On reading issue
     */
    public void parseBitmapStream(final InputStream inputStream) throws IOException
    {
        this.clear();
        final byte[] buffer = new byte[4];
        int          y      = this.height - 1;
        int          red, green, blue, x, line, index;

        while (y >= 0)
        {
            line = y * this.width;
            x = 0;
            while (x < this.width)
            {
                UtilIO.readStream(inputStream, buffer);
                index = 0;

                while ((index < 4) && (x < this.width))
                {
                    red = (buffer[index] & 0xF0) >> 4;
                    green = buffer[index] & 0xF;
                    index++;
                    blue = (buffer[index] & 0xF0) >> 4;
                    index++;
                    this.data[x + line] = 0xFF000000 | (red << 20) | (green << 12) | (blue << 4);
                    x++;
                }
            }

            y--;
        }
    }

    /**
     * clear the image <br>
     * <br>
     * <b>Parent documentation:</b><br>
     * {@inheritDoc}
     *
     * @see jhelp.util.image.raster.RasterImage#clear()
     */
    @Override
    public void clear()
    {
        for (int i = this.data.length - 1; i >= 0; i--)
        {
            this.data[i] = (byte) 0;
        }
    }

    /**
     * Image type <br>
     * <br>
     * <b>Parent documentation:</b><br>
     * {@inheritDoc}
     *
     * @return Image type
     * @see jhelp.util.image.raster.RasterImage#getImageType()
     */
    @Override
    public RasterImageType getImageType()
    {
        return RasterImageType.IMAGE_16_BITS;
    }

    /**
     * Convert to JHelp image <br>
     * <br>
     * <b>Parent documentation:</b><br>
     * {@inheritDoc}
     *
     * @return Converted image
     * @see jhelp.util.image.raster.RasterImage#toJHelpImage()
     */
    @Override
    public JHelpImage toJHelpImage()
    {
        return new JHelpImage(this.width, this.height, this.data);
    }

    /**
     * Change one pixel color
     *
     * @param x
     *           X
     * @param y
     *           Y
     * @param color
     *           Color
     */
    public void setColor(final int x, final int y, final int color)
    {
        if ((x < 0) || (x >= this.width) || (y < 0) || (y >= this.height))
        {
            throw new IllegalArgumentException(
                    "x must be in [0, " + this.width + "[ and y in [0, " + this.height + "[ but specified point (" + x +
                            ", " + y + ")");
        }

        this.data[x + (y * this.width)] = color;
    }
}