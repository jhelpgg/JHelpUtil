package jhelp.util.gui.alphabet;

import java.awt.Dimension;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import jhelp.util.gui.JHelpImage;
import jhelp.util.gui.JHelpTextAlign;
import jhelp.util.text.StringCutter;
import jhelp.util.text.UtilText;

/**
 * Generic alphabet for draw text
 * 
 * @author JHelp
 */
public abstract class Alphabet
{
   /** Character used for "foot print" (if exists in alphabet) */
   public static final char                     ANIMAL_FOOT_PRINT = 'ù';
   /** Character used for "cancel" (if exists in alphabet) */
   public static final char                     CANCEL            = '×';
   /** Character used for "delete back" (if exists in alphabet) */
   public static final char                     DELETE_BACK       = '≤';
   /** Character used for "end" (if exists in alphabet) */
   public static final char                     END               = '€';
   /** Character used for "female symbol" (if exists in alphabet) */
   public static final char                     FEMALE            = '£';
   /** Character used for "hart" (if exists in alphabet) */
   public static final char                     HART              = 'ð';
   /** Character used for "infinite" (if exists in alphabet) */
   public static final char                     INFINITE          = 'œ';
   /** Character used for "male symbol" (if exists in alphabet) */
   public static final char                     MALE              = 'µ';
   /** Character used for "return" (if exists in alphabet) */
   public static final char                     RETURN            = '¡';
   /**
    * Indicates if alphabet is case sensitive.<br>
    * That is to say if alphabet have upper and lower case or only one of them
    */
   private final boolean                        caseSensitive;
   /** Character height */
   private final int                            characterHeight;
   /** Map of character and associated image */
   private final HashMap<Character, JHelpImage> characters;
   /** Character width */
   private final int                            characterWidth;

   /**
    * Create a new instance of Alphabet
    * 
    * @param characterWidth
    *           Character width
    * @param characterHeight
    *           Character height
    * @param caseSensitive
    *           Indicates if alphabet case sensitive
    */
   public Alphabet(final int characterWidth, final int characterHeight, final boolean caseSensitive)
   {

      this.characterWidth = characterWidth;
      this.characterHeight = characterHeight;
      this.caseSensitive = caseSensitive;
      this.characters = new HashMap<Character, JHelpImage>();
   }

   /**
    * Draw alphabet images on image
    * 
    * @param lines
    *           Lines to draw
    * @param x
    *           X location
    * @param y
    *           Y location
    * @param width
    *           Width
    * @param height
    *           Height
    * @param textAlign
    *           Text alignment
    * @param image
    *           Image where draw
    * @param center
    *           Indicates if have to center text
    */
   private void drawOn(final List<JHelpImage[]> lines, final int x, final int y, final int width, final int height, final JHelpTextAlign textAlign,
         final JHelpImage image, final boolean center)
   {
      int xx = x;
      int yy = y;

      if(center == true)
      {
         xx -= width >> 1;
         yy -= height >> 1;
      }

      int startX;

      for(final JHelpImage[] lineImages : lines)
      {
         startX = xx;

         switch(textAlign)
         {
            case LEFT:
            break;
            case CENTER:
               startX = xx + ((width - (lineImages.length * this.characterWidth)) >> 1);
            break;
            case RIGHT:
               startX = xx + (width - (lineImages.length * this.characterWidth));
            break;
         }

         for(final JHelpImage characterImage : lineImages)
         {
            image.drawImage(startX, yy, characterImage);
            startX += this.characterWidth;
         }

         yy += this.characterHeight;
      }
   }

   /**
    * Create image for given character
    * 
    * @param character
    *           Character to have its image
    * @return Created image
    */
   protected abstract JHelpImage createImageFor(char character);

   /**
    * Compute text dimension
    * 
    * @param text
    *           Text
    * @return Text dimension
    */
   public final Dimension computeTextDimension(final String text)
   {
      final StringCutter stringCutter = new StringCutter(text, '\n');
      String line = stringCutter.next();
      int width = 0;
      int height = 0;

      while(line != null)
      {
         width = Math.max(width, line.length());
         height++;
         line = stringCutter.next();
      }

      return new Dimension(width * this.characterWidth, height * this.characterHeight);
   }

   /**
    * Draw text on image
    * 
    * @param text
    *           Text to draw
    * @param textAlign
    *           Text alignment
    * @param image
    *           Image where draw
    * @param x
    *           X coordinate in image
    * @param y
    *           Y coordinate in image
    */
   public final void drawOn(final String text, final JHelpTextAlign textAlign, final JHelpImage image, final int x, final int y)
   {
      this.drawOn(text, textAlign, image, x, y, false);
   }

   /**
    * Draw text on image
    * 
    * @param text
    *           Text to draw
    * @param textAlign
    *           Text alignment
    * @param image
    *           Image where draw
    * @param x
    *           X coordinate in image
    * @param y
    *           Y coordinate in image
    * @param center
    *           Indicates if given (x, y) is the center of text ({@code true}) OR the up-left corner ({@code false})
    */
   public final void drawOn(final String text, final JHelpTextAlign textAlign, final JHelpImage image, final int x, final int y, final boolean center)
   {
      final List<JHelpImage[]> lines = new ArrayList<JHelpImage[]>();
      final StringCutter stringCutter = new StringCutter(text, '\n');
      int width = 0;
      int height = 0;
      JHelpImage[] images;
      String line = stringCutter.next();

      while(line != null)
      {
         images = this.getImagesFor(line);
         lines.add(images);
         width = Math.max(width, images.length * this.characterWidth);
         height += this.characterHeight;

         line = stringCutter.next();
      }

      this.drawOn(lines, x, y, width, height, textAlign, image, center);
   }

