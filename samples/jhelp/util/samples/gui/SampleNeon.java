package jhelp.util.samples.gui;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.WindowConstants;

import jhelp.util.gui.JHelpFont;
import jhelp.util.gui.JHelpImage;
import jhelp.util.gui.Path;
import jhelp.util.gui.UtilGUI;
import jhelp.util.samples.common.gui.SampleLabelJHelpImage;
import jhelp.util.thread.ThreadManager;
import jhelp.util.thread.ThreadedVerySimpleTask;

/**
 * Neon effect sample
 *
 * @author JHelp <br>
 */
public class SampleNeon
      extends ThreadedVerySimpleTask
{
   /** Image where draw */
   static JHelpImage image;
   /** Animation end index */
   static int        indexEnd;
   /** Animation start index */
   static int        indexStart;
   /** Number of animation step */
   static final int numberStep = 256;
   /** Path follow by neon effect */
   static Path       path;
   /** Animation thread ID */
   static int        threadID;

   /**
    * Launch the sample of neon effect
    *
    * @param args
    *           Unused
    */
   public static void main(final String[] args)
   {
      final JHelpFont font = new JHelpFont("Arial", 150);
      SampleNeon.path = new Path();
      SampleNeon.path.appendText("Neon sample", font, 20, 20);
      SampleNeon.image = new JHelpImage(1024, 512, 0xFF000000);

      final JFrame frame = new JFrame();
      frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
      frame.setLayout(new BorderLayout());
      final SampleLabelJHelpImage sampleLabelJHelpImage = new SampleLabelJHelpImage(SampleNeon.image);
      sampleLabelJHelpImage.setResize(true);
      frame.add(sampleLabelJHelpImage, BorderLayout.CENTER);
      UtilGUI.packedSize(frame);
      UtilGUI.centerOnScreen(frame);
      frame.setVisible(true);

      SampleNeon.indexStart = 0;
      SampleNeon.indexEnd = 0;
      SampleNeon.threadID = ThreadManager.THREAD_MANAGER.repeatThread(new SampleNeon(), null, 1024, 1);
   }

   /**
    * Do one animation step <br>
    * <br>
    * <b>Parent documentation:</b><br>
    * {@inheritDoc}
    *
    * @see jhelp.util.thread.ThreadedVerySimpleTask#doVerySimpleAction()
    */
   @Override
   protected void doVerySimpleAction()
   {
      final double percentStart = (double) SampleNeon.indexStart / (double) SampleNeon.numberStep;
      final double percentEnd = (double) SampleNeon.indexEnd / (double) SampleNeon.numberStep;

      SampleNeon.image.startDrawMode();
      SampleNeon.image.clear(0xFF000000);
      SampleNeon.image.drawNeon(SampleNeon.path, 12, 0xFF0A7E07, percentStart, percentEnd);
      SampleNeon.image.endDrawMode();

      if(SampleNeon.indexEnd < SampleNeon.numberStep)
      {
         SampleNeon.indexEnd++;
      }
      else if(SampleNeon.indexStart < SampleNeon.numberStep)
      {
         SampleNeon.indexStart++;
      }
      else
      {
         SampleNeon.indexStart = 0;
         SampleNeon.indexEnd = 0;
      }
   }
}