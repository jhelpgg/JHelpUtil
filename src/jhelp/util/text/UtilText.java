/**
 * <h1>License :</h1> <br>
 * The following code is deliver as is. I take care that code compile and work, but I am not responsible about any damage it may
 * cause.<br>
 * You can use, modify, the code as your need for any usage. But you can't do any action that avoid me or other person use,
 * modify this code. The code is free for usage and modification, you can't change that fact.<br>
 * <br>
 *
 * @author JHelp
 */
package jhelp.util.text;

import java.awt.Dimension;
import java.nio.charset.Charset;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import jhelp.util.Utilities;
import jhelp.util.debug.Debug;
import jhelp.util.gui.JHelpFont;
import jhelp.util.io.UtilIO;
import jhelp.util.list.Pair;
import jhelp.util.reflection.Reflector;

/**
 * Textual utilities
 *
 * @author JHelp
 */
public final class UtilText
{
   /** Pattern for class reference */
   private static final Pattern IMAGE_TAG_CLASS_REFERENCE    = Pattern.compile("(<\\s*img\\s+src=\\\")class:([a-zA-Z0-9.]+)([^\"]*)");
   /** Pattern for external reference */
   private static final Pattern IMAGE_TAG_EXTERNAL_REFERENCE = Pattern.compile("(<\\s*img\\s+src=\\\")external:([^\"]*)");
   /** Default escape characters : \ (see {@link StringExtractor}) */
   public static final String   DEFAULT_ESCAPE_CHARACTERS    = "\\";
   /**
    * Default escape separators : [space], [Line return \n], [tabulation \t], [carriage return \r] and [\f] (see
    * {@link StringExtractor})
    */
   public static final String   DEFAULT_SEPARATORS           = " \n\t\r\f";

   /** Default string limiters : " and ' (see {@link StringExtractor}) */
   public static final String   DEFAULT_STRING_LIMITERS      = "\"'";

   /** UTF-8 char set */
   public static final Charset  UTF8                         = Charset.forName("UTF-8");

   /**
    * Append an object to string buffer
    *
    * @param stringBuffer
    *           Where append
    * @param object
    *           Object to append
    */
   @SuppressWarnings(
   {
         "rawtypes", "unchecked"
   })
   private static void appendObject(final StringBuffer stringBuffer, final Object object)
   {
      if((object == null) || (object.getClass().isArray() == false))
      {
         if(object != null)
         {
            if(object instanceof Iterable)
            {
               stringBuffer.append('{');
               boolean first = true;

               for(final Object obj : (Iterable) object)
               {
                  if(first == false)
                  {
                     stringBuffer.append("; ");
                  }

                  UtilText.appendObject(stringBuffer, obj);
                  first = false;
               }

               stringBuffer.append('}');
               return;
            }

            if(object instanceof Map)
            {
               stringBuffer.append('{');
               boolean first = true;

               for(final Entry entry : (Set<Entry>) ((Map) object).entrySet())
               {
                  if(first == false)
                  {
                     stringBuffer.append(" | ");
                  }

                  UtilText.appendObject(stringBuffer, entry.getKey());
                  stringBuffer.append("=");
                  UtilText.appendObject(stringBuffer, entry.getValue());
                  first = false;
               }

               stringBuffer.append('}');
               return;
            }
         }

         stringBuffer.append(object);

         return;
      }

      stringBuffer.append('[');

      if(object.getClass().getComponentType().isPrimitive() == true)
      {
         final String name = object.getClass().getComponentType().getName();

         if(Reflector.PRIMITIVE_BOOLEAN.equals(name) == true)
         {
            for(final boolean element : (boolean[]) object)
            {
               stringBuffer.append(element);
               stringBuffer.append(", ");
            }
         }
         else if(Reflector.PRIMITIVE_BYTE.equals(name) == true)
         {
            for(final byte element : (byte[]) object)
            {
               stringBuffer.append(element);
               stringBuffer.append(", ");
            }
         }
         else if(Reflector.PRIMITIVE_CHAR.equals(name) == true)
         {
            for(final char element : (char[]) object)
            {
               stringBuffer.append(element);
               stringBuffer.append(", ");
            }
         }
         else if(Reflector.PRIMITIVE_DOUBLE.equals(name) == true)
         {
            for(final double element : (double[]) object)
            {
               stringBuffer.append(element);
               stringBuffer.append(", ");
            }
         }
         else if(Reflector.PRIMITIVE_FLOAT.equals(name) == true)
         {
            for(final float element : (float[]) object)
            {
               stringBuffer.append(element);
               stringBuffer.append(", ");
            }
         }
         else if(Reflector.PRIMITIVE_INT.equals(name) == true)
         {
            for(final int element : (int[]) object)
            {
               stringBuffer.append(element);
               stringBuffer.append(", ");
            }
         }
         else if(Reflector.PRIMITIVE_LONG.equals(name) == true)
         {
            for(final long element : (long[]) object)
            {
               stringBuffer.append(element);
               stringBuffer.append(", ");
            }
         }
         else if(Reflector.PRIMITIVE_SHORT.equals(name) == true)
         {
            for(final short element : (short[]) object)
            {
               stringBuffer.append(element);
               stringBuffer.append(", ");
            }
         }
      }
      else
      {
         for(final Object element : (Object[]) object)
         {
            UtilText.appendObject(stringBuffer, element);
            stringBuffer.append(", ");
         }
      }

      final int length = stringBuffer.length();
      stringBuffer.delete(length - 2, length);

      stringBuffer.append(']');
   }

