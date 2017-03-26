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
package jhelp.util.image.ani;

import jhelp.util.gui.JHelpImage;
import jhelp.util.image.cur.CurImage;
import jhelp.util.image.ico.IcoImage;
import jhelp.util.image.raster.RasterImage;

/**
 * Ani image information
 * 
 * @author JHelp
 */
public class AniImageInformation
{
   /** Cur image embed */
   private final CurImage curImage;
   /** Ico image embed */
   private final IcoImage icoImage;
   /** Raster image embed */
   private RasterImage    rasterImage;

   /**
    * Create a new instance of AniImageInformation based on cur image
    * 
    * @param curImage
    *           Cur image
    */
   public AniImageInformation(final CurImage curImage)
   {
      this.icoImage = null;
      this.curImage = curImage;
      this.rasterImage = null;
   }

   /**
    * Create a new instance of AniImageInformation based on ico image
    * 
    * @param icoImage
    *           Ico image
    */
   public AniImageInformation(final IcoImage icoImage)
   {
      this.icoImage = icoImage;
      this.curImage = null;
      this.rasterImage = null;
   }

   /**
    * Create a new instance of AniImageInformation based on raster image
    * 
    * @param rasterImage
    *           Raster image
    */
   public AniImageInformation(final RasterImage rasterImage)
   {
      this.icoImage = null;
      this.curImage = null;
      this.rasterImage = rasterImage;
   }

   /**
    * Draw ani element on image
    * 
    * @param parent
    *           Image where draw
    * @param x
    *           X
    * @param y
    *           Y
    */
   public void draw(final JHelpImage parent, final int x, final int y)
   {
      if(this.icoImage != null)
      {
         this.icoImage.getElement(0).draw(parent, x, y);
      }
      else if(this.curImage != null)
      {
         this.curImage.getElement(0).draw(parent, x, y);
      }
      else
      {
         this.rasterImage = this.rasterImage.toJHelpImage();
         parent.drawImage(x, y, (JHelpImage) this.rasterImage);
      }
   }
}