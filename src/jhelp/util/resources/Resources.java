package jhelp.util.resources;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Hashtable;
import java.util.StringTokenizer;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

import jhelp.util.gui.JHelpImage;

/**
 * Access to internal resources.<br>
 * We strongly recommend to use the {@link Resources#Resources(Class)} constructor, for resources embed in different jar than
 * the JHelp utilities.<br>
 * For resources inside same jar as JHelp utilities, both constructor works
 * 
 * @author JHelp
 */
public class Resources
{
   /** Reference class */
   private final Class<?>                        referenceClass;
   /** Path relative to Resources class */
   private final String                          relativePathFormClass;
   /** Map of already loaded resources text */
   private final Hashtable<String, ResourceText> resourcesTexts;

   /**
    * Create a new instance of Resources with a reference class.<br>
    * The class reference must be in same jar as resources, all the given path will be relative to this given class
    * 
    * @param referenceClass
    *           Reference class
    */
   public Resources(final Class<?> referenceClass)
   {
      this.referenceClass = referenceClass;
      this.relativePathFormClass = null;
      this.resourcesTexts = new Hashtable<String, ResourceText>();
   }

   /**
    * Create a new instance of Resources with a relative base path.<br>
    * Resources to reach must be in same jar as this class, the path given is relative to this Resources class. Then givn path
    * will be relative to the given path.
    * 
    * @param pathOfEmbedResources
    *           Relative path where found resources
    */
   public Resources(final String pathOfEmbedResources)
   {
      this.referenceClass = null;

      StringTokenizer stringTokenizer = new StringTokenizer(pathOfEmbedResources, "./\\:,;!|", false);
      final int numberPath = stringTokenizer.countTokens();
      final String[] path = new String[numberPath];

      for(int i = 0; i < numberPath; i++)
      {
         path[i] = stringTokenizer.nextToken();
      }

      stringTokenizer = new StringTokenizer(Resources.class.getPackage().getName(), "./\\:,;!|", false);
      final int numberPack = stringTokenizer.countTokens();
      final String[] pack = new String[numberPack];

      for(int i = 0; i < numberPack; i++)
      {
         pack[i] = stringTokenizer.nextToken();
      }

      final int limit = Math.min(numberPath, numberPack);
      int indexCommon = -1;

      for(int i = 0; i < limit; i++, indexCommon++)
      {
         if(pack[i].equals(path[i]) == false)
         {
            break;
         }
      }

      final StringBuilder stringBuilder = new StringBuilder();

      for(int i = numberPack - 1; i > indexCommon; i--)
      {
         stringBuilder.append("../");
      }

      for(int i = indexCommon + 1; i < numberPath; i++)
      {
         stringBuilder.append(path[i]);
         stringBuilder.append('/');
      }

      this.relativePathFormClass = stringBuilder.toString();

      this.resourcesTexts = new Hashtable<String, ResourceText>();
   }

   /**
    * Obtain a buffered image
    * 
    * @param path
    *           Relative path of the image (Separator is "/")
    * @return The buffered image
    * @throws IOException
    *            On reading resource failure
    */
   public BufferedImage obtainBufferedImage(final String path) throws IOException
   {
      return ImageIO.read(this.obtainResourceStream(path));
   }

   /**
    * Obtain a image icon
    * 
    * @param path
    *           Relative path of the image (Separator is "/")
    * @return The buffered image
    */
   public ImageIcon obtainImageIcon(final String path)
   {
      return new ImageIcon(this.obtainResourceURL(path));
   }

   /**
    * Obtain a {@link JHelpImage}
    * 
    * @param path
    *           Relative path of the image (Separator is "/")
    * @return The buffered image
    * @throws IOException
    *            On reading resource failure
    */
   public JHelpImage obtainJHelpImage(final String path) throws IOException
   {
      return JHelpImage.loadImage(this.obtainResourceStream(path));
   }

   /**
    * Open stream to a resource
    * 
    * @param path
    *           Relative path of the resource (Separator is "/")
    * @return Opened stream or null if the resource not found
    */
   public InputStream obtainResourceStream(final String path)
   {
      if(this.referenceClass != null)
      {
         return this.referenceClass.getResourceAsStream(path);
      }

      return Resources.class.getResourceAsStream(this.relativePathFormClass + path);
   }

   /**
    * Obtain a resource of texts
    * 
    * @param path
    *           Relative path of the resource of texts (Separator is "/")
    * @return Resources of text or null if the resource not found
    */
   public ResourceText obtainResourceText(final String path)
   {
      ResourceText resourceText = this.resourcesTexts.get(path);

      if(resourceText != null)
      {
         return resourceText;
      }

      resourceText = new ResourceText(this, path);
      this.resourcesTexts.put(path, resourceText);

      return resourceText;
   }

   /**
    * URL of a resource
    * 
    * @param path
    *           Relative path of the resource (Separator is "/")
    * @return URL or null if the resource not found
    */
   public URL obtainResourceURL(final String path)
   {
      if(this.referenceClass != null)
      {
         return this.referenceClass.getResource(path);
      }

      return Resources.class.getResource(this.relativePathFormClass + path);
   }
}