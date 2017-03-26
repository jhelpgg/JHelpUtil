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
package jhelp.util.gui.alphabet;

import java.awt.Dimension;

import jhelp.util.gui.GIF;
import jhelp.util.gui.JHelpImage;

/**
 * Alphabet with "graffiti" letters
 *
 * @author JHelp
 */
public class AlphabetGraffiti
        extends Alphabet
{
    /** Graffiti letters alphabet with big size (64 pixels width and height) */
    public static final  AlphabetGraffiti BIG        = new AlphabetGraffiti(64);
    /** Graffiti letters alphabet with normal size (32 pixels width and height) */
    public static final  AlphabetGraffiti NORMAL     = new AlphabetGraffiti(32);
    /** Graffiti letters alphabet with small size (16 pixels width and height) */
    public static final  AlphabetGraffiti SMALL      = new AlphabetGraffiti(16);
    /** Graffiti letters alphabet with very big size (128 pixels width and height) */
    public static final  AlphabetGraffiti VERY_BIG   = new AlphabetGraffiti(128);
    /** Graffiti letters alphabet with very small size (8 pixels width and height) */
    public static final  AlphabetGraffiti VERY_SMALL = new AlphabetGraffiti(8);
    /** Graffiti letters images */
    private static final GIF              BASE       = AlphabetResources.RESOURCES.obtainGIF("alphabetGraffiti.gif");

    /**
     * Create a new instance of AlphabetGraffiti
     *
     * @param size
     *           Size of alphabet in pixels
     */
    private AlphabetGraffiti(final int size)
    {
        this(size, size);
    }

    /**
     * Create a new instance of AlphabetGraffiti
     *
     * @param characterWidth
     *           Character width in pixels
     * @param characterHeight
     *           Character height in pixels
     */
    private AlphabetGraffiti(final int characterWidth, final int characterHeight)
    {
        super(characterWidth, characterHeight, false);
    }

    /**
     * Create image for a letter <br>
     * <br>
     * <b>Parent documentation:</b><br>
     * {@inheritDoc}
     *
     * @param character
     *           Letter
     * @return Image associated with
     * @see Alphabet#createImageFor(char)
     */
    @Override
    protected JHelpImage createImageFor(final char character)
    {
        if ((character < 'A') || (character > 'Z'))
        {
            return null;
        }

        final Dimension dimension = this.getCharacterDimension();

        return JHelpImage.createResizedImage(AlphabetGraffiti.BASE.getImage(character - 'A'), dimension.width,
                                             dimension.height);
    }
}