   /**
    * Draw text with alphabet on limited area
    * 
    * @param offset
    *           Text offset where start
    * @param text
    *           Text to draw
    * @param textAlign
    *           Text alignment
    * @param image
    *           Image where draw
    * @param x
    *           X
    * @param y
    *           Y
    * @param center
    *           Center the text
    * @param limitWidth
    *           Limit width
    * @param limitHeight
    *           Limit height
    * @return Offset end
    */
   public final int drawOnLimited(int offset, final String text, final JHelpTextAlign textAlign, final JHelpImage image, final int x, final int y,
         final boolean center, final int limitWidth, final int limitHeight)
   {
      final List<JHelpImage[]> lines = new ArrayList<JHelpImage[]>();
      final char[] characters = text.toCharArray();
      final int length = characters.length;
      offset = Math.max(0, offset);

      if(offset >= length)
      {
         return length;
      }

      int width = 0;
      int height = 0;
      int xx = 0;
      final List<JHelpImage> line = new ArrayList<JHelpImage>();
      char character;

      while(((height + this.characterHeight) <= limitHeight) && (offset < length))
      {
         character = characters[offset];

         if(character >= ' ')
         {
            line.add(this.getImageFor(character));
            xx += this.characterWidth;
            width = Math.max(xx, width);
         }

         if(((xx + this.characterWidth) > limitWidth) || (character < ' '))
         {
            lines.add(line.toArray(new JHelpImage[line.size()]));
            line.clear();
            xx = 0;
            height += this.characterHeight;
         }

         offset++;
      }

      if(line.isEmpty() == false)
      {
         lines.add(line.toArray(new JHelpImage[line.size()]));
         height += this.characterHeight;
      }

      this.drawOn(lines, x, y, width, height, textAlign, image, center);

      return offset;
   }

   /**
    * Draw text with alphabet on limited area
    * 
    * @param offset
    *           Text offset where start
    * @param text
    *           Text to draw
    * @param textAlign
    *           Text alignment
    * @param image
    *           Image where draw
    * @param x
    *           X
    * @param y
    *           Y
    * @param limitWidth
    *           Limit width
    * @param limitHeight
    *           Limit height
    * @return Offset end
    */
   public final int drawOnLimited(final int offset, final String text, final JHelpTextAlign textAlign, final JHelpImage image, final int x, final int y,
         final int limitWidth, final int limitHeight)
   {
      return this.drawOnLimited(offset, text, textAlign, image, x, y, false, limitWidth, limitHeight);
   }

   /**
    * Draw text with alphabet on limited area
    * 
    * @param text
    *           Text to draw
    * @param textAlign
    *           Text alignment
    * @param image
    *           Image where draw
    * @param x
    *           X
    * @param y
    *           Y
    * @param center
    *           Center the text
    * @param limitWidth
    *           Limit width
    * @param limitHeight
    *           Limit height
    * @return Offset end
    */
   public final int drawOnLimited(final String text, final JHelpTextAlign textAlign, final JHelpImage image, final int x, final int y, final boolean center,
         final int limitWidth, final int limitHeight)
   {
      return this.drawOnLimited(0, text, textAlign, image, x, y, center, limitWidth, limitHeight);
   }

   /**
    * Draw text with alphabet on limited area
    * 
    * @param text
    *           Text to draw
    * @param textAlign
    *           Text alignment
    * @param image
    *           Image where draw
    * @param x
    *           X
    * @param y
    *           Y
    * @param limitWidth
    *           Limit width
    * @param limitHeight
    *           Limit height
    * @return Offset end
    */
   public final int drawOnLimited(final String text, final JHelpTextAlign textAlign, final JHelpImage image, final int x, final int y, final int limitWidth,
         final int limitHeight)
   {
      return this.drawOnLimited(0, text, textAlign, image, x, y, false, limitWidth, limitHeight);
   }

   /**
    * One character size
    * 
    * @return One character size
    */
   public final Dimension getCharacterDimension()
   {
      return new Dimension(this.characterWidth, this.characterHeight);
   }

   /**
    * Compute image for one character
    * 
    * @param character
    *           Character to have its image
    * @return Character image
    */
   public final JHelpImage getImageFor(char character)
   {
      if(character <= 32)
      {
         return JHelpImage.DUMMY;
      }

      if(this.caseSensitive == false)
      {
         character = UtilText.upperCaseWithoutAccent(character);
      }

      JHelpImage image = this.characters.get(character);

      if(image == null)
      {
         image = this.createImageFor(character);

         if(image == null)
         {
            image = JHelpImage.DUMMY;
         }

         this.characters.put(character, image);
      }

      return image;
   }

   /**
    * Compute images (One per character) for a given text
    * 
    * @param text
    *           Text to have its images
    * @return Computed images
    */
   public final JHelpImage[] getImagesFor(final String text)
   {
      if((text == null) || (text.length() == 0))
      {
         return new JHelpImage[0];
      }

      final char[] characters = text.toCharArray();
      final int size = characters.length;
      final JHelpImage[] images = new JHelpImage[size];

      for(int i = 0; i < size; i++)
      {
         images[i] = this.getImageFor(characters[i]);
      }

      return images;
   }

   /**
    * Indicates if alphabet case sensitive
    * 
    * @return {@code true} if alphabet case sensitive
    */
   public final boolean isCaseSensitive()
   {
      return this.caseSensitive;
   }
}