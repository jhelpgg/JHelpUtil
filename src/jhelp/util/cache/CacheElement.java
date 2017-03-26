/**
 * <h1>License :</h1> <br>
 * The following code is deliver as is. I take care that code compile and work, but I am not responsible about any
 * damage it may
 * cause.<br>
 * You can use, modify, the code as your need for any usage. But you can't do any action that avoid me or other person use,
 * modify this code. The code is free for usage and modification, you can't change that fact.<br>
 * <br>
 *
 * @author JHelp
 */
package jhelp.util.cache;

import java.lang.ref.SoftReference;

/**
 * Element of a {@link Cache}<br>
 * It describes how create an element <br>
 * <br>
 * Last modification : 13 avr. 2010<br>
 * Version 0.0.0<br>
 *
 * @author JHelp
 * @param <ELEMENT>
 *           Element type
 */
public abstract class CacheElement<ELEMENT>
{
    /** Hide reference */
    private SoftReference<ELEMENT> softReference;

    /**
     * Constructs CacheElement
     */
    public CacheElement()
    {
    }

    /**
     * Remove the element
     */
    public final void clear()
    {
        if (this.softReference != null)
        {
            final ELEMENT element = this.softReference.get();

            if (element != null)
            {
                this.clearElement(element);
            }

            this.softReference.clear();
        }

        this.softReference = null;
    }

    /**
     * Called when element is cleared.<br>
     * Does nothing by default
     *
     * @param element
     *           Cleared value
     */
    protected void clearElement(final ELEMENT element)
    {
        //Does nothing by default
    }

    /**
     * Obtain the element
     *
     * @return The element
     */
    public final ELEMENT getElement()
    {
        ELEMENT element = null;

        // Check if element is already store
        if (this.softReference != null)
        {
            element = this.softReference.get();
        }

        if (element != null)
        {
            // If already store, return it
            return element;
        }

        // Create element and store it
        element = this.createElement();
        this.softReference = new SoftReference<ELEMENT>(element);

        return element;
    }

    /**
     * Create the element
     *
     * @return Created element
     */
    protected abstract ELEMENT createElement();
}