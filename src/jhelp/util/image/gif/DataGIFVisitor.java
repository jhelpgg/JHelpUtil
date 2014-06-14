package jhelp.util.image.gif;

import jhelp.util.gui.JHelpImage;

/**
 * Visitor of {@link DataGIF} to collect images in {@link DataGIF#collectImages(DataGIFVisitor)}
 * 
 * @author JHelp
 */
public interface DataGIFVisitor
{
   /**
    * Called when collecting is finished
    */
   public void endCollecting();

   /**
    * Called when next image is computed
    * 
    * @param duration
    *           Image duration in milliseconds
    * @param image
    *           Image computed
    */
   public void nextImage(long duration, JHelpImage image);

   /**
    * Called when collecting starts
    * 
    * @param width
    *           Images width
    * @param height
    *           Images height
    */
   public void startCollecting(int width, int height);
}