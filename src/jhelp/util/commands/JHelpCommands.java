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
package jhelp.util.commands;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import jhelp.util.debug.Debug;
import jhelp.util.debug.DebugLevel;

public class JHelpCommands
{
    private static final CommandsProvider COMMANDS_PROVIDER;

    static
    {
        CommandsProvider commandsProvider = null;

        String osName = System.getProperty("os.name");

        if (osName != null)
        {
            Debug.printMark(DebugLevel.VERBOSE, osName);

            osName = osName.toLowerCase();

            if (osName.indexOf("linux") >= 0)
            {
                commandsProvider = new LinuxCommandsProvider();
            }
            else if (osName.indexOf("win") >= 0)
            {
                commandsProvider = new WindowsCommandsProvider();
            }
        }

        COMMANDS_PROVIDER = commandsProvider;
    }

    public static boolean changeWallpaper(final File wallpaper)
    {
        if ((!wallpaper.exists()) || (!wallpaper.canRead()) || (!wallpaper.isFile()))
        {
            throw new IllegalArgumentException(
                    "The file " + wallpaper.getAbsolutePath() + " doesn't exits OR can't be read OR is not a file");
        }

        final List<String>[] commands = JHelpCommands.COMMANDS_PROVIDER.createChangeWallPaperCommands(wallpaper);

        if (commands == null)
        {
            return false;
        }

        ProcessBuilder processBuilder;
        for (final List<String> command : commands)
        {
            processBuilder = new ProcessBuilder(command);

            try
            {
                processBuilder.start();// JHelpCommands.showLog(processBuilder);
            }
            catch (final IOException exception)
            {
                Debug.printException(exception, "Issue while apply command : ", command);
                return false;
            }
        }

        return true;
    }

    public static List<String> executeCommand(final String... command)
    {
        final ArrayList<String> resultLines = new ArrayList<String>();

        final ProcessBuilder processBuilder = new ProcessBuilder(command);

        BufferedReader bufferedReader = null;

        try
        {
            final Process process = processBuilder.start();

            bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()));

            String line = bufferedReader.readLine();

