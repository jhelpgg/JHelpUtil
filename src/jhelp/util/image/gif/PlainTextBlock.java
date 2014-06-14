package jhelp.util.image.gif;

import java.io.IOException;
import java.io.InputStream;

/**
 * Plain text extension block<br>
 * 
 * @see <a href="http://www.w3.org/Graphics/GIF/spec-gif89a.txt">GIF specification</a>
 * @author JHelp
 */
class PlainTextBlock
      extends BlockExtention
{
   /** Background color index */
   private int    backgroundIndex;
   /** Cell height */
   private int    cellHeight;
   /** Cell width */
   private int    cellWidth;
   /** Foreground color index */
   private int    foregroundIndex;
   /** Grid height */
   private int    gridHeight;
   /** Grid width */
   private int    gridWidth;
   /** Grid X */
   private int    gridX;
   /** Grid Y */
   private int    gridY;
   /** Text to print */
   private String text;

   /**
    * Create a new instance of PlainTextBlock
    */
   PlainTextBlock()
   {
   }

   /**
    * Read the plain text extension block <br>
    * <br>
    * <b>Parent documentation:</b><br>
    * {@inheritDoc}
    * 
    * @param inputStream
    *           Stream to read
    * @throws IOException
    *            If data aren't a valid plain text entension block
    * @see jhelp.util.image.gif.Block#read(java.io.InputStream)
    */
   @Override
   protected void read(final InputStream inputStream) throws IOException
   {
      final int size = inputStream.read();

      if(size != 12)
      {
         throw new IOException("Size of plain text MUST be 12, not " + size);
      }

      this.gridX = UtilGIF.read2ByteInt(inputStream);
      this.gridY = UtilGIF.read2ByteInt(inputStream);
      this.gridWidth = UtilGIF.read2ByteInt(inputStream);
      this.gridHeight = UtilGIF.read2ByteInt(inputStream);
      this.cellWidth = inputStream.read();
      this.cellHeight = inputStream.read();
      this.foregroundIndex = inputStream.read();
      this.backgroundIndex = inputStream.read();

      final StringBuilder stringBuilder = new StringBuilder();
      SubBlock subBlock = SubBlock.read(inputStream);

      while(subBlock != SubBlock.EMPTY)
      {
         UtilGIF.appendAsciiBytes(stringBuilder, subBlock.getData());
         subBlock = SubBlock.read(inputStream);
      }

      this.text = stringBuilder.toString();
   }

   /**
    * Background color index
    * 
    * @return Background color index
    */
   public int getBackgroundIndex()
   {
      return this.backgroundIndex;
   }

   /**
    * Cell height
    * 
    * @return Cell height
    */
   public int getCellHeight()
   {
      return this.cellHeight;
   }

   /**
    * Cell width
    * 
    * @return Cell width
    */
   public int getCellWidth()
   {
      return this.cellWidth;
   }

   /**
    * Foreground color index
    * 
    * @return Foreground color index
    */
   public int getForegroundIndex()
   {
      return this.foregroundIndex;
   }

   /**
    * Grid height
    * 
    * @return Grid height
    */
   public int getGridHeight()
   {
      return this.gridHeight;
   }

   /**
    * Grid width
    * 
    * @return Grid width
    */
   public int getGridWidth()
   {
      return this.gridWidth;
   }

   /**
    * Grid X
    * 
    * @return Grid X
    */
   public int getGridX()
   {
      return this.gridX;
   }

   /**
    * Grind Y
    * 
    * @return Grid Y
    */
   public int getGridY()
   {
      return this.gridY;
   }

   /**
    * Text to print
    * 
    * @return Text to print
    */
   public String getText()
   {
      return this.text;
   }
}