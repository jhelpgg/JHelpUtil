package jhelp.util.io.riff;

import java.io.IOException;

/**
 * RIFF information
 * 
 * @author JHelp
 */
public class RiffInfo
{
   /** Artist information key */
   public static final String INFO_ARTIST    = "IART";
   /** Chunck information name */
   public static final String INFO_CHUNK     = "INFO";
   /** Copyright information key */
   public static final String INFO_COPYRIGHT = "ICOP";
   /** Name information key */
   public static final String INFO_NAME      = "INAM";

   /**
    * Extract RIFF information from RIFF
    * 
    * @param riff
    *           RIFF where extract information
    * @return Extracted information
    * @throws IOException
    *            If issue while reading RIFF embed stream
    */
   public static RiffInfo extractInfo(final Riff riff) throws IOException
   {
      final int count = riff.chunkCount();
      RiffInfo riffInfo;

      for(int i = 0; i < count; i++)
      {
         riffInfo = RiffInfo.extractInfo(riff.getChunk(i));

         if(riffInfo != null)
         {
            return riffInfo;
         }
      }

      return null;
   }

   /**
    * Extract RIFF information from RIFF chunk
    * 
    * @param chunk
    *           RIFF chunk where extract information
    * @return Extracted information
    * @throws IOException
    *            If issue while reading RIFF chunk embed stream
    */
   public static RiffInfo extractInfo(final RiffChunk chunk) throws IOException
   {
      if(RiffInfo.INFO_CHUNK.equals(chunk.listName()))
      {
         return new RiffInfo(chunk);
      }

      final int count = chunk.chunkCount();
      RiffInfo riffInfo;

      for(int i = 0; i < count; i++)
      {
         riffInfo = RiffInfo.extractInfo(chunk.getChunk(i));

         if(riffInfo != null)
         {
            return riffInfo;
         }
      }

      return null;
   }

   /** Artist */
   private final String artist;
   /** Copyright */
   private final String copyright;
   /** Name */
   private final String name;

   /**
    * Create a new instance of RiffInfo
    * 
    * @param chunkInfo
    *           Chunk information to parse
    * @throws IOException
    *            If issue while reading chunk embed stream
    */
   private RiffInfo(final RiffChunk chunkInfo)
         throws IOException
   {
      final int count = chunkInfo.chunkCount();
      String artist = null;
      String copyright = null;
      String name = null;
      RiffChunk chunk;
      String id;

      for(int i = 0; i < count; i++)
      {
         chunk = chunkInfo.getChunk(i);
         id = chunk.getID();

         if(RiffInfo.INFO_ARTIST.endsWith(id))
         {
            artist = RiffChunk.readAsciiString(chunk.dataAsStream());
         }
         else if(RiffInfo.INFO_COPYRIGHT.endsWith(id))
         {
            copyright = RiffChunk.readAsciiString(chunk.dataAsStream());
         }
         else if(RiffInfo.INFO_NAME.endsWith(id))
         {
            name = RiffChunk.readAsciiString(chunk.dataAsStream());
         }
      }

      this.artist = artist;
      this.copyright = copyright;
      this.name = name;
   }

   /**
    * Artist
    * 
    * @return Artist
    */
   public String getArtist()
   {
      return this.artist;
   }

   /**
    * Copyright
    * 
    * @return Copyright
    */
   public String getCopyright()
   {
      return this.copyright;
   }

   /**
    * Name
    * 
    * @return Name
    */
   public String getName()
   {
      return this.name;
   }
}