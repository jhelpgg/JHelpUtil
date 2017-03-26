package jhelp.util.io.json;

import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;

import jhelp.util.debug.Debug;
import jhelp.util.io.ByteArray;

/**
 * Test of {@link JSONWriter}.
 */
public class TestJsonWriter
{
    /**
     * Simple test.
     */
    @Test
    public void testSimple()
    {
        Simple simple = new Simple();
        simple.setBool(true);
        simple.setInteger(73);
        simple.setString("This is simple");
        ByteArray byteArray = new ByteArray();

        try
        {
            JSONWriter.writeJSON(simple, byteArray.getOutputStream(), true);
            String string = new String(byteArray.toArray(), "UTF-8");
            Assert.assertEquals("{\"integer\":73,\"bool\":true,\"string\":\"This is simple\"}",
                                string);
            ObjectJSON objectJSON = ObjectJSON.parse(byteArray.getInputStream());
            Assert.assertEquals(simple.getInteger(), objectJSON.getInt("integer", 0));
            Assert.assertEquals(simple.isBool(), objectJSON.getBoolean("bool", false));
            Assert.assertEquals(simple.getString(), objectJSON.getString("string", ""));

            byteArray.clear();
            JSONWriter.writeJSON(simple, byteArray.getOutputStream(), false);
            string = new String(byteArray.toArray(), "UTF-8");
            Assert.assertEquals(
                    "{\n   \"integer\":73,\n   \"bool\":true,\n   \"string\":\"This is " +
                            "simple\"\n}\n",
                    string);
            objectJSON = ObjectJSON.parse(byteArray.getInputStream());
            Assert.assertEquals(simple.getInteger(), objectJSON.getInt("integer", 0));
            Assert.assertEquals(simple.isBool(), objectJSON.getBoolean("bool", false));
            Assert.assertEquals(simple.getString(), objectJSON.getString("string", ""));
        }
        catch (IOException | JSONException e)
        {
            Debug.printException(e);
            Assert.fail("Exception happen");
        }
    }

    @Test
    public void testAllSimpleTypes()
    {
        try
        {
            AllSimpleTypes allSimpleTypes = new AllSimpleTypes();
            allSimpleTypes.bool = true;
            allSimpleTypes.character = 'A';
            allSimpleTypes.b = (byte) 25;
            allSimpleTypes.s = (short) 42;
            allSimpleTypes.i = 73;
            allSimpleTypes.l = 1234567890L;
            allSimpleTypes.f = 0.123456789f;
            allSimpleTypes.d = 0.12345678910111213141516171819;
            allSimpleTypes.string = "Something";
            ByteArray byteArray = new ByteArray();
            JSONWriter.writeJSON(allSimpleTypes, byteArray.getOutputStream(), true);
            String     string     = new String(byteArray.toArray(), "UTF-8");
            ObjectJSON objectJSON = ObjectJSON.parse(byteArray.getInputStream());
            Assert.assertEquals(allSimpleTypes.bool, objectJSON.getBoolean("bool", false));
            Assert.assertEquals(allSimpleTypes.character, objectJSON.getString("character", "€")
                                                                    .charAt(0));
            Assert.assertEquals(allSimpleTypes.b, (byte) (objectJSON.getInt("b", 0) & 0xFF));
            Assert.assertEquals(allSimpleTypes.s, (short) (objectJSON.getInt("s", 0) & 0xFFFF));
            Assert.assertEquals(allSimpleTypes.i, objectJSON.getInt("i", 0));
            Assert.assertEquals(allSimpleTypes.l, objectJSON.getLong("l", 0));
            Assert.assertEquals(allSimpleTypes.f, objectJSON.getFloat("f", 0), 1e-5f);
            Assert.assertEquals(allSimpleTypes.d, objectJSON.getDouble("d", 0), 1e-5);
            Assert.assertEquals(allSimpleTypes.string, objectJSON.getString("string", ""));
        }
        catch (IOException | JSONException e)
        {
            Debug.printException(e);
            Assert.fail("Exception happen");
        }
    }

