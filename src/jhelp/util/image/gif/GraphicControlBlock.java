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
 * Graphical control block extension<br>
 * 
 * @see <a href="http://www.w3.org/Graphics/GIF/spec-gif89a.txt">GIF specification</a>
 * @author JHelp
 */
class GraphicControlBlock
      extends BlockExtension
{
   /** Disposal method */
   private int  disposalMethod;
   /** Image duration time in milliseconds */
   private long time;
   /** Transparency index */
   private int  transparencyIndex;

   /**
    * Create a new instance of GraphicControlBlock
    */
   GraphicControlBlock()
   {
   }

   /**
    * Read stream to extract block information <br>
    * <br>
    * <b>Parent documentation:</b><br>
    * {@inheritDoc}
    * 
    * @param inputStream
    *           Stream to read
    * @throws IOException
    *            If stream not contains valid data for graphic control block extension
    * @see jhelp.util.image.gif.Block#read(java.io.InputStream)
    */
   @Override
   protected void read(final InputStream inputStream) throws IOException
   {
      final int size = inputStream.read();

      if(size != 4)
      {
         throw new IOException("Size of graphic control MUST be 4, not " + size);
      }

      final int flags = inputStream.read();

      if(flags < 0)
      {
         throw new IOException("No enough data for read flags of graphic control block");
      }

      this.disposalMethod = (flags & GIFConstants.MASK_DISPOSAL_METHOD) >> GIFConstants.SHIFT_DISPOSAL_METHOD;
      final boolean transparencyIndexGiven = (flags & GIFConstants.MASK_TRANSPARENCY_GIVEN) != 0;

      this.time = 10L * UtilGIF.read2ByteInt(inputStream);
      this.transparencyIndex = inputStream.read();

      if(!transparencyIndexGiven)
      {
         this.transparencyIndex = -1;
      }

      // Consume the block terminator
      //noinspection ResultOfMethodCallIgnored
      inputStream.read();
   }

   /**
    * Disposal method
    * 
    * @return Disposal method
    */
   public int getDisposalMethod()
   {
      return this.disposalMethod;
   }

   /**
    * Duration time in milliseconds
    * 
    * @param defaultTime
    *           Default time if time was not set
    * @return Duration time in milliseconds
    */
   public long getTime(final long defaultTime)
   {
      if(this.time == 0)
      {
         return defaultTime;
      }

      return this.time;
   }

   /**
    * Transparency color index
    * 
    * @return Transparency color index, or -1 to indicates no transparent index
    */
   public int getTransparencyIndex()
   {
      return this.transparencyIndex;
   }
}