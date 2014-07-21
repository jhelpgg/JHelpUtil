package jhelp.util.text;

public class StringParser
{
   private final char[] characters;
   private int          index;
   private final int    length;

   public StringParser(final String string)
   {
      this.characters = string.toCharArray();
      this.index = 0;
      this.length = this.characters.length;
   }

   public void advanceIndexWhileCharacterIn(final String skipped)
   {
      while((this.index < this.length) && (skipped.indexOf(this.characters[this.index]) >= 0))
      {
         this.index++;
      }
   }

   public int getIndex()
   {
      return this.index;
   }

   public int getLength()
   {
      return this.length;
   }

   public boolean hasNext()
   {
      return this.index < this.length;
   }

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

   public void setIndex(final int index)
   {
      this.index = Math.max(0, Math.min(index, this.length));
   }

   @Override
   public String toString()
   {
      return UtilText.concatenate(this.index, " : ", this.characters);
   }
}