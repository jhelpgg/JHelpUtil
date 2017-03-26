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
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.font.LineMetrics;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import jhelp.util.debug.Debug;
import jhelp.util.list.Pair;
import jhelp.util.text.StringExtractor;
import jhelp.util.text.UtilText;

/**
 * Represents a font with underline information
 *
 * @author JHelp
 */
@SuppressWarnings("deprecation")
public final class JHelpFont
        implements ConstantsGUI
{
    /**
     * Default font
     */
    public static final JHelpFont DEFAULT      = new JHelpFont("Monospaced", 18);
    /**
     * Character unicode for the smiley :)
     */
    public static final char      SMILEY_HAPPY = (char) 0x263A;
    /**
     * Character unicode for the smiley :(
     */
    public static final char      SMILEY_SAD   = (char) 0x2639;
    /**
     * Embed font
     */
    private final Font        font;
    /**
     * Metrics for measure strings
     */
    private final FontMetrics fontMetrics;
    /**
     * Underline information
     */
    private final boolean     underline;
    /**
     * Font maximum character width
     */
    private int maximumWidth = -1;

    /**
     * Create a new instance of JHelpFont
     *
     * @param font      Based font
     * @param underline Underline enable/disable
     */
    public JHelpFont(final Font font, final boolean underline)
    {
        this.underline = underline;
        this.font = font;

        final BufferedImage bufferedImage = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
        final Graphics2D    graphics2d    = bufferedImage.createGraphics();
        this.applyHints(graphics2d);
        this.fontMetrics = graphics2d.getFontMetrics(this.font);
        graphics2d.dispose();
        bufferedImage.flush();
    }

    /**
     * Apply hints on given graphics
     *
     * @param graphics2d Graphics where apply hints
     */
    private void applyHints(final Graphics2D graphics2d)
    {
        graphics2d.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION,
                                    RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);

        if (ConstantsGUI.FONT_RENDER_CONTEXT.isAntiAliased())
        {
            graphics2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        }
        else
        {
            graphics2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
        }

        graphics2d.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
        graphics2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
        graphics2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

        if (ConstantsGUI.FONT_RENDER_CONTEXT.usesFractionalMetrics())
        {
            graphics2d.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
        }
        else
        {
            graphics2d.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_OFF);
        }
    }

    /**
     * Create a new instance of JHelpFont not bold, not italic, not underline
     *
     * @param family Font family name
     * @param size   Font size
     */
    public JHelpFont(final String family, final int size)
    {
        this(family, size, false);
    }

    /**
     * Create a new instance of JHelpFont not italic, not underline
     *
     * @param family Font family name
     * @param size   Font size
     * @param bold   Indicates if bold or not
     */
    public JHelpFont(final String family, final int size, final boolean bold)
    {
        this(family, size, bold, false);
    }

    /**
     * Create a new instance of JHelpFont not underline
     *
     * @param family Font family name
     * @param size   Font size
     * @param bold   Bold enable/disable
     * @param italic Italic enable/disable
     */
    public JHelpFont(final String family, final int size, final boolean bold, final boolean italic)
    {
        this(family, size, bold, italic, false);
    }

    /**
     * Create a new instance of JHelpFont
     *
     * @param family    Font family name
     * @param size      Font size
     * @param bold      Bold enable/disable
     * @param italic    Italic enable/disable
     * @param underline Underline enable/disable
     */
    public JHelpFont(final String family, final int size, final boolean bold, final boolean italic,
                     final boolean underline)
    {
        this.underline = underline;

        int style = Font.PLAIN;

        if (bold)
        {
            style |= Font.BOLD;
        }

        if (italic)
        {
            style |= Font.ITALIC;
        }

        this.font = new Font(family, style, size);

        final BufferedImage bufferedImage = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
        final Graphics2D    graphics2d    = bufferedImage.createGraphics();
        this.applyHints(graphics2d);
        this.fontMetrics = graphics2d.getFontMetrics(this.font);
        graphics2d.dispose();
        bufferedImage.flush();
    }

    /**
     * Create a font from a stream
     *
     * @param type      Font type
     * @param stream    Stream to get the font data
     * @param size      Size of created font
     * @param bold      Bold value
     * @param italic    Italic value
     * @param underline Indicates if have to underline or not
     * @return Created font
     */
    public static JHelpFont createFont(final Type type, final InputStream stream, int size, final Value bold,
                                       final Value italic, final boolean underline)
    {
        final JHelpFont font = JHelpFont.obtainFont(type, stream, size, bold, italic, underline);

        if (font == null)
        {
            if (size < 1)
            {
                size = 18;
            }

            return new JHelpFont("Arial", size, bold == Value.TRUE, italic == Value.TRUE, underline);
        }

        return font;
    }

    /**
     * Create a font from a stream
     *
     * @param type      Font type
     * @param stream    Stream to get the font data
     * @param size      Size of created font
     * @param bold      Bold value
     * @param italic    Italic value
     * @param underline Indicates if have to underline or not
     * @return Created font OR {@code null} if stream not a managed font
     */
    public static JHelpFont obtainFont(final Type type, final InputStream stream, final int size, final Value bold,
                                       final Value italic, final boolean underline)
    {
        try
        {
            final int fontFormat = type == Type.TYPE1
                                   ? Font.TYPE1_FONT
                                   : Font.TRUETYPE_FONT;

            Font      font      = Font.createFont(fontFormat, stream);
            final int fontSize  = font.getSize();
            final int fontStyle = font.getStyle();

            int style = 0;

            switch (bold)
            {
                case FALSE:
                    break;
                case FREE:
                    style |= fontStyle & Font.BOLD;
                    break;
                case TRUE:
                    style |= Font.BOLD;
                    break;
            }

            switch (italic)
            {
                case FALSE:
                    break;
                case FREE:
                    style |= fontStyle & Font.ITALIC;
                    break;
                case TRUE:
                    style |= Font.ITALIC;
                    break;
            }

            if ((fontSize != size) || (style != fontStyle))
            {
                if (fontSize == size)
                {
                    font = font.deriveFont(style);
                }
                else if (style == fontStyle)
                {
                    font = font.deriveFont((float) size);
                }
                else
                {
                    font = font.deriveFont(style, size);
                }
            }

            return new JHelpFont(font, underline);
        }
        catch (final Exception exception)
        {
            Debug.printException(exception, "Failed to create the font");
            return null;
        }
    }

    /**
     * Compute shape of a string
     *
     * @param string String
     * @param x      X position of top-left
     * @param y      Y position of top-left
     * @return Computed shape
     */
    public Shape computeShape(final String string, final int x, final int y)
    {
        return this.font.createGlyphVector(ConstantsGUI.FONT_RENDER_CONTEXT, string)
                        .getOutline(x,
                                    y + this.font.getLineMetrics(string, ConstantsGUI.FONT_RENDER_CONTEXT)
                                                 .getAscent());
    }

    /**
     * Compute text lines representation with this font
     *
     * @param text      Text to use
     * @param textAlign Align to use
     * @return The couple of the list of each computed lines and the total size of all lines together
     * @deprecated Smoother result with {@link #computeTextLinesAlpha(String, JHelpTextAlign, int, int, boolean)}
     */
    @Deprecated
    public Pair<List<JHelpTextLine>, Dimension> computeTextLines(final String text, final JHelpTextAlign textAlign)
    {
        return this.computeTextLines(text, textAlign, Integer.MAX_VALUE);
    }

    /**
     * Compute text lines representation with this font
     *
     * @param text       Text to use
     * @param textAlign  Align to use
     * @param limitWidth Number maximum of pixels in width
     * @return The couple of the list of each computed lines and the total size of all lines together
     * @deprecated Smoother result with {@link #computeTextLinesAlpha(String, JHelpTextAlign, int, int, boolean)}
     */
    @Deprecated
    public Pair<List<JHelpTextLine>, Dimension> computeTextLines(final String text, final JHelpTextAlign textAlign,
                                                                 final int limitWidth)
    {
        return this.computeTextLines(text, textAlign, limitWidth, Integer.MAX_VALUE);
    }

    /**
     * Compute text lines representation with this font
     *
     * @param text        Text to use
     * @param textAlign   Align to use
     * @param limitWidth  Number maximum of pixels in width
     * @param limitHeight Limit height in pixels
     * @return The couple of the list of each computed lines and the total size of all lines together
     * @deprecated Smoother result with {@link #computeTextLinesAlpha(String, JHelpTextAlign, int, int, boolean)}
     */
    @Deprecated
    public Pair<List<JHelpTextLine>, Dimension> computeTextLines(final String text, final JHelpTextAlign textAlign,
                                                                 final int limitWidth, final int limitHeight)
    {
        return this.computeTextLines(text, textAlign, limitWidth, limitHeight, true);
    }

    /**
     * Compute text lines representation with this font
     *
     * @param text        Text to use
     * @param textAlign   Align to use
     * @param limitWidth  Number maximum of pixels in width
     * @param limitHeight Limit height in pixels
     * @param trim        Indicates if have to trim lines
     * @return The couple of the list of each computed lines and the total size of all lines together
     * @deprecated Smoother result with {@link #computeTextLinesAlpha(String, JHelpTextAlign, int, int, boolean)}
     */
    @Deprecated
    public Pair<List<JHelpTextLine>, Dimension> computeTextLines(final String text, final JHelpTextAlign textAlign,
                                                                 final int limitWidth, final int limitHeight,
                                                                 final boolean trim)
    {
        final int limit = Math.max(this.getMaximumCharacterWidth() + 2, limitWidth);

        final ArrayList<JHelpTextLine> textLines = new ArrayList<JHelpTextLine>();
        final StringExtractor          lines     = new StringExtractor(text, "\n\f\r", "", "");
        final Dimension                size      = new Dimension();

        int       width, index, start;
        final int height = this.getHeight();

        String line = lines.next();
        String head, tail;

        while (line != null)
        {
            if (trim)
            {
                line = line.trim();
            }

            width = this.stringWidth(line);
            index = line.length() - 1;
            head = line;
            tail = "";

            while ((width > limit) && (index > 0))
            {
                start = index;
                index = UtilText.lastIndexOf(line, index, ' ', '\t', '\'', '&', '~', '"', '#', '{', '(', '[', '-', '|',
                                             '`', '_', '\\', '^', '@', '°', ')', ']',
                                             '+', '=', '}', '"', 'µ', '*', ',', '?', '.', ';', ':', '/', '!', '§', '<',
                                             '>', '²');

                if (index >= 0)
                {
                    if (trim)
                    {
                        head = line.substring(0, index)
                                   .trim();
                        tail = line.substring(index)
                                   .trim();
                    }
                    else
                    {
                        head = line.substring(0, index);
                        tail = line.substring(index);
                    }
                }
                else
                {
                    start--;
                    index = start;
                    head = line.substring(0, index) + "-";
                    tail = line.substring(index);
                }

                width = this.stringWidth(head);
                if (width <= limit)
                {
                    size.width = Math.max(size.width, width);

                    textLines.add(new JHelpTextLine(head, 0, size.height, width, height, this.createMask(head), false));

                    size.height += height;

                    line = tail;
                    head = tail;
                    tail = "";
                    width = this.stringWidth(line);
                    index = line.length() - 1;

                    if (size.height >= limitHeight)
                    {
                        break;
                    }
                }
                else
                {
                    index--;
                }
            }

            if (size.height >= limitHeight)
            {
                break;
            }

            size.width = Math.max(size.width, width);

            textLines.add(new JHelpTextLine(line, 0, size.height, width, height, this.createMask(line), true));

            size.height += height;

            if (size.height >= limitHeight)
            {
                break;
            }

            line = lines.next();
        }

        for (final JHelpTextLine textLine : textLines)
        {
            switch (textAlign)
            {
                case CENTER:
                    textLine.x = (size.width - textLine.getWidth()) >> 1;
                    break;
                case LEFT:
                    textLine.x = 0;
                    break;
                case RIGHT:
                    textLine.x = size.width - textLine.getWidth();
                    break;
            }
        }

        size.width = Math.max(1, size.width);
        size.height = Math.max(1, size.height);
        return new Pair<List<JHelpTextLine>, Dimension>(Collections.unmodifiableList(textLines), size);
    }

    /**
     * Create a mask from a string
     *
     * @param string String to convert in mask
     * @return Created mask
     */
    public JHelpMask createMask(final String string)
    {
        final int width  = Math.max(1, this.fontMetrics.stringWidth(string));
        final int height = Math.max(1, this.fontMetrics.getHeight());
        final int ascent = this.fontMetrics.getAscent();

        int[]               pixels        = new int[width * height];
        final BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        bufferedImage.setRGB(0, 0, width, height, pixels, 0, width);
        final Graphics2D graphics2d = bufferedImage.createGraphics();
        this.applyHints(graphics2d);

        graphics2d.setColor(Color.WHITE);
        graphics2d.setFont(this.font);
        graphics2d.drawString(string, 0, ascent);
        pixels = bufferedImage.getRGB(0, 0, width, height, pixels, 0, width);

        final JHelpMask mask = new JHelpMask(width, height);
        int             pix  = 0;
        for (int y = 0; y < height; y++)
        {
            for (int x = 0; x < width; x++)
            {
                if ((pixels[pix++] & 0xFF) > 0x80)
                {
                    mask.setValue(x, y, true);
                }
            }
        }

        graphics2d.dispose();
        bufferedImage.flush();

        return mask;
    }

    /**
     * Font height
     *
     * @return Font height
     */
    public int getHeight()
    {
        return this.fontMetrics.getHeight();
    }

    /**
     * Maximum character width
     *
     * @return Biggest width of one character
     */
    public int getMaximumCharacterWidth()
    {
        if (this.maximumWidth < 0)
        {
            for (char car = 32; car < 128; car++)
            {
                this.maximumWidth = Math.max(this.maximumWidth, this.fontMetrics.charWidth(car));
            }
        }

        return this.maximumWidth;
    }

    /**
     * Compute string width
     *
     * @param string String to measure
     * @return String width
     */
    public int stringWidth(final String string)
    {
        final Rectangle2D bounds = this.font.getStringBounds(string, ConstantsGUI.FONT_RENDER_CONTEXT);
        return (int) Math.ceil(bounds.getWidth());
    }

    public Pair<List<JHelpTextLineAlpha>, Dimension> computeTextLinesAlpha(final String text,
                                                                           final JHelpTextAlign textAlign)
    {
        return this.computeTextLinesAlpha(text, textAlign, Integer.MAX_VALUE);
    }

    public Pair<List<JHelpTextLineAlpha>, Dimension> computeTextLinesAlpha(final String text,
                                                                           final JHelpTextAlign textAlign,
                                                                           final int limitWidth)
    {
        return this.computeTextLinesAlpha(text, textAlign, limitWidth, Integer.MAX_VALUE);
    }

    public Pair<List<JHelpTextLineAlpha>, Dimension> computeTextLinesAlpha(final String text,
                                                                           final JHelpTextAlign textAlign,
                                                                           final int limitWidth, final int limitHeight)
    {
        return this.computeTextLinesAlpha(text, textAlign, limitWidth, limitHeight, true);
    }

    /**
     * Compute text lines representation with this font with alpha mask
     *
     * @param text        Text to use
     * @param textAlign   Align to use
     * @param limitWidth  Number maximum of pixels in width
     * @param limitHeight Limit height in pixels
     * @param trim        Indicates if have to trim lines
     * @return The couple of the list of each computed lines and the total size of all lines together
     */
    public Pair<List<JHelpTextLineAlpha>, Dimension> computeTextLinesAlpha(final String text, final JHelpTextAlign
            textAlign, final int limitWidth,
                                                                           final int limitHeight, final boolean trim)
    {
        final int limit = Math.max(this.getMaximumCharacterWidth() + 2, limitWidth);

        final ArrayList<JHelpTextLineAlpha> textLines = new ArrayList<JHelpTextLineAlpha>();
        final StringExtractor               lines     = new StringExtractor(text, "\n\f\r", "", "");
        final Dimension                     size      = new Dimension();

        int       width, index, start;
        final int height = this.getHeight();

        String line = lines.next();
        String head, tail;

        while (line != null)
        {
            if (trim)
            {
                line = line.trim();
            }

            width = this.stringWidth(line);
            index = line.length() - 1;
            head = line;
            tail = "";

            while ((width > limit) && (index > 0))
            {
                start = index;
                index = UtilText.lastIndexOf(line, index, ' ', '\t', '\'', '&', '~', '"', '#', '{', '(', '[', '-', '|',
                                             '`', '_', '\\', '^', '@', '°', ')', ']',
                                             '+', '=', '}', '"', 'µ', '*', ',', '?', '.', ';', ':', '/', '!', '§', '<',
                                             '>', '²');

                if (index >= 0)
                {
                    if (trim)
                    {
                        head = line.substring(0, index)
                                   .trim();
                        tail = line.substring(index)
                                   .trim();
                    }
                    else
                    {
                        head = line.substring(0, index);
                        tail = line.substring(index);
                    }
                }
                else
                {
                    start--;
                    index = start;
                    head = line.substring(0, index) + "-";
                    tail = line.substring(index);
                }

                width = this.stringWidth(head);

                if (width <= limit)
                {
                    size.width = Math.max(size.width, width);

                    textLines.add(new JHelpTextLineAlpha(head, 0, size.height, width, height,
                                                         this.createImage(head, 0xFFFFFFFF, 0), false));

                    size.height += height;

                    line = tail;
                    head = tail;
                    tail = "";
                    width = this.stringWidth(line);
                    index = line.length() - 1;

                    if (size.height >= limitHeight)
                    {
                        break;
                    }
                }
                else
                {
                    index--;
                }
            }

            if (size.height >= limitHeight)
            {
                break;
            }

            size.width = Math.max(size.width, width);

            textLines.add(
                    new JHelpTextLineAlpha(line, 0, size.height, width, height, this.createImage(line, 0xFFFFFFFF, 0),
                                           true));

            size.height += height;

            if (size.height >= limitHeight)
            {
                break;
            }

            line = lines.next();
        }

        for (final JHelpTextLineAlpha textLine : textLines)
        {
            switch (textAlign)
            {
                case CENTER:
                    textLine.x = (size.width - textLine.getWidth()) >> 1;
                    break;
                case LEFT:
                    textLine.x = 0;
                    break;
                case RIGHT:
                    textLine.x = size.width - textLine.getWidth();
                    break;
            }
        }

        size.width = Math.max(1, size.width);
        size.height = Math.max(1, size.height);
        return new Pair<List<JHelpTextLineAlpha>, Dimension>(Collections.unmodifiableList(textLines), size);
    }

    /**
     * Create image with text draw on it.<br>
     * Image size fit exactly to the text
     *
     * @param string     Text to create its image
     * @param foreground Foreground color
     * @param background Background color
     * @return Created image
     */
    public JHelpImage createImage(final String string, final int foreground, final int background)
    {
        final int width  = Math.max(1, this.stringWidth(string));
        final int height = Math.max(1, this.getHeight());
        final int ascent = this.fontMetrics.getAscent();

        final int nb     = width * height;
        int[]     pixels = new int[nb];

        for (int i = 0; i < nb; i++)
        {
            pixels[i] = background;
        }

        final BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        bufferedImage.setRGB(0, 0, width, height, pixels, 0, width);
        final Graphics2D graphics2d = bufferedImage.createGraphics();
        this.applyHints(graphics2d);
        graphics2d.setColor(new Color(foreground, true));
        graphics2d.setFont(this.font);
        graphics2d.drawString(string, 0, ascent);

        if (this.underline)
        {
            final int y = this.underlinePosition(string, 0);
            graphics2d.drawLine(0, y, width, y);
        }

        pixels = bufferedImage.getRGB(0, 0, width, height, pixels, 0, width);
        final JHelpImage image = new JHelpImage(width, height, pixels);
        graphics2d.dispose();
        bufferedImage.flush();
        return image;
    }

    /**
     * Compute underline position
     *
     * @param string String
     * @param y      Y of top
     * @return Y result
     */
    public int underlinePosition(final String string, final int y)
    {
        final LineMetrics lineMetrics = this.font.getLineMetrics(string, ConstantsGUI.FONT_RENDER_CONTEXT);

        return Math.round(y + lineMetrics.getUnderlineOffset() + lineMetrics.getAscent());
    }

    /**
     * Compute text lines drawing text vertically (one character per line)
     *
     * @param text        Text to write
     * @param limitHeight Height size limit
     * @return Computed lines
     */
    public Pair<List<JHelpTextLineAlpha>, Dimension> computeTextLinesAlphaVertical(final String text,
                                                                                   final int limitHeight)
    {
        final ArrayList<JHelpTextLineAlpha> textLines  = new ArrayList<JHelpTextLineAlpha>();
        final char[]                        characters = text.toCharArray();
        String                              charText;
        final Dimension                     size       = new Dimension(1, 1);
        int                                 y          = 0;
        JHelpImage                          mask;
        final int                           height     = this.getHeight();

        for (final char character : characters)
        {
            if (character > 32)
            {
                charText = String.valueOf(character);
                mask = this.createImage(charText, 0xFFFFFFFF, 0);
                size.width = Math.max(size.width, mask.getWidth());
                textLines.add(new JHelpTextLineAlpha(charText, 0, y, mask.getWidth(), height, mask, false));
            }

            y += height;
            size.height += height;

            if (size.height > limitHeight)
            {
                break;
            }
        }

        return new Pair<List<JHelpTextLineAlpha>, Dimension>(textLines, size);
    }

    /**
     * Compute text lines drawing text vertically (one character per line)
     *
     * @param text        Text to write
     * @param limitHeight Height size limit
     * @return Computed lines
     * @deprecated Smoother result with {@link #computeTextLinesAlphaVertical(String, int)}
     */
    @Deprecated
    public Pair<List<JHelpTextLine>, Dimension> computeTextLinesVertical(final String text, final int limitHeight)
    {
        final ArrayList<JHelpTextLine> textLines  = new ArrayList<JHelpTextLine>();
        final char[]                   characters = text.toCharArray();
        String                         charText;
        final Dimension                size       = new Dimension(1, 1);
        int                            y          = 0;
        JHelpMask                      mask;
        final int                      height     = this.getHeight();

        for (final char character : characters)
        {
            if (character > 32)
            {
                charText = String.valueOf(character);
                mask = this.createMask(charText);
                size.width = Math.max(size.width, mask.getWidth());
                textLines.add(new JHelpTextLine(charText, 0, y, mask.getWidth(), height, mask, false));
            }

            y += height;
            size.height += height;

            if (size.height > limitHeight)
            {
                break;
            }
        }

        return new Pair<List<JHelpTextLine>, Dimension>(textLines, size);
    }

    /**
     * Indicates if an object is equals to this font <br>
     * <br>
     * <b>Parent documentation:</b><br>
     * {@inheritDoc}
     *
     * @param object Compared object
     * @return {@code true} if equals
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(final Object object)
    {
        if (object == null)
        {
            return false;
        }

        if (object == this)
        {
            return true;
        }

        if (!(object instanceof JHelpFont))
        {
            return false;
        }

        final JHelpFont font = (JHelpFont) object;

        if (this.underline != font.underline)
        {
            return false;
        }

        return this.font.equals(font.font);
    }

    /**
     * Font family
     *
     * @return Font family
     */
    public String getFamily()
    {
        return this.font.getFamily();
    }

    /**
     * {@link Font} embed by this font
     *
     * @return {@link Font} embed by this font
     */
    public Font getFont()
    {
        return this.font;
    }

    /**
     * Font size
     *
     * @return Font size
     */
    public int getSize()
    {
        return this.font.getSize();
    }

    /**
     * Indicates if font is bold
     *
     * @return {@code true} if font is bold
     */
    public boolean isBold()
    {
        return (this.font.getStyle() & Font.BOLD) != 0;
    }

    /**
     * Indicates if font is italic
     *
     * @return {@code true} if font is italic
     */
    public boolean isItalic()
    {
        return (this.font.getStyle() & Font.ITALIC) != 0;
    }

    /**
     * Underline status
     *
     * @return Underline status
     */
    public boolean isUnderline()
    {
        return this.underline;
    }

    /**
     * Compute size of a string
     *
     * @param string String to measure
     * @return String size
     */
    public Dimension stringSize(final String string)
    {
        final Rectangle2D bounds = this.font.getStringBounds(string, ConstantsGUI.FONT_RENDER_CONTEXT);
        return new Dimension((int) (Math.ceil(bounds.getWidth())), (int) (Math.ceil(bounds.getHeight())));
    }

    /**
     * Possible font type
     *
     * @author JHelp
     */
    public static enum Type
    {
        /**
         * True type font
         */
        TRUE_TYPE,
        /**
         * Type 1 font
         */
        TYPE1
    }

    /**
     * Choice in {@link JHelpFont#createFont(Type, InputStream, int, Value, Value, boolean)} to be able say : "as it
     * defines in
     * the stream"
     *
     * @author JHelp
     */
    public static enum Value
    {
        /**
         * Force the value to be {@code false} (It transform the font if need)
         */
        FALSE,
        /**
         * Use what is defined in the stream value
         */
        FREE,
        /**
         * Force the value to be {@code true} (It transform the font if need)
         */
        TRUE
    }
}