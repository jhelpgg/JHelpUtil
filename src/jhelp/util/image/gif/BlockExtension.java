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
package jhelp.util.image.gif;

import java.io.IOException;
import java.io.InputStream;

/**
 * Block that a GIF extension block (New in 89a from 87a)<br>
 * 
 * @see <a href="http://www.w3.org/Graphics/GIF/spec-gif89a.txt">GIF specification</a>
 * @author JHelp
 */
abstract class BlockExtension
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
   static BlockExtension readBlockExtention(final InputStream inputStream) throws IOException
   {
      BlockExtension blockExtention = null;
      final int      subType        = inputStream.read();

      switch(subType)
      {
         case BLOCK_EXTENSION_GRAPHIC_CONTROL:
            blockExtention = new GraphicControlBlock();
         break;
         case BLOCK_EXTENSION_COMMENT:
            blockExtention = new CommentBlock();
         break;
         case BLOCK_EXTENSION_PLAIN_TEXT:
            blockExtention = new PlainTextBlock();
         break;
         case BLOCK_EXTENSION_APPLICATION:
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