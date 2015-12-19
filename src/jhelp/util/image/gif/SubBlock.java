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

import jhelp.util.io.UtilIO;

/**
 * GIF sub-block<br>
 * 
 * @see <a href="http://www.w3.org/Graphics/GIF/spec-gif89a.txt">GIF specification</a>
 * @author JHelp
 */
class SubBlock
{
   /** The empty/terminator sub-block */
   static final SubBlock EMPTY = new SubBlock(0, new byte[0]);

   /**
    * Read next sub-block
    * 
    * @param inputStream
    *           Stream where read
    * @return Block read
    * @throws IOException
    *            If data not corresponds to a valid sub-block
    */
   static SubBlock read(final InputStream inputStream) throws IOException
   {
      final int size = inputStream.read();

      if(size < 0)
      {
         throw new IOException("Not enough data to read a SubBlock (No size !)");
      }

      if(size == 0)
      {
         return SubBlock.EMPTY;
      }

      final byte[] data = new byte[size];
      final int read = UtilIO.readStream(inputStream, data);

      if(read < size)
      {
         throw new IOException("Not enough data to read a SubBlock (read=" + read + ", size=" + size + ")");
      }

      return new SubBlock(size, data);
   }

   /** Data read */
   private final byte[] data;
   /**
    * A data sub-block may contain from 0 to 255 data bytes. The size of the block does not account for the size byte itself,
    * therefore, the empty sub-block is one whose size field contains 0x00
    */
   private final int    size;

   /**
    * Create a new instance of SubBlock
    * 
    * @param size
    *           Size
    * @param data
    *           Data
    */
   private SubBlock(final int size, final byte[] data)
   {
      this.size = size;
      this.data = data;
   }

   /**
    * Data
    * 
    * @return Data
    */
   public byte[] getData()
   {
      return this.data;
   }

   /**
    * Size
    * 
    * @return Size
    */
   public int getSize()
   {
      return this.size;
   }
}