   /**
    * Return a list of indexes pairs. Each pair contains a start index and a end index. Each couple delimit where an integer is
    * inside a string.
    *
    * @param string
    *           String where search all integers inside
    * @return List of couple information to know where find the integers inside the given string
    */
   private static List<Pair<Integer, Integer>> extractIntegersPair(final String string)
   {
      final char[] chars = string.toCharArray();
      final int length = chars.length;
      boolean valid = false;
      int start = -1;
      char car;
      final List<Pair<Integer, Integer>> pairs = new ArrayList<Pair<Integer, Integer>>();

      for(int i = 0; i < length; i++)
      {
         car = chars[i];

         if(((car == '-') && (start < 0)) || ((car >= '0') && (car <= '9')))
         {
            if(start < 0)
            {
               start = i;
            }

            if((car >= '0') && (car <= '9'))
            {
               valid = true;
            }
         }
         else if((start >= 0) && (start < i) && (valid == true))
         {
            pairs.add(new Pair<Integer, Integer>(start, i));

            start = -1;
            valid = false;
         }
      }

      if((start >= 0) && (valid == true))
      {
         pairs.add(new Pair<Integer, Integer>(start, length));
      }

      return pairs;
   }

   /**
    * Return a list of indexes pairs. Each pair contains a start index and a end index. Each couple delimit where a real is
    * inside a string.
    *
    * @param string
    *           String where search all reals inside
    * @return List of couple information to know where find the reals inside the given string
    */
   private static List<Pair<Integer, Integer>> extractRealsPair(final String string)
   {
      final char[] chars = string.toCharArray();
      final int length = chars.length;
      boolean valid = false;
      boolean havePoint = false;
      int start = -1;
      char car;
      final List<Pair<Integer, Integer>> pairs = new ArrayList<Pair<Integer, Integer>>();

      for(int i = 0; i < length; i++)
      {
         car = chars[i];

         if(((car == '-') && (start < 0)) || ((car == '.') && ((havePoint == false) || (start == (i - 1)))) || ((car >= '0') && (car <= '9')))
         {
            if(start < 0)
            {
               start = i;
            }

            if(car == '.')
            {
               if((havePoint == true) && (start == (i - 1)))
               {
                  start = i;
               }

               havePoint = true;
            }

            if((car >= '0') && (car <= '9'))
            {
               valid = true;
            }
         }
         else if((start >= 0) && (start < i) && (valid == true))
         {
            pairs.add(new Pair<Integer, Integer>(start, i));

            start = -1;
            valid = false;
            havePoint = false;

            if((car == '.') && (chars[i - 1] == '.'))
            {
               i--;
            }
         }
      }

      if((start >= 0) && (valid == true))
      {
         pairs.add(new Pair<Integer, Integer>(start, length));
      }

      return pairs;
   }

   /**
    * Add \ before each " we can chose do treat the ' as normal character or add \ before it also
    *
    * @param string
    *           String to replace
    * @param simpleQuote
    *           Indicates if we also add \ before '
    * @return Result string
    */
   public static String addAntiSlash(final String string, final boolean simpleQuote)
   {
      final int length = string.length();
      final StringBuffer stringBuffer = new StringBuffer(length);

      for(final char car : string.toCharArray())
      {
         switch(car)
         {
            case '\'':
               if(simpleQuote == false)
               {
                  stringBuffer.append(car);
                  break;
               }
               // No break
               //$FALL-THROUGH$
            case '\\':
            case '"':
               stringBuffer.append('\\');
               // No break, we want also add the character itself, like other
               // ones
               //$FALL-THROUGH$
            default:
               stringBuffer.append(car);
            break;
         }
      }

      return stringBuffer.toString();
   }

   public static String[] capture(final String pattern, final String text)
   {
      final char[] charPattern = pattern.toCharArray();
      final int lengthPattern = charPattern.length;
      int nb = 0;
      for(int i = 0; i < lengthPattern; i++)
      {
         if(charPattern[i] == '?')
         {
            nb++;
         }
      }

      if(nb == 0)
      {
         if(pattern.equals(text) == true)
         {
            return new String[0];
         }
         else
         {
            return null;
         }
      }

      final String[] result = new String[nb];

      final char[] charText = text.toCharArray();
      final int lengthText = charText.length;

      int indexPattern = 0;
      int indexText = 0;
      char waitFor = ' ';
      boolean waiting = false;
      int start = 0;
      int indexResult = 0;
      int count1 = 0;
      int count2 = 0;
      int count3 = 0;
      boolean bool1 = true;
      boolean bool2 = true;

      while((indexPattern < lengthPattern) && (indexText < lengthText))
      {
         if(waiting == true)
         {
            if((charText[indexText] == waitFor) && (count1 == 0) && (count2 == 0) && (count3 == 0) && (bool1 == true) && (bool2 == true))
            {
               waiting = false;

               result[indexResult++] = text.substring(start, indexText);
               indexPattern++;
            }
            else
            {
               switch(charText[indexText])
               {
                  case '(':
                     count1++;
                  break;
                  case ')':
                     count1--;
                  break;
                  case '[':
                     count2++;
                  break;
                  case ']':
                     count2--;
                  break;
                  case '{':
                     count3++;
                  break;
                  case '}':
                     count3--;
                  break;
                  case '\'':
                     bool1 = !bool1;
                  break;
                  case '"':
                     bool2 = !bool2;
                  break;
               }

               indexText++;
            }
         }
         else
         {
            if(charPattern[indexPattern] == '?')
            {
               if((indexPattern + 1) < lengthPattern)
               {
                  waitFor = charPattern[indexPattern + 1];
               }
               else
               {
                  waitFor = (char) 0;
               }

               start = indexText;
               waiting = true;
            }
            else if(charText[indexText] == charPattern[indexPattern])
            {
               indexText++;
               indexPattern++;
            }
            else
            {
               return null;
            }
         }
      }

      if((waiting == true) && (waitFor == (char) 0))
      {
         waiting = false;

         result[indexResult++] = text.substring(start, indexText);
         indexPattern++;
      }

      if((indexPattern != lengthPattern) || (indexText != lengthText))
      {
         return null;
      }

      return result;
   }

