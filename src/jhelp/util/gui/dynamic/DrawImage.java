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
package jhelp.util.gui.dynamic;

import jhelp.util.gui.JHelpImage;

/**
 * Draw an image inside an animation
 *
 * @author JHelp
 */
public class DrawImage
        extends ImmediateAnimation
{
    /** Indicates if image coordinate are center of image */
    private final boolean    center;
    /** Image to draw */
    private final JHelpImage image;
    /** X */
    private final int        x;
    /** Y */
    private final int        y;

    /**
     * Create a new instance of DrawImage
     *
     * @param image
     *           Image to draw
     */
    public DrawImage(final JHelpImage image)
    {
        if (image == null)
        {
            throw new NullPointerException("image MUST NOT be null");
        }

        this.x = 0;
        this.y = 0;
        this.center = true;
        this.image = image;
    }

    /**
     * Create a new instance of DrawImage
     *
     * @param image
     *           Image to draw
     * @param x
     *           X
     * @param y
     *           Y
     */
    public DrawImage(final JHelpImage image, final int x, final int y)
    {
        if (image == null)
        {
            throw new NullPointerException("image MUST NOT be null");
        }

        this.x = x;
        this.y = y;
        this.image = image;
        this.center = false;
    }

    /**
     * Draw the image <br>
     * <br>
     * <b>Parent documentation:</b><br>
     * {@inheritDoc}
     *
     * @param image
     *           Image where draw
     * @see jhelp.util.gui.dynamic.ImmediateAnimation#doImmediately(jhelp.util.gui.JHelpImage)
     */
    @Override
    public void doImmediately(final JHelpImage image)
    {
        int x = this.x;
        int y = this.y;

        if (this.center)
        {
            x = (image.getWidth() - this.image.getWidth()) >> 1;
            y = (image.getHeight() - this.image.getHeight()) >> 1;
        }

        image.drawImage(x, y, this.image);
    }
}