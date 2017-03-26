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

import java.awt.Color;
import java.awt.Dimension;
import java.util.List;

import jhelp.util.list.Pair;

/**
 * Utilities for image manipulation
 *
 * @author JHelp
 */
@SuppressWarnings("deprecation")
public class UtilImage
{
    /**
     * Create image with text
     *
     * @param text       Text to draw
     * @param font       Font to use
     * @param textAlign  Text align
     * @param foreground Foreground color
     * @param background Background color
     * @return Created image
     */
    public static JHelpImage createImageWithText(final String text, final JHelpFont font, final JHelpTextAlign textAlign,
                                                 final int foreground,
                                                 final int background)
    {
        final Pair<List<JHelpTextLine>, Dimension> pair  = font.computeTextLines(text, textAlign);
        final JHelpImage                           image = new JHelpImage(pair.element2.width, pair.element2.height);
        image.startDrawMode();
        image.clear(background);

        for (final JHelpTextLine line : pair.element1)
        {
            image.paintMask(line.getX(), line.getY(), line.getMask(), foreground, 0, true);
        }

        image.endDrawMode();
        return image;
    }

    /**
     * Create image with text
     *
     * @param text       Text to draw
     * @param font       Font to use
     * @param textAlign  Text align
     * @param foreground Foreground color
     * @param background Background image
     * @return Created image
     */
    public static JHelpImage createImageWithText(final String text, final JHelpFont font, final JHelpTextAlign textAlign,
                                                 final int foreground,
                                                 final JHelpImage background)
    {
        final Pair<List<JHelpTextLine>, Dimension> pair  = font.computeTextLines(text, textAlign);
        final JHelpImage                           image = new JHelpImage(pair.element2.width, pair.element2.height);
        image.startDrawMode();
        image.clear(0);
        image.fillRectangleScaleBetter(0, 0, pair.element2.width, pair.element2.height, background);

        for (final JHelpTextLine line : pair.element1)
        {
            image.paintMask(line.getX(), line.getY(), line.getMask(), foreground, 0, true);
        }

        image.endDrawMode();
        return image;
    }

    /**
     * Create image with text
     *
     * @param text       Text to draw
     * @param font       Font to use
     * @param textAlign  Text align
     * @param foreground Foreground color
     * @param background Background paint
     * @return Created image
     */
    public static JHelpImage createImageWithText(final String text, final JHelpFont font, final JHelpTextAlign textAlign,
                                                 final int foreground,
                                                 final JHelpPaint background)
    {
        final Pair<List<JHelpTextLine>, Dimension> pair  = font.computeTextLines(text, textAlign);
        final JHelpImage                           image = new JHelpImage(pair.element2.width, pair.element2.height);
        image.startDrawMode();
        image.clear(0);
        image.fillRectangle(0, 0, pair.element2.width, pair.element2.height, background);

        for (final JHelpTextLine line : pair.element1)
        {
            image.paintMask(line.getX(), line.getY(), line.getMask(), foreground, 0, true);
        }

        image.endDrawMode();
        return image;
    }

    /**
     * Create image with text
     *
     * @param text       Text to draw
     * @param font       Font to use
     * @param textAlign  Text align
     * @param foreground Foreground image
     * @param background Background color
     * @return Created image
     */
    public static JHelpImage createImageWithText(final String text, final JHelpFont font, final JHelpTextAlign textAlign,
                                                 final JHelpImage foreground,
                                                 final int background)
    {
        final Pair<List<JHelpTextLine>, Dimension> pair  = font.computeTextLines(text, textAlign);
        final JHelpImage                           image = new JHelpImage(pair.element2.width, pair.element2.height);
        image.startDrawMode();
        image.clear(background);

        for (final JHelpTextLine line : pair.element1)
        {
            image.paintMask(line.getX(), line.getY(), line.getMask(), foreground, 0, 0, 0, true);
        }

        image.endDrawMode();
        return image;
    }