   /**
    * Color to text representation
    *
    * @param color
    *           Color to convert
    * @return Text representation
    */
   public static String colorText(final int color)
   {
      final char[] characters = new char[8];

      for(int i = 0, shift = 28; i < 8; i++, shift -= 4)
      {
         characters[i] = Integer.toHexString((color >> shift) & 0xF).charAt(0);
      }

      return new String(characters);
   }

   /**
    * Compute "distance" between two texts.<br>
    * To compute how "far" are the tow texts, we take all words of each and if a word of one not exits in the other, we decide
    * they are more far than if the word is in the second but not in the same place. If the word is in same place, we say
    * distance is zero
    *
    * @param text1
    *           First text
    * @param text2
    *           Second text
    * @param caseSensitive
    *           Indicates if for word comparison case is important or not
    * @param wordDelimiters
    *           List of delimiters of words (Each characters of this string are see as separator and only them)
    * @return The computed distance
    */
   public static int computeDistance(String text1, String text2, final boolean caseSensitive, final String wordDelimiters)
   {
      if(text1 == null)
      {
         throw new NullPointerException("text1 musn't be null");
      }
      if(text2 == null)
      {
         throw new NullPointerException("text2 musn't be null");
      }
      if(caseSensitive == false)
      {
         text1 = UtilText.upperCaseWithoutAccent(text1);
         text2 = UtilText.upperCaseWithoutAccent(text2);
      }
      final Hashtable<String, Integer> wordList1 = new Hashtable<String, Integer>();
      final Hashtable<String, Integer> wordList2 = new Hashtable<String, Integer>();
      StringTokenizer stringTokenizer = new StringTokenizer(text1, wordDelimiters, false);
      int index = 0;
      while(stringTokenizer.hasMoreTokens() == true)
      {
         wordList1.put(stringTokenizer.nextToken(), index++);
      }
      stringTokenizer = new StringTokenizer(text2, wordDelimiters, false);
      index = 0;
      while(stringTokenizer.hasMoreTokens() == true)
      {
         wordList2.put(stringTokenizer.nextToken(), index++);
      }
      String key;
      int index1;
      int index2;
      Integer integer;
      int distance = 0;
      Enumeration<String> keys = wordList1.keys();
      boolean atLeast = false;
      while(keys.hasMoreElements() == true)
      {
         key = keys.nextElement();
         index1 = wordList1.get(key).intValue();
         integer = wordList2.get(key);
         if(integer == null)
         {
            distance += 10;
         }
         else
         {
            atLeast = true;
            index2 = integer.intValue();
            if(index1 != index2)
            {
               distance++;
            }
         }
      }

      if(atLeast == false)
      {
         distance += 100;
      }

      atLeast = false;
      keys = wordList2.keys();
      while(keys.hasMoreElements() == true)
      {
         key = keys.nextElement();
         index2 = wordList2.get(key).intValue();
         integer = wordList1.get(key);
         if(integer == null)
         {
            distance += 10;
         }
         else
         {
            atLeast = true;
            index1 = integer.intValue();
            if(index1 != index2)
            {
               distance++;
            }
         }
      }

      if(atLeast == false)
      {
         distance += 100;
      }

      return distance;
   }

   /**
    * Compute a "distance" between 2 words. <br>
    * It says hw "far" a test word is from a reference word <br>
    * 0 means they are the same word
    *
    * @param test
    *           Word to test
    * @param reference
    *           Reference word
    * @return The "distance"
    */
   public static int computeDistanceWord(String test, String reference)
   {
      if(test.equals(reference) == true)
      {
         return 0;
      }

      test = UtilText.upperCaseWithoutAccent(test.replace("œ", "oe").replace("Œ", "OE").replace("æ", "ae").replace("Æ", "AE"));
      reference = UtilText.upperCaseWithoutAccent(reference.replace("œ", "oe").replace("Œ", "OE").replace("æ", "ae").replace("Æ", "AE"));
      int distance = 1;

      if(test.equals(reference) == true)
      {
         return distance;
      }

      final char[] charactersTest = test.toCharArray();
      final int lengthTest = charactersTest.length;
      final char[] charactersReference = reference.toCharArray();
      final int lengthReference = charactersReference.length;
      distance += 1 + Math.abs(lengthTest - lengthReference);
      char character;
      int minimum;

      for(int indexTest = 0; indexTest < lengthTest; indexTest++)
      {
         character = charactersTest[indexTest];
         minimum = lengthReference;

         for(int indexReference = 0; indexReference < lengthReference; indexReference++)
         {
            if(character == charactersReference[indexReference])
            {
               minimum = Math.min(minimum, Math.abs(indexReference - indexTest));
            }
         }

         distance += minimum;
      }

      return distance;
   }

   /**
    * Compute font with maximum size with given constraints for given text.
    *
    * @param text
    *           Text that must respect the constraints
    * @param maximumWidth
    *           Maximum width that the text can take
    * @param maximumHeight
    *           Maximum height that the text can take
    * @param familly
    *           Font family to use
    * @param bold
    *           Indicates if bold text
    * @param italic
    *           Indicates if italic text
    * @param underline
    *           Indicates if underline text
    * @return Computed font
    */
   public static JHelpFont computeMaximumSizedFont(final String text, final int maximumWidth, final int maximumHeight, final String familly, final boolean bold,
         final boolean italic, final boolean underline)
   {
      int max = maximumHeight << 1;
      int min = 1;
      int size;
      JHelpFont font;
      Dimension dimension;

      do
      {
         size = (max + min) >> 1;
         font = new JHelpFont(familly, size, bold, italic, underline);
         dimension = font.stringSize(text);

         if((dimension.width > maximumWidth) || (dimension.height > maximumHeight))
         {
            max = size;
         }
         else
         {
            min = size;
         }
      }
      while((min + 1) < max);

      return font;
   }

