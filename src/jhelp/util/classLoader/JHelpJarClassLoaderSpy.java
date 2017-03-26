package jhelp.util.classLoader;

/**
 * Spy/listener of {@link JHelpJarClassLoader} activity
 *
 * @author JHelp <br>
 */
@SuppressWarnings("rawtypes")
public interface JHelpJarClassLoaderSpy
{
    /**
     * Called if loading class is already know
     *
     * @param clas Loading class
     */
    public void alreadyKnown(Class clas);

    /**
     * Called if loading class is load by default class loader
     *
     * @param clas Loading class
     */
    public void loadByDefaultClassLoader(Class clas);

    /**
     * Called if loading class is load by the {@link JHelpJarClassLoader}
     *
     * @param clas Loading class
     */
    public void loadByJHelpJarClassLoader(Class clas);

    /**
     * Called if loading class is load by an other class loader
     *
     * @param clas Loading class
     */
    public void loadByOtherClassLoader(Class clas);

    /**
     * Called if loading class can't be load
     *
     * @param className Loading class name
     */
    public void notLoad(String className);
}