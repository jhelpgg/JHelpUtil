package jhelp.util.samples.math.maya;

import java.awt.BorderLayout;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import jhelp.util.debug.Debug;
import jhelp.util.gui.UtilGUI;
import jhelp.util.math.oldSystem.MayaNumber;
import jhelp.util.math.oldSystem.TribalNumber;

public class SampleTribalFrame
      extends JFrame
      implements ActionListener
{
   private final JLabel     label;
   private final JTextField textField;

   public SampleTribalFrame()
         throws HeadlessException
   {
      super("Tribal");

      this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

      this.label = new JLabel("Type a number ...", SwingConstants.CENTER);
      this.label.setFont(MayaNumber.FONT_NUMBERS.getFont());
      this.textField = new JTextField(64);
      this.textField.setFont(MayaNumber.FONT_NUMBERS.getFont());
      this.textField.addActionListener(this);

      this.setLayout(new BorderLayout());
      this.add(this.label, BorderLayout.NORTH);
      this.add(this.textField, BorderLayout.SOUTH);

      final TribalNumber tribalNumber = TribalNumber.parse(TribalNumber.GROUP_625, TribalNumber.GROUP_125, TribalNumber.GROUP_25, TribalNumber.DIGIT_5, TribalNumber.DIGIT_4, TribalNumber.DIGIT_3, TribalNumber.DIGIT_2,
            TribalNumber.DIGIT_1, TribalNumber.DIGIT_0);
      this.label.setText(tribalNumber.toString());
      this.textField.setText(String.valueOf(tribalNumber.getNumber()));

      UtilGUI.packedSize(this);
      UtilGUI.centerOnScreen(this);
   }

   @Override
   public void actionPerformed(final ActionEvent actionEvent)
   {
      final String text = this.textField.getText().trim();
      this.textField.setText("");

      try
      {
         final TribalNumber tribalNumber = new TribalNumber(Long.parseLong(text));
         this.label.setText(tribalNumber.toString());
      }
      catch(final Exception exception)
      {
         Debug.printException(exception, "Bad number : ", text);
         this.label.setText("ERROR : Bad number : " + text);
      }
   }
}