package jhelp.util.image.gif;

import java.io.IOException;
import java.io.InputStream;

/**
 * Comment extension block<br>
 * 
 * @see <a href="http://www.w3.org/Graphics/GIF/spec-gif89a.txt">GIF specification</a>
 * @author JHelp
 */
class CommentBlock
      extends BlockExtention
{
   /** Embed comment */
   private String comment;

   /**
    * Create a new instance of CommentBlock
    */
   CommentBlock()
   {
   }

   /**
    * Read the block information <br>
    * <br>
    * <b>Parent documentation:</b><br>
    * {@inheritDoc}
    * 
    * @param inputStream
    *           Stream to read
    * @throws IOException
    *            If stream contains invalid comment block extention data
    * @see jhelp.util.image.gif.Block#read(java.io.InputStream)
    */
   @Override
   protected void read(final InputStream inputStream) throws IOException
   {
      final StringBuilder stringBuilder = new StringBuilder();
      SubBlock subBlock = SubBlock.read(inputStream);

      while(subBlock != SubBlock.EMPTY)
      {
         UtilGIF.appendAsciiBytes(stringBuilder, subBlock.getData());
         subBlock = SubBlock.read(inputStream);
      }

      this.comment = stringBuilder.toString();
   }

   /**
    * Embed comment
    * 
    * @return Embed comment
    */
   public String getComment()
   {
      return this.comment;
   }
}