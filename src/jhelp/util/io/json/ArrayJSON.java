package jhelp.util.io.json;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import jhelp.util.HashCode;
import jhelp.util.list.Pair;

/**
 * JSON array.<br>
 * It is a list of values
 * 
 * @author JHelp
 */
public class ArrayJSON
      implements Iterable<ValueJSON>
{
   /**
    * Parse a part of String to JSON array and next index to read.<br>
    * It returns {@code null} if String part not a JSON array
    * 
    * @param string
    *           String to parse
    * @param first
    *           First index for start parse
    * @param last
    *           Last index for end parse
    * @return JSON array and next index OR {@code null} if String part not a JSON array
    */
   static Pair<ArrayJSON, Integer> parse(final String string, int first, int last)
   {
      first = string.indexOf('[', first);
      last = string.lastIndexOf(']', last);

      if((first < 0) || (last < 0) || (first >= last))
      {
         return null;
      }

      final ArrayJSON arrayJSON = new ArrayJSON();

      while((first >= 0) && (first < last))
      {
         final Pair<ValueJSON, Integer> pair = ValueJSON.parse(string, first + 1, last);

         if(pair == null)
         {
            break;
         }

         arrayJSON.addValue(pair.element1);
         first = string.indexOf(',', pair.element2);
      }

      return new Pair<ArrayJSON, Integer>(arrayJSON, last);
   }

   /** List of values */
   private final List<ValueJSON> values;

   /**
    * Create a new instance of ArrayJSON
    */
   public ArrayJSON()
   {
      this.values = new ArrayList<ValueJSON>();
   }

   /**
    * Add value in the array
    * 
    * @param value
    *           Value to add
    */
   public void addValue(final ValueJSON value)
   {
      if(value == null)
      {
         throw new NullPointerException("value musn't be null");
      }

      this.values.add(value);
   }

   /***
    * Clear the array
    */
   public void clear()
   {
      this.values.clear();
   }

   /**
    * Indicates if an Object is equals to this array <br>
    * <br>
    * <b>Parent documentation:</b><br>
    * {@inheritDoc}
    * 
    * @param object
    *           Object to compare with
    * @return {@code true} if equals
    * @see java.lang.Object#equals(java.lang.Object)
    */
   @Override
   public boolean equals(final Object object)
   {
      if(this == object)
      {
         return true;
      }

      if(null == object)
      {
         return false;
      }

      if((object instanceof ArrayJSON) == false)
      {
         return false;
      }

      final ArrayJSON array = (ArrayJSON) object;

      final int length = this.values.size();

      if(length != array.values.size())
      {
         return false;
      }

      for(int i = 0; i < length; i++)
      {
         if(this.values.get(i).equals(array.values.get(i)) == false)
         {
            return false;
         }
      }

      return true;
   }

   /**
    * Obtain a JSON value
    * 
    * @param index
    *           Value index
    * @return The value
    */
   public ValueJSON getValue(final int index)
   {
      return this.values.get(index);
   }

   /**
    * Array hash code <br>
    * <br>
    * <b>Parent documentation:</b><br>
    * {@inheritDoc}
    * 
    * @return Array hash code
    * @see java.lang.Object#hashCode()
    */
   @Override
   public int hashCode()
   {
      return HashCode.computeHashCode(this.values);
   }

   /**
    * Obtain a value index.<br>
    * It returns -1 if value not inside the array
    * 
    * @param valueJSON
    *           Value searched
    * @return Value index OR -1 if value not inside the array
    */
   public int indexOf(final ValueJSON valueJSON)
   {
      return this.values.indexOf(valueJSON);
   }

   /**
    * Insert a value inside the array
    * 
    * @param value
    *           Value to insert
    * @param index
    *           Insert index
    */
   public void insertValue(final ValueJSON value, final int index)
   {
      if(value == null)
      {
         throw new NullPointerException("value musn't be null");
      }

      this.values.add(index, value);
   }

   /**
    * Iterator on values <br>
    * <br>
    * <b>Parent documentation:</b><br>
    * {@inheritDoc}
    * 
    * @return Values iterator
    * @see java.lang.Iterable#iterator()
    */
   @Override
   public Iterator<ValueJSON> iterator()
   {
      return this.values.iterator();
   }

   /**
    * Number of value
    * 
    * @return Number of value
    */
   public int numberOfValue()
   {
      return this.values.size();
   }

   /**
    * Remove a value from array
    * 
    * @param index
    *           Value index to remove
    */
   public void removeValue(final int index)
   {
      this.values.remove(index);
   }

   /**
    * Remove a value from array
    * 
    * @param value
    *           Value to remove
    */
   public void removeValue(final ValueJSON value)
   {
      this.values.remove(value);
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
      final StringBuilder stringBuilder = new StringBuilder();

      stringBuilder.append('[');

      final int lenght = this.values.size();

      if(lenght > 0)
      {
         stringBuilder.append(this.values.get(0));

         for(int i = 1; i < lenght; i++)
         {
            stringBuilder.append(" , ");
            stringBuilder.append(this.values.get(i));
         }
      }

      stringBuilder.append(']');

      return stringBuilder.toString();
   }
}