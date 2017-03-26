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

/**
 * {@link JSONMap} element.<br>
 * Public to be able to serialize it with {@link JSONWriter} or parse it with {@link JSONReader}
 * .<br>
 * It is useless to instantiate it outside the {@link JSONMap} context.<br>
 *
 * @param <KEY>   Key type
 * @param <VALUE> Value type
 */
@JSONObject
public class Node<KEY extends Comparable, VALUE>
{
    /**
     * Key
     */
    @JSONElement
    KEY              key;
    /**
     * Node for lower values
     */
    @JSONElement
    Node<KEY, VALUE> lower;
    /**
     * Node for upper values
     */
    @JSONElement
    Node<KEY, VALUE> upper;
    /**
     * Value
     */
    @JSONElement
    VALUE            value;

    /**
     * Create node
     */
    public Node()
    {
    }

    /**
     * Create node
     *
     * @param key   Key
     * @param value Value
     */
    Node(KEY key, VALUE value)
    {
        this.key = key;
        this.value = value;
    }
}
