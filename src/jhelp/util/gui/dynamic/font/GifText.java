package jhelp.util.gui.dynamic.font;

import java.util.Collections;
import java.util.List;

/**
 * Text create with GIF images
 * 
 * @author JHelp
 */
public class GifText
{
   /** List of gifs and their position */
   private final List<GifPosition> gifPositions;
   /** Total width */
   private final int               height;
   /** Total height */
   private final int               width;

   /**
    * Create a new instance of GifText
    * 
    * @param width
    *           Total width
    * @param height
    *           Total height
    * @param gifPositions
    *           List of gifs and their position
    */
   public GifText(final int width, final int height, final List<GifPosition> gifPositions)
   {
      this.width = width;
      this.height = height;
      this.gifPositions = gifPositions;
   }

   /**
    * List of gifs and their position
    * 
    * @return List of gifs and their position
    */
   public List<GifPosition> getGifPositions()
   {
      return Collections.unmodifiableList(this.gifPositions);
   }

   /**
    * Total height
    * 
    * @return Total height
    */
   public int getHeight()
   {
      return this.height;
   }

   /**
    * Total width
    * 
    * @return Total width
    */
   public int getWidth()
   {
      return this.width;
   }
}