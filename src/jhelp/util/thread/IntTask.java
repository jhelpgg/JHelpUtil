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
 * Action that return an integer
 *
 * @author JHelp <br>
 * @param <PARAMETER>
 *           Action parameter type
 */
public interface IntTask<PARAMETER>
      extends ActionTask<PARAMETER, Integer>
{
   /**
    * Do the action <br>
    * <br>
    * <b>Parent documentation:</b><br>
    * {@inheritDoc}
    *
    * @param parameter
    *           Action parameter
    * @return Action result
    * @see jhelp.util.thread.ActionTask#doAction(java.lang.Object)
    */
   @Override
   public default Integer doAction(final PARAMETER parameter)
   {
      return this.doIntAction(parameter);
   }

   /**
    * Do the action
    *
    * @param parameter
    *           Action parameter
    * @return Action result
    */
   public int doIntAction(PARAMETER parameter);
}
