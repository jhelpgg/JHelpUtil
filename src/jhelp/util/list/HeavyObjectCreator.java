package jhelp.util.list;

/**
 * Creator off {@link HeavyObject}, to be able to create the heavy object only if necessary
 *
 * @param <HEAVY> Heavy object type
 * @author JHelp
 */
public interface HeavyObjectCreator<HEAVY extends HeavyObject>
{
    /**
     * Create the object
     *
     * @return Created object
     */
    public HEAVY createHeavyObject();

    /**
     * Weight of the future object, it is recommends to know it without created the object
     *
     * @return Weight of the future object
     */
    public long getFutureWeight();
}