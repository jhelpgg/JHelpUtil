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

import java.awt.font.FontRenderContext;
import java.awt.geom.AffineTransform;

/**
 * Common constants for GUI
 * 
 * @author JHelp
 */
public interface ConstantsGUI
{
   /** Identity transform */
   public static final AffineTransform   AFFINE_TRANSFORM    = new AffineTransform();
   /** Flatness to use */
   public static final double            FLATNESS            = 0.01;
   /** Font render context */
   public static final FontRenderContext FONT_RENDER_CONTEXT = new FontRenderContext(ConstantsGUI.AFFINE_TRANSFORM, true, false);
}