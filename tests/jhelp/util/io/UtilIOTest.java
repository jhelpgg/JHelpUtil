package jhelp.util.io;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;

import org.junit.Assert;
import org.junit.Test;

import jhelp.util.HashCode;
import jhelp.util.debug.Debug;

/**
 * {@link UtilIO} unit tests
 *
 * @author JHelp
 */
public class UtilIOTest
{
   /**
    * Binarizable for test
    *
    * @author JHelp <br>
    */
   public class TestBinarizable
         implements Binarizable
   {
      /** Integer */
      private int    integer;
      /** Name */
      private String name;

      /**
       * Create a new instance of TestBinarizable
       */
      public TestBinarizable()
      {
      }

      /**
       * Get parent type
       *
       * @return Parent type
       */
      private UtilIOTest getOuterType()
      {
         return UtilIOTest.this;
      }

      /**
       * Indicates if given object is equals <br>
       * <br>
       * <b>Parent documentation:</b><br>
       * {@inheritDoc}
       *
       * @param obj
       *           Tested object
       * @return {@code true} if given object is equals
       * @see java.lang.Object#equals(java.lang.Object)
       */
      @Override
      public boolean equals(final Object obj)
      {
         if(this == obj)
         {
            return true;
         }
         if(obj == null)
         {
            return false;
         }
         if(this.getClass() != obj.getClass())
         {
            return false;
         }
         final TestBinarizable other = (TestBinarizable) obj;
         if(this.integer != other.integer)
         {
            return false;
         }
         if(this.name == null)
         {
            if(other.name != null)
            {
               return false;
            }
         }
         else if(!this.name.equals(other.name))
         {
            return false;
         }
         return true;
      }

      /**
       * Integer
       *
       * @return Integer
       */
      public int getInteger()
      {
         return this.integer;
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

      /**
       * Hash code <br>
       * <br>
       * <b>Parent documentation:</b><br>
       * {@inheritDoc}
       *
       * @return Hash code
       * @see java.lang.Object#hashCode()
       */
      @Override
      public int hashCode()
      {
         return HashCode.computeHashCode(this.getOuterType(), this.integer, this.name);
      }

      /**
       * Parse byte array to fill object <br>
       * <br>
       * <b>Parent documentation:</b><br>
       * {@inheritDoc}
       *
       * @param byteArray
       *           Array to parse
       * @see jhelp.util.io.Binarizable#parseBinary(jhelp.util.io.ByteArray)
       */
      @Override
      public void parseBinary(final ByteArray byteArray)
      {
         this.name = byteArray.readString();
         this.integer = byteArray.readInteger();
      }

      /**
       * Serialize inside array <br>
       * <br>
       * <b>Parent documentation:</b><br>
       * {@inheritDoc}
       *
       * @param byteArray
       *           Array where serialize
       * @see jhelp.util.io.Binarizable#serializeBinary(jhelp.util.io.ByteArray)
       */
      @Override
      public void serializeBinary(final ByteArray byteArray)
      {
         byteArray.writeString(this.name);
         byteArray.writeInteger(this.integer);
      }

      /**
       * Change integer
       *
       * @param integer
       *           New integer
       */
      public void setInteger(final int integer)
      {
         this.integer = integer;
      }

      /**
       * Change name
       *
       * @param name
       *           New name
       */
      public void setName(final String name)
      {
         this.name = name;
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
         return this.integer + " - " + this.name;
      }
   }

   /** Name of test directory */
   private static final String TEST_DIRECTORY                       = "testDirectory";
   /** Name of created directory, during the test */
   private static final String TEST_DIRECTORY_CREATE_DIRECTORY      = UtilIOTest.TEST_DIRECTORY + "/createDirectory";
   /** Name of the created file during the test */
   private static final String TEST_DIRECTORY_CREATE_FILE           = UtilIOTest.TEST_DIRECTORY + "/createFile";
   /** Name of directory to delete during the test */
   private static final String TEST_DIRECTORY_DELETE_DIRECTORY      = UtilIOTest.TEST_DIRECTORY + "/deleteDirectory";
   /** Name of sub directory 1 to delete during the test */
   private static final String TEST_DIRECTORY_DELETE_DIRECTORY_DIR1 = UtilIOTest.TEST_DIRECTORY + "/deleteDirectory/dir1";
   /** Name of sub directory 2 to delete during the test */
   private static final String TEST_DIRECTORY_DELETE_DIRECTORY_DIR2 = UtilIOTest.TEST_DIRECTORY + "/deleteDirectory/dir2";
   /** Name of file 1 to delete during the test */
   private static final String TEST_DIRECTORY_DELETE_DIRECTORY_F1   = UtilIOTest.TEST_DIRECTORY + "/deleteDirectory/dir1/f1";
   /** Name of file 2 to delete during the test */
   private static final String TEST_DIRECTORY_DELETE_DIRECTORY_F2   = UtilIOTest.TEST_DIRECTORY + "/deleteDirectory/dir1/f2";
   /** Name of file 3 to delete during the test */
   private static final String TEST_DIRECTORY_DELETE_DIRECTORY_F3   = UtilIOTest.TEST_DIRECTORY + "/deleteDirectory/dir2/f3";
   /** Name of file to delete during the test */
   private static final String TEST_DIRECTORY_DELETE_FILE           = UtilIOTest.TEST_DIRECTORY + "/deleteFile";
   /** Name of file to copy during the test */
   private static final String TEST_DIRECTORY_FILE                  = UtilIOTest.TEST_DIRECTORY + "/file";
   /** Name of directory where unzip during the test */
   private static final String TEST_DIRECTORY_UNZIP                 = UtilIOTest.TEST_DIRECTORY + "/unzip";
   /** Name of file to unzip during the test */
   private static final String TEST_DIRECTORY_UNZIP_FILE            = UtilIOTest.TEST_DIRECTORY + "/unzip.zip";
   /** Name of virtual link tested */
   private static final String TEST_DIRECTORY_VIRTUAL_LINK          = UtilIOTest.TEST_DIRECTORY + "/virtualLink";
   /** Name of directory to zip test */
   private static final String TEST_DIRECTORY_ZIP                   = UtilIOTest.TEST_DIRECTORY + "/zip";

   /** Name of result zip file */
   private static final String TEST_DIRECTORY_ZIP_FILE              = UtilIOTest.TEST_DIRECTORY + "/zip.zip";

   /**
    * Compare 2 directories contents
    *
    * @param directory1
    *           First directory
    * @param directory2
    *           Second directory
    * @return {@code true} if directories have same tree
    */
   public static boolean compareDirectories(final File directory1, final File directory2)
   {
      final File[] children1 = directory1.listFiles();
      assert children1 != null;
      Arrays.sort(children1, ComparatorFile.COMPARATOR_FILE);

      final File[] children2 = directory2.listFiles();
      assert children2 != null;
      Arrays.sort(children2, ComparatorFile.COMPARATOR_FILE);

      if(children1.length != children2.length)
      {
         return false;
      }

      final int length = children1.length;
      File child1;
      File child2;

      for(int i = 0; i < length; i++)
      {
         child1 = children1[i];
         child2 = children2[i];

         if(ComparatorFile.COMPARATOR_FILE.compare(child1, child2) != 0)
         {
            return false;
         }

         if(!UtilIO.isVirtualLink(child1))
         {
            if(child1.isDirectory())
            {
               if(!UtilIOTest.compareDirectories(child1, child2))
               {
                  return false;
               }
            }
            else if(!UtilIOTest.compareFiles(child1, child2))
            {
               return false;
            }
         }
      }

      return true;
   }

   /**
    * Indicates if 2 files are the same
    *
    * @param file1
    *           First file
    * @param file2
    *           Second file
    * @return {@code true} if files have same content
    */
   public static boolean compareFiles(final File file1, final File file2)
   {
      FileInputStream fileInputStream1 = null;
      FileInputStream fileInputStream2 = null;

      try
      {
         fileInputStream1 = new FileInputStream(file1);
         fileInputStream2 = new FileInputStream(file2);

         int read1 = fileInputStream1.read();
         int read2 = fileInputStream2.read();

         if(read1 != read2)
         {
            return false;
         }

         while(read1 >= 0)
         {
            read1 = fileInputStream1.read();
            read2 = fileInputStream2.read();

            if(read1 != read2)
            {
               return false;
            }
         }

         return true;
      }
      catch(final IOException exception)
      {
         Debug.printException(exception, "Failed on comparision of " + file1.getAbsolutePath() + " and " + file2.getAbsolutePath());

         return false;
      }
      finally
      {
         if(fileInputStream1 != null)
         {
            try
            {
               fileInputStream1.close();
            }
            catch(final Exception ignored)
            {
            }
         }

         if(fileInputStream2 != null)
         {
            try
            {
               fileInputStream2.close();
            }
            catch(final Exception ignored)
            {
            }
         }
      }
   }

   /**
    * Initialize directories for code coverage
    *
    * @throws IOException
    *            On copying issue
    */
   // @BeforeClass
   public static void initialiazeForCodeCoverage() throws IOException
   {
      UtilIO.copy(new File("/home/jhelp/travo/worksp/JHelpUtil/testDirectory"),
            new File("/home/jhelp/travo/worksp/.metadata/.plugins/com.mountainminds.eclemma.core/.instr/testDirectory"));
   }

   /**
    * Test of {@link UtilIO#writeBinarizableNamed(Binarizable, java.io.OutputStream)} and
    * {@link UtilIO#readBinarizableNamed(java.io.InputStream)}
    *
    * @throws IOException
    *            On issue
    */
   @Test
   public void testBinarizableObjects() throws IOException
   {
      final TestBinarizable testBinarizable1 = new TestBinarizable();
      testBinarizable1.setName("Name");
      testBinarizable1.setInteger(123456789);

      final ByteArray byteArray = new ByteArray();
      UtilIO.writeBinarizableNamed(testBinarizable1, byteArray.getOutputStream());

      final TestBinarizable testBinarizable2 = UtilIO.readBinarizableNamed(byteArray.getInputStream());

      Assert.assertEquals(testBinarizable1, testBinarizable2);
   }

   /**
    * Test of {@link UtilIO#createDirectory(File)}
    */
   @Test
   public void testCreateDirectory()
   {
      final File directory = UtilIO.obtainExternalFile(UtilIOTest.TEST_DIRECTORY_CREATE_DIRECTORY);

      Assert.assertTrue(UtilIO.createDirectory(directory));
      Assert.assertTrue(directory.exists());
      Assert.assertTrue(directory.isDirectory());

      Assert.assertTrue(UtilIO.delete(directory));
   }

   /**
    * Test of {@link UtilIO#createFile(File)}
    */
   @Test
   public void testCreateFile()
   {
      final File file = UtilIO.obtainExternalFile(UtilIOTest.TEST_DIRECTORY_CREATE_FILE);

      Assert.assertTrue(UtilIO.createFile(file));
      Assert.assertTrue(file.exists());
      Assert.assertFalse(file.isDirectory());

      Assert.assertTrue(UtilIO.delete(file));
   }

   /**
    * Test of {@link UtilIO#delete(File)}
    */
   @Test
   public void testDelete()
   {
      File file = UtilIO.obtainExternalFile(UtilIOTest.TEST_DIRECTORY_DELETE_FILE);

      Assert.assertTrue(UtilIO.delete(file));
      Assert.assertFalse(file.exists());

      //

      UtilIO.createFile(file);

      //

      file = UtilIO.obtainExternalFile(UtilIOTest.TEST_DIRECTORY_DELETE_DIRECTORY);

      Assert.assertTrue(UtilIO.delete(file));
      Assert.assertFalse(file.exists());

      file = UtilIO.obtainExternalFile(UtilIOTest.TEST_DIRECTORY_DELETE_DIRECTORY_DIR1);
      Assert.assertFalse(file.exists());

      file = UtilIO.obtainExternalFile(UtilIOTest.TEST_DIRECTORY_DELETE_DIRECTORY_DIR2);
      Assert.assertFalse(file.exists());

      file = UtilIO.obtainExternalFile(UtilIOTest.TEST_DIRECTORY_DELETE_DIRECTORY_F1);
      Assert.assertFalse(file.exists());

      file = UtilIO.obtainExternalFile(UtilIOTest.TEST_DIRECTORY_DELETE_DIRECTORY_F2);
      Assert.assertFalse(file.exists());

      file = UtilIO.obtainExternalFile(UtilIOTest.TEST_DIRECTORY_DELETE_DIRECTORY_F3);
      Assert.assertFalse(file.exists());

      //

      file = UtilIO.obtainExternalFile(UtilIOTest.TEST_DIRECTORY_DELETE_DIRECTORY_F1);
      UtilIO.createFile(file);

      file = UtilIO.obtainExternalFile(UtilIOTest.TEST_DIRECTORY_DELETE_DIRECTORY_F2);
      UtilIO.createFile(file);

      file = UtilIO.obtainExternalFile(UtilIOTest.TEST_DIRECTORY_DELETE_DIRECTORY_F3);
      UtilIO.createFile(file);
   }

   /**
    * Test of {@link UtilIO#isVirtualLink(File)}
    */
   @Test
   public void testIsVirtualLink()
   {
      File file = UtilIO.obtainExternalFile(UtilIOTest.TEST_DIRECTORY_FILE);
      Assert.assertFalse(UtilIO.isVirtualLink(file));

      file = UtilIO.obtainExternalFile(UtilIOTest.TEST_DIRECTORY_ZIP);
      Assert.assertFalse(UtilIO.isVirtualLink(file));

      file = UtilIO.obtainExternalFile(UtilIOTest.TEST_DIRECTORY_VIRTUAL_LINK);
      Assert.assertTrue(UtilIO.isVirtualLink(file));
   }

   /**
    * Test of {@link UtilIO#obtainExternalFile(String)}
    */
   @Test
   public void testObtainExternalFile()
   {
      final File file = UtilIO.obtainExternalFile(UtilIOTest.TEST_DIRECTORY_FILE);
      Assert.assertEquals("file", file.getName());
      Assert.assertEquals(UtilIOTest.TEST_DIRECTORY, file.getParentFile().getName());
   }

   /**
    * Test of {@link UtilIO#obtainFile(File, String)}
    */
   @Test
   public void testObtainFileFileString()
   {
      final File file = UtilIO.obtainFile(UtilIO.obtainOutsideDirectory(), UtilIOTest.TEST_DIRECTORY_FILE);
      Assert.assertEquals("file", file.getName());
      Assert.assertEquals(UtilIOTest.TEST_DIRECTORY, file.getParentFile().getName());
   }

   /**
    * Test of {@link UtilIO#obtainFile(File, String, char)}
    */
   @Test
   public void testObtainFileFileStringChar()
   {
      final File file = UtilIO.obtainFile(UtilIO.obtainOutsideDirectory(), UtilIOTest.TEST_DIRECTORY_FILE.replace('/', '*'), '*');
      Assert.assertEquals("file", file.getName());
      Assert.assertEquals(UtilIOTest.TEST_DIRECTORY, file.getParentFile().getName());
   }

   /**
    * Test of {@link UtilIO#obtainOutsideDirectory()}
    */
   @Test
   public void testObtainOutsideDirectory()
   {
      final File directory = UtilIO.obtainOutsideDirectory();

      boolean ok = false;
      final File[] content = directory.listFiles();

      assert content != null;
      for(final File child : content)
      {
         if(UtilIOTest.TEST_DIRECTORY.equals(child.getName()))
         {
            ok = true;

            break;
         }
      }

      Assert.assertTrue(ok);
   }

   /**
    * Test of {@link UtilIO#unzip(File, File)}
    *
    * @throws IOException
    *            On unzipping issue
    */
   @Test
   public void testUnzipFileFile() throws IOException
   {
      UtilIO.unzip(UtilIO.obtainExternalFile(UtilIOTest.TEST_DIRECTORY_UNZIP), UtilIO.obtainExternalFile(UtilIOTest.TEST_DIRECTORY_UNZIP_FILE));

      Assert.assertTrue(UtilIOTest.compareDirectories(UtilIO.obtainExternalFile(UtilIOTest.TEST_DIRECTORY_UNZIP),
            UtilIO.obtainExternalFile(UtilIOTest.TEST_DIRECTORY_ZIP)));

      UtilIO.delete(UtilIO.obtainExternalFile(UtilIOTest.TEST_DIRECTORY_UNZIP));
   }

   /**
    * Test of {@link UtilIO#write(File, File)}
    *
    * @throws IOException
    *            On copy issue
    */
   @Test
   public void testWriteFileFile() throws IOException
   {
      UtilIO.write(UtilIO.obtainExternalFile(UtilIOTest.TEST_DIRECTORY_UNZIP_FILE), UtilIO.obtainExternalFile(UtilIOTest.TEST_DIRECTORY_ZIP_FILE));

      Assert.assertTrue(UtilIOTest.compareFiles(UtilIO.obtainExternalFile(UtilIOTest.TEST_DIRECTORY_UNZIP_FILE),
            UtilIO.obtainExternalFile(UtilIOTest.TEST_DIRECTORY_ZIP_FILE)));

      UtilIO.delete(UtilIO.obtainExternalFile(UtilIOTest.TEST_DIRECTORY_ZIP_FILE));
   }

   /**
    * Test of {@link UtilIO#zip(File, File)}
    *
    * @throws IOException
    *            On zipping issue
    */
   @Test
   public void testZipFileFile() throws IOException
   {
      UtilIO.zip(UtilIO.obtainExternalFile(UtilIOTest.TEST_DIRECTORY_ZIP), UtilIO.obtainExternalFile(UtilIOTest.TEST_DIRECTORY_ZIP_FILE));

      // {@todo} TODO Find a way to compare 2 zip file (without extract them if possible)
      Debug.printTodo("Find a way to compare 2 zip file (without extract them if possible)");

      // Assert.assertTrue(UtilIOTest.compareFiles(UtilIO.obtainExternalFile(UtilIOTest.TEST_DIRECTORY_ZIP_RESULT_FILE),
      // UtilIO.obtainExternalFile(UtilIOTest.TEST_DIRECTORY_ZIP_FILE)));

      UtilIO.delete(UtilIO.obtainExternalFile(UtilIOTest.TEST_DIRECTORY_ZIP_FILE));
   }
}