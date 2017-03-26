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
package jhelp.util.image.gif;

/**
 * Constants used when read GIF stream<br>
 *
 * @author JHelp
 * @see <a href="http://www.w3.org/Graphics/GIF/spec-gif89a.txt">GIF specification</a>
 */
interface GIFConstants
{
    /**
     * This block is a single-field block indicating the end of the GIF Data Stream
     */
    public static final int    BLOCK_END_GIF                            = 0x3B;
    /**
     * Indicates that is an extended bock
     */
    public static final int    BLOCK_EXTENSION                          = 0x21;
    /**
     * The Application Extension contains application-specific information
     */
    public static final int    BLOCK_EXTENSION_APPLICATION              = 0xFF;
    /**
     * The Comment Extension contains textual information which is not part of the actual graphics in the GIF Data
     * Stream. It is
     * suitable for including comments about the graphics, credits, descriptions or any other type of non-control and
     * non-graphic
     * data.
     */
    public static final int    BLOCK_EXTENSION_COMMENT                  = 0xFE;
    /**
     * The Graphic Control Extension contains parameters used when processing a graphic rendering block
     */
    public static final int    BLOCK_EXTENSION_GRAPHIC_CONTROL          = 0xF9;
    /**
     * The Plain Text Extension contains textual data and the parameters necessary to render that data as a graphic, in
     * a simple
     * form.
     */
    public static final int    BLOCK_EXTENSION_PLAIN_TEXT               = 0x01;
    /**
     * The Image Descriptor contains the parameters necessary to process a table based image.
     */
    public static final int    BLOCK_IMAGE_DESCRIPTOR                   = 0x2C;
    /**
     * Default image duration in milliseconds
     */
    public static final long   DEFAULT_TIME                             = 100;
    /**
     * Do not dispose. The graphic is to be left in place.
     */
    public static final int    DISPOSAL_METHOD_NOT_DISPOSE              = 1;
    /**
     * Restore to background color. The area used by the graphic must be restored to the background color.
     */
    public static final int    DISPOSAL_METHOD_RESTORE_BACKGROUND_COLOR = 2;
    /**
     * Restore to previous. The decoder is required to restore the area overwritten by the graphic with what was there
     * prior to
     * rendering the graphic.
     */
    public static final int    DISPOSAL_METHOD_RESTORE_PREVIOUS         = 3;
    /**
     * No disposal specified. The decoder is not required to take any action.
     */
    public static final int    DISPOSAL_METHOD_UNSPECIFIED              = 0;
    /**
     * GIF header
     */
    public static final String HEADER_GIF                               = "GIF";
    /**
     * Mask used for extract color resolution from flags : b01110000
     */
    public static final int    MASK_COLOR_RESOLUTION                    = 0x70;
    /**
     * Mask used for extract information about a following color table from flags : b10000000
     */
    public static final int    MASK_COLOR_TABLE_FOLLOW                  = 0x80;
    /**
     * Mask used for extract information about ordered table from table : b00100000
     */
    public static final int    MASK_COLOR_TABLE_ORDERED                 = 0x20;
    /**
     * Mask used for extract disposal method from flags : b00011100
     */
    public static final int    MASK_DISPOSAL_METHOD                     = 0x1C;
    /**
     * Mask used for extract information about global color table ordered from flags : b00001000
     */
    public static final int    MASK_GLOBAL_COLOR_TABLE_ORDERED          = 0x08;
    /**
     * Mask used for extract global color table size from flags : b00000111
     */
    public static final int    MASK_GLOBAL_COLOR_TABLE_SIZE             = 0x07;
    /**
     * Mask used for extract information about interlaced image from flags : b01000000
     */
    public static final int    MASK_IMAGE_INTERLACED                    = 0x40;
    /**
     * Mask used for extract information about transparency given from flags : b00000001
     */
    public static final int    MASK_TRANSPARENCY_GIVEN                  = 0x01;
    /**
     * Shift of bits to use to get the right value of color resolution
     */
    public static final int    SHIFT_COLOR_RESOLUTION                   = 4;
    /**
     * Shift of bits to use to have right value of disposal method
     */
    public static final int    SHIFT_DISPOSAL_METHOD                    = 2;
    /**
     * GIF version 87a : May 1987
     */
    public static final String VERSION_87_A                             = "87a";
    /**
     * GIF version 89a : July 1989
     */
    public static final String VERSION_89_A                             = "89a";
}