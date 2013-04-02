package jhelp.util.io;

import java.io.File;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.FileImageInputStream;

import jhelp.util.debug.Debug;
import jhelp.util.list.EnumerationIterator;
import jhelp.util.text.UtilText;

/**
 * Describes information about a image file
 * 
 * @author JHelp
 */
public class FileImageInformation
{
   /** JVM known readers */
   private static final ImageReader[] IMAGES_READERS;

   static
   {
      final ArrayList<ImageReader> imageReaders = new ArrayList<ImageReader>();

      final String[] suffixs =
      {
         "JPG", "PNG", "GIF", "BMP"
      };

      for(final String suffix : suffixs)
      {
         for(final ImageReader imageReader : new EnumerationIterator<ImageReader>(ImageIO.getImageReadersBySuffix(suffix)))
         {
            imageReaders.add(imageReader);
         }
      }

      IMAGES_READERS = imageReaders.toArray(new ImageReader[imageReaders.size()]);
   }

   /** Image file */
   private final File                 file;
   /** Image height */
   private int                        height;
   /** Image width */
   private int                        width;

   /**
    * Create a new instance of FileImageInformation
    * 
    * @param file
    *           Image file
    */
   public FileImageInformation(final File file)
   {
      this.file = file;
      this.width = this.height = -1;

      try
      {
         FileImageInputStream fileInputStream;

         for(final ImageReader imageReader : FileImageInformation.IMAGES_READERS)
         {
            fileInputStream = null;

            try
            {
               fileInputStream = new FileImageInputStream(file);
               imageReader.setInput(fileInputStream, false, false);
               final int nb = imageReader.getNumImages(true);

               for(int i = 0; i < nb; i++)
               {
                  this.width = Math.max(this.width, imageReader.getWidth(i));
                  this.height = Math.max(this.height, imageReader.getHeight(i));
               }

               break;
            }
            catch(final Exception exception)
            {
            }
            finally
            {
               if(fileInputStream != null)
               {
                  try
                  {
                     fileInputStream.close();
                  }
                  catch(final Exception exception)
                  {
                  }
               }
            }
         }
      }
      catch(final Exception exception)
      {
         Debug.printException(exception);
      }
   }

   /**
    * Image file
    * 
    * @return Image file
    */
   public File getFile()
   {
      return this.file;
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
    * Image width
    * 
    * @return Image width
    */
   public int getWidth()
   {
      return this.width;
   }

   /**
    * String representation <br>
    * <br>
    * <b>Parent documentation:</b><br>
    * {@inheritDoc}
    * 
    * @return String representation
    * @see java.lang.Object#toString()
    */
   @Override
   public String toString()
   {
      return UtilText.concatenate(this.file.getAbsolutePath(), " : ", this.width, "x", this.height);
   }
}