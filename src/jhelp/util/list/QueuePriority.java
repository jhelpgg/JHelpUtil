/**
 * Project : JHelpUtil<br>
 * Package : jhelp.util.list<br>
 * Class : QueuePriority<br>
 * Date : 8 mai 2010<br>
 * By JHelp
 */
package jhelp.util.list;

import java.util.Comparator;
import java.util.PriorityQueue;

import jhelp.util.text.UtilText;

/**
 * Queue with priority<br>
 * You decide the priority just at the moment of add, the information of priority not link to the element instance, so
 * you can
 * add the same instance two times with a different priority<br>
 * You needn't that element is {@link Comparable}<br>
 * <br>
 * Last modification : 8 mai 2010<br>
 * Version 0.0.0<br>
 *
 * @param <T> Elements type
 * @author JHelp
 */
public class QueuePriority<T>
{
    /**
     * Priority queue
     */
    private final PriorityQueue<Element<T>> queue;

    /**
     * Constructs QueuePriority
     *
     * @param increment Indicates if priority are take in increment order
     */
    public QueuePriority(final boolean increment)
    {
        this.queue = new PriorityQueue<Element<T>>(123, new ComparatorElement<T>(increment));
    }

    /**
     * Dequeue an element from queue
     *
     * @return Element dequeued
     */
    public T dequeue()
    {
        final Element<T> element = this.queue.poll();
        if (element == null)
        {
            return null;
        }

        return element.element;
    }

    /**
     * Enqueue an element
     *
     * @param priority Priority
     * @param element  Element
     */
    public void enqueue(final int priority, final T element)
    {
        if (element == null)
        {
            throw new NullPointerException("element MUST NOT be null");
        }

        this.queue.add(new Element<T>(priority, element));
    }

    /**
     * Indicates if queue is empty
     *
     * @return {@code true} if queue is empty
     */
    public boolean isEmpty()
    {
        return this.queue.isEmpty();
    }

    /**
     * Queue size
     *
     * @return Queue size
     */
    public int size()
    {
        return this.queue.size();
    }

    /**
     * Comparator of {@link QueuePriority.Element} <br>
     * <br>
     * Last modification : 19 juin 2010<br>
     * Version 0.0.0<br>
     *
     * @param <T> Element type
     * @author JHelp
     */
    static class ComparatorElement<T>
            implements Comparator<Element<T>>
    {
        /**
         * Increment order multiplier
         */
        final int increment;

        /**
         * Constructs ComparatorElement
         *
         * @param increment Indicates if priority are take in increment order
         */
        ComparatorElement(final boolean increment)
        {
            if (increment)
            {
                this.increment = 1;
            }
            else
            {
                this.increment = -1;
            }
        }

        /**
         * Compare two elements
         *
         * @param o1 First element
         * @param o2 Second element
         * @return Compare result
         * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
         */
        @Override
        public int compare(final Element<T> o1, final Element<T> o2)
        {
            return this.increment * (o1.priority - o2.priority);
        }
    }

    /**
     * Element of the queue <br>
     * <br>
     * Last modification : 19 juin 2010<br>
     * Version 0.0.0<br>
     *
     * @param <T> Element type
     * @author JHelp
     */
    static class Element<T>
    {
        /**
         * Element itself
         */
        final T   element;
        /**
         * Priority link to the element
         */
        final int priority;

        /**
         * Constructs Element
         *
         * @param priority Priority link to the element
         * @param element  Element itself
         */
        Element(final int priority, final T element)
        {
            this.priority = priority;
            this.element = element;
        }

        /**
         * String representation
         *
         * @return String representation
         * @see java.lang.Object#toString()
         */
        @Override
        public String toString()
        {
            return UtilText.concatenate('[', this.priority, "] : ", this.element);
        }
    }

    /**
     * String representation
     *
     * @return String representation
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString()
    {
        return this.queue.toString();
    }
}