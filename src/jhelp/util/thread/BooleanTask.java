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
 * Action return boolean value
 *
 * @author JHelp <br>
 * @param <PARAMETER>
 *           Action parameter type
 */
public interface BooleanTask<PARAMETER>
      extends ActionTask<PARAMETER, Boolean>
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
   public default Boolean doAction(final PARAMETER parameter)
   {
      return this.doBooleanAction(parameter);
   }

   /**
    * Do the action
    *
    * @param parameter
    *           Action parameter
    * @return Action result
    */
   public boolean doBooleanAction(PARAMETER parameter);
}
