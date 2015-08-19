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