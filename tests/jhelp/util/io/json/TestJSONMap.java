package jhelp.util.io.json;

import org.junit.Assert;
import org.junit.Test;

/**
 * Test of {@link JSONMap}.
 */
public class TestJSONMap
{
    /**
     * Simple test.
     */
    @Test
    public void testSimple()
    {
        JSONMap<String, String> map = new JSONMap<>();
        Assert.assertTrue(map.isEmpty());
        Assert.assertEquals(0, map.size());
        Assert.assertEquals("{}", map.toString());
        Assert.assertNull(map.get("key"));
        Assert.assertTrue(map.keys()
                             .isEmpty());
        Assert.assertNull(map.remove("key"));

        map.put("key", "value");
        Assert.assertFalse(map.isEmpty());
        Assert.assertEquals(1, map.size());
        Assert.assertEquals("{key=value}", map.toString());
        Assert.assertEquals("value", map.get("key"));
        Assert.assertEquals("[key]", map.keys()
                                        .toString());
        Assert.assertEquals("value", map.remove("key"));
        Assert.assertTrue(map.keys()
                             .isEmpty());

        for (int i = 0; i < 10; i++)
        {
            map.put((Math.random() + i * (((i & 1) << 1) - 1)) + "key", "value" + i);
        }

        map.put("key", "value");

        for (int i = 10; i < 20; i++)
        {
            map.put((Math.random() + i * (((i & 1) << 1) - 1)) + "key", "value" + i);
        }

        Assert.assertEquals(21, map.size());
        Assert.assertEquals("value", map.get("key"));
        map.put("key", "product");
        Assert.assertEquals(21, map.size());
        Assert.assertEquals("product", map.get("key"));
        Assert.assertEquals("product", map.remove("key"));
        Assert.assertEquals(20, map.size());
        Assert.assertNull(map.get("key"));
        Assert.assertNull(map.remove("key"));
    }
}