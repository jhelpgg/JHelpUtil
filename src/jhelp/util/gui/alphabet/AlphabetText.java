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
import java.awt.geom.RoundRectangle2D;

import jhelp.util.gui.JHelpImage;
import jhelp.util.gui.JHelpPaint;
import jhelp.util.gui.JHelpTextAlign;
import jhelp.util.gui.Path;
import jhelp.util.gui.UtilImage;
import jhelp.util.list.ArrayInt;

/**
 * Text with alphabet
 * 
 * @author JHelp
 */
public class AlphabetText
{
   /** Border size */
   private static final int BORDER_SIZE       = 8;
   /** Half size of border */
   private static final int BORDER_SIZE_HALF  = AlphabetText.BORDER_SIZE >> 1;
   /** Double size of border */
   private static final int BORDER_SIZE_TWICE = AlphabetText.BORDER_SIZE << 1;

   /**
    * Compute the number of columns and lines that can be draw on given dimension.<br>
    * The number of columns are in the "width" of the result and number of lines in the "height"
    * 
    * @param alphabet
    *           Alphabet to get number off columns and lines
    * @param width
    *           Area limit width
    * @param height
    *           Area limit height
    * @return Number of columns (in width) and lines (in height)
    */
   public static Dimension obtainNumberColumnsLines(final Alphabet alphabet, final int width, final int height)
   {
      final Dimension dimension = alphabet.getCharacterDimension();
      return new Dimension((width - AlphabetText.BORDER_SIZE_TWICE) / dimension.width,//
            (height - AlphabetText.BORDER_SIZE_TWICE) / dimension.height);
   }

   /** Alphabet to draw */
   private final Alphabet       alphabet;
   /** Background color */
   private final int            backgroundColor;
   /** Background image */
   private final JHelpImage     backgroundImage;
   /** Background paint */
   private final JHelpPaint     backgroundPaint;
   /** Border color */
   private final int            borderColor;
   /** Border path */
   private final Path           borderPath;
   /** Alphabet text height */
   private final int            height;
   /** Embed image where alphabet text is draw */
   private final JHelpImage     image;
   /** Text length */
   private int                  length;
   /** Limited height */
   private final int            limitHeight;
   /** Limited width */
   private final int            limitWidth;
   /** Offset where start text */
   private int                  offset;
   /** Previous offsets */
   private final ArrayInt       previousOffsets;
   /** Text */
   private String               text;
   /** Text alignment */
   private final JHelpTextAlign textAlign;
   /** Width */
   private final int            width;
   /** X location */
   private final int            x;
   /** Y location */
   private final int            y;

   /**
    * Create a new instance of AlphabetText
    * 
    * @param alphabet
    *           Alphabet to use
    * @param numberCharacterPerLine
    *           Number of character per line
    * @param numberLine
    *           Number of lines
    * @param text
    *           Text to draw
    * @param textAlign
    *           Text alignment
    * @param borderColor
    *           Border color
    * @param backgroundColor
    *           Background color
    * @param backgroundImage
    *           Background image
    * @param backgroundPaint
    *           Background paint
    */
   private AlphabetText(final Alphabet alphabet, final int numberCharacterPerLine, final int numberLine, final String text, final JHelpTextAlign textAlign,
         final int borderColor, final int backgroundColor, final JHelpImage backgroundImage, final JHelpPaint backgroundPaint)
   {
      this.alphabet = alphabet;
      this.textAlign = textAlign;
      final Dimension dimension = this.alphabet.getCharacterDimension();
      this.limitWidth = Math.max(16, numberCharacterPerLine) * dimension.width;
      this.limitHeight = Math.max(1, numberLine) * dimension.height;
      this.x = AlphabetText.BORDER_SIZE_TWICE + 1;
      this.y = AlphabetText.BORDER_SIZE_TWICE + 1;
      this.width = this.limitWidth + (this.x << 1);
      this.height = this.limitHeight + (this.y << 1);
      this.image = new JHelpImage(this.width, this.height);
      this.borderColor = borderColor;
      this.backgroundColor = backgroundColor;
      this.backgroundImage = backgroundImage;
      this.backgroundPaint = backgroundPaint;
      this.previousOffsets = new ArrayInt();
      this.borderPath = new Path();
      final RoundRectangle2D roundRectangle2D = new RoundRectangle2D.Double(AlphabetText.BORDER_SIZE_HALF, AlphabetText.BORDER_SIZE_HALF, this.width
            - AlphabetText.BORDER_SIZE, this.height - AlphabetText.BORDER_SIZE, AlphabetText.BORDER_SIZE, AlphabetText.BORDER_SIZE);
      this.borderPath.append(roundRectangle2D);

      this.setText(text);
   }

   /**
    * Create a new instance of AlphabetText
    * 
    * @param alphabet
    *           Alphabet to use
    * @param numberCharacterPerLine
    *           Number of character per line
    * @param numberLine
    *           Number of lines
    * @param text
    *           Text to draw
    * @param textAlign
    *           Text alignment
    * @param borderColor
    *           Border color
    * @param backgroundColor
    *           Background color
    */
   public AlphabetText(final Alphabet alphabet, final int numberCharacterPerLine, final int numberLine, final String text, final JHelpTextAlign textAlign,
         final int borderColor, final int backgroundColor)
   {
      this(alphabet, numberCharacterPerLine, numberLine, text, textAlign, borderColor, backgroundColor, null, null);
   }

