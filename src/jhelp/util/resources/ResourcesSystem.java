package jhelp.util.resources;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import jhelp.util.io.UtilIO;
import jhelp.util.list.EnumerationIterator;

/**
 * System file like to see embed resources files as inside a browser.<br>
 * With it you can explore resources files tree.<br>
 * If resources are embed inside a jar only read operations are availables, if there are in outside directory read and write
 * operations are possible.<br>
 * The path separator is /, what ever the system you are, convertions are made for you.<br>
 * To have an instance use {@link Resources#obtainResourcesSystem()}
 * 
 * @author JHelp
 */
public class ResourcesSystem
{
   /** Represents the resources files tree root */
   public static final ResourceDirectory ROOT = new ResourceDirectory("");
   /** For read resources inside a jar */
   private final JarFile                 jarFile;
   /** {@link Resources} reference to find resources */
   private final Resources               resources;
   /** Outside directory where lays the resources (if they are in outside directory) */
   private final File                    rootFile;
   /** Root path inside the jar (if resources are inside a jar) */
   private final String                  rootPath;

   /**
    * Create a new instance of ResourcesSystem in outside directory mode
    * 
    * @param resources
    *           {@link Resources} reference to find resources
    * @param rootFile
    *           Outside root directory
    */
   ResourcesSystem(final Resources resources, final File rootFile)
   {
      this.resources = resources;
      this.rootFile = rootFile;
      this.jarFile = null;
      this.rootPath = null;
   }

   /**
    * Create a new instance of ResourcesSystem in jar embed mode
    * 
    * @param resources
    *           {@link Resources} reference to find resources
    * @param jarFile
    *           Jar file that contains resources
    * @param rootPath
    *           Root opath inside the jar
    */
   ResourcesSystem(final Resources resources, final JarFile jarFile, final String rootPath)
   {
      this.resources = resources;
      this.rootFile = null;
      this.jarFile = jarFile;
      this.rootPath = rootPath;
   }

   /**
    * Create a resources directory (Only available if resources are in outside directory, check with {@link #insideJar()})
    * 
    * @param resourceDirectory
    *           Resources directory to create
    * @return {@code true} if directory created (or allready exists). {@code false} if failed to create (Two situations, inside
    *         a jar or the system forbid it)
    */
   public boolean createDirectory(final ResourceDirectory resourceDirectory)
   {
      if(resourceDirectory == null)
      {
         throw new NullPointerException("resourceDirectory musn't be null");
      }

      if(this.rootFile == null)
      {
         return false;
      }

      return UtilIO.createDirectory(new File(this.rootFile, resourceDirectory.getPath()));
   }

   /**
    * Create a resources file (Only available if resources are in outside directory, check with {@link #insideJar()})
    * 
    * @param resourceFile
    *           Resources file to create
    * @return {@code true} if file created (or allready exists). {@code false} if failed to create (Two situations, inside a jar
    *         or the system forbid it)
    */
   public boolean createFile(final ResourceFile resourceFile)
   {
      if(resourceFile == null)
      {
         throw new NullPointerException("resourceFile musn't be null");
      }

      if(this.rootFile == null)
      {
         return false;
      }

      return UtilIO.createFile(new File(this.rootFile, resourceFile.getPath()));
   }

   /**
    * Obtain the parent of a resource element (file or directory).<br>
    * {@code null} is return for the parent of the root directory (By definition root don't have parent)
    * 
    * @param resourceElement
    *           Resource elemnt to have its parent directory
    * @return Resource directory parent or {@code null} if the elemnt is the root directory
    */
   public ResourceDirectory getParent(final ResourceElement resourceElement)
   {
      if(resourceElement == null)
      {
         throw new NullPointerException("resourceElement musn't be null");
      }

      String path = resourceElement.getPath();

      if(path.length() == 0)
      {
         return null;
      }

      if(resourceElement.isDirectory() == true)
      {
         path = path.substring(0, path.length() - 1);
      }

      final int index = path.lastIndexOf('/');

      if(index < 0)
      {
         return ResourcesSystem.ROOT;
      }

      return new ResourceDirectory(path.substring(0, index + 1));
   }

   /**
    * Indicates if resources are inside a jar
    * 
    * @return {@code true} if resources are inside a jar. {@code false} if resources are outside directory
    */
   public boolean insideJar()
   {
      return this.rootFile == null;
   }

   /**
    * Test if a resource element (file or directory) exists.
    * 
    * @param resourceElement
    *           Resource element to test
    * @return {@code true} if element exists
    */
   public boolean isExists(final ResourceElement resourceElement)
   {
      if(resourceElement == null)
      {
         throw new NullPointerException("resourceElement musn't be null");
      }

      if(this.rootFile != null)
      {
         final File file = new File(this.rootFile, resourceElement.getPath());
         return file.exists();
      }

      String name = this.rootPath;

      if(name.length() > 0)
      {
         name += "/";
      }

      name += resourceElement.getPath();

      final JarEntry jarEntry = this.jarFile.getJarEntry(name);
      return jarEntry != null;
   }