   /**
    * Compute a name not inside a set of name
    *
    * @param base
    *           Base name
    * @param names
    *           Set of names
    * @return Created name
    */
   public static String computeNotInsideName(String base, final Collection<String> names)
   {
      while(names.contains(base) == true)
      {
         base = UtilText.computeOtherName(base);
      }

      return base;
   }

   /**
    * Compute an other name for a String name. It add a number or increase the number at the end of the String.<br>
    * Use for auto generate names
    *
    * @param name
    *           Name base
    * @return Computed name
    */
   public static String computeOtherName(final String name)
   {
      final char[] characters = name.toCharArray();
      final int length = characters.length;
      int index = length - 1;
      int count = 0;

      while((count < 6) && (index >= 0) && (characters[index] >= '0') && (characters[index] <= '9'))
      {
         index--;
         count++;
      }

      index++;

      if(index >= length)
      {
         return UtilText.concatenate(name, " 0");
      }

      if(index == 0)
      {
         return String.valueOf(Integer.parseInt(name) + 1);
      }

      return UtilText.concatenate(name.substring(0, index), Integer.parseInt(name.substring(index)) + 1);
   }

   /**
    * Concatenate several object to make a string representation
    *
    * @param objects
    *           Objects to concatenate
    * @return String representation
    */
   public static String concatenate(final Object... objects)
   {
      if((objects == null) || (objects.length < 1))
      {
         return "";
      }

      final StringBuffer stringBuffer = new StringBuffer(12 * objects.length);

      for(final Object object : objects)
      {
         UtilText.appendObject(stringBuffer, object);
      }

      return stringBuffer.toString();
   }

   /**
    * Cut String in several parts
    *
    * @param string
    *           String to cut
    * @param separator
    *           Separator where cut
    * @return Parts
    */
   public static String[] cutSringInPart(final String string, final char separator)
   {
      if(string == null)
      {
         return null;
      }

      final int length = string.length();

      if(length == 0)
      {
         return new String[0];
      }

      final char[] chars = string.toCharArray();

      if(length == 1)
      {
         if(chars[0] == separator)
         {
            return new String[]
            {
                  ""
            };
         }

         return new String[]
         {
               string
         };
      }

      int count = 1;

      for(int i = 0; i < length; i++)
      {
         if(chars[i] == separator)
         {
            count++;
         }
      }

      final String[] array = new String[count];

      int start = 0;
      int index = 0;

      for(int i = 0; i < length; i++)
      {
         if(chars[i] == separator)
         {
            if(start < i)
            {
               array[index] = new String(chars, start, i - start);
            }
            else
            {
               array[index] = "";
            }

            index++;
            start = i + 1;
         }
      }

      if(start < length)
      {
         array[index] = new String(chars, start, length - start);
      }
      else
      {
         array[index] = "";
      }

      return array;
   }

   /**
    * Extract all double inside a string
    *
    * @param string
    *           String where extract the doubles
    * @return Doubles founds inside the given string
    */
   public static double[] extractDoubleFrom(final String string)
   {
      if(string == null)
      {
         return null;
      }

      final List<Pair<Integer, Integer>> pairs = UtilText.extractRealsPair(string);
      final int size = pairs.size();
      final double[] array = new double[size];
      Pair<Integer, Integer> pair;

      for(int i = 0; i < size; i++)
      {
         pair = pairs.get(i);
         array[i] = Double.parseDouble(string.substring(pair.element1, pair.element2));
      }

      return array;
   }

   /**
    * Extract all float inside a string
    *
    * @param string
    *           String where extract the floats
    * @return Floats founds inside the given string
    */
   public static float[] extractFloatFrom(final String string)
   {
      if(string == null)
      {
         return null;
      }

      final List<Pair<Integer, Integer>> pairs = UtilText.extractRealsPair(string);
      final int size = pairs.size();
      final float[] array = new float[size];
      Pair<Integer, Integer> pair;

      for(int i = 0; i < size; i++)
      {
         pair = pairs.get(i);
         array[i] = Float.parseFloat(string.substring(pair.element1, pair.element2));
      }

      return array;
   }

   /**
    * Extract all integers inside a string
    *
    * @param string
    *           String where extract the integers
    * @return Integers founds inside the given string
    */
   public static int[] extractIntFrom(final String string)
   {
      if(string == null)
      {
         return null;
      }

      final List<Pair<Integer, Integer>> pairs = UtilText.extractIntegersPair(string);
      final int size = pairs.size();
      final int[] array = new int[size];
      Pair<Integer, Integer> pair;

      for(int i = 0; i < size; i++)
      {
         pair = pairs.get(i);
         array[i] = Integer.parseInt(string.substring(pair.element1, pair.element2));
      }

      return array;
   }

   /**
    * Extract all longs inside a string
    *
    * @param string
    *           String where extract the longs
    * @return Longs founds inside the given string
    */
   public static long[] extractLongFrom(final String string)
   {
      if(string == null)
      {
         return null;
      }

      final List<Pair<Integer, Integer>> pairs = UtilText.extractIntegersPair(string);
      final int size = pairs.size();
      final long[] array = new long[size];
      Pair<Integer, Integer> pair;

      for(int i = 0; i < size; i++)
      {
         pair = pairs.get(i);
         array[i] = Long.parseLong(string.substring(pair.element1, pair.element2));
      }

      return array;
   }

   /**
    * Compute the first index in the char sequence of one of given characters
    *
    * @param charSequence
    *           Char sequence where search one character
    * @param characters
    *           Characters search
    * @return Index of the first character found in char sequence that inside in the given list. -1 if the char sequence doesn't
    *         contains any of given characters
    */
   public static int indexOf(final CharSequence charSequence, final char... characters)
   {
      return UtilText.indexOf(charSequence, 0, characters);
   }

