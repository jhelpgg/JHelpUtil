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
package jhelp.util.image.pcx;

import java.awt.Dimension;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import jhelp.util.gui.JHelpImage;
import jhelp.util.io.UtilIO;
import jhelp.util.text.UtilText;

/**
 * PCX image file loader.<br>
 * It load in memory a PCX image file an store PCX information and uncompressed image data.<br>
 * It is possible to create an {@link JHelpImage} from this loader with method {@link #createImage()}<br>
 * The loader here is the combination of <a href="http://www.fileformat.info/format/pcx/corion.htm">PCX header</a>, <a
 * href="http://en.wikipedia.org/wiki/PCX">PCX image data info </a> and tests in SamplePCX
 *
 * @author JHelp
 * @see <a href="http://www.fileformat.info/format/pcx/corion.htm">PCX header</a>
 * @see <a href="http://en.wikipedia.org/wiki/PCX">PCX image data info </a>
 */
public class PCX
{
    /** Original PCX manufacturer code */
    public static final byte MANUFACTURER_ZSOFT                 = (byte) 10;
    /** PCX version Paintbrush v2.5 */
    public static final byte VERSION_PAINTBRUSH_V_2_5           = (byte) 0;
    /** PCX version Paintbrush v2.5 unofficial version */
    public static final byte VERSION_PAINTBRUSH_V_2_5_UNOFFICAL = (byte) 1;
    /** PCX version Paintbrush v2.8 w palette information */
    public static final byte VERSION_PAINTBRUSH_V_2_8_W         = (byte) 2;
    /** PCX version Paintbrush v2.8 w/o palette information */
    public static final byte VERSION_PAINTBRUSH_V_2_8_WO        = (byte) 3;
    /** PCX version Paintbrush v3.0+ */
    public static final byte VERSION_PAINTBRUSH_V_3_0           = (byte) 5;
    /** PCX version Paintbrush/Windows */
    public static final byte VERSION_PAINTBRUSH_WINDOWS         = (byte) 4;
    /** Indicates if a 256 palette is defined */
    private boolean has256Palette;
    /** Image height */
    private int     height;
    /** DPI in horizontal */
    private int     horizontalDPI;
    /** Manufacturer code */
    private byte    manufacturer;
    /** Number of byte per scanline */
    private int     numberBitsPerScanline;
    /** Number of byte per pixel */
    private int     numberBytePerPixel;
    /** Number of color plane */
    private int     numberOfColorPlane;
    /** The 16 colors palette */
    private int[]   palette16;
    /** The 256 colors palette */
    private int[]   palette256;
    /** Scanline size */
    private int     scanLineSize;
    /** Screen height */
    private int     screenHeight;
    /** Screen width */
    private int     screenWidth;
    /** Uncompressed image data */
    private int[]   uncompressed;
    /** PCX version code */
    private byte    version;
    /** Vertical DPI */
    private int verticalDPI;
    /** Image width */
    private int width;
    /**
     * Create a new instance of PCX
     */
    private PCX()
    {
    }
    /**
     * Create a new instance of PCX from a stream<br>
     * The stream is not closed, you have to close it yourself
     *
     * @param inputStream
     *           Stream to read
     * @throws IOException
     *            On reading issue
     */
    public PCX(final InputStream inputStream)
            throws IOException
    {
        this.readHeader(inputStream);
        this.readImageData(inputStream);
        this.read256Palette(inputStream);
    }

