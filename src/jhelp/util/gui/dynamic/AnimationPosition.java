package jhelp.util.gui.dynamic;

/**
 * Animation key frame that change an object position
 * 
 * @author JHelp
 */
public class AnimationPosition
      extends AnimationKeyFrame<Positionable, Position>
{
   /**
    * Create a new instance of AnimationPosition
    * 
    * @param object
    *           Object to move
    * @param numberOfLoop
    *           Number of loop
    * @param interpolation
    *           Interpolation type
    */
   public AnimationPosition(final Positionable object, final int numberOfLoop, final Interpolation interpolation)
   {
      super(object, numberOfLoop, interpolation);
   }

   /**
    * Obtain an object position <br>
    * <br>
    * <b>Parent documentation:</b><br>
    * {@inheritDoc}
    * 
    * @param object
    *           Object to get its position
    * @return Object's position
    * @see jhelp.util.gui.dynamic.AnimationKeyFrame#createValue(java.lang.Object)
    */
   @Override
   public Position createValue(final Positionable object)
   {
      return object.getPosition();
   }

   /**
    * Interpolate position and locate object in interpolated value <br>
    * <br>
    * <b>Parent documentation:</b><br>
    * {@inheritDoc}
    * 
    * @param object
    *           Object to move
    * @param before
    *           Position before
    * @param after
    *           Position after
    * @param percent
    *           Interpolation value
    * @see jhelp.util.gui.dynamic.AnimationKeyFrame#interpolate(java.lang.Object, java.lang.Object, java.lang.Object, float)
    */
   @Override
   public void interpolate(final Positionable object, final Position before, final Position after, final float percent)
   {
      object.setPosition(Position.computeInterpolation(before, after, percent));
   }

   /**
    * Set object's position <br>
    * <br>
    * <b>Parent documentation:</b><br>
    * {@inheritDoc}
    * 
    * @param object
    *           Object to place
    * @param value
    *           New position
    * @see jhelp.util.gui.dynamic.AnimationKeyFrame#setValue(java.lang.Object, java.lang.Object)
    */
   @Override
   public void setValue(final Positionable object, final Position value)
   {
      object.setPosition(value);
   }
}