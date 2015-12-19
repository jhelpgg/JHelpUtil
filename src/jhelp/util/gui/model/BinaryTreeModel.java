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
package jhelp.util.gui.model;

import java.awt.Dimension;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import jhelp.util.gui.JHelpImage;
import jhelp.util.gui.event.BinaryTreeModelListener;
import jhelp.util.gui.renderer.BinaryTreeModelElementRenderer;
import jhelp.util.list.BinaryTree;
import jhelp.util.list.Tree;

/**
 * Binary tree model for draw a binary tree
 * 
 * @author JHelp
 * @param <ELEMENT>
 *           Tree element type
 */
public class BinaryTreeModel<ELEMENT>
{
   /** Line size */
   private static final int                              LINE_SIZE = 16;
   /** Background color */
   private final int                                     background;
   /** Binary tree draw */
   private final BinaryTree<ELEMENT>                     binaryTree;
   /** Describes how draw tree elements */
   private final BinaryTreeModelElementRenderer<ELEMENT> binaryTreeModelElementRenderer;
   /** How sort elements in tree */
   private final Comparator<ELEMENT>                     comparator;
   /** Tree image */
   private JHelpImage                                    imageTree;
   /** Lines color */
   private final int                                     linesColor;
   /** Listeners of model events */
   private final List<BinaryTreeModelListener<ELEMENT>>  listeners;

   /**
    * Create a new instance of BinaryTreeModel
    * 
    * @param elementClass
    *           Element in tree class type
    * @param comparator
    *           Comparator to sort elements in tree
    * @param binaryTreeModelElementRenderer
    *           Describes how draw tree elements
    * @param linesColor
    *           Lines color
    * @param background
    *           Background color
    */
   public BinaryTreeModel(final Class<ELEMENT> elementClass, final Comparator<ELEMENT> comparator,
         final BinaryTreeModelElementRenderer<ELEMENT> binaryTreeModelElementRenderer, final int linesColor, final int background)
   {
      if(elementClass == null)
      {
         throw new NullPointerException("elementClass musn't be null");
      }

      if(comparator == null)
      {
         throw new NullPointerException("comparator musn't be null");
      }

      if(binaryTreeModelElementRenderer == null)
      {
         throw new NullPointerException("binaryTreeModelElementRenderer musn't be null");
      }

      this.comparator = comparator;
      this.binaryTree = new BinaryTree<ELEMENT>(elementClass, this.comparator);
      this.listeners = new ArrayList<BinaryTreeModelListener<ELEMENT>>();
      this.binaryTreeModelElementRenderer = binaryTreeModelElementRenderer;
      this.linesColor = linesColor;
      this.background = background;
   }

   /**
    * Compute size of a image tree (Tree contains pre-computed images)
    * 
    * @param tree
    *           Tree to measure
    * @return Tree size
    */
   private Dimension computeSize(final Tree<JHelpImage> tree)
   {
      final JHelpImage image = tree.getInformation();
      int width = image.getWidth();
      int height = image.getHeight();

      if(tree.numberOfBranch() == 0)
      {
         return new Dimension(width, height);
      }

      width += BinaryTreeModel.LINE_SIZE;

      Dimension subSize = this.computeSize(tree.getBranch(0));
      int subWidth = subSize.width;
      int subHeight = subSize.height;

      subSize = this.computeSize(tree.getBranch(1));
      subWidth = Math.max(subWidth, subSize.width);
      subHeight = Math.max(subHeight, subSize.height);

      subHeight = (subHeight << 1) + 3;
      width += subWidth;
      height = Math.max(height, subHeight);

      return new Dimension(width, height);
   }

   /**
    * Create tree image
    */
   private void createImage()
   {
      if(this.binaryTree.isEmpty() == true)
      {
         this.imageTree = JHelpImage.DUMMY;
         return;
      }

      // Create tree of images
      final BinaryTree<ELEMENT>.Branch<ELEMENT> branch = this.binaryTree.getTrunk();
      final Tree<JHelpImage> tree = new Tree<JHelpImage>(this.binaryTreeModelElementRenderer.obtainBinaryTreeModelElementImage(this, branch.getElement()));
      this.insertInTree(branch, tree);

      // Compute size
      final Dimension dimension = this.computeSize(tree);

      // Create image itself
      this.imageTree = new JHelpImage(dimension.width, dimension.height, this.background);
      this.imageTree.startDrawMode();
      this.drawTree(tree, 0, 0, dimension.height);
      this.imageTree.endDrawMode();
   }