    /**
     * Read the 256 color palette
     *
     * @param inputStream
     *           Stream to read
     * @throws IOException
     *            On reading issue
     */
    private void read256Palette(final InputStream inputStream) throws IOException
    {
        int read = inputStream.read();
        this.has256Palette = read == 0x0C;

        if (this.has256Palette)
        {
            final byte[] data = new byte[768];
            read = UtilIO.readStream(inputStream, data);

            if (read < 768)
            {
                throw new IOException("Not enough data for the 256 palette");
            }

            int index;
            this.palette256 = new int[256];

            for (index = 0, read = 0; index < 256; index++, read += 3)
            {
                this.palette256[index] = 0xFF000000 | ((data[read] & 0xFF) << 16) | ((data[read + 1] & 0xFF) << 8) |
                        (data[read + 2] & 0xFF);
            }
        }
        else if ((this.numberBytePerPixel == 8) && (this.numberOfColorPlane == 1))
        {
            // In that case we have to use 256 gray shade, to treat it has normal 256 palette, we generate a gray shade one
            this.has256Palette = true;
            this.palette256 = new int[256];

            for (int index = 0; index < 256; index++)
            {
                this.palette256[index] = 0xFF000000 | (index << 16) | (index << 8) | index;
            }
        }
    }

    /**
     * Read image data and uncompress them
     *
     * @param inputStream
     *           Stream to read
     * @throws IOException
     *            On reading issue
     */
    private void readImageData(final InputStream inputStream) throws IOException
    {
        final int total = this.height * this.scanLineSize;
        this.uncompressed = new int[total];
        int index = 0;
        int read, count, i;

        while (index < total)
        {
            read = inputStream.read();

            if (read < 0)
            {
                throw new EOFException("Unexpected end of stream !");
            }

            if (read < 0xC0)
            {
                // If 2 first bits aren't 1 together, then it is an isolated value
                this.uncompressed[index++] = read;
            }
            else
            {
                // If 2 first bits are 1, then the byte represents the number of time to repeat the following byte
                // Have to remove 2 first bits to have the repetition number
                count = read & 0x3F;
                read = inputStream.read();

                if (read < 0)
                {
                    throw new EOFException("Unexpected end of stream !");
                }

                for (i = 0; i < count; i++)
                {
                    this.uncompressed[index++] = read;
                }
            }
        }
    }

    /**
     * Indicates if a file is a PCX image file
     *
     * @param file
     *           Tested file
     * @return {@code true} if the file is a PCX image file
     */
    public static boolean isPCX(final File file)
    {
        return PCX.computePcxSize(file) != null;
    }

    /**
     * Compute size of an PCX image.<br>
     * If the given file is not a PCX image file, {@code null} is return
     *
     * @param file
     *           Image PCX file
     * @return PCX image size OR {@code null} if given file not a valid PCX image file
     */
    public static Dimension computePcxSize(final File file)
    {
        if ((file == null) || (!file.exists()) || (file.isDirectory()) || (!file.canRead()))
        {
            return null;
        }

        InputStream inputStream = null;

        try
        {
            inputStream = new FileInputStream(file);
            final PCX pcx = new PCX();
            pcx.readHeader(inputStream);
            return new Dimension(pcx.width, pcx.height);
        }
        catch (final Exception exception)
        {
            return null;
        }
        finally
        {
            if (inputStream != null)
            {
                try
                {
                    inputStream.close();
                }
                catch (final Exception ignored)
                {
                }
            }
        }
    }