   /**
    * Create a new instance of AlphabetText
    * 
    * @param alphabet
    *           Alphabet to use
    * @param numberCharacterPerLine
    *           Number of character per line
    * @param numberLine
    *           Number of lines
    * @param text
    *           Text to draw
    * @param textAlign
    *           Text alignment
    * @param borderColor
    *           Border color
    * @param backgroundImage
    *           Background image
    */
   public AlphabetText(final Alphabet alphabet, final int numberCharacterPerLine, final int numberLine, final String text, final JHelpTextAlign textAlign,
         final int borderColor, final JHelpImage backgroundImage)
   {
      this(alphabet, numberCharacterPerLine, numberLine, text, textAlign, borderColor, 0, backgroundImage, null);
   }

   /**
    * Create a new instance of AlphabetText
    * 
    * @param alphabet
    *           Alphabet to use
    * @param numberCharacterPerLine
    *           Number of character per line
    * @param numberLine
    *           Number of lines
    * @param text
    *           Text to draw
    * @param textAlign
    *           Text alignment
    * @param borderColor
    *           Border color
    * @param backgroundPaint
    *           Background paint
    */
   public AlphabetText(final Alphabet alphabet, final int numberCharacterPerLine, final int numberLine, final String text, final JHelpTextAlign textAlign,
         final int borderColor, final JHelpPaint backgroundPaint)
   {
      this(alphabet, numberCharacterPerLine, numberLine, text, textAlign, borderColor, 0, null, backgroundPaint);
   }

   /**
    * Update the text
    */
   private void updateText()
   {
      synchronized(this.image)
      {
         this.image.startDrawMode();
         this.image.clear();

         if(this.backgroundImage != null)
         {
            this.image.fillRoundRectangle(0, 0, this.width, this.height, AlphabetText.BORDER_SIZE, AlphabetText.BORDER_SIZE, this.backgroundImage);
         }
         else if(this.backgroundPaint != null)
         {
            this.image.fillRoundRectangle(0, 0, this.width, this.height, AlphabetText.BORDER_SIZE, AlphabetText.BORDER_SIZE, this.backgroundPaint);
         }
         else
         {
            this.image.fillRoundRectangle(0, 0, this.width, this.height, AlphabetText.BORDER_SIZE, AlphabetText.BORDER_SIZE, this.backgroundColor);
         }

         this.image.drawNeon(this.borderPath, AlphabetText.BORDER_SIZE, this.borderColor, 0, 1);
         int xx = this.x;
         int yy = this.y;
         boolean center = false;

         if(this.textAlign == JHelpTextAlign.CENTER)
         {
            center = true;
            xx = this.image.getWidth() >> 1;
            yy = this.image.getHeight() >> 1;
         }

         this.offset = this.alphabet.drawOnLimited(this.offset, this.text, this.textAlign, this.image, xx, yy, center, this.limitWidth, this.limitHeight);

         if(this.hasPrevious())
         {
            UtilImage.drawIncrustedUpTriangle(this.image.getWidth() - AlphabetText.BORDER_SIZE_TWICE, AlphabetText.BORDER_SIZE, AlphabetText.BORDER_SIZE,
                  this.image);
         }

         if(this.hasNext())
         {
            UtilImage.drawIncrustedDownTriangle(this.image.getWidth() - AlphabetText.BORDER_SIZE_TWICE,
                  this.image.getHeight() - AlphabetText.BORDER_SIZE_TWICE, AlphabetText.BORDER_SIZE, this.image);
         }

         this.image.endDrawMode();
      }
   }

   /**
    * Image where alphabet text is draw
    * 
    * @return Image where alphabet text is draw
    */
   public JHelpImage getImage()
   {
      return this.image;
   }

   /**
    * Current text
    * 
    * @return Current text
    */
   public String getText()
   {
      return this.text;
   }

   /**
    * Indicates if text have next part
    * 
    * @return {@code true} if text have next part
    */
   public boolean hasNext()
   {
      return this.offset < this.length;
   }

   /**
    * Indicates if text have previous part
    * 
    * @return {@code true} if text have previous part
    */
   public boolean hasPrevious()
   {
      final int size = this.previousOffsets.getSize();

      return (this.offset > 0) && (size > 0) && (this.previousOffsets.getInteger(size - 1) > 0);
   }

   /**
    * Go next text part (If have one)
    */
   public void next()
   {
      if(!this.hasNext())
      {
         return;
      }

      this.previousOffsets.add(this.offset);
      this.updateText();
   }

   /**
    * Go previous text part (If have one)
    */
   public void previous()
   {
      if(!this.hasPrevious())
      {
         return;
      }

      final int size = this.previousOffsets.getSize();

      if(size > 1)
      {
         this.previousOffsets.remove(size - 1);
         this.offset = this.previousOffsets.getInteger(size - 2);
      }
      else
      {
         this.offset = 0;
         this.previousOffsets.add(0);
      }

      this.updateText();
   }

   /**
    * Change the text
    * 
    * @param text
    *           New text
    */
   public void setText(final String text)
   {
      this.text = text;
      this.offset = 0;
      this.previousOffsets.clear();
      this.previousOffsets.add(0);
      this.length = this.text.length();
      this.updateText();
   }
}