package jhelp.util.samples.image.colorMap;

import java.awt.BorderLayout;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import javax.swing.JFrame;
import javax.swing.WindowConstants;

import jhelp.util.debug.Debug;
import jhelp.util.gui.JHelpImage;
import jhelp.util.gui.UtilGUI;
import jhelp.util.image.colorMap.BinaryImage;
import jhelp.util.io.UtilIO;
import jhelp.util.samples.common.gui.SampleLabelJHelpImage;
import jhelp.util.time.TimeDebug;

/**
 * Binary image test
 *
 * @author JHelp <br>
 */
public class BinaryImageTest
{
   /**
    * Launch the binary image test
    *
    * @param args
    *           Unused
    */
   public static void main(final String[] args)
   {
      try
      {
         final TimeDebug timeDebug = new TimeDebug("BinaryImageTest");
         InputStream inputStream = BinaryImageTest.class.getResourceAsStream("flames.png");
         JHelpImage image = JHelpImage.loadImage(inputStream);
         inputStream.close();
         timeDebug.add("Image read");
         BinaryImage binaryImage = new BinaryImage(image);
         timeDebug.add("To binary");
         image = binaryImage.toJHelpImage(0xFF000000, 0xFFFFFFFF);
         timeDebug.add("To image");
         SampleLabelJHelpImage labelJHelpImage = new SampleLabelJHelpImage(image);
         JFrame frame = new JFrame("BinaryImage");
         frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
         frame.setLayout(new BorderLayout());
         frame.add(labelJHelpImage, BorderLayout.CENTER);
         frame.pack();
         timeDebug.add("Frame 1 created");
         UtilGUI.centerOnScreen(frame);
         frame.setVisible(true);
         timeDebug.add("Frame 1 show");

         //

         final File file = UtilIO.createTemporaryFile("image");
         UtilIO.createFile(file);
         final OutputStream outputStream = new FileOutputStream(file);
         timeDebug.add("Create File for write");
         binaryImage.write(outputStream);
         outputStream.close();
         timeDebug.add("saved");

         //

         inputStream = new FileInputStream(file);
         timeDebug.add("Create File for read");
         binaryImage = BinaryImage.read(inputStream);
         inputStream.close();
         timeDebug.add("loaded");

         //

         UtilIO.delete(file);
         timeDebug.add("temporary file deleted");

         //

         image = binaryImage.toJHelpImage(0xFFFF0000, 0xFF0000FF);
         timeDebug.add("To image 2");
         labelJHelpImage = new SampleLabelJHelpImage(image);
         frame = new JFrame("BinaryImage 2");
         frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
         frame.setLayout(new BorderLayout());
         frame.add(labelJHelpImage, BorderLayout.CENTER);
         frame.pack();
         timeDebug.add("Frame 2 created");
         UtilGUI.centerOnScreen(frame);
         frame.setVisible(true);
         timeDebug.add("Frame 2 show");

         //

         timeDebug.dump();
      }
      catch(final Exception exception)
      {
         Debug.printException(exception, "Failed to launch the test !");
      }
   }
}