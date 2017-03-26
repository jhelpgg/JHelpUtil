package jhelp.util.io.json;

import org.junit.Assert;
import org.junit.Test;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Test of {@link JSONList}.
 */
public class TestJSONList
{
    /**
     * Simple tests.
     */
    @Test
    public void testSimple()
    {
        JSONList<String> list = new JSONList<>();
        Assert.assertTrue(list.isEmpty());
        Assert.assertEquals(0, list.size());
        Assert.assertEquals("[]", list.toString());

        list.add("car");
        Assert.assertFalse(list.isEmpty());
        Assert.assertEquals(1, list.size());
        Assert.assertEquals("car", list.get(0));
        Assert.assertEquals("[car]", list.toString());

        try
        {
            list.get(5);
            Assert.fail("5 element not exits !");
        }
        catch (IllegalArgumentException e)
        {
            //That what we want
        }

        list.add("plane");
        Assert.assertFalse(list.isEmpty());
        Assert.assertEquals(2, list.size());
        Assert.assertEquals("car", list.get(0));
        Assert.assertEquals("plane", list.get(1));
        Assert.assertEquals("[car, plane]", list.toString());

        list.add("middle", 1);
        Assert.assertFalse(list.isEmpty());
        Assert.assertEquals(3, list.size());
        Assert.assertEquals("car", list.get(0));
        Assert.assertEquals("middle", list.get(1));
        Assert.assertEquals("plane", list.get(2));
        Assert.assertEquals("[car, middle, plane]", list.toString());

        list.add("start", 0);
        Assert.assertFalse(list.isEmpty());
        Assert.assertEquals(4, list.size());
        Assert.assertEquals("start", list.get(0));
        Assert.assertEquals("car", list.get(1));
        Assert.assertEquals("middle", list.get(2));
        Assert.assertEquals("plane", list.get(3));
        Assert.assertEquals("[start, car, middle, plane]", list.toString());

        Assert.assertEquals(2, list.indexOf("middle"));

        Iterator<String> iterator = list.iterator();
        Assert.assertTrue(iterator.hasNext());
        Assert.assertEquals("start", iterator.next());
        Assert.assertTrue(iterator.hasNext());
        Assert.assertEquals("car", iterator.next());
        Assert.assertTrue(iterator.hasNext());
        Assert.assertEquals("middle", iterator.next());
        Assert.assertTrue(iterator.hasNext());
        Assert.assertEquals("plane", iterator.next());
        Assert.assertFalse(iterator.hasNext());

        try
        {
            iterator.next();
            Assert.fail("Should fail if no next element");
        }
        catch (NoSuchElementException e)
        {
            //That's what we want
        }

        Assert.assertEquals("middle", list.remove(2));
        Assert.assertFalse(list.isEmpty());
        Assert.assertEquals(3, list.size());
        Assert.assertEquals("start", list.get(0));
        Assert.assertEquals("car", list.get(1));
        Assert.assertEquals("plane", list.get(2));
        Assert.assertEquals("[start, car, plane]", list.toString());

        Assert.assertTrue(list.remove("car"));
        Assert.assertFalse(list.isEmpty());
        Assert.assertEquals(2, list.size());
        Assert.assertEquals("start", list.get(0));
        Assert.assertEquals("plane", list.get(1));
        Assert.assertEquals("[start, plane]", list.toString());

        Assert.assertFalse(list.remove("car"));
        Assert.assertFalse(list.isEmpty());
        Assert.assertEquals(2, list.size());
        Assert.assertEquals("start", list.get(0));
        Assert.assertEquals("plane", list.get(1));
        Assert.assertEquals("[start, plane]", list.toString());

        Assert.assertEquals("start", list.remove(0));
        Assert.assertFalse(list.isEmpty());
        Assert.assertEquals(1, list.size());
        Assert.assertEquals("plane", list.get(0));
        Assert.assertEquals("[plane]", list.toString());

        Assert.assertEquals("plane", list.remove(0));
        Assert.assertTrue(list.isEmpty());
        Assert.assertEquals(0, list.size());
        Assert.assertEquals("[]", list.toString());
    }
}