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
package jhelp.util.thread;

/**
 * Task with no parameter
 *
 * @author JHelp <br>
 * @param <RESULT>
 *           Action result type
 */
public interface NoParameterTask<RESULT>
      extends ActionTask<Void, RESULT>
{
   /**
    * Do the action
    *
    * @return Action result
    */
   public RESULT doAction();

   /**
    * Do the action <br>
    * <br>
    * <b>Parent documentation:</b><br>
    * {@inheritDoc}
    *
    * @param parameter
    *           Unused
    * @return Action result
    * @see jhelp.util.thread.ActionTask#doAction(java.lang.Object)
    */
   @Override
   public default RESULT doAction(final Void parameter)
   {
      return this.doAction();
   }
}
