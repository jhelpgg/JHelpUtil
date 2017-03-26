/**
 * <h1>License :</h1> <br>
 * The following code is deliver as is. I take care that code compile and work, but I am not
 * responsible about any damage it may
 * cause.<br>
 * You can use, modify, the code as your need for any usage. But you can't do any action that
 * avoid me or other person use,
 * modify this code. The code is free for usage and modification, you can't change that fact.<br>
 * <br>
 *
 * @author JHelp
 */
package jhelp.util.io.json;

import jhelp.util.text.UtilText;

/**
 * Exception that may happen in {@link JSONWriter} or {@link JSONReader}
 */
public class JSONException extends Exception
{
    /**
     * Create the exception
     *
     * @param message Message
     */
    public JSONException(Object... message)
    {
        super(UtilText.concatenate(message));
    }

    /**
     * Create the exception
     *
     * @param cause   Exception that trigger this exception
     * @param message Message
     */
    public JSONException(Throwable cause, Object... message)
    {
        super(UtilText.concatenate(message), cause);
    }
}
