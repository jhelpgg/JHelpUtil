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
 * Background based on image
 *
 * @author JHelp
 */
public class BackgroundImage
        extends Background
{
    /** Image on background */
    private JHelpImage image;

    /**
     * Create a new instance of BackgroundImage
     *
     * @param image
     *           Image on background
     */
    public BackgroundImage(final JHelpImage image)
    {
        if (image == null)
        {
            throw new NullPointerException("image MUST NOT be null");
        }

        this.image = image;
    }

    /**
     * Draw the background <br>
     * <br>
     * <b>Parent documentation:</b><br>
     * {@inheritDoc}
     *
     * @param absoluteFrame
     *           Absolute frame
     * @param image
     *           Image where draw background
     * @see jhelp.util.gui.dynamic.Background#drawBackground(float, jhelp.util.gui.JHelpImage)
     */
    @Override
    public void drawBackground(final float absoluteFrame, final JHelpImage image)
    {
        this.image = JHelpImage.createResizedImage(this.image, image.getWidth(), image.getHeight());
        image.drawImage(0, 0, this.image, false);
    }
}