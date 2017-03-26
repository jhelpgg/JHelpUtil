package jhelp.util.post;

/**
 * Message description
 *
 * @author JHelp
 */
public class Message
{
    /**
     * Message sender, use for reply
     */
    public final String from;
    /**
     * Message ID
     */
    public final int    messageId;
    /**
     * Message itself
     */
    public final Object message;

    /**
     * Create a new instance of Message
     *
     * @param messageId Message ID
     * @param from      Sender
     * @param message   Message itself
     */
    Message(final int messageId, final String from, final Object message)
    {
        this.messageId = messageId;
        this.from = from;
        this.message = message;
    }
}