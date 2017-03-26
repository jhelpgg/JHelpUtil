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
 * Generic GIF block <br>
 * 
 * @see <a href="http://www.w3.org/Graphics/GIF/spec-gif89a.txt">GIF specification</a>
 * @author JHelp
 */
abstract class Block
      implements GIFConstants
{
   /**
    * Read next block from stream
    * 
    * @param inputStream
    *           Stream to read
    * @param colorResolution
    *           Color resolution
    * @return Read block
    * @throws IOException
    *            If stream not contains a valid block
    */
   static Block readBlock(final InputStream inputStream, final int colorResolution) throws IOException
   {
      final int type = inputStream.read();

      if(type < 0)
      {
         throw new IOException("No block to read");
      }

      Block block = null;

      switch(type)
      {
         case BLOCK_IMAGE_DESCRIPTOR:
            block = new ImageDescriptorBlock(colorResolution);
         break;
         case BLOCK_EXTENSION:
            block = BlockExtension.readBlockExtention(inputStream);
         break;
         case BLOCK_END_GIF:
            block = EndBlock.END_BLOCK;
         break;
         case 0:
            block = IgnoreBlock.IGNORE_BLOCK;
         break;
      }

      if(block == null)
      {
         throw new IOException("Unknown block type : " + type);
      }

      block.type = type;
      block.read(inputStream);

      return block;
   }

   /** Block type */
   private int type;

   /**
    * Read block specific data.<br>
    * Note : the type and eventual sub-type are already read and set
    * 
    * @param inputStream
    *           Stream to read
    * @throws IOException
    *            If stream not contains valid data for the target block
    */
   protected abstract void read(InputStream inputStream) throws IOException;

   /**
    * Block type
    * 
    * @return Block type
    */
   public final int getType()
   {
      return this.type;
   }
}