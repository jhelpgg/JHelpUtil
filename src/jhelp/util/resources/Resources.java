package jhelp.util.resources;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Hashtable;
import java.util.StringTokenizer;
import java.util.jar.JarFile;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

import jhelp.util.debug.Debug;
import jhelp.util.gui.GIF;
import jhelp.util.gui.JHelpFont;
import jhelp.util.gui.JHelpImage;
import jhelp.util.image.pcx.PCX;
import jhelp.util.io.UtilIO;

/**
 * Access to internal resources.<br>
 * We strongly recommend to use the {@link Resources#Resources(Class)} constructor, for resources embed in different jar
 * than
 * the JHelp utilities.<br>
 * For resources inside same jar as JHelp utilities, both constructor works
 *
 * @author JHelp
 */
public class Resources
{
    /**
     * Indicates if resources are outside the jar
     */
    private final boolean                         externalFiles;
    /**
     * Reference class
     */
    private final Class<?>                        referenceClass;
    /**
     * Path relative to Resources class
     */
    private final String                          relativePathFormClass;
    /**
     * Map of already loaded resources text
     */
    private final Hashtable<String, ResourceText> resourcesTexts;
    /**
     * Base directory
     */
    private       File                            baseDirectory;
    /**
     * Resources system associated to the resources
     */
    private       ResourcesSystem                 resourcesSystem;

    /**
     * Create a new instance of Resources.<br>
     * The given path are relative next to the jar that contains this class
     */
    public Resources()
    {
        this.referenceClass = null;
        this.relativePathFormClass = null;
        this.resourcesTexts = new Hashtable<String, ResourceText>();
        this.externalFiles = true;
    }

    /**
     * Create a new instance of Resources with a reference class.<br>
     * The class reference must be in same jar as resources, all the given path will be relative to this given class
     *
     * @param referenceClass Reference class
     */
    public Resources(final Class<?> referenceClass)
    {
        this.referenceClass = referenceClass;
        this.relativePathFormClass = null;
        this.resourcesTexts = new Hashtable<String, ResourceText>();
        this.externalFiles = false;
    }

    /**
     * Create a new instance of Resources based on directory
     *
     * @param directory Directory base
     */
    public Resources(final File directory)
    {
        if ((!directory.exists()) || (!directory.isDirectory()))
        {
            throw new IllegalArgumentException(directory.getAbsolutePath() + " doesn't exits or not a directory");
        }

        this.referenceClass = null;
        this.relativePathFormClass = null;
        this.resourcesTexts = new Hashtable<String, ResourceText>();
        this.externalFiles = true;
        this.baseDirectory = directory;
    }

    /**
     * Create a new instance of Resources with a relative base path.<br>
     * Resources to reach must be in same jar as this class, the path given is relative to this Resources class. The path
     * will be relative to the given path.
     *
     * @param pathOfEmbedResources Relative path where found resources
     */
    public Resources(final String pathOfEmbedResources)
    {
        this.referenceClass = null;

        StringTokenizer stringTokenizer = new StringTokenizer(pathOfEmbedResources, "./\\:,;!|", false);
        final int       numberPath      = stringTokenizer.countTokens();
        final String[]  path            = new String[numberPath];

        for (int i = 0; i < numberPath; i++)
        {
            path[i] = stringTokenizer.nextToken();
        }

        stringTokenizer = new StringTokenizer(Resources.class.getPackage()
                                                             .getName(), "./\\:,;!|", false);
        final int      numberPack = stringTokenizer.countTokens();
        final String[] pack       = new String[numberPack];

        for (int i = 0; i < numberPack; i++)
        {
            pack[i] = stringTokenizer.nextToken();
        }

        final int limit       = Math.min(numberPath, numberPack);
        int       indexCommon = -1;

        for (int i = 0; i < limit; i++, indexCommon++)
        {
            if (!pack[i].equals(path[i]))
            {
                break;
            }
        }

        final StringBuilder stringBuilder = new StringBuilder();

        for (int i = numberPack - 1; i > indexCommon; i--)
        {
            stringBuilder.append("../");
        }

        for (int i = indexCommon + 1; i < numberPath; i++)
        {
            stringBuilder.append(path[i]);
            stringBuilder.append('/');
        }

        this.relativePathFormClass = stringBuilder.toString();

        this.resourcesTexts = new Hashtable<String, ResourceText>();
        this.externalFiles = false;
    }

    /**
     * Obtain a buffered image
     *
     * @param path Relative path of the image (Separator is "/")
     * @return The buffered image
     * @throws IOException On reading resource failure
     */
    public BufferedImage obtainBufferedImage(final String path) throws IOException
    {
        return ImageIO.read(this.obtainResourceStream(path));
    }

    /**
     * Open stream to a resource
     *
     * @param path Relative path of the resource (Separator is "/")
     * @return Opened stream or null if the resource not found
     */
    public InputStream obtainResourceStream(final String path)
    {
        if (this.externalFiles)
        {
            try
            {
                if (this.baseDirectory != null)
                {
                    return new FileInputStream(new File(this.baseDirectory, path));
                }

                return new FileInputStream(UtilIO.obtainExternalFile(path));
            }
            catch (final FileNotFoundException exception)
            {
                Debug.printException(exception);

                return null;
            }
        }

        if (this.referenceClass != null)
        {
            return this.referenceClass.getResourceAsStream(path);
        }

        return Resources.class.getResourceAsStream(this.relativePathFormClass + path);
    }

