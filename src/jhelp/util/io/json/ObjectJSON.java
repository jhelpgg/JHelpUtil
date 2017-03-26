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

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.Set;

import jhelp.util.HashCode;
import jhelp.util.list.Pair;
import jhelp.util.text.UtilText;

/**
 * JSON object.<br>
 * It contains couples of key value
 *
 * @author JHelp
 */
public class ObjectJSON
{
    /**
     * Couples of key, value
     */
    private final HashMap<String, ValueJSON> values;

    /**
     * Create a new instance of ObjectJSON
     */
    public ObjectJSON()
    {
        this.values = new HashMap<String, ValueJSON>();
    }

    /**
     * Parse stream to read object
     *
     * @param inputStream Stream to parse
     * @return Object read
     * @throws IOException On reading issue
     */
    public static ObjectJSON parse(final InputStream inputStream) throws IOException
    {
        final StringBuilder stringBuilder = new StringBuilder();
        final BufferedReader bufferedReader = new BufferedReader(
                new InputStreamReader(inputStream));
        String line = bufferedReader.readLine();

        while (line != null)
        {
            stringBuilder.append(line);
            stringBuilder.append('\n');
            line = bufferedReader.readLine();
        }

        return ObjectJSON.parse(stringBuilder.toString()
                                             .trim());
    }

    /**
     * Parse a String to JSON object.<br>
     * It returns {@code null} if String not a JSON object
     *
     * @param string String to parse
     * @return JSON object parsed OR {@code null} if String not a JSON object
     */
    public static ObjectJSON parse(final String string)
    {
        final Pair<ObjectJSON, Integer> pair = ObjectJSON.parse(string, 0, string.length() - 1);

        if (pair == null)
        {
            return null;
        }

        return pair.element1;
    }

    /**
     * Parse String part to get JSON object and the next index to read the rest of the String.<br>
     * It returns {@code null} if the String part is not a JSON object
     *
     * @param string String to parse
     * @param first  First index to start parsing
     * @param last   Last index to stop parsing
     * @return JSON object parsed and next index to read String OR {@code null} if String part is
     * not a JSON object
     */
    static Pair<ObjectJSON, Integer> parse(final String string, int first, int last)
    {
        first = string.indexOf('{', first);
        last = string.lastIndexOf('}', last);

        if ((first < 0) || (last < 0) || (first >= last))
        {
            return null;
        }

        final ObjectJSON objectJSON = new ObjectJSON();

        while ((first >= 0) && (first < last))
        {
            first = string.indexOf('"', first);

            if (first < 0)
            {
                break;
            }

            int end = string.indexOf('"', first + 1);

            while ((end > 0) && (string.charAt(end - 1) == '\\'))
            {
                end = string.indexOf('"', end + 1);
            }

            if (end < 0)
            {
                break;
            }

            final String name = string.substring(first + 1, end);
            first = string.indexOf(':', end + 1);

            if (first < 0)
            {
                break;
            }

            final Pair<ValueJSON, Integer> pair = ValueJSON.parse(string, first + 1, last);

            if (pair == null)
            {
                break;
            }

            objectJSON.put(name, pair.element1);
            first = string.indexOf(',', pair.element2);
        }

        return new Pair<ObjectJSON, Integer>(objectJSON, last);
    }

    /**
     * Associate a key and a value
     *
     * @param key       Key
     * @param valueJSON Value
     */
    public void put(final String key, final ValueJSON valueJSON)
    {
        if (key == null)
        {
            throw new NullPointerException("key MUST NOT be null");
        }

        if (valueJSON == null)
        {
            throw new NullPointerException("valueJSON MUST NOT be null");
        }

        this.values.put(key, valueJSON);
    }

    /**
     * Obtain a value.<br>
     * It returns {@code null} if key not defined
     *
     * @param key Key
     * @return Value or {@code null} if key not defined
     */
    public ValueJSON get(final String key)
    {
        return this.values.get(key);
    }

    /**
     * Obtain an array.<br>
     * It returns {@code null} if key not defined
     *
     * @param key Key
     * @return Array or {@code null} if key not defined
     */
    public ArrayJSON getArray(final String key)
    {
        final ValueJSON json = this.values.get(key);

        if (json == null)
        {
            return null;
        }

        return json.getArray();
    }

    /**
     * Obtain a boolean
     *
     * @param key          Key
     * @param defaultValue Value to use if key not defined
     * @return Boolean value
     */
    public boolean getBoolean(final String key, final boolean defaultValue)
    {
        final ValueJSON json = this.values.get(key);

        if (json == null)
        {
            return defaultValue;
        }

        return json.getBoolean();
    }

    /**
     * Obtain a double
     *
     * @param key          Key
     * @param defaultValue Value to use if key not defined
     * @return Double value
     */
    public double getDouble(final String key, final double defaultValue)
    {
        final ValueJSON json = this.values.get(key);

        if (json == null)
        {
            return defaultValue;
        }

        return json.getNumber();
    }

    /**
     * Obtain a float
     *
     * @param key          Key
     * @param defaultValue Value to use if key not defined
     * @return Float value
     */
    public float getFloat(final String key, final float defaultValue)
    {
        final ValueJSON json = this.values.get(key);

        if (json == null)
        {
            return defaultValue;
        }

        return (float) json.getNumber();
    }

