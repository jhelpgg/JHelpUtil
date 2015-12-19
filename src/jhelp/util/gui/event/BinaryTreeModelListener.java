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