package jhelp.util.image.gif;

import java.io.IOException;
import java.io.InputStream;

/**
 * Block that end GIF stream<br>
 * 
 * @see <a href="http://www.w3.org/Graphics/GIF/spec-gif89a.txt">GIF specification</a>
 * @author JHelp
 */
class EndBlock
      extends Block
{
   /** End block single instance */
   static final EndBlock END_BLOCK = new EndBlock();

   /**
    * Create a new instance of EndBlock
    */
   private EndBlock()
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