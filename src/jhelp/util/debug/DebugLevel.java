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
package jhelp.util.debug;

/**
 * Debug level
 * 
 * @author JHelp
 */
public enum DebugLevel
{
   /** Debug level */
   DEBUG(3, "DEBUG : "),
   /** Error level */
   ERROR(0, "$@! ERROR !@$ : "),
   /** Information level */
   INFORMATION(2, "INFORMATION : "),
   /** Verbose level */
   VERBOSE(4, "VERBOSE : "),
   /** Warning level */
   WARNING(1, "/!\\ WARNING /!\\ : ");

   /** Header */
   private String header;
   /** Level */
   private int    level;

   /**
    * Create a new instance of DebugLevel
    * 
    * @param level
    *           Level
    * @param header
    *           Header
    */
   DebugLevel(final int level, final String header)
   {
      this.level = level;
      this.header = header;
   }

   /**
    * Header
    * 
    * @return Header
    */
   public String getHeader()
   {
      return this.header;
   }

   /**
    * Level
    * 
    * @return Level
    */
   public int getLevel()
   {
      return this.level;
   }
}