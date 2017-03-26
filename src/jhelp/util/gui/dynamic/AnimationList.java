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

import java.util.ArrayList;
import java.util.List;

import jhelp.util.gui.JHelpImage;
import jhelp.util.list.Pair;
import jhelp.util.thread.ThreadManager;
import jhelp.util.thread.ThreadedSimpleTask;

/**
 * List of animation to be played one after other
 *
 * @author JHelp
 */
public class AnimationList
        implements DynamicAnimation
{
    /** Animations list */
    private final List<Pair<DynamicAnimation, DynamicAnimationFinishListener>> animations;
    /** Number of loop to do */
    private final int                                                          numberOfLoop;
    /** Task for signal to a listener that one animation is finished */
    private final TaskCallBackFinishListener                                   taskCallBackFinishListener;
    /** Current animation index */
    private       int                                                          index;
    /** Number of loop left */
    private       int                                                          loopLeft;

    /**
     * Create a new instance of AnimationList
     *
     * @param numberOfLoop
     *           Number of loop
     */
    public AnimationList(final int numberOfLoop)
    {
        this.numberOfLoop = Math.max(1, numberOfLoop);
        this.animations = new ArrayList<Pair<DynamicAnimation, DynamicAnimationFinishListener>>();
        this.taskCallBackFinishListener = new TaskCallBackFinishListener();
    }

    /**
     * Add animation to current list
     *
     * @param dynamicAnimation
     *           Animation to add
     */
    public void addAnimation(final DynamicAnimation dynamicAnimation)
    {
        this.addAnimation(dynamicAnimation, null);
    }

    /**
     * Add animation to a list and register a listener to alert when this registered animation is finished
     *
     * @param dynamicAnimation
     *           Animation to add
     * @param listener
     *           Listener to register. Use {@code null} for no listener
     */
    public void addAnimation(final DynamicAnimation dynamicAnimation, final DynamicAnimationFinishListener listener)
    {
        if (dynamicAnimation == null)
        {
            throw new NullPointerException("dynamicAnimation MUST NOT be null");
        }

        synchronized (this.animations)
        {
            this.animations.add(new Pair<DynamicAnimation, DynamicAnimationFinishListener>(dynamicAnimation, listener));
        }
    }

    /**
     * Called each time animation refresh <br>
     * <br>
     * <b>Parent documentation:</b><br>
     * {@inheritDoc}
     *
     * @param absoluteFrame
     *           Animation frame
     * @param image
     *           Image parent
     * @return {@code true} if animation still playing.{@code false} if animation finished
     * @see jhelp.util.gui.dynamic.DynamicAnimation#animate(float, jhelp.util.gui.JHelpImage)
     */
    @Override
    public boolean animate(final float absoluteFrame, final JHelpImage image)
    {
        synchronized (this.animations)
        {
            final int size = this.animations.size();

            if (this.index >= size)
            {
                return false;
            }

            Pair<DynamicAnimation, DynamicAnimationFinishListener> animation;

            animation = this.animations.get(this.index);

            while (!animation.element1.animate(absoluteFrame, image))
            {
                image.endDrawMode();
                animation.element1.endAnimation(image);
                image.startDrawMode();

                if (animation.element2 != null)
                {
                    ThreadManager.THREAD_MANAGER.doThread(this.taskCallBackFinishListener, animation);
                }

                this.index++;

                if (this.index < size)
                {
                    animation = this.animations.get(this.index);
                    image.endDrawMode();
                    animation.element1.startAnimation(absoluteFrame, image);
                    image.startDrawMode();
                }
                else
                {
                    this.loopLeft--;

                    if (this.loopLeft <= 0)
                    {
                        return false;
                    }

                    this.index = 0;
                    animation = this.animations.get(this.index);
                    image.endDrawMode();
                    animation.element1.startAnimation(absoluteFrame, image);
                    image.startDrawMode();
                }
            }
        }

        return true;
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
        synchronized (this.animations)
        {
            if (this.index < this.animations.size())
            {
                final Pair<DynamicAnimation, DynamicAnimationFinishListener> animation = this.animations.get(this.index);
                animation.element1.endAnimation(image);

                if (animation.element2 != null)
                {
                    ThreadManager.THREAD_MANAGER.doThread(this.taskCallBackFinishListener, animation);
                }
            }
        }
    }

    /**
     * Called when animation started <br>
     * <br>
     * <b>Parent documentation:</b><br>
     * {@inheritDoc}
     *
     * @param startAbsoluteFrame
     *           Start absolute frame
     * @param image
     *           Image parent
     * @see jhelp.util.gui.dynamic.DynamicAnimation#startAnimation(float, jhelp.util.gui.JHelpImage)
     */
    @Override
    public void startAnimation(final float startAbsoluteFrame, final JHelpImage image)
    {
        this.loopLeft = this.numberOfLoop;
        this.index = 0;

        synchronized (this.animations)
        {
            if (this.animations.size() > 0)
            {
                this.animations.get(0).element1.startAnimation(startAbsoluteFrame, image);
            }
        }
    }

    /**
     * Task for indicates to a listener that one animation inside list he is register for is finish
     *
     * @author JHelp
     */
    class TaskCallBackFinishListener
            extends ThreadedSimpleTask<Pair<DynamicAnimation, DynamicAnimationFinishListener>>
    {
        /**
         * Create a new instance of TaskCallBackFinishListener
         */
        TaskCallBackFinishListener()
        {
        }

        /**
         * Do the task <br>
         * <br>
         * <b>Parent documentation:</b><br>
         * {@inheritDoc}
         *
         * @param parameter
         *           Pair of finished animation and listener to alert
         * @see jhelp.util.thread.ThreadedSimpleTask#doSimpleAction(java.lang.Object)
         */
        @Override
        protected void doSimpleAction(final Pair<DynamicAnimation, DynamicAnimationFinishListener> parameter)
        {
            parameter.element2.dynamicAnimationFinished(parameter.element1);
        }
    }
}