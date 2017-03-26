package jhelp.util.io;

import org.junit.Assert;
import org.junit.Test;

/**
 * Hash map binarizable tests
 *
 * @author JHelp <br>
 */
public class HashmapedBinarizableTest
{
    /**
     * Height key
     */
    public static final String HEIGHT     = "height";
    /**
     * Visibility key
     */
    public static final String VISIBILITY = "visibility";
    /**
     * Width key
     */
    public static final String WIDTH      = "width";
    /**
     * X key
     */
    public static final String X          = "x";
    /**
     * Y key
     */
    public static final String Y          = "y";

    /**
     * Change version test
     */
    @Test
    public void test()
    {
        final ByteArray byteArray = new ByteArray();

        final ImageVersion1 imageVersion1 = new ImageVersion1();
        imageVersion1.setSize(123, 321);
        imageVersion1.setVisible(false);

        byteArray.clear();
        imageVersion1.serializeBinary(byteArray);

        final ImageVersion2 imageVersion2 = new ImageVersion2();
        imageVersion2.parseBinary(byteArray);

        Assert.assertEquals(123, imageVersion2.getWidth());
        Assert.assertEquals(321, imageVersion2.getHeight());
        Assert.assertEquals(0, imageVersion2.getAlpha());
        Assert.assertEquals(0, imageVersion2.getX());
        Assert.assertEquals(0, imageVersion2.getY());

        //

        imageVersion2.setPosition(42, 24);
        imageVersion2.setSize(987, 789);
        imageVersion2.setAlpha(128);

        byteArray.clear();
        imageVersion2.serializeBinary(byteArray);

        imageVersion1.parseBinary(byteArray);
        Assert.assertEquals(987, imageVersion1.getWidth());
        Assert.assertEquals(789, imageVersion1.getHeight());
        Assert.assertTrue(imageVersion1.isVisible());

        //

        byteArray.clear();
        imageVersion2.serializeBinary(byteArray);

        imageVersion2.setPosition(7842, 7824);
        imageVersion2.setSize(97, 7);
        imageVersion2.setAlpha(51);

        imageVersion2.parseBinary(byteArray);

        Assert.assertEquals(987, imageVersion2.getWidth());
        Assert.assertEquals(789, imageVersion2.getHeight());
        Assert.assertEquals(128, imageVersion2.getAlpha());
        Assert.assertEquals(42, imageVersion2.getX());
        Assert.assertEquals(24, imageVersion2.getY());
    }

    /**
     * Image representation version 1
     *
     * @author JHelp <br>
     */
    public static class ImageVersion1
            extends HashmapedBinarizable
    {
        /**
         * Create a new instance of ImageVersion1
         */
        public ImageVersion1()
        {
        }

        /**
         * Initialize image representation <br>
         * <br>
         * <b>Parent documentation:</b><br>
         * {@inheritDoc}
         *
         * @see jhelp.util.io.HashmapedBinarizable#initializeFields()
         */
        @Override
        protected void initializeFields()
        {
            this.put(HashmapedBinarizableTest.WIDTH, 0);
            this.put(HashmapedBinarizableTest.HEIGHT, 0);
            this.put(HashmapedBinarizableTest.VISIBILITY, true);
        }        /**
         * Call if have to convert a variable from old version type to current version type <br>
         * <br>
         * <b>Parent documentation:</b><br>
         * {@inheritDoc}
         *
         * @param name      Variable name
         * @param value     Variable current value
         * @param actual    Variable current type
         * @param toConvert Desired type
         * @return Converted value or {@code null} if can't be converted
         * @see jhelp.util.io.HashmapedBinarizable#convert(java.lang.String, java.lang.Object,
         * jhelp.util.io.HashmapedBinarizable.Type, jhelp.util.io.HashmapedBinarizable.Type)
         */
        @Override
        protected Object convert(final String name, final Object value, final Type actual, final Type toConvert)
        {
            return null;
        }

        /**
         * Height
         *
         * @return Height
         */
        public int getHeight()
        {
            return this.forceGet(HashmapedBinarizableTest.HEIGHT, 0);
        }

        /**
         * Width
         *
         * @return Width
         */
        public int getWidth()
        {
            return this.forceGet(HashmapedBinarizableTest.WIDTH, 0);
        }

        /**
         * Image visibility
         *
         * @return Image visibility
         */
        public boolean isVisible()
        {
            return this.forceGet(HashmapedBinarizableTest.VISIBILITY, true);
        }

        /**
         * Change visibility
         *
         * @param visible Visibility status
         */
        public void setVisible(final boolean visible)
        {
            this.forcePut(HashmapedBinarizableTest.VISIBILITY, visible);
        }

        /**
         * Change image size
         *
         * @param width  New width
         * @param height New height
         */
        public void setSize(final int width, final int height)
        {
            this.forcePut(HashmapedBinarizableTest.WIDTH, width);
            this.forcePut(HashmapedBinarizableTest.HEIGHT, height);
        }


    }

