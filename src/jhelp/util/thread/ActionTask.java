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
 * Action that do something in its own thread.<br>
 * To launch the action use {@link ThreadManager#doAction(ActionTask, Object)}.<br>
 * To link it with then end of an other action use {@link FutureTask#then(ActionTask)}.<br>
 * Whatever the way you choose to launch it you will receive a {@link FutureTask} to let you manage its life cycle, do something
 * when action is finished or wait for the result.
 *
 * @author JHelp <br>
 * @param <PARAMETER>
 *           Action parameter type
 * @param <RESULT>
 *           Action result type
 */
public interface ActionTask<PARAMETER, RESULT>
{
   /**
    * Called in separate thread to do an action.
    *
    * @param parameter
    *           Action parameter
    * @return Action result
    */
   public RESULT doAction(PARAMETER parameter);
}
