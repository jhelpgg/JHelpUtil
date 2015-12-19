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
 * Block ignored appear on some malformed GIF<br>
 * 
 * @author JHelp
 */
class IgnoreBlock
      extends Block
{
   /** Ignore block single instance */
   static final IgnoreBlock IGNORE_BLOCK = new IgnoreBlock();

   /**
    * Create a new instance of IgnoreBlock
    */
   private IgnoreBlock()
   {
   }

   /**
    * Read the block <br>
    * <br>
    * <b>Parent documentation:</b><br>
    * {@inheritDoc}
    * 
    * @param inputStream
    *           Stream to read
    * @throws IOException
    *            Never happen
    * @see jhelp.util.image.gif.Block#read(java.io.InputStream)
    */
   @Override
   protected void read(final InputStream inputStream) throws IOException
   {
   }
}