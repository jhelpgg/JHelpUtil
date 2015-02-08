package jhelp.util.gui.dynamic;

import jhelp.util.gui.JHelpImage;
import jhelp.util.list.SortedArray;

/**
 * Generic animation by key frames.<br>
 * This animation types say sat this frame the object must have this value and interpolate the values if the frame is between 2
 * defined frame.<br>
 * See {@link AnimationPosition} to have an example how extends it
 * 
 * @author JHelp
 * @param <OBJECT>
 *           Object to animate type
 * @param <VALUE>
 *           Value to change type
 */
public abstract class AnimationKeyFrame<OBJECT, VALUE>
      implements DynamicAnimation
{
   /**
    * Key frame description
    * 
    * @author JHelp
    * @param <V>
    *           Value type
    */
   @SuppressWarnings("rawtypes")
   class KeyFrame<V>
         implements Comparable<KeyFrame>
   {
      /** Frame number */
      final int frame;
      /** Value at given frame */
      V         value;

      /**
       * Create a new instance of KeyFrame
       * 
       * @param frame
       *           Frame number
       * @param value
       *           Value at given frame
       */
      KeyFrame(final int frame, final V value)
      {
         this.frame = frame;
         this.value = value;
      }

      /**
       * Compare with an other key frame.<br>
       * It returns :
       * <table border=0>
       * <tr>
       * <th>&lt;0</th>
       * <td>: If this frame before given one</td>
       * </tr>
       * <tr>
       * <th>0</th>
       * <td>: If this frame at same place at given one</td>
       * </tr>
       * <tr>
       * <th>&gt;0</th>
       * <td>: If this frame after given one</td>
       * </tr>
       * </table>
       * <br>
       * <br>
       * <b>Parent documentation:</b><br>
       * {@inheritDoc}
       * 
       * @param keyFrame
       *           Key frame to compare with
       * @return Comparison result
       * @see java.lang.Comparable#compareTo(java.lang.Object)
       */
      @Override
      public int compareTo(final KeyFrame keyFrame)
      {
         return this.frame - keyFrame.frame;
      }
   }

   /** Interpolation to use */
   private final Interpolation         interpolation;
   /** Key frame list */
   @SuppressWarnings("rawtypes")
   private final SortedArray<KeyFrame> keyFrames;
   /** Number of loop */
   private final int                   loop;
   /** Number of loop left */
   private int                         loopLeft;
   /** Object to modify */
   private final OBJECT                object;
   /** Start absolute frame */
   private float                       startAbsoluteFrame;
   /** Start value */
   private VALUE                       startValue;

   /**
    * Create a new instance of AnimationKeyFrame
    * 
    * @param object
    *           Object to modify
    * @param numberOfLoop
    *           Number of loop to do
    * @param interpolation
    *           Interpolation type to use
    */
   @SuppressWarnings("rawtypes")
   public AnimationKeyFrame(final OBJECT object, final int numberOfLoop, final Interpolation interpolation)
   {
      if(object == null)
      {
         throw new NullPointerException("object musn't be null");
      }

      if(interpolation == null)
      {
         throw new NullPointerException("interpolation musn't be null");
      }

      this.loop = Math.max(1, numberOfLoop);
      this.object = object;
      this.interpolation = interpolation;
      this.keyFrames = new SortedArray<KeyFrame>(KeyFrame.class, true);
   }

   /**
    * Do an animation loop
    * 
    * @param absoluteFrame
    *           Absolute frame
    * @return Indicates if there more loop to do
    */
   @SuppressWarnings("unchecked")
   private boolean doLoop(final float absoluteFrame)
   {
      this.loopLeft--;

      if(this.loopLeft <= 0)
      {
         return false;
      }

      this.startAbsoluteFrame = absoluteFrame;
      this.startValue = null;

      final KeyFrame<VALUE> keyFrame = this.keyFrames.getElement(0);

      if(keyFrame.frame == 0)
      {
         this.setValue(this.object, keyFrame.value);
      }

      return true;
   }

   /**
    * Define a value to a frame position
    * 
    * @param frame
    *           Frame position
    * @param value
    *           Value to set
    */
   @SuppressWarnings("unchecked")
   public final void addFrame(final int frame, final VALUE value)
   {
      if(frame < 0)
      {
         throw new IllegalArgumentException("frame MUST be >=0");
      }

      if(value == null)
      {
         throw new NullPointerException("value musn't be null");
      }

      synchronized(this.keyFrames)
      {
         final KeyFrame<VALUE> keyFrame = new KeyFrame<VALUE>(frame, value);
         final int index = this.keyFrames.indexOf(keyFrame);

         if(index < 0)
         {
            this.keyFrames.add(keyFrame);
         }
         else
         {
            this.keyFrames.getElement(index).value = value;
         }
      }
   }

   /**
    * Called at each time animation refresh <br>
    * <br>
    * <b>Parent documentation:</b><br>
    * {@inheritDoc}
    * 
    * @param absoluteFrame
    *           Absolute frame
    * @param image
    *           Image parent
    * @return {@code true} if animation have to continue. {@code false} if animation finished
    * @see jhelp.util.gui.dynamic.DynamicAnimation#animate(float, jhelp.util.gui.JHelpImage)
    */
   @SuppressWarnings("unchecked")
   @Override
   public final boolean animate(final float absoluteFrame, final JHelpImage image)
   {
      synchronized(this.keyFrames)
      {
         int firstFrame;
         int lastFrame;
         int frame;
         int size;
         float actualFrame;
         float percent;
         VALUE before;
         VALUE after;

         // If there are no frame, nothing to do
         size = this.keyFrames.getSize();
         if(size < 1)
         {
            return false;
         }

         // Compute reference frames
         KeyFrame<VALUE> keyframeFirst = this.keyFrames.getElement(0);
         KeyFrame<VALUE> keyframeLast = this.keyFrames.getElement(size - 1);
         firstFrame = keyframeFirst.frame;
         lastFrame = keyframeLast.frame;
         actualFrame = absoluteFrame - this.startAbsoluteFrame;

         // If we are before the first frame (It is possible to start at a frame
         // >0, the effect is an interpolation from the actual value, to the first
         // frame)
         if(actualFrame < firstFrame)
         {
            // Interpolate actual position to first frame
            if(this.startValue == null)
            {
               this.startValue = this.createValue(this.object);
            }

            before = this.startValue;
            after = keyframeFirst.value;
            percent = actualFrame / firstFrame;

            this.interpolate(this.object, before, after, this.interpolation.interpolation(percent));

            return true;
         }

         this.startValue = null;

         // If we are after the last frame, just position in the last frame and the
         // animation is done
         if(actualFrame >= lastFrame)
         {
            this.setValue(this.object, keyframeLast.value);
            return this.doLoop(absoluteFrame);
         }

         // Compute the nearest frame index from the actual frame
         for(frame = 0; (frame < size) && (this.keyFrames.getElement(frame).frame < actualFrame); frame++)
         {
            ;
         }

         // If it is the first frame, just locate to the first and the animation
         // continue
         if(frame == 0)
         {
            this.setValue(this.object, keyframeFirst.value);
            return true;
         }

         // If it is after the last frame, locate at last and the animation is
         // finish
         if(frame >= size)
         {
            this.setValue(this.object, keyframeLast.value);
            return this.doLoop(absoluteFrame);
         }

         // Interpolate the value and animation continue
         keyframeFirst = this.keyFrames.getElement(frame - 1);
         keyframeLast = this.keyFrames.getElement(frame);
         before = keyframeFirst.value;
         after = keyframeLast.value;
         percent = (actualFrame - keyframeFirst.frame) / (keyframeLast.frame - keyframeFirst.frame);

         this.interpolate(this.object, before, after, this.interpolation.interpolation(percent));

         return true;
      }
   }

   /**
    * Obtain the current value for the object
    * 
    * @param object
    *           Object to extract value
    * @return Current value from the object
    */
   public abstract VALUE createValue(OBJECT object);

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
    * Interpolate value and set the object
    * 
    * @param object
    *           Object to modify
    * @param before
    *           Value of first frame
    * @param after
    *           Value of second frame
    * @param percent
    *           Percent of interpolation between first and second frame
    */
   public abstract void interpolate(OBJECT object, VALUE before, VALUE after, float percent);

   /**
    * Remove a frame
    * 
    * @param frame
    *           Frame to remove
    */
   public final void removeFrame(final int frame)
   {
      synchronized(this.keyFrames)
      {
         final KeyFrame<VALUE> keyFrame = new KeyFrame<VALUE>(frame, null);
         this.keyFrames.remove(keyFrame);
      }
   }

   /**
    * Set a value to the object
    * 
    * @param object
    *           Object to modify
    * @param value
    *           Value to set
    */
   public abstract void setValue(OBJECT object, VALUE value);

   /**
    * Called when animation start <br>
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
   @SuppressWarnings("unchecked")
   @Override
   public final void startAnimation(final float startAbsoluteFrame, final JHelpImage image)
   {
      this.startAbsoluteFrame = startAbsoluteFrame;
      this.loopLeft = this.loop;
      this.startValue = null;

      synchronized(this.keyFrames)
      {
         if(this.keyFrames.isEmpty() == false)
         {
            final KeyFrame<VALUE> keyFrame = this.keyFrames.getElement(0);

            if(keyFrame.frame == 0)
            {
               this.setValue(this.object, keyFrame.value);
            }
         }
      }
   }
}