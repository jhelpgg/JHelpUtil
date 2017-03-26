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
 * Task that return nothing
 *
 * @author JHelp <br>
 * @param <PARAMETER>
 *           task parameter type
 */
public interface VoidTask<PARAMETER>
      extends ActionTask<PARAMETER, Void>
{
   /**
    * Do the action <br>
    * <br>
    * <b>Parent documentation:</b><br>
    * {@inheritDoc}
    *
    * @param parameter
    *           Task parameter
    * @return {@code null}
    * @see jhelp.util.thread.ActionTask#doAction(java.lang.Object)
    */
   @Override
   public default Void doAction(final PARAMETER parameter)
   {
      this.doVoidAction(parameter);
      return null;
   }

   /**
    * Do the action
    *
    * @param parameter
    *           Action parameter
    */
   public void doVoidAction(PARAMETER parameter);
}