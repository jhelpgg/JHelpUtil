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
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import jhelp.util.debug.Debug;
import jhelp.util.list.EnumerationIterator;
import jhelp.util.text.UtilText;

/**
 * Loader of class inside several jars<br>
 * <br>
 * Last modification : 26 mai 2009<br>
 * Version 0.0.0<br>
 *
 * @author JHelp
 */
public class JHelpJarClassLoader
      extends ClassLoader
{
   /** List of jars */
   private final ArrayList<JarFile>          arrayList;
   /** Reference class loader */
   private final ClassLoader                 classLoader;

   /** Already loaded classes */
   private final Hashtable<String, Class<?>> loadedClass;
   /** Current spy/listener */
   private JHelpJarClassLoaderSpy            spy;

   /**
    * Constructs JHelpClassLoader
    */
   public JHelpJarClassLoader()
   {
      this.classLoader = null;

      this.arrayList = new ArrayList<JarFile>();
      this.loadedClass = new Hashtable<String, Class<?>>();
   }

   /**
    * Create a new instance of JHelpJarClassLoader
    *
    * @param classLoader
    *           Reference class loader
    */
   public JHelpJarClassLoader(final ClassLoader classLoader)
   {
      this.classLoader = classLoader;

      this.arrayList = new ArrayList<JarFile>();
      this.loadedClass = new Hashtable<String, Class<?>>();
   }

   /**
    * Load a class
    *
    * @param name
    *           Class complete name
    * @param resolve
    *           Indicates if have to resolve
    * @return Loaded class
    * @throws ClassNotFoundException
    *            If file not exists in one of jars and can't be retrieve by default class loader
    * @see java.lang.ClassLoader#loadClass(java.lang.String, boolean)
    */
   @Override
   protected synchronized Class<?> loadClass(final String name, final boolean resolve) throws ClassNotFoundException
   {
      Class<?> clazz = this.loadedClass.get(name);

      if(clazz != null)
      {
         if(this.spy != null)
         {
            this.spy.aleradyKnown(clazz);
         }

         return clazz;
      }

      JarEntry jarEntry = null;
      JarFile jar = null;
      final String entryName = name.replace('.', '/') + ".class";
      for(final JarFile jarFile : this.arrayList)
      {
         jarEntry = jarFile.getJarEntry(entryName);
         if(jarEntry != null)
         {
            jar = jarFile;
            break;
         }
      }

      if((jar != null) && (jarEntry != null))
      {
         try
         {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

            InputStream inputStream = jar.getInputStream(jarEntry);
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

            if(resolve == true)
            {
               this.resolveClass(clazz);
            }

            this.loadedClass.put(name, clazz);

            if(this.spy != null)
            {
               this.spy.loadByJHelpJarClassLoader(clazz);
            }

            return clazz;
         }
         catch(final Exception exception)
         {
            Debug.printException(exception);
         }
      }

      if(this.classLoader != null)
      {
         try
         {
            clazz = this.classLoader.loadClass(name);
            if(clazz != null)
            {
               this.loadedClass.put(name, clazz);

               if(this.spy != null)
               {
                  this.spy.loadByOtherClassLoader(clazz);
               }

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

         if(this.spy != null)
         {
            this.spy.loadByDefaultClassLoader(clazz);
         }

         return clazz;
      }

      if(this.spy != null)
      {
         this.spy.notLoad(name);
      }

      throw new ClassNotFoundException("Can't find : " + name);
   }

   /**
    * Add a jar file
    *
    * @param file
    *           Jar file to add
    * @throws IOException
    *            If file is not a jar file
    */
   public void add(final File file) throws IOException
   {
      this.arrayList.add(new JarFile(file));
   }

   /**
    * Add a jar file
    *
    * @param jarFile
    *           Jar file to add
    */
   public void add(final JarFile jarFile)
   {
      this.arrayList.add(jarFile);
   }

   /**
    * Add a jar file
    *
    * @param name
    *           Jar file path
    * @throws IOException
    *            If path is not a jar file
    */
   public void add(final String name) throws IOException
   {
      this.arrayList.add(new JarFile(name));
   }

   /**
    * Get URL for a resource
    *
    * @param name
    *           Resource complete name
    * @return URL on resource or {@code null} if resource not found
    * @see java.lang.ClassLoader#getResource(java.lang.String)
    */
   @Override
   public URL getResource(final String name)
   {
      JarEntry jarEntry = null;
      for(final JarFile jarFile : this.arrayList)
      {
         jarEntry = jarFile.getJarEntry(name);
         if(jarEntry != null)
         {
            try
            {
               return new URL(UtilText.concatenate("jar:file:", jarFile.getName(), "!/", name));
            }
            catch(final Exception exception)
            {
               Debug.printException(exception);
            }
         }
      }

      return super.getResource(name);
   }

   /**
    * Get stream for read a resource
    *
    * @param name
    *           Resource complete name
    * @return Stream for read or {@code null} if resource not found
    * @see java.lang.ClassLoader#getResourceAsStream(java.lang.String)
    */
   @Override
   public InputStream getResourceAsStream(final String name)
   {
      try
      {
         return this.getResource(name).openStream();
      }
      catch(final Exception e)
      {
         Debug.printException(e);

         return null;
      }
   }

   /**
    * Get all resources with same name
    *
    * @param name
    *           Resource name
    * @return List of resources URL
    * @throws IOException
    *            On creating list issus
    * @see java.lang.ClassLoader#getResources(java.lang.String)
    */
   @Override
   public Enumeration<URL> getResources(final String name) throws IOException
   {
      final ArrayList<URL> urls = new ArrayList<URL>();

      JarEntry jarEntry = null;
      for(final JarFile jarFile : this.arrayList)
      {
         jarEntry = jarFile.getJarEntry(name);
         if(jarEntry != null)
         {
            try
            {
               urls.add(new URL(UtilText.concatenate("jar:file:", jarFile.getName(), "!/", name)));
            }
            catch(final Exception exception)
            {
               Debug.printException(exception);
            }
         }
      }

      final Enumeration<URL> enumeration = super.getResources(name);
      while(enumeration.hasMoreElements() == true)
      {
         urls.add(enumeration.nextElement());
      }

      return new EnumerationIterator<URL>(urls.iterator());
   }

   /**
    * List of classes are directly internal to a given one
    *
    * @param name
    *           Class base name
    * @return List of internal
    */
   public HashSet<String> listOfDirectInternal(final String name)
   {
      final HashSet<String> list = new HashSet<String>();

      final String plus = name.replace('.', '/') + '$';
      final int index = plus.length();
      Enumeration<JarEntry> enumeration;
      JarEntry jarEntry;
      String nameEntry;
      int ind;

      for(final JarFile jarFile : this.arrayList)
      {
         enumeration = jarFile.entries();
         while(enumeration.hasMoreElements() == true)
         {
            jarEntry = enumeration.nextElement();
            nameEntry = jarEntry.getName();
            if(nameEntry.endsWith(".class") && (nameEntry.startsWith(plus) == true) && (nameEntry.indexOf('$', index) < 0))
            {
               ind = nameEntry.lastIndexOf('.');
               list.add(nameEntry.substring(0, ind).replace('/', '.'));
            }
         }
      }

      return list;
   }

   /**
    * Load a class
    *
    * @param name
    *           Class complete name
    * @return Loaded class
    * @throws ClassNotFoundException
    *            If class is not found
    * @see java.lang.ClassLoader#loadClass(java.lang.String)
    */
   @Override
   public Class<?> loadClass(final String name) throws ClassNotFoundException
   {
      return this.loadClass(name, true);
   }

   /**
    * Indicates if given resource is defined inside of one of embed jar
    *
    * @param name
    *           Resource path
    * @return {@code true} if given resource is defined inside of one of embed jar
    */
   public boolean resourceDefinedInside(final String name)
   {
      JarEntry jarEntry = null;

      for(final JarFile jarFile : this.arrayList)
      {
         jarEntry = jarFile.getJarEntry(name);

         if(jarEntry != null)
         {
            return true;
         }
      }

      return false;
   }

   /**
    * Define the spy/listener of activities
    *
    * @param spy
    *           Spy/listener to register
    */
   public void setSpy(final JHelpJarClassLoaderSpy spy)
   {
      this.spy = spy;
   }

   /**
    * Un load a class
    *
    * @param name
    *           Class complete name
    */
   public void unloadClass(final String name)
   {
      this.loadedClass.remove(name);
   }
}