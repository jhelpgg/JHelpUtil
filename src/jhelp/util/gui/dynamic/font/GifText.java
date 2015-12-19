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