package jhelp.util.list.foreach;

import java.util.Collection;
import java.util.concurrent.atomic.AtomicInteger;

import jhelp.util.list.SortedArray;
import jhelp.util.list.Triplet;
import jhelp.util.thread.ThreadManager;
import jhelp.util.thread.ThreadedSimpleTask;
import jhelp.util.thread.ThreadedVerySimpleTask;

/**
 * For do same task in parallel in a list of elements.<br>
 * Its possible to filter elements where task is done
 *
 * @param <ELEMENT> Element of the list type
 * @author JHelp
 */
public class ForEach<ELEMENT>
{
    /**
     * Current task count
     */
    final AtomicInteger taskCount = new AtomicInteger();

    /**
     * Create a new instance of ForEach
     */
    ForEach()
    {
    }

    /**
     * Launch same action for each element in parallel of the given collection.<br>
     * The method waits that all parallel tasks are done before exit.
     *
     * @param <ELEMENT>  Collection element's type
     * @param collection Collection to get elements
     * @param action     Action to do on each element
     */
    public static <ELEMENT> void forEach(final Collection<ELEMENT> collection, final ActionEach<ELEMENT> action)
    {
        ForEach.forEach(collection, null, action);
    }

    /**
     * Launch same action for each filtered element in parallel of the given collection.<br>
     * The method waits that all parallel tasks are done before exit.
     *
     * @param <ELEMENT>  Collection element's type
     * @param collection Collection to get elements
     * @param filter     Filter to use. Use {@code null} for no filter (all elements are treated)
     * @param action     Action to do on each element
     */
    public static <ELEMENT> void forEach(final Collection<ELEMENT> collection, final FilterEach<ELEMENT> filter,
                                         final ActionEach<ELEMENT> action)
    {
        if (collection == null)
        {
            throw new NullPointerException("collection MUST NOT be null");
        }

        if (action == null)
        {
            throw new NullPointerException("action MUST NOT be null");
        }

        final ForEach<ELEMENT> forEach = new ForEach<ELEMENT>();
        forEach.launchForEach(collection, null, filter, action);
    }

    /**
     * Launch the action on each element in parallel
     *
     * @param collection  Collection to gets element (or {@code null} if elements are from a list)
     * @param sortedArray List to gets element (or {@code null} if elements are from a collection)
     * @param filter      Filter to use on each element (Or {@code null} if do for all elements)
     * @param action      Action to do on filtered elements
     */
    void launchForEach(final Collection<ELEMENT> collection, final SortedArray<ELEMENT> sortedArray,
                       final FilterEach<ELEMENT> filter,
                       final ActionEach<ELEMENT> action)
    {
        // Get the list of elements and initialize the number of tasks
        Iterable<ELEMENT> iterable;
        if (sortedArray != null)
        {
            this.taskCount.set(sortedArray.getSize());
            iterable = sortedArray;
        }
        else
        {
            this.taskCount.set(collection.size());
            iterable = collection;
        }

        // Initialize task for one element
        final Each each = new Each();

        // Launch in parallel the action on each elements
        for (final ELEMENT element : iterable)
        {
            ThreadManager.THREAD_MANAGER.doThread(each,
                                                  new Triplet<ELEMENT, FilterEach<ELEMENT>, ActionEach<ELEMENT>>(element,
                                                                                                                 filter,
                                                                                                                 action));
        }

        // Wait for all threads are finished
        synchronized (this.taskCount)
        {
            while (this.taskCount.get() > 0)
            {
                try
                {
                    this.taskCount.wait();
                }
                catch (final Exception ignored)
                {
                }
            }
        }
    }

    /**
     * Launch same action for each element in parallel of the given list.<br>
     * The method waits that all parallel tasks are done before exit.
     *
     * @param <ELEMENT>   List element's type
     * @param sortedArray List to get elements
     * @param action      Action to do on each element
     */
    public static <ELEMENT> void forEach(final SortedArray<ELEMENT> sortedArray, final ActionEach<ELEMENT> action)
    {
        ForEach.forEach(sortedArray, null, action);
    }

