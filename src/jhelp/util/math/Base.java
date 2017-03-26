package jhelp.util.math;

import jhelp.util.Utilities;
import jhelp.util.text.UtilText;

/**
 * Represents any base number.<br>
 * Only the character '-' is reserved for negative numbers. Others characters can be used has digit<br>
 * This implementation is case sensitive
 * 
 * @author JHelp
 */
public class Base
{
   /**
    * Buffer size , the maximum number of digit for a long is in base 2 : 1(negative sign)+16(number of bytes for long)*8(byte
    * size in base 2)
    */
   private static final int BUFFER_SIZE = 1 + (16 * 8);
   /** Base number */
   private final int        base;
   /** Digits to use, first represents '0', second '1', ... */
   private final char[]     baseDigits;
   /** Buffer used in {@link #convert(long)} (to avoid create new one each time) */
   private final char[]     buffer;

   /**
    * Create a new instance of Base with given digits.<br>
    * Must have at least 2 characters.<br>
    * The character - (minus sign) not allowed since it reserved for negative numbers<br>
    * Each character MUST be unique, duplicate same character is an error.<br>
    * First character corresponds at 0, second at 1, ...
    * 
    * @param baseDigits
    *           Digits symbols
    */
   public Base(final char... baseDigits)
   {
      this.base = baseDigits.length;

      if(this.base < 2)
      {
         throw new IllegalArgumentException("Base must have at least 2 represents digits");
      }

      if(Utilities.indexOf(baseDigits, '-') >= 0)
      {
         throw new IllegalArgumentException("The - (minus sign) MUST NOT be used as digit");
      }

      for(int i = 0; i < this.base; i++)
      {
         if(Utilities.indexOf(baseDigits, baseDigits[i]) != i)
         {
            throw new IllegalArgumentException(UtilText.concatenate("Duplicate symbols are forbidden in list, but found at least two '", baseDigits[i],
                  "' inside ", baseDigits));
         }
      }

      this.baseDigits = Utilities.createCopy(baseDigits);
      this.buffer = new char[Base.BUFFER_SIZE];
   }

   /**
    * Create a new instance of Base more standard, it use first [0-9] then [a-z], then [A-Z], then characters after 128
    * 
    * @param base
    *           Base size
    */
   public Base(final int base)
   {
      this(base, true);
   }

   /**
    * Create a new instance of Base more standard, it use first [0-9] then you can choose have [a-z] before or after [A-Z]
    * 
    * @param base
    *           Base size
    * @param lowerCaseBeforeUpperCase
    *           Indicates if just after [0-9] will found lower case ({@code true}) or upper case ({@code false})
    */
   public Base(int base, final boolean lowerCaseBeforeUpperCase)
   {
      if(base < 2)
      {
         throw new IllegalArgumentException("Base must have at least 2 represents digits");
      }

      this.base = base;
      this.baseDigits = new char[base];
      this.buffer = new char[Base.BUFFER_SIZE];
      final int limit = Math.min(base, 10);

      for(int i = 0; i < limit; i++)
      {
         this.baseDigits[i] = (char) ('0' + i);
      }

      if(base > 10)
      {
         base -= 10;

         if(lowerCaseBeforeUpperCase)
         {
            this.addLowerCaseLetterAt(10, base);
         }
         else
         {
            this.addUpperCaseLetterAt(10, base);
         }

         if(base > 26)
         {
            base -= 26;

            if(lowerCaseBeforeUpperCase)
            {
               this.addUpperCaseLetterAt(36, base);
            }
            else
            {
               this.addLowerCaseLetterAt(36, base);
            }

            if(base > 26)
            {
               base -= 26;

               for(int i = 0; i < base; i++)
               {
                  this.baseDigits[i + 62] = (char) (128 + i);
               }
            }
         }
      }
   }

   /**
    * Create a new instance of Base given String contains the digits to use in order.<br>
    * Must have at least 2 characters.<br>
    * The character - (minus sign) not allowed since it reserved for negative numbers<br>
    * Each character MUST be unique, duplicate same character is an error.<br>
    * First character corresponds at 0, second at 1, ...
    * 
    * @param string
    *           Digits to use
    */
   public Base(final String string)
   {
      this(string.toCharArray());
   }

   /**
    * Add lower case letters in digits
    * 
    * @param offset
    *           Offset where write
    * @param number
    *           Number of character to write at maximum
    */
   private void addLowerCaseLetterAt(final int offset, final int number)
   {
      final int limit = Math.min(number, 26);

      for(int i = 0; i < limit; i++)
      {
         this.baseDigits[i + offset] = (char) ('a' + i);
      }
   }

   /**
    * Add upper case letters in digits
    * 
    * @param offset
    *           Offset where write
    * @param number
    *           Number of character to write at maximum
    */
   private void addUpperCaseLetterAt(final int offset, final int number)
   {
      final int limit = Math.min(number, 26);

      for(int i = 0; i < limit; i++)
      {
         this.baseDigits[i + offset] = (char) ('A' + i);
      }
   }

   /**
    * Convert number to its string representation
    * 
    * @param number
    *           Number to convert
    * @return String representation
    */
   public String convert(long number)
   {
      final boolean negative = number < 0;

      if(negative)
      {
         number *= -1;
      }

      int index = Base.BUFFER_SIZE;
      int length = 0;

      do
      {
         index--;
         this.buffer[index] = this.baseDigits[(int) (number % this.base)];
         length++;
         number /= this.base;
      }
      while(number > 0);

      if(negative)
      {
         index--;
         this.buffer[index] = '-';
         length++;
      }

      return new String(this.buffer, index, length);
   }

   /**
    * Base number
    * 
    * @return Base number
    */
   public int getBase()
   {
      return this.base;
   }

   /**
    * Obtain a digit used by the base
    * 
    * @param index
    *           Digit index
    * @return Digit
    */
   public char getDigit(final int index)
   {
      return this.baseDigits[index];
   }

   /**
    * Obtain all digits used in order
    * 
    * @return All digits
    */
   public char[] getDigits()
   {
      return Utilities.createCopy(this.baseDigits);
   }

   /**
    * Indicates if given character is a digit
    * 
    * @param character
    *           Character tested
    * @return {@code true} if given character is a digit
    */
   public boolean isDigit(final char character)
   {
      return Utilities.contains(character, this.baseDigits);
   }

   /**
    * Parse a String to have its value
    * 
    * @param string
    *           String to parse
    * @return Value computed
    * @throws IllegalArgumentException
    *            If given String not valid for the base
    */
   public long parse(final String string)
   {
      final char[] characters = string.toCharArray();
      final int length = characters.length;

      if(length == 0)
      {
         throw new IllegalArgumentException("Can't parse empty string");
      }

      final boolean negative = characters[0] == '-';
      int start = 0;

      if(negative)
      {
         if(length == 1)
         {
            throw new IllegalArgumentException("Negative numbers MUST have at least one character after the - (minus sign)");
         }

         start = 1;
      }

      long number = 0;
      int digit;

      for(int index = start; index < length; index++)
      {
         digit = Utilities.indexOf(this.baseDigits, characters[index]);

         if(digit < 0)
         {
            throw new IllegalArgumentException("Given string have at least one invalid character at " + index + " given string : " + string);
         }

         number = (number * this.base) + digit;
      }

      if(negative)
      {
         return -number;
      }

      return number;
   }
}