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
import javax.swing.WindowConstants;

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

/**
 * Frame that show a binary tree
 *
 * @author JHelp <br>
 */
@SuppressWarnings("deprecation")
public class SampleBinaryTreeFrame
      extends JFrame
{
   /**
    * UI events manager
    *
    * @author JHelp <br>
    */
   class EventManager
         implements ActionListener, Comparator<String>, BinaryTreeModelElementRenderer<String>, BinaryTreeModelListener<String>
   {
      /** Font use for print text in tree */
      private final JHelpFont font = new JHelpFont("Arial", 24);

      /**
       * Create a new instance of EventManager
       */
      EventManager()
      {
      }

      /**
       * Called when "Add" button is pressed or enter in text field <br>
       * <br>
       * <b>Parent documentation:</b><br>
       * {@inheritDoc}
       *
       * @param actionEvent
       *           Event description
       * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
       */
      @Override
      public void actionPerformed(final ActionEvent actionEvent)
      {
         SampleBinaryTreeFrame.this.doActionPerformed(actionEvent);
      }

      /**
       * Called when binary tree model changed <br>
       * <br>
       * <b>Parent documentation:</b><br>
       * {@inheritDoc}
       *
       * @param binaryTreeModel
       *           Binary tree model
       * @see jhelp.util.gui.event.BinaryTreeModelListener#binaryTreeModelChanged(jhelp.util.gui.model.BinaryTreeModel)
       */
      @Override
      public void binaryTreeModelChanged(final BinaryTreeModel<String> binaryTreeModel)
      {
         SampleBinaryTreeFrame.this.updateImage();
      }

      /**
       * Compare two strings <br>
       * <br>
       * <b>Parent documentation:</b><br>
       * {@inheritDoc}
       *
       * @param string1
       *           First
       * @param string2
       *           Second
       * @return Comparison result
       * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
       */
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

      /**
       * Compute image to use for one tree element <br>
       * <br>
       * <b>Parent documentation:</b><br>
       * {@inheritDoc}
       *
       * @param binaryTreeModel
       *           Binary tree model
       * @param element
       *           Element to render
       * @return Computed image
       * @see jhelp.util.gui.renderer.BinaryTreeModelElementRenderer#obtainBinaryTreeModelElementImage(jhelp.util.gui.model.BinaryTreeModel,
       *      java.lang.Object)
       */
      @SuppressWarnings("deprecation")
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

   /** Add button text and action */
   private static final String               ACTION_ADD = "ADD";
   /** Binary tree model */
   private final BinaryTreeModel<String>     binaryTreeModel;
   /** UI events manager */
   private final EventManager                eventManager;
   /** Label where tree is draw */
   private final SampleLabelJHelpImageCenter sampleLabelJHelpImageCenter;
   /** Edit text for user input */
   private final JTextField                  textField;

   /**
    * Create a new instance of SampleBinaryTreeFrame
    *
    * @throws HeadlessException
    *            On creation issue
    */
   public SampleBinaryTreeFrame()
         throws HeadlessException
   {
      super("Sample binary tree");

      this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

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

   /**
    * Called when button press or enter in edit text field
    *
    * @param actionEvent
    *           Event description
    */
   void doActionPerformed(final ActionEvent actionEvent)
   {
      final String action = actionEvent.getActionCommand();

      if(SampleBinaryTreeFrame.ACTION_ADD.equals(action))
      {
         final String text = this.textField.getText().trim();
         this.textField.setText("");
         this.binaryTreeModel.putElement(text);
      }
   }

   /**
    * Update tree image
    */
   void updateImage()
   {
      this.sampleLabelJHelpImageCenter.setJHelpImage(this.binaryTreeModel.obtainImageTree());
   }
}