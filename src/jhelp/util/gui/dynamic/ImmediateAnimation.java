package jhelp.util.gui.dynamic;

import jhelp.util.gui.JHelpImage;

/**
 * Animation that done immediately
 * 
 * @author JHelp
 */
public abstract class ImmediateAnimation
      implements DynamicAnimation
{
   /**
    * Create a new instance of ImmediateAnimation
    */
   public ImmediateAnimation()
   {
   }

   /**
    * Called each tile animation refresh <br>
    * <br>
    * <b>Parent documentation:</b><br>
    * {@inheritDoc}
    * 
    * @param absoluteFrame
    *           Absolute frame
    * @param image
    *           Image parent
    * @return {@code false} because animation is immediately finished when done
    * @see jhelp.util.gui.dynamic.DynamicAnimation#animate(float, jhelp.util.gui.JHelpImage)
    */
   @Override
   public final boolean animate(final float absoluteFrame, final JHelpImage image)
   {
      this.doImmediately(image);
      return false;
   }

   /**
    * Do the animation immediate
    * 
    * @param image
    *           Image parent
    */
   public abstract void doImmediately(JHelpImage image);

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
   public final void endAnimation(final JHelpImage image)
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
   public final void startAnimation(final float startAbslouteFrame, final JHelpImage image)
   {
   }
}