   /**
    * Compute the first index >= at the given offset in the char sequence of one of given characters
    *
    * @param charSequence
    *           Char sequence where search one character
    * @param offset
    *           The offset where start the search
    * @param characters
    *           Characters search
    * @return Index of the first character >= of given offset found in char sequence that inside in the given list. -1 if the
    *         char sequence doesn't contains any of given characters after the given offset
    */
   public static int indexOf(final CharSequence charSequence, final int offset, final char... characters)
   {
      final int start = Math.max(0, offset);
      final int length = charSequence.length();
      char character;

      for(int index = start; index < length; index++)
      {
         character = charSequence.charAt(index);

         for(final char car : characters)
         {
            if(car == character)
            {
               return index;
            }
         }
      }

      return -1;
   }

   /**
    * Index of a string in string array
    *
    * @param tableau
    *           Array where search
    * @param chaine
    *           String search
    * @return Index or -1 if not found
    */
   public static int indexOf(final String tableau[], final String chaine)
   {
      if(tableau == null)
      {
         return -1;
      }
      for(int i = 0; i < tableau.length; i++)
      {
         if(chaine.equals(tableau[i]))
         {
            return i;
         }
      }
      return -1;
   }

   /**
    * Compute the index of a character in a string on ignoring characters between " or ' an in ignore characters with \ just
    * before them
    *
    * @param string
    *           String where search
    * @param character
    *           Looking for character
    * @return Index of character or -1 if not found
    */
   public static int indexOfIgnoreString(final String string, final char character)
   {
      return UtilText.indexOfIgnoreString(string, character, 0);
   }

   /**
    * Compute the index of a character in a string on ignoring characters between " or ' an in ignore characters with \ just
    * before them
    *
    * @param string
    *           String where search
    * @param character
    *           Looking for character
    * @param startIndex
    *           Index where start to search
    * @return Index of character or -1 if not found
    */
   public static int indexOfIgnoreString(final String string, final char character, final int startIndex)
   {
      final char[] characters = string.toCharArray();
      final int length = characters.length;
      char ch;
      char delimString = 0;
      boolean antiSlash = false;

      for(int i = Math.max(0, startIndex); i < length; i++)
      {
         ch = characters[i];

         if(antiSlash == true)
         {
            antiSlash = false;
         }
         else if(delimString > 0)
         {
            if(delimString == ch)
            {
               delimString = 0;
            }
         }
         else
         {
            switch(ch)
            {
               case '\\':
                  antiSlash = true;
               break;
               case '"':
               case '\'':
                  delimString = ch;
               break;
               default:
                  if(ch == character)
                  {
                     return i;
                  }
            }
         }
      }

      return -1;
   }

   /**
    * Give index of a string inside an other one on ignoring characters bettwen string delimiters.<br>
    * By example you type:<br>
    * <code lang="java">
    * int index=indexOfIgnoreStrings("Hello 'this is a test' and this end", "this", 0, UtilText.DEFAULT_STRING_LIMITERS);
    * </code> <br>
    * The index will be 26 (not 6)
    *
    * @param text
    *           Text where search
    * @param search
    *           Searched text
    * @param startIndex
    *           Index where start the search
    * @param stringLimiters
    *           String limiters to use
    * @return Index found or -1 if not found
    */
   public static int indexOfIgnoreStrings(final String text, final String search, final int startIndex, final String stringLimiters)
   {
      final int lengthText = text.length();
      final char[] charsText = text.toCharArray();

      final int lengthSearch = search.length();
      final char[] charsSearch = search.toCharArray();

      final char[] limiters = stringLimiters.toCharArray();

      boolean insideString = false;
      char currentStringLimiter = ' ';

      int index = startIndex;
      int i;
      char character;

      while((index + lengthSearch) <= lengthText)
      {
         character = charsText[index];

         if(insideString == true)
         {
            if(character == currentStringLimiter)
            {
               insideString = false;
            }
         }
         else if(character == charsSearch[0])
         {
            for(i = 1; i < lengthSearch; i++)
            {
               if(charsText[index + i] != charsSearch[i])
               {
                  break;
               }
            }

            if(i == lengthSearch)
            {
               return index;
            }
         }
         else if(Utilities.contains(character, limiters) == true)
         {
            insideString = true;
            currentStringLimiter = character;
         }

         index++;
      }

      return -1;
   }

   /**
    * Replace character that follow an \ by it's symbol : \n by carriage return, \t by tabulation and \&lt;other&gt; by
    * &lt;other&gt;
    *
    * @param string
    *           String to replace
    * @return Result string
    */
   public static String interpretAntiSlash(final String string)
   {
      final StringBuffer stringBuffer = new StringBuffer(string.length());

      final int limit = string.length() - 1;
      int start = 0;
      int index = string.indexOf('\\');
      char car;

      while(index >= 0)
      {
         stringBuffer.append(string.substring(start, index));

         if(index < limit)
         {
            car = string.charAt(index + 1);
            switch(car)
            {
               case 'n':
                  stringBuffer.append('\n');
               break;
               case 't':
                  stringBuffer.append('\t');
               break;
               case 'f':
                  stringBuffer.append('\f');
               break;
               case 'r':
                  stringBuffer.append('\r');
               break;
               case 'b':
                  stringBuffer.append('\b');
               break;
               default:
                  stringBuffer.append(car);
               break;
            }
         }

         start = index + 2;
         index = string.indexOf('\\', start);
      }

      stringBuffer.append(string.substring(start));

      return stringBuffer.toString();
   }

   /**
    * Compute the last index in the char sequence of one of given characters
    *
    * @param charSequence
    *           Char sequence where search one character
    * @param characters
    *           Characters search
    * @return Index of the last character found in char sequence that inside in the given list. -1 if the char sequence doesn't
    *         contains any of given characters
    */
   public static int lastIndexOf(final CharSequence charSequence, final char... characters)
   {
      return UtilText.lastIndexOf(charSequence, charSequence.length(), characters);
   }