    /**
     * Create image with text
     *
     * @param text       Text to draw
     * @param font       Font to use
     * @param textAlign  Text align
     * @param foreground Foreground image
     * @param background Background image
     * @return Created image
     */
    public static JHelpImage createImageWithText(final String text, final JHelpFont font, final JHelpTextAlign textAlign,
                                                 final JHelpImage foreground,
                                                 final JHelpImage background)
    {
        final Pair<List<JHelpTextLine>, Dimension> pair  = font.computeTextLines(text, textAlign);
        final JHelpImage                           image = new JHelpImage(pair.element2.width, pair.element2.height);
        image.startDrawMode();
        image.clear(0);
        image.fillRectangleScaleBetter(0, 0, pair.element2.width, pair.element2.height, background);

        for (final JHelpTextLine line : pair.element1)
        {
            image.paintMask(line.getX(), line.getY(), line.getMask(), foreground, 0, 0, 0, true);
        }

        image.endDrawMode();
        return image;
    }

    /**
     * Create image with text
     *
     * @param text       Text to draw
     * @param font       Font to use
     * @param textAlign  Text align
     * @param foreground Foreground image
     * @param background Background paint
     * @return Created image
     */
    public static JHelpImage createImageWithText(final String text, final JHelpFont font, final JHelpTextAlign textAlign,
                                                 final JHelpImage foreground,
                                                 final JHelpPaint background)
    {
        final Pair<List<JHelpTextLine>, Dimension> pair  = font.computeTextLines(text, textAlign);
        final JHelpImage                           image = new JHelpImage(pair.element2.width, pair.element2.height);
        image.startDrawMode();
        image.clear(0);
        image.fillRectangle(0, 0, pair.element2.width, pair.element2.height, background);

        for (final JHelpTextLine line : pair.element1)
        {
            image.paintMask(line.getX(), line.getY(), line.getMask(), foreground, 0, 0, 0, true);
        }

        image.endDrawMode();
        return image;
    }

    /**
     * Create image with text
     *
     * @param text       Text to draw
     * @param font       Font to use
     * @param textAlign  Text align
     * @param foreground Foreground paint
     * @param background Background color
     * @return Created image
     */
    public static JHelpImage createImageWithText(final String text, final JHelpFont font, final JHelpTextAlign textAlign,
                                                 final JHelpPaint foreground,
                                                 final int background)
    {
        final Pair<List<JHelpTextLine>, Dimension> pair  = font.computeTextLines(text, textAlign);
        final JHelpImage                           image = new JHelpImage(pair.element2.width, pair.element2.height);
        image.startDrawMode();
        image.clear(background);

        for (final JHelpTextLine line : pair.element1)
        {
            image.paintMask(line.getX(), line.getY(), line.getMask(), foreground, 0, true);
        }

        image.endDrawMode();
        return image;
    }

    /**
     * Create image with text
     *
     * @param text       Text to draw
     * @param font       Font to use
     * @param textAlign  Text align
     * @param foreground Foreground paint
     * @param background Background image
     * @return Created image
     */
    public static JHelpImage createImageWithText(final String text, final JHelpFont font, final JHelpTextAlign textAlign,
                                                 final JHelpPaint foreground,
                                                 final JHelpImage background)
    {
        final Pair<List<JHelpTextLine>, Dimension> pair  = font.computeTextLines(text, textAlign);
        final JHelpImage                           image = new JHelpImage(pair.element2.width, pair.element2.height);
        image.startDrawMode();
        image.clear(0);
        image.fillRectangleScaleBetter(0, 0, pair.element2.width, pair.element2.height, background);

        for (final JHelpTextLine line : pair.element1)
        {
            image.paintMask(line.getX(), line.getY(), line.getMask(), foreground, 0, true);
        }

        image.endDrawMode();
        return image;
    }

    /**
     * Create image with text
     *
     * @param text       Text to draw
     * @param font       Font to use
     * @param textAlign  Text align
     * @param foreground Foreground paint
     * @param background Background paint
     * @return Created image
     */
    public static JHelpImage createImageWithText(final String text, final JHelpFont font, final JHelpTextAlign textAlign,
                                                 final JHelpPaint foreground,
                                                 final JHelpPaint background)
    {
        final Pair<List<JHelpTextLine>, Dimension> pair  = font.computeTextLines(text, textAlign);
        final JHelpImage                           image = new JHelpImage(pair.element2.width, pair.element2.height);
        image.startDrawMode();
        image.clear(0);
        image.fillRectangle(0, 0, pair.element2.width, pair.element2.height, background);

        for (final JHelpTextLine line : pair.element1)
        {
            image.paintMask(line.getX(), line.getY(), line.getMask(), foreground, 0, true);
        }

        image.endDrawMode();
        return image;
    }

