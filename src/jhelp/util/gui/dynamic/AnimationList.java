package jhelp.util.gui.dynamic;

import java.util.ArrayList;
import java.util.List;

import jhelp.util.gui.JHelpImage;

/**
 * List of animation to be played one after other
 * 
 * @author JHelp
 */
public class AnimationList
      implements DynamicAnimation
{
   /** Animations list */
   private final List<DynamicAnimation> animations;
   /** Current animation index */
   private int                          index;
   /** Number of loop left */
   private int                          loopLeft;
   /** Number of loop to do */
   private final int                    numberOfLoop;

   /**
    * Create a new instance of AnimationList
    * 
    * @param numberOfLoop
    *           Number of loop
    */
   public AnimationList(final int numberOfLoop)
   {
      this.numberOfLoop = Math.max(1, numberOfLoop);
      this.animations = new ArrayList<DynamicAnimation>();
   }

   /**
    * Add animation to current list
    * 
    * @param dynamicAnimation
    *           Animation to add
    */
   public void addAnimation(final DynamicAnimation dynamicAnimation)
   {
      if(dynamicAnimation == null)
      {
         throw new NullPointerException("dynamicAnimation musn't be null");
      }

      synchronized(this.animations)
      {
         this.animations.add(dynamicAnimation);
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
      synchronized(this.animations)
      {
         final int size = this.animations.size();

         if(this.index >= size)
         {
            return false;
         }

         DynamicAnimation dynamicAnimation;

         dynamicAnimation = this.animations.get(this.index);

         while(dynamicAnimation.animate(absoluteFrame, image) == false)
         {
            image.endDrawMode();
            dynamicAnimation.endAnimation(image);
            image.startDrawMode();

            this.index++;

            if(this.index < size)
            {
               dynamicAnimation = this.animations.get(this.index);
               image.endDrawMode();
               dynamicAnimation.startAnimation(absoluteFrame, image);
               image.startDrawMode();
            }
            else
            {
               this.loopLeft--;

               if(this.loopLeft <= 0)
               {
                  return false;
               }

               this.index = 0;
               dynamicAnimation = this.animations.get(this.index);
               image.endDrawMode();
               dynamicAnimation.startAnimation(absoluteFrame, image);
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
      synchronized(this.animations)
      {
         if(this.index < this.animations.size())
         {
            this.animations.get(this.index).endAnimation(image);
         }
      }
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
      this.loopLeft = this.numberOfLoop;
      this.index = 0;

      synchronized(this.animations)
      {
         if(this.animations.size() > 0)
         {
            this.animations.get(0).startAnimation(startAbslouteFrame, image);
         }
      }
   }
}