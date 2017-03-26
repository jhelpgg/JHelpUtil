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

import java.awt.Shape;

/**
 * Way to specify a color, texture OR paint to use for fill a shape
 *
 * @author JHelp
 */
public class JHelpDrawable
{
    /** Color */
    private final int        color;
    /** Image texture */
    private final JHelpImage image;
    /** Paint */
    private final JHelpPaint paint;

    /**
     * Create a new instance of JHelpDrawable based on one color
     *
     * @param color
     *           Color
     */
    public JHelpDrawable(final int color)
    {
        this.color = color;
        this.image = null;
        this.paint = null;
    }

    /**
     * Create a new instance of JHelpDrawable based on one texture image
     *
     * @param image
     *           Texture image
     */
    public JHelpDrawable(final JHelpImage image)
    {
        if (image == null)
        {
            throw new NullPointerException("image MUST NOT be null");
        }

        this.color = 0;
        this.image = image;
        this.paint = null;
    }

    /**
     * Create a new instance of JHelpDrawable Based on one paint
     *
     * @param paint
     *           Paint
     */
    public JHelpDrawable(final JHelpPaint paint)
    {
        if (paint == null)
        {
            throw new NullPointerException("paint MUST NOT be null");
        }

        this.color = 0;
        this.image = null;
        this.paint = paint;
    }

    /**
     * Draw a filled ellipse inside an image
     *
     * @param x
     *           X up-left corner of ellipse bounding box
     * @param y
     *           Y up-left corner of ellipse bounding box
     * @param width
     *           Ellipse width
     * @param height
     *           Ellipse height
     * @param image
     *           Image where draw
     */
    public void fillEllipse(final int x, final int y, final int width, final int height, final JHelpImage image)
    {
        final boolean draw = image.isDrawMode();

        if (!draw)
        {
            image.startDrawMode();
        }

        if (this.paint != null)
        {
            image.fillEllipse(x, y, width, height, this.paint);
        }
        else if (this.image != null)
        {
            image.fillEllipse(x, y, width, height, this.image);
        }
        else
        {
            image.fillEllipse(x, y, width, height, this.color);
        }

        if (!draw)
        {
            image.endDrawMode();
        }
    }

    /**
     * Draw a filled polygon inside given image
     *
     * @param xs
     *           Polygon point X's coordinate
     * @param offsetX
     *           Offset where start read X's coordinate in <b>xs</b> array
     * @param ys
     *           Polygon point Y's coordinate
     * @param offsetY
     *           Offset where start read Y's coordinate in <b>ys</b> array
     * @param length
     *           Number polygon points
     * @param image
     *           Image where draw
     */
    public void fillPolygon(final int[] xs, final int offsetX, final int[] ys, final int offsetY, final int length,
                            final JHelpImage image)
    {
        final boolean draw = image.isDrawMode();

        if (!draw)
        {
            image.startDrawMode();
        }

        if (this.paint != null)
        {
            image.fillPolygon(xs, offsetX, ys, offsetY, length, this.paint);
        }
        else if (this.image != null)
        {
            image.fillPolygon(xs, offsetX, ys, offsetY, length, this.image);
        }
        else
        {
            image.fillPolygon(xs, offsetX, ys, offsetY, length, this.color);
        }

        if (!draw)
        {
            image.endDrawMode();
        }
    }

    /**
     * Draw a filled rectangle inside given image
     *
     * @param x
     *           X up-left corner
     * @param y
     *           Y up-left corner
     * @param width
     *           Rectangle width
     * @param height
     *           Rectangle height
     * @param image
     *           Image where draw
     */
    public void fillRectangle(final int x, final int y, final int width, final int height, final JHelpImage image)
    {
        final boolean draw = image.isDrawMode();

        if (!draw)
        {
            image.startDrawMode();
        }

        if (this.paint != null)
        {
            image.fillRectangle(x, y, width, height, this.paint);
        }
        else if (this.image != null)
        {
            image.fillRectangle(x, y, width, height, this.image);
        }
        else
        {
            image.fillRectangle(x, y, width, height, this.color);
        }

        if (!draw)
        {
            image.endDrawMode();
        }
    }

    /**
     * Draw a filled round rectangle inside given image
     *
     * @param x
     *           X up-left corner
     * @param y
     *           Y up-left corner
     * @param width
     *           Rectangle width
     * @param height
     *           Rectangle height
     * @param arcWidth
     *           Arc size in width
     * @param arcHeight
     *           Arc size in height
     * @param image
     *           Image where draw
     */
    public void fillRoundRectangle(final int x, final int y, final int width, final int height, final int arcWidth,
                                   final int arcHeight, final JHelpImage image)
    {
        final boolean draw = image.isDrawMode();

        if (!draw)
        {
            image.startDrawMode();
        }

        if (this.paint != null)
        {
            image.fillRoundRectangle(x, y, width, height, arcWidth, arcHeight, this.paint);
        }
        else if (this.image != null)
        {
            image.fillRoundRectangle(x, y, width, height, arcWidth, arcHeight, this.image);
        }
        else
        {
            image.fillRoundRectangle(x, y, width, height, arcWidth, arcHeight, this.color);
        }

        if (!draw)
        {
            image.endDrawMode();
        }
    }

    /**
     * Draw a filled shape inside given image
     *
     * @param shape
     *           Shape to draw
     * @param image
     *           Image where draw
     */
    public void fillShape(final Shape shape, final JHelpImage image)
    {
        final boolean draw = image.isDrawMode();

        if (!draw)
        {
            image.startDrawMode();
        }

        if (this.paint != null)
        {
            image.fillShape(shape, this.paint);
        }
        else if (this.image != null)
        {
            image.fillShape(shape, this.image);
        }
        else
        {
            image.fillShape(shape, this.color);
        }

        if (!draw)
        {
            image.endDrawMode();
        }
    }
}