   /**
    * Draw a tree branch inside image defined area
    * 
    * @param tree
    *           Tree branch to draw
    * @param x
    *           X coordinate where draw
    * @param yMin
    *           Y minimum
    * @param yMax
    *           Y maximum
    */
   private void drawTree(final Tree<JHelpImage> tree, int x, final int yMin, final int yMax)
   {
      final JHelpImage image = tree.getInformation();
      final int yMil = (yMin + yMax) >> 1;
      final int y = yMil - ((image.getHeight()) >> 1);
      this.imageTree.drawImage(x, y, image);

      if(tree.numberOfBranch() == 0)
      {
         return;
      }

      x += image.getWidth();
      final int xx = x + BinaryTreeModel.LINE_SIZE;
      this.imageTree.drawLine(x, yMil, xx, (yMin + yMil) >> 1, this.linesColor);
      this.imageTree.drawLine(x, yMil, xx, (yMax + yMil) >> 1, this.linesColor);

      this.drawTree(tree.getBranch(0), xx, yMin, yMil);
      this.drawTree(tree.getBranch(1), xx, yMil, yMax);
   }

   /**
    * Insert a binary branch inside a tree image branch
    * 
    * @param branch
    *           Binary branch to insert
    * @param tree
    *           Tree image where insert
    */
   private void insertInTree(final BinaryTree<ELEMENT>.Branch<ELEMENT> branch, final Tree<JHelpImage> tree)
   {
      BinaryTree<ELEMENT>.Branch<ELEMENT> subBranch = branch.getLeft();

      if(subBranch != null)
      {
         final Tree<JHelpImage> subTree = tree.addBranch(this.binaryTreeModelElementRenderer.obtainBinaryTreeModelElementImage(this, subBranch.getElement()));
         this.insertInTree(subBranch, subTree);
      }
      else
      {
         tree.addBranch(JHelpImage.DUMMY);
      }

      subBranch = branch.getRight();

      if(subBranch != null)
      {
         final Tree<JHelpImage> subTree = tree.addBranch(this.binaryTreeModelElementRenderer.obtainBinaryTreeModelElementImage(this, subBranch.getElement()));
         this.insertInTree(subBranch, subTree);
      }
      else
      {
         tree.addBranch(JHelpImage.DUMMY);
      }
   }

   /**
    * Signal to listeners that the model changed
    */
   protected void fireModelChanged()
   {
      synchronized(this.listeners)
      {
         for(final BinaryTreeModelListener<ELEMENT> listener : this.listeners)
         {
            listener.binaryTreeModelChanged(this);
         }
      }
   }

   /**
    * Compute tree image
    * 
    * @return Tree image
    */
   public JHelpImage obtainImageTree()
   {
      if(this.imageTree == null)
      {
         this.createImage();
      }

      return this.imageTree;
   }

   /**
    * Add a element to the tree
    * 
    * @param element
    *           Element to add
    */
   public void putElement(final ELEMENT element)
   {
      if(this.binaryTree.putElement(element) == true)
      {
         this.imageTree = null;
         this.fireModelChanged();
      }
   }

   /**
    * Register a listener of model changes
    * 
    * @param listener
    *           Listener to register
    */
   public void registerBinaryTreeModelListener(final BinaryTreeModelListener<ELEMENT> listener)
   {
      if(listener == null)
      {
         throw new NullPointerException("listener musn't be null");
      }

      synchronized(this.listeners)
      {
         if(this.listeners.contains(listener) == false)
         {
            this.listeners.add(listener);
         }
      }
   }

   /**
    * Remove an element
    * 
    * @param element
    *           Element to remove
    */
   public void removeElement(final ELEMENT element)
   {
      if(this.binaryTree.reomveElement(element) == true)
      {
         this.imageTree = null;
         this.fireModelChanged();
      }
   }

   /**
    * Unregister a listener to model changes
    * 
    * @param listener
    *           Listener to unregister
    */
   public void unregisterBinaryTreeModelListener(final BinaryTreeModelListener<ELEMENT> listener)
   {
      synchronized(this.listeners)
      {
         this.listeners.remove(listener);
      }
   }
}