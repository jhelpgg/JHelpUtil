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
package jhelp.util.gui;

import java.awt.Rectangle;

/**
 * Limit bounds
 *
 * @author JHelp
 */
public class Bounds
{
    /** Empty bounds */
    public static final Bounds EMPTY = new Bounds(Integer.MAX_VALUE, Integer.MIN_VALUE, Integer.MAX_VALUE,
                                                  Integer.MIN_VALUE);
    /** Full bounds */
    public static final Bounds FULL  = new Bounds(Integer.MIN_VALUE, Integer.MAX_VALUE, Integer.MIN_VALUE,
                                                  Integer.MAX_VALUE);
    /** Maximum value of X */
    private final int xMax;
    /** Minimum value of X */
    private final int xMin;
    /** Maximum value of Y */
    private final int yMax;
    /** Minimum value of Y */
    private final int yMin;

    /**
     * Create a new instance of Bounds
     *
     * @param rectangle
     *           Rectangle base
     */
    public Bounds(final Rectangle rectangle)
    {
        this(rectangle.x, (rectangle.x + rectangle.width) - 1, rectangle.y, (rectangle.y + rectangle.height) - 1);
    }

    /**
     * Create a new instance of Bounds
     *
     * @param xMin
     *           Minimum value of X
     * @param xMax
     *           Maximum value of X
     * @param yMin
     *           Minimum value of Y
     * @param yMax
     *           Maximum value of Y
     */
    public Bounds(final int xMin, final int xMax, final int yMin, final int yMax)
    {
        this.xMin = xMin;
        this.xMax = xMax;
        this.yMin = yMin;
        this.yMax = yMax;
    }

    /**
     * Maximum value of X
     *
     * @return Maximum value of X
     */
    public int getxMax()
    {
        return this.xMax;
    }

    /**
     * Minimum value of X
     *
     * @return Minimum value of X
     */
    public int getxMin()
    {
        return this.xMin;
    }

    /**
     * Maximum value of Y
     *
     * @return Maximum value of Y
     */
    public int getyMax()
    {
        return this.yMax;
    }

    /**
     * Minimum value of Y
     *
     * @return Minimum value of Y
     */
    public int getyMin()
    {
        return this.yMin;
    }

    /**
     * Indicates if a point is inside the bounds
     *
     * @param x
     *           Point X
     * @param y
     *           Point Y
     * @return {@code true} if the point is inside
     */
    public boolean inside(final int x, final int y)
    {
        return (x >= this.xMin) && (x <= this.xMax) && (y >= this.yMin) && (y <= this.yMax);
    }

    /**
     * Intersects with an other bounds
     *
     * @param bounds
     *           Bounds to intersect with
     * @return Intersection
     */
    public Bounds intersect(final Bounds bounds)
    {
        final int minX = Math.max(this.xMin, bounds.xMin);
        final int maxX = Math.min(this.xMax, bounds.xMax);

        if (minX > maxX)
        {
            return Bounds.EMPTY;
        }

        final int minY = Math.max(this.yMin, bounds.yMin);
        final int maxY = Math.min(this.yMax, bounds.yMax);

        if (minY > maxY)
        {
            return Bounds.EMPTY;
        }

        if ((minX == this.xMin) && (maxX == this.xMax) && (minY == this.yMin) && (maxY == this.yMax))
        {
            return this;
        }

        if ((minX == bounds.xMin) && (maxX == bounds.xMax) && (minY == bounds.yMin) && (maxY == bounds.yMax))
        {
            return bounds;
        }

        return new Bounds(minX, maxX, minY, maxY);
    }

    /**
     * Intersects with a clip
     *
     * @param clip
     *           Clip to intersect with
     * @return Intersection
     */
    public Bounds intersect(final Clip clip)
    {
        final int minX = Math.max(this.xMin, clip.xMin);
        final int maxX = Math.min(this.xMax, clip.xMax);

        if (minX > maxX)
        {
            return Bounds.EMPTY;
        }

        final int minY = Math.max(this.yMin, clip.yMin);
        final int maxY = Math.min(this.yMax, clip.yMax);

        if (minY > maxY)
        {
            return Bounds.EMPTY;
        }

        if ((minX == this.xMin) && (maxX == this.xMax) && (minY == this.yMin) && (maxY == this.yMax))
        {
            return this;
        }

        return new Bounds(minX, maxX, minY, maxY);
    }

    /**
     * Indicates if bounds are empty (No point can be inside)
     *
     * @return {@code true} if bounds are empty
     */
    public boolean isEmpty()
    {
        return (this.xMin > this.xMax) || (this.yMin > this.yMax);
    }

    /**
     * String representation <br>
     * <br>
     * <b>Parent documentation:</b><br>
     * {@inheritDoc}
     *
     * @return String representation
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString()
    {
        return this.xMin + "<=>" + this.xMax + " x " + this.yMin + "<=>" + this.yMax;
    }
}