    /**
     * Draw a triangle for go in a given way
     *
     * @param x           X position
     * @param y           Y position
     * @param size        Triangle size
     * @param image       Image where draw
     * @param wayTriangle Triangle way
     */
    public static void drawIncrustedTriangle(final int x, final int y, final int size, final JHelpImage image, final
    WayTriangle wayTriangle)
    {
        if (wayTriangle == null)
        {
            throw new NullPointerException("wayTriangle MUST NOT be null");
        }

        switch (wayTriangle)
        {
            case DOWN:
                UtilImage.drawIncrustedDownTriangle(x, y, size, image);
                break;
            case LEFT:
                UtilImage.drawIncrustedLeftTriangle(x, y, size, image);
                break;
            case RIGHT:
                UtilImage.drawIncrustedRightTriangle(x, y, size, image);
                break;
            case UP:
                UtilImage.drawIncrustedUpTriangle(x, y, size, image);
                break;
        }
    }

    /**
     * Draw a triangle for go down
     *
     * @param x     X position
     * @param y     Y position
     * @param size  Triangle size
     * @param image Image where draw
     */
    public static void drawIncrustedDownTriangle(final int x, final int y, final int size, final JHelpImage image)
    {
        final boolean drawMode = image.isDrawMode();

        if (!drawMode)
        {
            image.startDrawMode();
        }

        final int xx = x + (size >> 1);
        image.drawThickLine(x, y, x + size, y, 2, 0xFF000000);
        image.drawThickLine(x, y, xx, y + size, 2, 0xFF808080);
        image.drawThickLine(x + size, y, xx, y + size, 2, 0xFFFFFFFF);

        if (!drawMode)
        {
            image.endDrawMode();
        }
    }

    /**
     * Draw a triangle for go left
     *
     * @param x     X position
     * @param y     Y position
     * @param size  Triangle size
     * @param image Image where draw
     */
    public static void drawIncrustedLeftTriangle(final int x, final int y, final int size, final JHelpImage image)
    {
        final boolean drawMode = image.isDrawMode();

        if (!drawMode)
        {
            image.startDrawMode();
        }

        final int yy = y + (size >> 1);
        image.drawThickLine(x, yy, x + size, y, 2, 0xFF000000);
        image.drawThickLine(x, yy, x + size, y + size, 2, 0xFF808080);
        image.drawThickLine(x + size, y, x + size, y + size, 2, 0xFFFFFFFF);

        if (!drawMode)
        {
            image.endDrawMode();
        }
    }

    /**
     * Draw a triangle for go right
     *
     * @param x     X position
     * @param y     Y position
     * @param size  Triangle size
     * @param image Image where draw
     */
    public static void drawIncrustedRightTriangle(final int x, final int y, final int size, final JHelpImage image)
    {
        final boolean drawMode = image.isDrawMode();

        if (!drawMode)
        {
            image.startDrawMode();
        }

        final int yy = y + (size >> 1);
        image.drawThickLine(x + size, yy, x, y, 2, 0xFF000000);
        image.drawThickLine(x + size, yy, x, y + size, 2, 0xFFFFFFFF);
        image.drawThickLine(x, y, x, y + size, 2, 0xFF808080);

        if (!drawMode)
        {
            image.endDrawMode();
        }
    }

    /**
     * Draw a triangle for go up
     *
     * @param x     X position
     * @param y     Y position
     * @param size  Triangle size
     * @param image Image where draw
     */
    public static void drawIncrustedUpTriangle(final int x, final int y, final int size, final JHelpImage image)
    {
        final boolean drawMode = image.isDrawMode();

        if (!drawMode)
        {
            image.startDrawMode();
        }

        final int xx = x + (size >> 1);
        image.drawThickLine(x, y + size, x + size, y + size, 2, 0xFF000000);
        image.drawThickLine(x, y + size, xx, y, 2, 0xFF808080);
        image.drawThickLine(x + size, y + size, xx, y, 2, 0xFFFFFFFF);

        if (!drawMode)
        {
            image.endDrawMode();
        }
    }

