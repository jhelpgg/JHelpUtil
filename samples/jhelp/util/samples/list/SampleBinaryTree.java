package jhelp.util.samples.list;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import jhelp.util.debug.Debug;
import jhelp.util.debug.DebugLevel;
import jhelp.util.list.BinaryTree;
import jhelp.util.list.Tree.TestFoundListener;
import jhelp.util.math.UtilMath;

public class SampleBinaryTree
      implements Comparator<Integer>, TestFoundListener<Integer>
{

   /**
    * @param args
    */
   public static void main(final String[] args)
   {
      final SampleBinaryTree sampleBinaryTree = new SampleBinaryTree();
      final BinaryTree<Integer> binaryTree = new BinaryTree<Integer>(Integer.class, sampleBinaryTree);
      Debug.println(DebugLevel.INFORMATION, binaryTree);
      Debug.println(DebugLevel.INFORMATION, "<   --- *  ---   >");
      binaryTree.putElement(1);
      Debug.println(DebugLevel.INFORMATION, binaryTree);
      Debug.println(DebugLevel.INFORMATION, "<   --- *  ---   >");
      binaryTree.putElement(2);
      Debug.println(DebugLevel.INFORMATION, binaryTree);
      Debug.println(DebugLevel.INFORMATION, "<   --- *  ---   >");
      binaryTree.putElement(3);
      Debug.println(DebugLevel.INFORMATION, binaryTree);
      Debug.println(DebugLevel.INFORMATION, "<   --- *  ---   >");
      binaryTree.putElement(4);
      Debug.println(DebugLevel.INFORMATION, binaryTree);
      Debug.println(DebugLevel.INFORMATION, "<   --- *  ---   >");
      binaryTree.putElement(5);
      Debug.println(DebugLevel.INFORMATION, binaryTree);
      Debug.println(DebugLevel.INFORMATION, "<   --- *  ---   >");
      binaryTree.putElement(6);
      Debug.println(DebugLevel.INFORMATION, binaryTree);
      Debug.println(DebugLevel.INFORMATION, "<   --- *  ---   >");
      binaryTree.putElement(7);
      Debug.println(DebugLevel.INFORMATION, binaryTree);
      Debug.println(DebugLevel.INFORMATION, "<   --- *  ---   >");
      binaryTree.putElement(8);
      Debug.println(DebugLevel.INFORMATION, binaryTree);
      Debug.println(DebugLevel.INFORMATION, "<   --- *  ---   >");
      Debug.println(DebugLevel.INFORMATION, binaryTree.searchLeftToRightDepth(sampleBinaryTree));
      Debug.println(DebugLevel.INFORMATION, binaryTree.searchLeftToRightHigh(sampleBinaryTree));
      Debug.println(DebugLevel.INFORMATION, binaryTree.searchRightToLeftDepth(sampleBinaryTree));
      Debug.println(DebugLevel.INFORMATION, binaryTree.searchRightToLeftHigh(sampleBinaryTree));
      List<Integer> list = new ArrayList<Integer>();
      binaryTree.collectLeftToRightDepth(sampleBinaryTree, list);
      Debug.println(DebugLevel.INFORMATION, list);
      list = new ArrayList<Integer>();
      binaryTree.collectLeftToRightHigh(sampleBinaryTree, list);
      Debug.println(DebugLevel.INFORMATION, list);
      list = new ArrayList<Integer>();
      binaryTree.collectRightToLeftDepth(sampleBinaryTree, list);
      Debug.println(DebugLevel.INFORMATION, list);
      list = new ArrayList<Integer>();
      binaryTree.collectRightToLeftHigh(sampleBinaryTree, list);
      Debug.println(DebugLevel.INFORMATION, list);

      final SampleBinaryTreeFrame sampleBinaryTreeFrame = new SampleBinaryTreeFrame();
      sampleBinaryTreeFrame.setVisible(true);
   }

   @Override
   public int compare(final Integer integer1, final Integer integer2)
   {
      return integer1 - integer2;
   }

   @Override
   public boolean isElementSearched(final Integer integer)
   {
      final int log2 = UtilMath.log2(integer);
      return (1 << log2) != integer;// (integer % 2) == 1;// true;
   }
}