package jhelp.util.io.json;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import jhelp.util.HashCode;
import jhelp.util.io.UtilIO;
import jhelp.util.list.Pair;
import jhelp.util.math.UtilMath;
import jhelp.util.text.UtilText;

/**
 * JSON value.<br>
 * For {@link #NULL}, {@link #TRUE} and {@link #FALSE}, it is safe to use == for comparison, because anyway they are created,
 * the unique instance is used
 * 
 * @author JHelp
 */
public final class ValueJSON
{
   /** Key word for <b>false</b> value */
   private static final String   KEY_FALSE = "false";
   /** Key word for <b>null</b> value */
   private static final String   KEY_NULL  = "null";
   /** Key word for <b>true</b> value */
   private static final String   KEY_TRUE  = "true";
   /** False value */
   public final static ValueJSON FALSE     = new ValueJSON(ValueType.BOOLEAN, false);
   /** Null value */
   public static final ValueJSON NULL      = new ValueJSON(ValueType.NULL, null);
   /** True value */
   public final static ValueJSON TRUE      = new ValueJSON(ValueType.BOOLEAN, true);

   /**
    * Search the corresponding } OR ] on string.<br>
    * We count { and [ as +1, } and ] as -1, if we have negative value, we found the corresponding.<br>
    * It returns -1 if not found
    * 
    * @param string
    *           String to parse
    * @param start
    *           Index where start the search (Just after the opening [ OR {
    * @param end
    *           Last index where search
    * @return Corresponding index or -1 if not found
    */
   private static int serachCorrespondingEnd(final String string, final int start, final int end)
   {
      final char[] chars = string.toCharArray();

      boolean insideString = false;
      boolean antiSlash = false;
      int accolades = 0;
      int barks = 0;

      for(int i = start; i <= end; i++)
      {
         switch(chars[i])
         {
            case '{':
               if((insideString == false) && (antiSlash == false))
               {
                  accolades++;
               }

               antiSlash = false;
            break;
            case '[':
               if((insideString == false) && (antiSlash == false))
               {
                  barks++;
               }

               antiSlash = false;
            break;
            case '}':
               if((insideString == false) && (antiSlash == false))
               {
                  accolades--;

                  if(accolades < 0)
                  {
                     return i;
                  }
               }

               antiSlash = false;
            break;
            case ']':
               if((insideString == false) && (antiSlash == false))
               {
                  barks--;

                  if(barks < 0)
                  {
                     return i;
                  }
               }

               antiSlash = false;
            break;
            case '\\':
               antiSlash = !antiSlash;
            break;
            case '"':
               if(antiSlash == false)
               {
                  insideString = !insideString;
               }

               antiSlash = false;
            break;
            default:
               antiSlash = false;
            break;
         }
      }

      return -1;
   }

   /**
    * Parse a part of String to JSON value and next index to read.<br>
    * It returns {@code null} if part String not a JSON value
    * 
    * @param string
    *           String to parse
    * @param first
    *           Start index to parse
    * @param last
    *           End index to parse
    * @return JSON value and next index OR {@code null} if part String not a JSON value
    */
   static Pair<ValueJSON, Integer> parse(final String string, int first, final int last)
   {
      // Search first not white character
      while((first <= last) && (string.charAt(first) <= 32))
      {
         first++;
      }

      if(first > last)
      {
         return null;
      }

      // Is their, at least, 2 characters
      if(first < last)
      {
         switch(string.charAt(first))
         {
         // Try to parse as String
            case '"':
            {
               int end = first + 1;
               boolean antiSlash = false;
               boolean found = false;

               // Search String end
               for(; (end <= last) && (found == false); end++)
               {
                  switch(string.charAt(end))
                  {
                     case '\\':
                        antiSlash = !antiSlash;
                     break;
                     case '"':
                        if(antiSlash == false)
                        {
                           found = true;
                        }

                        antiSlash = false;
                     break;
                     default:
                        antiSlash = false;
                     break;
                  }
               }

               if(end <= last)
               {
                  // Found String
                  return new Pair<ValueJSON, Integer>(ValueJSON.newValue(string.substring(first + 1, end - 1)), end);
               }
            }
            break;
            // Try to parse as JSON object
            case '{':
            {
               // Search ending definition
               final int end = ValueJSON.serachCorrespondingEnd(string, first + 1, last);

               if(end >= 0)
               {
                  // Parse the JSON object
                  final Pair<ObjectJSON, Integer> pair = ObjectJSON.parse(string, first, end);

                  if(pair != null)
                  {
                     return new Pair<ValueJSON, Integer>(ValueJSON.newValue(pair.element1), pair.element2);
                  }
               }
            }
            break;
            // Try to parse as JSON array
            case '[':
            {
               // Search ending definition
               final int end = ValueJSON.serachCorrespondingEnd(string, first + 1, last);

               if(end >= 0)
               {
                  // Parse the JSON array
                  final Pair<ArrayJSON, Integer> pair = ArrayJSON.parse(string, first, end);

                  if(pair != null)
                  {
                     return new Pair<ValueJSON, Integer>(ValueJSON.newValue(pair.element1), pair.element2);
                  }
               }
            }
            break;
         }
      }

      // Search key word or number end
      int end = first + 1;
      while((end <= last) && (string.charAt(end) > 32))
      {
         end++;
      }

      // Try to recognize the extracted word
      final String value = string.substring(first, end);

      if(ValueJSON.KEY_NULL.equals(value) == true)
      {
         return new Pair<ValueJSON, Integer>(ValueJSON.NULL, end);
      }

      if(ValueJSON.KEY_TRUE.equals(value) == true)
      {
         return new Pair<ValueJSON, Integer>(ValueJSON.TRUE, end);
      }

      if(ValueJSON.KEY_FALSE.equals(value) == true)
      {
         return new Pair<ValueJSON, Integer>(ValueJSON.FALSE, end);
      }

      try
      {
         return new Pair<ValueJSON, Integer>(ValueJSON.newValue(Double.parseDouble(value)), end);
      }
      catch(final Exception exception)
      {
      }

      return null;
   }