    /**
     * Read PCX header
     *
     * @param inputStream
     *           Stream to read
     * @throws IOException
     *            On reading issue
     */
    private void readHeader(final InputStream inputStream) throws IOException
    {
        // Header has 128 bytes fixed size
        final byte[] header = new byte[128];
        int          read   = UtilIO.readStream(inputStream, header);

        if (read < 128)
        {
            throw new IOException("Not enough data for read the header");
        }

        this.manufacturer = header[0x00];
        this.version = header[0x01];
        final byte encoding = header[0x02];

        if (encoding != (byte) 1)
        {
            throw new IOException("Unknown encoding=" + encoding + ", only supported is 1 (RLE)");
        }

        this.numberBytePerPixel = header[0x03] & 0xFF;
        final int left   = PCX.word(header, 0x04);
        final int up     = PCX.word(header, 0x06);
        final int right  = PCX.word(header, 0x08);
        final int bottom = PCX.word(header, 0x0A);

        if ((left > right) || (up > bottom))
        {
            throw new IOException("Invalid PCX size !");
        }

        this.horizontalDPI = PCX.word(header, 0x0C);
        this.verticalDPI = PCX.word(header, 0x0E);

        int index;
        this.palette16 = new int[16];

        for (read = 0x10, index = 0; index < 16; read += 3, index++)
        {
            this.palette16[index] = 0xFF000000 | ((header[read] & 0xFF) << 16) | ((header[read + 1] & 0xFF) << 8) |
                    (header[read + 2] & 0xFF);
        }

        this.numberOfColorPlane = header[0x41] & 0xFF;
        this.numberBitsPerScanline = PCX.word(header, 0x42);
        // UNUSED : int paletteInformation = PCX.word(header, 0x44);
        this.screenWidth = PCX.word(header, 0x46);
        this.screenHeight = PCX.word(header, 0x48);

        if ((this.numberOfColorPlane < 1) || (this.numberOfColorPlane > 4) || (this.numberBytePerPixel < 1) || (this.numberBytePerPixel > 8))
        {
            throw new IOException("Invalid PCX header !");
        }

        this.width = (right - left) + 1;
        this.height = (bottom - up) + 1;
        this.scanLineSize = this.numberOfColorPlane * this.numberBitsPerScanline;
    }

    /**
     * Read one word from an array
     *
     * @param array
     *           Array where read
     * @param offset
     *           Offset where start read the word
     * @return Read word
     */
    private static int word(final byte[] array, final int offset)
    {
        return (array[offset] & 0xFF) | ((array[offset + 1] & 0xFF) << 8);
    }

    /**
     * Create a new image from PCX information
     *
     * @return Created image
     * @throws IllegalStateException
     *            If how create image for this specific PCX information is unknown
     */
    public JHelpImage createImage()
    {
        final int[] pixels = new int[this.width * this.height];

        switch (this.numberBytePerPixel)
        {
            case 1:
                switch (this.numberOfColorPlane)
                {
                    case 1:
                        this.fillPixels_1_BytePerPixel_1_ColorPlane(pixels);
                        break;
                    case 3:
                        this.fillPixels_1_BytePerPixel_3_ColorPlane(pixels);
                        break;
                    case 4:
                        this.fillPixels_1_BytePerPixel_4_ColorPlane(pixels);
                        break;
                    default:
                        throw new IllegalStateException(
                                "Unknown how to convert numberBytePerPixel=" + this.numberBytePerPixel + " " +
                                        "numberOfColorPlane="
                                        + this.numberOfColorPlane);
                }
                break;
            case 4:
                switch (this.numberOfColorPlane)
                {
                    case 1:
                        this.fillPixels_4_BytePerPixel_1_ColorPlane(pixels);
                        break;
                    case 4:
                        this.fillPixels_4_BytePerPixel_4_ColorPlane(pixels);
                        break;
                    default:
                        throw new IllegalStateException(
                                "Unknown how to convert numberBytePerPixel=" + this.numberBytePerPixel + " " +
                                        "numberOfColorPlane="
                                        + this.numberOfColorPlane);

                }
                break;
            case 8:
                switch (this.numberOfColorPlane)
                {
                    case 1:
                        this.fillPixels_8_BytePerPixel_1_ColorPlane(pixels);
                        break;
                    case 3:
                        this.fillPixels_8_BytePerPixel_3_ColorPlane(pixels);
                        break;
                    case 4:
                        this.fillPixels_8_BytePerPixel_4_ColorPlane(pixels);
                        break;
                    default:
                        throw new IllegalStateException(
                                "Unknown how to convert numberBytePerPixel=" + this.numberBytePerPixel + " numberOfColorPlane="
                                        + this.numberOfColorPlane);

                }
                break;
            default:
                throw new IllegalStateException(
                        "Unknown how to convert numberBytePerPixel=" + this.numberBytePerPixel + " numberOfColorPlane="
                                + this.numberOfColorPlane);
        }

        return new JHelpImage(this.width, this.height, pixels);
    }

