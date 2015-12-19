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

/**
 * Paint for fill a shape.<br>
 * When the paint is used, first its called {@link #initializePaint(int, int)} to give to the paint information about size of
 * the bounding box of the shape it will be filled. Then for each pixel inside the shape it will call
 * {@link #obtainColor(int, int)} to know the color to use for a specific point inside the shape. Coordinate are relative to the
 * shape, in other words (0, 0) is the upper left corner of the bonding box of the shape
 * 
 * @author JHelp
 */
public interface JHelpPaint
{
   /**
    * Initialize the shape.<br>
    * It is called just before fill shape with this paint.
    * 
    * @param width
    *           Shape bounding box width
    * @param height
    *           Shape bounding box height
    */
   public void initializePaint(int width, int height);

   /**
    * Compute color to use for a specific point.<br>
    * Coordinate are relative to the bounding box upper left corner
    * 
    * @param x
    *           X of the pixel coordinate
    * @param y
    *           Y of the pixel coordinate
    * @return Computed color
    */
   public int obtainColor(int x, int y);
}