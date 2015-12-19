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

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import jhelp.util.gui.JHelpImage;
import jhelp.util.list.Pair;
import jhelp.util.thread.ThreadManager;
import jhelp.util.thread.ThreadedSimpleTask;

/**
 * Image that can play animation
 * 
 * @author JHelp
 */
public class JHelpDynamicImage
{
   /**
    * Task for signal to a listener that an animation is finished
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
       * Do the task : signal to a listener that an animation is finished <br>
       * <br>
       * <b>Parent documentation:</b><br>
       * {@inheritDoc}
       * 
       * @param parameter
       *           Pair of animation finished and lister to alert
       * @see jhelp.util.thread.ThreadedSimpleTask#doSimpleAction(java.lang.Object)
       */
      @Override
      protected void doSimpleAction(final Pair<DynamicAnimation, DynamicAnimationFinishListener> parameter)
      {
         parameter.element2.dynamicAnimationFinished(parameter.element1);
      }
   }

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
   public static final int                                                    FPS = 25;
   /** Indicates if refresh thread still alive */
   private final AtomicBoolean                                                alive;
   /** Animations to refresh list */
   private final List<Pair<DynamicAnimation, DynamicAnimationFinishListener>> animations;
   /** Current background */
   private Background                                                         background;
   /** Height */
   private final int                                                          height;
   /** Image that show animation */
   private final JHelpImage                                                   image;
   /** Lock for synchronization */
   private final Object                                                       lock;
   /** Task for signal to a listener that an animation is finished */
   private final TaskCallBackFinishListener                                   taskCallBackFinishListener;
   /** Refresh image task */
   private TaskRefreshImage                                                   taskRefreshImage;
   /** Time when animation started */
   private long                                                               timeStart;
   /** Indicates if task refresh wait next instruction */
   private final AtomicBoolean                                                waiting;
   /** Width */
   private final int                                                          width;
   /** Listener of image dynamic events */
   JHelpDynamicImageListener                                                  dynamicImageListener;

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
      this.animations = new ArrayList<Pair<DynamicAnimation, DynamicAnimationFinishListener>>();
      this.width = Math.max(128, width);
      this.height = Math.max(128, height);
      this.image = new JHelpImage(this.width, this.height);
      this.taskCallBackFinishListener = new TaskCallBackFinishListener();
      this.background = new Background();
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
      Pair<DynamicAnimation, DynamicAnimationFinishListener> animation;
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
            this.background.drawBackground(this.getAbsoluteFrame(), this.image);
            size = this.animations.size();

            for(index = size - 1; index >= 0; index--)
            {
               animation = this.animations.get(index);

               if(animation.element1.animate(this.getAbsoluteFrame(), this.image) == false)
               {
                  this.animations.remove(index);

                  this.image.endDrawMode();
                  animation.element1.endAnimation(this.image);
                  this.image.startDrawMode();

                  if(animation.element2 != null)
                  {
                     ThreadManager.THREAD_MANAGER.doThread(this.taskCallBackFinishListener, animation);
                  }
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
      this.playAnimation(dynamicAnimation, null);
   }

   /**
    * Play an animation
    * 
    * @param dynamicAnimation
    *           Animation to play
    * @param listener
    *           Listener to call back when given animation finished. {@code null} means no listener
    */
   public void playAnimation(final DynamicAnimation dynamicAnimation, final DynamicAnimationFinishListener listener)
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

         this.animations.add(new Pair<DynamicAnimation, DynamicAnimationFinishListener>(dynamicAnimation, listener));

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
    * Change background
    * 
    * @param background
    *           New background
    */
   public void setBackground(final Background background)
   {
      if(background == null)
      {
         throw new NullPointerException("background musn't be null");
      }

      this.background = background;
      this.background.startBackground(this.getAbsoluteFrame());
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
         Pair<DynamicAnimation, DynamicAnimationFinishListener> animation = null;

         for(final Pair<DynamicAnimation, DynamicAnimationFinishListener> pair : this.animations)
         {
            if(pair.element1.equals(dynamicAnimation) == true)
            {
               animation = pair;
               break;
            }
         }

         if(animation != null)
         {
            this.animations.remove(animation);
            final boolean drawMode = this.image.isDrawMode();
            this.image.endDrawMode();
            dynamicAnimation.endAnimation(this.image);

            if(animation.element2 != null)
            {
               ThreadManager.THREAD_MANAGER.doThread(this.taskCallBackFinishListener, animation);
            }

            if(drawMode == true)
            {
               this.image.startDrawMode();
            }
         }
      }
   }
}