   /**
    * Compute the last index <= of given offset in the char sequence of one of given characters
    *
    * @param charSequence
    *           Char sequence where search one character
    * @param offset
    *           Offset maximum for search
    * @param characters
    *           Characters search
    * @return Index of the last character <= given offset found in char sequence that inside in the given list. -1 if the char
    *         sequence doesn't contains any of given characters before the given offset
    */
   public static int lastIndexOf(final CharSequence charSequence, final int offset, final char... characters)
   {
      final int start = Math.min(charSequence.length() - 1, offset);
      char character;

      for(int index = start; index >= 0; index--)
      {
         character = charSequence.charAt(index);

         for(final char car : characters)
         {
            if(car == character)
            {
               return index;
            }
         }
      }

      return -1;
   }

   /**
    * Parse an hexadecimal string to integer
    *
    * @param string
    *           String to parse
    * @return Integer
    */
   public static int parseHexaString(final String string)
   {
      final char[] characters = string.toUpperCase().toCharArray();
      final int length = characters.length;
      if(length > 8)
      {
         throw new IllegalArgumentException(
               UtilText.concatenate("The number in hexadecimal can't be more than 8 characters, so '", string, "' can't be converted"));
      }

      int integer = 0;
      char character;

      for(int i = 0; i < length; i++)
      {
         integer <<= 4;

         if(((character = characters[i]) >= '0') && (character <= '9'))
         {
            integer |= character - '0';
         }
         else if((character >= 'A') && (character <= 'F'))
         {
            integer |= (character - 'A') + 10;
         }
         else
         {
            throw new IllegalArgumentException(UtilText.concatenate("The string '", string, "' have illegal character at ", i));
         }
      }

      return integer;
   }

   /**
    * Parse a string to integer
    *
    * @param string
    *           String to parse
    * @return Integer
    */
   public static int parseInteger(final String string)
   {
      if(string.startsWith("0x") == false)
      {
         return Integer.parseInt(string);
      }

      final char[] characters = string.substring(2).toUpperCase().toCharArray();
      final int length = characters.length;
      if(length > 8)
      {
         throw new IllegalArgumentException(
               UtilText.concatenate("The number in hexadecimal can't be more than 8 characters (after the 0x), so '", string, "' can't be converted"));
      }

      int integer = 0;
      char character;

      for(int i = 0; i < length; i++)
      {
         integer <<= 4;

         if(((character = characters[i]) >= '0') && (character <= '9'))
         {
            integer |= character - '0';
         }
         else if((character >= 'A') && (character <= 'F'))
         {
            integer |= (character - 'A') + 10;
         }
         else
         {
            throw new IllegalArgumentException(UtilText.concatenate("The string '", string, "' have illegal character at ", i + 2));
         }
      }

      return integer;
   }

   /**
    * Read a UTF-8 string from a part of byte array
    *
    * @param array
    *           Array to read
    * @param offset
    *           Start read index in array
    * @param length
    *           Number of bytes to read
    * @return Read string
    */
   public static String readUTF8(final byte[] array, final int offset, final int length)
   {
      return new String(array, offset, length, UtilText.UTF8);
   }

   /**
    * Remove accent of given character
    *
    * @param character
    *           Character to transform
    * @return Character without accent
    */
   public static char removeAccent(final char character)
   {
      return UtilText.removeAccent(String.valueOf(character)).charAt(0);
   }

   /**
    * Remove all accent inside given String
    *
    * @param string
    *           String to transform
    * @return String without accent
    */
   public static String removeAccent(final String string)
   {
      return Normalizer.normalize(string, Normalizer.Form.NFD).replaceAll("[^\\p{ASCII}]", " ");
   }

   /**
    * Remove all white characters of a string
    *
    * @param string
    *           String to "clean"
    * @return String without white characters
    */
   public static String removeWhiteCharacters(final String string)
   {
      if(string == null)
      {
         return null;
      }

      final int length = string.length();
      final char[] chars = string.toCharArray();
      final char[] result = new char[length];
      int size = 0;

      for(final char ch : chars)
      {
         if(ch > ' ')
         {
            result[size++] = ch;
         }
      }

      return new String(result, 0, size);
   }

   /**
    * Create a String full of same character
    *
    * @param character
    *           Character to repeat
    * @param time
    *           Number of character
    * @return Created string
    */
   public static String repeat(final char character, final int time)
   {
      if(time < 1)
      {
         return "";
      }

      final char[] characters = new char[time];
      for(int i = 0; i < time; i++)
      {
         characters[i] = character;
      }

      return new String(characters);
   }

   /**
    * Create string that repeat always the same string
    *
    * @param string
    *           String to repeat
    * @param time
    *           Number of repeat
    * @return Result string
    */
   public static String repeat(final String string, final int time)
   {
      final int length = string.length();
      if((time < 1) || (length == 0))
      {
         return "";
      }

      final StringBuffer stringBuffer = new StringBuffer(time * length);
      for(int i = 0; i < time; i++)
      {
         stringBuffer.append(string);
      }

      return stringBuffer.toString();
   }

