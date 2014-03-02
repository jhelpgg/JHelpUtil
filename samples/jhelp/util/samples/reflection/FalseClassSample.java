package jhelp.util.samples.reflection;

import jhelp.util.reflection.FalseClass;

public class FalseClassSample
{

   /**
    * @param args
    */
   public static void main(final String[] args)
   {
      final SampleInterface sampleInterface = FalseClass.createFalseClass(SampleInterface.class);

      sampleInterface.inverse("Test");
      sampleInterface.operation("ADD", 7, 5);
      sampleInterface.operation("NOT", 42);
   }
}