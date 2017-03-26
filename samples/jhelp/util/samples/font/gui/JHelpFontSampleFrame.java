package jhelp.util.samples.font.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.ScrollPaneConstants;
import javax.swing.SpinnerNumberModel;
import javax.swing.WindowConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import jhelp.util.gui.JHelpFont;
import jhelp.util.gui.JHelpImage;
import jhelp.util.gui.JHelpTextAlign;
import jhelp.util.gui.JHelpTextLine;
import jhelp.util.list.Pair;
import jhelp.util.samples.common.gui.SampleLabelJHelpImage;

/**
 * Frame for font size and text alignment sample
 *
 * @author JHelp
 */
@SuppressWarnings("deprecation")
public class JHelpFontSampleFrame
      extends JFrame
      implements ChangeListener
{
   /** Image that draw the text height */
   private final static int            HEIGHT = 1000;
   /** Text for test */
   private final static String         STRING = "Pseudopseudohypoparathyroidism is a long word. So Pseudopseudohypoparathyroidism is a good candidate for show how cut text work";
   /** Image that draw the text height */
   private final static int            WIDTH  = 500;
   /** Actual font */
   private JHelpFont                   font;
   /** Image that draw the text */
   private final JHelpImage            imageMain;
   /** Swing component that carry the image */
   private final SampleLabelJHelpImage sampleLabelJHelpImage;
   /** Actual font size */
   private int                         size;
   /** Spinner for change size */
   private final JSpinner              spinnerSize;

   /**
    * Create a new instance of JHelpFontSampleFrame
    */
   public JHelpFontSampleFrame()
   {
      super("JHelpFont sample");

      this.size = 20;
      this.spinnerSize = new JSpinner(new SpinnerNumberModel(this.size, 10, 400, 1));
      this.imageMain = new JHelpImage(JHelpFontSampleFrame.WIDTH, JHelpFontSampleFrame.HEIGHT, 0xFFFFFFFF);
      this.sampleLabelJHelpImage = new SampleLabelJHelpImage(this.imageMain);

      this.setLayout(new BorderLayout());

      this.add(new JScrollPane(this.sampleLabelJHelpImage, ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER),
            BorderLayout.CENTER);
      this.add(this.spinnerSize, BorderLayout.SOUTH);

      this.updateFont();

      this.spinnerSize.addChangeListener(this);

      this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
      this.pack();
   }

   /**
    * Update the font and the text draw
    */
   private void updateFont()
   {
      this.font = new JHelpFont("Arial", this.size);

      this.imageMain.startDrawMode();

      this.imageMain.clear(0xFFFFFFFF);

      final Pair<List<JHelpTextLine>, Dimension> lines = this.font.computeTextLines(JHelpFontSampleFrame.STRING, JHelpTextAlign.LEFT,
            JHelpFontSampleFrame.WIDTH, JHelpFontSampleFrame.HEIGHT);

      for(final JHelpTextLine line : lines.element1)
      {
         this.imageMain.paintMask(line.getX(), line.getY(), line.getMask(), 0xFF000000, 0xFFFFFFFF, false);
      }

      this.imageMain.endDrawMode();
   }

   /**
    * Called when user change the value of the size spinner <br>
    * <br>
    * <b>Parent documentation:</b><br>
    * {@inheritDoc}
    *
    * @param e
    *           Event description
    * @see javax.swing.event.ChangeListener#stateChanged(javax.swing.event.ChangeEvent)
    */
   @Override
   public void stateChanged(final ChangeEvent e)
   {
      this.size = (Integer) this.spinnerSize.getValue();

      this.updateFont();
   }
}