    /**
     * Get a GIF image from resources
     *
     * @param source Source path
     * @return The GIF image OR {@code null} if the resource path not exists or not a GIF
     */
    public GIF obtainGIF(final String source)
    {
        try
        {
            return new GIF(this.obtainResourceStream(source));
        }
        catch (final Exception exception)
        {
            Debug.printException(exception, "Failed to load gif : ", source);
        }

        return null;
    }

    /**
     * Obtain a image icon
     *
     * @param path Relative path of the image (Separator is "/")
     * @return The buffered image
     */
    public ImageIcon obtainImageIcon(final String path)
    {
        return new ImageIcon(this.obtainResourceURL(path));
    }

    /**
     * URL of a resource
     *
     * @param path Relative path of the resource (Separator is "/")
     * @return URL or null if the resource not found
     */
    public URL obtainResourceURL(final String path)
    {
        if (this.externalFiles)
        {
            try
            {
                if (this.baseDirectory != null)
                {
                    return (new File(this.baseDirectory, path)).toURI()
                                                               .toURL();
                }

                return (UtilIO.obtainExternalFile(path)).toURI()
                                                        .toURL();
            }
            catch (final MalformedURLException exception)
            {
                Debug.printException(exception);

                return null;
            }
        }

        if (this.referenceClass != null)
        {
            return this.referenceClass.getResource(path);
        }

        return Resources.class.getResource(this.relativePathFormClass + path);
    }

    /**
     * Obtain a font embed in resources
     *
     * @param type Font type
     * @param path Resource path
     * @param size Font size
     * @return Created font
     */
    public JHelpFont obtainJHelpFont(final JHelpFont.Type type, final String path, final int size)
    {
        return this.obtainJHelpFont(type, path, size, JHelpFont.Value.FREE, JHelpFont.Value.FREE, false);
    }

    /**
     * Obtain a font embed in resources
     *
     * @param type      Font type
     * @param path      Resource path
     * @param size      Font size
     * @param bold      Bold value
     * @param italic    Italic value
     * @param underline Indicates if have to underline
     * @return Created font
     */
    public JHelpFont obtainJHelpFont(final JHelpFont.Type type, final String path, final int size,
                                     final JHelpFont.Value bold, final JHelpFont.Value italic,
                                     final boolean underline)
    {
        return JHelpFont.createFont(type, this.obtainResourceStream(path), size, bold, italic, underline);
    }

    /**
     * Obtain an image from resources resized to given size
     *
     * @param path   Resource path
     * @param width  Desired width
     * @param height Desired height
     * @return Resized image
     */
    public JHelpImage obtainResizedJHelpImage(final String path, final int width, final int height)
    {
        return JHelpImage.createResizedImage(this.obtainJHelpImage(path), width, height);
    }

    /**
     * Obtain a {@link JHelpImage}
     *
     * @param path Relative path of the image (Separator is "/")
     * @return The image
     */
    public JHelpImage obtainJHelpImage(final String path)
    {
        try
        {
            return JHelpImage.loadImage(this.obtainResourceStream(path));
        }
        catch (final Exception exception)
        {
            try
            {
                final PCX pcx = new PCX(this.obtainResourceStream(path));
                return pcx.createImage();
            }
            catch (final Exception exception2)
            {
                return JHelpImage.DUMMY;
            }
        }
    }

    /**
     * Obtain the resources system linked to the resources
     *
     * @return Resources system linked
     * @throws IOException If failed to create the resources system
     */
    public ResourcesSystem obtainResourcesSystem() throws IOException
    {
        if (this.resourcesSystem == null)
        {
            if (this.externalFiles)
            {
                if (this.baseDirectory != null)
                {
                    this.resourcesSystem = new ResourcesSystem(this, this.baseDirectory);
                }
                else
                {
                    this.resourcesSystem = new ResourcesSystem(this, UtilIO.obtainOutsideDirectory());
                }
            }
            else
            {
                Class<?> clas = this.referenceClass;

                if (clas == null)
                {
                    clas = Resources.class;
                }

                final URL    url   = clas.getResource(clas.getSimpleName() + ".class");
                final String path  = url.getFile();
                final int    index = path.indexOf(".jar!");

                if (index < 0)
                {
                    final File file = new File(path);
                    this.resourcesSystem = new ResourcesSystem(this, file.getParentFile());
                }
                else
                {
                    final JarFile jarFile = new JarFile(path.substring(5, index + 4));
                    final int     end     = Math.max(index + 5, path.lastIndexOf('/'));
                    this.resourcesSystem = new ResourcesSystem(this, jarFile, path.substring(index + 6, end));
                }
            }
        }

        return this.resourcesSystem;
    }

    /**
     * Obtain a resource of texts
     *
     * @param path Relative path of the resource of texts (Separator is "/")
     * @return Resources of text or null if the resource not found
     */
    public ResourceText obtainResourceText(final String path)
    {
        ResourceText resourceText = this.resourcesTexts.get(path);

        if (resourceText != null)
        {
            return resourceText;
        }

        resourceText = new ResourceText(this, path);
        this.resourcesTexts.put(path, resourceText);

        return resourceText;
    }
}