    /**
     * Launch same action for each filtered element in parallel of the given list.<br>
     * The method waits that all parallel tasks are done before exit.
     *
     * @param <ELEMENT>   List element's type
     * @param sortedArray List to get elements
     * @param filter      Filter to use. Use {@code null} for no filter (all elements are treated)
     * @param action      Action to do on each element
     */
    public static <ELEMENT> void forEach(final SortedArray<ELEMENT> sortedArray, final FilterEach<ELEMENT> filter,
                                         final ActionEach<ELEMENT> action)
    {
        if (sortedArray == null)
        {
            throw new NullPointerException("sortedArray MUST NOT be null");
        }

        if (action == null)
        {
            throw new NullPointerException("action MUST NOT be null");
        }

        final ForEach<ELEMENT> forEach = new ForEach<ELEMENT>();
        forEach.launchForEach(null, sortedArray, filter, action);
    }

    /**
     * Launch asynchronously the for each. Didn't wait that tasks are all done before exit, it launch the tasks and return
     * immediately
     *
     * @param <ELEMENT>            Element type
     * @param collection           Collection where found elements
     * @param action               Action to do on each elements
     * @param forEachAsyncListener Listener to call when for each is terminated (If {@code null} no callback)
     * @param forEachID            Identifier of for each
     */
    public static <ELEMENT> void forEachAsync(final Collection<ELEMENT> collection, final ActionEach<ELEMENT> action,
                                              final ForEachAsyncListener forEachAsyncListener, final int forEachID)
    {
        if (collection == null)
        {
            throw new NullPointerException("collection MUST NOT be null");
        }

        if (action == null)
        {
            throw new NullPointerException("action MUST NOT be null");
        }

        ThreadManager.THREAD_MANAGER.doThread(
                new ForEachAsync<ELEMENT>(collection, null, null, action, forEachAsyncListener, forEachID), null);
    }

    /**
     * Launch asynchronously the for each. Didn't wait that tasks are all done before exit, it launch the tasks and return
     * immediately
     *
     * @param <ELEMENT>            Element type
     * @param collection           Collection where found elements
     * @param filter               Filter to select elements to do the action ({@code null} for no filter)
     * @param action               Action to do on each filtered elements
     * @param forEachAsyncListener Listener to call when for each is terminated (If {@code null} no callback)
     * @param forEachID            Identifier of for each
     */
    public static <ELEMENT> void forEachAsync(final Collection<ELEMENT> collection, final FilterEach<ELEMENT> filter,
                                              final ActionEach<ELEMENT> action,
                                              final ForEachAsyncListener forEachAsyncListener, final int forEachID)
    {
        if (collection == null)
        {
            throw new NullPointerException("collection MUST NOT be null");
        }

        if (action == null)
        {
            throw new NullPointerException("action MUST NOT be null");
        }

        ThreadManager.THREAD_MANAGER.doThread(
                new ForEachAsync<ELEMENT>(collection, null, filter, action, forEachAsyncListener, forEachID), null);
    }

    /**
     * Launch asynchronously the for each. Didn't wait that tasks are all done before exit, it launch the tasks and return
     * immediately
     *
     * @param <ELEMENT>            Element type
     * @param sortedArray          Sorted array where found elements
     * @param action               Action to do on each elements
     * @param forEachAsyncListener Listener to call when for each is terminated (If {@code null} no callback)
     * @param forEachID            Identifier of for each
     */
    public static <ELEMENT> void forEachAsync(final SortedArray<ELEMENT> sortedArray, final ActionEach<ELEMENT> action,
                                              final ForEachAsyncListener forEachAsyncListener, final int forEachID)
    {
        if (sortedArray == null)
        {
            throw new NullPointerException("sortedArray MUST NOT be null");
        }

        if (action == null)
        {
            throw new NullPointerException("action MUST NOT be null");
        }

        ThreadManager.THREAD_MANAGER.doThread(
                new ForEachAsync<ELEMENT>(null, sortedArray, null, action, forEachAsyncListener, forEachID), null);
    }

    /**
     * Launch asynchronously the for each. Didn't wait that tasks are all done before exit, it launch the tasks and return
     * immediately
     *
     * @param <ELEMENT>            Element type
     * @param sortedArray          Sorted array where found elements
     * @param filter               Filter to select elements to do the action ({@code null} for no filter)
     * @param forEachAsyncListener Listener to call when for each is terminated (If {@code null} no callback)
     * @param forEachID            Identifier of for each
     * @param action               Action to do on each filtered elements
     */
    public static <ELEMENT> void forEachAsync(final SortedArray<ELEMENT> sortedArray, final FilterEach<ELEMENT> filter,
                                              final ActionEach<ELEMENT> action,
                                              final ForEachAsyncListener forEachAsyncListener, final int forEachID)
    {
        if (sortedArray == null)
        {
            throw new NullPointerException("sortedArray MUST NOT be null");
        }

        if (action == null)
        {
            throw new NullPointerException("action MUST NOT be null");
        }

        ThreadManager.THREAD_MANAGER.doThread(
                new ForEachAsync<ELEMENT>(null, sortedArray, filter, action, forEachAsyncListener, forEachID), null);
    }

