package jhelp.util.math.oldSystem;

import java.util.ArrayList;
import java.util.List;

import jhelp.util.debug.Debug;
import jhelp.util.gui.JHelpFont;
import jhelp.util.gui.JHelpFont.Type;
import jhelp.util.gui.JHelpFont.Value;
import jhelp.util.gui.JHelpImage;
import jhelp.util.math.UtilMath;
import jhelp.util.text.UtilText;

/**
 * Numbers as Maya represented them.<br>
 * To see correctly the numbers in String use {@link #FONT_MAYA} or {@link #FONT_NUMBERS}.<br>
 * We choose arbitrary one of the 2 possible zero.<br>
 * Maya numbers are base 20.<br>
 * Maya also used "totem" to represents numbers, a representation image can be generated with
 * {@link #toTotemImage(int, boolean)}
 * 
 * @author JHelp
 */
public class MayaNumber
      extends Number
      implements Comparable<MayaNumber>
{
   /** Totem images base (One totem per "digit") */
   private static final String[] TOTEM_NUMBERS       =
                                                     {
         "0_mil.png", "1_hun.png", "2_ca.png", "3_ox.png", "4_can.png",//
         "5_ho.png", "6_uac.png", "7_uuc.png", "8_uaxac.png", "9_bolon.png",//
         "10_lahun.png", "11_buluc.png", "12_lahca.png", "13_oxlahun.png", "14_canlahun.png",//
         "15_holahun.png", "16_uaclahun.png", "17_uuclahun.png", "18_uaxaclahun.png", "19_bolonlahun.png"
                                                     };
   /** 0 digit Maya character */
   public static final char      DIGIT_00_MIL        = (char) 128;
   /** 1 digit Maya character */
   public static final char      DIGIT_01_HUN        = (char) 129;
   /** 2 digit Maya character */
   public static final char      DIGIT_02_CA         = (char) 130;
   /** 3 digit Maya character */
   public static final char      DIGIT_03_OX         = (char) 131;
   /** 4 digit Maya character */
   public static final char      DIGIT_04_CAN        = (char) 132;
   /** 5 digit Maya character */
   public static final char      DIGIT_05_HO         = (char) 133;
   /** 6 digit Maya character */
   public static final char      DIGIT_06_UAC        = (char) 134;
   /** 7 digit Maya character */
   public static final char      DIGIT_07_UUC        = (char) 135;
   /** 8 digit Maya character */
   public static final char      DIGIT_08_UAXAC      = (char) 136;
   /** 9 digit Maya character */
   public static final char      DIGIT_09_BOLON      = (char) 137;
   /** 10 digit Maya character */
   public static final char      DIGIT_10_LAHUN      = (char) 138;
   /** 11 digit Maya character */
   public static final char      DIGIT_11_BULUC      = (char) 139;
   /** 12 digit Maya character */
   public static final char      DIGIT_12_LAHCA      = (char) 140;
   /** 13 digit Maya character */
   public static final char      DIGIT_13_OXLAHUN    = (char) 141;
   /** 14 digit Maya character */
   public static final char      DIGIT_14_CANLAHUN   = (char) 142;
   /** 15 digit Maya character */
   public static final char      DIGIT_15_HOLAHUN    = (char) 143;
   /** 16 digit Maya character */
   public static final char      DIGIT_16_UACLAHUN   = (char) 144;
   /** 17 digit Maya character */
   public static final char      DIGIT_17_UUCLAHUN   = (char) 145;
   /** 18 digit Maya character */
   public static final char      DIGIT_18_UAXACLAHUN = (char) 146;
   /** 19 digit Maya character */
   public static final char      DIGIT_19_BOLONLAHUN = (char) 147;
   /** Font with Maya digits */
   public static final JHelpFont FONT_MAYA;
   /** Font with Maya digits + tribal numbers + old digital clock numbers */
   public static final JHelpFont FONT_NUMBERS;
   static
   {
      JHelpFont font = new JHelpFont("Arial", 32);

      try
      {
         font = JHelpFont.createFont(Type.TRUE_TYPE, MayaNumber.class.getResourceAsStream("Roboto-Regular-Maya.ttf"), 32, Value.FREE, Value.FREE, false);
      }
      catch(final Exception exception)
      {
         Debug.printException(exception, "Failed to get Maya font");
      }

      FONT_MAYA = font;

      try
      {
         font = JHelpFont.createFont(Type.TRUE_TYPE, MayaNumber.class.getResourceAsStream("Roboto-Regular-Numbers.ttf"), 32, Value.FREE, Value.FREE, false);
      }
      catch(final Exception exception)
      {
         Debug.printException(exception, "Failed to get Number font");
      }

      FONT_NUMBERS = font;
   }
   /** Number digits in base 20 */
   private final byte[]          base20;
   /** Number in decimal */
   private final long            number;

   /**
    * Create a new instance of MayaNumber with a list of digits.<br>
    * Use list of defined digits : {@link MayaNumber#DIGIT_00_MIL}, {@link MayaNumber#DIGIT_01_HUN},
    * {@link MayaNumber#DIGIT_02_CA}, {@link MayaNumber#DIGIT_03_OX}, {@link MayaNumber#DIGIT_04_CAN},
    * {@link MayaNumber#DIGIT_05_HO}, {@link MayaNumber#DIGIT_06_UAC}, {@link MayaNumber#DIGIT_07_UUC},
    * {@link MayaNumber#DIGIT_08_UAXAC}, {@link MayaNumber#DIGIT_09_BOLON}, {@link MayaNumber#DIGIT_10_LAHUN},
    * {@link MayaNumber#DIGIT_11_BULUC}, {@link MayaNumber#DIGIT_12_LAHCA}, {@link MayaNumber#DIGIT_13_OXLAHUN},
    * {@link MayaNumber#DIGIT_14_CANLAHUN}, {@link MayaNumber#DIGIT_15_HOLAHUN}, {@link MayaNumber#DIGIT_16_UACLAHUN},
    * {@link MayaNumber#DIGIT_17_UUCLAHUN}, {@link MayaNumber#DIGIT_18_UAXACLAHUN} and {@link MayaNumber#DIGIT_19_BOLONLAHUN}
    * 
    * @param digits
    *           List of defined digits to represents a Maya number
    * @throws NullPointerException
    *            if the list is {@code null}
    * @throws IllegalArgumentException
    *            If the list is empty OR Or of given digit is not a define one
    */
   public MayaNumber(final char... digits)
   {
      final int length = digits.length;

      if(length == 0)
      {
         throw new IllegalArgumentException("Must have at least one digit");
      }

      long number = 0;
      int value;
      char digit;

      for(int i = 0; i < length; i++)
      {
         digit = digits[i];

         if((digit < MayaNumber.DIGIT_00_MIL) || (digit > MayaNumber.DIGIT_19_BOLONLAHUN))
         {
            throw new IllegalArgumentException(UtilText.concatenate("Given digits contains at least one illegal character at index ", i, " digits=", digits));
         }

         value = digit - MayaNumber.DIGIT_00_MIL;
         number *= 20L;
         number += value;
      }

      this.number = number;
      final List<Byte> bytes = new ArrayList<Byte>();
      bytes.add((byte) (number % 20L));

      number /= 20L;

      while(number > 0)
      {
         bytes.add(0, (byte) (number % 20L));
         number /= 20L;
      }

      final int size = bytes.size();
      this.base20 = new byte[size];

      for(int i = 0; i < size; i++)
      {
         this.base20[i] = bytes.get(i);
      }
   }

   /**
    * Create a new instance of MayaNumber.<br>
    * The number can't be negative, because Maya didn't know them
    * 
    * @param number
    *           Number to convert in Maya
    * @throws IllegalArgumentException
    *            If number is negative
    */
   public MayaNumber(long number)
   {
      if(number < 0)
      {
         throw new IllegalArgumentException("Maya didn't know negative numbers like " + number);
      }

      this.number = number;

      final List<Byte> bytes = new ArrayList<Byte>();
      bytes.add((byte) (number % 20L));

      number /= 20L;

      while(number > 0)
      {
         bytes.add(0, (byte) (number % 20L));
         number /= 20L;
      }

      final int size = bytes.size();
      this.base20 = new byte[size];

      for(int i = 0; i < size; i++)
      {
         this.base20[i] = bytes.get(i);
      }
   }

   /**
    * Create a new instance of MayaNumber from a String.<br>
    * String MUST only contains defined digits and MUSN'T be {@code null} or empty
    * 
    * @param toParse
    *           String to parse in Maya number
    * @throws NullPointerException
    *            If string is {@code null}
    */
   public MayaNumber(final String toParse)
   {
      this(toParse.toCharArray());
   }

   /**
    * Compare to an other Maya number.<br>
    * It returns :
    * <table border=0>
    * <tr>
    * <th>&lt;0</th>
    * <td><b>:</b> If this Maya number before/smaller the given one</td>
    * </tr>
    * <tr>
    * <th>0</th>
    * <td><b>:</b> If this Maya number is equals to the given one</td>
    * </tr>
    * <tr>
    * <th>&gt;0</th>
    * <td><b>:</b> If this Maya number after/bigger the given one</td>
    * </tr>
    * </table>
    * <br>
    * <br>
    * <b>Parent documentation:</b><br>
    * {@inheritDoc}
    * 
    * @param mayaNumber
    *           Maya number to compare with
    * @return Comparison result
    * @see java.lang.Comparable#compareTo(java.lang.Object)
    */
   @Override
   public int compareTo(final MayaNumber mayaNumber)
   {
      return UtilMath.sign(this.number - mayaNumber.number);
   }

   /**
    * Convert number to double <br>
    * <br>
    * <b>Parent documentation:</b><br>
    * {@inheritDoc}
    * 
    * @return Double value
    * @see java.lang.Number#doubleValue()
    */
   @Override
   public double doubleValue()
   {
      return this.number;
   }

   /**
    * Convert number to float <br>
    * <br>
    * <b>Parent documentation:</b><br>
    * {@inheritDoc}
    * 
    * @return Float value
    * @see java.lang.Number#floatValue()
    */
   @Override
   public float floatValue()
   {
      return this.number;
   }

   /**
    * Number in decimal
    * 
    * @return Number value
    */
   public long getNumber()
   {
      return this.number;
   }

   /**
    * Convert in int the number <br>
    * <br>
    * <b>Parent documentation:</b><br>
    * {@inheritDoc}
    * 
    * @return int value
    * @see java.lang.Number#intValue()
    */
   @Override
   public int intValue()
   {
      return (int) this.number;
   }

   /**
    * Convert in long the number <br>
    * <br>
    * <b>Parent documentation:</b><br>
    * {@inheritDoc}
    * 
    * @return long value
    * @see java.lang.Number#longValue()
    */
   @Override
   public long longValue()
   {
      return this.number;
   }

   /**
    * String representation.<br>
    * To be sure to see correctly the result, we recommend to use {@link #FONT_MAYA} or {@link #FONT_NUMBERS} <br>
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
      final int size = this.base20.length;
      final char[] characters = new char[size];

      for(int i = 0; i < size; i++)
      {
         characters[i] = (char) (MayaNumber.DIGIT_00_MIL + (this.base20[i] & 0xFF));
      }

      return new String(characters);
   }

   /**
    * Convert the number in totems.
    * 
    * @param baseSize
    *           Size given to each totem (width and height)
    * @param horizontal
    *           Indicates if toem are put horizontally ({@code true}) OR vertically ({@code false})
    * @return Image or totems
    */
   public JHelpImage toTotemImage(final int baseSize, final boolean horizontal)
   {
      int width = baseSize;
      int height = baseSize;
      final int length = this.base20.length;

      if(horizontal == true)
      {
         width *= length;
      }
      else
      {
         height *= length;
      }

      JHelpImage image;
      final JHelpImage totem = new JHelpImage(width, height);
      totem.startDrawMode();

      int x = 0;
      int y = 0;

      for(int i = 0; i < length; i++)
      {
         try
         {
            image = JHelpImage.loadImageThumb(MayaNumber.class.getResourceAsStream(MayaNumber.TOTEM_NUMBERS[this.base20[i] & 0xFF]), baseSize, baseSize);
            totem.drawImage(x, y, image, false);
         }
         catch(final Exception exception)
         {
            Debug.printException(exception, "Failed to get image for number ", this.base20[i] & 0xFF);
         }

         if(horizontal == true)
         {
            x += baseSize;
         }
         else
         {
            y += baseSize;
         }
      }

      totem.endDrawMode();

      return totem;
   }
}