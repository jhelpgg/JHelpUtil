package jhelp.util.samples.io.idl;

/**
 * Interface shared by sender and receiver
 * 
 * @author JHelp <br>
 */
public interface TestInterface
{
   /**
    * Compute next enum
    * 
    * @param actual
    *           Actual enum
    * @return Next enum
    */
   public TestEnum next(TestEnum actual);

   /**
    * Compute binarizable from reference
    * 
    * @param integer
    *           Reference
    * @return Computed binarizable
    */
   public TestBinarizable obtainTestBinarizable(int integer);

   /**
    * Compute a String to print
    * 
    * @param integer
    *           Integer
    * @param string
    *           Some words
    * @param array
    *           Data
    * @return Computed String
    */
   public String test(int integer, String string, byte[] array);
}