package jhelp.util.list.foreach;

/**
 * Listener to alert when : {@link ForEach#forEachAsync(java.util.Collection, ActionEach, ForEachAsyncListener, int)},
 * {@link ForEach#forEachAsync(jhelp.util.list.SortedArray, ActionEach, ForEachAsyncListener, int)},
 * {@link ForEach#forEachAsync(java.util.Collection, FilterEach, ActionEach, ForEachAsyncListener, int)} or
 * {@link ForEach#forEachAsync(jhelp.util.list.SortedArray, FilterEach, ActionEach, ForEachAsyncListener, int)} is terminated
 * 
 * @author JHelp
 */
public interface ForEachAsyncListener
{
   /**
    * Called when {@link ForEach#forEachAsync(java.util.Collection, ActionEach, ForEachAsyncListener, int)},
    * {@link ForEach#forEachAsync(jhelp.util.list.SortedArray, ActionEach, ForEachAsyncListener, int)},
    * {@link ForEach#forEachAsync(java.util.Collection, FilterEach, ActionEach, ForEachAsyncListener, int)} or
    * {@link ForEach#forEachAsync(jhelp.util.list.SortedArray, FilterEach, ActionEach, ForEachAsyncListener, int)} is terminated
    * 
    * @param forEachID
    *           For each ID
    */
   public void forEachAsyncTerminated(int forEachID);
}