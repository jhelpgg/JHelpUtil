package jhelp.util.gui.renderer;

import jhelp.util.gui.JHelpImage;
import jhelp.util.gui.model.BinaryTreeModel;

/**
 * Renderer of elements in binary tree model.<br>
 * It describes how draw a binary tree element
 * 
 * @author JHelp
 * @param <ELEMENT>
 *           Elements in model type
 */
public interface BinaryTreeModelElementRenderer<ELEMENT>
{
   /**
    * Create image for a specific tree element
    * 
    * @param binaryTreeModel
    *           Model where element from
    * @param element
    *           Element to draw
    * @return Image representation
    */
   public JHelpImage obtainBinaryTreeModelElementImage(BinaryTreeModel<ELEMENT> binaryTreeModel, ELEMENT element);
}