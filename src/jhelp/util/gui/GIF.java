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
package jhelp.util.gui;

import java.awt.Dimension;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import jhelp.util.image.gif.DataGIF;
import jhelp.util.image.gif.DataGIFVisitor;
import jhelp.util.io.IntegerArrayInputStream;
import jhelp.util.list.ArrayInt;

/**
 * GIF image, now without SUN proprietary files :) <br>
 * <br>
 * Version 1.0.0<br>
 *
 * @author JHelp
 */
public class GIF
{
    /** Images delay */
    private final ArrayInt     delays;
    /** Image height */
    private final int          height;
    /** Image width */
    private final int width;
    /** Images contains in the GIF */
    private       JHelpImage[] images;
    /** Last seen index in automatic show */
    private       int          previousIndex;
    /** Start animation time */
    private       long         startTime;
    /** Total animation time */
    private       int          totalTime;
    /**
     * Constructs GIF
     *
     * @param inputStream
     *           Stream to read the GIF
     * @throws IOException
     *            On reading problem
     */
    public GIF(final InputStream inputStream)
            throws IOException
    {
        if (inputStream == null)
        {
            throw new NullPointerException("inputStream MUST NOT be null");
        }

        this.delays = new ArrayInt();
        this.totalTime = 0;

        final DataGIF dataGIF = new DataGIF();
        dataGIF.read(inputStream);

        final InternalVisitor internalVisitor = new InternalVisitor(this.delays);
        dataGIF.collectImages(internalVisitor);

        this.width = internalVisitor.width;
        this.height = internalVisitor.height;
        this.images = internalVisitor.getArray();

        final int size = this.delays.getSize();

        for (int i = 0; i < size; i++)
        {
            this.totalTime += this.delays.getIndex(i);
        }

        if (this.images.length == 0)
        {
            throw new IOException("Failed to load GIF, no extracted image");
        }
    }

    /**
     * Compute size of an GIF image.<br>
     * If the given file is not a GIF image file, {@code null} is return
     *
     * @param file
     *           Image GIF file
     * @return GIF image size OR {@code null} if given file not a valid GIF image file
     */
    public static Dimension computeGifSize(final File file)
    {
        return DataGIF.computeGifSize(file);
    }

    /**
     * Indicates if a file is a GIF image file
     *
     * @param file
     *           Tested file
     * @return {@code true} if the file is a GIF image file
     */
    public static boolean isGIF(final File file)
    {
        return DataGIF.isGIF(file);
    }

    /**
     * Compute GIF MD5
     *
     * @return GIF MD5
     * @throws NoSuchAlgorithmException
     *            If MD5 unknown
     * @throws IOException
     *            On computing problem
     */
    public String computeMD5() throws NoSuchAlgorithmException, IOException
    {
        final MessageDigest md5 = MessageDigest.getInstance("MD5");

        final int numberOfImages = this.images.length;

        byte[] temp = new byte[4096];
        temp[0] = (byte) ((numberOfImages >> 24) & 0xFF);
        temp[1] = (byte) ((numberOfImages >> 16) & 0xFF);
        temp[2] = (byte) ((numberOfImages >> 8) & 0xFF);
        temp[3] = (byte) (numberOfImages & 0xFF);

        md5.update(temp, 0, 4);

        IntegerArrayInputStream inputStream;
        JHelpImage              bufferedImage;
        int[]                   pixels;
        int                     read, width, height;

        for (JHelpImage image1 : this.images)
        {
            bufferedImage = image1;

            width = bufferedImage.getWidth();
            height = bufferedImage.getHeight();

            pixels = bufferedImage.getPixels(0, 0, width, height, 2);
            pixels[0] = width;
            pixels[1] = height;

            inputStream = new IntegerArrayInputStream(pixels);
            pixels = null;

            read = inputStream.read(temp);
            while (read >= 0)
            {
                md5.update(temp, 0, read);

                read = inputStream.read(temp);
            }

            inputStream.close();
            inputStream = null;
        }

        temp = md5.digest();
        final StringBuilder stringBuffer = new StringBuilder();
        for (final byte b : temp)
        {
            read = b & 0xFF;
            stringBuffer.append(Integer.toHexString((read >> 4) & 0xF));
            stringBuffer.append(Integer.toHexString(read & 0xF));
        }
        temp = null;

        return stringBuffer.toString();
    }

