package jhelp.util.list;

import java.util.Iterator;
import java.util.TreeSet;

/**
 * Hash table where keys are integers
 * 
 * @author JHelp
 * @param <TYPE>
 *           Element stored type
 */
public class HashInt<TYPE>
      implements Iterable<TYPE>
{
   /**
    * Inside element
    * 
    * @author JHelp
    * @param <DATA>
    *           Dtat type
    */
   static class Element<DATA>
         implements Comparable<Element<DATA>>
   {
      /** Stored data */
      DATA      data;
      /** Element's hash */
      final int hash;

      /**
       * Create a new instance of Element
       * 
       * @param hash
       *           Element's hash
       */
      Element(final int hash)
      {
         this.hash = hash;
      }

      /**
       * Create a new instance of Element
       * 
       * @param hash
       *           Element's hash
       * @param data
       *           Stored data
       */
      Element(final int hash, final DATA data)
      {
         this.hash = hash;
         this.data = data;
      }

      /**
       * Compre with an other element.<br>
       * It returns:
       * <table border=0>
       * <tr>
       * <th>&lt;0</th>
       * <td>:</td>
       * <td>If this element is before given one</td>
       * </tr>
       * <tr>
       * <th>0</th>
       * <td>:</td>
       * <td>If this element and given one have the same place</td>
       * </tr>
       * <tr>
       * <th>&gt;0</th>
       * <td>:</td>
       * <td>If this element is after given one</td>
       * </tr>
       * </table>
       * <br>
       * <br>
       * <b>Parent documentation:</b><br>
       * {@inheritDoc}
       * 
       * @param element
       *           Element to compare with
       * @return Comparison result
       * @see java.lang.Comparable#compareTo(java.lang.Object)
       */
      @Override
      public int compareTo(final Element<DATA> element)
      {
         return this.hash - element.hash;
      }

      /**
       * Indicates if an object is equals to this element <br>
       * <br>
       * <b>Parent documentation:</b><br>
       * {@inheritDoc}
       * 
       * @param object
       *           Object to compare with
       * @return {@code true} if the object is equals to the element
       * @see java.lang.Object#equals(java.lang.Object)
       */
      @Override
      public boolean equals(final Object object)
      {
         if(object == null)
         {
            return false;
         }

         if(object == this)
         {
            return true;
         }

         if((object instanceof Element) == false)
         {
            return false;
         }

         return this.hash == ((Element<?>) object).hash;
      }

      /**
       * Element hash code <br>
       * <br>
       * <b>Parent documentation:</b><br>
       * {@inheritDoc}
       * 
       * @return Element hash code
       * @see java.lang.Object#hashCode()
       */
      @Override
      public int hashCode()
      {
         return this.hash;
      }

      /**
       * Element string representation <br>
       * <br>
       * <b>Parent documentation:</b><br>
       * {@inheritDoc}
       * 
       * @return Element string representation
       * @see java.lang.Object#toString()
       */
      @Override
      public String toString()
      {
         return this.hash + ":" + this.data;
      }
   }

   /**
    * Iterator on hash table values
    * 
    * @author JHelp
    * @param <ELEMENT>
    *           Element type
    */
   static class IteratorElement<ELEMENT>
         implements Iterator<ELEMENT>
   {
      /** Iterator reference */
      private final Iterator<Element<ELEMENT>> iteratorBase;

      /**
       * Create a new instance of IteratorElement
       * 
       * @param iteratorBase
       *           Iterator reference
       */
      IteratorElement(final Iterator<Element<ELEMENT>> iteratorBase)
      {
         this.iteratorBase = iteratorBase;
      }

      /**
       * Indicates if their are on more element in the iterator <br>
       * <br>
       * <b>Parent documentation:</b><br>
       * {@inheritDoc}
       * 
       * @return {@code true} if their are one more element in the iterator
       * @see java.util.Iterator#hasNext()
       */
      @Override
      public boolean hasNext()
      {
         return this.iteratorBase.hasNext();
      }

      /**
       * Next element in iterator <br>
       * <br>
       * <b>Parent documentation:</b><br>
       * {@inheritDoc}
       * 
       * @return Next element in iterator
       * @see java.util.Iterator#next()
       */
      @Override
      public ELEMENT next()
      {
         return this.iteratorBase.next().data;
      }

      /**
       * Do nothing here <br>
       * <br>
       * <b>Parent documentation:</b><br>
       * {@inheritDoc}
       * 
       * @see java.util.Iterator#remove()
       */
      @Override
      public void remove()
      {
      }
   }

   /** Set where stored hash table elements */
   private final TreeSet<Element<TYPE>> treeSet;

   /**
    * Create a new instance of HashInt
    */
   public HashInt()
   {
      this.treeSet = new TreeSet<Element<TYPE>>();
   }

   /**
    * Clear the hash table
    */
   public void clear()
   {
      this.treeSet.clear();
   }

   /**
    * Indicates if a key exists in the table
    * 
    * @param key
    *           Tested key
    * @return {@code true} if the ket exists
    */
   public boolean contains(final int key)
   {
      return this.treeSet.contains(new Element<TYPE>(key));
   }

   /**
    * Obtain element associated to a key.<br>
    * It returns {@code null} if no element are associated to the key
    * 
    * @param key
    *           Ket for get the value
    * @return Associated value or {@code null} if the key not associated
    */
   public TYPE get(final int key)
   {
      final Element<TYPE> element = new Element<TYPE>(key);

      final Element<TYPE> found = this.treeSet.ceiling(element);

      if(found == null)
      {
         return null;
      }

      if(found.hash == key)
      {
         return found.data;
      }

      return null;
   }

   /**
    * Number of elements in the table
    * 
    * @return Number of elements in the table
    */
   public int getSize()
   {
      return this.treeSet.size();
   }

   /**
    * Indicates if table is empty
    * 
    * @return {@code true} if table is empty
    */
   public boolean isEmpty()
   {
      return this.treeSet.isEmpty();
   }

   /**
    * Iterator on elements <br>
    * <br>
    * <b>Parent documentation:</b><br>
    * {@inheritDoc}
    * 
    * @return Iterator on elements
    * @see java.lang.Iterable#iterator()
    */
   @Override
   public Iterator<TYPE> iterator()
   {
      return new IteratorElement<TYPE>(this.treeSet.iterator());
   }

   /**
    * Associate an elemnt to a key
    * 
    * @param key
    *           Key
    * @param value
    *           Value to associate
    */
   public void put(final int key, final TYPE value)
   {
      if(value == null)
      {
         throw new NullPointerException("value musn't be null");
      }

      final Element<TYPE> element = new Element<TYPE>(key, value);
      this.treeSet.remove(element);
      this.treeSet.add(element);
   }

   /**
    * Remove an element
    * 
    * @param key
    *           Key of element to remove
    */
   public void remove(final int key)
   {
      this.treeSet.remove(new Element<TYPE>(key));
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
      final StringBuilder stringBuilder = new StringBuilder(1024);

      stringBuilder.append('[');

      final Iterator<Element<TYPE>> iterator = this.treeSet.iterator();

      if(iterator.hasNext() == true)
      {
         Element<TYPE> element = iterator.next();
         stringBuilder.append(element.hash);
         stringBuilder.append("->");
         stringBuilder.append(element.data);

         while(iterator.hasNext() == true)
         {
            element = iterator.next();
            stringBuilder.append(" | ");
            stringBuilder.append(element.hash);
            stringBuilder.append("->");
            stringBuilder.append(element.data);
         }
      }

      stringBuilder.append(']');

      return stringBuilder.toString();
   }
}