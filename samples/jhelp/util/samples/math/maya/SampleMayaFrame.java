package jhelp.util.samples.math.maya;

import java.awt.BorderLayout;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;

import jhelp.util.debug.Debug;
import jhelp.util.gui.UtilGUI;
import jhelp.util.math.oldSystem.MayaNumber;
import jhelp.util.samples.common.gui.SampleLabelJHelpImageCenter;

/**
 * Frame that show Maya numbers sample
 *
 * @author JHelp <br>
 */
public class SampleMayaFrame
      extends JFrame
      implements ActionListener
{
   /** Label that show Maya number in simple version */
   private final JLabel                      label;
   /** Image that show Maya number in totem version */
   private final SampleLabelJHelpImageCenter sampleLabelJHelpImageCenter;
   /** Text field for type decimal number */
   private final JTextField                  textField;

   /**
    * Create a new instance of SampleMayaFrame
    *
    * @throws HeadlessException
    *            On creation issue
    */
   public SampleMayaFrame()
         throws HeadlessException
   {
      super("Maya");

      this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

      this.label = new JLabel("", SwingConstants.CENTER);
      this.label.setFont(MayaNumber.FONT_MAYA.getFont());
      this.sampleLabelJHelpImageCenter = new SampleLabelJHelpImageCenter();
      this.textField = new JTextField(128);
      this.textField.setFont(MayaNumber.FONT_NUMBERS.getFont());
      this.textField.addActionListener(this);

      this.setLayout(new BorderLayout());
      this.add(this.label, BorderLayout.NORTH);
      this.add(new JScrollPane(this.sampleLabelJHelpImageCenter), BorderLayout.CENTER);
      this.add(this.textField, BorderLayout.SOUTH);

      UtilGUI.takeAllScreen(this);
   }

   /**
    * Called when user validate the number <br>
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
      final String text = this.textField.getText().trim();
      this.textField.setText("");

      try
      {
         final MayaNumber mayaNumber = new MayaNumber(Long.parseLong(text));
         this.sampleLabelJHelpImageCenter.setJHelpImage(mayaNumber.toTotemImage(128, true));
         this.label.setText(mayaNumber.toString());
      }
      catch(final Exception exception)
      {
         Debug.printException(exception, "Bad number : ", text);
         this.label.setText("ERROR : Bad number : " + text);
      }
   }
}