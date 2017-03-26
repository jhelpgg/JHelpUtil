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
 * Animation that shift an image
 *
 * @author JHelp
 */
public class ShiftImageAnimation
        implements DynamicAnimation
{
    /** Image to shift */
    private final JHelpImage image;
    /** Number of frame for doing the shift */
    private final float      numberFrame;
    /** Shift X to do */
    private final int        shiftX;
    /** Shift Y to do */
    private final int        shiftY;
    /** Shift X already done */
    private       int        shiftXDone;
    /** Shift Y already done */
    private       int        shiftYDone;
    /** Frame absolute where start the animation */
    private       float      startAbsoluteFrame;

    /**
     * Create a new instance of ShiftImageAnimation
     *
     * @param image
     *           Image to shift
     * @param shiftX
     *           Shift X
     * @param shiftY
     *           Shift Y
     * @param numberFrame
     *           Number of frame for do animation
     */
    public ShiftImageAnimation(final JHelpImage image, final int shiftX, final int shiftY, final float numberFrame)
    {
        if (image == null)
        {
            throw new NullPointerException("image MUST NOT be null");
        }

        this.image = image;
        this.shiftX = shiftX;
        this.shiftY = shiftY;
        this.numberFrame = Math.max(1, numberFrame);
    }

    /**
     * Called each time animation refreshed <br>
     * <br>
     * <b>Parent documentation:</b><br>
     * {@inheritDoc}
     *
     * @param absoluteFrame
     *           Current absolute frame
     * @param image
     *           Image parent
     * @return {@code true} if animation have to continue. {@code false} if animation is finished
     * @see jhelp.util.gui.dynamic.DynamicAnimation#animate(float, jhelp.util.gui.JHelpImage)
     */
    @Override
    public boolean animate(final float absoluteFrame, final JHelpImage image)
    {
        final float percent = Math.min(1, (absoluteFrame - this.startAbsoluteFrame) / this.numberFrame);
        final int   x       = (int) ((this.shiftX - this.shiftXDone) * percent);
        final int   y       = (int) ((this.shiftY - this.shiftYDone) * percent);

        if ((x > 0) || (y > 0))
        {
            this.image.startDrawMode();
            this.image.shift(x, y);
            this.image.endDrawMode();
        }

        this.shiftXDone += x;
        this.shiftYDone += y;

        image.fillRectangle(0, 0, image.getWidth(), image.getHeight(), this.image);

        return percent < 1;
    }

    /**
     * Called when animation is stopped <br>
     * <br>
     * <b>Parent documentation:</b><br>
     * {@inheritDoc}
     *
     * @param image
     *           Image parent
     * @see jhelp.util.gui.dynamic.DynamicAnimation#endAnimation(jhelp.util.gui.JHelpImage)
     */
    @Override
    public void endAnimation(final JHelpImage image)
    {
    }

    /**
     * Called when animation started <br>
     * <br>
     * <b>Parent documentation:</b><br>
     * {@inheritDoc}
     *
     * @param startAbslouteFrame
     *           Start absolute frame
     * @param image
     *           Image parent
     * @see jhelp.util.gui.dynamic.DynamicAnimation#startAnimation(float, jhelp.util.gui.JHelpImage)
     */
    @Override
    public void startAnimation(final float startAbslouteFrame, final JHelpImage image)
    {
        this.startAbsoluteFrame = startAbslouteFrame;
        this.shiftXDone = 0;
        this.shiftYDone = 0;
    }
}