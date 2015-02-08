package jhelp.util.image.gif;

import java.io.IOException;
import java.io.InputStream;

import jhelp.util.debug.Debug;
import jhelp.util.debug.DebugLevel;
import jhelp.util.list.Pair;

/**
 * Image descriptor block<br>
 * Images' colors are referenced via their index in color table.<br>
 * Moreover the indexes list is compress in a derived algorithm of LZW.<br>
 * An image can be interlaced or not.<br>
 * 
 * @see <a href="http://www.w3.org/Graphics/GIF/spec-gif89a.txt">GIF specification</a>
 * @author JHelp
 */
class ImageDescriptorBlock
      extends Block
{
   /** Jump between row to do if interlaced image */
   private static final int[] passJump       =
                                             {
         8, 8, 4, 2
                                             };
   /** Start y for each interlaced pass */
   private static final int[] passStart      =
                                             {
         0, 4, 2, 1
                                             };
   /** Actual read bit position */
   private int                bitPos         = 0;
   /** Actual buffered 32 bits */
   private int                buffer32;
   /** Image well ordered, uncompressed color indexes */
   private int[]              colorIndexes;
   /** Image color resolution */
   private final int          colorResoultion;
   /** Image width */
   private int                height;
   /** Indicates if image is interlaced */
   private boolean            interlaced;
   /** Indicates that we reach the last block */
   private boolean            lastBlockFound = false;
   /** Local image color table */
   private GIFColorTable      localColorTable;
   /** Next byte index to read in current block */
   private int                nextByte       = 4;
   /** Pass current index */
   private int                passIndex      = 0;
   /** Pixel index where write */
   private int                pix            = 0;
   /** Image width */
   private int                width;
   /** Image x */
   private int                x;
   /** Current write pixel X */
   private int                xx             = 0;
   /** Image Y */
   private int                y;
   /** Current pixel write Y */
   private int                yy             = 0;

   /**
    * Create a new instance of ImageDescriptorBlock
    * 
    * @param colorResoultion
    *           Global color resolution
    */
   ImageDescriptorBlock(final int colorResoultion)
   {
      this.colorResoultion = colorResoultion;
   }

   /**
    * Read the next code and next block couple
    * 
    * @param codeSize
    *           Size of code
    * @param codeMask
    *           Code mask
    * @param inputStream
    *           Stream to read
    * @param subBlock
    *           Current block to read
    * @param endCode
    *           Code for terminate
    * @return Next code and next block pair
    * @throws IOException
    *            If stream reach end or close suddenly
    */
   private Pair<Integer, SubBlock> getCode(final int codeSize, final int codeMask, final InputStream inputStream, SubBlock subBlock, final int endCode)
         throws IOException
   {
      if((this.bitPos + codeSize) > 32)
      {
         return new Pair<Integer, SubBlock>(endCode, subBlock); // No more data available
      }

      final int code = (this.buffer32 >> this.bitPos) & codeMask;
      this.bitPos += codeSize;
      int blockLength = subBlock.getSize();
      byte[] block = subBlock.getData();

      // Shift in a byte of new data at a time
      while((this.bitPos >= 8) && (this.lastBlockFound == false))
      {
         this.buffer32 >>>= 8;
         this.bitPos -= 8;

         // Check if current block is out of bytes
         if(this.nextByte >= blockLength)
         {
            // Get next block size
            subBlock = SubBlock.read(inputStream);
            if(subBlock == SubBlock.EMPTY)
            {
               this.lastBlockFound = true;
               return new Pair<Integer, SubBlock>(code, subBlock);
            }
            else
            {
               blockLength = subBlock.getSize();
               block = subBlock.getData();
               this.nextByte = 0;
            }
         }

         this.buffer32 |= block[this.nextByte++] << 24;
      }

      return new Pair<Integer, SubBlock>(code, subBlock);
   }

   /**
    * Initialize LZW table
    * 
    * @param prefix
    *           Prefixes to initialize
    * @param suffix
    *           Suffixes to initialize
    * @param initial
    *           Initial values to initialize
    * @param length
    *           Code lengths to initialize
    * @param lzwCode
    *           Read LZW code
    */
   private void initializeStringTable(final int[] prefix, final byte[] suffix, final byte[] initial, final int[] length, final int lzwCode)
   {
      final int numEntries = 1 << lzwCode;
      for(int i = 0; i < numEntries; i++)
      {
         prefix[i] = -1;
         suffix[i] = (byte) i;
         initial[i] = (byte) i;
         length[i] = 1;
      }

      // Fill in the entire table for robustness against
      // out-of-sequence codes.
      for(int i = numEntries; i < 4096; i++)
      {
         prefix[i] = -1;
         length[i] = 1;
      }
   }

   /**
    * Read the image data
    * 
    * @param inputStream
    *           Stream to read
    * @throws IOException
    *            If stream is not valid image data
    */
   private void readImage(final InputStream inputStream) throws IOException
   {
      final int lzwCode = inputStream.read();

      if(lzwCode < 0)
      {
         throw new IOException("No enough data to read LZW code");
      }

      this.colorIndexes = new int[this.width * this.height];
      SubBlock subBlock = SubBlock.read(inputStream);

      if(subBlock == SubBlock.EMPTY)
      {
         return;
      }

      subBlock.getSize();
      byte[] data = subBlock.getData();

      if(data.length < 4)
      {
         // To avoid some malformed GIF
         return;
      }

      this.buffer32 = (data[0] & 0xFF) | ((data[1] & 0xFF) << 8) | ((data[2] & 0xFF) << 16) | ((data[3] & 0xFF) << 24);
      final int clearCode = 1 << lzwCode;
      final int endCode = clearCode + 1;
      int code, oldCode = 0;
      final int[] prefix = new int[4096];
      final byte[] suffix = new byte[4096];
      final byte[] initial = new byte[4096];
      final int[] length = new int[4096];
      final byte[] string = new byte[4096];
      this.initializeStringTable(prefix, suffix, initial, length, lzwCode);
      int tableIndex = (1 << lzwCode) + 2;
      int codeSize = lzwCode + 1;
      int codeMask = (1 << codeSize) - 1;
      Pair<Integer, SubBlock> pair = new Pair<Integer, SubBlock>(0, subBlock);

      while(pair.element1 != endCode)
      {
         subBlock.getSize();
         data = subBlock.getData();

         pair = this.getCode(codeSize, codeMask, inputStream, subBlock, endCode);
         code = pair.element1;
         subBlock = pair.element2;

         if(code == clearCode)
         {
            this.initializeStringTable(prefix, suffix, initial, length, lzwCode);
            tableIndex = (1 << lzwCode) + 2;
            codeSize = lzwCode + 1;
            codeMask = (1 << codeSize) - 1;

            pair = this.getCode(codeSize, codeMask, inputStream, subBlock, endCode);
            code = pair.element1;
            subBlock = pair.element2;
            if(code == endCode)
            {
               return;
            }
         }
         else if(code == endCode)
         {
            return;
         }
         else
         {
            int newSuffixIndex;
            if(code < tableIndex)
            {
               newSuffixIndex = code;
            }
            else
            { // code == tableIndex
               newSuffixIndex = oldCode;
               if(code != tableIndex)
               {
                  // warning - code out of sequence
                  // possibly data corruption
                  Debug.println(DebugLevel.WARNING, "Out-of-sequence code!");
               }
            }

            final int ti = tableIndex;
            final int oc = oldCode;

            prefix[ti] = oc;
            suffix[ti] = initial[newSuffixIndex];
            initial[ti] = initial[oc];
            length[ti] = length[oc] + 1;

            tableIndex++;
            if((tableIndex == (1 << codeSize)) && (tableIndex < 4096))
            {
               codeSize++;
               codeMask = (1 << codeSize) - 1;
            }
         }

         // Reverse code
         int c = code;
         final int len = length[c];
         for(int i = len - 1; i >= 0; i--)
         {
            string[i] = suffix[c];
            c = prefix[c];
         }

         this.writeImage(string, len);
         oldCode = code;
      }
   }

   /**
    * Write in correct order uncompressed indexes
    * 
    * @param data
    *           Uncompressed indexes
    * @param length
    *           Data length
    */
   private void writeImage(final byte[] data, final int length)
   {
      final int size = this.colorIndexes.length;

      for(int i = 0; (i < length) && (this.pix < size); i++)
      {
         this.colorIndexes[this.pix] = data[i] & 0xFF;

         this.xx++;
         this.pix++;

         if(this.xx >= this.width)
         {
            this.xx = 0;

            if(this.interlaced == true)
            {
               this.yy += ImageDescriptorBlock.passJump[this.passIndex];

               if(this.yy >= this.height)
               {
                  this.passIndex = Math.min(3, this.passIndex + 1);
                  this.yy = ImageDescriptorBlock.passStart[this.passIndex];
               }

               this.pix = this.yy * this.width;
            }
            else
            {
               this.yy++;
            }
         }
      }
   }

   /**
    * Read image descriptor block <br>
    * <br>
    * <b>Parent documentation:</b><br>
    * {@inheritDoc}
    * 
    * @param inputStream
    *           Stream to read
    * @throws IOException
    *            If stream is not a valid image descriptor block
    * @see jhelp.util.image.gif.Block#read(java.io.InputStream)
    */
   @Override
   protected void read(final InputStream inputStream) throws IOException
   {
      this.x = UtilGIF.read2ByteInt(inputStream);
      this.y = UtilGIF.read2ByteInt(inputStream);
      this.width = UtilGIF.read2ByteInt(inputStream);
      this.height = UtilGIF.read2ByteInt(inputStream);
      final int flags = inputStream.read();

      if(flags < 0)
      {
         throw new IOException("No enough data for have image flags");
      }

      final boolean colorTableFollow = (flags & GIFConstants.MASK_COLOR_TABLE_FOLLOW) != 0;
      this.interlaced = (flags & GIFConstants.MASK_IMAGE_INTERLACED) != 0;
      final boolean ordered = (flags & GIFConstants.MASK_COLOR_TABLE_ORDERED) != 0;
      final int localTablSize = 1 << ((flags & GIFConstants.MASK_GLOABAL_COLOR_TABLE_SIZE) + 1);

      if(colorTableFollow == true)
      {
         this.localColorTable = new GIFColorTable(this.colorResoultion, ordered, localTablSize);
         this.localColorTable.read(inputStream);
      }

      this.readImage(inputStream);
   }

   /**
    * Uncompressed and ordered color indexes
    * 
    * @return Uncompressed and ordered color indexes
    */
   public int[] getColorIndexes()
   {
      return this.colorIndexes;
   }

   /**
    * Image height
    * 
    * @return Image height
    */
   public int getHeight()
   {
      return this.height;
   }

   /**
    * Local color table
    * 
    * @return Local color table or {@code null} if have to use global color table
    */
   public GIFColorTable getLocalColorTable()
   {
      return this.localColorTable;
   }

   /**
    * Image width
    * 
    * @return Image width
    */
   public int getWidth()
   {
      return this.width;
   }

   /**
    * Image X
    * 
    * @return Image X
    */
   public int getX()
   {
      return this.x;
   }

   /**
    * Image Y
    * 
    * @return Image Y
    */
   public int getY()
   {
      return this.y;
   }
}