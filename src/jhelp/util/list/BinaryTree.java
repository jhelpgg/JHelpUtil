package jhelp.util.list;

import java.util.Comparator;
import java.util.List;
import java.util.Stack;

import jhelp.util.list.Tree.TestFoundListener;

/**
 * A binary tree
 *
 * @param <ELEMENT> Element carry type
 * @author JHelp
 */
public class BinaryTree<ELEMENT>
{
    /**
     * Tree content
     */
    private final SortedArray<ELEMENT> content;
    /**
     * Main branch
     */
    private final Branch<ELEMENT>      trunk;
    /**
     * Indicates if tree is updated
     */
    private       boolean              isUpdated;
    /**
     * Create a new instance of BinaryTree
     *
     * @param elementClass Element carry class type
     * @param comparator   Comparator used to know how sort the tree
     */
    public BinaryTree(final Class<ELEMENT> elementClass, final Comparator<ELEMENT> comparator)
    {
        if (comparator == null)
        {
            throw new NullPointerException("comparator MUST NOT be null");
        }

        this.trunk = new Branch<ELEMENT>();
        this.content = new SortedArray<ELEMENT>(elementClass, comparator, true);
        this.isUpdated = false;
        this.updateTree();
    }

    /**
     * Fill a string builder with a branch description
     *
     * @param branch        Branch to add
     * @param stringBuilder String builder to fill
     * @param number        Number of branch level
     */
    private void fill(final Branch<ELEMENT> branch, final StringBuilder stringBuilder, final int number)
    {
        stringBuilder.append('\n');

        for (int i = 0; i < number; i++)
        {
            stringBuilder.append("| ");
        }

        stringBuilder.append(branch.element);

        if (branch.left != null)
        {
            this.fill(branch.left, stringBuilder, number + 1);
        }

        if (branch.right != null)
        {
            this.fill(branch.right, stringBuilder, number + 1);
        }
    }

    /**
     * Fill a tree with a branch
     *
     * @param branch Branch to put in the tree
     * @param tree   Tree to fill
     */
    private void fillTree(final Branch<ELEMENT> branch, final Tree<ELEMENT> tree)
    {
        if (branch.left != null)
        {
            this.fillTree(branch.left, tree.addBranch(branch.left.element));
        }

        if (branch.right != null)
        {
            this.fillTree(branch.right, tree.addBranch(branch.right.element));
        }
    }

    /**
     * Insert content interval inside a branch
     *
     * @param branch Branch where insert interval
     * @param start  Interval start index
     * @param end    Interval end index
     */
    private void insert(final Branch<ELEMENT> branch, final int start, final int end)
    {
        final int midle = (start + end) >> 1;
        branch.element = this.content.getElement(midle);

        if (midle > start)
        {
            branch.left = new Branch<ELEMENT>();
            this.insert(branch.left, start, midle - 1);
        }

        if (midle < end)
        {
            branch.right = new Branch<ELEMENT>();
            this.insert(branch.right, midle + 1, end);
        }
    }

    /**
     * Update the tree
     */
    private void updateTree()
    {
        if (this.isUpdated)
        {
            return;
        }

        this.trunk.element = null;
        this.trunk.left = null;
        this.trunk.right = null;
        final int size = this.content.getSize();

        if (size > 0)
        {
            this.insert(this.trunk, 0, size - 1);
        }

        this.isUpdated = true;
    }

    /**
     * Clear the tree
     */
    public void clear()
    {
        this.trunk.element = null;
        this.trunk.left = null;
        this.trunk.right = null;
        this.content.clear();
        this.isUpdated = true;
    }

    /**
     * Collect elements of the tree left to right, deep first
     *
     * @param testFoundListener Filter to use to know if one element is inside searched ones
     * @param list              List to fill with founded elements
     */
    public void collectLeftToRightDepth(final TestFoundListener<ELEMENT> testFoundListener, final List<ELEMENT> list)
    {
        if (testFoundListener == null)
        {
            throw new NullPointerException("testFoundListener MUST NOT be null");
        }

        if (list == null)
        {
            throw new NullPointerException("list MUST NOT be null");
        }

        this.updateTree();

        final Stack<Branch<ELEMENT>> stack = new Stack<Branch<ELEMENT>>();
        stack.push(this.trunk);
        Branch<ELEMENT> branch;

        while (!stack.isEmpty())
        {
            branch = stack.pop();

            if (testFoundListener.isElementSearched(branch.element))
            {
                list.add(branch.element);
            }

            if (branch.right != null)
            {
                stack.push(branch.right);
            }

            if (branch.left != null)
            {
                stack.push(branch.left);
            }
        }
    }

