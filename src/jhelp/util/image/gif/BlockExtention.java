package jhelp.util.image.gif;

import java.io.IOException;
import java.io.InputStream;

/**
 * Block that a GIF extension block (New in 89a from 87a)<br>
 * 
 * @see <a href="http://www.w3.org/Graphics/GIF/spec-gif89a.txt">GIF specification</a>
 * @author JHelp
 */
abstract class BlockExtention
      extends Block
{
   /**
    * Read an extension block <br>
    * Note the type is already read and set
    * 
    * @param inputStream
    *           Stream to read
    * @return Read block
    * @throws IOException
    *            If stream not contains available sub-type
    */
   static BlockExtention readBlockExtention(final InputStream inputStream) throws IOException
   {
      BlockExtention blockExtention = null;
      final int subType = inputStream.read();

      switch(subType)
      {
         case BLOCK_EXTENTION_GRAPHIC_CONTROL:
            blockExtention = new GraphicControlBlock();
         break;
         case BLOCK_EXTENTION_COMMENT:
            blockExtention = new CommentBlock();
         break;
         case BLOCK_EXTENTION_PLAIN_TEXT:
            blockExtention = new PlainTextBlock();
         break;
         case BLOCK_EXTENTION_APPLICATION:
            blockExtention = new ApplicationBlock();
         break;
      }

      if(blockExtention == null)
      {
         throw new IOException("Invalid block extension sub type " + subType);
      }

      blockExtention.subType = subType;
      return blockExtention;
   }

   /** Block sub-type */
   private int subType;

   /**
    * Block sub-type
    * 
    * @return Block sub-type
    */
   public final int getSubType()
   {
      return this.subType;
   }
}