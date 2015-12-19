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
package jhelp.util.gui.alphabet;

/**
 * Alphabet with 16x16 orange characters
 * 
 * @author JHelp
 */
public class AlphabetOrange16x16
      extends AlphabetUnique
{
   /** Big image characters order */
   private static final char[]             CHARACTERS_ORDER     =
                                                                {
         ' ', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O',//
         'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', '!', ',', '-', '.', '?',//
         '0', '1', '2', '3', '4', '5', '6', '7', '8', '9'
                                                                };

   /** Alphabet with 16x16 orange characters singleton */
   public static final AlphabetOrange16x16 ALPHABET_ORANGE16X16 = new AlphabetOrange16x16();

   /**
    * Create a new instance of AlphabetOrange16x16
    */
   private AlphabetOrange16x16()
   {
      super(16, 16, false);
   }

   /**
    * Big image characters order <br>
    * <br>
    * <b>Parent documentation:</b><br>
    * {@inheritDoc}
    * 
    * @return Big image characters order
    * @see AlphabetUnique#charactersOrder()
    */
   @Override
   protected char[] charactersOrder()
   {
      return AlphabetOrange16x16.CHARACTERS_ORDER;
   }

   /**
    * Number of characters in line inside big image <br>
    * <br>
    * <b>Parent documentation:</b><br>
    * {@inheritDoc}
    * 
    * @return Number of characters in line inside big image
    * @see AlphabetUnique#numberCharactersPerLine()
    */
   @Override
   protected int numberCharactersPerLine()
   {
      return 16;
   }

   /**
    * Big image resource path <br>
    * <br>
    * <b>Parent documentation:</b><br>
    * {@inheritDoc}
    * 
    * @return Big image resource path
    * @see AlphabetUnique#resourcePath()
    */
   @Override
   protected String resourcePath()
   {
      return "alphabetOrange16x16.png";
   }

   /**
    * Space between characters inside big image <br>
    * <br>
    * <b>Parent documentation:</b><br>
    * {@inheritDoc}
    * 
    * @return Space between characters inside big image
    * @see AlphabetUnique#spaceBetwwenCharacters()
    */
   @Override
   protected int spaceBetwwenCharacters()
   {
      return 0;
   }
}