package jhelp.util.samples.font.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.ScrollPaneConstants;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import jhelp.util.gui.JHelpFont;
import jhelp.util.gui.JHelpImage;
import jhelp.util.gui.JHelpTextAlign;
import jhelp.util.gui.JHelpTextLine;
import jhelp.util.list.Pair;
import jhelp.util.samples.common.gui.SampleLabelJHelpImage;

public class JHelpFontSampleFrame
      extends JFrame
      implements ChangeListener
{
   private final static int            HEIGHT = 2000;
   private final static String         STRING = "Pseudopseudohypoparathyroidism is a long word. So Pseudopseudohypoparathyroidism is a good candidate for show how cut text work";
   private final static int            WIDTH  = 500;
   private JHelpFont                   font;
   private final JHelpImage            imageMain;
   private final SampleLabelJHelpImage sampleLabelJHelpImage;
   private int                         size;
   private final JSpinner              spinnerSize;

   public JHelpFontSampleFrame()
   {
      super("JHelpFont sample");

      this.size = 20;
      this.spinnerSize = new JSpinner(new SpinnerNumberModel(this.size, 10, 400, 1));
      this.imageMain = new JHelpImage(JHelpFontSampleFrame.WIDTH, JHelpFontSampleFrame.HEIGHT, 0xFFFFFFFF);
      this.sampleLabelJHelpImage = new SampleLabelJHelpImage(this.imageMain);

      this.setLayout(new BorderLayout());

      this.add(new JScrollPane(this.sampleLabelJHelpImage, ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER), BorderLayout.CENTER);
      this.add(this.spinnerSize, BorderLayout.SOUTH);

      this.updateFont();

      this.spinnerSize.addChangeListener(this);

      this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      this.pack();
   }

   private void updateFont()
   {
      this.font = new JHelpFont("Arial", this.size);

      this.imageMain.startDrawMode();

      this.imageMain.clear(0xFFFFFFFF);

      final Pair<List<JHelpTextLine>, Dimension> lines = this.font.computeTextLines(JHelpFontSampleFrame.STRING, JHelpTextAlign.LEFT, JHelpFontSampleFrame.WIDTH, JHelpFontSampleFrame.HEIGHT);

      for(final JHelpTextLine line : lines.element1)
      {
         this.imageMain.paintMask(line.getX(), line.getY(), line.getMask(), 0xFF000000, 0xFFFFFFFF, false);
      }

      this.imageMain.endDrawMode();
   }

   @Override
   public void stateChanged(final ChangeEvent e)
   {
      this.size = (Integer) this.spinnerSize.getValue();

      this.updateFont();
   }
}