    /**
     * Fill image pixels for the case : 1 byte per pixel and 1 color plane
     *
     * @param pixels
     *           Pixels image to fill
     */
    private void fillPixels_1_BytePerPixel_1_ColorPlane(final int[] pixels)
    {
        // Each bit represents a pixel, 1 => white, 0 => black
        final int[] scanLine = new int[this.scanLineSize];
        int         lineData = 0;
        int         pix      = 0;
        int         x, shift, read, index;

        for (int y = 0; y < this.height; y++)
        {
            System.arraycopy(this.uncompressed, lineData, scanLine, 0, this.scanLineSize);
            x = 0;
            shift = 7;
            index = 0;
            read = scanLine[0];

            while (x < this.width)
            {
                if (((read >> shift) & 1) == 1)
                {
                    pixels[pix++] = 0xFFFFFFFF;
                }
                else
                {
                    pixels[pix++] = 0xFF000000;
                }

                shift--;

                if (shift < 0)
                {
                    shift = 7;
                    index++;

                    if (index >= this.scanLineSize)
                    {
                        break;
                    }

                    read = scanLine[index];
                }

                x++;
            }

            lineData += this.scanLineSize;
        }
    }

    /**
     * Fill image pixels for the case : 1 byte per pixel and 3 color planes
     *
     * @param pixels
     *           Pixels image to fill
     */
    private void fillPixels_1_BytePerPixel_3_ColorPlane(final int[] pixels)
    {
        // The palette 16 index is dispatch like that, lower bits first, upper bits last (only first 8 colors (0-7) of
       // the palette
        // are used)
        final int[] scanLine = new int[this.scanLineSize];
        final int[] codes    = new int[this.width];
        int         lineData = 0;
        int         pix      = 0;
        int         x, shift, read, index;

        for (int y = 0; y < this.height; y++)
        {
            System.arraycopy(this.uncompressed, lineData, scanLine, 0, this.scanLineSize);

            x = 0;
            shift = 7;
            index = 0;
            read = scanLine[0];

            while (x < this.width)
            {
                if (((read >> shift) & 1) == 1)
                {
                    codes[x] = 0x1;
                }
                else
                {
                    codes[x] = 0;
                }

                shift--;

                if (shift < 0)
                {
                    shift = 7;
                    index++;
                    read = scanLine[index];
                }

                x++;
            }

            x = 0;

            while (x < this.width)
            {
                if (((read >> shift) & 1) == 1)
                {
                    codes[x] |= 0x2;
                }

                shift--;

                if (shift < 0)
                {
                    shift = 7;
                    index++;
                    read = scanLine[index];
                }

                x++;
            }

            x = 0;

            while (x < this.width)
            {
                if (((read >> shift) & 1) == 1)
                {
                    codes[x] |= 0x4;
                }

                shift--;

                if (shift < 0)
                {
                    shift = 7;
                    index++;

                    if (index >= this.scanLineSize)
                    {
                        break;
                    }

                    read = scanLine[index];
                }

                x++;
            }

            for (x = 0; x < this.width; x++)
            {
                pixels[pix++] = this.palette16[codes[x]];
            }

            lineData += this.scanLineSize;
        }
    }

