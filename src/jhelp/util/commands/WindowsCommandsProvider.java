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

import jhelp.util.debug.Debug;
import jhelp.util.debug.DebugLevel;

class WindowsCommandsProvider
      implements CommandsProvider
{
   @Override
   public List<String>[] createChangeWallPaperCommands(final File wallpaper)
   {
      // {@todo} TODO Implements createChangeWallPaperCommands
      Debug.printTodo("Implements createChangeWallPaperCommands");
      return null;
   }

   @Override
   public String extractIPFormResultLines(final List<String> linesResult)
   {
      for(final String line : linesResult)
      {
         Debug.println(DebugLevel.VERBOSE, line);
      }

      // {@todo} TODO Implements extractIPFormResultLines in jhelp.util.commands [JHelpUtil]
      Debug.printTodo("Implements extractIPFormResultLines in jhelp.util.commands [JHelpUtil]");

      return "127.0.0.1";
   }

   @Override
   public void fillCommandForGetIP(final List<String> command)
   {
      command.add("ipconfig");
   }

   @Override
   public List<String> openFileExplorerCommand(final File file)
   {
      // {@todo} TODO Implements openFileExplorerCommand
      Debug.printTodo("Implements openFileExplorerCommand");
      return null;
   }
}