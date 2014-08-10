package jhelp.util.image.gif;

import java.io.IOException;
import java.io.InputStream;

import jhelp.util.Utilities;
import jhelp.util.io.ByteArray;
import jhelp.util.io.UtilIO;

/**
 * Application extension block <br>
 * 
 * @see <a href="http://www.w3.org/Graphics/GIF/spec-gif89a.txt">GIF specification</a>
 * @author JHelp
 */
class ApplicationBlock
      extends BlockExtention
{
   /** Application code */
   private final byte[]    applicationCode;
   /** Application specific data */
   private final ByteArray applicationData;
   /** Application identifier */
   private String          applicationIdentifier;

   /**
    * Create a new instance of ApplicationBlock
    */
   ApplicationBlock()
   {
      this.applicationData = new ByteArray();
      this.applicationCode = new byte[3];
   }

   /**
    * Read block from stream <br>
    * <br>
    * <b>Parent documentation:</b><br>
    * {@inheritDoc}
    * 
    * @param inputStream
    *           Stream to read
    * @throws IOException
    *            If stream contains invalid data for Application extension block
    * @see jhelp.util.image.gif.Block#read(java.io.InputStream)
    */
   @Override
   protected void read(final InputStream inputStream) throws IOException
   {
      final int size = inputStream.read();

      if(size != 11)
      {
         throw new IOException("Size of application MUST be 11, not " + size);
      }

      this.applicationIdentifier = UtilGIF.readString(8, inputStream);

      UtilIO.readStream(inputStream, this.applicationCode);

      SubBlock subBlock = SubBlock.read(inputStream);

      while(subBlock != SubBlock.EMPTY)
      {
         this.applicationData.write(subBlock.getData());
         subBlock = SubBlock.read(inputStream);
      }
   }

   /**
    * Application code
    * 
    * @return Application code
    */
   public byte[] getApplicationCode()
   {
      return Utilities.createCopy(this.applicationCode);
   }

   /**
    * Application specific data
    * 
    * @return Application specific data
    */
   public ByteArray getApplicationData()
   {
      return this.applicationData;
   }

   /**
    * Application identifier
    * 
    * @return Application identifier
    */
   public String getApplicationIdentifier()
   {
      return this.applicationIdentifier;
   }
}