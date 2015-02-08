package jhelp.util.thread;

import jhelp.util.list.Pair;

/**
 * Task that post a progress information
 * 
 * @author JHelp
 * @param <PROGRESS>
 *           Progression information type
 */
final class PostProgress<PROGRESS>
      extends ThreadedSimpleTask<Pair<ThreadedTask<?, ?, PROGRESS>, PROGRESS>>
{
   /**
    * Post the progression information <br>
    * <br>
    * <b>Parent documentation:</b><br>
    * {@inheritDoc}
    * 
    * @param parameter
    *           Pair of task to alert and the progression information to give
    * @see jhelp.util.thread.ThreadedSimpleTask#doSimpleAction(java.lang.Object)
    */
   @Override
   protected void doSimpleAction(final Pair<ThreadedTask<?, ?, PROGRESS>, PROGRESS> parameter)
   {
      parameter.element1.doProgress(parameter.element2);
   }
}