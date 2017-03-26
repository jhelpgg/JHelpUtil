package jhelp.util.samples.io.idl;

import jhelp.util.debug.Debug;
import jhelp.util.debug.DebugLevel;
import jhelp.util.io.idl.JHelpIDL;
import jhelp.util.text.UtilText;

/**
 * JHelp IDL sample
 *
 * @author JHelp <br>
 */
public class JHelpIDLSample
      implements TestInterface
{

   /**
    * Launch JHelp IDL sample
    *
    * @param args
    *           Unused
    */
   public static void main(final String[] args)
   {
      try
      {
         JHelpIDL.declareIDLs(TestInterface.class);

         JHelpIDL.registerReceiver(new JHelpIDLSample(), TestInterface.class);
         final TestInterface sender = JHelpIDL.obtainSenderInstance(TestInterface.class);

         final byte[] array =
         {
               0, 1, 2, 3, 4, 5, 6, 7, 8, 9
         };
         String result;
         TestEnum testEnum = TestEnum.T1;
         for(int i = 0; i < 10; i++)
         {
            array[0] = (byte) (i & 0xFF);
            result = sender.test(i, "String" + i, array);

            Debug.println(DebugLevel.VERBOSE, "result=", result);

            Debug.println(DebugLevel.VERBOSE, "BEFORE : ", testEnum);
            testEnum = sender.next(testEnum);
            Debug.println(DebugLevel.VERBOSE, "AFTER : ", testEnum);

            Debug.println(DebugLevel.VERBOSE, "Bin=", sender.obtainTestBinarizable(i));
         }

         JHelpIDL.stopReceiver(TestInterface.class);
      }
      catch(final Exception exception)
      {
         Debug.printException(exception);
      }
   }

   /**
    * Give the next element <br>
    * <br>
    * <b>Parent documentation:</b><br>
    * {@inheritDoc}
    *
    * @param actual
    *           Element to have its next
    * @return Next element
    * @see jhelp.util.samples.io.idl.TestInterface#next(jhelp.util.samples.io.idl.TestEnum)
    */
   @Override
   public TestEnum next(final TestEnum actual)
   {
      switch(actual)
      {
         case T1:
            return TestEnum.T2;
         case T2:
            return TestEnum.T3;
         case T3:
            return TestEnum.T1;
      }

      return actual;
   }

   /**
    * Create a binarizable <br>
    * <br>
    * <b>Parent documentation:</b><br>
    * {@inheritDoc}
    *
    * @param integer
    *           Integer
    * @return Created binarizable
    * @see jhelp.util.samples.io.idl.TestInterface#obtainTestBinarizable(int)
    */
   @Override
   public TestBinarizable obtainTestBinarizable(final int integer)
   {
      return new TestBinarizable(integer * integer);
   }

   /**
    * Create String to print <br>
    * <br>
    * <b>Parent documentation:</b><br>
    * {@inheritDoc}
    *
    * @param integer
    *           Integer
    * @param string
    *           String
    * @param array
    *           Data
    * @return String to print
    * @see jhelp.util.samples.io.idl.TestInterface#test(int, java.lang.String, byte[])
    */
   @Override
   public String test(final int integer, final String string, final byte[] array)
   {
      return UtilText.concatenate("integer=", integer, " string=", string, " array=", array);
   }
}