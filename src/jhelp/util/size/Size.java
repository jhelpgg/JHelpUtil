package jhelp.util.size;

/**
 * Represents a size in byte, kilo byte, ...
 * 
 * @author JHelp
 */
public class Size
{
   /** A bronto-bytes (2^90 bytes) */
   public static final long BRONTO;
   /** An exa-bytes (2^60 bytes) */
   public static final long EXA;
   /** A giga-bytes (2^30 bytes) */
   public static final long GIGA;
   /** A kilo-bytes (2^10 bytes) */
   public static final long KILO;
   /** A mega-bytes (2^20 bytes) */
   public static final long MEGA;
   /** A peta-bytes (2^50 bytes) */
   public static final long PETA;
   /** A tera-bytes (2^40 bytes) */
   public static final long TERA;
   /** A yotta-bytes (2^80 bytes) */
   public static final long YOTTA;
   /** A zetta-bytes (2^70 bytes) */
   public static final long ZETTA;
   static
   {
      KILO = 1024L;
      MEGA = 1024L * Size.KILO;
      GIGA = 1024L * Size.MEGA;
      TERA = 1024L * Size.GIGA;
      PETA = 1024L * Size.TERA;
      EXA = 1024L * Size.PETA;
      ZETTA = 1024L * Size.EXA;
      YOTTA = 1024L * Size.ZETTA;
      BRONTO = 1024L * Size.YOTTA;
   }
   /** Actual size */
   private long             size;

   /**
    * Create a new instance of Size
    * 
    * @param size
    *           Size in bytes
    */
   public Size(final long size)
   {
      this.setSize(size);
   }

   /**
    * Size in bytes
    * 
    * @return Size in bytes
    */
   public long getSize()
   {
      return this.size;
   }

   /**
    * Size in {@link #BRONTO} bytes
    * 
    * @return Size in {@link #BRONTO} bytes
    */
   public long inBronto()
   {
      return this.size / Size.BRONTO;
   }

   /**
    * Size in {@link #EXA} bytes
    * 
    * @return Size in {@link #EXA} bytes
    */
   public long inExa()
   {
      return this.size / Size.EXA;
   }

   /**
    * Size in {@link #GIGA} bytes
    * 
    * @return Size in {@link #GIGA} bytes
    */
   public long inGiga()
   {
      return this.size / Size.GIGA;
   }

   /**
    * Size in {@link #KILO} bytes
    * 
    * @return Size in {@link #KILO} bytes
    */
   public long inKilo()
   {
      return this.size / Size.KILO;
   }

   /**
    * Size in {@link #MEGA} bytes
    * 
    * @return Size in {@link #MEGA} bytes
    */
   public long inMega()
   {
      return this.size / Size.MEGA;
   }

   /**
    * Size in {@link #PETA} bytes
    * 
    * @return Size in {@link #PETA} bytes
    */
   public long inPeta()
   {
      return this.size / Size.PETA;
   }

   /**
    * Size in {@link #TERA} bytes
    * 
    * @return Size in {@link #TERA} bytes
    */
   public long inTera()
   {
      return this.size / Size.TERA;
   }

   /**
    * Size in {@link #YOTTA} bytes
    * 
    * @return Size in {@link #YOTTA} bytes
    */
   public long inYotta()
   {
      return this.size / Size.YOTTA;
   }

   /**
    * Size in {@link #ZETTA} bytes
    * 
    * @return Size in {@link #ZETTA} bytes
    */
   public long inZetta()
   {
      return this.size / Size.ZETTA;
   }

   /**
    * Change the size
    * 
    * @param size
    *           Size in bytes
    */
   public void setSize(final long size)
   {
      if(size < 0)
      {
         throw new IllegalArgumentException("Size must be >=0 not " + size);
      }

      this.size = size;
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
      return this.toString(true);
   }

   /**
    * String representation
    * 
    * @param full
    *           Indicates if full version ({@code true}) or short one ({@code false})
    * @return String representation
    */
   public String toString(final boolean full)
   {
      final StringBuilder stringBuilder = new StringBuilder();

      boolean started = false;

      long size = this.size;
      long value = size / Size.BRONTO;

      if(value > 0)
      {
         stringBuilder.append(value);
         stringBuilder.append("B ");
         started = true;
      }

      size = size % Size.BRONTO;
      value = size / Size.YOTTA;

      if((value > 0) || (started))
      {
         stringBuilder.append(value);
         stringBuilder.append("Y ");

         if((started) && (!full))
         {
            return stringBuilder.toString().trim();
         }

         started = true;
      }

      size = size % Size.YOTTA;
      value = size / Size.ZETTA;

      if((value > 0) || (started))
      {
         stringBuilder.append(value);
         stringBuilder.append("Z ");

         if((started) && (!full))
         {
            return stringBuilder.toString().trim();
         }

         started = true;
      }

      size = size % Size.ZETTA;
      value = size / Size.EXA;

      if((value > 0) || (started))
      {
         stringBuilder.append(value);
         stringBuilder.append("E ");

         if((started) && (!full))
         {
            return stringBuilder.toString().trim();
         }

         started = true;
      }

      size = size % Size.EXA;
      value = size / Size.PETA;

      if((value > 0) || (started))
      {
         stringBuilder.append(value);
         stringBuilder.append("P ");

         if((started) && (!full))
         {
            return stringBuilder.toString().trim();
         }

         started = true;
      }

      size = size % Size.PETA;
      value = size / Size.TERA;

      if((value > 0) || (started))
      {
         stringBuilder.append(value);
         stringBuilder.append("T ");

         if((started) && (!full))
         {
            return stringBuilder.toString().trim();
         }

         started = true;
      }

      size = size % Size.TERA;
      value = size / Size.GIGA;

      if((value > 0) || (started))
      {
         stringBuilder.append(value);
         stringBuilder.append("G ");

         if((started) && (!full))
         {
            return stringBuilder.toString().trim();
         }

         started = true;
      }

      size = size % Size.GIGA;
      value = size / Size.MEGA;

      if((value > 0) || (started))
      {
         stringBuilder.append(value);
         stringBuilder.append("M ");

         if((started) && (!full))
         {
            return stringBuilder.toString().trim();
         }

         started = true;
      }

      size = size % Size.MEGA;
      value = size / Size.KILO;

      if((value > 0) || (started))
      {
         stringBuilder.append(value);
         stringBuilder.append("K ");

         if((started) && (!full))
         {
            return stringBuilder.toString().trim();
         }
      }

      size = size % Size.KILO;
      stringBuilder.append(value);
      stringBuilder.append(" bytes");

      return stringBuilder.toString();
   }
}