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

/**
 * Animation on dynamic image
 * 
 * @author JHelp
 */
public interface DynamicAnimation
{
   /**
    * Play the animation.<br>
    * The given image is on draw mode <b>DON'T change this !</b><br>
    * It for draw the animation
    * 
    * @param absoluteFrame
    *           Absolute frame
    * @param image
    *           Image parent where draw
    * @return {@code true} if animation continues. {@code false} if animation finished
    */
   public boolean animate(float absoluteFrame, JHelpImage image);

   /**
    * Terminate properly the animation<br>
    * The given image is not in draw mode <b>DON'T change this !</b>. It just here to remove properly created sprites for the
    * animation
    * 
    * @param image
    *           Image parent
    */
   public void endAnimation(JHelpImage image);

   /**
    * Start the animation.<br>
    * The given image is not in draw mode <b>DON'T change this !</b>. It let you opportunity to create sprites for your
    * animation
    * 
    * @param startAbsoluteFrame
    *           Start absolute frame
    * @param image
    *           Image parent
    */
   public void startAnimation(float startAbsoluteFrame, JHelpImage image);
}