    /**
     * Obtain an integer
     *
     * @param key          Key
     * @param defaultValue Value to use if key not defined
     * @return Integer value
     */
    public int getInt(final String key, final int defaultValue)
    {
        final ValueJSON json = this.values.get(key);

        if (json == null)
        {
            return defaultValue;
        }

        return (int) json.getNumber();
    }

    /**
     * Set of keys
     *
     * @return Set of keys
     */
    public Set<String> getKeys()
    {
        return this.values.keySet();
    }

    public int numberOfValue()
    {
        return this.values.size();
    }

    /**
     * Obtain a long
     *
     * @param key          Key
     * @param defaultValue Value to use if key not defined
     * @return Long value
     */
    public long getLong(final String key, final long defaultValue)
    {
        final ValueJSON json = this.values.get(key);

        if (json == null)
        {
            return defaultValue;
        }

        return (long) json.getNumber();
    }

    /**
     * Obtain an object<br>
     * It return {@code null} if key not defined
     *
     * @param key Key
     * @return Object value or {@code null} if key not defined
     */
    public ObjectJSON getObject(final String key)
    {
        final ValueJSON json = this.values.get(key);

        if (json == null)
        {
            return null;
        }

        return json.getObject();
    }

    /**
     * Obtain a String
     *
     * @param key          Key
     * @param defaultValue Value to use if key not defined
     * @return String value
     */
    public String getString(final String key, final String defaultValue)
    {
        final ValueJSON json = this.values.get(key);

        if (json == null)
        {
            return defaultValue;
        }

        return json.getString();
    }

    /**
     * Object hash code <br>
     * <br>
     * <b>Parent documentation:</b><br>
     * {@inheritDoc}
     *
     * @return Object hash code
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode()
    {
        return HashCode.computeHashCode(this.values);
    }

    /**
     * Indicates if an Object is equals to this JSON object <br>
     * <br>
     * <b>Parent documentation:</b><br>
     * {@inheritDoc}
     *
     * @param object Object to compare with
     * @return {@code true} if equals
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(final Object object)
    {
        if (this == object)
        {
            return true;
        }

        if (null == object)
        {
            return false;
        }

        if (!(object instanceof ObjectJSON))
        {
            return false;
        }

        final ObjectJSON objectJSON = (ObjectJSON) object;

        if (this.values.size() != objectJSON.values.size())
        {
            return false;
        }

        for (final String key : this.values.keySet())
        {
            if (!this.values.get(key)
                            .equals(objectJSON.values.get(key)))
            {
                return false;
            }
        }

        return true;
    }

    /**
     * String representation.<br>
     * Can be used on server communication <br>
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
        final StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append('{');

        boolean first = true;

        for (final String key : this.values.keySet())
        {
            if (!first)
            {
                stringBuilder.append(",");
            }

            first = false;
            stringBuilder.append('"');
            stringBuilder.append(key);
            stringBuilder.append("\":");
            stringBuilder.append(this.values.get(key));
        }

        stringBuilder.append('}');

        return stringBuilder.toString();
    }

    /**
     * Serialize inside a stream
     *
     * @param outputStream Stream where write
     * @param compact      Indicates is compact version
     * @throws IOException On writing issue
     */
    public void serialize(final OutputStream outputStream, final boolean compact) throws IOException
    {
        this.serialize(new BufferedWriter(new OutputStreamWriter(outputStream)), compact);
    }

    /**
     * Serialize inside a stream
     *
     * @param bufferedWriter Stream where write
     * @param compact        Indicates is compact version
     * @throws IOException On writing issue
     */
    public void serialize(final BufferedWriter bufferedWriter, final boolean compact) throws
            IOException
    {
        this.serialize(bufferedWriter, compact, 0);
    }

    /**
     * Serialize inside a stream
     *
     * @param bufferedWriter Stream where write
     * @param compact        Indicates if compact mode
     * @param headerSize     Header size
     * @throws IOException On writing issue
     */
    void serialize(final BufferedWriter bufferedWriter, final boolean compact, int headerSize)
            throws IOException
    {
        String head = UtilText.repeat('\t', headerSize);

        if (!compact)
        {
            bufferedWriter.write(head);
        }

        bufferedWriter.write("{");

        if (!compact)
        {
            bufferedWriter.newLine();
            headerSize++;
            head = UtilText.repeat('\t', headerSize);
        }

        boolean   first = true;
        ValueJSON valueJSON;
        ValueType valueType;
        int       more;

        for (final String key : this.values.keySet())
        {
            if (!first)
            {
                bufferedWriter.write(",");

                if (!compact)
                {
                    bufferedWriter.newLine();
                }
            }

            first = false;

            if (!compact)
            {
                bufferedWriter.write(head);
            }

            bufferedWriter.write("\"");
            bufferedWriter.write(key);
            bufferedWriter.write("\":");
            valueJSON = this.values.get(key);
            valueType = valueJSON.getType();
            more = 0;

            if ((!compact) && ((valueType == ValueType.ARRAY) || (valueType == ValueType
                    .OBJECT)))
            {
                more = 1;
                bufferedWriter.newLine();
            }

            valueJSON.serialize(bufferedWriter, compact, headerSize + more);
        }

        if (!compact)
        {
            if (this.values.size() > 0)
            {
                bufferedWriter.newLine();
            }

            headerSize--;
            bufferedWriter.write(UtilText.repeat('\t', headerSize));
        }

        bufferedWriter.write("}");
        bufferedWriter.flush();
    }
}