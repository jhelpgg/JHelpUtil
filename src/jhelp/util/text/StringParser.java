package jhelp.util.text;

/**
 * Helper for parse Strings.<br>
 * Its possible :
 * <ul>
 * <li>To place parser cursor/index anywhere in the String</li>
 * <li>Know exactly where the parser cursor/index is</li>
 * <li>Skipped a group of characters</li>
 * </ul>
 * 
 * @author JHelp
 */
public class StringParser
{
   /** String to parse characters */
   private final char[] characters;
   /** Current parse cursor/index */
   private int          index;
   /** String length */
   private final int    length;

   /**
    * Create a new instance of StringParser.<br>
    * Cursor/index parse place at string start
    * 
    * @param string
    *           String to parse
    */
   public StringParser(final String string)
   {
      this.characters = string.toCharArray();
      this.index = 0;
      this.length = this.characters.length;
   }

   /**
    * Advanced to cursor/index parse while it meet one of characters inside the given String
    * 
    * @param skipped
    *           String contains list of characters to skip
    */
   public void advanceIndexWhileCharacterIn(final String skipped)
   {
      while((this.index < this.length) && (skipped.indexOf(this.characters[this.index]) >= 0))
      {
         this.index++;
      }
   }

   /**
    * Current cursor/index parse
    * 
    * @return Current cursor/index parse
    */
   public int getIndex()
   {
      return this.index;
   }

   /**
    * String to parse length
    * 
    * @return String to parse length
    */
   public int getLength()
   {
      return this.length;
   }

   /**
    * Indicates if it left at least one character to parse
    * 
    * @return {@code true} if it left at least one character to parse
    */
   public boolean hasNext()
   {
      return this.index < this.length;
   }

   /**
    * Read the next character.
    * 
    * @return Next character
    * @throws IllegalStateException
    *            If cursor/index is a the end of the String
    */
   public char readNext()
   {
      if(this.index >= this.length)
      {
         throw new IllegalStateException("The end of string is reach");
      }

      final char character = this.characters[this.index];
      this.index++;

      return character;
   }

   /**
    * Read a number of characters.<br>
    * If it left not enough character to read a shorter String is returned
    * 
    * @param count
    *           Number of desired characters read
    * @return String read
    */
   public String readNext(int count)
   {
      count = Math.max(0, Math.min(this.length - this.index, count));

      if(count < 1)
      {
         return "";
      }

      final String string = new String(this.characters, this.index, count);
      this.index += count;

      return string;
   }

   /**
    * Change cursor/index parse position
    * 
    * @param index
    *           New cursor/index parse position
    */
   public void setIndex(final int index)
   {
      this.index = Math.max(0, Math.min(index, this.length));
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
      return UtilText.concatenate(this.index, " : ", this.characters);
   }
}