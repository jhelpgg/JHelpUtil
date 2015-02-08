package jhelp.util.samples.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.List;

import javax.swing.JFrame;

import jhelp.util.gui.JHelpImage;
import jhelp.util.gui.JHelpTextAlign;
import jhelp.util.gui.JHelpTextLine;
import jhelp.util.gui.UtilGUI;
import jhelp.util.gui.transformation.Transformation;
import jhelp.util.list.Pair;
import jhelp.util.math.oldSystem.MayaNumber;
import jhelp.util.samples.common.gui.SampleLabelJHelpImage;

public class SampleTransformation
{

   /**
    * @param args
    */
   public static void main(final String[] args)
   {
      final Pair<List<JHelpTextLine>, Dimension> text = MayaNumber.FONT_NUMBERS.computeTextLines("Transformation\nSinusoidale", JHelpTextAlign.CENTER);
      final JHelpImage imageText = new JHelpImage(text.element2.width, text.element2.height, 0x44440000);
      imageText.startDrawMode();

      for(final JHelpTextLine textLine : text.element1)
      {
         imageText.paintMask(textLine.getX(), textLine.getY(), textLine.getMask(), 0xFF000000, 0, true);
      }

      imageText.endDrawMode();

      final Transformation transformation = new Transformation(text.element2.width, text.element2.height);
      transformation.toVerticalSin(1, 5);
      transformation.combineHorizontalSin(2, 5);

      final JHelpImage image = new JHelpImage(1024, 512, 0xFFFFFFFF);
      image.startDrawMode();
      image.drawImage((1024 - imageText.getWidth()) >> 1, (512 - imageText.getHeight()) >> 1, imageText, transformation, true);
      image.endDrawMode();

      final JFrame frame = new JFrame();
      frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      frame.setLayout(new BorderLayout());
      frame.add(new SampleLabelJHelpImage(image), BorderLayout.CENTER);
      UtilGUI.packedSize(frame);
      UtilGUI.centerOnScreen(frame);
      frame.setVisible(true);
   }
}