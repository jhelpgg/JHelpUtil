package jhelp.util.list.foreach;

/**
 * Action to do on each element in {@link ForEach}.
 *
 * @param <ELEMENT> Element type
 * @author JHelp
 */
public interface ActionEach<ELEMENT>
{
    /**
     * Do the action on element.
     *
     * @param element Element where do the action
     */
    public void doAction(ELEMENT element);

    /**
     * Called if any error is throw while doing the task.
     *
     * @param element Element with error happen
     * @param error   Error throw
     */
    public void report(ELEMENT element, Error error);

    /**
     * Called if any exception is throw while doing the task.
     *
     * @param element   Element with exception happen
     * @param exception Exception throw
     */
    public void report(ELEMENT element, Exception exception);
}