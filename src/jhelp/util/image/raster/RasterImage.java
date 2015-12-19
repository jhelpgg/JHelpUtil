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
package jhelp.util.image.raster;

import jhelp.util.gui.JHelpImage;
import jhelp.util.list.SizedObject;

/**
 * Raster image
 * 
 * @author JHelp
 */
public interface RasterImage
      extends SizedObject
{
   /**
    * Clear the image
    */
   public void clear();

   /**
    * Image type
    * 
    * @return Image type
    */
   public RasterImageType getImageType();

   /**
    * Convert image to JHelp image
    * 
    * @return Converted image
    */
   public JHelpImage toJHelpImage();
}