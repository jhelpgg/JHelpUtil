package jhelp.util.gui.event;

import jhelp.util.gui.model.BinaryTreeModel;

/**
 * Listener of binary tree model changes
 * 
 * @author JHelp
 * @param <ELEMENT>
 *           Element type
 */
public interface BinaryTreeModelListener<ELEMENT>
{
   /**
    * Called when binary tree model changed
    * 
    * @param binaryTreeModel
    *           Binary tree model changed
    */
   public void binaryTreeModelChanged(BinaryTreeModel<ELEMENT> binaryTreeModel);
}