   /**
    * Create JSON value with a JSON array inside
    * 
    * @param array
    *           Array content
    * @return Created JSON value
    */
   public static ValueJSON newValue(final ArrayJSON array)
   {
      if(array == null)
      {
         throw new NullPointerException("array musn't be null");
      }

      return new ValueJSON(ValueType.ARRAY, array);
   }

   /**
    * Create boolean value
    * 
    * @param value
    *           Value
    * @return Created value
    */
   public static ValueJSON newValue(final boolean value)
   {
      if(value == true)
      {
         return ValueJSON.TRUE;
      }

      return ValueJSON.FALSE;
   }

   /**
    * Create JSON value with a number inside
    * 
    * @param number
    *           Number content
    * @return Created JSON value
    */
   public static ValueJSON newValue(final double number)
   {
      return new ValueJSON(ValueType.NUMBER, number);
   }

   /**
    * Create a JSON value from a file
    * 
    * @param file
    *           File to put inside JSON value
    * @return Created JSON value
    * @throws IOException
    *            On reading issue
    */
   public static ValueJSON newValue(final File file) throws IOException
   {
      if(file.exists() == false)
      {
         throw new IOException(file.getAbsolutePath() + " doesn't exists !");
      }

      FileInputStream fileInputStream = null;

      try
      {
         fileInputStream = new FileInputStream(file);
         return ValueJSON.newValue(fileInputStream);
      }
      catch(final Exception exception)
      {
         throw new IOException("Failed to store " + file.getAbsolutePath(), exception);
      }
      finally
      {
         if(fileInputStream != null)
         {
            try
            {
               fileInputStream.close();
            }
            catch(final Exception exception)
            {
            }
         }
      }
   }

   /**
    * Create a JSON value from a stream.<br>
    * The stream not close by the method
    * 
    * @param inputStream
    *           Stream to read
    * @return Created JSON value
    * @throws IOException
    *            On reading issue
    */
   public static ValueJSON newValue(final InputStream inputStream) throws IOException
   {
      return ValueJSON.newValue(UtilIO.toBase64(inputStream));
   }

   /**
    * Create JSON value with a JSON object inside
    * 
    * @param object
    *           Object content
    * @return Created JSON value
    */
   public static ValueJSON newValue(final ObjectJSON object)
   {
      if(object == null)
      {
         throw new NullPointerException("object musn't be null");
      }

      return new ValueJSON(ValueType.OBJECT, object);
   }

   /**
    * Create JSON value with a String inside
    * 
    * @param string
    *           String content
    * @return Created JSON value
    */
   public static ValueJSON newValue(final String string)
   {
      if(string == null)
      {
         throw new NullPointerException("string musn't be null");
      }

      return new ValueJSON(ValueType.STRING, string);
   }

   /** Value content */
   private final Object    value;

   /** Value type */
   private final ValueType valueType;

   /**
    * Create a new instance of ValueJSON
    * 
    * @param valueType
    *           Value type
    * @param value
    *           Value content
    */
   private ValueJSON(final ValueType valueType, final Object value)
   {
      this.valueType = valueType;
      this.value = value;
   }

   /**
    * Indicates if an object is equals to this value <br>
    * <br>
    * <b>Parent documentation:</b><br>
    * {@inheritDoc}
    * 
    * @param object
    *           Tested object
    * @return {@code true} if the object is equals to this value
    * @see java.lang.Object#equals(java.lang.Object)
    */
   @Override
   public boolean equals(final Object object)
   {
      if(this == object)
      {
         return true;
      }

      if(object == null)
      {
         return false;
      }

      if((object instanceof ValueJSON) == false)
      {
         return false;
      }

      final ValueJSON value = (ValueJSON) object;

      if(this.valueType != value.valueType)
      {
         return false;
      }

      if(this.valueType == ValueType.NUMBER)
      {
         final double thisNumber = (Double) this.value;
         final double otherNumber = (Double) value.value;
         return UtilMath.equals(thisNumber, otherNumber);
      }

      if(this.valueType == ValueType.NULL)
      {
         return true;
      }

      return this.value.equals(value.value);
   }

