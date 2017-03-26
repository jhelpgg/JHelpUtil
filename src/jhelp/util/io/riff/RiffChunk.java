package jhelp.util.io.riff;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import jhelp.util.image.bmp.BitmapHeader;
import jhelp.util.io.ByteArray;
import jhelp.util.io.UtilIO;

/**
 * RIFF generic chunk
 * 
 * @author JHelp
 */
public class RiffChunk
{
   /** Byte buffer of 4 bytes */
   private final static byte[] BUFFER_4 = new byte[4];
   /** List of chunk name */
   public static final String  LIST     = "LIST";
   /** Main RIFF chunk name */
   public static final String  RIFF     = "RIFF";

   /**
    * Read a 4 bytes name from stream
    * 
    * @param inputStream
    *           Stream to read
    * @return Name read
    * @throws IOException
    *            On reading issue
    */
   public static String read4charactersName(final InputStream inputStream) throws IOException
   {
      UtilIO.readStream(inputStream, RiffChunk.BUFFER_4);
      final char[] characters = new char[4];

      for(int i = 0; i < 4; i++)
      {
         characters[i] = (char) (RiffChunk.BUFFER_4[i] & 0XFF);
      }

      return new String(characters);
   }

   /**
    * Read ASCII string from stream
    * 
    * @param inputStream
    *           Stream to read
    * @return ASCII String
    * @throws IOException
    *            On reading issue
    */
   public static String readAsciiString(final InputStream inputStream) throws IOException
   {
      int size = inputStream.available();
      final byte[] buffer = new byte[size];
      size = UtilIO.readStream(inputStream, buffer);
      final char[] characters = new char[size];

      for(int i = 0; i < size; i++)
      {
         characters[i] = (char) (RiffChunk.BUFFER_4[i] & 0XFF);
      }

      return new String(characters);
   }

   /** Chunk children */
   private final List<RiffChunk> children;
   /** Chunk data */
   private byte[]                data;
   /** Data size */
   private int                   dataSize;
   /** Chunk ID */
   private final String          id;
   /** Name of list if chunk is a list */
   private String                listName;

   /**
    * Create a new instance of RiffChunk
    * 
    * @param inputStream
    *           Stream to read
    * @throws IOException
    *            On reading issue
    */
   public RiffChunk(final InputStream inputStream)
         throws IOException
   {
      this.children = new ArrayList<RiffChunk>();
      this.id = RiffChunk.read4charactersName(inputStream);
      this.dataSize = BitmapHeader.read4bytes(inputStream);
      this.data = new byte[this.dataSize];
      UtilIO.readStream(inputStream, this.data);

      if((this.dataSize & 1) != 0)
      {
         // Consume 1 byte to be sure have read even bytes
         //noinspection ResultOfMethodCallIgnored
         inputStream.read();
      }

      if((RiffChunk.RIFF.equals(this.id)) || (RiffChunk.LIST.equals(this.id)))
      {
         this.parseDataAsListOfChunk();
      }
   }

   /**
    * Number of children
    * 
    * @return Number of children
    */
   public int chunkCount()
   {
      return this.children.size();
   }

   /**
    * Transform chunk data to stream
    * 
    * @return Stream for read data
    */
   public InputStream dataAsStream()
   {
      final ByteArray byteArray = new ByteArray();
      byteArray.write(this.data);
      return byteArray.getInputStream();
   }

   /**
    * Obtain a chunk child
    * 
    * @param index
    *           Child index
    * @return Chunk at index
    */
   public RiffChunk getChunk(final int index)
   {
      return this.children.get(index);
   }

   /**
    * Chunk ID
    * 
    * @return Chunk ID
    */
   public String getID()
   {
      return this.id;
   }

   /**
    * List name, if chunk is a list of chunks.<br>
    * If chunk not a list it is {@code null}
    * 
    * @return List name, if chunk is a list of chunks OR {@code null} if not a list
    */
   public String listName()
   {
      return this.listName;
   }

   /**
    * Parse chuck data as list of chunks.<br>
    * The most case it is automatically called, public for very special cases
    * 
    * @throws IOException
    *            On reading issue
    */
   public void parseDataAsListOfChunk() throws IOException
   {
      if(this.dataSize == 0)
      {
         return;
      }

      final InputStream inputStream = this.dataAsStream();
      this.listName = RiffChunk.read4charactersName(inputStream);

      while(inputStream.available() > 0)
      {
         this.children.add(new RiffChunk(inputStream));
      }

      this.dataSize = 0;
      this.data = new byte[0];
   }
}