    /**
     * Image representation version 1
     *
     * @author JHelp <br>
     */
    public static class ImageVersion2
            extends HashmapedBinarizable
    {
        /**
         * Create a new instance of ImageVersion2
         */
        public ImageVersion2()
        {
        }

        /**
         * Image alpha
         *
         * @return Image alpha
         */
        public int getAlpha()
        {
            return this.forceGet(HashmapedBinarizableTest.VISIBILITY, 255);
        }        /**
         * Call if have to convert a variable from old version type to current version type <br>
         * <br>
         * <b>Parent documentation:</b><br>
         * {@inheritDoc}
         *
         * @param name      Variable name
         * @param value     Variable current value
         * @param actual    Variable current type
         * @param toConvert Desired type
         * @return Converted value or {@code null} if can't be converted
         * @see jhelp.util.io.HashmapedBinarizable#convert(java.lang.String, java.lang.Object,
         * jhelp.util.io.HashmapedBinarizable.Type, jhelp.util.io.HashmapedBinarizable.Type)
         */
        @Override
        protected Object convert(final String name, final Object value, final Type actual, final Type toConvert)
        {
            if ((HashmapedBinarizableTest.VISIBILITY.equals(name)) && (actual == Type.BOOLEAN) && (toConvert == Type.INT))
            {
                if (((Boolean) value).booleanValue())
                {
                    return 255;
                }

                return 0;
            }

            return null;
        }

        /**
         * Change alpha
         *
         * @param alpha New alpha
         */
        public void setAlpha(final int alpha)
        {
            this.forcePut(HashmapedBinarizableTest.VISIBILITY, alpha);
        }        /**
         * Initialize image representation <br>
         * <br>
         * <b>Parent documentation:</b><br>
         * {@inheritDoc}
         *
         * @see jhelp.util.io.HashmapedBinarizable#initializeFields()
         */
        @Override
        protected void initializeFields()
        {
            this.put(HashmapedBinarizableTest.X, 0);
            this.put(HashmapedBinarizableTest.Y, 0);
            this.put(HashmapedBinarizableTest.WIDTH, 0);
            this.put(HashmapedBinarizableTest.HEIGHT, 0);
            this.put(HashmapedBinarizableTest.VISIBILITY, 255);
        }

        /**
         * Height
         *
         * @return Height
         */
        public int getHeight()
        {
            return this.forceGet(HashmapedBinarizableTest.HEIGHT, 0);
        }

        /**
         * Width
         *
         * @return Width
         */
        public int getWidth()
        {
            return this.forceGet(HashmapedBinarizableTest.WIDTH, 0);
        }

        /**
         * X position
         *
         * @return X
         */
        public int getX()
        {
            return this.forceGet(HashmapedBinarizableTest.X, 0);
        }

        /**
         * Y position
         *
         * @return Y
         */
        public int getY()
        {
            return this.forceGet(HashmapedBinarizableTest.Y, 0);
        }

        /**
         * Change position
         *
         * @param x New X
         * @param y New Y
         */
        public void setPosition(final int x, final int y)
        {
            this.forcePut(HashmapedBinarizableTest.X, x);
            this.forcePut(HashmapedBinarizableTest.Y, y);
        }

        /**
         * Change image size
         *
         * @param width  New width
         * @param height New height
         */
        public void setSize(final int width, final int height)
        {
            this.forcePut(HashmapedBinarizableTest.WIDTH, width);
            this.forcePut(HashmapedBinarizableTest.HEIGHT, height);
        }




    }
}