    /**
     * Fill a triangle on image
     *
     * @param x           Upper left corner X
     * @param y           Upper left corner Y
     * @param size        Triangle size
     * @param image       Image where draw
     * @param wayTriangle Way of triangle
     * @param color       Color use for fill
     */
    public static void fillTriangle(final int x, final int y, final int size, final JHelpImage image, final WayTriangle
            wayTriangle, final int color)
    {
        if (wayTriangle == null)
        {
            throw new NullPointerException("wayTriangle MUST NOT be null");
        }

        switch (wayTriangle)
        {
            case DOWN:
                UtilImage.fillDownTriangle(x, y, size, image, color);
                break;
            case LEFT:
                UtilImage.fillLeftTriangle(x, y, size, image, color);
                break;
            case RIGHT:
                UtilImage.fillRightTriangle(x, y, size, image, color);
                break;
            case UP:
                UtilImage.fillUpTriangle(x, y, size, image, color);
                break;
        }
    }

    /**
     * Fill a triangle on image that show the down
     *
     * @param x     Upper left corner X
     * @param y     Upper left corner Y
     * @param size  Triangle size
     * @param image Image where draw
     * @param color Color use for fill
     */
    public static void fillDownTriangle(final int x, final int y, final int size, final JHelpImage image, final int color)
    {
        final boolean drawMode = image.isDrawMode();

        if (!drawMode)
        {
            image.startDrawMode();
        }

        final int xx = x + (size >> 1);
        final int[] xs =
                {
                        x, x + size, xx
                };
        final int[] ys =
                {
                        y, y, y + size
                };
        image.fillPolygon(xs, ys, color);

        if (!drawMode)
        {
            image.endDrawMode();
        }
    }

    /**
     * Fill a triangle on image that show the left
     *
     * @param x     Upper left corner X
     * @param y     Upper left corner Y
     * @param size  Triangle size
     * @param image Image where draw
     * @param color Color use for fill
     */
    public static void fillLeftTriangle(final int x, final int y, final int size, final JHelpImage image, final int color)
    {
        final boolean drawMode = image.isDrawMode();

        if (!drawMode)
        {
            image.startDrawMode();
        }

        final int yy = y + (size >> 1);
        final int[] xs =
                {
                        x, x + size, x + size
                };
        final int[] ys =
                {
                        yy, y, y + size
                };
        image.fillPolygon(xs, ys, color);

        if (!drawMode)
        {
            image.endDrawMode();
        }
    }

    /**
     * Fill a triangle on image that show the right
     *
     * @param x     Upper left corner X
     * @param y     Upper left corner Y
     * @param size  Triangle size
     * @param image Image where draw
     * @param color Color use for fill
     */
    public static void fillRightTriangle(final int x, final int y, final int size, final JHelpImage image, final int color)
    {
        final boolean drawMode = image.isDrawMode();

        if (!drawMode)
        {
            image.startDrawMode();
        }

        final int yy = y + (size >> 1);
        final int[] xs =
                {
                        x, x + size, x
                };
        final int[] ys =
                {
                        y, yy, y + size
                };
        image.fillPolygon(xs, ys, color);

        if (!drawMode)
        {
            image.endDrawMode();
        }
    }

    /**
     * Fill a triangle on image that show the up
     *
     * @param x     Upper left corner X
     * @param y     Upper left corner Y
     * @param size  Triangle size
     * @param image Image where draw
     * @param color Color use for fill
     */
    public static void fillUpTriangle(final int x, final int y, final int size, final JHelpImage image, final int color)
    {
        final boolean drawMode = image.isDrawMode();

        if (!drawMode)
        {
            image.startDrawMode();
        }

        final int xx = x + (size >> 1);
        final int[] xs =
                {
                        x, x + size, xx
                };
        final int[] ys =
                {
                        y + size, y + size, y
                };
        image.fillPolygon(xs, ys, color);

        if (!drawMode)
        {
            image.endDrawMode();
        }
    }

    /**
     * Invert a color
     *
     * @param color Color to invert
     * @return Inverted color
     */
    public static Color invertColor(final Color color)
    {
        return new Color(255 - color.getRed(), 255 - color.getGreen(), 255 - color.getBlue(), color.getAlpha());
    }

    /**
     * Triangle way
     *
     * @author JHelp
     */
    public enum WayTriangle
    {
        /**
         * Triangle for down
         */
        DOWN,
        /**
         * Triangle for left
         */
        LEFT,
        /**
         * Triangle for right
         */
        RIGHT,
        /**
         * Triangle for up
         */
        UP
    }
}