    /**
     * Destroy the gif to free memory
     */
    public void destroy()
    {
        for (int i = this.images.length - 1; i >= 0; i--)
        {
            this.images[i] = null;
        }

        this.images = null;
    }

    /**
     * Obtin an image delay
     *
     * @param index
     *           Image index
     * @return Delay in millisecond
     */
    public int getDelay(final int index)
    {
        return this.delays.getInteger(index);
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
     * Get a image
     *
     * @param index
     *           Image index
     * @return Desired image
     */
    public JHelpImage getImage(final int index)
    {
        return this.images[index];
    }

    /**
     * Get the image suggest between last time {@link #startAnimation()} was called and time this method is called based on
     * images delays
     *
     * @return Image since last time {@link #startAnimation()} was called
     */
    public JHelpImage getImageFromStartAnimation()
    {
        final long time         = (System.currentTimeMillis() - this.startTime) >> 3L;
        final int  max          = this.images.length - 1;
        final int  relativeTime = (int) (time % this.totalTime);
        int        index        = 0;
        int        actualTime   = 0;
        int        delay;

        for (; index < max; index++)
        {
            delay = this.delays.getInteger(index);
            actualTime += delay;

            if (actualTime >= relativeTime)
            {
                break;
            }
        }

        final int nextIndex = ((this.previousIndex + 1) % this.images.length);
        if (index != this.previousIndex)
        {
            index = nextIndex;
        }

        this.previousIndex = index;
        return this.images[index];
    }

    /**
     * Total animation time
     *
     * @return Total animation time
     */
    public int getTotalTime()
    {
        return this.totalTime;
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
     * Indicates if this instance can be use.<br>
     * That is to say if {@link #destroy()} never call
     *
     * @return {@code true} if the instance can be use
     */
    public boolean isUsable()
    {
        return this.images != null;
    }

    /**
     * Number of images
     *
     * @return Number of images
     */
    public int numberOfImage()
    {
        return this.images.length;
    }

    /**
     * Start/restart animation from beginning, to follow evolution, use {@link #getImageFromStartAnimation()} to have current
     * image of the animation
     */
    public void startAnimation()
    {
        this.startTime = System.currentTimeMillis();
    }

    /**
     * Visitor for collect images inside GIF
     *
     * @author JHelp
     */
    class InternalVisitor
            implements DataGIFVisitor
    {
        /** List of collecting delays */
        final   ArrayInt         delays;
        /** Image height */
        int height;
        /** Image width */
        int width;
        /** List of collecting images */
        private List<JHelpImage> list;

        /**
         * Create a new instance of InternalVisitor
         *
         * @param delays
         *           Delays to fill
         */
        InternalVisitor(final ArrayInt delays)
        {
            this.delays = delays;
        }

        /**
         * Transform collected list in array, and empty the list
         *
         * @return Array of images
         */
        JHelpImage[] getArray()
        {
            final JHelpImage[] array = this.list.toArray(new JHelpImage[this.list.size()]);
            this.list.clear();
            this.list = null;
            return array;
        }

        /**
         * Called when collection of images is finished <br>
         * <br>
         * <b>Parent documentation:</b><br>
         * {@inheritDoc}
         *
         * @see jhelp.util.image.gif.DataGIFVisitor#endCollecting()
         */
        @Override
        public void endCollecting()
        {
        }

        /**
         * Called when next image is computed <br>
         * <br>
         * <b>Parent documentation:</b><br>
         * {@inheritDoc}
         *
         * @param duration
         *           Image duration in millisecond
         * @param image
         *           Image
         * @see jhelp.util.image.gif.DataGIFVisitor#nextImage(long, jhelp.util.gui.JHelpImage)
         */
        @Override
        public void nextImage(final long duration, final JHelpImage image)
        {
            this.delays.add((int) duration);
            this.list.add(image);
        }

        /**
         * Called when collect image start <br>
         * <br>
         * <b>Parent documentation:</b><br>
         * {@inheritDoc}
         *
         * @param width
         *           Image width
         * @param height
         *           Image height
         * @see jhelp.util.image.gif.DataGIFVisitor#startCollecting(int, int)
         */
        @Override
        public void startCollecting(final int width, final int height)
        {
            this.width = width;
            this.height = height;
            this.list = new ArrayList<JHelpImage>();
        }
    }
}