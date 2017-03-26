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
package jhelp.util.image.ani;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import jhelp.util.debug.Debug;
import jhelp.util.debug.DebugLevel;
import jhelp.util.gui.JHelpImage;
import jhelp.util.gui.dynamic.DynamicAnimation;
import jhelp.util.image.bmp.BitmapHeader;
import jhelp.util.image.cur.CurImage;
import jhelp.util.image.ico.IcoImage;
import jhelp.util.image.raster.BinaryImage;
import jhelp.util.image.raster.Image16Bit;
import jhelp.util.image.raster.Image24Bit;
import jhelp.util.image.raster.Image32Bit;
import jhelp.util.image.raster.Image4Bit;
import jhelp.util.image.raster.Image8Bit;
import jhelp.util.image.raster.RasterImageType;
import jhelp.util.io.riff.Riff;
import jhelp.util.io.riff.RiffChunk;
import jhelp.util.list.ArrayInt;

/**
 * Ani image
 * 
 * @author JHelp
 */
public class AniImage
      extends Riff
      implements DynamicAnimation
{
   /** Chunk ani header name */
   public static final String              CHUNK_ANI_HEADER = "anih";
   /** Chunk icon name */
   public static final String              CHUNK_ICON       = "icon";
   /** Chunk rate header name */
   public static final String              CHUNK_RATE       = "rate";
   /** Sequence chunk name : the end space is put by purpose (since name MUST have exactly 4 characters) */
   public static final String              CHUNK_SEQUENCE   = "seq ";
   /** Chunk frame name */
   public static final String              FRAME_LIST_NAME  = "fram";
   /** Chunk list header name */
   public static final String              LIST_NAME        = "ACON";
   /** Indicates if ani have sequence data */
   private boolean                         asSequenceData;
   /** Number of bit */
   private int                             bitCount;
   /** Frame Per Second animation */
   private int                             fps;
   /** Ani height */
   private int                             height;
   /** Image duration */
   private int                             imageDuration;
   /** List of information */
   private final List<AniImageInformation> images;
   /** Number of frames */
   private int                             numberOfFrames;
   /** Number of steps */
   private int                             numberOfSteps;
   /** Rates */
   private final ArrayInt                  rates;
   /** Relative frame */
   private float                           relativeFrame;
   /** Indicates if ani is row data */
   private boolean                         rowData;
   /** Sequences */
   private final ArrayInt                  sequences;
   /** Start absolute frame */
   private float                           startAbsoluteFrame;
   /** Number of total frame */
   private int                             totalFrame;
   /** Ani width */
   private int                             width;
   /** X */
   private int                             x;
   /** Y */
   private int                             y;

   /**
    * Create a new instance of AniImage
    * 
    * @param inputStream
    *           Stream to parse
    * @throws IOException
    *            On reading issue
    */
   public AniImage(final InputStream inputStream)
         throws IOException
   {
      super(inputStream);

      this.images = new ArrayList<AniImageInformation>();
      this.fps = 25;
      this.imageDuration = 1000;
      this.sequences = new ArrayInt();
      this.rates = new ArrayInt();
      this.asSequenceData = false;
      this.rowData = false;
      final RiffChunk chunk = this.getChunk(0);

      if((!RiffChunk.RIFF.equals(chunk.getID())) || (!AniImage.LIST_NAME.equals(chunk.listName())))
      {
         throw new IOException("Not a valid ANI file : " + chunk.getID() + "=>" + chunk.listName());
      }

      final int count = chunk.chunkCount();

      for(int i = 0; i < count; i++)
      {
         this.parse(chunk.getChunk(i));
      }

      if((!this.asSequenceData) || (this.sequences.isEmpty()))
      {
         this.sequences.clear();

         for(int i = 0; i < this.numberOfSteps; i++)
         {
            this.sequences.add(i);
         }
      }

      if(this.rates.isEmpty())
      {
         for(int i = 0; i < this.numberOfSteps; i++)
         {
            this.rates.add(this.imageDuration);
         }
      }

      this.totalFrame = 0;

      for(int i = 0; i < this.numberOfSteps; i++)
      {
         this.rates.setInteger(i, this.rates.getInteger(i) / 40);
         this.totalFrame += this.rates.getInteger(i);
      }

      Debug.println(DebugLevel.INFORMATION, "this.images.size()=" + this.images.size());
      Debug.println(DebugLevel.INFORMATION, "this.totalFrame=" + this.totalFrame);
   }

   /**
    * Parse a chunk
    * 
    * @param chunk
    *           Chunk to parse
    * @throws IOException
    *            On reading issue
    */
   private void parse(final RiffChunk chunk) throws IOException
   {
      final String id = chunk.getID();
      final String listName = chunk.listName();

      if(AniImage.CHUNK_ANI_HEADER.equals(id))
      {
         this.parseAniHeader(chunk);
      }
      else if((RiffChunk.LIST.equals(id)) && (AniImage.FRAME_LIST_NAME.equals(listName)))
      {
         this.parseFrameList(chunk);
      }
      else if(AniImage.CHUNK_ICON.equals(id))
      {
         this.parseIcon(chunk);
      }
      else if(AniImage.CHUNK_SEQUENCE.equals(id))
      {
         this.parseSequences(chunk);
      }
      else if(AniImage.CHUNK_RATE.equals(id))
      {
         this.parseRate(chunk);
      }
   }

   /**
    * Parse ani header chunk
    * 
    * @param chunk
    *           Ani header chunk to parse
    * @throws IOException
    *            On reading issue
    */
   private void parseAniHeader(final RiffChunk chunk) throws IOException
   {
      final InputStream stream = chunk.dataAsStream();
      int info = BitmapHeader.read4bytes(stream);

      if(info != 36)
      {
         throw new IOException("header size MUST be 36 not " + info);
      }

      this.numberOfFrames = BitmapHeader.read4bytes(stream);
      this.numberOfSteps = BitmapHeader.read4bytes(stream);
      this.width = BitmapHeader.read4bytes(stream);
      this.height = BitmapHeader.read4bytes(stream);
      this.bitCount = BitmapHeader.read4bytes(stream);

      Debug.println(DebugLevel.INFORMATION, "this.numberOfFrames=" + this.numberOfFrames);
      Debug.println(DebugLevel.INFORMATION, "this.bitCount=" + this.bitCount);

      info = BitmapHeader.read4bytes(stream);

      if(info != 1)
      {
         throw new IOException("number of plane size MUST be 1 not " + info);
      }

      this.fps = 60 / BitmapHeader.read4bytes(stream);
      this.imageDuration = (BitmapHeader.read4bytes(stream) * 1000) / 60;

      info = BitmapHeader.read4bytes(stream);
      this.asSequenceData = (info & 2) != 0;
      this.rowData = (info & 1) == 0;

      Debug.println(DebugLevel.INFORMATION, "this.rowData=" + this.rowData);
      Debug.println(DebugLevel.INFORMATION, "this.numberOfFrames=" + this.numberOfFrames);
   }

   /**
    * Parse frame list chunk
    * 
    * @param chunk
    *           Chunk to parse
    * @throws IOException
    *            On reading issue
    */
   private void parseFrameList(final RiffChunk chunk) throws IOException
   {
      final int count = chunk.chunkCount();

      for(int i = 0; i < count; i++)
      {
         this.parse(chunk.getChunk(i));
      }
   }

   /**
    * Parse icon chunk
    * 
    * @param chunk
    *           Chunk to parse
    * @throws IOException
    *            On reading issue
    */
   private void parseIcon(final RiffChunk chunk) throws IOException
   {
      if(this.rowData)
      {
         switch(this.bitCount)
         {
            case BitmapHeader.IMAGE_BINARY:
               final BinaryImage binaryImage = new BinaryImage(this.width, this.height);
               binaryImage.parseBitmapStream(chunk.dataAsStream());
               this.images.add(new AniImageInformation(binaryImage));
            break;
            case BitmapHeader.IMAGE_4_BITS:
               final Image4Bit image4Bit = new Image4Bit(this.width, this.height);
               image4Bit.parseBitmapStream(chunk.dataAsStream());
               this.images.add(new AniImageInformation(image4Bit));
            break;
            case BitmapHeader.IMAGE_8_BITS:
               final Image8Bit image8Bit = new Image8Bit(this.width, this.height);
               image8Bit.parseBitmapStream(chunk.dataAsStream());
               this.images.add(new AniImageInformation(image8Bit));
            break;
            case BitmapHeader.IMAGE_16_BITS:
               final Image16Bit image16Bit = new Image16Bit(this.width, this.height);
               image16Bit.parseBitmapStream(chunk.dataAsStream());
               this.images.add(new AniImageInformation(image16Bit));
            break;
            case BitmapHeader.IMAGE_24_BITS:
               final Image24Bit image24Bit = new Image24Bit(this.width, this.height);
               image24Bit.parseBitmapStream(chunk.dataAsStream());
               this.images.add(new AniImageInformation(image24Bit));
            break;
            case BitmapHeader.IMAGE_32_BITS:
               final Image32Bit image32Bit = new Image32Bit(this.width, this.height);
               image32Bit.parseBitmapStream(chunk.dataAsStream());
               this.images.add(new AniImageInformation(image32Bit));
            break;
         }

         return;
      }

      final InputStream inputStream = chunk.dataAsStream();
      int info = BitmapHeader.read2bytes(inputStream);

      if(info != 0)
      {
         throw new IOException("First 2 bytes MUST be 0, not " + info);
      }

      info = BitmapHeader.read2bytes(inputStream);

      switch(info)
      {
         case 2:
            this.images.add(new AniImageInformation(new IcoImage(chunk.dataAsStream(), RasterImageType.getRasterImageType(this.bitCount))));
         break;
         case 1:
            this.images.add(new AniImageInformation(new CurImage(chunk.dataAsStream())));
         break;
      }
   }

   /**
    * Parse chunk rate
    * 
    * @param chunk
    *           Chunk to parse
    * @throws IOException
    *            On reading issue
    */
   private void parseRate(final RiffChunk chunk) throws IOException
   {
      final InputStream stream = chunk.dataAsStream();

      for(int i = 0; i < this.numberOfSteps; i++)
      {
         this.rates.add((BitmapHeader.read4bytes(stream) * 1000) / 60);
      }
   }

   /**
    * Parse chunk sequences
    * 
    * @param chunk
    *           Chunk to parse
    * @throws IOException
    *            On reading issue
    */
   private void parseSequences(final RiffChunk chunk) throws IOException
   {
      final InputStream stream = chunk.dataAsStream();

      for(int i = 0; i < this.numberOfSteps; i++)
      {
         this.sequences.add(BitmapHeader.read4bytes(stream));
      }
   }

   /**
    * Update ani animation <br>
    * <br>
    * <b>Parent documentation:</b><br>
    * {@inheritDoc}
    * 
    * @param absoluteFrame
    *           Absolute frame
    * @param image
    *           Image where draw
    * @return {@code true} If animation should continue
    * @see jhelp.util.gui.dynamic.DynamicAnimation#animate(float, jhelp.util.gui.JHelpImage)
    */
   @Override
   public boolean animate(final float absoluteFrame, final JHelpImage image)
   {
      if(this.totalFrame == 0)
      {
         this.images.get(0).draw(image, this.x, this.y);
         return true;
      }

      float frame = absoluteFrame - this.relativeFrame;

      while(frame >= this.totalFrame)
      {
         frame -= this.totalFrame;
         this.relativeFrame = absoluteFrame;
      }

      int index = 0;

      while(frame > this.rates.getInteger(index))
      {
         frame -= this.rates.getInteger(index);
         index++;
      }

      final int                 sequence            = this.sequences.getInteger(index);
      final AniImageInformation aniImageinformation = this.images.get(sequence);
      aniImageinformation.draw(image, this.x, this.y);

      return true;
   }

   /**
    * Called when animation end <br>
    * <br>
    * <b>Parent documentation:</b><br>
    * {@inheritDoc}
    * 
    * @param image
    *           Image parent
    * @see jhelp.util.gui.dynamic.DynamicAnimation#endAnimation(jhelp.util.gui.JHelpImage)
    */
   @Override
   public void endAnimation(final JHelpImage image)
   {
   }

   /**
    * Ani FPS
    * 
    * @return Ani FPS
    */
   public int getFPS()
   {
      return this.fps;
   }

   /**
    * Ani height
    * 
    * @return Ani height
    */
   public int getHeight()
   {
      return this.height;
   }

   /**
    * Ani width
    * 
    * @return Ani width
    */
   public int getWidth()
   {
      return this.width;
   }

   /**
    * X
    * 
    * @return X
    */
   public int getX()
   {
      return this.x;
   }

   /**
    * Y
    * 
    * @return Y
    */
   public int getY()
   {
      return this.y;
   }

   /**
    * Change ani location
    * 
    * @param x
    *           X
    * @param y
    *           Y
    */
   public void setPosition(final int x, final int y)
   {
      this.x = x;
      this.y = y;
   }

   /**
    * Called when animation start <br>
    * <br>
    * <b>Parent documentation:</b><br>
    * {@inheritDoc}
    * 
    * @param startAbsoluteFrame
    *           Start absolute frame
    * @param image
    *           Image parent
    * @see jhelp.util.gui.dynamic.DynamicAnimation#startAnimation(float, jhelp.util.gui.JHelpImage)
    */
   @Override
   public void startAnimation(final float startAbsoluteFrame, final JHelpImage image)
   {
      this.startAbsoluteFrame = startAbsoluteFrame;
      this.relativeFrame = this.startAbsoluteFrame;
   }
}