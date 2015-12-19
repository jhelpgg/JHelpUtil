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

import java.awt.Dimension;

import jhelp.util.Utilities;
import jhelp.util.gui.JHelpImage;

/**
 * Generic alphabet based on one big image with all managed characters draw on it.<br>
 * Each character have same width and same height.<br>
 * They are regularly spaces
 * 
 * @author JHelp
 */
public abstract class AlphabetUnique
      extends Alphabet
{
   /** Base big image */
   private JHelpImage imageBase;

   /**
    * Create a new instance of AlphabetUnique
    * 
    * @param characterWidth
    *           Character with
    * @param characterHeight
    *           Character height
    * @param caseSensitive
    *           Indicates if have low and up case characters ({@code true}) OR only up case characters ({@code false})
    */
   public AlphabetUnique(final int characterWidth, final int characterHeight, final boolean caseSensitive)
   {
      super(characterWidth, characterHeight, caseSensitive);
   }

   /**
    * Order of characters inside big image.<br>
    * Characters are described from left to right, up to bottom
    * 
    * @return Order of characters inside big image
    */
   protected abstract char[] charactersOrder();

   /**
    * Create image for a character.<br>
    * It take from big image the part corresponding to the given character <br>
    * <br>
    * <b>Parent documentation:</b><br>
    * {@inheritDoc}
    * 
    * @param character
    *           Character to have its image
    * @return Character's image OR {@code null} if character not defined
    * @see Alphabet#createImageFor(char)
    */
   @Override
   protected final JHelpImage createImageFor(final char character)
   {
      if(this.imageBase == null)
      {
         this.imageBase = AlphabetResources.RESOURCES.obtainJHelpImage(this.resourcePath());
      }

      final int index = Utilities.indexOf(this.charactersOrder(), character);

      if(index < 0)
      {
         return null;
      }

      final Dimension dimension = this.getCharacterDimension();
      final int numberPerLine = this.numberCharactersPerLine();
      final int space = this.spaceBetwwenCharacters();
      final int x = (index % numberPerLine) * (dimension.width + space);
      final int y = (index / numberPerLine) * dimension.height;

      return this.imageBase.extractSubImage(x, y, dimension.width, dimension.height);
   }

   /**
    * Number of character per line in big image
    * 
    * @return Number of character per line in big image
    */
   protected abstract int numberCharactersPerLine();

   /**
    * Big image resource path
    * 
    * @return Big image resource path
    */
   protected abstract String resourcePath();

   /**
    * Space between characters in big image
    * 
    * @return Space between characters in big image
    */
   protected abstract int spaceBetwwenCharacters();
}