    /**
     * Collect elements of the tree left to right, high first
     *
     * @param testFoundListener Filter to use to know if one element is inside searched ones
     * @param list              List to fill with founded elements
     */
    public void collectLeftToRightHigh(final TestFoundListener<ELEMENT> testFoundListener, final List<ELEMENT> list)
    {
        if (testFoundListener == null)
        {
            throw new NullPointerException("testFoundListener MUST NOT be null");
        }

        if (list == null)
        {
            throw new NullPointerException("list MUST NOT be null");
        }

        this.updateTree();

        final Queue<Branch<ELEMENT>> queue = new Queue<Branch<ELEMENT>>();
        queue.inQueue(this.trunk);
        Branch<ELEMENT> branch;

        while (!queue.isEmpty())
        {
            branch = queue.outQueue();

            if (testFoundListener.isElementSearched(branch.element))
            {
                list.add(branch.element);
            }

            if (branch.left != null)
            {
                queue.inQueue(branch.left);
            }

            if (branch.right != null)
            {
                queue.inQueue(branch.right);
            }
        }
    }

    /**
     * Collect elements of the tree right to left, deep first
     *
     * @param testFoundListener Filter to use to know if one element is inside searched ones
     * @param list              List to fill with founded elements
     */
    public void collectRightToLeftDepth(final TestFoundListener<ELEMENT> testFoundListener, final List<ELEMENT> list)
    {
        if (testFoundListener == null)
        {
            throw new NullPointerException("testFoundListener MUST NOT be null");
        }

        if (list == null)
        {
            throw new NullPointerException("list MUST NOT be null");
        }

        this.updateTree();

        final Stack<Branch<ELEMENT>> stack = new Stack<Branch<ELEMENT>>();
        stack.push(this.trunk);
        Branch<ELEMENT> branch;

        while (!stack.isEmpty())
        {
            branch = stack.pop();

            if (testFoundListener.isElementSearched(branch.element))
            {
                list.add(branch.element);
            }

            if (branch.left != null)
            {
                stack.push(branch.left);
            }

            if (branch.right != null)
            {
                stack.push(branch.right);
            }
        }
    }

    /**
     * Collect elements of the tree right to left, high first
     *
     * @param testFoundListener Filter to use to know if one element is inside searched ones
     * @param list              List to fill with founded elements
     */
    public void collectRightToLeftHigh(final TestFoundListener<ELEMENT> testFoundListener, final List<ELEMENT> list)
    {
        if (testFoundListener == null)
        {
            throw new NullPointerException("testFoundListener MUST NOT be null");
        }

        if (list == null)
        {
            throw new NullPointerException("list MUST NOT be null");
        }

        this.updateTree();

        final Queue<Branch<ELEMENT>> queue = new Queue<Branch<ELEMENT>>();
        queue.inQueue(this.trunk);
        Branch<ELEMENT> branch;

        while (!queue.isEmpty())
        {
            branch = queue.outQueue();

            if (testFoundListener.isElementSearched(branch.element))
            {
                list.add(branch.element);
            }

            if (branch.right != null)
            {
                queue.inQueue(branch.right);
            }

            if (branch.left != null)
            {
                queue.inQueue(branch.left);
            }
        }
    }

    /**
     * Obtain the tree trunk
     *
     * @return Tree trunk
     */
    public Branch<ELEMENT> getTrunk()
    {
        this.updateTree();
        return this.trunk;
    }

    /**
     * Indicates if tree is empty
     *
     * @return {@code true} if tree is empty
     */
    public boolean isEmpty()
    {
        this.updateTree();
        return this.trunk.element == null;
    }

    /**
     * Add element inside the tree.<br>
     * Only different element can be added
     *
     * @param element Element to add
     * @return {@code true} if the element is added. {@code false} if element is considers equals (With the given
     * comparator at
     * constructor) and not added
     */
    public boolean putElement(final ELEMENT element)
    {
        if (element == null)
        {
            throw new NullPointerException("element MUST NOT be null");
        }

        if (this.content.add(element))
        {
            this.isUpdated = false;
            return true;
        }

        return false;
    }

    /**
     * Remove an element considers equals (With the given comparator at constructor) to given one
     *
     * @param element Element to remove
     * @return {@code true} if element removed
     */
    public boolean reomveElement(final ELEMENT element)
    {
        if (element == null)
        {
            return false;
        }

        if (this.content.remove(element) != null)
        {
            this.isUpdated = false;
            return true;
        }

        return false;
    }

    /**
     * Search first element of the tree left to right, deep first that corresponds to defined test
     *
     * @param testFoundListener Filter to use to know if one element is the search one
     * @return Found element or {@code null} if not found
     */
    public ELEMENT searchLeftToRightDepth(final TestFoundListener<ELEMENT> testFoundListener)
    {
        if (testFoundListener == null)
        {
            throw new NullPointerException("testFoundListener MUST NOT be null");
        }

        this.updateTree();

        final Stack<Branch<ELEMENT>> stack = new Stack<Branch<ELEMENT>>();
        stack.push(this.trunk);
        Branch<ELEMENT> branch;

        while (!stack.isEmpty())
        {
            branch = stack.pop();

            if (testFoundListener.isElementSearched(branch.element))
            {
                return branch.element;
            }

            if (branch.right != null)
            {
                stack.push(branch.right);
            }

            if (branch.left != null)
            {
                stack.push(branch.left);
            }
        }

        return null;
    }

