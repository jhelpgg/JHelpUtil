package jhelp.util.samples.io.idl;

import jhelp.util.io.Binarizable;
import jhelp.util.io.ByteArray;

/**
 * Binarizable for test binarizable transfer
 *
 * @author JHelp <br>
 */
public class TestBinarizable
      implements Binarizable
{
   /** Integer to transfer */
   private int integer;

   /**
    * Create a new instance of TestBinarizable
    *
    * @param integer
    *           Integer to transfer
    */
   public TestBinarizable(final int integer)
   {
      this.integer = integer;
   }

   /**
    * Integer to transfer
    *
    * @return Integer to transfer
    */
   public int getInteger()
   {
      return this.integer;
   }

   /**
    * Parse the binarizable from byte array <br>
    * <br>
    * <b>Parent documentation:</b><br>
    * {@inheritDoc}
    *
    * @param byteArray
    *           Byte array to parse
    * @see jhelp.util.io.Binarizable#parseBinary(jhelp.util.io.ByteArray)
    */
   @Override
   public void parseBinary(final ByteArray byteArray)
   {
      this.integer = byteArray.readInteger();
   }

   /**
    * Serialize inside a byte array <br>
    * <br>
    * <b>Parent documentation:</b><br>
    * {@inheritDoc}
    *
    * @param byteArray
    *           Byte array where write
    * @see jhelp.util.io.Binarizable#serializeBinary(jhelp.util.io.ByteArray)
    */
   @Override
   public void serializeBinary(final ByteArray byteArray)
   {
      byteArray.writeInteger(this.integer);
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
      return "TestBinarizable_" + this.integer;
   }
}