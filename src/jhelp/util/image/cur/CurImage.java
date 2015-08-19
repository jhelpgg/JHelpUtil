package jhelp.util.image.cur;

import java.io.IOException;
import java.io.InputStream;

import jhelp.util.image.bmp.BitmapHeader;
import jhelp.util.io.UtilIO;

/**
 * Cursor image
 * 
 * @author JHelp
 */
public class CurImage
{
   /** Cursor image elements */
   private final CurElementImage[] curElementImages;

   /**
    * Create a new instance of CurImage
    * 
    * @param inputStream
    *           Stream to parse
    * @throws IOException
    *            On reading issue
    */
   public CurImage(final InputStream inputStream)
         throws IOException
   {
      int info = BitmapHeader.read2bytes(inputStream);

      if(info != 0)
      {
         throw new IOException("First 2 bytes MUST be 0, not " + info);
      }

      info = BitmapHeader.read2bytes(inputStream);

      if(info != 2)
      {
         // throw new IOException("type MUST be 2, not " + info);
      }

      final int length = BitmapHeader.read2bytes(inputStream);
      this.curElementImages = new CurElementImage[length];

      // Just jump rest of header
      UtilIO.skip(inputStream, length << 4);

      for(int i = 0; i < length; i++)
      {
         this.curElementImages[i] = new CurElementImage(inputStream);
      }
   }

   /**
    * One cursor element
    * 
    * @param index
    *           Element index
    * @return Element
    */
   public CurElementImage getElement(final int index)
   {
      return this.curElementImages[index];
   }

   /**
    * Number of elements
    * 
    * @return Number of elements
    */
   public int numberOfElement()
   {
      return this.curElementImages.length;
   }
}