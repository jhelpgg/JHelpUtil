/**
 * <h1>License :</h1> <br>
 * The following code is deliver as is. I take care that code compile and work, but I am not responsible about any damage it may
 * cause.<br>
 * You can use, modify, the code as your need for any usage. But you can't do any action that avoid me or other person use,
 * modify this code. The code is free for usage and modification, you can't change that fact.<br>
 * <br>
 *
 * @author JHelp
 */
package jhelp.util.classLoader;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import jhelp.util.debug.Debug;
import jhelp.util.list.EnumerationIterator;
import jhelp.util.text.UtilText;

/**
 * Loader of class, can add several other class loader and also individual .class file<br>
 * <br>
 * Last modification : 26 mai 2010<br>
 * Version 0.0.0<br>
 *
 * @author JHelp
 */
public class JHelpClassLoader
      extends ClassLoader
{
   /** File list */
   private ArrayList<File>             files;
   /** Already loaded classes */
   private Hashtable<String, Class<?>> loadedClass;
   /** Class loaders */
   private ArrayList<ClassLoader>      loaders;
   /** Manually defined classes waiting to be load at least one time. map : class complete name <=> byte code */
   private Map<String, byte[]>         manualDefined;

   /**
    * Constructs JHelpClassLoader
    */
   public JHelpClassLoader()
   {
      this.initialize();
   }

   /**
    * Constructs JHelpClassLoader
    *
    * @param parent
    *           Class loader parent
    */
   public JHelpClassLoader(final ClassLoader parent)
   {
      super(parent);

      this.initialize();
   }

   /**
    * Initialize the loader
    */
   private void initialize()
   {
      this.files = new ArrayList<File>();
      this.loaders = new ArrayList<ClassLoader>();
      this.loadedClass = new Hashtable<String, Class<?>>();
      this.manualDefined = new HashMap<String, byte[]>();
   }

   /**
    * Load a class
    *
    * @param name
    *           Class complete name
    * @param resolve
    *           Indicates if need to resolve
    * @return Loaded class
    * @throws ClassNotFoundException
    *            If class is not found
    * @see java.lang.ClassLoader#loadClass(java.lang.String, boolean)
    */
   @Override
   protected synchronized Class<?> loadClass(final String name, final boolean resolve) throws ClassNotFoundException
   {
      Class<?> clazz = this.loadedClass.get(name);

      if(clazz != null)
      {
         return clazz;
      }

      final byte[] code = this.manualDefined.get(name);

      if(code != null)
      {
         this.manualDefined.remove(name);
         clazz = this.defineClass(name, code, 0, code.length);

         if(resolve)
         {
            this.resolveClass(clazz);
         }

         this.loadedClass.put(name, clazz);
         return clazz;
      }

      final String[] path = UtilText.cutSringInPart(name, '.');
      path[path.length - 1] += ".class";

      File file = null;
      File tempFile;
      int index;

      for(final File f : this.files)
      {
         tempFile = f;
         index = path.length - 1;

         while((tempFile != null) && (tempFile.getName()
                                              .equals(path[index])))
         {
            tempFile = tempFile.getParentFile();
            index--;

            if(index < 0)
            {
               file = f;
               break;
            }
         }

         if(file != null)
         {
            break;
         }
      }

      if(file != null)
      {
         try
         {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

            InputStream inputStream = new FileInputStream(file);
            byte[] temp = new byte[4096];

            int read = inputStream.read(temp);
            while(read >= 0)
            {
               byteArrayOutputStream.write(temp, 0, read);

               read = inputStream.read(temp);
            }

            byteArrayOutputStream.flush();
            byteArrayOutputStream.close();
            inputStream.close();
            inputStream = null;

            temp = byteArrayOutputStream.toByteArray();
            byteArrayOutputStream = null;

            clazz = this.defineClass(name, temp, 0, temp.length);
            temp = null;

            if(resolve)
            {
               this.resolveClass(clazz);
            }

            this.loadedClass.put(name, clazz);

            return clazz;
         }
         catch(final Exception exception)
         {
            Debug.printException(exception);
         }
      }

      for(final ClassLoader classLoader : this.loaders)
      {
         try
         {
            clazz = classLoader.loadClass(name);

            if(clazz != null)
            {
               this.loadedClass.put(name, clazz);

               return clazz;
            }
         }
         catch(final Exception exception)
         {
            Debug.printException(exception);
         }
      }

      clazz = super.loadClass(name, resolve);
      if(clazz != null)
      {
         this.loadedClass.put(name, clazz);

         return clazz;
      }

      throw new ClassNotFoundException("Can't find : " + name);
   }

   /**
    * Add class loader
    *
    * @param classLoader
    *           Class loader to add
    */
   public void add(final ClassLoader classLoader)
   {
      if(!this.loaders.contains(classLoader))
      {
         this.loaders.add(classLoader);
      }
   }

   /**
    * Add a file. <br>
    * The file must be in the same hierarchy as its package.<br>
    * for example for : pack1.pack2.pack3.MyClasss the path must end like this : .../pack1/pack2/pack3/MyClass.class
    *
    * @param file
    *           File to add
    */
   public void add(final File file)
   {
      if((file.exists()) && (!this.files.contains(file)))
      {
         this.files.add(file);
      }
   }

   /**
    * Add a class with its byte code
    *
    * @param name
    *           Class complete name
    * @param byteCode
    *           Class byte code
    * @throws IllegalArgumentException
    *            If class already loaded/resolved
    */
   public void addClass(final String name, final byte[] byteCode)
   {
      if(this.loadedClass.containsKey(name))
      {
         throw new IllegalArgumentException(name + " already loaded and it its impossible to unload");
      }

      this.manualDefined.put(name, byteCode);
   }

   /**
    * URL for a resources
    *
    * @param name
    *           Resource complete name
    * @return URL of resource or {@code null} if not found
    * @see java.lang.ClassLoader#getResource(java.lang.String)
    */
   @Override
   public URL getResource(final String name)
   {
      final String[] path = UtilText.cutSringInPart(name, '.');
      path[path.length - 1] += ".class";

      File file = null;
      File tempFile;
      int index;

      for(final File f : this.files)
      {
         tempFile = f;
         index = path.length - 1;

         if(tempFile.getName()
                    .equals(path[index]))
         {
            while((tempFile != null) && (tempFile.getName()
                                                 .equals(path[index])))
            {
               tempFile = tempFile.getParentFile();
               index--;

               if(index < 0)
               {
                  file = f;
                  break;
               }
            }

            if(file != null)
            {
               break;
            }
         }
      }

      if(file != null)
      {
         try
         {
            return file.toURI().toURL();
         }
         catch(final MalformedURLException e)
         {
            Debug.printException(e);
         }
      }

      URL url = null;

      for(final ClassLoader classLoader : this.loaders)
      {
         url = classLoader.getResource(name);

         if(url != null)
         {
            return url;
         }
      }

      return super.getResource(name);
   }

   /**
    * Get resource as stream for read
    *
    * @param name
    *           Resource complete name
    * @return Stream for read or {@code null} if not found
    * @see java.lang.ClassLoader#getResourceAsStream(java.lang.String)
    */
   @Override
   public InputStream getResourceAsStream(final String name)
   {
      try
      {
         //noinspection ConstantConditions
         return this.getResource(name).openStream();
      }
      catch(final Exception e)
      {
         Debug.printException(e);

         return null;
      }
   }

   /**
    * List of resources of same name
    *
    * @param name
    *           Resource complete name
    * @return List of resource
    * @throws IOException
    *            On reading problem
    * @see java.lang.ClassLoader#getResources(java.lang.String)
    */
   @Override
   public Enumeration<URL> getResources(final String name) throws IOException
   {
      final ArrayList<URL> urls = new ArrayList<URL>();

      final String[] path = UtilText.cutSringInPart(name, '.');
      path[path.length - 1] += ".class";

      File tempFile;
      int index;

      for(final File f : this.files)
      {
         tempFile = f;
         index = path.length - 1;

         if(tempFile.getName()
                    .equals(path[index]))
         {
            while((tempFile != null) && (tempFile.getName()
                                                 .equals(path[index])))
            {
               tempFile = tempFile.getParentFile();
               index--;

               if(index < 0)
               {
                  try
                  {
                     urls.add(f.toURI().toURL());
                  }
                  catch(final MalformedURLException e)
                  {
                     Debug.printException(e);
                  }

                  break;
               }
            }
         }
      }

      for(final ClassLoader classLoader : this.loaders)
      {
         for(final URL u : new EnumerationIterator<URL>(classLoader.getResources(name)))
         {
            urls.add(u);
         }
      }

      for(final URL u : new EnumerationIterator<URL>(super.getResources(name)))
      {
         urls.add(u);
      }

      return new EnumerationIterator<URL>(urls.iterator());
   }

   /**
    * Indicates if a class is already loaded/resolved (So can't be changed)
    *
    * @param name
    *           Class complete name
    * @return {@code true} if loaded/resolved
    */
   public boolean isLoaded(final String name)
   {
      return this.loadedClass.containsKey(name);
   }
}