    /**
     * Task for do asynchronously the for each
     *
     * @param <ELT> Element type
     * @author JHelp
     */
    static class ForEachAsync<ELT>
            extends ThreadedVerySimpleTask
    {
        /**
         * Action to do
         */
        private final ActionEach<ELT>      action;
        /**
         * List of element in collection
         */
        private final Collection<ELT>      collection;
        /**
         * filter to apply (may be {@code null}
         */
        private final FilterEach<ELT>      filter;
        /**
         * Listener to call when for each terminated
         */
        private final ForEachAsyncListener forEachAsyncListener;
        /**
         * For each ID
         */
        private final int                  forEachID;
        /**
         * Sorted list of element
         */
        private final SortedArray<ELT>     sortedArray;

        /**
         * Create a new instance of ForEachAsync
         *
         * @param collection           Collection of element ({@code null} if use sorted array)
         * @param sortedArray          Sorted array of element ({@code null} if use collection)
         * @param filter               Filter to apply ({@code null} if no filter)
         * @param action               Action to do on each filtered elements
         * @param forEachAsyncListener Listener to call when for each is terminated (If {@code null} no callback)
         * @param forEachID            Identifier of for each
         */
        ForEachAsync(final Collection<ELT> collection, final SortedArray<ELT> sortedArray, final FilterEach<ELT> filter,
                     final ActionEach<ELT> action,
                     final ForEachAsyncListener forEachAsyncListener, final int forEachID)
        {
            this.collection = collection;
            this.sortedArray = sortedArray;
            this.filter = filter;
            this.action = action;
            this.forEachAsyncListener = forEachAsyncListener;
            this.forEachID = forEachID;
        }

        /**
         * Launch the parallel actions <br>
         * <br>
         * <b>Parent documentation:</b><br>
         * {@inheritDoc}
         *
         * @see jhelp.util.thread.ThreadedVerySimpleTask#doVerySimpleAction()
         */
        @Override
        protected void doVerySimpleAction()
        {
            final ForEach<ELT> forEach = new ForEach<ELT>();
            forEach.launchForEach(this.collection, this.sortedArray, this.filter, this.action);

            if (this.forEachAsyncListener != null)
            {
                this.forEachAsyncListener.forEachAsyncTerminated(this.forEachID);
            }
        }
    }

    /**
     * Do task for one element
     *
     * @author JHelp
     */
    class Each
            extends ThreadedSimpleTask<Triplet<ELEMENT, FilterEach<ELEMENT>, ActionEach<ELEMENT>>>
    {
        /**
         * Create a new instance of Each
         */
        Each()
        {
        }

        /**
         * Do the task for one element <br>
         * <br>
         * <b>Parent documentation:</b><br>
         * {@inheritDoc}
         *
         * @param parameter Triplet of element, filter to use and action to do
         * @see jhelp.util.thread.ThreadedSimpleTask#doSimpleAction(java.lang.Object)
         */
        @Override
        protected void doSimpleAction(final Triplet<ELEMENT, FilterEach<ELEMENT>, ActionEach<ELEMENT>> parameter)
        {
            // Do the action if the element is filtered
            try
            {
                if ((parameter.element2 == null) || (parameter.element2.isFiltered(parameter.element1)))
                {
                    parameter.element3.doAction(parameter.element1);
                }
            }
            catch (final Exception exception)
            {
                parameter.element3.report(parameter.element1, exception);
            }
            catch (final Error error)
            {
                parameter.element3.report(parameter.element1, error);
            }
            finally
            {
                // One task is done
                synchronized (ForEach.this.taskCount)
                {
                    final int count = ForEach.this.taskCount.decrementAndGet();

                    if (count <= 0)
                    {
                        ForEach.this.taskCount.notify();
                    }
                }
            }
        }
    }
}