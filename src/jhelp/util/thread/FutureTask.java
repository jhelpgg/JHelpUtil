/**
 * <h1>License :</h1> <br>
 * The following code is deliver as is. I take care that code compile and work, but I am not responsible about any
 * damage it may
 * cause.<br>
 * You can use, modify, the code as your need for any usage. But you can't do any action that avoid me or other person use,
 * modify this code. The code is free for usage and modification, you can't change that fact.<br>
 * <br>
 *
 * @author JHelp
 */
package jhelp.util.thread;

import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Control and follow life cycle of {@link ActionTask}<br>
 * To have a new instance, use {@link ThreadManager#doAction(ActionTask, Object)} or {@link #then(ActionTask)}.
 *
 * @param <PARAMETER> Action parameter type
 * @param <RESULT>    Action result type
 * @author JHelp <br>
 */
public class FutureTask<PARAMETER, RESULT>
{
    /**
     * Fake future task : does nothing, always canceled
     */
    public static final  FutureTask<?, ?> FAKE_FUTURE_TASK = new FutureTask<>();
    /**
     * Cancel status
     */
    private static final int              CANCELED         = 3;
    /**
     * Computing status
     */
    private static final int              COMPUTING        = 0;
    /**
     * Error status
     */
    private static final int              ERROR            = 2;
    /**
     * Succeed status
     */
    private static final int              SUCCEED          = 1;
    /**
     * Current status
     */
    private final AtomicInteger         status;
    /**
     * Indicates if some thread wait for result
     */
    private final AtomicBoolean         waiting;
    /**
     * Task to do on error
     */
    private       ErrorTask             errorTask;
    /**
     * Filter to use on result
     */
    private       BooleanTask<RESULT>   filter;
    /**
     * Last error
     */
    private       Throwable             lastError;
    /**
     * Next future task
     */
    private       FutureTask<RESULT, ?> nextFutureTask;
    /**
     * Future task played on finish
     */
    private       FutureTask            finishFutureTask;
    /**
     * Next action to do if succeed
     */
    private       ActionTask<RESULT, ?> nextTask;
    /**
     * Action to do if succeed or failed
     */
    private       ActionTask            finishTask;
    /**
     * Parameter give to finish action
     */
    private       Object                finishParameter;
    /**
     * Parallel task
     */
    private       Promise               promise;
    /**
     * Action result
     */
    private       RESULT                result;

    /**
     * Create a new instance of FutureTask
     */
    private FutureTask()
    {
        this.status = new AtomicInteger(FutureTask.CANCELED);
        this.waiting = new AtomicBoolean(false);
    }

    /**
     * Create a new instance of FutureTask
     *
     * @param action Action to do
     */
    FutureTask(final ActionTask<PARAMETER, RESULT> action)
    {
        this.status = new AtomicInteger(FutureTask.COMPUTING);
        this.waiting = new AtomicBoolean(false);
        this.promise = new Promise(action);
    }

    /**
     * Do next action, if there one
     *
     * @param nextTask Next task to do
     * @return Future link to next action
     */
    @SuppressWarnings("unchecked")
    private FutureTask<RESULT, ?> doNextAction(final ActionTask<RESULT, ?> nextTask)
    {
        if (this == FutureTask.FAKE_FUTURE_TASK)
        {
            return (FutureTask<RESULT, ?>) FutureTask.FAKE_FUTURE_TASK;
        }

        if (this.filter != null)
        {
            try
            {
                if (!ThreadManager.THREAD_MANAGER.doAction(this.filter, this.result)
                                                 .get())
                {
                    this.doCancel();
                    return (FutureTask<RESULT, ?>) FutureTask.FAKE_FUTURE_TASK;
                }
            }
            catch (final Throwable exception)
            {
                this.failed(exception);
                return (FutureTask<RESULT, ?>) FutureTask.FAKE_FUTURE_TASK;
            }
        }

        final FutureTask<RESULT, ?> future = this.obtainNextFutureTask(nextTask);
        future.play(this.result);
        return future;
    }

    /**
     * Do finish action, if there one
     *
     * @param finishTask Next task to do
     * @return Future link to next action
     */
    @SuppressWarnings("unchecked")
    private FutureTask<?, ?> doFinishAction(final ActionTask<?, ?> finishTask)
    {
        if (this == FutureTask.FAKE_FUTURE_TASK)
        {
            return FutureTask.FAKE_FUTURE_TASK;
        }

        final FutureTask future = this.obtainFinishTask(finishTask);
        future.play(this.finishParameter);
        return future;
    }

    /**
     * Obtain or create finish future linked to finish action
     *
     * @param finishTask Finish action
     * @return Finish future
     */
    @SuppressWarnings("unchecked")
    private FutureTask<?, ?> obtainFinishTask(ActionTask<?, ?> finishTask)
    {
        if (this.finishFutureTask == null)
        {
            this.finishFutureTask = new FutureTask(finishTask);
        }
        else
        {
            this.finishFutureTask.promise.setAction(finishTask);
        }

        return this.finishFutureTask;
    }

    /**
     * Play the parallel task
     *
     * @param parameter Action parameter
     */
    void play(final PARAMETER parameter)
    {
        if (this == FutureTask.FAKE_FUTURE_TASK)
        {
            return;
        }

        ThreadManager.THREAD_MANAGER.doThread(this.promise, parameter);
    }

    /**
     * Obtain or create next future linked to next action
     *
     * @param nextTask Next action
     * @return Next future
     */
    private FutureTask<RESULT, ?> obtainNextFutureTask(final ActionTask<RESULT, ?> nextTask)
    {
        if (this.nextFutureTask == null)
        {
            this.nextFutureTask = new FutureTask<>(nextTask);
        }
        else
        {
            this.nextFutureTask.promise.setAction(nextTask);
        }

        return this.nextFutureTask;
    }

    /**
     * Cancel the action
     */
    void doCancel()
    {
        synchronized (this.status)
        {
            this.status.set(FutureTask.CANCELED);

            if (this.waiting.get())
            {
                this.waiting.set(false);
                this.status.notifyAll();
            }

            if (this.nextFutureTask != null)
            {
                this.nextFutureTask.cancel();
            }

            this.nextTask = null;
            this.errorTask = null;
            this.promise = null;
        }
    }

    /**
     * Called if action failed
     *
     * @param error Error happen
     */
    @SuppressWarnings("unchecked")
    void failed(final Throwable error)
    {
        synchronized (this.status)
        {
            this.status.set(FutureTask.ERROR);
            this.lastError = error;

            if (this.waiting.get())
            {
                this.waiting.set(false);
                this.status.notifyAll();
            }

            if (this.errorTask != null)
            {
                this.reportError(this.errorTask);
            }

            if (this.nextFutureTask != null)
            {
                this.nextFutureTask.failed(this.lastError);
            }

            if (this.finishFutureTask != null)
            {
                this.finishFutureTask.play(this.finishParameter);
            }

            this.nextTask = null;
            this.errorTask = null;
            this.finishTask = null;
            this.promise = null;
        }
    }

    /**
     * Called if result is successfully computed
     *
     * @param result Computed result
     */
    @SuppressWarnings("unchecked")
    void succeed(final RESULT result)
    {
        synchronized (this.status)
        {
            this.result = result;
            this.status.set(FutureTask.SUCCEED);

            if (this.waiting.get())
            {
                this.waiting.set(false);
                this.status.notifyAll();
            }

            if (this.nextTask != null)
            {
                this.doNextAction(this.nextTask);
            }

            if (this.finishFutureTask != null)
            {
                this.finishFutureTask.play(this.finishParameter);
            }

            this.nextTask = null;
            this.finishTask = null;
            this.errorTask = null;
            this.promise = null;
        }
    }

    /**
     * Cancel the action
     */
    public void cancel()
    {
        synchronized (this.status)
        {
            if (this.promise != null)
            {
                this.promise.cancel();
            }
            else
            {
                this.doCancel();
            }
        }
    }

    /**
     * Filter the result. If result not accept by the filter, then the next action is not done
     *
     * @param filter Filter to use
     * @return this
     */
    public
    @NotNull
    FutureTask<PARAMETER, RESULT> filter(
            @NotNull
            final BooleanTask<RESULT> filter)
    {
        if (filter == null)
        {
            throw new NullPointerException("filter MUST NOT be null !");
        }

        synchronized (this.status)
        {
            this.filter = filter;
        }

        return this;
    }

    /**
     * Indicates if task is canceled<br>
     * It is just informative, for example use it if {@link #get()} return {@code null}
     *
     * @return {@code true} if task is canceled
     */
    public boolean isCanceled()
    {
        synchronized (this.status)
        {
            if (this.promise != null)
            {
                return !this.promise.isActive();
            }

            return this.status.get() == FutureTask.CANCELED;
        }
    }

    /**
     * Indicates if embed action still computing<br>
     * It is just informative, it is strongly discourage to do a loop for wait the end with this value.<br>
     * If need wait the end use {@link #get()} or {@link #join()}
     *
     * @return If embed action still computing
     */
    public boolean isComputing()
    {
        synchronized (this.status)
        {
            if (this.promise != null)
            {
                return this.promise.isActive();
            }

            return this.status.get() == FutureTask.COMPUTING;
        }
    }

    /**
     * Indicates if embed action have failed on on error<br>
     * It is just informative, for example use it if {@link #get()} return {@code null}<br>
     * To get the error, use {@link #onError(ErrorTask)}}
     *
     * @return If embed action have failed on on error
     */
    public boolean isOnError()
    {
        synchronized (this.status)
        {
            if (this.promise != null)
            {
                if (this.promise.isActive())
                {
                    return false;
                }
            }

            return (this.status.get() == FutureTask.ERROR) || (this.lastError != null);
        }
    }

    /**
     * Wait task finish, cancel or error
     */
    public void join()
    {
        this.get();
    }

    /**
     * Wait action finished and obtain the result if no error happen.<br>
     * On error or cancel {@code null} is return
     *
     * @return Action result OR {@code null} if error or cancel happen
     */
    public
    @Nullable
    RESULT get()
    {
        if (this == FutureTask.FAKE_FUTURE_TASK)
        {
            return null;
        }

        synchronized (this.status)
        {
            while (this.status.get() == FutureTask.COMPUTING)
            {
                this.waiting.set(true);

                try
                {
                    this.status.wait();
                }
                catch (final Exception ignored)
                {
                }
            }
        }

        return this.result;
    }

    /**
     * Link an error task to action.<br>
     * On error this action is called with action parameter
     *
     * @param errorTask Error listener task
     * @return this
     */
    public
    @NotNull
    FutureTask<PARAMETER, RESULT> onError(
            @NotNull
            final ErrorTask errorTask)
    {
        if (errorTask == null)
        {
            throw new NullPointerException("errorTask MUST NOT be null !");
        }

        synchronized (this.status)
        {
            switch (this.status.get())
            {
                case COMPUTING:
                    this.errorTask = errorTask;
                    break;
                case ERROR:
                    this.reportError(errorTask);
                    break;
                default:
                    break;
            }
        }

        return this;
    }

    /**
     * Report error
     *
     * @param errorTask Error task
     */
    private void reportError(final ErrorTask errorTask)
    {
        ThreadManager.THREAD_MANAGER.doAction(errorTask, this.lastError);
    }

    /**
     * Declare task do after this one.<br>
     * The task will be played if this action succeed or failed.
     *
     * @param finishTask Task to play when this task finish
     * @param parameter  Parameter give at action when it launch
     * @param <PARAM>    Finish task parameter type
     * @param <RES>      Finish task result type
     * @return Future on given task to follow its lifz cycle
     */
    @SuppressWarnings("unchecked")
    public <PARAM, RES> FutureTask<PARAM, RES> onFinish(
            @NotNull
                    ActionTask<PARAM, RES> finishTask, PARAM parameter)
    {
        if (finishTask == null)
        {
            throw new NullPointerException("finishTask MUST NOT be null !");
        }

        this.finishTask = finishTask;
        this.finishParameter = parameter;
        return (FutureTask<PARAM, RES>) this.obtainFinishTask(this.finishTask);
    }

    /**
     * Link an action to do when this action finish with success.<br>
     * The given action will receive this action result as parameter
     *
     * @param <SECOND_RESULT> Action given result type
     * @param nextTask        Action to call next
     * @return Future link to given action
     */
    @SuppressWarnings("unchecked")
    public
    @NotNull
    <SECOND_RESULT> FutureTask<RESULT, SECOND_RESULT> then(
            @NotNull
            final ActionTask<RESULT, SECOND_RESULT> nextTask)
    {
        if (nextTask == null)
        {
            throw new NullPointerException("nextTask MUST NOT be null !");
        }

        synchronized (this.status)
        {
            switch (this.status.get())
            {
                case COMPUTING:
                    this.nextTask = nextTask;
                    return (FutureTask<RESULT, SECOND_RESULT>) this.obtainNextFutureTask(nextTask);
                case SUCCEED:
                    return (FutureTask<RESULT, SECOND_RESULT>) this.doNextAction(nextTask);
                default:
                    return (FutureTask<RESULT, SECOND_RESULT>) FutureTask.FAKE_FUTURE_TASK;
            }
        }
    }

    /**
     * Task in separate thread to play the action
     *
     * @author JHelp <br>
     */
    class Promise
            extends ThreadedTask<PARAMETER, RESULT, Void>
    {
        /**
         * Action play
         */
        private ActionTask<PARAMETER, RESULT> action;
        /**
         * Indicates that action is active
         */
        private boolean                       active;
        /**
         * Last error happened
         */
        private Throwable                     error;
        /**
         * Action parameter
         */
        private PARAMETER                     parameter;

        /**
         * Create a new instance of Promise
         *
         * @param action Action to manage
         */
        Promise(final ActionTask<PARAMETER, RESULT> action)
        {
            this.active = true;
            this.error = null;
            this.action = action;
        }

        /**
         * Called when task canceled <br>
         * <br>
         * <b>Parent documentation:</b><br>
         * {@inheritDoc}
         *
         * @see jhelp.util.thread.ThreadedTask#canceled()
         */
        @Override
        protected void canceled()
        {
            this.active = false;
            FutureTask.this.doCancel();
        }

        /**
         * Do the task : launch the action and collect result or error <br>
         * <br>
         * <b>Parent documentation:</b><br>
         * {@inheritDoc}
         *
         * @param parameter Action parameter
         * @return Action result
         * @see jhelp.util.thread.ThreadedTask#doThreadAction(java.lang.Object)
         */
        @Override
        protected RESULT doThreadAction(final PARAMETER parameter)
        {
            try
            {
                this.parameter = parameter;
                return this.action.doAction(parameter);
            }
            catch (final Exception | Error exception)
            {
                this.error = exception;
            }

            return null;
        }

        /**
         * Called when result is known <br>
         * <br>
         * <b>Parent documentation:</b><br>
         * {@inheritDoc}
         *
         * @param result Computed result
         * @see jhelp.util.thread.ThreadedTask#setResult(java.lang.Object)
         */
        @Override
        protected void setResult(final RESULT result)
        {
            if (!this.active)
            {
                FutureTask.this.doCancel();
                return;
            }

            if (this.error != null)
            {
                FutureTask.this.failed(this.error);
                return;
            }

            FutureTask.this.succeed(result);

        }

        /**
         * Actual parameter value
         *
         * @return Actual parameter value
         */
        public PARAMETER getParameter()
        {
            return this.parameter;
        }

        /**
         * Indicated if task is active
         *
         * @return {@code true} if task is active
         */
        public boolean isActive()
        {
            return this.active;
        }

        /**
         * Change action
         *
         * @param action New action value
         */
        @SuppressWarnings("unchecked")
        public void setAction(final ActionTask<?, ?> action)
        {
            this.action = (ActionTask<PARAMETER, RESULT>) action;
        }
    }
}