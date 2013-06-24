package jhelp.util.samples.io.idl;

public interface TestInterface
{
   public TestEnum next(TestEnum actual);

   public TestBinarizable obtainTestBinarizable(int integer);

   public String test(int integer, String string, byte[] array);
}