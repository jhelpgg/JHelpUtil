package jhelp.util.io.riff;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * RIFF document file
 * 
 * @author JHelp
 */
public class Riff
{
   /** RIFF chunks */
   private final List<RiffChunk> chunks;

   /**
    * Create a new instance of Riff
    * 
    * @param inputStream
    *           Stream to parse
    * @throws IOException
    *            On reading issue
    */
   public Riff(final InputStream inputStream)
         throws IOException
   {
      this.chunks = new ArrayList<RiffChunk>();

      while(inputStream.available() > 0)
      {
         this.chunks.add(new RiffChunk(inputStream));
      }
   }

   /**
    * Number of chunks
    * 
    * @return Number of chunks
    */
   public int chunkCount()
   {
      return this.chunks.size();
   }

   /**
    * Get one chunk
    * 
    * @param index
    *           Chunk index
    * @return Chunk
    */
   public RiffChunk getChunk(final int index)
   {
      return this.chunks.get(index);
   }
}