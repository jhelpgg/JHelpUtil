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
package jhelp.util.commands;

import java.io.File;
import java.util.List;

interface CommandsProvider
{
   List<String>[] createChangeWallPaperCommands(File wallpaper);

   String extractIPFormResultLines(List<String> linesResult);

   void fillCommandForGetIP(List<String> command);

   List<String> openFileExplorerCommand(File file);
}