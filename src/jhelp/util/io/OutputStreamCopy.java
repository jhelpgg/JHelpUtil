package jhelp.util.io;

import java.io.IOException;
import java.io.OutputStream;

/**
 * Output stream that write into two streams in same time
 * 
 * @author JHelp
 */
public class OutputStreamCopy
      extends OutputStream
{
   /** Output stream where copy whats write */
   private final OutputStream copy;
   /** Normal destination */
   private final OutputStream destination;

   /**
    * Create a new instance of OutputStreamCopy
    * 
    * @param destination
    *           Normal destination
    * @param copy
    *           Output stream where copy whats write
    */
   public OutputStreamCopy(final OutputStream destination, final OutputStream copy)
   {
      if(destination == null)
      {
         throw new NullPointerException("destination musn't be null");
      }

      if(copy == null)
      {
         throw new NullPointerException("copy musn't be null");
      }

      this.destination = destination;
      this.copy = copy;
   }

   /**
    * Close thre streams <br>
    * <br>
    * <b>Parent documentation:</b><br>
    * {@inheritDoc}
    * 
    * @throws IOException
    *            On writing issue
    * @see java.io.OutputStream#close()
    */
   @Override
   public void close() throws IOException
   {
      this.destination.close();
      this.copy.close();
   }

   /**
    * Flush the streams <br>
    * <br>
    * <b>Parent documentation:</b><br>
    * {@inheritDoc}
    * 
    * @throws IOException
    *            On writing issue
    * @see java.io.OutputStream#flush()
    */
   @Override
   public void flush() throws IOException
   {
      this.destination.flush();
      this.copy.flush();
   }

   /**
    * Write a byte array into streams <br>
    * <br>
    * <b>Parent documentation:</b><br>
    * {@inheritDoc}
    * 
    * @param b
    *           Byte array to write
    * @throws IOException
    *            On writing issues
    * @see java.io.OutputStream#write(byte[])
    */
   @Override
   public void write(final byte[] b) throws IOException
   {
      this.destination.write(b);
      this.copy.write(b);
   }

   /**
    * Write a part of a byte array <br>
    * <br>
    * <b>Parent documentation:</b><br>
    * {@inheritDoc}
    * 
    * @param b
    *           Byte array to write
    * @param off
    *           Index where start to read the byte array for writing
    * @param len
    *           Number of bytes of the byte array to write
    * @throws IOException
    *            On writing issue
    * @see java.io.OutputStream#write(byte[], int, int)
    */
   @Override
   public void write(final byte[] b, final int off, final int len) throws IOException
   {
      this.destination.write(b, off, len);
      this.copy.write(b, off, len);
   }

   /**
    * Write one byte <br>
    * <br>
    * <b>Parent documentation:</b><br>
    * {@inheritDoc}
    * 
    * @param b
    *           Byte to write
    * @throws IOException
    *            On wrinting issue
    * @see java.io.OutputStream#write(int)
    */
   @Override
   public void write(final int b) throws IOException
   {
      this.destination.write(b);
      this.copy.write(b);
   }
}