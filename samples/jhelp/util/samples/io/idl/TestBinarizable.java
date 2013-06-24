package jhelp.util.samples.io.idl;

import jhelp.util.io.Binarizable;
import jhelp.util.io.ByteArray;

public class TestBinarizable
      implements Binarizable
{
   private int integer;

   public TestBinarizable(final int integer)
   {
      this.integer = integer;
   }

   public int getInteger()
   {
      return this.integer;
   }

   @Override
   public void parseBinary(final ByteArray byteArray)
   {
      this.integer = byteArray.readInteger();
   }

   @Override
   public void serializeBinary(final ByteArray byteArray)
   {
      byteArray.writeInteger(this.integer);
   }

   @Override
   public String toString()
   {
      return "TestBinarizable_" + this.integer;
   }
}