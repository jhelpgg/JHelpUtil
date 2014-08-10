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