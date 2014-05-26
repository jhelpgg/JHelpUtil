package jhelp.util.gui;

import java.awt.Dimension;

/**
 * Image that represents the draw of an edit text
 * 
 * @author JHelp
 */
public final class JHelpEditTextImage
      extends JHelpImage
{
   /**
    * Compute the image size
    * 
    * @param font
    *           Font to use in edit text
    * @param size
    *           Number of letter in edit text
    * @return Computed size
    */
   private static Dimension computeDimension(final JHelpFont font, final int size)
   {
      final int width = (font.getMaximumCharacterWidth() * Math.max(size, 5)) + 6;
      final int height = font.getHeight() + 6;
      return new Dimension(width, height);
   }

   /**
    * Create an edit text image with a unique color for the text
    * 
    * @param font
    *           Font to use
    * @param size
    *           Number of letter in edit text
    * @param color
    *           Text color
    * @param colorCursor
    *           Cursor color
    * @return Created edit text image
    */
   public static JHelpEditTextImage createEditTextImage(final JHelpFont font, final int size, final int color, final int colorCursor)
   {
      if(font == null)
      {
         throw new NullPointerException("font musn't be null");
      }

      final Dimension dimension = JHelpEditTextImage.computeDimension(font, size);
      return new JHelpEditTextImage(dimension.width, dimension.height, font, color, null, null, colorCursor, Math.max(3, dimension.height >> 3), dimension.height);
   }

   /**
    * Create an edit text image with a texture for draw the text
    * 
    * @param font
    *           Font to use
    * @param size
    *           Number of letter in edit text
    * @param texture
    *           Texture use for text
    * @param colorCursor
    *           Cursor cursor
    * @return Created edit text image
    */
   public static JHelpEditTextImage createEditTextImage(final JHelpFont font, final int size, final JHelpImage texture, final int colorCursor)
   {
      if(font == null)
      {
         throw new NullPointerException("font musn't be null");
      }

      if(texture == null)
      {
         throw new NullPointerException("texture musn't be null");
      }

      final Dimension dimension = JHelpEditTextImage.computeDimension(font, size);
      return new JHelpEditTextImage(dimension.width, dimension.height, font, 0, JHelpImage.createResizedImage(texture, dimension.width, dimension.height), null, colorCursor, Math.max(3, dimension.height >> 3), dimension.height);
   }

   /**
    * Create an edit text image with paint for fill text
    * 
    * @param font
    *           Font to use
    * @param size
    *           Number of letter in edit text
    * @param paint
    *           Paint to use for fill the text
    * @param colorCursor
    *           Cursor color
    * @return Created edit text
    */
   public static JHelpEditTextImage createEditTextImage(final JHelpFont font, final int size, final JHelpPaint paint, final int colorCursor)
   {
      if(font == null)
      {
         throw new NullPointerException("font musn't be null");
      }

      if(paint == null)
      {
         throw new NullPointerException("paint musn't be null");
      }

      final Dimension dimension = JHelpEditTextImage.computeDimension(font, size);
      return new JHelpEditTextImage(dimension.width, dimension.height, font, 0, null, paint, colorCursor, Math.max(3, dimension.height >> 3), dimension.height);
   }

   /** Background color */
   private int              backgroundColor;
   /** Background paint */
   private JHelpPaint       backgroundPaint;
   /** Background texture */
   private JHelpImage       backgroundTexture;
   /** Text color */
   private final int        color;
   /** Cursor color */
   private final int        colorCursor;
   /** Cursor position */
   private int              cursor;
   /** Cursor height */
   private final int        cursorHeight;
   /** Cursor width */
   private final int        cursorWitdh;
   /** Font to use */
   private final JHelpFont  font;
   /** Text paint */
   private final JHelpPaint paint;
   /** Current text */
   private String           text;
   /** Text texture */
   private final JHelpImage texture;

   /**
    * Create a new instance of JHelpEditTextImage
    * 
    * @param width
    *           Image width
    * @param height
    *           Image height
    * @param font
    *           Font to use
    * @param color
    *           Text color (used if texture is {@code null} and paint is {@code null})
    * @param texture
    *           Text texture (used if not {@code null} and paint is {@code null})
    * @param paint
    *           Text paint (used if not {@code null})
    * @param colorCursor
    *           Cursor color
    * @param cursorWitdh
    *           Cursor width
    * @param cursorHeight
    *           Cursor height
    */
   private JHelpEditTextImage(final int width, final int height, final JHelpFont font, final int color, final JHelpImage texture, final JHelpPaint paint, final int colorCursor, final int cursorWitdh, final int cursorHeight)
   {
      super(width, height);

      this.font = font;
      this.color = color;
      this.texture = texture;
      this.paint = paint;
      this.colorCursor = colorCursor;
      this.cursorWitdh = cursorWitdh;
      this.cursorHeight = cursorHeight;
      this.backgroundColor = 0;
      this.backgroundPaint = null;
      this.backgroundTexture = null;
      this.text = "";
      this.cursor = 0;
      this.setText("", 0);
   }

   /**
    * Current cursor position.<br>
    * Negative value means cursor not visible
    * 
    * @return Cursor position or -1 if not visible
    */
   public int getCursor()
   {
      return this.cursor;
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
    * Change the background to unique color.<br>
    * The draw mode is automatically managed.<br>
    * You will retrieve the same state on enter and exit.<br>
    * Just remember to see result of being not in draw mode or refresh explicitly (see {@link #endDrawMode()} or
    * {@link #update()})
    * 
    * @param backgroundColor
    *           New background color
    */
   public void setBackgroundColor(final int backgroundColor)
   {
      this.backgroundColor = backgroundColor;
      this.backgroundPaint = null;
      this.backgroundTexture = null;
      this.setText(this.text, this.cursor);
   }

   /**
    * Change the background to a paint.<br>
    * The draw mode is automatically managed.<br>
    * You will retrieve the same state on enter and exit.<br>
    * Just remember to see result of being not in draw mode or refresh explicitly (see {@link #endDrawMode()} or
    * {@link #update()})
    * 
    * @param backgroundPaint
    *           New background paint
    */
   public void setBackgroundPaint(final JHelpPaint backgroundPaint)
   {
      if(backgroundPaint == null)
      {
         throw new NullPointerException("backgroundPaint musn't be null");
      }

      this.backgroundColor = 0;
      this.backgroundPaint = backgroundPaint;
      this.backgroundTexture = null;
      this.setText(this.text, this.cursor);
   }

   /**
    * Change the background to texture.<br>
    * The draw mode is automatically managed.<br>
    * You will retrieve the same state on enter and exit.<br>
    * Just remember to see result of being not in draw mode or refresh explicitly (see {@link #endDrawMode()} or
    * {@link #update()})
    * 
    * @param backgroundTexture
    *           New background texture
    */
   public void setBackgroundTexture(final JHelpImage backgroundTexture)
   {
      if(backgroundTexture == null)
      {
         throw new NullPointerException("backgroundTexture musn't be null");
      }

      this.backgroundColor = 0;
      this.backgroundPaint = null;
      this.backgroundTexture = JHelpImage.createResizedImage(backgroundTexture, this.getWidth(), this.getHeight());
      this.setText(this.text, this.cursor);
   }

   /**
    * Draw the text and the cursor.<br>
    * If the cursor position is negative, the cursor is not draw.<br>
    * The draw mode is automatically managed.<br>
    * You will retrieve the same state on enter and exit.<br>
    * Just remember to see result of being not in draw mode or refresh explicitly (see {@link #endDrawMode()} or
    * {@link #update()})
    * 
    * @param text
    *           Text to write
    * @param cursor
    *           Cursor position or -1 to hide the cursor
    */
   public void setText(final String text, int cursor)
   {
      if(text == null)
      {
         throw new NullPointerException("text musn't be null");
      }

      this.text = text;
      this.cursor = Math.max(-1, Math.min(text.length(), cursor));
      final boolean cursorVisible = cursor >= 0;
      cursor = Math.max(0, this.cursor);
      final JHelpMask mask = this.font.createMask(text);
      int cursorX = 0;

      if(cursor > 0)
      {
         cursorX = this.font.stringWidth(text.substring(0, cursor));
      }

      final boolean drawMode = this.isDrawMode();

      if(drawMode == false)
      {
         this.startDrawMode();
      }

      if(this.backgroundPaint != null)
      {
         this.fillRectangle(0, 0, this.getWidth(), this.getHeight(), this.backgroundPaint, false);
      }
      else if(this.backgroundTexture != null)
      {
         this.fillRectangle(0, 0, this.getWidth(), this.getHeight(), this.backgroundTexture, false);
      }
      else
      {
         this.clear(this.backgroundColor);
      }

      if(this.paint != null)
      {
         this.paintMask(3, 3, mask, this.paint, 0, true);
      }
      else if(this.texture != null)
      {
         this.paintMask(3, 3, mask, this.texture, 0, 0, 0, true);
      }
      else
      {
         this.paintMask(3, 3, mask, this.color, 0, true);
      }

      if(cursorVisible == true)
      {
         this.fillRectangle(cursorX + 3, 3, this.cursorWitdh, this.cursorHeight, this.colorCursor);
      }

      if(drawMode == false)
      {
         this.endDrawMode();
      }
   }
}