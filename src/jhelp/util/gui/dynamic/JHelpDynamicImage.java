package jhelp.util.gui.dynamic;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import jhelp.util.gui.JHelpImage;

/**
 * Image that can play animation
 * 
 * @author JHelp
 */
public class JHelpDynamicImage
{
   /**
    * Task that refresh the image
    * 
    * @author JHelp
    */
   class TaskRefreshImage
         extends Thread
   {
      /**
       * Create a new instance of TaskRefreshImage
       */
      TaskRefreshImage()
      {
      }

      /**
       * Refresh the image <br>
       * <br>
       * <b>Parent documentation:</b><br>
       * {@inheritDoc}
       * 
       * @see java.lang.Thread#run()
       */
      @Override
      public void run()
      {
         JHelpDynamicImage.this.doRefreshImage();
      }
   }

   /** Animation frame per seconds */
   public static final int              FPS = 25;
   /** Indicates if refresh thread still alive */
   private final AtomicBoolean          alive;
   /** Animations to refresh list */
   private final List<DynamicAnimation> animations;
   /** Height */
   private final int                    height;
   /** Image that show animation */
   private final JHelpImage             image;
   /** Lock for synchronization */
   private final Object                 lock;
   /** Refresh image task */
   private TaskRefreshImage             taskRefreshImage;
   /** Time when animation started */
   private long                         timeStart;
   /** Indicates if task refresh wait next instruction */
   private final AtomicBoolean          waiting;
   /** Width */
   private final int                    width;
   /** Listener of image dynamic events */
   JHelpDynamicImageListener            dynamicImageListener;

   /**
    * Create a new instance of JHelpDynamicImage
    * 
    * @param width
    *           Width
    * @param height
    *           Height
    */
   public JHelpDynamicImage(final int width, final int height)
   {
      this.lock = new Object();
      this.alive = new AtomicBoolean(true);
      this.waiting = new AtomicBoolean(false);
      this.taskRefreshImage = new TaskRefreshImage();
      this.animations = new ArrayList<DynamicAnimation>();
      this.width = Math.max(128, width);
      this.height = Math.max(128, height);
      this.image = new JHelpImage(this.width, this.height);

      this.timeStart = System.currentTimeMillis();
      this.taskRefreshImage.start();
   }

   /**
    * Actual absolute frame
    * 
    * @return Actual absolute frame
    */
   private float getAbsoluteFrame()
   {
      return (float) (((System.currentTimeMillis() - this.timeStart) * JHelpDynamicImage.FPS) / 1000d);
   }

   /**
    * Refresh the image
    */
   void doRefreshImage()
   {
      DynamicAnimation dynamicAnimation;
      int size, index;

      while(this.alive.get() == true)
      {
         synchronized(this.lock)
         {
            while(this.animations.isEmpty() == true)
            {
               this.waiting.set(true);

               try
               {
                  this.lock.wait();
               }
               catch(final Exception exception)
               {
               }

               this.waiting.set(false);

               if(this.alive.get() == false)
               {
                  return;
               }
            }
         }

         synchronized(this.lock)
         {
            this.image.startDrawMode();
            size = this.animations.size();

            for(index = size - 1; index >= 0; index--)
            {
               dynamicAnimation = this.animations.get(index);

               if(dynamicAnimation.animate(this.getAbsoluteFrame(), this.image) == false)
               {
                  this.animations.remove(index);

                  this.image.endDrawMode();
                  dynamicAnimation.endAnimation(this.image);
                  this.image.startDrawMode();
               }
            }

            this.image.endDrawMode();
         }

         if(this.dynamicImageListener != null)
         {
            this.dynamicImageListener.dynamicImageUpdate(this);
         }

         synchronized(this.lock)
         {
            try
            {
               this.lock.wait(8);
            }
            catch(final Exception exception)
            {
            }
         }
      }
   }

   /**
    * Destroy properly the image and stop the refresh thread
    */
   public void destroy()
   {
      synchronized(this.lock)
      {
         this.alive.set(false);

         if(this.waiting.get() == true)
         {
            this.lock.notify();
         }
      }
   }

   /**
    * Height
    * 
    * @return Height
    */
   public int getHeight()
   {
      return this.height;
   }

   /**
    * Image that shows animations
    * 
    * @return Image that shows animations
    */
   public JHelpImage getImage()
   {
      return this.image;
   }

   /**
    * Lock for synchronization.<br>
    * Be sure on what you do on using it, don't block animations
    * 
    * @return Lock for synchronization
    */
   public Object getLock()
   {
      return this.lock;
   }

   /**
    * Width
    * 
    * @return Width
    */
   public int getWidth()
   {
      return this.width;
   }

   /**
    * Play an animation
    * 
    * @param dynamicAnimation
    *           Animation to play
    */
   public void playAnimation(final DynamicAnimation dynamicAnimation)
   {
      synchronized(this.lock)
      {
         final boolean drawMode = this.image.isDrawMode();
         this.image.endDrawMode();
         dynamicAnimation.startAnimation(this.getAbsoluteFrame(), this.image);

         if(drawMode == true)
         {
            this.image.startDrawMode();
         }

         this.animations.add(dynamicAnimation);

         if(this.waiting.get() == true)
         {
            this.lock.notify();
         }
      }

      if(this.alive.get() == false)
      {
         this.alive.set(true);
         this.taskRefreshImage = new TaskRefreshImage();
         this.timeStart = System.currentTimeMillis();
         this.taskRefreshImage.start();
      }
   }

   /**
    * Stop an animation
    * 
    * @param dynamicAnimation
    *           Animation to stop
    */
   public void stopAnimation(final DynamicAnimation dynamicAnimation)
   {
      synchronized(this.lock)
      {
         this.animations.remove(dynamicAnimation);

         final boolean drawMode = this.image.isDrawMode();
         this.image.endDrawMode();
         dynamicAnimation.endAnimation(this.image);

         if(drawMode == true)
         {
            this.image.startDrawMode();
         }
      }
   }
}