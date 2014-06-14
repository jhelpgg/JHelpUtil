package jhelp.util.samples.gui;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;

import jhelp.util.debug.Debug;
import jhelp.util.gui.GIF;
import jhelp.util.math.random.JHelpRandom;
import jhelp.util.resources.ResourceElement;
import jhelp.util.resources.ResourceFile;
import jhelp.util.resources.Resources;
import jhelp.util.resources.ResourcesSystem;
import jhelp.util.samples.common.gui.SampleLabelJHelpImage;

/**
 * Sample for test the GIF
 * 
 * @author JHelp
 */
public class SampleGIF
      implements ActionListener
{
   /**
    * Launch the sample
    * 
    * @param args
    *           Unused
    */
   public static void main(final String[] args)
   {
      final SampleGIF sampleGIF = new SampleGIF();
      sampleGIF.initialize();
   }

   /** GIF played */
   private GIF                   gif;
   /** GIF image index */
   private int                   index;
   /** Label draw GIF current image */
   private SampleLabelJHelpImage labelJHelpImage;

   /**
    * Create a new instance of SampleGIF
    */
   public SampleGIF()
   {
   }

   /**
    * Called when next is pressed <br>
    * <br>
    * <b>Parent documentation:</b><br>
    * {@inheritDoc}
    * 
    * @param e
    *           Action event
    * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
    */
   @Override
   public void actionPerformed(final ActionEvent e)
   {
      if(this.gif != null)
      {
         this.index = (this.index + 1) % this.gif.numberOfImage();
         this.labelJHelpImage.setJHelpImage(this.gif.getImage(this.index));
      }
   }

   /**
    * Initialize the frame and the GIF
    */
   public void initialize()
   {
      final JFrame frame = new JFrame();
      this.labelJHelpImage = new SampleLabelJHelpImage();
      frame.setLayout(new BorderLayout());
      frame.add(this.labelJHelpImage, BorderLayout.CENTER);
      final JButton button = new JButton("NEXT");
      button.addActionListener(this);
      frame.add(button, BorderLayout.SOUTH);
      this.index = 0;

      try
      {
         final Resources resources = new Resources(SampleGIF.class);
         final ResourcesSystem resourcesSystem = resources.obtainResourcesSystem();
         final List<ResourceElement> elements = resourcesSystem.obtainList(ResourcesSystem.ROOT);
         ResourceElement element = null;

         while(element == null)
         {
            element = JHelpRandom.random(elements);

            if(element.getName().endsWith(".gif") == false)
            {
               element = null;
            }
         }

         this.gif = new GIF(resourcesSystem.obtainInputStream((ResourceFile) element));
         this.labelJHelpImage.setJHelpImage(this.gif.getImage(0));
      }
      catch(final Exception exception)
      {
         Debug.printException(exception, "Failed to show !");
      }

      frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      frame.pack();
      frame.setVisible(true);
   }
}