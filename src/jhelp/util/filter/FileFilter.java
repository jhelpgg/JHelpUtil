/**
 * <h1>License :</h1> <br>
 * The following code is deliver as is. I take care that code compile and work, but I am not responsible about any
 * damage it may
 * cause.<br>
 * You can use, modify, the code as your need for any usage. But you can't do any action that avoid me or other person use,
 * modify this code. The code is free for usage and modification, you can't change that fact.<br>
 * <br>
 *
 * @author JHelp
 */
package jhelp.util.filter;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;

import jhelp.util.debug.Debug;
import jhelp.util.io.FileImageInformation;

/**
 * File filter on extension.<br>
 * You can add a second filter to filter more<br>
 * <br>
 * Last modification : 23 avr. 2009<br>
 * Version 0.0.0<br>
 *
 * @author JHelp
 */
public class FileFilter
        extends javax.swing.filechooser.FileFilter
        implements FilenameFilter, java.io.FileFilter
{
    /** Indicates if hidden files are accepted */
    private final boolean            acceptHidden;
    /** Indicates if virtual links are accepted */
    private final boolean            acceptVirtualLink;
    /** Filtered extensions */
    private final ArrayList<String>  extentions;
    /** Indicates if directories are accepted */
    private       boolean            acceptDirectory;
    /** Filter information */
    private       String             information;
    /** Second filter */
    private       java.io.FileFilter secondFileFilter;

    /**
     * Constructs FileFilter
     */
    public FileFilter()
    {
        this(false, false);
    }

    /**
     * Create a new instance of FileFilter
     *
     * @param acceptHidden
     *           Indicates if hidden files are accepted
     * @param acceptVirtualLink
     *           Indicates if virtual links files are accepted
     */
    public FileFilter(final boolean acceptHidden, final boolean acceptVirtualLink)
    {
        this.extentions = new ArrayList<String>();
        this.information = "All";

        this.acceptHidden = acceptHidden;
        this.acceptVirtualLink = acceptVirtualLink;
        this.acceptDirectory = true;
    }

    /**
     * Create filter initialize to filter images
     *
     * @return Created filter
     */
    public static FileFilter createFilterForImage()
    {
        return FileFilter.createFilterForImage(false, false);
    }

    /**
     * Create a filter for image
     *
     * @param acceptHidden
     *           Indicates if hidden files are accepted
     * @param acceptVirtualLink
     *           Indicates if virtual links are accepted
     * @return Created filter
     */
    public static FileFilter createFilterForImage(final boolean acceptHidden, final boolean acceptVirtualLink)
    {
        final FileFilter fileFilter = new FileFilter(acceptHidden, acceptVirtualLink);

        fileFilter.addExtension("png");
        fileFilter.addExtension("gif");
        fileFilter.addExtension("jpg");
        fileFilter.addExtension("bmp");
        fileFilter.addExtension("pcx");

        fileFilter.setInformation("Images");

        return fileFilter;
    }

    /**
     * Add an extension in the filter
     *
     * @param extension
     *           Extension added
     */
    public void addExtension(String extension)
    {
        if (extension == null)
        {
            throw new NullPointerException("extension MUST NOT be null");
        }

        extension = extension.trim()
                             .toLowerCase();

        if (extension.length() == 0)
        {
            throw new NullPointerException("extension MUST NOT be empty");
        }

        this.extentions.add(extension);
    }

    /**
     * Create a file filter for image based on image information not on file extension.<br>
     * Same as {@link #createFilterForImageByFileImageInformation(boolean, boolean)
     * createFilterForImageByFileImageInformation(false, false)}
     *
     * @return Created file filter
     */
    public static FileFilter createFilterForImageByFileImageInformation()
    {
        return FileFilter.createFilterForImageByFileImageInformation(false, false);
    }

    /**
     * Create a file filter for image based on image information not on file extension.
     *
     * @param acceptHidden
     *           Indicates if hidden files are accepted
     * @param acceptVirtualLink
     *           Indicates if virtual link are accepted
     * @return Created file filter
     */
    public static FileFilter createFilterForImageByFileImageInformation(final boolean acceptHidden,
                                                                        final boolean acceptVirtualLink)
    {
        final FileFilter fileFilter = new FileFilter(acceptHidden, acceptVirtualLink);

        fileFilter.setSecondFileFilter(FileImageInformation.FILTER_BY_FILE_INFORMATION);
        fileFilter.setInformation("Images");

        return fileFilter;
    }

    /**
     * Create a file filter for sounds (Supported by JHelpSounds) that didn't accepts hidden files neither virtual links
     *
     * @return Created filter
     */
    public static FileFilter createFilterForSound()
    {
        return FileFilter.createFilterForSound(false, false);
    }

    /**
     * Create a file filter for sounds (Supported by JHelpSounds)
     *
     * @param acceptHidden
     *           Indicates if hidden files or directory are allowed
     * @param acceptVirtualLink
     *           Indicated if virtual links are allowed
     * @return Created filter
     */
    public static FileFilter createFilterForSound(final boolean acceptHidden, final boolean acceptVirtualLink)
    {
        final FileFilter fileFilter = new FileFilter(acceptHidden, acceptVirtualLink);

        fileFilter.addExtension("mp3");
        fileFilter.addExtension("au");
        fileFilter.addExtension("wav");
        fileFilter.addExtension("mid");

        fileFilter.setInformation("Sounds");

        return fileFilter;
    }

    /**
     * Create filter initialize to filter videos (Supported by JHelpMultimedia)
     *
     * @return Created filter
     */
    public static FileFilter createFilterForVideos()
    {
        return FileFilter.createFilterForVideos(false, false);
    }

    /**
     * Create a filter for videos (Supported by JHelpMultimedia)
     *
     * @param acceptHidden
     *           Indicates if hidden files are accepted
     * @param acceptVirtualLink
     *           Indicates if virtual links are accepted
     * @return Created filter
     */
    public static FileFilter createFilterForVideos(final boolean acceptHidden, final boolean acceptVirtualLink)
    {
        final FileFilter fileFilter = new FileFilter(acceptHidden, acceptVirtualLink);

        fileFilter.addExtension("avi");
        fileFilter.addExtension("mkv");
        fileFilter.addExtension("mov");
        fileFilter.addExtension("mp4");
        fileFilter.addExtension("mpg");
        fileFilter.addExtension("flv");

        fileFilter.setInformation("Videos");

        return fileFilter;
    }

    /**
     * Indicates if a file pass this filter
     *
     * @param dir
     *           Directory path
     * @param name
     *           File name
     * @return {@code true} if the file pass this filter
     * @see java.io.FilenameFilter#accept(java.io.File, java.lang.String)
     */
    @Override
    public boolean accept(final File dir, final String name)
    {
        return this.accept(new File(dir, name));
    }

    /**
     * Indicates if a file pass this filter
     *
     * @param file
     *           File test
     * @return {@code true} if the file pass
     * @see javax.swing.filechooser.FileFilter#accept(java.io.File)
     * @see java.io.FileFilter#accept(java.io.File)
     */
    @Override
    public boolean accept(final File file)
    {
        try
        {
            if ((file == null) //
                    || ((!this.acceptHidden) && (file.isHidden()))//
                    || ((!this.acceptVirtualLink) && (!file.getCanonicalPath()
                                                           .equals(file.getAbsolutePath())))//
                    || (!file.exists())//
                    || (!file.canRead()))
            {
                return false;
            }
        }
        catch (final Exception exception)
        {
            Debug.printException(exception, "Issue while filter : ", file.getAbsolutePath());

            return false;
        }

        if ((this.secondFileFilter != null) && (!this.secondFileFilter.accept(file)))
        {
            return false;
        }

        if (file.isDirectory())
        {
            return this.acceptDirectory;
        }

        return this.isFiltered(file.getName());
    }

    /**
     * Filter description
     *
     * @return Filter description
     * @see javax.swing.filechooser.FileFilter#getDescription()
     */
    @Override
    public String getDescription()
    {
        final StringBuilder stringBuffer = new StringBuilder();
        if (this.information != null)
        {
            stringBuffer.append(this.information);
            stringBuffer.append(" [");
        }

        stringBuffer.append(this.filter());

        if (this.information != null)
        {
            stringBuffer.append("]");
        }

        return stringBuffer.toString();
    }

    /**
     * Filter string
     *
     * @return Filter string
     */
    public String filter()
    {
        final int size = this.extentions.size();
        if (size == 0)
        {
            return "*";
        }

        final StringBuilder stringBuffer = new StringBuilder();

        stringBuffer.append("*.");
        stringBuffer.append(this.extentions.get(0));

        for (int i = 1; i < size; i++)
        {
            stringBuffer.append(";*.");
            stringBuffer.append(this.extentions.get(i));
        }

        return stringBuffer.toString();
    }

    /**
     * Indicates if a file is filter
     *
     * @param fileName
     *           File name
     * @return {@code true} if the file is filter
     */
    public boolean isFiltered(String fileName)
    {
        if (this.extentions.isEmpty())
        {
            return true;
        }

        final int index = fileName.lastIndexOf('.');
        if (index < 0)
        {
            return false;
        }

        fileName = fileName.substring(index + 1)
                           .toLowerCase();

        for (final String extention : this.extentions)
        {
            if (extention.equals(fileName))
            {
                return true;
            }
        }

        return false;
    }

    /**
     * Get an extention in the filter
     *
     * @param index
     *           Extention index
     * @return Extention
     */
    public String getExtention(final int index)
    {
        return this.extentions.get(index);
    }

    /**
     * Return information
     *
     * @return information
     */
    public String getInformation()
    {
        return this.information;
    }

    /**
     * Modify information
     *
     * @param information
     *           New information value
     */
    public void setInformation(final String information)
    {
        this.information = information;
    }

    /**
     * Return secondFileFilter
     *
     * @return secondFileFilter
     */
    public java.io.FileFilter getSecondFileFilter()
    {
        return this.secondFileFilter;
    }

    /**
     * Modify secondFileFilter
     *
     * @param secondFileFilter
     *           New secondFileFilter value
     */
    public void setSecondFileFilter(final java.io.FileFilter secondFileFilter)
    {
        this.secondFileFilter = secondFileFilter;
    }

    /**
     * Indicates if directories are accepted
     *
     * @return {@code true} if directories are accepted
     */
    public boolean isAcceptDirectory()
    {
        return this.acceptDirectory;
    }

    /**
     * Change the accept directories value
     *
     * @param acceptDirectory
     *           Accept or not directories
     */
    public void setAcceptDirectory(final boolean acceptDirectory)
    {
        this.acceptDirectory = acceptDirectory;
    }

    /**
     * Indicates if an extention is filter
     *
     * @param extention
     *           Extention test
     * @return {@code true} if the extention is filter
     */
    public boolean isAnExtentionFiltered(String extention)
    {
        if (extention == null)
        {
            throw new NullPointerException("extention MUST NOT be null");
        }

        extention = extention.trim()
                             .toLowerCase();

        if (extention.length() == 0)
        {
            throw new NullPointerException("extention MUST NOT be empty");
        }

        return this.extentions.contains(extention);
    }

    /**
     * Number of extentions
     *
     * @return Number of extentions
     */
    public int numberOfExtentions()
    {
        return this.extentions.size();
    }

    /**
     * Remove an extention
     *
     * @param extention
     *           Extension to remove
     */
    public void removeExtention(String extention)
    {
        if (extention == null)
        {
            throw new NullPointerException("extention MUST NOT be null");
        }

        extention = extention.trim()
                             .toLowerCase();

        if (extention.length() == 0)
        {
            throw new NullPointerException("extention MUST NOT be empty");
        }

        this.extentions.remove(extention);
    }
}