   /**
    * Replace "hole" by a value.<br>
    * holes are like {0}, {8}, ... That means that the first replacement replace every holes {0}, then ninth every {8}, ...<br>
    * Example: <table border>
    * <tr>
    * <td><center><b>originalString</b></center></td>
    * <td><center><b>replacement</b></center></td>
    * <td><center><b>result</b></center></td>
    * </tr>
    * <tr>
    * <td>I want exchange this {0} with this {1}</td>
    * <td>hammer, apple</td>
    * <td>I want exchange this <b>hammer</b> with this <b>apple</b></td>
    * </tr>
    * <tr>
    * <td>I saw a {0} run after a {1}. I wonder if the {1} can escape from the {0}</td>
    * <td>dog, cat</td>
    * <td>I saw a <b>dog</b> run after a <b>cat</b>. I wonder if the <b>cat</b> can escape from the <b>dog</b></td>
    * </tr>
    * </table>
    *
    * @param originalString
    *           String with "holes"
    * @param replacement
    *           List of replacement for "holes". First replace hole {0}, second {1}, third {2} ,....
    * @return Result string with replacement
    */
   public static String replaceHole(final String originalString, String... replacement)
   {
      StringBuffer stringBuffer;
      int start;
      int indexOpenHole;
      int indexCloseHole;
      int indexReplacement;

      if(originalString == null)
      {
         throw new NullPointerException("The originalString musn't be null !");
      }

      if(replacement == null)
      {
         replacement = new String[0];
      }

      stringBuffer = new StringBuffer();

      start = 0;
      indexOpenHole = originalString.indexOf('{');
      while(indexOpenHole >= 0)
      {
         indexCloseHole = originalString.indexOf('}', indexOpenHole + 1);
         if(indexCloseHole >= 0)
         {
            stringBuffer.append(originalString.substring(start, indexOpenHole));

            indexReplacement = -1;

            try
            {
               indexReplacement = Integer.parseInt(originalString.substring(indexOpenHole + 1, indexCloseHole));
            }
            catch(final Exception exception)
            {
               // Nothing to do here
            }

            if((indexReplacement >= 0) && (indexReplacement < replacement.length))
            {
               stringBuffer.append(replacement[indexReplacement]);
            }

            start = indexCloseHole + 1;
            indexOpenHole = originalString.indexOf('{', start);
         }
         else
         {
            indexOpenHole = -1;
         }
      }
      stringBuffer.append(originalString.substring(start));

      return stringBuffer.toString();
   }

   /**
    * Replace holes like <b>{0}</b> or <b>{2_s}</b> or <b>{1_###eet}</b> or <b>...</b> depends of the given numbers.<br>
    * If the word just before the <b>{</b>, need to be change if the number is at least 2, then put after the hole number the
    * character <b>_</b>. If the rule is just to add a <b>s</b>, by example, like <b>1 cat, 2 cats</b>, just put after the
    * <b>_</b>, the letters to add. Example : <b>cat{3_s}</b>, means if the fourth number is under 2, the word will be
    * <b>cat</b> and id the fourth number is 2 or more, the word will become <b>cats</b>. But it exist some case or just add
    * letters is no enough, like <b>1 foot, 2 feet</b>. In this case, just put some <b>#</b> character after the <b>_</b>
    * followed by the letters to add. The number of last letter you want remove from the original word corresponds to the number
    * of <b>#</b> to put. By example, for <b>foot</b> become <b>feet</b>, we have to remove the 3 last letters (So have to put 3
    * <b>#</b>) then add letters <b>"eet"</b>, so have to write : <b>foot{2_###eet}</b>. That's means if third number is under
    * 2, <b>foot</b> is write, and if third number is 2 or over, <b>foot</b> is write, and 3 last letters are removed (Because
    * of the 3 <b>#</b>) and <b>"eet"</b> is add, so its become <b>feet</b>. Examples: <table border>
    * <tr>
    * <td><center><b>originalString</b></center></td>
    * <td><center><b>replacement</b></center></td>
    * <td><center><b>result</b></center></td>
    * </tr>
    * <tr>
    * <td>There {0} cat{0_s} and {1} foot{1_###eet}</td>
    * <td>1, 1</td>
    * <td>There 1 cat and 1 foot</td>
    * </tr>
    * <tr>
    * <td>There {0} cat{0_s} and {1} foot{1_###eet}</td>
    * <td>5,1</td>
    * <td>There 5 cats and 1 foot</td>
    * </tr>
    * <tr>
    * <td>There {0} cat{0_s} and {1} foot{1_###eet}</td>
    * <td>1,3</td>
    * <td>There 1 cat and 3 feet</td>
    * </tr>
    * <tr>
    * <td>There {0} cat{0_s} and {1} foot{1_###eet}</td>
    * <td>5, 3</td>
    * <td>There 5 cats and 3 feet</td>
    * </tr>
    * </table>
    *
    * @param originalString
    *           String with "holes"
    * @param values
    *           List of values for "holes". First evaluate hole {0}, second {1}, third {2} ,....
    * @return Result string with replacement
    */
   public static String replaceSeveral(final String originalString, int... values)
   {
      StringBuffer stringBuffer;
      int start;
      int indexOpenHole;
      int indexCloseHole;
      int indexUnderscore;
      int indexReplacement;
      String several;

      if(originalString == null)
      {
         throw new NullPointerException("The originalString musn't be null !");
      }

      if(values == null)
      {
         values = new int[0];
      }

      stringBuffer = new StringBuffer();

      start = 0;
      indexOpenHole = originalString.indexOf('{');
      while(indexOpenHole >= 0)
      {
         indexCloseHole = originalString.indexOf('}', indexOpenHole + 1);
         if(indexCloseHole >= 0)
         {
            stringBuffer.append(originalString.substring(start, indexOpenHole));

            indexReplacement = -1;
            several = originalString.substring(indexOpenHole + 1, indexCloseHole);
            try
            {
               indexReplacement = Integer.parseInt(several);
               if((indexReplacement >= 0) && (indexReplacement < values.length))
               {
                  stringBuffer.append(values[indexReplacement]);
               }
            }
            catch(final Exception exception)
            {
               indexUnderscore = several.indexOf('_');
               if(indexUnderscore >= 0)
               {
                  try
                  {
                     indexReplacement = Integer.parseInt(several.substring(0, indexUnderscore));
                     if((indexReplacement >= 0) && (indexReplacement < values.length) && (values[indexReplacement] > 1))
                     {
                        indexUnderscore++;
                        while(several.charAt(indexUnderscore) == '#')
                        {
                           stringBuffer.delete(stringBuffer.length() - 1, stringBuffer.length());
                           indexUnderscore++;
                        }
                        stringBuffer.append(several.substring(indexUnderscore));
                     }
                  }
                  catch(final Exception exception1)
                  {
                     // Nothing to do, just ignore this error
                  }
               }
            }

            start = indexCloseHole + 1;
            indexOpenHole = originalString.indexOf('{', start);
         }
         else
         {
            indexOpenHole = -1;
         }
      }
      stringBuffer.append(originalString.substring(start));

      return stringBuffer.toString();
   }

