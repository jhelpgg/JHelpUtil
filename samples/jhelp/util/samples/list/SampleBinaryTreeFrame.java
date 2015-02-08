package jhelp.util.samples.list;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Comparator;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import jhelp.util.gui.JHelpFont;
import jhelp.util.gui.JHelpImage;
import jhelp.util.gui.JHelpTextAlign;
import jhelp.util.gui.JHelpTextLine;
import jhelp.util.gui.UtilGUI;
import jhelp.util.gui.event.BinaryTreeModelListener;
import jhelp.util.gui.model.BinaryTreeModel;
import jhelp.util.gui.renderer.BinaryTreeModelElementRenderer;
import jhelp.util.list.Pair;
import jhelp.util.samples.common.gui.SampleLabelJHelpImageCenter;

public class SampleBinaryTreeFrame
      extends JFrame
{
   class EventManager
         implements ActionListener, Comparator<String>, BinaryTreeModelElementRenderer<String>, BinaryTreeModelListener<String>
   {
      private final JHelpFont font = new JHelpFont("Arial", 24);

      EventManager()
      {
      }

      @Override
      public void actionPerformed(final ActionEvent actionEvent)
      {
         SampleBinaryTreeFrame.this.doActionPerformed(actionEvent);
      }

      @Override
      public void binaryTreeModelChanged(final BinaryTreeModel<String> binaryTreeModel)
      {
         SampleBinaryTreeFrame.this.updateImage();
      }

      @Override
      public int compare(final String string1, final String string2)
      {
         final int comparison = string1.compareToIgnoreCase(string2);

         if(comparison != 0)
         {
            return comparison;
         }

         return string1.compareTo(string2);
      }

      @Override
      public JHelpImage obtainBinaryTreeModelElementImage(final BinaryTreeModel<String> binaryTreeModel, final String element)
      {
         final Pair<List<JHelpTextLine>, Dimension> textLines = this.font.computeTextLines(element, JHelpTextAlign.CENTER);
         final JHelpImage image = new JHelpImage(textLines.element2.width, textLines.element2.height, 0xFFFFFFFF);
         image.startDrawMode();

         for(final JHelpTextLine textLine : textLines.element1)
         {
            image.paintMask(textLine.getX(), textLine.getY(), textLine.getMask(), 0xFF0000FF, 0xFFFFFFFF, false);
         }

         image.endDrawMode();
         return image;
      }
   }

   private static final String               ACTION_ADD = "ADD";
   private final BinaryTreeModel<String>     binaryTreeModel;
   private final EventManager                eventManager;
   private final SampleLabelJHelpImageCenter sampleLabelJHelpImageCenter;
   private final JTextField                  textField;

   public SampleBinaryTreeFrame()
         throws HeadlessException
   {
      super("Sample binary tree");

      this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

      this.eventManager = new EventManager();
      this.binaryTreeModel = new BinaryTreeModel<String>(String.class, this.eventManager, this.eventManager, 0xFF000000, 0xFFFFFFFF);
      this.binaryTreeModel.registerBinaryTreeModelListener(this.eventManager);

      this.sampleLabelJHelpImageCenter = new SampleLabelJHelpImageCenter();
      this.textField = new JTextField(128);
      this.textField.setActionCommand(SampleBinaryTreeFrame.ACTION_ADD);
      this.textField.addActionListener(this.eventManager);
      final JButton button = new JButton(SampleBinaryTreeFrame.ACTION_ADD);
      button.setActionCommand(SampleBinaryTreeFrame.ACTION_ADD);
      button.addActionListener(this.eventManager);

      this.setLayout(new BorderLayout());
      this.add(new JScrollPane(this.sampleLabelJHelpImageCenter), BorderLayout.CENTER);

      final JPanel panel = new JPanel(new BorderLayout());
      panel.add(this.textField, BorderLayout.CENTER);
      panel.add(button, BorderLayout.EAST);

      this.add(panel, BorderLayout.SOUTH);

      UtilGUI.takeAllScreen(this);
   }

   void doActionPerformed(final ActionEvent actionEvent)
   {
      final String action = actionEvent.getActionCommand();

      if(SampleBinaryTreeFrame.ACTION_ADD.equals(action) == true)
      {
         final String text = this.textField.getText().trim();
         this.textField.setText("");
         this.binaryTreeModel.putElement(text);
         return;
      }
   }

   void updateImage()
   {
      this.sampleLabelJHelpImageCenter.setJHelpImage(this.binaryTreeModel.obtainImageTree());
   }
}