    @Test
    public void testCombined()
    {
        try
        {
            Combined combined = new Combined();

            combined.simple = new Simple();
            combined.simple.setBool(true);
            combined.simple.setInteger(73);
            combined.simple.setString("This is simple");

            combined.allSimpleTypes = new AllSimpleTypes();
            combined.allSimpleTypes.bool = true;
            combined.allSimpleTypes.character = 'A';
            combined.allSimpleTypes.b = (byte) 25;
            combined.allSimpleTypes.s = (short) 42;
            combined.allSimpleTypes.i = 73;
            combined.allSimpleTypes.l = 1234567890L;
            combined.allSimpleTypes.f = 0.123456789f;
            combined.allSimpleTypes.d = 0.12345678910111213141516171819;
            combined.allSimpleTypes.string = "Something";

            ByteArray byteArray = new ByteArray();
            JSONWriter.writeJSON(combined, byteArray.getOutputStream(), true);
            ObjectJSON objectJSON = ObjectJSON.parse(byteArray.getInputStream());

            ObjectJSON simple = objectJSON.getObject("simple");
            Assert.assertNotNull(simple);
            Assert.assertEquals(combined.simple.getInteger(), simple.getInt("integer", 0));
            Assert.assertEquals(combined.simple.isBool(), simple.getBoolean("bool", false));
            Assert.assertEquals(combined.simple.getString(), simple.getString("string", ""));

            ObjectJSON allSimpleTypes = objectJSON.getObject("allSimpleTypes");
            Assert.assertNotNull(allSimpleTypes);
            Assert.assertEquals(combined.allSimpleTypes.bool,
                                allSimpleTypes.getBoolean("bool", false));
            Assert.assertEquals(combined.allSimpleTypes.character,
                                allSimpleTypes.getString("character", "€")
                                              .charAt(0));
            Assert.assertEquals(combined.allSimpleTypes.b,
                                (byte) (allSimpleTypes.getInt("b", 0) & 0xFF));
            Assert.assertEquals(combined.allSimpleTypes.s,
                                (short) (allSimpleTypes.getInt("s", 0) & 0xFFFF));
            Assert.assertEquals(combined.allSimpleTypes.i, allSimpleTypes.getInt("i", 0));
            Assert.assertEquals(combined.allSimpleTypes.l, allSimpleTypes.getLong("l", 0));
            Assert.assertEquals(combined.allSimpleTypes.f, allSimpleTypes.getFloat("f", 0), 1e-5f);
            Assert.assertEquals(combined.allSimpleTypes.d, allSimpleTypes.getDouble("d", 0), 1e-5);
            Assert.assertEquals(combined.allSimpleTypes.string,
                                allSimpleTypes.getString("string", ""));
        }
        catch (IOException | JSONException e)
        {
            Debug.printException(e);
            Assert.fail("Exception happen");
        }
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testList()
    {
        try
        {
            JSONList<String> list = new JSONList<>();
            list.add("pilot");
            list.add("pear");
            list.add("meting");
            System.out.println(list);
            System.out.println("******");
            ByteArray byteArray = new ByteArray();
            JSONWriter.writeJSON(list, byteArray.getOutputStream(), false);
            ObjectJSON objectJSON = ObjectJSON.parse(byteArray.getInputStream());
            System.out.println(new String(byteArray.toArray(), "UTF-8"));
            System.out.println("----");
            objectJSON.serialize(System.out, false);
            byteArray.readFromStart();
            list = JSONReader.readJSON(JSONList.class, byteArray.getInputStream(), true);
            System.out.println("******");
            System.out.println(list);
        }
        catch (IOException | JSONException e)
        {
            Debug.printException(e);
            Assert.fail("Exception happen");
        }
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testMap()
    {
        try
        {
            JSONMap<String, String> list = new JSONMap<>();
            list.put("pilot", "fsodfispfdoi");
            list.put("pear", "kkkkkkkkkkkkkkkkkkkkkkk");
            list.put("meting", "pollllll");
            System.out.println(list);
            System.out.println("******");
            ByteArray byteArray = new ByteArray();
            JSONWriter.writeJSON(list, byteArray.getOutputStream(), false);
            ObjectJSON objectJSON = ObjectJSON.parse(byteArray.getInputStream());
            System.out.println(new String(byteArray.toArray(), "UTF-8"));
            System.out.println("----");
            objectJSON.serialize(System.out, false);
            byteArray.readFromStart();
            list = JSONReader.readJSON(JSONMap.class, byteArray.getInputStream(), true);
            System.out.println("******");
            System.out.println(list);
        }
        catch (IOException | JSONException e)
        {
            Debug.printException(e);
            Assert.fail("Exception happen");
        }
    }

    @JSONObject
    static class Simple
    {
        @JSONElement
        private int     integer;
        @JSONElement
        private boolean bool;
        @JSONElement
        private String  string;

        Simple()
        {
        }

        public Simple(int integer, boolean bool, String string)
        {
            this.integer = integer;
            this.bool = bool;
            this.string = string;
        }

        public int getInteger()
        {
            return this.integer;
        }

        public void setInteger(int integer)
        {
            this.integer = integer;
        }

        public String getString()
        {
            return this.string;
        }

        public void setString(String string)
        {
            this.string = string;
        }

        public boolean isBool()
        {
            return this.bool;
        }

        public void setBool(boolean bool)
        {
            this.bool = bool;
        }
    }

    @JSONObject
    static class AllSimpleTypes
    {
        @JSONElement
        boolean bool;
        @JSONElement
        char    character;
        @JSONElement
        byte    b;
        @JSONElement
        short   s;
        @JSONElement
        int     i;
        @JSONElement
        long    l;
        @JSONElement
        float   f;
        @JSONElement
        double  d;
        @JSONElement
        String  string;
    }

    @JSONObject
    static class Combined
    {
        @JSONElement
        Simple         simple;
        @JSONElement
        AllSimpleTypes allSimpleTypes;
    }
}