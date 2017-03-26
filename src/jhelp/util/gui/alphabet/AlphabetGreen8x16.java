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
 * Alphabet green letters 8x16 pixels each character
 * 
 * @author JHelp
 */
public class AlphabetGreen8x16
      extends AlphabetUnique
{
   /** Order are letters inside main image */
   private static final char[]           CHARACTERS_ORDER   =
                                                            {
         ' ', '!', '"', '#', '$', '&', '%', '\'', '(', ')', '*', '+', ',', '-', '.', '/',//
         '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', ':', ';', '<', '=', '>', '?',//
         '@', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O',//
         'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', '[', '\\', ']', '^', '_',//
         ' ', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o',//
         'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '{', '|', '}', '~', '`',//
         '@', '®', Alphabet.INFINITE
                                                            };
   /** Alphabet green 8x16 singleton instance */
   public static final AlphabetGreen8x16 ALPHABET_GREEN8X16 = new AlphabetGreen8x16();

   /**
    * Create a new instance of AlphabetGreen8x16
    */
   private AlphabetGreen8x16()
   {
      super(8, 16, true);
   }

   /**
    * Order of letters inside big image <br>
    * <br>
    * <b>Parent documentation:</b><br>
    * {@inheritDoc}
    * 
    * @return Order of letters inside big image
    * @see AlphabetUnique#charactersOrder()
    */
   @Override
   protected char[] charactersOrder()
   {
      return AlphabetGreen8x16.CHARACTERS_ORDER;
   }

   /**
    * Number of characters in one line inside big image <br>
    * <br>
    * <b>Parent documentation:</b><br>
    * {@inheritDoc}
    * 
    * @return Number of characters in one line inside big image
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
      return "alphabetGreen8x16space8.png";
   }

   /**
    * Space between characters inside big image <br>
    * <br>
    * <b>Parent documentation:</b><br>
    * {@inheritDoc}
    * 
    * @return Space between characters inside big image
    * @see AlphabetUnique#spaceBetweenCharacters()
    */
   @Override
   protected int spaceBetweenCharacters()
   {
      return 8;
   }
}