    /**
     * Fill image pixels for the case : 1 byte per pixel and 4 color planes
     *
     * @param pixels
     *           Pixels image to fill
     */
    private void fillPixels_1_BytePerPixel_4_ColorPlane(final int[] pixels)
    {
        // The palette 16 index is dispatch like that, lower bits first, upper bits last
        final int[] scanLine = new int[this.scanLineSize];
        final int[] codes    = new int[this.width];
        int         lineData = 0;
        int         pix      = 0;
        int         x, shift, read, index;

        for (int y = 0; y < this.height; y++)
        {
            System.arraycopy(this.uncompressed, lineData, scanLine, 0, this.scanLineSize);

            x = 0;
            shift = 7;
            index = 0;
            read = scanLine[0];

            while (x < this.width)
            {
                if (((read >> shift) & 1) == 1)
                {
                    codes[x] = 0x1;
                }
                else
                {
                    codes[x] = 0;
                }

                shift--;

                if (shift < 0)
                {
                    shift = 7;
                    index++;
                    read = scanLine[index];
                }

                x++;
            }

            x = 0;

            while (x < this.width)
            {
                if (((read >> shift) & 1) == 1)
                {
                    codes[x] |= 0x2;
                }

                shift--;

                if (shift < 0)
                {
                    shift = 7;
                    index++;
                    read = scanLine[index];
                }

                x++;
            }

            x = 0;

            while (x < this.width)
            {
                if (((read >> shift) & 1) == 1)
                {
                    codes[x] |= 0x4;
                }

                shift--;

                if (shift < 0)
                {
                    shift = 7;
                    index++;
                    read = scanLine[index];
                }

                x++;
            }

            x = 0;

            while (x < this.width)
            {
                if (((read >> shift) & 1) == 1)
                {
                    codes[x] |= 0x8;
                }

                shift--;

                if (shift < 0)
                {
                    shift = 7;
                    index++;

                    if (index >= this.scanLineSize)
                    {
                        break;
                    }

                    read = scanLine[index];
                }

                x++;
            }

            for (x = 0; x < this.width; x++)
            {
                pixels[pix++] = this.palette16[codes[x]];
            }

            lineData += this.scanLineSize;
        }
    }

    /**
     * Fill image pixels for the case : 4 bytes per pixel and 1 color plane
     *
     * @param pixels
     *           Pixels image to fill
     */
    private void fillPixels_4_BytePerPixel_1_ColorPlane(final int[] pixels)
    {
        // Each byte contains 2 palette 16 indexes
        final int[] scanLine = new int[this.scanLineSize];
        int         lineData = 0;
        int         pix      = 0;
        int         x;
        int         read, index;

        for (int y = 0; y < this.height; y++)
        {
            System.arraycopy(this.uncompressed, lineData, scanLine, 0, this.scanLineSize);

            x = 0;
            index = 0;

            while (x < this.width)
            {
                read = scanLine[index];

                pixels[pix++] = this.palette16[(read >> 4) & 0xF];
                x++;

                if (x < this.width)
                {
                    pixels[pix++] = this.palette16[read & 0xF];
                    x++;
                }

                index++;
            }

            lineData += this.scanLineSize;
        }
    }

    /**
     * Fill image pixels for the case : 4 bytes per pixel and 4 color planes
     *
     * @param pixels
     *           Pixels image to fill
     */
    private void fillPixels_4_BytePerPixel_4_ColorPlane(final int[] pixels)
    {
        // RGBA all codes in 0-16, so have to multiply values per 16
        // 2 parts per byte
        final int[] scanLine = new int[this.scanLineSize];
        int         lineData = 0;
        int         pix      = 0;
        int         x;
        int         read, index, start, write;

        for (int y = 0; y < this.height; y++)
        {
            System.arraycopy(this.uncompressed, lineData, scanLine, 0, this.scanLineSize);

            start = 0;
            index = 0;
            x = 0;
            write = pix;

            while (x < this.width)
            {
                read = scanLine[index];

                pixels[write++] = ((read >> 4) & 0xF) << 20;
                x++;

                if (x < this.width)
                {
                    pixels[write++] = (read & 0xF) << 20;
                    x++;
                }
            }

            start += this.numberBitsPerScanline;
            index = start;
            x = 0;
            write = pix;

            while (x < this.width)
            {
                read = scanLine[index];

                pixels[write++] |= ((read >> 4) & 0xF) << 12;
                x++;

                if (x < this.width)
                {
                    pixels[write++] |= (read & 0xF) << 12;
                    x++;
                }
            }

            start += this.numberBitsPerScanline;
            index = start;
            x = 0;
            write = pix;

            while (x < this.width)
            {
                read = scanLine[index];

                pixels[write++] |= ((read >> 4) & 0xF) << 4;
                x++;

                if (x < this.width)
                {
                    pixels[write++] |= (read & 0xF) << 4;
                    x++;
                }
            }

            start += this.numberBitsPerScanline;
            index = start;
            x = 0;
            write = pix;

            while (x < this.width)
            {
                read = scanLine[index];

                pixels[write++] |= ((read >> 4) & 0xF) << 28;
                x++;

                if (x < this.width)
                {
                    pixels[write++] |= (read & 0xF) << 28;
                    x++;
                }
            }

            pix += this.width;

            lineData += this.scanLineSize;
        }
    }