   /**
    * Obtain JSON array content.<br>
    * The value type <b>MUST</b> be an array
    * 
    * @return JSON array content
    */
   public ArrayJSON getArray()
   {
      if(this.valueType != ValueType.ARRAY)
      {
         throw new IllegalStateException("The value is " + this.valueType + " not an Array");
      }

      return (ArrayJSON) this.value;
   }

   /**
    * Get JSON value as binary and write it inside a file
    * 
    * @param file
    *           File where write
    * @throws IOException
    *            On writing issue
    */
   public void getBinary(final File file) throws IOException
   {
      if(UtilIO.createFile(file) == false)
      {
         throw new IOException("Can't create file " + file.getAbsolutePath());
      }

      FileOutputStream fileOutputStream = null;

      try
      {
         fileOutputStream = new FileOutputStream(file);
         this.getBinary(fileOutputStream);
      }
      catch(final Exception exception)
      {
         throw new IOException("Failed to extract data in " + file.getAbsolutePath());
      }
      finally
      {
         if(fileOutputStream != null)
         {
            try
            {
               fileOutputStream.flush();
            }
            catch(final Exception exception)
            {
            }

            try
            {
               fileOutputStream.close();
            }
            catch(final Exception exception)
            {
            }
         }
      }
   }

   /**
    * Get JSON value as binary and write it inside a stream.<br>
    * The stream i not close by the method
    * 
    * @param outputStream
    *           Stream where write
    * @throws IOException
    *            On writing issue
    */
   public void getBinary(final OutputStream outputStream) throws IOException
   {
      UtilIO.fromBase64(this.getString(), outputStream);
   }

   /**
    * Obtain boolean content.<br>
    * The value type <b>MUST</b> be a boolean<br>
    * If value type is a String, it tries to convert it to boolean
    * 
    * @return Boolean content
    */
   public boolean getBoolean()
   {
      switch(this.valueType)
      {
         case BOOLEAN:
            return (Boolean) this.value;
         case STRING:
            final String value = ((String) this.value).trim();

            if("true".equalsIgnoreCase(value) == true)
            {
               return true;
            }

            if("false".equalsIgnoreCase(value) == true)
            {
               return false;
            }

            throw new IllegalStateException("Can't transform '" + value + "' to boolean");
         default:
            throw new IllegalStateException("Can't see " + this.valueType + " value as boolean");
      }
   }

   /**
    * Obtain number content.<br>
    * The value type <b>MUST</b> be a number.<br>
    * If value type is a String it tries to convert it to a number
    * 
    * @return Number content
    */
   public double getNumber()
   {
      switch(this.valueType)
      {
         case NUMBER:
            return (Double) this.value;
         case STRING:
            return Double.parseDouble((String) this.value);
         default:
            throw new IllegalStateException("Can't see " + this.valueType + " value as number");
      }
   }

   /**
    * Obtain JSON object content.<br>
    * The value type <b>MUST</b> be an object
    * 
    * @return JSON object content
    */
   public ObjectJSON getObject()
   {
      if(this.valueType != ValueType.OBJECT)
      {
         throw new IllegalStateException("The value is " + this.valueType + " not an Object");
      }

      return (ObjectJSON) this.value;
   }

   /**
    * Obtain String content.<br>
    * It returns {@code null} if this is the {@link #NULL} value
    * 
    * @return String content OR {@code null} if this is the {@link #NULL} value
    */
   public String getString()
   {
      if(this.value == null)
      {
         return null;
      }

      return this.value.toString();
   }

   /**
    * Value type
    * 
    * @return Value type
    */
   public ValueType getType()
   {
      return this.valueType;
   }

   /**
    * Hash code <br>
    * <br>
    * <b>Parent documentation:</b><br>
    * {@inheritDoc}
    * 
    * @return Hash code
    * @see java.lang.Object#hashCode()
    */
   @Override
   public int hashCode()
   {
      return HashCode.computeHashCode(this.valueType, this.value);
   }

   /**
    * Indicates if it is the{@link #NULL} value
    * 
    * @return {@code true} if it is the{@link #NULL} value
    */
   public boolean isNull()
   {
      return this.value == null;
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
      switch(this.valueType)
      {
         case ARRAY:
            return this.value.toString();
         case BOOLEAN:
            if(((Boolean) this.value) == true)
            {
               return "true";
            }
            else
            {
               return "false";
            }
         case NULL:
            return "null";
         case NUMBER:
            return this.value.toString();
         case OBJECT:
            return this.value.toString();
         case STRING:
            return UtilText.concatenate('"', this.value, '"');
      }

      return null;
   }
}