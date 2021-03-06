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
 * Represents a background
 *
 * @author JHelp
 */
public class Background
{
    /**
     * Create a new instance of Background
     */
    public Background()
    {
    }

    /**
     * Draw background
     *
     * @param absoluteFrame
     *           Absolute frame
     * @param image
     *           Image where draw background
     */
    public void drawBackground(final float absoluteFrame, final JHelpImage image)
    {
        //Do nothing by default
    }

    /**
     * Called on animation start
     *
     * @param startAbsoluteFrame
     *           Start absolute frame
     */
    public void startBackground(final float startAbsoluteFrame)
    {
        //Do nothing by default
    }
}