   /**
    * Replace all white characters by a given characters
    *
    * @param text
    *           Text where replace white characters
    * @param c
    *           Character used for replacement
    * @return Modified string
    */
   public static String replaceWhiteCharactersBy(final String text, final char c)
   {
      final char[] array = text.toCharArray();
      final int length = array.length;

      for(int i = 0; i < length; i++)
      {
         if(array[i] <= 32)
         {
            array[i] = c;
         }
      }

      return new String(array);
   }

   /**
    * Resolve image reference for class and external protocols.<br>
    * Class protocol aim to get resources embed near a class, the idea is to give the class and relative path of image from the
    * class. Syntax :
    * <table border=1>
    * <tr>
    * <td><code>class:&lt;classCompleteName&gt;/&lt;relativePath&gt;</code></td>
    * </tr>
    * </table>
    * .<br>
    * External link give possibility to get image relatively of directory where running jar is. So you can deploy application
    * and resource without care what is the absolute path. Syntax :
    * <table border=1>
    * <tr>
    * <td><code>external:&lt;relativePathFromDirectoryJar&gt;</code></td>
    * </tr>
    * </table>
    * <br>
    * Other protocols with absolute path (file:..., jar:..., ...) or url (http:..., https:..., ...) are not modified and copy as
    * they are
    *
    * @param html
    *           HTML text to resolve
    * @return Resolved text
    */
   @SuppressWarnings("rawtypes")
   public static String resolveImagesLinkInHTML(String html)
   {
      int length = html.length();
      final StringBuilder stringBuilder = new StringBuilder(length + (length >> 3));

      Matcher matcher = UtilText.IMAGE_TAG_CLASS_REFERENCE.matcher(html);
      int start = 0;
      int end = length;
      String className;
      String path;
      Class claz;

      while(matcher.find() == true)
      {
         end = matcher.start();

         if(start < end)
         {
            stringBuilder.append(html.substring(start, end));
         }

         stringBuilder.append(matcher.group(1));
         className = matcher.group(2);
         path = matcher.group(3);

         try
         {
            claz = Class.forName(className);
            stringBuilder.append(claz.getResource(path.substring(1)));
         }
         catch(final Exception exception)
         {
            Debug.printException(exception, "Failed to resolve resource class=", className, " path=", path);
            stringBuilder.append("file:");
            stringBuilder.append(path);
         }

         start = matcher.end();
      }

      if(start < length)
      {
         stringBuilder.append(html.substring(start));
      }

      html = stringBuilder.toString();
      stringBuilder.delete(0, stringBuilder.length());
      length = html.length();
      start = 0;
      end = length;
      matcher = UtilText.IMAGE_TAG_EXTERNAL_REFERENCE.matcher(html);

      while(matcher.find() == true)
      {
         end = matcher.start();

         if(start < end)
         {
            stringBuilder.append(html.substring(start, end));
         }

         stringBuilder.append(matcher.group(1));
         stringBuilder.append("file:");
         stringBuilder.append(UtilIO.obtainExternalFile(matcher.group(2)).getAbsolutePath());
         start = matcher.end();
      }

      if(start < length)
      {
         stringBuilder.append(html.substring(start));
      }

      return stringBuilder.toString();
   }

   /**
    * Convert a byte array to hexadecimal representation
    *
    * @param array
    *           Array to convert
    * @return Hexadecimal representation
    */
   public static String toHexadecimal(final byte[] array)
   {
      final int length = array.length;
      final char[] chars = new char[length * 2];

      int indexWrite = 0;
      int b;

      for(int indexRead = 0; indexRead < length; indexRead++)
      {
         b = array[indexRead] & 0xFF;

         chars[indexWrite++] = Integer.toHexString((b >> 4) & 0xF).charAt(0);
         chars[indexWrite++] = Integer.toHexString(b & 0xF).charAt(0);
      }

      return new String(chars);
   }

   /**
    * Convert string to UTF-8 array
    *
    * @param string
    *           String to convert
    * @return Converted string
    */
   public static byte[] toUTF8(final String string)
   {
      return string.getBytes(UtilText.UTF8);
   }

   /**
    * Generic trim to string, it permits to remove any character at start at at end of a string.<br>
    * It does like {@link String#trim()} except remove white characters it remove given list of characters
    *
    * @param stringToTrim
    *           String to trim
    * @param charactersToRemove
    *           List of characters to remove
    * @return Trimmed string
    */
   public static String trim(final String stringToTrim, final String charactersToRemove)
   {
      final char[] characters = stringToTrim.toCharArray();
      final char[] toRemove = charactersToRemove.toCharArray();
      int start = 0;
      int end = characters.length;

      // head
      while(start < end)
      {
         if(Utilities.contains(characters[start], toRemove) == false)
         {
            break;
         }

         start++;
      }

      // tail
      while(start < end)
      {
         if(Utilities.contains(characters[end - 1], toRemove) == false)
         {
            break;
         }

         end--;
      }

      // If no change, returns the string itself
      if((start == 0) && (end == characters.length))
      {
         return stringToTrim;
      }

      return stringToTrim.substring(start, end);
   }

   /**
    * Compute the upper case version of character, and remove all accent.
    *
    * @param character
    *           Character to upper case
    * @return Upper case result
    */
   public static char upperCaseWithoutAccent(final char character)
   {
      return UtilText.removeAccent(Character.toUpperCase(character));
   }

   /**
    * Compute the upper case version of string, and remove all accent.
    *
    * @param text
    *           Text to upper case
    * @return Upper case result
    */
   public static String upperCaseWithoutAccent(final String text)
   {
      return UtilText.removeAccent(text.toUpperCase());
   }

   /** To avoid instance */
   private UtilText()
   {
   }
}