   /**
    * Obtain a input stram for read a resource file
    * 
    * @param resourceFile
    *           Resource file to read
    * @return Stream on the resource file
    */
   public InputStream obtainInputStream(final ResourceFile resourceFile)
   {
      if(resourceFile == null)
      {
         throw new NullPointerException("resourceFile musn't be null");
      }

      return this.resources.obtainResourceStream(resourceFile.getPath());
   }

   /**
    * Obtain the list of resources elements (files or directories) inside a resource directory
    * 
    * @param resourceDirectory
    *           Resource directory to obtain its list of elements
    * @return List of resources elements inside the given directory
    */
   public List<ResourceElement> obtainList(final ResourceDirectory resourceDirectory)
   {
      if(resourceDirectory == null)
      {
         throw new NullPointerException("resourceDirectory musn't be null");
      }

      final String path = resourceDirectory.getPath();
      final List<ResourceElement> list = new ArrayList<ResourceElement>();

      if(this.rootFile != null)
      {
         final File directory = new File(this.rootFile, path);
         String elementPath;

         for(final File file : directory.listFiles())
         {
            if(path.length() > 0)
            {
               elementPath = path + file.getName();
            }
            else
            {
               elementPath = file.getName();
            }

            if(file.isDirectory() == true)
            {
               list.add(new ResourceDirectory(elementPath));
            }
            else
            {
               list.add(new ResourceFile(elementPath));
            }
         }

         return list;
      }

      String start = this.rootPath;

      if(start.length() > 0)
      {
         start += "/";
      }

      final int indexRoot = start.length();

      if(path.length() > 0)
      {
         start += path;
      }

      final int min = start.length();
      int index;
      String name;

      for(final JarEntry entry : new EnumerationIterator<JarEntry>(this.jarFile.entries()))
      {
         name = entry.getName();

         if((name.length() > min) && (name.startsWith(start) == true))
         {
            index = name.indexOf('/', min + 1);

            if((index == (name.length() - 1)) || ((index < 0) && (name.endsWith("/") == true)))
            {
               list.add(new ResourceDirectory(name.substring(indexRoot)));
            }
            else if(index < 0)
            {
               list.add(new ResourceFile(name.substring(indexRoot)));
            }

         }
      }

      return list;
   }

   /**
    * Obtain the list of resources elements child of an other resource element.<br>
    * If the resource element is a file, {@code null} is return because a file can't have any children
    * 
    * @param resourceElement
    *           Resource elemnt to have its children
    * @return List of children or {@code null} if given element is a file
    */
   public List<ResourceElement> obtainList(final ResourceElement resourceElement)
   {
      if(resourceElement == null)
      {
         throw new NullPointerException("resourceElement musn't be null");
      }

      if(resourceElement.isDirectory() == false)
      {
         return null;
      }

      return this.obtainList((ResourceDirectory) resourceElement);
   }

   /**
    * Obtain an outputstream to write a resource file.<br>
    * Only available if resources are in outside directory (Can be check with {@link #insideJar()})
    * 
    * @param resourceFile
    *           Resource file to have stream for write
    * @return Output stream for write the file
    * @throws IOException
    *            If resources are inside a jar, or the file don't exists and can't be created (system not allow it)
    */
   public OutputStream obtainOutputStream(final ResourceFile resourceFile) throws IOException
   {
      if(resourceFile == null)
      {
         throw new NullPointerException("resourceFile musn't be null");
      }

      if(this.rootFile == null)
      {
         throw new IOException("Impossible to create output stream in resources inside a jar !");
      }

      final File file = new File(this.rootFile, resourceFile.getPath());

      if(UtilIO.createFile(file) == false)
      {
         throw new IOException("Failed to create new resource : " + resourceFile.getPath() + "[" + file.getAbsolutePath() + "]");
      }

      return new FileOutputStream(file);
   }

   /**
    * Obtain the real file or directory corresponds to a resource element if resources are in outside directory
    * 
    * @param resourceElement
    *           Resource element to have its real file
    * @return Real file or {@code null} if resources are inside a jar
    */
   public File obtainReaFile(final ResourceElement resourceElement)
   {
      if((this.rootFile == null) || (resourceElement == null))
      {
         return null;
      }

      if(ResourcesSystem.ROOT.equals(resourceElement) == true)
      {
         return this.rootFile;
      }

      return new File(this.rootFile, resourceElement.getPath());
   }
}