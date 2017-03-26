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
import jhelp.util.math.UtilMath;

/**
 * Make a transition between 2 images
 *
 * @author JHelp
 */
public class ImageTransition
        implements DynamicAnimation
{
    /** Start image */
    private final JHelpImage from;
    /** Intermediate image to show the animation */
    private final JHelpImage intermediate;
    /** Number of loop to do */
    private final int        numberOfLoop;
    /** Number of frame of one way transition */
    private final int        numberTransitionFrame;
    /** Indicates if its a ping-pong animation */
    private final boolean    pingPong;
    /** End image */
    private final JHelpImage to;
    /** Indicates if animation is playing */
    private       boolean    animating;
    /** Indicates the way of transition */
    private       boolean    increment;
    /** Number loop left before animation end */
    private       int        loopLeft;
    /** Start absolute frame */
    private       float      startAbsoluteFrame;

    /**
     * Create a new instance of ImageTransition
     *
     * @param numberTransitionFrame
     *           Number of frame for one transition
     * @param from
     *           Start image
     * @param to
     *           End image
     * @param numberOfLoop
     *           Number of loop to do
     * @param pingPong
     *           Indicates if its a ping-pong animation
     */
    public ImageTransition(final int numberTransitionFrame, final JHelpImage from, final JHelpImage to,
                           final int numberOfLoop, final boolean pingPong)
    {
        this.numberTransitionFrame = Math.max(1, numberTransitionFrame);

        if ((to.getWidth() != from.getWidth()) || (to.getHeight() != from.getHeight()))
        {
            throw new IllegalArgumentException("Images MUST have same size !");
        }

        this.animating = false;
        this.from = from;
        this.to = to;
        this.intermediate = from.createCopy();
        this.numberOfLoop = Math.max(1, numberOfLoop);
        this.pingPong = pingPong;
    }

    /**
     * Called each time animation refresh <br>
     * <br>
     * <b>Parent documentation:</b><br>
     * {@inheritDoc}
     *
     * @param absoluteFrame
     *           Absolute frame
     * @param image
     *           Image parent
     * @return {@code true} if animation continue. {@code false} if animation finished
     * @see jhelp.util.gui.dynamic.DynamicAnimation#animate(float, jhelp.util.gui.JHelpImage)
     */
    @Override
    public boolean animate(final float absoluteFrame, final JHelpImage image)
    {
        final float frame   = absoluteFrame - this.startAbsoluteFrame;
        final int   percent = (int) (256 * Math.min(1, frame / this.numberTransitionFrame));
        final int   anti    = 256 - percent;

        final int width  = this.from.getWidth();
        final int height = this.from.getHeight();
        final int size   = width * height;

        final int[] pixelsFrom = this.from.getPixels(0, 0, width, height);
        final int[] pixelsTo   = this.to.getPixels(0, 0, width, height);
        final int[] pixels     = new int[size];
        int         colorFrom;
        int         colorTo;

        if (this.increment)
        {
            for (int pix = size - 1; pix >= 0; pix--)
            {
                colorFrom = pixelsFrom[pix];
                colorTo = pixelsTo[pix];

                pixels[pix] =
                        // alpha
                        (ImageTransition.interpolate((colorFrom >> 24) & 0xFF, (colorTo >> 24) & 0xFF, percent,
                                                     anti) << 24)
                                // red
                                | (ImageTransition.interpolate((colorFrom >> 16) & 0xFF, (colorTo >> 16) & 0xFF, percent,
                                                               anti) << 16)
                                // green
                                | (ImageTransition.interpolate((colorFrom >> 8) & 0xFF, (colorTo >> 8) & 0xFF, percent,
                                                               anti) << 8)
                                // blue
                                | ImageTransition.interpolate(colorFrom & 0xFF, colorTo & 0xFF, percent, anti);
            }
        }
        else
        {
            for (int pix = size - 1; pix >= 0; pix--)
            {
                colorFrom = pixelsFrom[pix];
                colorTo = pixelsTo[pix];
                pixels[pix] =
                        // alpha
                        (ImageTransition.interpolate((colorFrom >> 24) & 0xFF, (colorTo >> 24) & 0xFF, anti,
                                                     percent) << 24)
                                // red
                                | (ImageTransition.interpolate((colorFrom >> 16) & 0xFF, (colorTo >> 16) & 0xFF, anti,
                                                               percent) << 16)
                                // green
                                | (ImageTransition.interpolate((colorFrom >> 8) & 0xFF, (colorTo >> 8) & 0xFF, anti,
                                                               percent) << 8)
                                // blue
                                | ImageTransition.interpolate(colorFrom & 0xFF, colorTo & 0xFF, anti, percent);
            }
        }

        this.intermediate.startDrawMode();
        this.intermediate.setPixels(0, 0, width, height, pixels);
        this.intermediate.endDrawMode();

        if (frame >= this.numberTransitionFrame)
        {
            boolean loopComplete = true;

            if (this.pingPong)
            {
                if (this.increment)
                {
                    this.increment = false;
                    loopComplete = false;
                }
                else
                {
                    this.increment = true;
                }
            }

            if (loopComplete)
            {
                this.loopLeft--;

                if (this.loopLeft <= 0)
                {
                    return false;
                }
            }

            this.startAbsoluteFrame = absoluteFrame;
        }

        return true;
    }

    /**
     * Interpolate a color part
     *
     * @param partFrom
     *           Part start
     * @param partTo
     *           Part end
     * @param percent
     *           Percent
     * @param anti
     *           Anti-percent
     * @return Interpolated part
     */
    private static int interpolate(final int partFrom, final int partTo, final int percent, final int anti)
    {
        return UtilMath.limit0_255(((partFrom * anti) + (partTo * percent)) >> 8);
    }

    /**
     * Called when animation stopped <br>
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
        this.animating = false;
    }

    /**
     * called when animation started <br>
     * <br>
     * <b>Parent documentation:</b><br>
     * {@inheritDoc}
     *
     * @param startAbsoluteFrame
     *           Start absolute path
     * @param image
     *           Image parent
     * @see jhelp.util.gui.dynamic.DynamicAnimation#startAnimation(float, jhelp.util.gui.JHelpImage)
     */
    @Override
    public void startAnimation(final float startAbsoluteFrame, final JHelpImage image)
    {
        this.animating = true;
        this.startAbsoluteFrame = startAbsoluteFrame;
        this.loopLeft = this.numberOfLoop;
        this.increment = true;
    }

    /**
     * Interpolated image to use to see animation
     *
     * @return Interpolated image to use to see animation
     */
    public JHelpImage getInterpolated()
    {
        return this.intermediate;
    }

    /**
     * Actual animating value
     *
     * @return Actual animating value
     */
    public boolean isAnimating()
    {
        return this.animating;
    }
}