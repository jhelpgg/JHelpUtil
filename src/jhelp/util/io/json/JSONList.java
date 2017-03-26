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

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Convenient list of element can be serialize with {@link JSONWriter} and parse by {@link JSONReader}.<br>
 * For serialization and parse works the given type must be one of primitive Object representation, String, enum or
 * valid annotated {@link JSONObject}
 *
 * @param <TYPE> Elements type
 */
@JSONObject
public class JSONList<TYPE> implements Iterable<TYPE>
{
    /**
     * List size
     */
    @JSONElement
    private int           size;
    /**
     * Start of the list
     */
    @JSONElement
    private Element<TYPE> head;

    /**
     * Create an empty list
     */
    public JSONList()
    {
        this.size = 0;
        this.head = null;
    }

    /**
     * Indicates if list is empty
     *
     * @return {@code true} if list is empty
     */
    public boolean isEmpty()
    {
        return this.size == 0;
    }

    /**
     * List size
     *
     * @return List size
     */
    public int size()
    {
        return this.size;
    }

    /**
     * Add element at the end of the list
     *
     * @param element Element to add
     */
    public void add(TYPE element)
    {
        this.add(element, this.size);
    }

    /**
     * Insert element at given index
     *
     * @param element Element to insert
     * @param index   Index where insert
     */
    public void add(TYPE element, int index)
    {
        this.size++;
        Element<TYPE> elt = new Element<TYPE>();
        elt.element = element;

        if (this.head == null)
        {
            this.head = elt;
            return;
        }

        Element<TYPE> previous = null;
        Element<TYPE> current  = this.head;

        while (index > 0 && current.next != null)
        {
            index--;
            previous = current;
            current = current.next;
        }

        if (index > 0)
        {
            Element<TYPE> next = current.next;
            current.next = elt;
            elt.next = next;
            return;
        }

        if (previous == null)
        {
            this.head = elt;
            this.head.next = current;
            return;
        }

        previous.next = elt;
        previous.next.next = current;
    }

    /**
     * Remove an element of the list
     *
     * @param element Element to remove
     * @return {@code true} if element removed
     */
    public boolean remove(TYPE element)
    {
        int index = this.indexOf(element);

        if (index < 0)
        {
            return false;
        }

        this.remove(index);
        return true;
    }

    /**
     * Remove an element at given index
     *
     * @param index Index to remove
     * @return Value of removed element
     * @throws IllegalArgumentException If given index not valid
     */
    public TYPE remove(int index)
    {
        this.checkIndex(index);
        Element<TYPE> element = this.head;
        this.size--;

        if (index == 0)
        {
            TYPE value = element.element;
            this.head = this.head.next;
            return value;
        }

        for (; index > 1; index--)
        {
            element = element.next;
        }

        TYPE value = element.next.element;
        element.next = element.next.next;
        return value;
    }

    /**
     * Check if an index is inside the list
     *
     * @param index Index to check
     * @throws IllegalArgumentException If given index not valid
     */
    private void checkIndex(int index)
    {
        if (index < 0 || index >= this.size)
        {
            throw new IllegalArgumentException(
                    "index MUST be in [0, " + this.size + "[ not " + index);
        }
    }

    /**
     * Index of element
     *
     * @param element Element to have index
     * @return Element index OR -1 if element not inside the list
     */
    public int indexOf(TYPE element)
    {
        int           index = 0;
        Element<TYPE> elt   = this.head;

        while (elt != null)
        {
            if ((element == null && elt.element == null)
                    || (element != null && element.equals(elt.element)))
            {
                return index;
            }

            elt = elt.next;
            index++;
        }

        return -1;
    }

    /**
     * Obtain an element
     *
     * @param index Element index
     * @return Desired element
     * @throws IllegalArgumentException If given index not valid
     */
    public TYPE get(int index)
    {
        this.checkIndex(index);
        return this.obtainElement(index).element;
    }

    /**
     * Obtain element at given index (It suppose index valid)
     *
     * @param index Element index
     * @return Desired element
     */
    private Element<TYPE> obtainElement(int index)
    {
        Element<TYPE> element = this.head;

        for (; index > 0; index--)
        {
            element = element.next;
        }

        return element;
    }

    /**
     * Returns an iterator over elements of type {@code TYPE}.
     *
     * @return an Iterator.
     */
    @Override
    public Iterator<TYPE> iterator()
    {
        return new ElementIterator(this.head);
    }

    /**
     * String representation for debug purpose
     *
     * @return String representation for debug purpose
     */
    @Override
    public String toString()
    {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append('[');
        Element<TYPE> element = this.head;

        while (element != null)
        {
            stringBuilder.append(element.element);
            element = element.next;

            if (element != null)
            {
                stringBuilder.append(", ");
            }
        }

        stringBuilder.append(']');
        return stringBuilder.toString();
    }

    /**
     * Iterator over
     */
    class ElementIterator implements Iterator<TYPE>
    {
        /**
         * Current element
         */
        private Element<TYPE> current;

        /**
         * Create iterator
         *
         * @param start First element
         */
        ElementIterator(Element<TYPE> start)
        {
            this.current = start;
        }

        /**
         * Returns {@code true} if the iteration has more elements.
         * (In other words, returns {@code true} if {@link #next} would
         * return an element rather than throwing an exception.)
         *
         * @return {@code true} if the iteration has more elements
         */
        @Override
        public boolean hasNext()
        {
            return this.current != null;
        }

        /**
         * Returns the next element in the iteration.
         *
         * @return the next element in the iteration
         * @throws NoSuchElementException if the iteration has no more elements
         */
        @Override
        public TYPE next()
        {
            if (this.current == null)
            {
                throw new NoSuchElementException("No more elements !");
            }

            TYPE element = this.current.element;
            this.current = this.current.next;
            return element;
        }
    }
}
