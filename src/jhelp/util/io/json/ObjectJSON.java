package jhelp.util.io.json;

import java.util.HashMap;
import java.util.Set;

import jhelp.util.HashCode;
import jhelp.util.list.Pair;

/**
 * JSON object.<br>
 * It contains couples of key value
 * 
 * @author JHelp
 */
public class ObjectJSON
{
   /**
    * Parse String part to get JSON object and the next index to read the rest of the String.<br>
    * It returns {@code null} if the String part is not a JSON object
    * 
    * @param string
    *           String to parse
    * @param first
    *           First index to start parsing
    * @param last
    *           Last index to stop parsing
    * @return JSON object parsed and next index to read String OR {@code null} if String part is not a JSON object
    */
   static Pair<ObjectJSON, Integer> parse(final String string, int first, int last)
   {
      first = string.indexOf('{', first);
      last = string.lastIndexOf('}', last);

      if((first < 0) || (last < 0) || (first >= last))
      {
         return null;
      }

      final ObjectJSON objectJSON = new ObjectJSON();

      while((first >= 0) && (first < last))
      {
         first = string.indexOf('"', first);

         if(first < 0)
         {
            break;
         }

         int end = string.indexOf('"', first + 1);

         while((end > 0) && (string.charAt(end - 1) == '\\'))
         {
            end = string.indexOf('"', end + 1);
         }

         if(end < 0)
         {
            break;
         }

         final String name = string.substring(first + 1, end);
         first = string.indexOf(':', end + 1);

         if(first < 0)
         {
            break;
         }

         final Pair<ValueJSON, Integer> pair = ValueJSON.parse(string, first + 1, last);

         if(pair == null)
         {
            break;
         }

         objectJSON.put(name, pair.element1);
         first = string.indexOf(',', pair.element2);
      }

      return new Pair<ObjectJSON, Integer>(objectJSON, last);
   }

   /**
    * Parse a String to JSON object.<br>
    * It returns {@code null} if String not a JSON object
    * 
    * @param string
    *           String to parse
    * @return JSON object parsed OR {@code null} if String not a JSON object
    */
   public static ObjectJSON parse(final String string)
   {
      final Pair<ObjectJSON, Integer> pair = ObjectJSON.parse(string, 0, string.length() - 1);

      if(pair == null)
      {
         return null;
      }

      return pair.element1;
   }

   /** Couples of key, value */
   private final HashMap<String, ValueJSON> values;

   /**
    * Create a new instance of ObjectJSON
    */
   public ObjectJSON()
   {
      this.values = new HashMap<String, ValueJSON>();
   }

   /**
    * Indicates if an Object is equals to this JSON object <br>
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

      if((object instanceof ObjectJSON) == false)
      {
         return false;
      }

      final ObjectJSON objectJSON = (ObjectJSON) object;

      if(this.values.size() != objectJSON.values.size())
      {
         return false;
      }

      for(final String key : this.values.keySet())
      {
         if(this.values.get(key).equals(objectJSON.values.get(key)) == false)
         {
            return false;
         }
      }

      return true;
   }

   /**
    * Obtain a value.<br>
    * It returns {@code null} if key not defined
    * 
    * @param key
    *           Key
    * @return Value or {@code null} if key not defined
    */
   public ValueJSON get(final String key)
   {
      return this.values.get(key);
   }

   /**
    * Set of keys
    * 
    * @return Set of keys
    */
   public Set<String> getKeys()
   {
      return this.values.keySet();
   }

   /**
    * Object hash code <br>
    * <br>
    * <b>Parent documentation:</b><br>
    * {@inheritDoc}
    * 
    * @return Object hash code
    * @see java.lang.Object#hashCode()
    */
   @Override
   public int hashCode()
   {
      return HashCode.computeHashCode(this.values);
   }

   /**
    * Associate a key and a value
    * 
    * @param key
    *           Key
    * @param valueJSON
    *           Value
    */
   public void put(final String key, final ValueJSON valueJSON)
   {
      if(key == null)
      {
         throw new NullPointerException("key musn't be null");
      }

      if(valueJSON == null)
      {
         throw new NullPointerException("valueJSON musn't be null");
      }

      this.values.put(key, valueJSON);
   }

   /**
    * String representation.<br>
    * Can be used on server communication <br>
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

      stringBuilder.append('{');

      boolean first = true;

      for(final String key : this.values.keySet())
      {
         if(first == false)
         {
            stringBuilder.append(" , ");
         }

         first = false;
         stringBuilder.append('"');
         stringBuilder.append(key);
         stringBuilder.append("\": ");
         stringBuilder.append(this.values.get(key));
      }

      stringBuilder.append('}');

      return stringBuilder.toString();
   }
}