    /**
     * Fill image pixels for the case : 8 bytes per pixel and 1 color plane
     *
     * @param pixels
     *           Pixels image to fill
     */
    private void fillPixels_8_BytePerPixel_1_ColorPlane(final int[] pixels)
    {
        // Each byte is 1 palette 256 index
        final int[] scanLine = new int[this.scanLineSize];
        int         lineData = 0;
        int         pix      = 0;
        int         x;
        for (int y = 0; y < this.height; y++)
        {
            System.arraycopy(this.uncompressed, lineData, scanLine, 0, this.scanLineSize);

            for (x = 0; x < this.width; x++)
            {
                pixels[pix++] = this.palette256[scanLine[x]];
            }

            lineData += this.scanLineSize;
        }
    }

    /**
     * Fill image pixels for the case : 8 bytes per pixel and 3 color planes
     *
     * @param pixels
     *           Pixels image to fill
     */
    private void fillPixels_8_BytePerPixel_3_ColorPlane(final int[] pixels)
    {
        // RGB color : ex for (5x3)
        // RRRRRGGGGGBBBBB
        // RRRRRGGGGGBBBBB
        // RRRRRGGGGGBBBBB
        final int[] scanLine = new int[this.scanLineSize];
        int         lineData = 0;
        int         pix      = 0;
        int         x;
        int         index, start, write;

        for (int y = 0; y < this.height; y++)
        {
            System.arraycopy(this.uncompressed, lineData, scanLine, 0, this.scanLineSize);

            start = 0;
            index = 0;
            write = pix;

            for (x = 0; x < this.width; x++)
            {
                pixels[write++] = scanLine[index++] << 16;
            }

            start += this.numberBitsPerScanline;
            index = start;
            write = pix;

            for (x = 0; x < this.width; x++)
            {
                pixels[write++] |= scanLine[index++] << 8;
            }

            start += this.numberBitsPerScanline;
            index = start;
            write = pix;

            for (x = 0; x < this.width; x++)
            {
                pixels[write++] |= scanLine[index++];
            }

            start += this.numberBitsPerScanline;
            index = start;
            write = pix;

            for (x = 0; x < this.width; x++)
            {
                pixels[write++] |= 0xFF000000;
            }

            pix += this.width;

            lineData += this.scanLineSize;
        }
    }