            while (line != null)
            {
                resultLines.add(line);

                line = bufferedReader.readLine();
            }
        }
        catch (final Exception exception)
        {
            Debug.printException(exception, "Execute '", command, "' failed");
        }
        finally
        {
            if (bufferedReader != null)
            {
                try
                {
                    bufferedReader.close();
                }
                catch (final Exception ignored)
                {
                }
            }
        }

        return resultLines;
    }

    public static void executeCommandLog(final List<String> command)
    {
        final ProcessBuilder processBuilder = new ProcessBuilder(command);

        try
        {
            JHelpCommands.showLog(processBuilder);
        }
        catch (final Exception exception)
        {
            Debug.printException(exception, "Execute '", command, "' failed");
        }
    }

    private static void showLog(final ProcessBuilder processBuilder) throws IOException
    {
        final Process process = processBuilder.start();

        Debug.printMark(DebugLevel.INFORMATION, "NORMAL");

        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()));

        String line = bufferedReader.readLine();

        while (line != null)
        {
            Debug.println(DebugLevel.VERBOSE, line);

            line = bufferedReader.readLine();
        }

        Debug.printMark(DebugLevel.INFORMATION, "ERRORS");

        bufferedReader = new BufferedReader(new InputStreamReader(process.getErrorStream()));

        line = bufferedReader.readLine();

        while (line != null)
        {
            Debug.println(DebugLevel.WARNING, line);

            line = bufferedReader.readLine();
        }

    }

    public static void executeCommandLog(final String... command)
    {
        final ProcessBuilder processBuilder = new ProcessBuilder(command);

        try
        {
            JHelpCommands.showLog(processBuilder);
        }
        catch (final Exception exception)
        {
            Debug.printException(exception, "Execute '", command, "' failed");
        }
    }

    public static void executeCommandQuiet(final List<String> command)
    {
        final ProcessBuilder processBuilder = new ProcessBuilder(command);

        try
        {
            processBuilder.start();
        }
        catch (final Exception exception)
        {
            Debug.printException(exception, "Execute '", command, "' failed");
        }
    }

    public static void executeCommandQuiet(final String... command)
    {
        final ProcessBuilder processBuilder = new ProcessBuilder(command);

        try
        {
            processBuilder.start();
        }
        catch (final Exception exception)
        {
            Debug.printException(exception, "Execute '", command, "' failed");
        }
    }

    public static void launchClass(final File directory, final String mainClass, final String... classPath)
    {
        if (mainClass == null)
        {
            throw new NullPointerException("mainClass MUST NOT be null");
        }

        final ArrayList<String> command = new ArrayList<String>();

        command.add("java");

        if ((classPath != null) && (classPath.length > 0))
        {
            command.add("-classpath");

            final StringBuilder path = new StringBuilder();
            path.append(classPath[0]);

            for (int i = 1; i < classPath.length; i++)
            {
                path.append(':');
                path.append(classPath[i]);
            }

            command.add(path.toString());
        }

        command.add(mainClass);

        final ProcessBuilder processBuilder = new ProcessBuilder(command);

        if (directory != null)
        {
            processBuilder.directory(directory);
        }

        try
        {
            JHelpCommands.showLog(processBuilder);
        }
        catch (final IOException exception)
        {
            // {@todo} TODO Check if print exception is enough
            Debug.printTodo("Check if print exception is enough");
            Debug.printException(exception);
        }
    }

    public static void launchJAR(final File directory, final String jar, final String... classPath)
    {
        if (jar == null)
        {
            throw new NullPointerException("jar MUST NOT be null");
        }

        final ArrayList<String> command = new ArrayList<String>();

        command.add("java");

        if ((classPath != null) && (classPath.length > 0))
        {
            command.add("-classpath");

            final StringBuilder path = new StringBuilder();
            path.append(classPath[0]);

            for (int i = 1; i < classPath.length; i++)
            {
                path.append(':');
                path.append(classPath[i]);
            }

            command.add(path.toString());
        }

        command.add("-jar");
        command.add(jar);

        final ProcessBuilder processBuilder = new ProcessBuilder(command);

        if (directory != null)
        {
            processBuilder.directory(directory);
        }

        try
        {
            JHelpCommands.showLog(processBuilder);
        }
        catch (final IOException exception)
        {
            // {@todo} TODO Check if print exception is enough
            Debug.printTodo("Check if print exception is enough");
            Debug.printException(exception);
        }
    }

    public static String obtainLocalIP()
    {
        if (JHelpCommands.COMMANDS_PROVIDER == null)
        {
            return "127.0.0.1";
        }

        final ArrayList<String> command = new ArrayList<String>();

        JHelpCommands.COMMANDS_PROVIDER.fillCommandForGetIP(command);

        return JHelpCommands.COMMANDS_PROVIDER.extractIPFormResultLines(JHelpCommands.executeCommand(command));
    }

    public static List<String> executeCommand(final List<String> command)
    {
        final ArrayList<String> resultLines = new ArrayList<String>();

        final ProcessBuilder processBuilder = new ProcessBuilder(command);

        BufferedReader bufferedReader = null;

        try
        {
            final Process process = processBuilder.start();

            bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()));

            String line = bufferedReader.readLine();

            while (line != null)
            {
                resultLines.add(line);

                line = bufferedReader.readLine();
            }
        }
        catch (final Exception exception)
        {
            Debug.printException(exception, "Execute '", command, "' failed");
        }
        finally
        {
            if (bufferedReader != null)
            {
                try
                {
                    bufferedReader.close();
                }
                catch (final Exception ignored)
                {
                }
            }
        }

        return resultLines;
    }

    public static boolean openFileExplorer(final File file)
    {
        final List<String> command = JHelpCommands.COMMANDS_PROVIDER.openFileExplorerCommand(file);
        if (command == null)
        {
            return false;
        }

        final ProcessBuilder processBuilder = new ProcessBuilder(command);

        try
        {
            processBuilder.start();// JHelpCommands.showLog(processBuilder);
        }
        catch (final IOException exception)
        {
            Debug.printException(exception, "Issue while apply command : ", command);
            return false;
        }

        return true;
    }
}