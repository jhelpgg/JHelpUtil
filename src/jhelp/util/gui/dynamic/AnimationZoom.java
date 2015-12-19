/**
 * <h1>License :</h1> <br>
 * The following code is deliver as is. I take care that code compile and work, but I am not responsible about any damage it may
 * cause.<br>
 * You can use, modify, the code as your need for any usage. But you can't do any action that avoid me or other person use,
 * modify this code. The code is free for usage and modification, you can't change that fact.<br>
 * <br>
 * 
 * @author JHelp
 */
package jhelp.util.gui.dynamic;

import jhelp.util.gui.JHelpImage;
import jhelp.util.gui.JHelpSprite;

/**
 * Animation zoom effect on an image
 * 
 * @author JHelp
 */
public class AnimationZoom
      implements DynamicAnimation, Positionable
{
   /** Image height at animation end */
   private final int           endHeight;
   /** Image width at animation end */
   private final int           endWidth;
   /** Image original height */
   private final int           imageHeight;
   /** Image original width */
   private final int           imageWidth;
   /** Interpolation to use */
   private final Interpolation interpolation;
   /** Original image */
   private final JHelpImage    maxZoomImage;
   /** Number of frame to play animation */
   private final int           numberOfFrame;
   /** Sprite where animation draw */
   private JHelpSprite         sprite;
   /** Start animation absolute frame */
   private float               startAbsoluteFrame;
   /** Image height at animation start */
   private final int           startHeight;
   /** Image width at animation start */
   private final int           startWidth;
   /** Animation position X */
   private int                 x;
   /** Animation position Y */
   private int                 y;

   /**
    * Create a new instance of AnimationZoom
    * 
    * @param maxZoomImage
    *           Original image. It must have the maximum size
    * @param startWidth
    *           Start animation width
    * @param startHeight
    *           Start animation height
    * @param endWidth
    *           End animation width
    * @param endHeight
    *           End animation height
    * @param numberOfFrame
    *           Number of frame for play animation
    * @param interpolation
    *           Interpolation to use
    */
   public AnimationZoom(final JHelpImage maxZoomImage, final int startWidth, final int startHeight, final int endWidth, final int endHeight,
         final int numberOfFrame, final Interpolation interpolation)
   {
      if(interpolation == null)
      {
         throw new NullPointerException("interpolation musn't be null");
      }

      this.maxZoomImage = maxZoomImage;
      this.imageWidth = this.maxZoomImage.getWidth();
      this.imageHeight = this.maxZoomImage.getHeight();
      this.startWidth = Math.max(1, Math.min(this.imageWidth, startWidth));
      this.startHeight = Math.max(1, Math.min(this.imageHeight, startHeight));
      this.endWidth = Math.max(1, Math.min(this.imageWidth, endWidth));
      this.endHeight = Math.max(1, Math.min(this.imageHeight, endHeight));
      this.numberOfFrame = Math.max(1, numberOfFrame);
      this.interpolation = interpolation;
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
    *           Image where draw animation
    * @return {@code true} if animation continue OR {@code false} if animation finished
    * @see jhelp.util.gui.dynamic.DynamicAnimation#animate(float, jhelp.util.gui.JHelpImage)
    */
   @Override
   public boolean animate(final float absoluteFrame, final JHelpImage image)
   {
      boolean willContinue = true;
      float percent, anti;
      final float frame = absoluteFrame - this.startAbsoluteFrame;

      if(frame >= this.numberOfFrame)
      {
         willContinue = false;
         percent = 1;
         anti = 0;
      }
      else
      {
         percent = this.interpolation.interpolation(frame / this.numberOfFrame);
         anti = 1f - percent;
      }

      final int currentWidth = (int) ((this.startWidth * anti) + (this.endWidth * percent));
      final int currentHeight = (int) ((this.startHeight * anti) + (this.endHeight * percent));
      final int xx = (this.imageWidth - currentWidth) >> 1;
      final int yy = (this.imageHeight - currentHeight) >> 1;

      final JHelpImage spriteImage = this.sprite.getImage();
      final JHelpImage temp = spriteImage.createCopy();
      temp.startDrawMode();
      temp.clear(0);
      temp.fillRectangleScale(xx, yy, currentWidth, currentHeight, this.maxZoomImage);
      temp.endDrawMode();
      spriteImage.copy(temp);

      return willContinue;
   }

   /**
    * Called when animation finished <br>
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
      if(this.sprite != null)
      {
         image.removeSprite(this.sprite);
         this.sprite = null;
      }
   }

   /**
    * Current animation position <br>
    * <br>
    * <b>Parent documentation:</b><br>
    * {@inheritDoc}
    * 
    * @return Current animation position
    * @see jhelp.util.gui.dynamic.Positionable#getPosition()
    */
   @Override
   public Position getPosition()
   {
      return new Position(this.x, this.y);
   }

   /**
    * Change animation position <br>
    * <br>
    * <b>Parent documentation:</b><br>
    * {@inheritDoc}
    * 
    * @param position
    *           New position
    * @see jhelp.util.gui.dynamic.Positionable#setPosition(jhelp.util.gui.dynamic.Position)
    */
   @Override
   public void setPosition(final Position position)
   {
      this.x = position.getX();
      this.y = position.getY();

      if(this.sprite != null)
      {
         this.sprite.setPosition(this.x, this.y);
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
      this.startAbsoluteFrame = startAbsoluteFrame;
      this.sprite = image.createSprite(this.x, this.y, this.imageWidth, this.imageHeight);
      this.animate(this.startAbsoluteFrame, image);
      this.sprite.setVisible(true);
   }
}