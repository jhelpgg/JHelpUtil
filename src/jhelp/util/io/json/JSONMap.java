/**
 * <h1>License :</h1> <br>
 * The following code is deliver as is. I take care that code compile and work, but I am not
 * responsible about any damage it may
 * cause.<br>
 * You can use, modify, the code as your need for any usage. But you can't do any action that
 * avoid me or other person use,
 * modify this code. The code is free for usage and modification, you can't change that fact.<br>
 * <br>
 *
 * @author JHelp
 */
package jhelp.util.io.json;

import com.sun.istack.internal.NotNull;

import java.util.Stack;

/**
 * Convenient map of element can be serialize with {@link JSONWriter} and parse by {@link JSONReader}.<br>
 * For serialization and parse works the given type of key and value must be one of primitive Object representation,
 * String, enum or
 * valid annotated {@link JSONObject}
 *
 * @param <KEY>   Map key type
 * @param <VALUE> Map value type
 */
@JSONObject
public class JSONMap<KEY extends Comparable, VALUE>
{
    /**
     * Map root
     */
    @JSONElement
    private Node<KEY, VALUE> root;
    /**
     * Map size
     */
    @JSONElement
    private int              size;

    /**
     * Create empty map
     */
    public JSONMap()
    {
        this.size = 0;
    }

    /**
     * Map size
     *
     * @return Map size
     */
    public int size()
    {
        return this.size;
    }

    /**
     * Indicates if map is empty
     *
     * @return {@code true} if map is empty
     */
    public boolean isEmpty()
    {
        return this.size == 0;
    }

    /**
     * Associate a key to a value
     *
     * @param key   Key reference
     * @param value Value associated
     */
    @SuppressWarnings("unchecked")
    public void put(
            @NotNull
                    KEY key, VALUE value)
    {
        if (key == null)
        {
            throw new NullPointerException("key MUST NOT be null !");
        }

        if (this.root == null)
        {
            this.size = 1;
            this.root = new Node<KEY, VALUE>(key, value);
            return;
        }

        Node<KEY, VALUE> node = this.root;
        int              comparison;

        while (true)
        {
            comparison = node.key.compareTo(key);

            if (comparison == 0)
            {
                node.value = value;
                return;
            }

            if (comparison < 0)
            {
                if (node.lower == null)
                {
                    this.size++;
                    node.lower = new Node<KEY, VALUE>(key, value);
                    return;
                }

                node = node.lower;
            }
            else
            {
                if (node.upper == null)
                {
                    this.size++;
                    node.upper = new Node<KEY, VALUE>(key, value);
                    return;
                }

                node = node.upper;
            }
        }
    }

    /**
     * Remove a value
     *
     * @param key Key to remove
     * @return Removed element OR {@code null} if nothing removed
     */
    @SuppressWarnings("unchecked")
    public VALUE remove(KEY key)
    {
        if (key == null)
        {
            return null;
        }

        Node<KEY, VALUE> parent  = null;
        Node<KEY, VALUE> node    = this.root;
        int              comparison;
        boolean          isLower = true;

        while (node != null)
        {
            comparison = node.key.compareTo(key);

            if (comparison == 0)
            {
                this.size--;
                VALUE value = node.value;

                if (parent == null)
                {
                    this.root = this.union(this.root.lower, this.root.upper);
                }
                else if (isLower)
                {
                    parent.lower = this.union(node.lower, node.upper);
                }
                else
                {
                    parent.upper = this.union(node.lower, node.upper);
                }

                return value;
            }

            parent = node;

            if (comparison < 0)
            {
                isLower = true;
                node = node.lower;
            }
            else
            {
                isLower = false;
                node = node.upper;
            }
        }

        return null;
    }

    /**
     * Create union of 2 nodes
     *
     * @param node1 First
     * @param node2 Second
     * @return Union
     */
    private Node<KEY, VALUE> union(Node<KEY, VALUE> node1, Node<KEY, VALUE> node2)
    {
        if (node1 == null)
        {
            return node2;
        }

        if (node2 == null)
        {
            return node1;
        }

        Node<KEY, VALUE>        union = new Node<>(node1.key, node1.value);
        Stack<Node<KEY, VALUE>> stack = new Stack<>();

        if (node1.lower != null)
        {
            stack.push(node1.lower);
        }

        if (node1.upper != null)
        {
            stack.push(node1.upper);
        }

        stack.push(node2);
        Node<KEY, VALUE> node;

        while (!stack.empty())
        {
            node = stack.pop();
            this.placeCopy(union, node);

            if (node.lower != null)
            {
                stack.push(node.lower);
            }

            if (node.upper != null)
            {
                stack.push(node.upper);
            }
        }

        return union;
    }

    /**
     * Place a copy of a node inside an other node
     *
     * @param parent  Node where add
     * @param toPlace Node to place
     */
    @SuppressWarnings("unchecked")
    private void placeCopy(Node<KEY, VALUE> parent, Node<KEY, VALUE> toPlace)
    {
        int comparison;

        while (true)
        {
            comparison = parent.key.compareTo(toPlace.key);

            if (comparison == 0)
            {
                parent.value = toPlace.value;
                return;
            }

            if (comparison < 0)
            {
                if (parent.lower == null)
                {
                    parent.lower = new Node<KEY, VALUE>(toPlace.key, toPlace.value);
                    return;
                }

                parent = parent.lower;
            }
            else
            {
                if (parent.upper == null)
                {
                    parent.upper = new Node<KEY, VALUE>(toPlace.key, toPlace.value);
                    return;
                }

                parent = parent.upper;
            }
        }
    }

    /**
     * String representation
     *
     * @return String representation
     */
    @Override
    public String toString()
    {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append('{');
        boolean first = true;

        for (KEY key : keys())
        {
            if (!first)
            {
                stringBuilder.append("; ");
            }

            first = false;
            stringBuilder.append(key);
            stringBuilder.append('=');
            stringBuilder.append(this.get(key));
        }

        stringBuilder.append('}');
        return stringBuilder.toString();
    }

    /**
     * Obtain a value
     *
     * @param key Value key
     * @return Desired value OR {@code null} if no key
     */
    @SuppressWarnings("unchecked")
    public VALUE get(
            @NotNull
                    KEY key)
    {
        if (key == null)
        {
            throw new NullPointerException("key MUST NOT be null !");
        }

        Node<KEY, VALUE> node = this.root;
        int              comparison;

        while (node != null)
        {
            comparison = node.key.compareTo(key);

            if (comparison == 0)
            {
                return node.value;
            }

            if (comparison < 0)
            {
                node = node.lower;
            }
            else
            {
                node = node.upper;
            }
        }

        return null;
    }

    /**
     * Keys list
     *
     * @return Keys list
     */
    public JSONList<KEY> keys()
    {
        JSONList<KEY> keys = new JSONList<KEY>();

        Stack<Node<KEY, VALUE>> stack = new Stack<>();

        if (this.root != null)
        {
            stack.push(this.root);
        }

        Node<KEY, VALUE> node;

        while (!stack.empty())
        {
            node = stack.pop();
            keys.add(node.key);

            if (node.lower != null)
            {
                stack.push(node.lower);
            }

            if (node.upper != null)
            {
                stack.push(node.upper);
            }
        }

        return keys;
    }
}