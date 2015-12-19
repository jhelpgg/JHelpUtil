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
package jhelp.util.gui.dynamic.font;

import jhelp.util.gui.GIF;
import jhelp.util.gui.JHelpImage;
import jhelp.util.gui.JHelpSprite;
import jhelp.util.gui.dynamic.DynamicAnimation;
import jhelp.util.gui.dynamic.Position;
import jhelp.util.gui.dynamic.Positionable;

/**
 * Animation of text draw with GIFs
 * 
 * @author JHelp
 */
public class AnimationFontGif
      implements DynamicAnimation, Positionable
{
   /** Current GIF text */
   private final GifText gifText;
   /** Sprite where draw animation */
   private JHelpSprite   sprite;
   /** Start absolute frame */
   private float         startAbslouteFrame;
   /** X */
   private int           x;
   /** Y */
   private int           y;

   /**
    * Create a new instance of AnimationFontGif
    * 
    * @param x
    *           Start X
    * @param y
    *           Start Y
    * @param text
    *           Text to draw
    * @param fontGif
    *           Font GIF to use
    */
   public AnimationFontGif(final int x, final int y, final String text, final FontGif fontGif)
   {
      this.x = x;
      this.y = y;
      this.gifText = fontGif.computeGifText(text);
   }

   /**
    * Called each time animation refreshed <br>
    * <br>
    * <b>Parent documentation:</b><br>
    * {@inheritDoc}
    * 
    * @param absoluteFrame
    *           Absolute frame
    * @param image
    *           Image parent
    * @return {@code true} if animation continues. {@code false} if animation stopped
    * @see jhelp.util.gui.dynamic.DynamicAnimation#animate(float, jhelp.util.gui.JHelpImage)
    */
   @Override
   public boolean animate(final float absoluteFrame, final JHelpImage image)
   {
      if(this.sprite == null)
      {
         return false;
      }

      final JHelpImage spriteImage = this.sprite.getImage();
      spriteImage.startDrawMode();
      spriteImage.clear(0);
      GIF gif;

      for(final GifPosition gifPosition : this.gifText.getGifPositions())
      {
         gif = gifPosition.getGif();
         spriteImage.drawImage(gifPosition.getX(), gifPosition.getY(),
               gif.getImage((int) ((absoluteFrame - this.startAbslouteFrame) / 4f) % gif.numberOfImage()));
      }

      spriteImage.endDrawMode();

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
      if(this.sprite != null)
      {
         image.removeSprite(this.sprite);
         this.sprite = null;
      }
   }

   /**
    * Actual position <br>
    * <br>
    * <b>Parent documentation:</b><br>
    * {@inheritDoc}
    * 
    * @return Actual position
    * @see jhelp.util.gui.dynamic.Positionable#getPosition()
    */
   @Override
   public Position getPosition()
   {
      return new Position(this.x, this.y);
   }

   /**
    * Change animated text position <br>
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
    * @param startAbslouteFrame
    *           Start absolute frame
    * @param image
    *           Image parent
    * @see jhelp.util.gui.dynamic.DynamicAnimation#startAnimation(float, jhelp.util.gui.JHelpImage)
    */
   @Override
   public void startAnimation(final float startAbslouteFrame, final JHelpImage image)
   {
      this.startAbslouteFrame = startAbslouteFrame;
      final int width = this.gifText.getWidth();
      final int height = this.gifText.getHeight();

      if((width <= 0) || (height <= 0))
      {
         return;
      }

      this.sprite = image.createSprite(this.x, this.y, width, height);
      GIF gif;
      final JHelpImage spriteImage = this.sprite.getImage();
      spriteImage.startDrawMode();

      for(final GifPosition gifPosition : this.gifText.getGifPositions())
      {
         gif = gifPosition.getGif();
         spriteImage.drawImage(gifPosition.getX(), gifPosition.getY(), gif.getImage(0));
      }

      spriteImage.endDrawMode();
      this.sprite.setVisible(true);
   }
}