    /**
     * Fill image pixels for the case : 8 bytes per pixel and 4 color planes
     *
     * @param pixels
     *           Pixels image to fill
     */
    private void fillPixels_8_BytePerPixel_4_ColorPlane(final int[] pixels)
    {
        // RGBA color : ex for (5x3)
        // RRRRRGGGGGBBBBBAAAAA
        // RRRRRGGGGGBBBBBAAAAA
        // RRRRRGGGGGBBBBBAAAAA
        final int[] scanLine = new int[this.scanLineSize];
        int         lineData = 0;
        int         pix      = 0;
        int         x;
        int         index, start, write;

        for (int y = 0; y < this.height; y++)
        {
            System.arraycopy(this.uncompressed, lineData, scanLine, 0, this.scanLineSize);

            start = 0;
            index = 0;
            write = pix;

            for (x = 0; x < this.width; x++)
            {
                pixels[write++] = scanLine[index++] << 16;
            }

            start += this.numberBitsPerScanline;
            index = start;
            write = pix;

            for (x = 0; x < this.width; x++)
            {
                pixels[write++] |= scanLine[index++] << 8;
            }

            start += this.numberBitsPerScanline;
            index = start;
            write = pix;

            for (x = 0; x < this.width; x++)
            {
                pixels[write++] |= scanLine[index++];
            }

            start += this.numberBitsPerScanline;
            index = start;
            write = pix;

            for (x = 0; x < this.width; x++)
            {
                pixels[write++] |= scanLine[index++] << 24;
            }

            pix += this.width;

            lineData += this.scanLineSize;
        }
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
     * Horizontal DPI
     *
     * @return Horizontal DPI
     */
    public int getHorizontalDPI()
    {
        return this.horizontalDPI;
    }

    /**
     * Manufacturer code
     *
     * @return Manufacturer code
     */
    public byte getManufacturer()
    {
        return this.manufacturer;
    }

    /**
     * Screen height
     *
     * @return Screen height
     */
    public int getScreenHeight()
    {
        return this.screenHeight;
    }

    /**
     * Screen width
     *
     * @return Screen width
     */
    public int getScreenWidth()
    {
        return this.screenWidth;
    }

    /**
     * PCX version code.<br>
     * If the version is &gt;5 its means that its more than {@link #VERSION_PAINTBRUSH_V_3_0}
     *
     * @return PCX version code
     */
    public byte getVersion()
    {
        return this.version;
    }

    /**
     * Vertical DPI
     *
     * @return Vertical DPI
     */
    public int getVerticalDPI()
    {
        return this.verticalDPI;
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
        return UtilText.concatenate("PCX ", PCX.manufacturerToString(this.manufacturer), " ",
                                    PCX.versionToString(this.version), ", ", this.width, 'x',
                                    this.height, ", DPI=", this.horizontalDPI, 'x', this.verticalDPI, ", ",
                                    this.numberBytePerPixel, " bit(s) per pixel, ", this.numberOfColorPlane,
                                    " color plane(s), ", this.numberBitsPerScanline, " bit(s) per scanline",
                                    (this.has256Palette
                                     ? ", 256 colors palette present"
                                     : ""));
    }

    /**
     * Convert manufacturer code to its name
     *
     * @param manufacturer
     *           Manufacturer code
     * @return Manufacturer name
     */
    public static String manufacturerToString(final byte manufacturer)
    {
        switch (manufacturer)
        {
            case MANUFACTURER_ZSOFT:
                return "ZSoft";
            default:
                return "Manufacturer_" + (manufacturer & 0xFF);
        }
    }

    /**
     * Convert a version code to its name
     *
     * @param version
     *           Version code
     * @return Version name
     */
    public static String versionToString(final byte version)
    {
        switch (version)
        {
            case VERSION_PAINTBRUSH_V_2_5:
            case VERSION_PAINTBRUSH_V_2_5_UNOFFICAL:
                return "Paintbrush v2.5";
            case VERSION_PAINTBRUSH_V_2_8_W:
                return "Paintbrush v2.8 w palette information";
            case VERSION_PAINTBRUSH_V_2_8_WO:
                return "Paintbrush v2.8 w/o palette information";
            case VERSION_PAINTBRUSH_WINDOWS:
                return "Paintbrush/Windows";
            case VERSION_PAINTBRUSH_V_3_0:
                return "Paintbrush v3.0+";
            default:
                return "More than Paintbrush v3.0+ (" + (version & 0xFF) + ")";
        }
    }
}