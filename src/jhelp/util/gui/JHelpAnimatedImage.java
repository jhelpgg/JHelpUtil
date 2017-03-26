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

import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Animate an image on using frames .<br>
 * A frame is an image to draw over the current image at a specified position on a specified time.<br>
 * Animation can be play in 2 modes, LOOP or REVERSE.<br>
 * For example if animation is made with frame 1, to 4,<br>
 * LOOP will do 1, 2, 3, 4, 1, 2, 3, 4, 1, 2, 3, 4, 1, 2, 3, 4, 1, 2, ...<br>
 * and REVERSE 1, 2, 3, 4, 3, 2, 1, 2, 3, 4, 3, 2, 1, 2, ...
 *
 * @author JHelp
 */
public class JHelpAnimatedImage
{
    /**
     * Actual mode
     */
    private final AnimationMode             animationMode;
    /**
     * Frames
     */
    private final ArrayList<AnimationFrame> frames;
    /**
     * Image where draw the animation
     */
    private final JHelpImage                imageToRefresh;
    /**
     * Actual frame index
     */
    private       int                       index;
    /**
     * Time passed in the current frame
     */
    private       int                       timePass;
    /**
     * Actual way for changing frames
     */
    private       int                       way;

    /**
     * Create a new instance of JHelpAnimatedImage
     *
     * @param imageToRefresh Image where draw animation
     * @param animationMode  Mode to use
     */
    public JHelpAnimatedImage(final JHelpImage imageToRefresh, final AnimationMode animationMode)
    {
        if (imageToRefresh == null)
        {
            throw new NullPointerException("imageToRefresh MUST NOT be null");
        }

        if (animationMode == null)
        {
            throw new NullPointerException("animationMode MUST NOT be null");
        }

        this.imageToRefresh = imageToRefresh;
        this.animationMode = animationMode;

        this.frames = new ArrayList<JHelpAnimatedImage.AnimationFrame>();
    }

    /**
     * Add a frame to the animation
     *
     * @param animationFrame Frame to add
     */
    public void addFrame(final AnimationFrame animationFrame)
    {
        if (animationFrame == null)
        {
            throw new NullPointerException("animationFrame MUST NOT be null");
        }

        this.frames.add(animationFrame);
    }

    /**
     * Add a frame
     *
     * @param image Image to draw
     * @param x     X position
     * @param y     Y position
     * @param time  Time duration
     */
    public void addFrame(final JHelpImage image, final int x, final int y, final int time)
    {
        if (image == null)
        {
            throw new NullPointerException("image MUST NOT be null");
        }

        if (time < 1)
        {
            throw new IllegalArgumentException("time must be >=1 not " + time);
        }

        this.frames.add(new AnimationFrame(image, x, y, time));
    }

    /**
     * List of animations
     *
     * @return List of animations
     */
    public List<AnimationFrame> getAnimationFrames()
    {
        return Collections.unmodifiableList(this.frames);
    }

    /**
     * Animation mode
     *
     * @return Animation mode
     */
    public AnimationMode getAnimatonMode()
    {
        return this.animationMode;
    }

    /**
     * Signal to animation that one unit of time passed, and refresh the image if need
     */
    public void nextTime()
    {
        if (this.frames.isEmpty())
        {
            return;
        }

        if (this.way == 0)
        {
            this.start();
            return;
        }

        final AnimationFrame animationFrame = this.frames.get(this.index);

        this.timePass++;

        if (this.timePass >= animationFrame.time)
        {
            this.timePass = 0;

            this.index += this.way;

            final int size = this.frames.size();

            if (this.index < 0)
            {
                this.index = 0;
                this.way = 1;
            }
            else if (this.index >= size)
            {
                switch (this.animationMode)
                {
                    case LOOP:
                        this.index = 0;
                        this.way = 1;
                        break;
                    case REVERSE:
                        this.index = size - 1;
                        this.way = -1;
                        break;
                }
            }

            this.refreshImage();
        }
    }

    /***
     * Start the animation, or restart the animation from the start
     */
    public void start()
    {
        this.index = 0;
        this.timePass = 0;
        this.way = 1;

        if (!this.frames.isEmpty())
        {
            this.refreshImage();
        }
    }

    /**
     * Refresh the image
     */
    private void refreshImage()
    {
        final AnimationFrame animationFrame = this.frames.get(this.index);

        this.imageToRefresh.startDrawMode();

        this.imageToRefresh.clear(0x00000000);
        this.imageToRefresh.drawImage(animationFrame.x, animationFrame.y, animationFrame.image);

        this.imageToRefresh.endDrawMode();
    }

    /**
     * Animation mode
     *
     * @author JHelp
     */
    public static enum AnimationMode
    {
        /**
         * Loop mode
         */
        LOOP,
        /**
         * Revese mode
         */
        REVERSE
    }

    /**
     * Represents a frame of the animation
     *
     * @author JHelp
     */
    public static class AnimationFrame
    {
        /**
         * Image to draw
         */
        public final JHelpImage image;
        /**
         * Zone of the image
         */
        public       Rectangle  imagePart;
        /**
         * Time duration of the frame
         */
        public       int        time;
        /**
         * Frame X position
         */
        public       int        x;
        /**
         * Frame Y position
         */
        public       int        y;

        /**
         * Create a new instance of AnimationFrame
         *
         * @param image Image for the frame
         * @param x     X position
         * @param y     Y position
         * @param time  Time to play the frame
         */
        public AnimationFrame(final JHelpImage image, final int x, final int y, final int time)
        {
            this.image = image;
            this.x = x;
            this.y = y;
            this.time = time;
        }
    }
}