    /**
     * Search first element of the tree left to right, deep high that corresponds to defined test
     *
     * @param testFoundListener Filter to use to know if one element is the search one
     * @return Found element or {@code null} if not found
     */
    public ELEMENT searchLeftToRightHigh(final TestFoundListener<ELEMENT> testFoundListener)
    {
        if (testFoundListener == null)
        {
            throw new NullPointerException("testFoundListener MUST NOT be null");
        }

        this.updateTree();

        final Queue<Branch<ELEMENT>> queue = new Queue<Branch<ELEMENT>>();
        queue.inQueue(this.trunk);
        Branch<ELEMENT> branch;

        while (!queue.isEmpty())
        {
            branch = queue.outQueue();

            if (testFoundListener.isElementSearched(branch.element))
            {
                return branch.element;
            }

            if (branch.left != null)
            {
                queue.inQueue(branch.left);
            }

            if (branch.right != null)
            {
                queue.inQueue(branch.right);
            }
        }

        return null;
    }

    /**
     * Search first element of the tree right to left, deep first that corresponds to defined test
     *
     * @param testFoundListener Filter to use to know if one element is the search one
     * @return Found element or {@code null} if not found
     */
    public ELEMENT searchRightToLeftDepth(final TestFoundListener<ELEMENT> testFoundListener)
    {
        if (testFoundListener == null)
        {
            throw new NullPointerException("testFoundListener MUST NOT be null");
        }

        this.updateTree();

        final Stack<Branch<ELEMENT>> stack = new Stack<Branch<ELEMENT>>();
        stack.push(this.trunk);
        Branch<ELEMENT> branch;

        while (!stack.isEmpty())
        {
            branch = stack.pop();

            if (testFoundListener.isElementSearched(branch.element))
            {
                return branch.element;
            }

            if (branch.left != null)
            {
                stack.push(branch.left);
            }

            if (branch.right != null)
            {
                stack.push(branch.right);
            }
        }

        return null;
    }

    /**
     * Search first element of the tree right to left, high first that corresponds to defined test
     *
     * @param testFoundListener Filter to use to know if one element is the search one
     * @return Found element or {@code null} if not found
     */
    public ELEMENT searchRightToLeftHigh(final TestFoundListener<ELEMENT> testFoundListener)
    {
        if (testFoundListener == null)
        {
            throw new NullPointerException("testFoundListener MUST NOT be null");
        }

        this.updateTree();

        final Queue<Branch<ELEMENT>> queue = new Queue<Branch<ELEMENT>>();
        queue.inQueue(this.trunk);
        Branch<ELEMENT> branch;

        while (!queue.isEmpty())
        {
            branch = queue.outQueue();

            if (testFoundListener.isElementSearched(branch.element))
            {
                return branch.element;
            }

            if (branch.right != null)
            {
                queue.inQueue(branch.right);
            }

            if (branch.left != null)
            {
                queue.inQueue(branch.left);
            }
        }

        return null;
    }

    /**
     * Convert binary tree to generic tree
     *
     * @return Generic tree
     */
    public Tree<ELEMENT> toTree()
    {
        this.updateTree();

        if (this.isEmpty())
        {
            return null;
        }

        final Tree<ELEMENT> tree = new Tree<ELEMENT>(this.trunk.element);
        this.fillTree(this.trunk, tree);
        return tree;
    }

    /**
     * A tree branch
     *
     * @param <TYPE> Element carry type
     * @author JHelp
     */
    public class Branch<TYPE>
    {
        /**
         * Element carry
         */
        TYPE         element;
        /**
         * Left branch
         */
        Branch<TYPE> left;
        /**
         * Parent branch
         */
        Branch<TYPE> parent;
        /**
         * Right branch
         */
        Branch<TYPE> right;

        /**
         * Create a new instance of Branch
         */
        Branch()
        {
        }

        /**
         * Element carry
         *
         * @return Element carry
         */
        public TYPE getElement()
        {
            return this.element;
        }

        /**
         * Left branch
         *
         * @return Left branch
         */
        public Branch<TYPE> getLeft()
        {
            return this.left;
        }

        /**
         * Branch parent.<br>
         * {@code null} if the branch is the root
         *
         * @return Parent branch or {@code null} if it is the root
         */
        public Branch<TYPE> getParent()
        {
            return this.parent;
        }

        /**
         * Right branch
         *
         * @return Right branch
         */
        public Branch<TYPE> getRight()
        {
            return this.right;
        }

        /**
         * String representation <br>
         * <br>
         * <b>Parent documentation:</b><br>
         * {@inheritDoc}
         *
         * @return String representation
         * @see java.lang.Object#toString()
         */
        @Override
        public String toString()
        {
            return this.element + " [ " + this.left + " | " + this.right + " ]";
        }
    }    /**
     * String representation <br>
     * <br>
     * <b>Parent documentation:</b><br>
     * {@inheritDoc}
     *
     * @return String representation
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString()
    {
        this.updateTree();

        if (this.trunk.element == null)
        {
            return "BinaryTree <EMPTY>";
        }

        final StringBuilder stringBuilder = new StringBuilder("BinaryTree :");
        this.fill(this.trunk, stringBuilder, 0);

        return stringBuilder.toString();
    }


}