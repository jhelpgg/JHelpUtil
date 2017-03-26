/**
 * <h1>License :</h1> <br>
 * The following code is deliver as is. I take care that code compile and work, but I am not responsible about any damage it may
 * cause.<br>
 * You can use, modify, the code as your need for any usage. But you can't do any action that avoid me or other person use,
 * modify this code. The code is free for usage and modification, you can't change that fact.<br>
 * <br>
 * 
 * @author JHelp
 */
package jhelp.util.gui;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Java file list selection in drag and drop operation between Swing GUI and Operating System <br>
 * <br>
 * Last modification : 9 aoet 2010<br>
 * Version 0.0.0<br>
 * 
 * @author JHelp
 */
public class JavaFileSelection
      implements Transferable
{
   /** Accepted flavors */
   public static final DataFlavor[] FLAVORS =
                                            {
                                               DataFlavor.javaFileListFlavor
                                            };

   /** File list carry */
   private final List<File>         listFile;

   /**
    * Constructs JavaFileSelection
    * 
    * @param files
    *           List of files
    */
   public JavaFileSelection(final File... files)
   {
      this.listFile = new ArrayList<File>();

      Collections.addAll(this.listFile, files);
   }

   /**
    * Constructs JavaFileSelection
    * 
    * @param listFile
    *           List file
    */
   public JavaFileSelection(final List<File> listFile)
   {
      this.listFile = listFile;
   }

   /**
    * Return the data associated to a flavor type
    * 
    * @param flavor
    *           Flavor type
    * @return List of files
    * @throws UnsupportedFlavorException
    *            If flavor is not {@link DataFlavor#javaFileListFlavor}
    * @throws IOException
    *            On IO issue
    * @see java.awt.datatransfer.Transferable#getTransferData(java.awt.datatransfer.DataFlavor)
    */
   @Override
   public Object getTransferData(final DataFlavor flavor) throws UnsupportedFlavorException, IOException
   {
      if(flavor.isFlavorJavaFileListType())
      {
         return this.listFile;
      }

      throw new UnsupportedFlavorException(flavor);
   }

   /**
    * Supported flavor list
    * 
    * @return Supported flavor list
    * @see java.awt.datatransfer.Transferable#getTransferDataFlavors()
    */
   @Override
   public DataFlavor[] getTransferDataFlavors()
   {
      return JavaFileSelection.FLAVORS;
   }

   /**
    * Indicates if flavor is supported
    * 
    * @param flavor
    *           Tested flavor
    * @return {@code true} if flavor supported
    * @see java.awt.datatransfer.Transferable#isDataFlavorSupported(java.awt.datatransfer.DataFlavor)
    */
   @Override
   public boolean isDataFlavorSupported(final DataFlavor flavor)
   {
      return flavor.isFlavorJavaFileListType();
   }
}